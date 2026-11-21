package com.minidbms.modelo;

public enum TipoCampo {
    ENTERO("Entero"),
    DECIMAL("Decimal"),
    FECHA("Fecha"),
    TEXTO("Texto"),
    BOOLEANO("Booleano");

    private final String etiqueta;

    TipoCampo(String etiqueta) {
        this.etiqueta = etiqueta;
    }

    @Override
    public String toString() {
        return etiqueta;
    }
}
