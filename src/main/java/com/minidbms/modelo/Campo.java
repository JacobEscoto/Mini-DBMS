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
        this.id = id;
        this.tipo = tipo;
        this.nombre = nombre;
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
        this.nombre = nuevoNombre;
    }

    public void cambiarTipo(TipoCampo nuevoTipo) throws IllegalStateException {
        if (!esValorCompatible(valorPredeterminado, nuevoTipo)) {
            throw new IllegalStateException(
                    "El valor predeterminado no es compatible con el tipo " + nuevoTipo + ".");
        }
        this.tipo = nuevoTipo;
    }

    public void reducirLongitudMaxima(int nuevaLongitudMaxima) throws IllegalArgumentException {
        if (nuevaLongitudMaxima <= 0) {
            throw new IllegalArgumentException("La longitud que ingresó es menor o igual a 0.");
        } else if (nuevaLongitudMaxima > longitudMaxima) {
            throw new IllegalArgumentException(
                    "La longitud que ingresó es mayor que la anterior. ¡No se puede reducir!");
        }
        this.longitudMaxima = nuevaLongitudMaxima;
    }

    private boolean esValorCompatible(String valor, TipoCampo tipo) {
        if (valor == null) {
            return true;
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
}
