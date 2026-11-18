package com.minidbms.modelo;

import java.util.ArrayList;
import java.util.List;

public class Metadata {
    private String nombreArchivo;
    private int versionFormato;

    private ArrayList<Campo> campos;
    private int campoSigId;
    private int llavePrimariaId;

    private final int minTamanioHueco;
    private int inicioAvailList;
    private PoliticaReutilizacion politicaReutilizacion;

    // Contadores
    private int registrosActivos;
    private int registrosEliminados;
    private int bytesLibres;

    public Metadata(String nombreArchivo, int minTamanioHueco, int campoSigId) {
        this.nombreArchivo = nombreArchivo;
        this.campos = new ArrayList<>();
        this.campoSigId = campoSigId;
        this.minTamanioHueco = minTamanioHueco;
        this.inicioAvailList = -1;
        this.politicaReutilizacion = PoliticaReutilizacion.FIRST_FIT;
    }

    public Campo agregarCampo(
            TipoCampo tipo,
            String nombre,
            int longitudMaxima,
            boolean obligatorio,
            String valorPredeterminado) {
        if (!esNombreUnico(nombre)) {
            throw new IllegalStateException("El campo ya existe");
        }
        Campo campoNuevo =
                new Campo(
                        campoSigId, tipo, nombre, longitudMaxima, obligatorio, valorPredeterminado);
        campoSigId++;
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
        campo.renombrar(nuevoNombre);
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
        registrosEliminados++;
    }

    public void establecerLlavePrimaria(int id) {
        if (buscarCampoPorId(id) == null) {
            throw new IllegalStateException(
                    "El campo a establecer como llave primaria es inexistente");
        }
        llavePrimariaId = id;
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

    public int obtenerInicioAvailList() {
        return inicioAvailList;
    }

    public int obtenerVersionFormato() {
        return versionFormato;
    }

    public int obtenerMinTamanioHueco() {
        return minTamanioHueco;
    }

    public PoliticaReutilizacion obtenerPoliticaReutilizacion() {
        return politicaReutilizacion;
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
}
