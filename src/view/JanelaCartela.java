package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import controller.ControladorJogo;
import model.ModelFacade;
import model.ObservadoIF;
import model.ObservadorIF;

// Cartela (bloco de notas) de um jogador: a imagem da cartela do herdeiro, as melhorias compradas
// em cada espaço e o marcador da cor do jogador sobre as colunas já ativadas.
// É um observador (Cap. 17): abre quando a fachada notifica ABRIR_CARTELA e se redesenha a cada mudança.
// As posições estão em pixels da imagem original da cartela e foram medidas com MODO_MEDICAO.
public class JanelaCartela extends JFrame implements ObservadorIF {
    // tamanho na tela
    private static final int LARGURA_CARTELA = 980;
    private static final int MARGEM = 16;
    private static final int LARGURA_CARTA = 140;
    private static final int ALTURA_RECURSOS = 230;
    private static final int ICONE = 36;          // tributos e tesouros na área de recursos
    private static final int PECA_BONUS = 64;     // peças de bônus ganhas

    // tamanhos em pixels da imagem original da cartela
    private static final int MELHORIA_IMAGEM = 880;          // um espaço de melhoria tem ~890 x 460
    private static final int MARCADOR_IMAGEM = 420;
    private static final int LARGURA_ORIGINAL = 5940;        // usado só para o tamanho da janela

    // imagens das cartelas, uma por herdeiro (ajuste os caminhos aos arquivos do projeto)
    private static final String IMG_CARTELA_ALTANI = "doc/img/cartelas/cartela1.jpg";
    private static final String IMG_CARTELA_JOCHI = "doc/img/cartelas/cartela2.jpg";
    private static final String IMG_CARTELA_CHAGATAI = "doc/img/cartelas/cartela3.jpg";
    private static final String IMG_CARTELA_TOLUI = "doc/img/cartelas/cartela4.jpg";
    private static final String IMG_CARTELA_OGEDEI = "doc/img/cartelas/cartela5.jpg";

    // marcador colocado sobre a coluna ativada, um por cor de jogador
    private static final String IMG_MARCADOR_AZUL = "doc/img/cartoes/cartoes4/cartoes4-1.jpg";
    private static final String IMG_MARCADOR_AMARELO = "doc/img/cartoes/cartoes4/cartoes4-2.jpg";
    private static final String IMG_MARCADOR_PRETO = "doc/img/cartoes/cartoes4/cartoes4-3.jpg";
    private static final String IMG_MARCADOR_VERMELHO = "doc/img/cartoes/cartoes4/cartoes4-4.jpg";
    private static final String IMG_MARCADOR_VERDE = "doc/img/cartoes/cartoes4/cartoes4-5.jpg";

