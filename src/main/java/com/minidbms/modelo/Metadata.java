package com.minidbms.modelo;

import com.minidbms.excepciones.ArchivoInvalidoException;
import com.minidbms.excepciones.CampoDuplicadoException;
import com.minidbms.excepciones.CampoInexistenteException;
import com.minidbms.utilidades.ValidadorTexto;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Metadata {

    private String nombreArchivo;
    private int versionFormato;

    private ArrayList<Campo> campos;
    private int siguienteCampoId;
    private int llavePrimariaId;

    private long inicioAvailList;

    // Contadores
    private int registrosActivos;
    private int registrosEliminados;
    private int bytesLibres;

    public Metadata(String nombreArchivo, int siguienteCampoId) {
        if (nombreArchivo == null) {
            throw new IllegalArgumentException("El nombre del archivo es nulo");
        } else if (nombreArchivo.trim().isEmpty() || nombreArchivo.trim().isBlank()) {
            throw new IllegalArgumentException("El nombre del archivo está vacío");
        }
        ValidadorTexto.validarCaracteresProhibidos(nombreArchivo);
        if (siguienteCampoId <= 0) {
            throw new IllegalArgumentException("El id del siguiente campo debe ser mayor que cero");
        }
        this.versionFormato = VERSION_FORMATO;
        this.nombreArchivo = nombreArchivo.trim();

        this.campos = new ArrayList<>();
        this.siguienteCampoId = siguienteCampoId;

        this.llavePrimariaId = -1;

        this.registrosActivos = 0;
        this.registrosEliminados = 0;
        this.bytesLibres = 0;
    }

    public Campo agregarCampo(
            String nombre,
            TipoCampo tipo,
            int longitudMaxima,
            boolean obligatorio,
            String valorPredeterminado) {
        if (!esNombreUnico(nombre)) {
            throw new CampoDuplicadoException("nombre", nombre);
        }

        Campo campoNuevo =
                tipo == TipoCampo.TEXTO
                        ? new Campo(
                                siguienteCampoId,
                                nombre,
                                tipo,
                                longitudMaxima,
                                obligatorio,
                                valorPredeterminado)
                        : new Campo(
                                siguienteCampoId, nombre, tipo, obligatorio, valorPredeterminado);
        siguienteCampoId++;
        campos.add(campoNuevo);
        return campoNuevo;
    }

    public void agregarCampo(Campo campo) {
        if (campo == null) {
            throw new CampoInexistenteException("El campo no debe ser nulo.");
        }
        if (buscarCampoPorId(campo.obtenerId()) != null) {
            throw new CampoDuplicadoException("ID", String.valueOf(campo.obtenerId()));
        }
        if (!esNombreUnico(campo.obtenerNombre())) {
            throw new CampoDuplicadoException("nombre", campo.obtenerNombre());
        }
        campos.add(campo);
        siguienteCampoId =
                siguienteCampoId < campo.obtenerId() + 1 ? campo.obtenerId() + 1 : siguienteCampoId;
    }

    public Campo buscarCampoPorId(int id) {
        return campos.stream().filter(campo -> campo.obtenerId() == id).findFirst().orElse(null);
    }

    public Campo buscarCampoPorNombre(String nombre) {
        if (nombre == null || nombre.isBlank()) {
            return null;
        }

        String nombreTrimmed = nombre.trim();
        return campos.stream()
                .filter(campo -> campo.obtenerNombre().equalsIgnoreCase(nombreTrimmed))
                .findFirst()
                .orElse(null);
    }

    public void renombrarCampo(int id, String nuevoNombre) {
        if (nuevoNombre == null || nuevoNombre.isBlank()) {
            throw new IllegalArgumentException("El nuevo nombre esta vacío.");
        }
        Campo campo = buscarCampoPorId(id);
        if (campo == null) {
            throw new CampoInexistenteException(id, "No se puede renombrar.");
        }

        nuevoNombre = nuevoNombre.trim();
        if (!esNombreUnico(nuevoNombre)) {
            throw new CampoDuplicadoException("nombre", nuevoNombre);
        }
        campo.renombrar(nuevoNombre.trim());
    }

    public void reducirLongitudCampo(int id, int longitudMaxima) {
        Campo campo = buscarCampoPorId(id);
        if (campo == null) {
            throw new CampoInexistenteException(id, "No se puede reducir la longitud.");
        }
        campo.reducirLongitudMaxima(longitudMaxima);
    }

    public void cambiarTipoCampo(int id, TipoCampo nuevoTipo, int nuevaLongitud) {
        Campo campo = buscarCampoPorId(id);
        if (campo == null) {
            throw new CampoInexistenteException(id, "No se puede cambiar el tipo de campo.");
        }
        campo.cambiarTipo(nuevoTipo, nuevaLongitud);
    }

    public void borrarCampo(int id) {
        Campo campo = buscarCampoPorId(id);
        if (campo == null) {
            throw new CampoInexistenteException(id, "No se puede borrar");
        }
        if (esLlavePrimaria(id)) {
            throw new IllegalStateException(
                    "El campo no se puede eliminar porque es llave primaria.");
        }
        campos.remove(campo);
    }

    public void establecerLlavePrimaria(int id) {
        Campo campo = buscarCampoPorId(id);
        if (campo == null) {
            throw new CampoInexistenteException(id, "No se puede establecer como llave primaria.");
        }
        if (!campo.esObligatorio()) {
            throw new IllegalStateException(
                    "El campo debe ser obligatorio para establecerse como llave primaria");
        }
        if (llavePrimariaId != -1 && llavePrimariaId != id) {
            throw new IllegalStateException("Ya existe una llave primaria definida");
        }
        llavePrimariaId = id;
    }

    public void establecerFormatoArchivo(int nuevaVersionFormato) {
        if (nuevaVersionFormato <= 0) {
            throw new IllegalArgumentException(
                    "La versión del formato del archivo debe ser mayor que cero");
        }
        this.versionFormato = nuevaVersionFormato;
    }

    private boolean esLlavePrimaria(int id) {
        return llavePrimariaId != -1 && llavePrimariaId == id;
    }

    private boolean esNombreUnico(String nombre) {
        return buscarCampoPorNombre(nombre) == null;
    }

    public String obtenerNombreArchivo() {
        return nombreArchivo;
    }

    public List<Campo> obtenerCampos() {
        return List.copyOf(campos);
    }

    public long obtenerInicioAvailList() {
        return inicioAvailList;
    }

    public int obtenerVersionFormato() {
        return versionFormato;
    }

    public int obtenerLlavePrimariaId() {
        return llavePrimariaId;
    }

    public int obtenerRegistrosActivos() {
        return registrosActivos;
    }

    public int obtenerRegistrosEliminados() {
        return registrosEliminados;
    }

    public int obtenerBytesLibres() {
        return bytesLibres;
    }

    public static Metadata leer(BufferedReader br) throws ArchivoInvalidoException {
        if (br == null) {
            throw new IllegalArgumentException("El buffer de lectura es nulo");
        }
        String linea, seccion = "";

        String nombreArchivo = "";
        int versionFormato = VERSION_FORMATO;
        int siguienteCampoId = 1;
        int llavePrimariaId = -1;
        Metadata metadata;
        List<Campo> campos = new ArrayList<>();

        boolean esPrimeraLinea = true;
        try {
            while ((linea = br.readLine()) != null) {
                linea = linea.trim();

                if (linea.isEmpty()) {
                    continue;
                }

                if (esPrimeraLinea && !linea.equals(FIRMA_FORMATO)) {
                    throw new ArchivoInvalidoException(
                            "La firma del archivo no coincide: " + linea);
                }
                esPrimeraLinea = false;

                String lineaUpper = linea.toUpperCase().replace("[", "").replace("]", "");
                if (lineaUpper.equals("ARCHIVO")
                        || lineaUpper.equals("LLAVE_PRIMARIA")
                        || lineaUpper.equals("CAMPOS")) {
                    seccion = lineaUpper;
                    continue;
                }

                switch (seccion) {
                    case "ARCHIVO" -> {
                        String[] informacion =
                                obtenerInformacion(linea, "=", CANTIDAD_INFO_ARCHIVO);
                        String clave = informacion[0].trim();
                        String valor = informacion[1].trim();

                        if (clave.equalsIgnoreCase("Nombre")) {
                            nombreArchivo = valor.trim();
                        } else if (clave.equalsIgnoreCase("Version")) {
                            versionFormato = Integer.parseInt(valor);
                            if (versionFormato > VERSION_FORMATO || versionFormato < 1) {
                                throw new ArchivoInvalidoException(
                                        "La versión del formato del archivo es inválida");
                            }
                        } else if (clave.equalsIgnoreCase("CampoSiguienteId")) {
                            siguienteCampoId = Integer.parseInt(valor);
                        }
                    }
                    case "CAMPOS" -> {
                        String[] informacion =
                                obtenerInformacion(linea, "\\|", CANTIDAD_MIN_CAMPO_NON_TEXTO);

                        int id = Integer.parseInt(informacion[0].trim());
                        String nombre = informacion[1].trim();
                        TipoCampo tipo = TipoCampo.valueOf(informacion[2].trim());

                        int tamanio = 0;
                        boolean obligatorio;
                        String valorPredeterminado;

                        if (tipo == TipoCampo.TEXTO) {
                            if (informacion.length < CANTIDAD_MIN_CAMPO_NON_TEXTO) {
                                throw new IllegalStateException(
                                        "El campo de texto no tiene la cantidad de datos esperado");
                            }
                            tamanio = Integer.parseInt(informacion[3].trim());
                            String valorBoolean = informacion[4].trim();
                            if (valorBoolean.equalsIgnoreCase("true")
                                    || valorBoolean.equalsIgnoreCase("false")) {
                                obligatorio = Boolean.parseBoolean(valorBoolean);
                            } else {
                                throw new IllegalStateException(
                                        "Valor boolean incorrecto: " + valorBoolean);
                            }
                            valorPredeterminado =
                                    informacion.length > CANTIDAD_MIN_CAMPO_TEXTO
                                            ? informacion[5].trim()
                                            : "";
                        } else {
                            obligatorio = Boolean.parseBoolean(informacion[3].trim());
                            valorPredeterminado =
                                    informacion.length > CANTIDAD_MIN_CAMPO_NON_TEXTO
                                            ? informacion[4].trim()
                                            : "";
                        }

                        Campo campo =
                                tipo == TipoCampo.TEXTO
                                        ? new Campo(
                                                id,
                                                nombre,
                                                tipo,
                                                tamanio,
                                                obligatorio,
                                                valorPredeterminado)
                                        : new Campo(
                                                id, nombre, tipo, obligatorio, valorPredeterminado);
                        campos.add(campo);
                    }
                    case "LLAVE_PRIMARIA" -> {
                        String[] informacion = obtenerInformacion(linea, "=", 2);
                        String clave = informacion[0].trim();
                        String valor = informacion[1].trim();
                        if (clave.equalsIgnoreCase("Campo")) {
                            llavePrimariaId = Integer.parseInt(valor);
                        }
                    }
                }
            }
            metadata = new Metadata(nombreArchivo, siguienteCampoId);
            for (Campo campo : campos) {
                metadata.agregarCampo(campo);
            }
            if (llavePrimariaId != -1) {
                metadata.establecerLlavePrimaria(llavePrimariaId);
            }
            metadata.establecerFormatoArchivo(versionFormato);
        } catch (IOException
                | IllegalStateException
                | IllegalArgumentException
                | ArrayIndexOutOfBoundsException ex) {
            throw new ArchivoInvalidoException("Error al leer el archivo: " + ex.getMessage());
        }
        return metadata;
    }

    public void guardar(File file) throws IOException {
        if (!campos.isEmpty() && llavePrimariaId == -1) {
            throw new IllegalStateException("Se debe definir una llave primaria antes de guardar");
        }

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {
            bw.write(FIRMA_FORMATO + "\n");
            bw.write("[ARCHIVO]\n");
            bw.write(
                    "Nombre="
                            + (nombreArchivo.contains(".txt")
                                    ? nombreArchivo.substring(0, nombreArchivo.indexOf(".txt"))
                                    : nombreArchivo)
                            + "\n");
            bw.write("Version=" + versionFormato + "\n");
            bw.write("CampoSiguienteId=" + siguienteCampoId + "\n");
            bw.write("[LLAVE_PRIMARIA]\n");
            bw.write("Campo=" + llavePrimariaId + "\n");
            bw.write("[CAMPOS]\n");
            for (Campo campo : campos) {
                bw.write(campo.toString());
                bw.newLine();
            }
        }
    }

    private static String[] obtenerInformacion(String linea, String regex, int cantidad) {
        String[] info = linea.split(regex);
        if (info.length < cantidad) {
            throw new IllegalStateException(
                    "La cantidad deseada de información no coincide con la información obtenida");
        }
        return info;
    }

    // Versión del formato actual de la metadata
    public static final int VERSION_FORMATO = 1;

    // Valores predeterminados
    private static final int CANTIDAD_INFO_ARCHIVO = 2;
    private static final int CANTIDAD_MIN_CAMPO_TEXTO = 5;
    private static final int CANTIDAD_MIN_CAMPO_NON_TEXTO = 4;

    // Firma única del formato propio de archivo
    public static final String FIRMA_FORMATO = "DBMS-AFJ";
}
