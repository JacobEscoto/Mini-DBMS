package com.minidbms.gui.menu;

import com.minidbms.gui.acciones.AccionesApp;
import java.awt.event.ActionListener;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

public class MenubarPrincipal extends JMenuBar {

    private final AccionesApp acciones;

    public MenubarPrincipal(AccionesApp acciones) {
        this.acciones = acciones;
        add(
                construirMenu(
                        "Archivo",
                        nuevoItem("Nuevo Archivo", null),
                        nuevoItem("Abrir Archivo", null),
                        nuevoItem("Cerrar Archivo", null),
                        nuevoItem("Salir", (e -> acciones.salir()))));
    }

    private JMenu construirMenu(String texto, JMenuItem... items) {
        JMenu menu = new JMenu(texto);

        for (JMenuItem item : items) {
            menu.add(item);
        }

        return menu;
    }

    private JMenuItem nuevoItem(String texto, ActionListener evt) {
        JMenuItem item = new JMenuItem(texto);
        item.addActionListener(evt);
        return item;
    }
}