    // imagens das melhorias (mesmos arquivos do mapa; sem arquivo, usa a peça gerada)
    private static final String IMG_MELHORIA_TRIBUTO_RUSSIA = "doc/img/cartoes/cartoes1/MAO-A.jpg";
    private static final String IMG_MELHORIA_TRIBUTO_PERSIA = "doc/img/cartoes/cartoes1/MAO-V.jpg";
    private static final String IMG_MELHORIA_TRIBUTO_CHINA = "doc/img/cartoes/cartoes1/MAO-R.jpg";
    private static final String IMG_MELHORIA_MOVIMENTOS_RUSSIA = "doc/img/cartoes/cartoes1/CAV-A.jpg";
    private static final String IMG_MELHORIA_MOVIMENTOS_PERSIA = "doc/img/cartoes/cartoes1/CAV-V.jpg";
    private static final String IMG_MELHORIA_MOVIMENTOS_CHINA = "doc/img/cartoes/cartoes1/CAV-R.jpg";
    private static final String IMG_MELHORIA_ESPADA_RUSSIA = "doc/img/cartoes/cartoes1/ESP-A.jpg";
    private static final String IMG_MELHORIA_ESPADA_PERSIA = "doc/img/cartoes/cartoes1/ESP-V.jpg";
    private static final String IMG_MELHORIA_ESPADA_CHINA = "doc/img/cartoes/cartoes1/ESP-R.jpg";
    private static final String IMG_MELHORIA_YURT_RUSSIA = "doc/img/cartoes/cartoes1/CAS-A.jpg";
    private static final String IMG_MELHORIA_YURT_PERSIA = "doc/img/cartoes/cartoes1/CAS-V.jpg";
    private static final String IMG_MELHORIA_YURT_CHINA = "doc/img/cartoes/cartoes1/CAS-R.jpg";
    private static final String IMG_MELHORIA_MOEDA_RUSSIA = "doc/img/cartoes/cartoes1/MOE-A.jpg";
    private static final String IMG_MELHORIA_MOEDA_PERSIA = "doc/img/cartoes/cartoes1/MOE-V.jpg";
    private static final String IMG_MELHORIA_MOEDA_CHINA = "doc/img/cartoes/cartoes1/MOE-R.jpg";
    private static final String IMG_MELHORIA_TESOURO_CHINA_FERRO = "doc/img/cartoes/cartoes1/MOB-R.jpg";
    private static final String IMG_MELHORIA_TESOURO_RUSSIA_GADO = "doc/img/cartoes/cartoes1/BOI-A.jpg";
    private static final String IMG_MELHORIA_TESOURO_RUSSIA_ESPECIARIAS = "doc/img/cartoes/cartoes1/CUI-A.jpg";
    private static final String IMG_MELHORIA_TESOURO_PERSIA_TECIDO = "doc/img/cartoes/cartoes1/PER-V.jpg";
    private static final String IMG_MELHORIA_TESOURO_PERSIA_PELE = "doc/img/cartoes/cartoes1/MOP-V.jpg";

    // ícones dos recursos: os mesmos arquivos usados no tabuleiro (sem arquivo, usa o desenho gerado)
    private static final String IMG_MOEDA = "doc/img/cartoes/cartoes6/cartoes6-3.png";
    private static final String IMG_ESPADA = "doc/img/cartoes/cartoes6/cartoes6-2.png";
    private static final String IMG_YURT = "doc/img/cartoes/cartoes6/cartoes6-1.png";
    private static final String IMG_PELE = "doc/img/cartoes/cartoes7/cartoes7-4.png";
    private static final String IMG_TECIDO = "doc/img/cartoes/cartoes7/cartoes7-2.png";
    private static final String IMG_GADO = "doc/img/cartoes/cartoes7/cartoes7-1.png";
    private static final String IMG_FERRO = "doc/img/cartoes/cartoes7/cartoes7-3.png";
    private static final String IMG_ESPECIARIAS = "doc/img/cartoes/cartoes7/cartoes7-5.png";

    // peças de bônus: os mesmos arquivos usados no tabuleiro
    private static final String IMG_BONUS_CAVALOS = "doc/img/cartoes/cartoes3/cartoes3-1.jpg";
    private static final String IMG_BONUS_VOTO = "doc/img/cartoes/cartoes3/cartoes3-2.jpg";
    private static final String IMG_BONUS_YURTS = "doc/img/cartoes/cartoes3/cartoes3-3.jpg";
    private static final String IMG_BONUS_ESPADAS = "doc/img/cartoes/cartoes3/cartoes3-4.jpg";
    private static final String IMG_BONUS_TESOURO = "doc/img/cartoes/cartoes3/cartoes3-5.jpg";
    private static final String IMG_BONUS_MAOS = "doc/img/cartoes/cartoes3/cartoes3-6.jpg";
    private static final String IMG_BONUS_MOEDAS = "doc/img/cartoes/cartoes3/cartoes3-7.jpg";

    // o que o clique mede; deixe NADA na entrega
    private static final int NADA = 0;
    private static final int MEDIR_MELHORIAS = 1;    // 16 cliques: coluna 1 de baixo para cima, depois a 2...
    private static final int MEDIR_MARCADORES = 2;   // 4 cliques: o centro da caixa de ações de cada coluna
    private static final int MODO_MEDICAO = NADA;

    private static final int COLUNAS = 4;
    private static final int LINHAS = 4;

    private ModelFacade modelo = ModelFacade.getInstance();
    private int jogador = 0;
    private int proximoClique = 0;

