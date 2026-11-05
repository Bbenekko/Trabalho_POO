package view;

import javax.swing.*;
import java.awt.*;
import java.util.List;

import controller.ControladorJogo;
import model.ModelFacade;
import model.ObservadoIF;
import model.ObservadorIF;

// Lateral com o estado do jogador da vez e os controles do turno. É um observador (Cap. 17).
// Os listeners dos botões são registrados pelo Controller.
class PainelControles extends JPanel implements ObservadorIF {
    static final int LARGURA = 300;
    private static final int ALTURA_INDICADOR = 40;
    private static final int MARGEM = 12;

    private ModelFacade modelo = ModelFacade.getInstance();

    private JLabel lblJogador = new JLabel();
    private JLabel lblTurno = new JLabel();
    private JLabel lblRecursos = new JLabel();
    private JLabel lblTesouros = new JLabel();
    private JLabel lblVotos = new JLabel();
    private JLabel lblKhan = new JLabel();

    private JComboBox<String> colunas = new JComboBox<String>();
    private JButton ativar = new JButton("Ativar coluna");
    private JComboBox<String> modos = new JComboBox<String>();
    private JButton khanMelhorias = new JButton("Khan nas melhorias");
    private JButton encerrar = new JButton("Encerrar turno");

    private JComboBox<String> melhorias = new JComboBox<String>();
    private JComboBox<String> colunaDestino = new JComboBox<String>();
    private JButton comprar = new JButton("Comprar (2 moedas)");
    private JButton gratis = new JButton("Pegar grátis");

    private JButton verCartela = new JButton("Ver minha cartela");
    private JButton verCartas = new JButton("Ver cartas de conselheiro");
    private JLabel lblAviso = new JLabel(" ");

    PainelControles() {
        setPreferredSize(new Dimension(LARGURA, 300));
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createEmptyBorder(MARGEM, MARGEM, MARGEM, MARGEM));

        // mesma ordem das constantes MOVER, TRIBUTO, KHAN, ATACAR e YURT do ControladorJogo
        modos.addItem("Clique move o peão");
        modos.addItem("Clique pega tributo");
        modos.addItem("Clique leva o Khan");
        modos.addItem("Clique ataca cidade");
        modos.addItem("Clique constrói yurt");

        lblJogador.setFont(new Font("SansSerif", Font.BOLD, 15));
        lblAviso.setForeground(new Color(180, 0, 0));

        add(Box.createRigidArea(new Dimension(0, ALTURA_INDICADOR + 8)));
        adicionar(lblJogador);
        adicionar(lblTurno);
        adicionar(lblRecursos);
        adicionar(lblTesouros);
        adicionar(lblVotos);
        adicionar(lblKhan);

        adicionar(titulo("Turno"));
        adicionar(colunas);
        adicionar(ativar);
        adicionar(espaco(4));
        adicionar(modos);
        adicionar(espaco(4));
        adicionar(khanMelhorias);
        adicionar(encerrar);

        adicionar(titulo("Melhorias expostas"));
        adicionar(melhorias);
        adicionar(colunaDestino);
        adicionar(comprar);
        adicionar(gratis);

        adicionar(titulo("Consultas"));
        adicionar(verCartela);
        adicionar(verCartas);
        adicionar(espaco(10));
        adicionar(lblAviso);

