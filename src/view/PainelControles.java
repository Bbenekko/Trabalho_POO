package view;

import javax.swing.*;
import java.awt.*;

import model.ModelFacade;

class PainelControles extends JPanel {
    private static final int LARGURA = 250;
    private static final int ALTURA = 300;           // mínima; a altura real acompanha o mapa
    private static final int ALTURA_INDICADOR = 50;
    private static final int MARGEM = 15;

    private ModelFacade modelo;
    private JLabel lblJogador = new JLabel();
    private JLabel lblMovimentos = new JLabel();
    private JComboBox<String> colunas = new JComboBox<String>();
    private JButton ativar = new JButton("Ativar coluna");
    private JButton encerrar = new JButton("Encerrar turno");
    private JLabel lblAviso = new JLabel(" ");       // " " para não ter altura zero

    PainelControles(ModelFacade modelo) {
        this.modelo = modelo;
        setPreferredSize(new Dimension(LARGURA, ALTURA));
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createEmptyBorder(MARGEM, MARGEM, MARGEM, MARGEM));

        for (int i = 1; i <= 4; i++)
            colunas.addItem("Coluna " + i);

        // os listeners dos botões são registrados pelo Controller (ObservadorAcoes)

        lblJogador.setFont(new Font("SansSerif", Font.BOLD, 16));
        lblAviso.setForeground(Color.RED);
        colunas.setMaximumSize(new Dimension(LARGURA, 30));   // não esticar no BoxLayout

        // espaço vazio no topo, onde o paintComponent pinta a cor do jogador
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

    // no BoxLayout, todos precisam do mesmo alinhamento para ficarem na mesma margem
    private void adicionar(JComponent comp) {
        comp.setAlignmentX(Component.LEFT_ALIGNMENT);
        add(comp);
    }

    // ---------- usados pela JanelaJogo ----------

    JButton getBtnAtivar() {
        return ativar;
    }

    JButton getBtnEncerrar() {
        return encerrar;
    }

    JComboBox<String> getComboColunas() {
        return colunas;
    }

    void atualizar() {
        int vez = modelo.jogadorDaVez();
        lblJogador.setText("Jogador " + (vez + 1) + " (" + modelo.corDoJogador(vez) + ")");
        lblMovimentos.setText("Movimentos restantes: " + modelo.movimentosRestantes());
        repaint();
    }

    void mostrarAviso(String texto) {
        lblAviso.setText(texto);
    }

    void limparAviso() {
        lblAviso.setText(" ");
    }

    // indicador obrigatório: retângulo na cor do jogador da vez
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        String cor = modelo.corDoJogador(modelo.jogadorDaVez());

        int largura = getWidth() - 2 * MARGEM;
        g2.setColor(PainelTabuleiro.converterCor(cor));
        g2.fillRect(MARGEM, MARGEM, largura, ALTURA_INDICADOR);
        g2.setColor(Color.BLACK);
        g2.drawRect(MARGEM, MARGEM, largura, ALTURA_INDICADOR);
    }
}