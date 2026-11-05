package view;

import javax.swing.*;
import java.awt.*;
import java.util.List;

import model.ModelFacade;

// Antes do primeiro turno, cada jogador vê suas 2 cartas secretas e fica com 1 (p. 19).
// Os dois botões são tratados pelo Controller.
public class JanelaCartaSecreta extends JFrame {
    private static final int LARGURA_CARTA = 200;

    private ModelFacade modelo = ModelFacade.getInstance();
    private int jogador = 0;
    private JLabel lblTitulo = new JLabel(" ");
    private JButton[] opcoes = {new JButton("Ficar com esta"), new JButton("Ficar com esta")};

    public JanelaCartaSecreta() {
        setTitle("Herdeiros de Khan · carta secreta");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 16));
        lblTitulo.setBorder(BorderFactory.createEmptyBorder(12, 12, 8, 12));

        JPanel cartas = new JPanel(new GridLayout(1, 2, 24, 0));
        cartas.setBorder(BorderFactory.createEmptyBorder(0, 24, 16, 24));
        for (int i = 0; i < 2; i++) {
            JPanel coluna = new JPanel(new BorderLayout(0, 8));
            coluna.add(new PainelCarta(i), BorderLayout.CENTER);
            coluna.add(opcoes[i], BorderLayout.SOUTH);
            cartas.add(coluna);
        }

        setLayout(new BorderLayout());
        add(lblTitulo, BorderLayout.NORTH);
        add(cartas, BorderLayout.CENTER);
        pack();
        setLocationRelativeTo(null);
    }

    // mostra as opções do jogador; os outros devem desviar o olhar
    public void mostrar(int jogador) {
        this.jogador = jogador;
        lblTitulo.setText("Jogador " + (jogador + 1) + " (" + modelo.corDoJogador(jogador)
                + "): escolha sua carta secreta");
        setVisible(true);
        repaint();
    }

    public JButton getBtnOpcao(int i) {
        return opcoes[i];
    }

    // a carta é desenhada com drawImage
    private class PainelCarta extends JPanel {
        private int opcao;

        PainelCarta(int opcao) {
            this.opcao = opcao;
            setPreferredSize(new Dimension(LARGURA_CARTA, LARGURA_CARTA * 3 / 2));
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            List<String> tipos = modelo.opcoesSecretas(jogador);
            g.drawImage(Desenhos.carta(tipos.get(opcao), LARGURA_CARTA, false), 0, 0, null);
        }
    }
}
