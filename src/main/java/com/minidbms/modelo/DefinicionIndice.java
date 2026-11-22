package com.minidbms.modelo;

public record DefinicionIndice(int campoId, int orden, boolean unico, String archivoIndice) {

    public static final int ORDEN_MIN = 3;
    public static final int ORDEN_DEFAULT = 6;

    public DefinicionIndice {
        if (campoId <= 0) {
            throw new IllegalArgumentException("El ID del campo debe ser positivo");
        }
        if (orden < ORDEN_MIN) {
            throw new IllegalArgumentException("El orden mínimo del árbol es: " + ORDEN_MIN);
        }
        if (archivoIndice == null || archivoIndice.isBlank()) {
            throw new IllegalArgumentException("El nombre del archivo de índice es obligatorio");
        }
        archivoIndice = archivoIndice.trim();
    }

    public DefinicionIndice(int campoId, boolean unico, String archivoIndice) {
        this(campoId, ORDEN_DEFAULT, unico, archivoIndice);
    }
}