    private Map<String, String> arquivosCartelas = new HashMap<String, String>();   // herdeiro → arquivo
    private Map<String, String> arquivosMarcadores = new HashMap<String, String>(); // cor → arquivo
    private Map<String, String> arquivosMelhorias = new HashMap<String, String>();  // "tipo:regiao[:tesouro]" → arquivo
    private Map<String, String> arquivosIcones = new HashMap<String, String>();     // tributo ou tesouro → arquivo
    private Map<String, String> arquivosBonus = new HashMap<String, String>();      // peça de bônus → arquivo
    private Map<String, String> nomesBonus = new HashMap<String, String>();         // peça de bônus → nome na tela
    private Map<String, Image> cache = new HashMap<String, Image>();                // imagens já reduzidas
    private Map<String, Integer> larguraOriginal = new HashMap<String, Integer>();  // herdeiro → largura do arquivo

    private Map<String, Point> espacos = new HashMap<String, Point>();   // "coluna:linha" → centro (linha 0 = embaixo)
    private Map<Integer, Point> marcadores = new HashMap<Integer, Point>(); // coluna → centro da caixa de ações

    public JanelaCartela() {
        setTitle("Cartela");
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        setResizable(false);
        registrarArquivos();
        carregarEspacos();
        carregarMarcadores();
        setContentPane(new PainelCartela());
        pack();
        ControladorJogo.getInstance().registra(this);
    }

    @Override
    public void notify(ObservadoIF o) {
        int evento = o.get(ModelFacade.EVENTO);
        if (evento == ModelFacade.ABRIR_CARTELA) {
            jogador = o.get(ModelFacade.JOGADOR_DA_CARTELA);
            setTitle("Cartela de " + modelo.nomeDoJogador(jogador) + " (" + modelo.corDoJogador(jogador) + ")");
            setLocationRelativeTo(null);
            setVisible(true);
            toFront();
            if (MODO_MEDICAO != NADA)
                avisarProximo();
        }
        if (isVisible())
            repaint();
    }

    // ---------- POSIÇÕES (pixels da imagem original da cartela) ----------

    // centro de cada espaço de melhoria; a linha 0 é a de baixo, logo acima da caixa de ações
    private void carregarEspacos() {
        int[] x = {1992, 3040, 4088, 5252};
        int[] y = {2132, 1600, 1072, 540};
        for (int c = 0; c < COLUNAS; c++)
            for (int l = 0; l < LINHAS; l++)
                espacos.put(chave(c, l), new Point(x[c], y[l]));
    }

    // onde o marcador fica quando a coluna é ativada: sobre a caixa de ações dela
    private void carregarMarcadores() {
        marcadores.put(0, new Point(1992, 2688));
        marcadores.put(1, new Point(3040, 2688));
        marcadores.put(2, new Point(4088, 2688));
        marcadores.put(3, new Point(5252, 2688));
    }

    private static String chave(int coluna, int linha) {
        return coluna + ":" + linha;
    }

    // ---------- IMAGENS ----------

