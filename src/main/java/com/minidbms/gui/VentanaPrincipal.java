package com.minidbms.gui;

import com.minidbms.gui.acciones.AccionesApp;
import com.minidbms.gui.menu.MenubarPrincipal;
import javax.swing.JFrame;

public class VentanaPrincipal extends JFrame {

    private AccionesApp acciones;

    private final MenubarPrincipal mainMenubar;
    private final PanelBienvenida panelBienvenida;

    public VentanaPrincipal(AccionesApp acciones) {
        this.acciones = acciones;
        this.panelBienvenida = new PanelBienvenida();
        super("Mini DBMS");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        initComponents();
        setLocationRelativeTo(null);
        setSize(1280, 720);

        mainMenubar = new MenubarPrincipal(acciones);
        setJMenuBar(mainMenubar);

        add(panelBienvenida);
    }

    private void initComponents() {}
}
