package view;

import javax.swing.*;
import java.awt.*;

// Janela principal: o mapa e a lateral. Os dois painéis são observadores da fachada.
public class JanelaJogo extends JFrame {
    private PainelTabuleiro tabuleiro;
    private PainelControles controles;

    public JanelaJogo() {
        tabuleiro = new PainelTabuleiro();
        controles = new PainelControles();

        setTitle("Herdeiros de Khan");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setLayout(new BorderLayout());
        add(tabuleiro, BorderLayout.CENTER);
        add(controles, BorderLayout.EAST);
        pack();
        setLocationRelativeTo(null);
        // setVisible(true) fica com o Controller, depois da escolha das cartas secretas
    }

    // mensagem simples na lateral, sem caixa de diálogo
    public void mostrarAviso(String texto) {
        controles.mostrarAviso(texto);
    }

    // ---------- usados pelo Controller para registrar os listeners e ler as escolhas ----------

    public PainelTabuleiro getPainelTabuleiro() { return tabuleiro; }
    public JButton getBtnAtivarColuna() { return controles.getBtnAtivar(); }
    public JButton getBtnEncerrarTurno() { return controles.getBtnEncerrar(); }
    public JButton getBtnKhanMelhorias() { return controles.getBtnKhanMelhorias(); }
    public JButton getBtnComprar() { return controles.getBtnComprar(); }
    public JButton getBtnMelhoriaGratis() { return controles.getBtnMelhoriaGratis(); }
    public JButton getBtnVerCartela() { return controles.getBtnVerCartela(); }
    public JButton getBtnVerCartas() { return controles.getBtnVerCartas(); }
    public JComboBox<String> getComboColunas() { return controles.getComboColunas(); }
    public JComboBox<String> getComboModo() { return controles.getComboModo(); }
    public JComboBox<String> getComboMelhorias() { return controles.getComboMelhorias(); }
    public JComboBox<String> getComboColunaDestino() { return controles.getComboColunaDestino(); }
}