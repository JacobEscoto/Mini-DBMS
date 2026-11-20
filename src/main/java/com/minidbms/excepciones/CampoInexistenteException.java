package com.minidbms.excepciones;

public class CampoInexistenteException extends IllegalArgumentException {

    public CampoInexistenteException(String nombre, String mensaje) {
        super(String.format("El campo %s no existe. %s", nombre, mensaje));
    }

    public CampoInexistenteException(int id, String mensaje) {
        super(String.format("El campo con id %d no existe. %s", id, mensaje));
    }

    public CampoInexistenteException(String mensaje) {
        super("El campo no existe. " + mensaje);
    }
}
