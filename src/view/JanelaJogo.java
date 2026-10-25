package view;

import javax.swing.*;
import java.awt.*;

import model.ModelFacade;

public class JanelaJogo extends JFrame {
    private PainelTabuleiro tabuleiro;
    private PainelControles controles;

    public JanelaJogo(ModelFacade modelo) {
        tabuleiro = new PainelTabuleiro(modelo);
        controles = new PainelControles(modelo);

        setTitle("Herdeiros de Khan");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setLayout(new BorderLayout());
        add(tabuleiro, BorderLayout.CENTER);
        add(controles, BorderLayout.EAST);
        pack();
        setLocationRelativeTo(null);
    }


    public PainelTabuleiro getPainelTabuleiro() {
        return tabuleiro;
    }

    public JButton getBtnAtivarColuna() {
        return controles.getBtnAtivar();
    }

    public JButton getBtnEncerrarTurno() {
        return controles.getBtnEncerrar();
    }

    public JComboBox<String> getComboColunas() {
        return controles.getComboColunas();
    }

    public void atualizar() {
        controles.limparAviso();
        controles.atualizar();
        tabuleiro.repaint();
    }

    public void mostrarAviso(String texto) {
        controles.mostrarAviso(texto);
    }
}
