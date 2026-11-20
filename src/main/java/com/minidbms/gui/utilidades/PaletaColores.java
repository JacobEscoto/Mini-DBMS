package com.minidbms.gui.utilidades;

import java.awt.Color;

public enum PaletaColores {
    FONDO_PRIMARIO("#11111B"),
    FONDO_SECUNDARIO("#181825"),
    FONDO_PESTANIAS("#1E1E2E"),
    TEXTO_PRIMARIO("#FFFFFF"),
    TEXTO_SECUNDARIO("#A6ADC8"),
    ACENTO_PRIMARIO("#1E66F5");

    private final Color color;

    PaletaColores(String hex) {
        this.color = Color.decode(hex.startsWith("#") ? hex : "#" + hex);
    }

    public Color color() {
        return color;
    }
}
