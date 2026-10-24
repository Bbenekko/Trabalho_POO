package view;

import javax.swing.*;
import java.awt.*;

import model.ModelFacade;

public class JanelaJogo extends JFrame {
    private PainelTabuleiro tabuleiro;
    private PainelControles controles;

    // a View só lê o Model para desenhar; quem altera o Model é o Controller
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
        // setVisible(true) fica no main, depois de o Controller registrar os listeners
    }

    // ---------- usados pelo Controller ----------

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

    // redesenha tudo depois de uma ação válida e apaga o aviso anterior
    public void atualizar() {
        controles.limparAviso();
        controles.atualizar();
        tabuleiro.repaint();
    }

    // mensagem simples na lateral, sem caixa de diálogo
    public void mostrarAviso(String texto) {
        controles.mostrarAviso(texto);
    }
}
