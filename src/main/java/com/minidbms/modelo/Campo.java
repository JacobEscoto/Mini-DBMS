package com.minidbms.modelo;

import com.minidbms.utilidades.ValidadorTexto;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class Campo {

    private final int id;
    private String nombre;
    private TipoCampo tipo;
    private int longitudMaxima;
    private final boolean obligatorio;
    private final String valorPredeterminado;

    public Campo(
            int id,
            String nombre,
            TipoCampo tipo,
            boolean obligatorio,
            String valorPredeterminado) {
        this(id, nombre, tipo, 0, obligatorio, valorPredeterminado);
    }

    public Campo(
            int id,
            String nombre,
            TipoCampo tipo,
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
        int longitudCoincide = (tipo == TipoCampo.TEXTO) ? longitudMaxima : 0;
        validarLongitud(tipo, longitudCoincide);

        if (!esValorCompatible(valorPredeterminado, tipo, longitudCoincide)) {
            throw new IllegalArgumentException(
                    "Valor incompatible -> campo: "
                            + nombre
                            + ", tipo: "
                            + tipo
                            + ", valorPredeterminado: '"
                            + valorPredeterminado
                            + "'"
                            + ", longitudCoincide: "
                            + longitudCoincide);
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

    public void cambiarTipo(TipoCampo nuevoTipo, int nuevaLongitud) {
        if (nuevoTipo == null) {
            throw new IllegalArgumentException("El tipo de campo no debe ser nulo");
        }
        if (nuevoTipo == TipoCampo.TEXTO && nuevaLongitud <= 0) {
            throw new IllegalArgumentException("La longitud debe ser mayor que cero");
        }
        int longitudNueva = nuevoTipo == TipoCampo.TEXTO ? nuevaLongitud : 0;
        validarLongitud(nuevoTipo, longitudNueva);

        if (!esValorCompatible(valorPredeterminado, nuevoTipo, longitudNueva)) {
            throw new IllegalArgumentException(
                    "El valor predeterminado no es compatible con el tipo: " + nuevoTipo);
        }
        ValidadorTexto.validarCaracteresProhibidos(valorPredeterminado);

        this.tipo = nuevoTipo;
        this.longitudMaxima = longitudNueva;
    }

    public void reducirLongitudMaxima(int nuevaLongitudMaxima) {
        if (tipo != TipoCampo.TEXTO) {
            throw new IllegalStateException("Solo los campos de texto tiene longitud máxima");
        }
        if (nuevaLongitudMaxima <= 0) {
            throw new IllegalArgumentException("La longitud que ingresó es menor o igual a cero");
        } else if (nuevaLongitudMaxima > longitudMaxima) {
            throw new IllegalArgumentException(
                    "La longitud que ingresó es mayor que la anterior. ¡No se puede reducir!");
        }
        if (!esValorCompatible(valorPredeterminado, tipo, nuevaLongitudMaxima)) {
            throw new IllegalArgumentException(
                    "El valor predeterminado no cabe en la nueva longitud máxima");
        }
        this.longitudMaxima = nuevaLongitudMaxima;
    }

    private boolean esValorCompatible(String valor, TipoCampo tipo, int longitud) {
        if (valor == null || valor.isBlank()) {
            return true;
        }

        if ((tipo == TipoCampo.ENTERO || tipo == TipoCampo.DECIMAL) && longitud != 0) {
            return false;
        }

        try {
            switch (tipo) {
                case ENTERO -> {
                    Integer.valueOf(valor);
                    return true;
                }
                case DECIMAL -> {
                    Double.valueOf(valor);
                    return true;
                }
                case FECHA -> {
                    LocalDate.parse(valor);
                    return true;
                }
                case BOOLEANO -> {
                    return valor.equalsIgnoreCase("true") || valor.equalsIgnoreCase("false");
                }
                case TEXTO -> {
                    return valor.length() <= longitud;
                }
                default -> {
                    return false;
                }
            }
        } catch (NumberFormatException | DateTimeParseException ex) {
            return false;
        }
    }

    private void validarNombre(String nombre) {
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("El nombre del campo no debe estar vacío");
        }

        ValidadorTexto.validarCaracteresProhibidos(nombre);
    }

    private void validarLongitud(TipoCampo tipo, int longitud) {
        if (tipo == TipoCampo.TEXTO && longitud <= 0) {
            throw new IllegalArgumentException("La longitud debe ser mayor que cero");
        }
    }

    @Override
    public String toString() {
        return id
                + "|"
                + nombre
                + "|"
                + tipo
                + "|"
                + (tipo == TipoCampo.TEXTO ? longitudMaxima + "|" : "")
                + (obligatorio ? "true" : "false")
                + "|"
                + valorPredeterminado;
    }
}
