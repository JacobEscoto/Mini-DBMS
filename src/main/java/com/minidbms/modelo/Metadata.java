package com.minidbms.modelo;

import java.util.ArrayList;
import java.util.List;

public class Metadata {
  private String nombreArchivo;
  private int versionFormato;

  private List<Campo> campos = new ArrayList<>();
  private int campoSigId;

  private int campoLlavePrimariaId;
  private int minTamanioHueco;

  private long inicioAvailList;
  private PoliticaReutilizacion politicaReutilizacion;

  private int registrosActivos;
  private int registrosEliminados;
  private int bytesLibres;

  public Metadata(String nombreArchivo, int minTamanioHueco) {
    this.nombreArchivo = nombreArchivo;
    this.minTamanioHueco = minTamanioHueco;
  }

  public List<Campo> obtenerCampos() {
    return List.copyOf(campos);
  }

  public int obtenerCampoLlavePrimariaId() {
    return campoLlavePrimariaId;
  }
}
