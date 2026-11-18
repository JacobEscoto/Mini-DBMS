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

    // private final int minTamanioHueco;
    private long encabezado;
    // private PoliticaReutilizacion politicaReutilizacion;

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

        // this.inicioAvailList = -1;
        // this.politicaReutilizacion = PoliticaReutilizacion.FIRST_FIT;
        this.llavePrimariaId = -1;

        // this.registrosActivos = 0;
        // this.registrosEliminados = 0;
        // this.bytesLibres = 0;
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
                new Campo(
                        siguienteCampoId,
                        nombre,
                        tipo,
                        longitudMaxima,
                        obligatorio,
                        valorPredeterminado);
        siguienteCampoId++;
        campos.add(campoNuevo);
        return campoNuevo;
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

    //    public void establecerPoliticaReutilizacion(PoliticaReutilizacion politicaReutilizacion) {
    //        if (politicaReutilizacion == null) {
    //            throw new IllegalArgumentException("La política de reutilización es nula");
    //        }
    //        this.politicaReutilizacion = politicaReutilizacion;
    //    }
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

    public long obtenerEncabezado() {
        return encabezado;
    }

    public int obtenerVersionFormato() {
        return versionFormato;
    }

    //    public PoliticaReutilizacion obtenerPoliticaReutilizacion() {
    //        return politicaReutilizacion;
    //    }
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

    public void LeerMetadata(BufferedReader br) throws IOException {
        String linea;
        String seccion = "";

        while ((linea = br.readLine()) != null) {
            linea = linea.trim();

            if (linea.equalsIgnoreCase("[ARCHIVO]")) {
                seccion = "Archivo";
            } else if (linea.equalsIgnoreCase("[LLAVE_PRIMARIA]")) {
                seccion = "Llave_primaria";
            } else if (linea.equalsIgnoreCase("[CAMPOS]")) {
                seccion = "Campos";
            }
            switch (seccion) {
                case "Archivo":
                    cargarArchivo(linea);
                    break;
                case "Llave_primaria":
                    cargarLlaveprimaria(linea);
                    break;
                case "Campos":
                    cargarCampo(linea);
                    break;
            }
        }
    }

    private void cargarArchivo(String linea) {
        String[] info = linea.split("=");
        String clave = info[0];
        String valor = info[1];
        if (clave.equalsIgnoreCase("Nombre")) {
            nombreArchivo = valor;
        } else if (clave.equalsIgnoreCase("Version")) {
            versionFormato = Integer.parseInt(valor);
        } else if (clave.equalsIgnoreCase("CampoSiguienteId")) {
            siguienteCampoId = Integer.parseInt(valor);
        }
    }

    private void cargarLlaveprimaria(String linea) {
        String[] info = linea.split("=");
        String clave = info[0];
        String valor = info[1];
        if (clave.equalsIgnoreCase("Campo")) {
            llavePrimariaId = Integer.parseInt(valor); // Revisalo
        }
    }

    private void cargarCampo(String linea) {
        String[] info = linea.split("\\|");
        String nombre = info[0];
        TipoCampo tipo = TipoCampo.valueOf(info[1]);
        int tamano = Integer.parseInt(info[2]);
        boolean obligatoria = info[3].equalsIgnoreCase("true") || info[3].equalsIgnoreCase("false");
        String valor = "";
        agregarCampo(nombre, tipo, tamano, obligatoria, valor);
    }

    // Versión del formato actual de la metadata
    public static final int VERSION_FORMATO = 1;
}
