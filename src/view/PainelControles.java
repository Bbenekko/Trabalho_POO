package view;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import controller.ControladorJogo;
import model.ModelFacade;
import model.ObservadoIF;
import model.ObservadorIF;

class PainelControles extends JPanel implements ObservadorIF {
    static final int LARGURA = 300;
    private static final int ALTURA_INDICADOR = 40;
    private static final int MARGEM = 12;
    private static final int LARGURA_LINHA = LARGURA - 2 * MARGEM;
    private static final int PECA_PEQUENA = 18;
    private static final Color COR_ALERTA = new Color(180, 0, 0);
    private static final Color COR_DICA = new Color(110, 110, 110);
    private static final String[] TRIBUTOS = {"moeda", "espada", "yurt"};
    private static final String[] TESOUROS = {"pele", "tecido", "gado", "ferro", "especiarias"};

    // ícones da lateral: os mesmos arquivos do tabuleiro (sem arquivo, usa o desenho gerado)
    private static final String IMG_MOEDA = "doc/img/cartoes/cartoes6/cartoes6-3.png";
    private static final String IMG_ESPADA = "doc/img/cartoes/cartoes6/cartoes6-2.png";
    private static final String IMG_YURT = "doc/img/cartoes/cartoes6/cartoes6-1.png";
    private static final String IMG_PELE = "doc/img/cartoes/cartoes7/cartoes7-4.png";
    private static final String IMG_TECIDO = "doc/img/cartoes/cartoes7/cartoes7-2.png";
    private static final String IMG_GADO = "doc/img/cartoes/cartoes7/cartoes7-1.png";
    private static final String IMG_FERRO = "doc/img/cartoes/cartoes7/cartoes7-3.png";
    private static final String IMG_ESPECIARIAS = "doc/img/cartoes/cartoes7/cartoes7-5.png";
    private static final String IMG_MOVIMENTOS = "doc/img/cartoes/cartoes3/cartoes3-1.jpg";   // cavalos
    private static final String IMG_ACOES_TRIBUTO = "doc/img/cartoes/cartoes3/cartoes3-6.jpg"; // mãos
    private static final String IMG_VOTOS = "doc/img/cartoes/cartoes3/cartoes3-2.jpg";        // cadeira
    private static final String IMG_BONUS_CAVALOS = "doc/img/cartoes/cartoes3/cartoes3-1.jpg";
    private static final String IMG_BONUS_VOTO = "doc/img/cartoes/cartoes3/cartoes3-2.jpg";
    private static final String IMG_BONUS_YURTS = "doc/img/cartoes/cartoes3/cartoes3-3.jpg";
    private static final String IMG_BONUS_ESPADAS = "doc/img/cartoes/cartoes3/cartoes3-4.jpg";
    private static final String IMG_BONUS_TESOURO = "doc/img/cartoes/cartoes3/cartoes3-5.jpg";
    private static final String IMG_BONUS_MAOS = "doc/img/cartoes/cartoes3/cartoes3-6.jpg";
    private static final String IMG_BONUS_MOEDAS = "doc/img/cartoes/cartoes3/cartoes3-7.jpg";

    private ModelFacade modelo = ModelFacade.getInstance();
    private Map<String, Image> icones = new HashMap<String, Image>();

    private JLabel lblJogador = new JLabel();

    // recursos do jogador (ficam de um turno para o outro)
    private LinhaIcones linhaTributos = new LinhaIcones(LARGURA_LINHA);
    private LinhaIcones linhaTesouros = new LinhaIcones(LARGURA_LINHA);
    private LinhaIcones linhaReserva = new LinhaIcones(LARGURA_LINHA);
    // só valem no turno em andamento
    private LinhaIcones linhaAcoes = new LinhaIcones(LARGURA_LINHA);
    private LinhaIcones linhaSituacao = new LinhaIcones(LARGURA_LINHA);
    // informações gerais da partida
    private LinhaIcones linhaRodada = new LinhaIcones(LARGURA_LINHA);
    private LinhaIcones linhaVotos = new LinhaIcones(LARGURA_LINHA);
    private LinhaIcones linhaFim = new LinhaIcones(LARGURA_LINHA);
    private LinhaIcones linhaKhan = new LinhaIcones(LARGURA_LINHA);

