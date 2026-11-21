package com.minidbms.excepcion;

public class CampoInexistenteException extends IllegalArgumentException {
    
    private final String archivo;
    private final String campo;

    public CampoInexistenteException(String archivo, String campo) {
        super(String.format("El campo %s no existe en el archivo '%s'.", campo, archivo));
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
