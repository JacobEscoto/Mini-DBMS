package com.minidbms.excepcion;

public class CampoInexistenteException extends IllegalArgumentException {

    private final String archivo;
    private int id;
    private String campo;

    public CampoInexistenteException(String archivo, String campo) {
        super(String.format("El campo %s no existe en el archivo '%s'", campo, archivo));
        this.archivo = archivo;
        this.campo = campo;
    }

    public CampoInexistenteException(String archivo, int id) {
        super(String.format("El campo con ID '%d' no existe en el archivo '%s'", id, archivo));
        this.archivo = archivo;
        this.id = id;
    }

    public String obtenerArchivo() {
        return archivo;
    }

    public String obtenerCampo() {
        return campo;
    }
}
