package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import controller.ControladorJogo;

class PainelControles extends JPanel {
    private static final int LARGURA = 250;
    private static final int ALTURA = 1000;    
    private static final int ALTURA_INDICADOR = 50;
    private static final int MARGEM = 15;

    private ControladorJogo controlador;
    private JLabel lblJogador = new JLabel();
    private JLabel lblMovimentos = new JLabel();
    private JComboBox<String> colunas = new JComboBox<String>();
    private JButton ativar = new JButton("Ativar coluna");
    private JButton encerrar = new JButton("Encerrar turno");
    private JLabel lblAviso = new JLabel(" ");

    PainelControles(ControladorJogo c) {
        this.controlador = c;
        setPreferredSize(new Dimension(LARGURA, ALTURA));
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createEmptyBorder(MARGEM, MARGEM, MARGEM, MARGEM));

        for (int i = 1; i <= 4; i++)
            colunas.addItem("Coluna " + i);

        ativar.addActionListener(new TratadorAtivar());
        encerrar.addActionListener(new TratadorEncerrar());

        lblJogador.setFont(new Font("SansSerif", Font.BOLD, 18));
        lblAviso.setForeground(Color.RED);
        colunas.setMaximumSize(new Dimension(LARGURA, 30));

        add(Box.createRigidArea(new Dimension(0, ALTURA_INDICADOR + 10)));
        adicionar(lblJogador);
        adicionar(lblMovimentos);
        add(Box.createRigidArea(new Dimension(0, 20)));
        adicionar(colunas);
        add(Box.createRigidArea(new Dimension(0, 5)));
        adicionar(ativar);
        add(Box.createRigidArea(new Dimension(0, 20)));
        adicionar(encerrar);
        add(Box.createRigidArea(new Dimension(0, 20)));
        adicionar(lblAviso);

        atualizar();
    }

    private void adicionar(JComponent comp) {
        comp.setAlignmentX(Component.LEFT_ALIGNMENT);
        add(comp);
    }

    void atualizar() {
        int vez = controlador.jogadorDaVez();
        lblJogador.setText("Vez do jogador " + (vez + 1) + " (" + controlador.corDoJogador(vez) + ")");
        lblMovimentos.setText("Movimentos restantes: " + controlador.movimentosRestantes());
        repaint();
    }

    void mostrarAviso(String texto) {
        lblAviso.setText(texto);
    }

    void limparAviso() {
        lblAviso.setText(" ");
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        String cor = controlador.corDoJogador(controlador.jogadorDaVez());

        int largura = getWidth() - 2 * MARGEM;
        g2.setColor(PainelTabuleiro.converterCor(cor));
        g2.fillRect(MARGEM, MARGEM, largura, ALTURA_INDICADOR);
        g2.setColor(Color.BLACK);
        g2.drawRect(MARGEM, MARGEM, largura, ALTURA_INDICADOR);
    }

    private class TratadorAtivar implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            limparAviso();
            controlador.ativarColuna(colunas.getSelectedIndex());
        }
    }

    private class TratadorEncerrar implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            limparAviso();
            controlador.encerrarTurno();
        }
    }
}