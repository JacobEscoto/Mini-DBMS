package com.minidbms;

import com.minidbms.gui.VentanaPrincipal;
import javax.swing.SwingUtilities;

/** Hello world! */
public class App {
    public static void main(String[] args) {
        System.out.println("=== Mini DBMS Project ===");
        SwingUtilities.invokeLater(
                () -> {
                    new VentanaPrincipal().setVisible(true);
                });
    }
}
