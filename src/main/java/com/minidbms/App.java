package com.minidbms;

import com.formdev.flatlaf.FlatDarculaLaf;
import com.minidbms.gui.VentanaPrincipal;
import com.minidbms.gui.acciones.AccionesApp;
import com.minidbms.gui.acciones.ControladorApp;
import javax.swing.SwingUtilities;

public class App {
    public static void main(String[] args) {
        System.out.println("=== Mini DBMS Project ===");
        SwingUtilities.invokeLater(
                () -> {
                    AccionesApp acciones = new ControladorApp();
                    FlatDarculaLaf.setup();
                    new VentanaPrincipal(acciones).setVisible(true);
                });
    }
}
