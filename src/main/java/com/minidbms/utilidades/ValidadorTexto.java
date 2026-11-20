package com.minidbms.utilidades;

public class ValidadorTexto {

    private static final String CHARS_PROHIBIDOS = "<>?[]{},.|=\n";

    private ValidadorTexto() {}

    public static void validarCaracteresProhibidos(String texto) {
        if (texto == null) {
            return;
        }

        for (int i = 0; i < CHARS_PROHIBIDOS.length(); i++) {
            char prohibido = CHARS_PROHIBIDOS.charAt(i);
            if (texto.indexOf(prohibido) >= 0) {
                throw new IllegalArgumentException(
                        "El valor de '" + texto + "' contiene un cáracter especial: " + prohibido);
            }
        }
    }
}
