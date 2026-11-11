package com.minidbms.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class PanelBienvenida extends JPanel {

    private static final Color COLOR_PRIMARIO = Color.decode("#4E8383");

    public PanelBienvenida() {
        initComponents();
    }

    private void initComponents() {
        setBackground(Color.WHITE);
        setPreferredSize(new Dimension(1280, 720));
        setLayout(new GridBagLayout());

        GridBagConstraints gbConstraints = new GridBagConstraints();
        gbConstraints.gridy = 0;
        gbConstraints.weighty = 1.0;
        gbConstraints.fill = GridBagConstraints.BOTH;

        gbConstraints.gridx = 0;
        gbConstraints.weightx = 0.5;

        JPanel logoPanel = new JPanel(new BorderLayout());
        logoPanel.setBackground(COLOR_PRIMARIO);

        JLabel logoIcon =
                new JLabel(new ImageIcon(getClass().getResource("/com/minidbms/gui/DBMS.png")));
        logoIcon.setHorizontalAlignment(SwingConstants.CENTER);
        logoIcon.setVerticalAlignment(SwingConstants.CENTER);

        logoPanel.add(logoIcon, BorderLayout.CENTER);

        add(logoPanel, gbConstraints);

        JPanel panelTexto = new JPanel(new GridBagLayout());
        panelTexto.setBackground(Color.BLUE.darker());

        gbConstraints.gridx = 1;
        gbConstraints.weightx = 0.5;

        JLabel title = new JLabel("Mini DBMS");
        title.setFont(new Font("Sans Serif", Font.BOLD, 36));
        title.setForeground(Color.WHITE);
        panelTexto.add(title);

        add(panelTexto, gbConstraints);
    }
}
