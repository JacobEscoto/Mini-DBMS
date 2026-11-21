package com.minidbms.gui.acciones;

import com.minidbms.excepciones.ArchivoInvalidoException;
import com.minidbms.excepciones.CampoDuplicadoException;
import com.minidbms.excepciones.CampoInexistenteException;
import com.minidbms.modelo.Campo;
import com.minidbms.modelo.Metadata;
import com.minidbms.modelo.TipoCampo;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

public class ControladorApp implements AccionesApp {

    private String ruta;
    private Metadata metadata;
    private boolean modificado;

    public void crearArchivo(String nombre) throws IOException, IllegalStateException {
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("Se necesita un nombre válido para el archivo");
        }
        nombre = nombre.trim();
        if (!nombre.toLowerCase().endsWith(".txt")) {
            nombre += ".txt";
        }

        File archivo = new File(nombre);
        if (!archivo.createNewFile()) {
            throw new IllegalStateException("El archivo ya existe");
        }
        Metadata metadataTemp = new Metadata(nombre, 1);
        metadataTemp.guardar(archivo);
        this.ruta = archivo.getAbsolutePath();
        this.metadata = metadataTemp;
        this.modificado = false;
    }

    public void abrirArchivo(File file) throws IOException, ArchivoInvalidoException {
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            Metadata metadataTemp = Metadata.leer(br);
            this.ruta = file.getAbsolutePath();
            this.metadata = metadataTemp;
            this.modificado = false;
        }
    }

    public void guardar() throws IOException {
        if (!hayArchivoAbierto()) {
            return;
        }
        metadata.guardar(new File(ruta));
        modificado = false;
    }

    public void crearCampo(
            String nombre,
            TipoCampo tipo,
            int longitudMax,
            boolean obligatorio,
            String valorPredeterminado)
            throws ArchivoInvalidoException, CampoInexistenteException, CampoDuplicadoException {
        if (!hayArchivoAbierto()) {
            throw new ArchivoInvalidoException("No hay un archivo abierto.");
        }
        metadata.agregarCampo(nombre, tipo, longitudMax, obligatorio, valorPredeterminado);
        this.modificado = true;
    }

    public void borrarCampo(int id) throws ArchivoInvalidoException {
        if (!hayArchivoAbierto()) {
            throw new ArchivoInvalidoException("No hay un archivo abierto");
        }
        metadata.borrarCampo(id);
        this.modificado = true;
    }

    public void cerrar() {
        this.ruta = null;
        this.metadata = null;
        this.modificado = false;
    }

    public boolean hayArchivoAbierto() {
        return metadata != null;
    }

    public String obtenerRuta() {
        return ruta;
    }

    public Metadata obtenerMetadata() {
        return metadata;
    }

    public List<Campo> listarCampos() {
        return metadata.obtenerCampos();
    }

    public boolean hayModificaciones() {
        return modificado;
    }

    @Override
    public void salir() {
        System.exit(0);
    }
}
