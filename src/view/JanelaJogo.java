package view;

import javax.swing.*;
import java.awt.*;

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
        // a lateral fica numa rolagem: em telas mais baixas aparece uma barra em vez de botões cortados
        JScrollPane rolagem = new JScrollPane(controles, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        rolagem.setBorder(null);
        rolagem.getVerticalScrollBar().setUnitIncrement(16);
        int barra = rolagem.getVerticalScrollBar().getPreferredSize().width;
        rolagem.setPreferredSize(new Dimension(PainelControles.LARGURA + barra, tabuleiro.getPreferredSize().height));
        add(rolagem, BorderLayout.EAST);
        pack();
        setLocationRelativeTo(null);
    }

    public void mostrarAviso(String texto) {
        controles.mostrarAviso(texto);
    }


    public PainelTabuleiro getPainelTabuleiro() { return tabuleiro; }
    public JButton getBtnAtivarColuna() { return controles.getBtnAtivar(); }
    public JButton getBtnEncerrarTurno() { return controles.getBtnEncerrar(); }
    public JButton getBtnKhanMelhorias() { return controles.getBtnKhanMelhorias(); }
    public JButton getBtnComprar() { return controles.getBtnComprar(); }
    public JButton getBtnMelhoriaGratis() { return controles.getBtnMelhoriaGratis(); }
    public JButton getBtnVerCartela() { return controles.getBtnVerCartela(); }
    public JButton getBtnVerCartas() { return controles.getBtnVerCartas(); }
    public JButton getBtnSalvar() { return controles.getBtnSalvar(); }
    public JButton getBtnComprarPilha() { return controles.getBtnComprarPilha(); }
    public JButton getBtnUsarBonus() { return controles.getBtnUsarBonus(); }
    public JButton getBtnTrocarEspada() { return controles.getBtnTrocarEspada(); }
    public int getBonusEscolhido() { return controles.bonusEscolhido(); }
    public JComboBox<String> getComboColunas() { return controles.getComboColunas(); }
    public JComboBox<String> getComboModo() { return controles.getComboModo(); }
    public JComboBox<String> getComboMelhorias() { return controles.getComboMelhorias(); }
    public JComboBox<String> getComboColunaDestino() { return controles.getComboColunaDestino(); }
}