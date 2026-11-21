package com.minidbms.utilidad;

public class ValidadorTexto {

    private static final String CHARS_PROHIBIDOS = "[]|=";

    private ValidadorTexto() {}

    public static void validarCaracteresProhibidos(String texto) {
        if (texto == null) {
            return;
        }

        for (int i = 0; i < CHARS_PROHIBIDOS.length(); i++) {
            char prohibido = CHARS_PROHIBIDOS.charAt(i);
            if (texto.indexOf(prohibido) != -1) {
                throw new IllegalArgumentException(
                        "El valor de '" + texto + "' contiene un cáracter especial: " + prohibido);
            }
        }
    }
}