    private void registrarArquivos() {
        arquivosCartelas.put("altani", IMG_CARTELA_ALTANI);
        arquivosCartelas.put("jochi", IMG_CARTELA_JOCHI);
        arquivosCartelas.put("chagatai", IMG_CARTELA_CHAGATAI);
        arquivosCartelas.put("tolui", IMG_CARTELA_TOLUI);
        arquivosCartelas.put("ogedei", IMG_CARTELA_OGEDEI);

        arquivosMarcadores.put("azul", IMG_MARCADOR_AZUL);
        arquivosMarcadores.put("amarelo", IMG_MARCADOR_AMARELO);
        arquivosMarcadores.put("preto", IMG_MARCADOR_PRETO);
        arquivosMarcadores.put("vermelho", IMG_MARCADOR_VERMELHO);
        arquivosMarcadores.put("verde", IMG_MARCADOR_VERDE);

        arquivosIcones.put("moeda", IMG_MOEDA);
        arquivosIcones.put("espada", IMG_ESPADA);
        arquivosIcones.put("yurt", IMG_YURT);
        arquivosIcones.put("pele", IMG_PELE);
        arquivosIcones.put("tecido", IMG_TECIDO);
        arquivosIcones.put("gado", IMG_GADO);
        arquivosIcones.put("ferro", IMG_FERRO);
        arquivosIcones.put("especiarias", IMG_ESPECIARIAS);

        arquivosBonus.put("cavalos", IMG_BONUS_CAVALOS);
        arquivosBonus.put("voto", IMG_BONUS_VOTO);
        arquivosBonus.put("yurts", IMG_BONUS_YURTS);
        arquivosBonus.put("espadas", IMG_BONUS_ESPADAS);
        arquivosBonus.put("tesouro", IMG_BONUS_TESOURO);
        arquivosBonus.put("maos", IMG_BONUS_MAOS);
        arquivosBonus.put("moedas", IMG_BONUS_MOEDAS);

        nomesBonus.put("cavalos", "Cavalos");
        nomesBonus.put("voto", "Voto");
        nomesBonus.put("yurts", "Yurts");
        nomesBonus.put("espadas", "Espadas");
        nomesBonus.put("tesouro", "Tesouro");
        nomesBonus.put("maos", "Mãos");
        nomesBonus.put("moedas", "Moedas");

        arquivosMelhorias.put("pegar_tributo:russia", IMG_MELHORIA_TRIBUTO_RUSSIA);
        arquivosMelhorias.put("pegar_tributo:persia", IMG_MELHORIA_TRIBUTO_PERSIA);
        arquivosMelhorias.put("pegar_tributo:china", IMG_MELHORIA_TRIBUTO_CHINA);
        arquivosMelhorias.put("dois_movimentos:russia", IMG_MELHORIA_MOVIMENTOS_RUSSIA);
        arquivosMelhorias.put("dois_movimentos:persia", IMG_MELHORIA_MOVIMENTOS_PERSIA);
        arquivosMelhorias.put("dois_movimentos:china", IMG_MELHORIA_MOVIMENTOS_CHINA);
        arquivosMelhorias.put("espada_virtual:russia", IMG_MELHORIA_ESPADA_RUSSIA);
        arquivosMelhorias.put("espada_virtual:persia", IMG_MELHORIA_ESPADA_PERSIA);
        arquivosMelhorias.put("espada_virtual:china", IMG_MELHORIA_ESPADA_CHINA);
        arquivosMelhorias.put("yurt_virtual:russia", IMG_MELHORIA_YURT_RUSSIA);
        arquivosMelhorias.put("yurt_virtual:persia", IMG_MELHORIA_YURT_PERSIA);
        arquivosMelhorias.put("yurt_virtual:china", IMG_MELHORIA_YURT_CHINA);
        arquivosMelhorias.put("moeda_virtual:russia", IMG_MELHORIA_MOEDA_RUSSIA);
        arquivosMelhorias.put("moeda_virtual:persia", IMG_MELHORIA_MOEDA_PERSIA);
        arquivosMelhorias.put("moeda_virtual:china", IMG_MELHORIA_MOEDA_CHINA);
        arquivosMelhorias.put("tesouro_virtual:china:ferro", IMG_MELHORIA_TESOURO_CHINA_FERRO);
        arquivosMelhorias.put("tesouro_virtual:russia:gado", IMG_MELHORIA_TESOURO_RUSSIA_GADO);
        arquivosMelhorias.put("tesouro_virtual:russia:especiarias", IMG_MELHORIA_TESOURO_RUSSIA_ESPECIARIAS);
        arquivosMelhorias.put("tesouro_virtual:persia:tecido", IMG_MELHORIA_TESOURO_PERSIA_TECIDO);
        arquivosMelhorias.put("tesouro_virtual:persia:pele", IMG_MELHORIA_TESOURO_PERSIA_PELE);
    }

