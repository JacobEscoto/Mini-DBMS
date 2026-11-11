package com.minidbms.modelo;

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
}
