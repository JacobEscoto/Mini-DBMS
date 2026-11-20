package com.minidbms.excepciones;

public class CampoDuplicadoException extends IllegalStateException {

    public CampoDuplicadoException(String llave, String valor) {
        super(String.format("El campo ya existe con el mismo %s: %s", llave, valor));
    }
}