    private JComboBox<String> colunas = new JComboBox<String>();
    private JButton ativar = new JButton("Ativar");
    private JPanel linhaBonus;                 // combo + "Usar"; só aparece se houver peça guardada
    private JComboBox<String> modos = new JComboBox<String>();
    private JButton khanMelhorias = new JButton("Khan → melhorias");
    private JButton encerrar = new JButton("Encerrar turno");

    private JComboBox<String> melhorias = new JComboBox<String>();
    private JComboBox<String> colunaDestino = new JComboBox<String>();
    private JButton comprar = new JButton("Comprar");
    private JButton comprarPilha = new JButton("Da pilha");
    private JComboBox<String> bonus = new JComboBox<String>();
    private JButton usarBonus = new JButton("Usar");
    private JButton trocarEspada = new JButton("Chagatai: 1 movimento → 1 espada");   // só aparece para Chagatai
    private java.util.List<Integer> indicesBonus = new java.util.ArrayList<Integer>();   // item do combo → peça do jogador
    private JButton gratis = new JButton("Grátis");

    private JButton verCartela = new JButton("Cartela");
    private JButton verCartas = new JButton("Cartas");
    private JButton salvar = new JButton("Salvar...");
    private JLabel lblAviso = new JLabel(" ");

    PainelControles() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createEmptyBorder(MARGEM, MARGEM, MARGEM, MARGEM));

        modos.addItem("Clique move o peão");
        modos.addItem("Clique pega tributo");
        modos.addItem("Clique leva o Khan");
        modos.addItem("Clique ataca cidade");
        modos.addItem("Clique constrói yurt");
        modos.addItem("Clique entrega tesouros");

        carregarIcones();
        lblJogador.setFont(new Font("SansSerif", Font.BOLD, 15));
        lblAviso.setForeground(new Color(180, 0, 0));
        lblAviso.setVerticalAlignment(SwingConstants.TOP);

        add(Box.createRigidArea(new Dimension(0, ALTURA_INDICADOR + 8)));
        adicionar(lblJogador);

        adicionar(secao("Recursos", null));
        adicionar(linhaTributos);
        adicionar(linhaTesouros);
        adicionar(linhaReserva);

        adicionar(secao("Neste turno", "somem ao encerrar"));
        adicionar(linhaAcoes);
        adicionar(linhaSituacao);
        linhaBonus = comBotao(bonus, usarBonus);
        adicionar(linhaBonus);
        trocarEspada.setFont(new Font("SansSerif", Font.BOLD, 12));
        trocarEspada.setMargin(new Insets(2, 2, 2, 2));
        trocarEspada.setToolTipText("1x por turno: deixa de usar 1 cavalo (movimento) para ganhar 1 espada");
        adicionar(trocarEspada);

        adicionar(secao("Partida", null));
        adicionar(linhaRodada);
        adicionar(linhaVotos);
        adicionar(linhaFim);
        adicionar(linhaKhan);

        // botões do mesmo grupo lado a lado, para a lateral caber em telas mais baixas
        adicionar(titulo("Turno"));
        adicionar(comBotao(colunas, ativar));
        adicionar(espaco(3));
        adicionar(modos);
        adicionar(espaco(3));
        adicionar(lado(khanMelhorias, encerrar));

        comprar.setToolTipText("Custa 2 moedas; a melhoria escolhida vai para a coluna escolhida acima");
        comprarPilha.setToolTipText("Custa 2 moedas; compra às cegas a do topo da pilha, que vai para a coluna da região dela");
        gratis.setToolTipText("Pegar a melhoria grátis da linha completa (vai para a coluna branca)");
        verCartela.setToolTipText("Ver a cartela do jogador da vez");
        verCartas.setToolTipText("Ver as cartas de conselheiro abertas e a sua secreta");
        salvar.setToolTipText("Salvar a partida (só antes de o jogador da vez começar)");
        usarBonus.setToolTipText("Usa a peça de bônus escolhida (uma vez só); ela continua contando para as cartas");
        adicionar(titulo("Melhorias expostas (compra: 2 moedas)"));
        adicionar(melhorias);
        adicionar(colunaDestino);
        adicionar(lado(comprar, comprarPilha, gratis));

        adicionar(titulo("Consultas e partida"));
        adicionar(lado(verCartela, verCartas, salvar));
        adicionar(espaco(6));
        adicionar(lblAviso);
        add(Box.createVerticalGlue());

        atualizar();
        ControladorJogo.getInstance().registra(this);
    }

    private JLabel titulo(String texto) {
        JLabel l = new JLabel(texto);
        l.setFont(new Font("SansSerif", Font.BOLD, 13));
        l.setBorder(BorderFactory.createEmptyBorder(8, 0, 3, 0));
        return l;
    }

    // combo ocupando a largura e um botão pequeno à direita
    private JPanel comBotao(JComboBox<String> combo, JButton botao) {
        JPanel p = new JPanel(new BorderLayout(6, 0));
        p.setOpaque(false);
        botao.setMargin(new Insets(2, 6, 2, 6));
        botao.setFont(new Font("SansSerif", Font.BOLD, 12));
        p.add(combo, BorderLayout.CENTER);
        p.add(botao, BorderLayout.EAST);
        p.setMaximumSize(new Dimension(LARGURA_LINHA, 28));
        return p;
    }

    // botões dividindo a largura da lateral
    private JPanel lado(JButton... botoes) {
        JPanel p = new JPanel(new GridLayout(1, botoes.length, 6, 0));
        p.setOpaque(false);
        for (JButton x : botoes) {
            x.setMargin(new Insets(2, 2, 2, 2));
            x.setFont(new Font("SansSerif", Font.BOLD, 12));   // tamanho fixo: cabe igual em qualquer sistema
            p.add(x);
        }
        p.setMaximumSize(new Dimension(LARGURA_LINHA, 28));
        return p;
    }

    // título de bloco com uma linha embaixo e, se houver, uma observação em cinza ao lado
    private JLabel secao(String texto, String observacao) {
        String html = "<html><b>" + texto + "</b>"
                + (observacao == null ? "" : " <font color='#6e6e6e'>(" + observacao + ")</font>") + "</html>";
        JLabel l = new JLabel(html);
        l.setFont(new Font("SansSerif", Font.PLAIN, 13));
        l.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(6, 0, 2, 0),
                BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(170, 170, 170))));
        l.setMaximumSize(new Dimension(LARGURA_LINHA, 30));
        return l;
    }

    // ---------- ÍCONES ----------

    private void carregarIcones() {
        int t = LinhaIcones.ICONE;
        icones.put("moeda", icone(IMG_MOEDA, t, Desenhos.tributo("moeda", t)));
        icones.put("espada", icone(IMG_ESPADA, t, Desenhos.tributo("espada", t)));
        icones.put("yurt", icone(IMG_YURT, t, Desenhos.tributo("yurt", t)));
        icones.put("pele", icone(IMG_PELE, t, Desenhos.tesouro("pele", t)));
        icones.put("tecido", icone(IMG_TECIDO, t, Desenhos.tesouro("tecido", t)));
        icones.put("gado", icone(IMG_GADO, t, Desenhos.tesouro("gado", t)));
        icones.put("ferro", icone(IMG_FERRO, t, Desenhos.tesouro("ferro", t)));
        icones.put("especiarias", icone(IMG_ESPECIARIAS, t, Desenhos.tesouro("especiarias", t)));
        icones.put("movimentos", icone(IMG_MOVIMENTOS, t, null));
        icones.put("acoes_tributo", icone(IMG_ACOES_TRIBUTO, t, null));
        icones.put("votos", icone(IMG_VOTOS, t, null));
        icones.put("khan", Desenhos.peao("branco", t - 4));
        icones.put("ficha_neutra", Desenhos.yurt("branco", t - 6));
        icones.put("bonus:cavalos", icone(IMG_BONUS_CAVALOS, PECA_PEQUENA, null));
        icones.put("bonus:voto", icone(IMG_BONUS_VOTO, PECA_PEQUENA, null));
        icones.put("bonus:yurts", icone(IMG_BONUS_YURTS, PECA_PEQUENA, null));
        icones.put("bonus:espadas", icone(IMG_BONUS_ESPADAS, PECA_PEQUENA, null));
        icones.put("bonus:tesouro", icone(IMG_BONUS_TESOURO, PECA_PEQUENA, null));
        icones.put("bonus:maos", icone(IMG_BONUS_MAOS, PECA_PEQUENA, null));
        icones.put("bonus:moedas", icone(IMG_BONUS_MOEDAS, PECA_PEQUENA, null));
    }

    // arquivo reduzido mantendo a proporção; sem arquivo, devolve a reserva (que pode ser null)
    private static Image icone(String caminho, int tamanho, Image reserva) {
        Image original = Imagens.carregar(caminho);
        if (original == null)
            return reserva;
        int w = original.getWidth(null), h = original.getHeight(null);
        int lw = (w >= h) ? tamanho : tamanho * w / h;
        int lh = (h >= w) ? tamanho : tamanho * h / w;
        return new ImageIcon(original.getScaledInstance(lw, lh, Image.SCALE_SMOOTH)).getImage();
    }

    private Component espaco(int altura) {
        return Box.createRigidArea(new Dimension(0, altura));
    }

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
        texto(lblJogador, modelo.nomeDoJogador(vez) + " · " + nome(modelo.herdeiroDoJogador(vez))
                + " (" + modelo.corDoJogador(vez) + ")");

        atualizarRecursos(vez);
        atualizarTurno();
        atualizarPartida();

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
        comprarPilha.setEnabled(emTurno && modelo.melhoriasNaPilha() > 0 && !fim);
        usarBonus.setEnabled(emTurno && !indicesBonus.isEmpty() && !fim);
        trocarEspada.setVisible(emTurno && !fim && modelo.herdeiroDoJogador(modelo.jogadorDaVez()).equals("chagatai"));
        trocarEspada.setEnabled(modelo.turnoEmAndamento() && modelo.podeTrocarMovimentoPorEspada());
        bonus.setEnabled(!indicesBonus.isEmpty());
        // 4ª iteração: só dá para salvar imediatamente antes de o jogador da vez começar a jogar
        salvar.setEnabled(modelo.podeSalvar());
        revalidate();
        repaint();
    }

    // largura fixa; a altura é a que o BoxLayout precisa (a JanelaJogo põe a lateral numa rolagem)
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(LARGURA, getLayout().preferredLayoutSize(this).height);
    }

    // ---------- BLOCOS DE INFORMAÇÃO ----------

    // o que o jogador tem guardado: tributos, tesouros, yurts que ainda pode construir e peças de bônus
    private void atualizarRecursos(int vez) {
        linhaTributos.limpar();
        linhaTributos.item(icones.get("moeda"), qtd(modelo.tributosDoJogador(vez, "moeda"), "moeda", "moedas"));
        linhaTributos.item(icones.get("espada"), qtd(modelo.tributosDoJogador(vez, "espada"), "espada", "espadas"));
        linhaTributos.item(icones.get("yurt"), qtd(modelo.tributosDoJogador(vez, "yurt"), "ficha", "fichas"));

        linhaTesouros.limpar();
        for (String t : TESOUROS)
            linhaTesouros.item(icones.get(t), String.valueOf(modelo.tesourosDoJogador(vez, t)));

        linhaReserva.limpar();
        linhaReserva.item(Desenhos.yurt(modelo.corDoJogador(vez), LinhaIcones.ICONE - 4),
                qtd(modelo.yurtsRestantes(vez), "yurt", "yurts") + " na reserva");
        List<String> pecas = modelo.pecasBonusDoJogador(vez);
        linhaReserva.texto("Bônus:", Color.BLACK);
        if (pecas.isEmpty())
            linhaReserva.texto("nenhum", COR_DICA);
        for (int i = 0; i < pecas.size(); i++) {
            Image icone = icones.get("bonus:" + pecas.get(i));
            if (modelo.pecaBonusUsada(vez, i))
                linhaReserva.itemApagado(icone, "");       // já usada: aparece clarinha
            else
                linhaReserva.item(icone, "");
        }

        // peças guardadas que ainda podem ser usadas
        bonus.removeAllItems();
        indicesBonus.clear();
        for (int i = 0; i < pecas.size(); i++) {
            if (modelo.pecaBonusUsada(vez, i))
                continue;
            indicesBonus.add(i);
            bonus.addItem(descricaoBonus(pecas.get(i)));
        }
        if (indicesBonus.isEmpty())
            bonus.addItem("Nenhuma peça de bônus guardada");
        linhaBonus.setVisible(!indicesBonus.isEmpty());     // sem peça guardada, a linha nem aparece
    }

    private static String descricaoBonus(String peca) {
        switch (peca) {
            case "cavalos": return "Cavalos: +3 mov. e +1 tributo";
            case "maos":    return "Mãos: +2 tributos";
            case "voto":    return "Voto: +1 voto";
            case "yurts":   return "Yurts: +2 fichas de yurt";
            case "espadas": return "Espadas: +2 espadas";
            case "moedas":  return "Moedas: +2 moedas";
            case "tesouro": return "Tesouro: +1 tesouro";
            default:        return peca;
        }
    }

    // índice (na lista de peças do jogador) da peça escolhida no combo, ou -1
    int bonusEscolhido() {
        int i = bonus.getSelectedIndex();
        return (i < 0 || i >= indicesBonus.size()) ? -1 : indicesBonus.get(i);
    }

    // vale só até o fim do turno: movimentos, ações de tributo e recursos virtuais da coluna ativada
    private void atualizarTurno() {
        linhaAcoes.limpar();
        linhaSituacao.limpar();
        if (modelo.jogoTerminou()) {
            linhaSituacao.texto("Partida encerrada", COR_ALERTA);
            return;
        }
        if (!modelo.turnoEmAndamento()) {
            linhaAcoes.texto("Escolha uma coluna para começar", COR_DICA);
            return;
        }
        linhaAcoes.item(icones.get("movimentos"), qtd(modelo.movimentosRestantes(), "movimento", "movimentos"));
        linhaAcoes.item(icones.get("acoes_tributo"), qtd(modelo.tributosRestantes(), "tributo", "tributos"));
        // recursos virtuais da coluna: ícone com anel roxo tracejado e a quantidade
        for (String t : TRIBUTOS) {
            int n = modelo.virtuaisNoTurno(t);
            if (n > 0)
                linhaAcoes.itemVirtual(icones.get(t), String.valueOf(n));
        }

        if (modelo.precisaEscolherPeao())
            linhaSituacao.texto("Escolha o peão (clique nele)", COR_ALERTA);
        else if (modelo.numeroDePeoes(modelo.jogadorDaVez()) > 1)
            linhaSituacao.texto("Peão " + (modelo.peaoDoTurno() + 1) + " age neste turno", COR_DICA);
        if (!modelo.podeEncerrarTurno())
            linhaSituacao.texto("Use o Khan!", COR_ALERTA);
        else if (modelo.khanUsado())
            linhaSituacao.texto("Khan já usado neste turno", COR_DICA);
    }

    // informações gerais: rodada, votos do jogador, votos de todos no Kurultai, fim de jogo e Khan
    private void atualizarPartida() {
        int vez = modelo.jogadorDaVez();
        int n = modelo.numeroDeJogadores();
        linhaRodada.limpar();
        linhaRodada.texto("Rodada " + modelo.rodadaAtual() + " · vez " + (vez + 1) + " de " + n, Color.BLACK);
        if (modelo.fimDisparado() && !modelo.jogoTerminou())
            linhaRodada.texto("ÚLTIMA RODADA", COR_ALERTA);

        // seus votos e, com a ficha branca, a soma dos votos de todos no Kurultai
        linhaVotos.limpar();
        linhaVotos.item(icones.get("votos"), "Seus votos: " + modelo.votosDe(vez));
        linhaVotos.item(icones.get("ficha_neutra"), "Todos: " + modelo.fichaNeutra());

        // quando a ficha branca chega ao marco do nº de jogadores, começa a última rodada
        linhaFim.limpar();
        int faltam = modelo.votosParaOFim();
        String fim = "Fim em " + modelo.limiteKurultai() + " votos: ";
        if (modelo.jogoTerminou())
            linhaFim.texto("Partida encerrada", COR_ALERTA);
        else if (faltam > 0)
            linhaFim.texto(fim + "faltam " + faltam, COR_DICA);
        else
            linhaFim.texto(fim + "limite atingido", COR_ALERTA);

        int prov = modelo.provinciaDoKhan();
        linhaKhan.limpar();
        linhaKhan.item(icones.get("khan"), "Khan: " + (modelo.khanNasMelhorias() ? "área de melhorias"
                : (prov == -1 ? "fora do mapa" : "província " + (char) ('a' + prov))));
    }

    private static String qtd(int n, String singular, String plural) {
        return n + " " + (n == 1 ? singular : plural);
    }

    private static String nome(String herdeiro) {
        return Character.toUpperCase(herdeiro.charAt(0)) + herdeiro.substring(1);
    }

    private static void texto(JLabel l, String s) {
        l.setText("<html><div style='width:" + (LARGURA - 3 * MARGEM) + "px'>" + s + "</div></html>");
    }

    void mostrarAviso(String texto) {
        lblAviso.setText("<html>" + quebrar(texto) + "</html>");
    }

    // quebra o aviso em linhas que cabem na lateral (o HTML do JLabel não respeita a largura do div)
    private String quebrar(String texto) {
        FontMetrics fm = lblAviso.getFontMetrics(lblAviso.getFont());
        int maximo = LARGURA - 3 * MARGEM;
        StringBuilder sb = new StringBuilder();
        StringBuilder linha = new StringBuilder();
        for (String palavra : texto.split(" ")) {
            String tentativa = (linha.length() == 0) ? palavra : linha + " " + palavra;
            if (linha.length() > 0 && fm.stringWidth(tentativa) > maximo) {
                sb.append(linha).append("<br>");
                linha = new StringBuilder(palavra);
            } else {
                linha = new StringBuilder(tentativa);
            }
        }
        return sb.append(linha).toString();
    }

    void limparAviso() {
        lblAviso.setText(" ");
    }

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
    JButton getBtnComprarPilha() { return comprarPilha; }
    JButton getBtnUsarBonus() { return usarBonus; }
    JButton getBtnTrocarEspada() { return trocarEspada; }
    JButton getBtnVerCartela() { return verCartela; }
    JButton getBtnVerCartas() { return verCartas; }
    JButton getBtnSalvar() { return salvar; }
    JComboBox<String> getComboColunas() { return colunas; }
    JComboBox<String> getComboModo() { return modos; }
    JComboBox<String> getComboMelhorias() { return melhorias; }
    JComboBox<String> getComboColunaDestino() { return colunaDestino; }
}