    // cartela do herdeiro reduzida para LARGURA_CARTELA (null se o arquivo não existir)
    private Image cartela(String herdeiro) {
        String k = "cartela:" + herdeiro;
        if (!cache.containsKey(k)) {
            Image original = Imagens.carregar(arquivosCartelas.get(herdeiro));
            Image reduzida = null;
            if (original != null) {
                int w = original.getWidth(null), h = original.getHeight(null);
                larguraOriginal.put(herdeiro, w);
                reduzida = pronta(original.getScaledInstance(LARGURA_CARTELA, LARGURA_CARTELA * h / w, Image.SCALE_SMOOTH));
            }
            cache.put(k, reduzida);
        }
        return cache.get(k);
    }

    private Image melhoria(String descricao, int tamanho) {
        String k = "melhoria:" + descricao + ":" + tamanho;
        if (!cache.containsKey(k)) {
            String caminho = arquivosMelhorias.get(descricao);
            Image original = (caminho == null) ? null : Imagens.carregar(caminho);
            cache.put(k, (original != null) ? reduzir(original, tamanho) : Desenhos.melhoria(descricao, tamanho));
        }
        return cache.get(k);
    }

    // ícone de tributo ou tesouro; sem arquivo, usa o desenho gerado
    private Image icone(String tipo, int tamanho) {
        String k = "icone:" + tipo + ":" + tamanho;
        if (!cache.containsKey(k)) {
            String caminho = arquivosIcones.get(tipo);
            Image original = (caminho == null) ? null : Imagens.carregar(caminho);
            Image gerado = (tipo.equals("moeda") || tipo.equals("espada") || tipo.equals("yurt"))
                    ? Desenhos.tributo(tipo, tamanho) : Desenhos.tesouro(tipo, tamanho);
            cache.put(k, (original != null) ? reduzir(original, tamanho) : gerado);
        }
        return cache.get(k);
    }

    // peça de bônus; null se o arquivo não existir
    private Image pecaBonus(String tipo, int tamanho) {
        String k = "bonus:" + tipo + ":" + tamanho;
        if (!cache.containsKey(k)) {
            String caminho = arquivosBonus.get(tipo);
            Image original = (caminho == null) ? null : Imagens.carregar(caminho);
            cache.put(k, (original != null) ? reduzir(original, tamanho) : null);
        }
        return cache.get(k);
    }

    private Image marcador(String cor, int tamanho) {
        String k = "marcador:" + cor + ":" + tamanho;
        if (!cache.containsKey(k)) {
            String caminho = arquivosMarcadores.get(cor);
            Image original = (caminho == null) ? null : Imagens.carregar(caminho);
            cache.put(k, (original != null) ? redondo(reduzir(original, tamanho), tamanho) : Desenhos.yurt(cor, tamanho));
        }
        return cache.get(k);
    }

    // reduz mantendo a proporção: a maior medida fica com "tamanho" pixels
    private static Image reduzir(Image original, int tamanho) {
        int w = original.getWidth(null), h = original.getHeight(null);
        int lw = (w >= h) ? tamanho : tamanho * w / h;
        int lh = (h >= w) ? tamanho : tamanho * h / w;
        return pronta(original.getScaledInstance(lw, lh, Image.SCALE_SMOOTH));
    }

