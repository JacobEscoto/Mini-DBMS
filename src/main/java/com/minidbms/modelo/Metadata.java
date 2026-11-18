package com.minidbms.modelo;

import java.io.BufferedReader;
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
        } else if (nombreArchivo.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del archivo está vacío");
        }
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
            throw new IllegalStateException("El campo ya existe");
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
            throw new IllegalArgumentException("El campo a agregar no debe ser nulo");
        }
        campos.add(campo);
    }

    public Campo buscarCampoPorId(int id) {
        return campos.stream().filter(campo -> campo.obtenerId() == id).findFirst().orElse(null);
    }

    public Campo buscarCampoPorNombre(String nombre) {
        if (nombre == null) {
            return null;
        }
        return campos.stream()
                .filter(campo -> campo.obtenerNombre().equalsIgnoreCase(nombre))
                .findFirst()
                .orElse(null);
    }

    public void renombrarCampo(int id, String nuevoNombre) {
        Campo campo = buscarCampoPorId(id);
        if (campo == null) {
            throw new IllegalStateException("El campo a renombrar no se encuentra");
        }
        campo.renombrar(nuevoNombre.trim());
    }

    public void reducirLongitudCampo(int id, int longitudMaxima) {
        Campo campo = buscarCampoPorId(id);
        if (campo == null) {
            throw new IllegalStateException("El campo a reducir la longitud es inexistente");
        }
        campo.reducirLongitudMaxima(longitudMaxima);
    }

    public void cambiarTipoCampo(int id, TipoCampo nuevoTipo) {
        Campo campo = buscarCampoPorId(id);
        if (campo == null) {
            throw new IllegalStateException("El campo a modificar es inexistente");
        }
        campo.cambiarTipo(nuevoTipo);
    }

    public void borrarCampo(int id) {
        Campo campo = buscarCampoPorId(id);
        if (campo == null) {
            throw new IllegalStateException("El campo a eliminar es inexistente");
        }
        if (esLlavePrimaria(id)) {
            throw new IllegalStateException(
                    "El campo no se puede eliminar porque es llave primaria");
        }
        campos.remove(campo);
    }

    public void establecerLlavePrimaria(int id) {
        Campo campo = buscarCampoPorId(id);
        if (campo == null) {
            throw new IllegalStateException(
                    "El campo a establecer como llave primaria es inexistente");
        }
        if (!campo.esObligatorio()) {
            throw new IllegalStateException(
                    "El campo debe ser obligatorio para establecerse como llave primaria");
        }
        llavePrimariaId = id;
    }

    public void establecerFormatoArchivo(int nuevaVersionFormato) {
        if (nuevaVersionFormato <= 0) {
            throw new IllegalArgumentException("");
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

    public static Metadata leer(BufferedReader br) {
        if (br == null) {
            throw new IllegalArgumentException("");
        }
        String linea, seccion = "";

        String nombreArchivo = "";
        int versionFormato = VERSION_FORMATO;
        int siguienteCampoId = 1;
        int llavePrimariaId = -1;
        Metadata metadata;
        List<Campo> campos = new ArrayList<>();

        try {
            while ((linea = br.readLine()) != null) {
                linea = linea.trim();

                if (linea.isEmpty()) {
                    continue;
                }

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
                        } else if (clave.equalsIgnoreCase("CampoSiguienteId")) {
                            siguienteCampoId = Integer.parseInt(valor);
                        }
                    }
                    case "CAMPOS" -> {
                        String[] informacion =
                                obtenerInformacion(linea, "\\|", CANTIDAD_MIN_CAMPO_NON_TEXTO);

                        String nombre = informacion[0].trim();
                        TipoCampo tipo = TipoCampo.valueOf(informacion[1].trim());
                        int tamanio = 0;
                        boolean obligatorio = false;
                        String valorPredeterminado = "";

                        if (tipo == TipoCampo.TEXTO) {
                            tamanio = Integer.parseInt(informacion[2].trim());
                            obligatorio = Boolean.parseBoolean(informacion[3].trim());
                            valorPredeterminado =
                                    informacion.length > CANTIDAD_MIN_CAMPO_TEXTO
                                            ? informacion[4].trim()
                                            : "";
                        } else {
                            obligatorio = Boolean.parseBoolean(informacion[2].trim());
                            valorPredeterminado =
                                    informacion.length > CANTIDAD_MIN_CAMPO_NON_TEXTO
                                            ? informacion[3].trim()
                                            : "";
                        }

                        Campo campo =
                                tipo == TipoCampo.TEXTO
                                        ? new Campo(
                                                siguienteCampoId,
                                                nombre,
                                                tipo,
                                                tamanio,
                                                obligatorio,
                                                valorPredeterminado)
                                        : new Campo(
                                                siguienteCampoId,
                                                nombre,
                                                tipo,
                                                obligatorio,
                                                valorPredeterminado);
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
        } catch (IOException | NumberFormatException | IllegalStateException ex) {
            return null;
        }
        metadata = new Metadata(nombreArchivo, siguienteCampoId);
        campos.stream().forEach(campo -> metadata.agregarCampo(campo));
        if (llavePrimariaId != -1) {
            try {
                metadata.establecerLlavePrimaria(llavePrimariaId);
            } catch (IllegalStateException ex) {
                return null;
            }
        }
        for (Campo campo : metadata.obtenerCampos()) {
            System.out.println(campo);
        }
        return metadata;
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
    private static final int CANTIDAD_MIN_CAMPO_TEXTO = 4;
    private static final int CANTIDAD_MIN_CAMPO_NON_TEXTO = 3;
}
