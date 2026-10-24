package view;

import javax.swing.*;
import java.awt.*;

import controller.ControladorJogo;

public class JanelaJogo extends JFrame {
    private PainelTabuleiro tabuleiro;
    private PainelControles controles;

    public JanelaJogo(ControladorJogo c) {
        tabuleiro = new PainelTabuleiro(c);
        controles = new PainelControles(c);
        c.registrarJanela(this); 

        setTitle("Herdeiros de Khan");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setLayout(new BorderLayout());
        add(tabuleiro, BorderLayout.CENTER);
        add(controles, BorderLayout.EAST);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public void atualizar() {
        tabuleiro.repaint();
        controles.atualizar();
    }
}