    // o arquivo do marcador é um quadrado branco com o disco dentro: recorta só o disco
    private static Image redondo(Image img, int tamanho) {
        java.awt.image.BufferedImage r = new java.awt.image.BufferedImage(tamanho, tamanho,
                java.awt.image.BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = r.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setClip(new java.awt.geom.Ellipse2D.Double(1, 1, tamanho - 2, tamanho - 2));
        g.drawImage(img, 0, 0, tamanho, tamanho, null);
        g.setClip(null);
        g.setColor(new Color(60, 60, 60));
        g.drawOval(1, 1, tamanho - 3, tamanho - 3);
        g.dispose();
        return r;
    }

    // getScaledInstance só calcula a imagem quando ela é desenhada; o ImageIcon espera ficar pronta,
    // assim getWidth/getHeight já devolvem o tamanho certo
    private static Image pronta(Image img) {
        return new ImageIcon(img).getImage();
    }

    // escala da cartela do herdeiro: pixels da tela por pixel da imagem original
    private double escala(String herdeiro) {
        Integer w = larguraOriginal.get(herdeiro);
        return (double) LARGURA_CARTELA / ((w == null) ? LARGURA_ORIGINAL : w);
    }

    private Point naTela(Point naImagem, double escala) {
        return new Point(MARGEM + (int) (naImagem.x * escala), MARGEM + (int) (naImagem.y * escala));
    }

    // ---------- MEDIÇÃO (só com MODO_MEDICAO diferente de NADA) ----------

    private class MedidorClique extends MouseAdapter {
        @Override
        public void mouseClicked(MouseEvent e) {
            double esc = escala(modelo.herdeiroDoJogador(jogador));
            int x = (int) Math.round((e.getX() - MARGEM) / esc);
            int y = (int) Math.round((e.getY() - MARGEM) / esc);
            if (MODO_MEDICAO == MEDIR_MELHORIAS && proximoClique < COLUNAS * LINHAS) {
                int c = proximoClique / LINHAS, l = proximoClique % LINHAS;
                System.out.println("espacos.put(chave(" + c + ", " + l + "), new Point(" + x + ", " + y + "));");
            } else if (MODO_MEDICAO == MEDIR_MARCADORES && proximoClique < COLUNAS) {
                System.out.println("marcadores.put(" + proximoClique + ", new Point(" + x + ", " + y + "));");
            }
            proximoClique++;
            avisarProximo();
        }
    }

    private void avisarProximo() {
        if (MODO_MEDICAO == MEDIR_MELHORIAS && proximoClique < COLUNAS * LINHAS)
            System.out.println("Clique no centro do espaço " + (proximoClique % LINHAS + 1) + " (de baixo para cima) da coluna "
                    + (proximoClique / LINHAS + 1));
        else if (MODO_MEDICAO == MEDIR_MARCADORES && proximoClique < COLUNAS)
            System.out.println("Clique no centro da caixa de ações da coluna " + (proximoClique + 1));
        else
            System.out.println("Pronto: copie as linhas acima");
    }

    // ---------- DESENHO ----------

    private class PainelCartela extends JPanel {
        PainelCartela() {
            int alturaCartela = LARGURA_CARTELA * 3237 / LARGURA_ORIGINAL;
            setPreferredSize(new Dimension(MARGEM + LARGURA_CARTELA + MARGEM + LARGURA_CARTA + MARGEM,
                    MARGEM + alturaCartela + ALTURA_RECURSOS));
            setBackground(new Color(245, 238, 220));
            if (MODO_MEDICAO != NADA)
                addMouseListener(new MedidorClique());
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (!modelo.jogoIniciado())
                return;
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

            String herdeiro = modelo.herdeiroDoJogador(jogador);
            Image img = cartela(herdeiro);
            double esc = escala(herdeiro);
            int alturaCartela = (img != null) ? img.getHeight(null) : LARGURA_CARTELA * 3237 / LARGURA_ORIGINAL;
            if (img != null)
                g2.drawImage(img, MARGEM, MARGEM, null);

            // melhorias: a 1ª comprada fica no espaço de baixo, as seguintes vão subindo
            int tamMelhoria = (int) (MELHORIA_IMAGEM * esc);
            for (int c = 0; c < COLUNAS; c++) {
                List<String> melhorias = modelo.melhoriasDaColuna(jogador, c);
                for (int l = 0; l < melhorias.size() && l < LINHAS; l++) {
                    Point p = naTela(espacos.get(chave(c, l)), esc);
                    Image m = melhoria(melhorias.get(l), tamMelhoria);
                    g2.drawImage(m, p.x - m.getWidth(null) / 2, p.y - m.getHeight(null) / 2, null);
                }
            }

            // marcador da cor do jogador sobre cada coluna ativada neste ciclo
            int tamMarcador = (int) (MARCADOR_IMAGEM * esc);
            for (int c = 0; c < COLUNAS; c++) {
                if (!modelo.colunaAtivada(jogador, c))
                    continue;
                Point p = naTela(marcadores.get(c), esc);
                Image m = marcador(modelo.corDoJogador(jogador), tamMarcador);
                g2.drawImage(m, p.x - m.getWidth(null) / 2, p.y - m.getHeight(null) / 2, null);
            }

            if (MODO_MEDICAO != NADA)
                desenharPontosMedidos(g2, esc);

            // carta secreta à direita
            String secreta = modelo.cartaSecreta(jogador);
            int xc = MARGEM + LARGURA_CARTELA + MARGEM;
            g2.setColor(Color.BLACK);
            g2.setFont(new Font("SansSerif", Font.BOLD, 13));
            g2.drawString("Carta secreta", xc, MARGEM + 12);
            if (secreta != null)
                g2.drawImage(Desenhos.carta(secreta, LARGURA_CARTA, false), xc, MARGEM + 20, null);

            desenharRecursos(g2, MARGEM + alturaCartela + 24);
        }

        private void desenharRecursos(Graphics2D g2, int y) {
            g2.setColor(Color.BLACK);
            g2.setFont(new Font("SansSerif", Font.BOLD, 14));
            g2.drawString("Recursos", MARGEM, y);
            g2.setFont(new Font("SansSerif", Font.PLAIN, 13));
            int x = MARGEM;
            y += 10;
            int meio = y + ICONE / 2 + 5;                  // linha de base do texto ao lado dos ícones
            for (String t : new String[] {"moeda", "espada", "yurt"}) {
                desenharCentrado(g2, icone(t, ICONE), x + ICONE / 2, y + ICONE / 2);
                g2.drawString("× " + modelo.tributosDoJogador(jogador, t), x + ICONE + 6, meio);
                x += 90;
            }
            x += 30;
            for (String t : new String[] {"pele", "tecido", "gado", "ferro", "especiarias"}) {
                desenharCentrado(g2, icone(t, ICONE), x + ICONE / 2, y + ICONE / 2);
                g2.drawString(t + " × " + modelo.tesourosDoJogador(jogador, t), x + ICONE + 6, meio);
                x += 140;
            }
            y += ICONE + 24;
            g2.drawString("Yurts na reserva: " + modelo.yurtsRestantes(jogador)
                    + "  ·  Votos: " + modelo.votosDe(jogador), MARGEM, y);

            // peças de bônus: a imagem de cada uma com o nome embaixo
            y += 30;
            g2.setFont(new Font("SansSerif", Font.BOLD, 14));
            g2.drawString("Peças de bônus", MARGEM, y);
            g2.setFont(new Font("SansSerif", Font.PLAIN, 12));
            List<String> pecas = modelo.pecasBonusDoJogador(jogador);
            if (pecas.isEmpty()) {
                g2.drawString("nenhuma ainda", MARGEM + 120, y);
                return;
            }
            FontMetrics fm = g2.getFontMetrics();
            x = MARGEM;
            int topo = y + 10;
            for (String p : pecas) {
                int centro = x + PECA_BONUS / 2;
                Image img = pecaBonus(p, PECA_BONUS);
                if (img != null)
                    desenharCentrado(g2, img, centro, topo + PECA_BONUS / 2);
                String nome = nomesBonus.containsKey(p) ? nomesBonus.get(p) : p;
                g2.drawString(nome, centro - fm.stringWidth(nome) / 2, topo + PECA_BONUS + 16);
                x += PECA_BONUS + 30;
            }
        }

        private void desenharCentrado(Graphics2D g2, Image img, int cx, int cy) {
            g2.drawImage(img, cx - img.getWidth(null) / 2, cy - img.getHeight(null) / 2, null);
        }

        private void desenharPontosMedidos(Graphics2D g2, double esc) {
            g2.setColor(Color.RED);
            for (String k : espacos.keySet()) {
                Point p = naTela(espacos.get(k), esc);
                g2.fillOval(p.x - 4, p.y - 4, 8, 8);
                g2.drawString(k, p.x + 6, p.y - 6);
            }
            for (Integer c : marcadores.keySet()) {
                Point p = naTela(marcadores.get(c), esc);
                g2.fillOval(p.x - 4, p.y - 4, 8, 8);
                g2.drawString("M" + c, p.x + 6, p.y - 6);
            }
        }
    }

}