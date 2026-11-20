package com.minidbms;

import com.formdev.flatlaf.FlatDarculaLaf;
import com.minidbms.gui.VentanaPrincipal;
import com.minidbms.gui.acciones.ControladorApp;
import javax.swing.SwingUtilities;

public class App {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(
                () -> {
                    ControladorApp controlador = new ControladorApp();
                    FlatDarculaLaf.setup();
                    new VentanaPrincipal(controlador).setVisible(true);
                });
    }
}