        atualizar();
        ControladorJogo.getInstance().registra(this);   // Cap. 17: registro pelo controlador
    }

    private JLabel titulo(String texto) {
        JLabel l = new JLabel(texto);
        l.setFont(new Font("SansSerif", Font.BOLD, 13));
        l.setBorder(BorderFactory.createEmptyBorder(12, 0, 4, 0));
        return l;
    }

    private Component espaco(int altura) {
        return Box.createRigidArea(new Dimension(0, altura));
    }

    // no BoxLayout, todos com o mesmo alinhamento e sem esticar na altura
    private void adicionar(Component comp) {
        if (comp instanceof JComponent) {
            JComponent j = (JComponent) comp;
            j.setAlignmentX(Component.LEFT_ALIGNMENT);
            if (j instanceof JComboBox || j instanceof JButton)
                j.setMaximumSize(new Dimension(LARGURA - 2 * MARGEM, 28));
        }
        add(comp);
    }

    // ---------- OBSERVER ----------

    @Override
    public void notify(ObservadoIF o) {
        int evento = o.get(ModelFacade.EVENTO);
        if (evento == ModelFacade.ABRIR_CARTELA || evento == ModelFacade.ABRIR_CARTAS)
            return;
        limparAviso();
        atualizar();
    }

    void atualizar() {
        if (!modelo.jogoIniciado())
            return;
        int vez = modelo.jogadorDaVez();
        texto(lblJogador, "Jogador " + (vez + 1) + " · " + nome(modelo.herdeiroDoJogador(vez))
                + " (" + modelo.corDoJogador(vez) + ")");

        if (modelo.jogoTerminou())
            texto(lblTurno, "Partida encerrada");
        else if (modelo.turnoEmAndamento())
            texto(lblTurno, "Movimentos: " + modelo.movimentosRestantes()
                    + " · Tributos: " + modelo.tributosRestantes()
                    + (modelo.podeEncerrarTurno() ? "" : " · usar Khan!"));
        else
            texto(lblTurno, "Escolha uma coluna para começar o turno");

        texto(lblRecursos, "Moedas " + modelo.tributosDoJogador(vez, "moeda")
                + " · Espadas " + modelo.tributosDoJogador(vez, "espada")
                + " · Fichas de yurt " + modelo.tributosDoJogador(vez, "yurt"));

        int tesouros = 0;
        for (String t : new String[] {"pele", "tecido", "gado", "ferro", "especiarias"})
            tesouros += modelo.tesourosDoJogador(vez, t);
        texto(lblTesouros, "Tesouros " + tesouros + " · Yurts na reserva " + modelo.yurtsRestantes(vez));

        texto(lblVotos, "Votos " + modelo.votosDe(vez) + " · Kurultai " + modelo.fichaNeutra()
                + "/" + modelo.limiteKurultai() + (modelo.fimDisparado() ? " · última rodada" : ""));

        int prov = modelo.provinciaDoKhan();
        texto(lblKhan, "Khan: " + (modelo.khanNasMelhorias() ? "área de melhorias"
                : (prov == -1 ? "fora do mapa" : "província " + prov)));

        colunas.removeAllItems();
        colunaDestino.removeAllItems();
        for (int c = 0; c < 4; c++) {
            String regiao = Desenhos.nomeDaRegiao(modelo.regiaoDaColuna(vez, c));
            colunas.addItem("Coluna " + (c + 1) + " · " + regiao + (modelo.colunaAtivada(vez, c) ? " (usada)" : ""));
            if (c < 3)
                colunaDestino.addItem("Para a coluna " + (c + 1) + " · " + regiao);
        }

        melhorias.removeAllItems();
        List<String> expostas = modelo.melhoriasExpostas();
        for (String m : expostas) {
            String[] partes = m.split(":");
            melhorias.addItem(Desenhos.nomeDaMelhoria(m) + " · " + Desenhos.nomeDaRegiao(partes[1]));
        }

        boolean emTurno = modelo.turnoEmAndamento();
        boolean fim = modelo.jogoTerminou();
        ativar.setEnabled(!emTurno && !fim);
        encerrar.setEnabled(emTurno && !fim);
        khanMelhorias.setEnabled(emTurno && !modelo.khanUsado() && !fim);
        comprar.setEnabled(emTurno && !expostas.isEmpty() && !fim);
        gratis.setEnabled(emTurno && modelo.melhoriaGratisDisponivel() && !fim);
        repaint();
    }

    private static String nome(String herdeiro) {
        return Character.toUpperCase(herdeiro.charAt(0)) + herdeiro.substring(1);
    }

    // JLabel com quebra de linha na largura da lateral
    private static void texto(JLabel l, String s) {
        l.setText("<html><div style='width:" + (LARGURA - 3 * MARGEM) + "px'>" + s + "</div></html>");
    }

    void mostrarAviso(String texto) {
        lblAviso.setText("<html><div style='width:" + (LARGURA - 3 * MARGEM) + "px'>" + texto + "</div></html>");
    }

    void limparAviso() {
        lblAviso.setText(" ");
    }

    // indicador obrigatório: faixa na cor do jogador da vez
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (!modelo.jogoIniciado())
            return;
        Graphics2D g2 = (Graphics2D) g;
        int largura = getWidth() - 2 * MARGEM;
        g2.setColor(Desenhos.cor(modelo.corDoJogador(modelo.jogadorDaVez())));
        g2.fillRect(MARGEM, MARGEM, largura, ALTURA_INDICADOR);
        g2.setColor(Color.BLACK);
        g2.drawRect(MARGEM, MARGEM, largura, ALTURA_INDICADOR);
    }

    // ---------- usados pela JanelaJogo ----------

    JButton getBtnAtivar() { return ativar; }
    JButton getBtnEncerrar() { return encerrar; }
    JButton getBtnKhanMelhorias() { return khanMelhorias; }
    JButton getBtnComprar() { return comprar; }
    JButton getBtnMelhoriaGratis() { return gratis; }
    JButton getBtnVerCartela() { return verCartela; }
    JButton getBtnVerCartas() { return verCartas; }
    JComboBox<String> getComboColunas() { return colunas; }
    JComboBox<String> getComboModo() { return modos; }
    JComboBox<String> getComboMelhorias() { return melhorias; }
    JComboBox<String> getComboColunaDestino() { return colunaDestino; }
}