package com.minidbms.modelo;

public record LlaveForanea(int campoId, String archivoRef, int campoIdRef) {
    public LlaveForanea {
        archivoRef = archivoRef.trim();
    }
}
