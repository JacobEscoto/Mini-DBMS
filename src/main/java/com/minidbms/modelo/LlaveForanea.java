package com.minidbms.modelo;

public record LlaveForanea(int campoId, String archivoRef, int campoIdRef) {
    public LlaveForanea {
        if (campoId <= 0) {
            throw new IllegalArgumentException("El ID del campo debe ser positivo");
        }
        if (campoIdRef <= 0) {
            throw new IllegalArgumentException("El ID del campo de referencia debe ser positivo");
        }
        if (archivoRef == null || archivoRef.isBlank()) {
            throw new IllegalArgumentException("El archivo de referencia es obligatorio");
        }
        archivoRef = archivoRef.trim();
    }
}
