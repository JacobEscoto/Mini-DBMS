package com.minidbms.excepcion;

public class CampoDuplicadoException extends IllegalArgumentException {

    private final String archivo;
    private final String campo;

    public CampoDuplicadoException(String archivo, String campo) {
        super(String.format("Ya existe un campo llamado %s en el archivo '%s'", campo, archivo));
        this.archivo = archivo;
        this.campo = campo;
    }

    public String obtenerArchivo() {
        return archivo;
    }

    public String obtenerCampo() {
        return campo;
    }
}
