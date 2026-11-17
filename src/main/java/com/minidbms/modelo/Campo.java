package com.minidbms.modelo;

import java.time.LocalDate;

public class Campo {

    private final int id;
    private TipoCampo tipo;
    private String nombre;
    private int longitudMaxima;
    private final boolean obligatorio;
    private final String valorPredeterminado;

    public Campo(
            int id,
            TipoCampo tipo,
            String nombre,
            int longitudMaxima,
            boolean obligatorio,
            String valorPredeterminado) {

        if (id <= 0) {
            throw new IllegalArgumentException("El ID debe ser mayor que cero");
        }
        if (tipo == null) {
            throw new IllegalArgumentException("El tipo de campo no debe ser nulo");
        }

        validarNombre(nombre);
        validarLongitud(longitudMaxima);

        if (!esValorCompatible(valorPredeterminado, tipo)) {
            throw new IllegalStateException(
                    "El valor predeterminado no es compatible con el tipo " + tipo);
        }

        this.id = id;
        this.tipo = tipo;
        this.nombre = nombre.trim();
        this.longitudMaxima = longitudMaxima;
        this.obligatorio = obligatorio;
        this.valorPredeterminado = valorPredeterminado;
    }

    public int obtenerId() {
        return id;
    }

    public TipoCampo obtenerTipo() {
        return tipo;
    }

    public String obtenerNombre() {
        return nombre;
    }

    public int obtenerLongitudMaxima() {
        return longitudMaxima;
    }

    public boolean esObligatorio() {
        return obligatorio;
    }

    public String obtenerValorPredeterminado() {
        return valorPredeterminado;
    }

    public void renombrar(String nuevoNombre) {
        validarNombre(nuevoNombre);
        this.nombre = nuevoNombre.trim();
    }

    public void cambiarTipo(TipoCampo nuevoTipo) {
        if (nuevoTipo == null) {
            throw new IllegalArgumentException("El tipo de campo no debe ser nulo");
        }
        if (!esValorCompatible(valorPredeterminado, nuevoTipo)) {
            throw new IllegalStateException(
                    "El valor predeterminado no es compatible con el tipo " + nuevoTipo);
        }
        this.tipo = nuevoTipo;
    }

    public void reducirLongitudMaxima(int nuevaLongitudMaxima) {
        if (nuevaLongitudMaxima <= 0) {
            throw new IllegalArgumentException("La longitud que ingresó es menor o igual a 0.");
        } else if (nuevaLongitudMaxima > longitudMaxima) {
            throw new IllegalArgumentException(
                    "La longitud que ingresó es mayor que la anterior. ¡No se puede reducir!");
        }

        if (!esValorCompatible(valorPredeterminado, tipo)) {
            throw new IllegalStateException(
                    "El valor predeterminado no es compatible con la nueva longitud");
        }
        this.longitudMaxima = nuevaLongitudMaxima;
    }

    private boolean esValorCompatible(String valor, TipoCampo tipo) {
        if (valor == null) {
            return true;
        }
        if (tipo == null) {
            return false;
        }

        try {
            switch (tipo) {
                case ENTERO -> Integer.parseInt(valor);
                case DECIMAL -> Double.parseDouble(valor);
                case FECHA -> LocalDate.parse(valor);
                case BOOLEANO -> {
                    if (!valor.equalsIgnoreCase("true") && !valor.equalsIgnoreCase("false")) {
                        return false;
                    }
                }
                case TEXTO -> {
                    return valor.length() <= longitudMaxima;
                }
            }
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    private void validarNombre(String nombre) {
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("El nombre del campo no debe estar vacío");
        }
    }

    private void validarLongitud(int longitud) {
        if (longitud <= 0) {
            throw new IllegalArgumentException("La longitud debe ser mayor que cero");
        }
    }
}
