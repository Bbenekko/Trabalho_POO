package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import controller.ControladorJogo;
import model.ModelFacade;
import model.ObservadoIF;
import model.ObservadorIF;

public class PainelTabuleiro extends JPanel implements ObservadorIF {
    private static final int LARGURA_IMAGEM = 2905;
    private static final int ALTURA_IMAGEM = 2919;

    private static final int LARGURA_MAXIMA = 1080;
    private static final int ALTURA_MAXIMA = 1000;
    private static final int LARGURA_LATERAL = PainelControles.LARGURA;
    private static final int BARRA_DE_TITULO = 40;

    private static final int RAIO_PARADA = 75;
    private static final int RAIO_PROVINCIA = 170;
    private static final int RAIO_CIDADE = 190;
    private static final int PEAO_IMAGEM = 110;
    private static final int YURT_IMAGEM = 80;
    private static final int TRIBUTO_IMAGEM = 190;
    private static final int CIDADE_IMAGEM = 250;
    private static final int TESOURO_IMAGEM = CIDADE_IMAGEM * 2 / 5;
    private static final int MELHORIA_IMAGEM = 200;
    private static final int RAIO_MELHORIA = 110;
    private static final int MELHORIAS_EXPOSTAS = 5; 
    private static final int BONUS_IMAGEM = 200;

    private static final String IMG_MOEDA = "doc/img/cartoes/cartoes6/cartoes6-3.png";
    private static final String IMG_ESPADA = "doc/img/cartoes/cartoes6/cartoes6-2.png";
    private static final String IMG_YURT = "doc/img/cartoes/cartoes6/cartoes6-1.png";

    private static final String IMG_KIEV = "doc/img/cartoes/cartoes2/cartoes2-4.jpg";
    private static final String IMG_MOSCOU = "doc/img/cartoes/cartoes2/cartoes2-5.jpg";
    private static final String IMG_SARAI = "doc/img/cartoes/cartoes2/cartoes2-6.jpg";
    private static final String IMG_BAGDA = "doc/img/cartoes/cartoes2/cartoes2-2.jpg";
    private static final String IMG_SAMARCANDA = "doc/img/cartoes/cartoes2/cartoes2-1.jpg";
    private static final String IMG_CABUL = "doc/img/cartoes/cartoes2/cartoes2-3.jpg";
    private static final String IMG_PEQUIM = "doc/img/cartoes/cartoes2/cartoes2-9.jpg";
    private static final String IMG_KAESONG = "doc/img/cartoes/cartoes2/cartoes2-8.jpg";
    private static final String IMG_CANTAO = "doc/img/cartoes/cartoes2/cartoes2-7.jpg";

    private static final String IMG_PELE = "doc/img/cartoes/cartoes7/cartoes7-4.png";
    private static final String IMG_TECIDO = "doc/img/cartoes/cartoes7/cartoes7-2.png";
    private static final String IMG_GADO = "doc/img/cartoes/cartoes7/cartoes7-1.png";
    private static final String IMG_FERRO = "doc/img/cartoes/cartoes7/cartoes7-3.png";
    private static final String IMG_ESPECIARIAS = "doc/img/cartoes/cartoes7/cartoes7-5.png";

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

    private static final String IMG_BONUS_CAVALOS = "doc/img/cartoes/cartoes3/cartoes3-1.jpg";   
    private static final String IMG_BONUS_VOTO = "doc/img/cartoes/cartoes3/cartoes3-2.jpg";        
    private static final String IMG_BONUS_YURTS = "doc/img/cartoes/cartoes3/cartoes3-3.jpg";       
    private static final String IMG_BONUS_ESPADAS = "doc/img/cartoes/cartoes3/cartoes3-4.jpg";   
    private static final String IMG_BONUS_TESOURO = "doc/img/cartoes/cartoes3/cartoes3-5.jpg";   
    private static final String IMG_BONUS_MAOS = "doc/img/cartoes/cartoes3/cartoes3-6.jpg";       
    private static final String IMG_BONUS_MOEDAS = "doc/img/cartoes/cartoes3/cartoes3-7.jpg";    

    private static final int NADA = 0;
    private static final int MEDIR_PARADAS = 1;
    private static final int MEDIR_KHAN = 2;
    private static final int MEDIR_TRIBUTOS = 3;
    private static final int MEDIR_CIDADES = 4;
    private static final int MEDIR_MELHORIAS = 5;
    private static final int MEDIR_BONUS = 6;
    private static final int MEDIR_TRILHA = 7;      // 17 cliques: os círculos da trilha, da cadeira (0) até o 16
    private static final int MODO_MEDICAO = NADA;

    // trilha do Kurultai: a casa 0 é a da cadeira, onde todas as fichas começam; os marcos de fim
    // impressos ficam nas casas 9 (2 jogadores), 13 (3), 15 (4) e 16 (5)
    private static final int ULTIMA_CASA_TRILHA = 16;
    private static final int FICHA_TRILHA = 110;         // diâmetro da ficha, em pixels da imagem original
    private static final int DESLOCAMENTO_PILHA = 42;    // quanto cada ficha empilhada sobe
    private static final int RAIO_CASA_TRILHA = 85;

    private ModelFacade modelo = ModelFacade.getInstance();
    private double escala;
    private int largura;
    private int altura;
    private Image imgTabuleiro;
    private Map<Integer, Point> coordenadas = new HashMap<Integer, Point>();
    private Map<Integer, Point> centrosProvincias = new HashMap<Integer, Point>();
    private Map<String, Point> centrosCidades = new HashMap<String, Point>();
    private Map<Integer, Point> posicoesKhan = new HashMap<Integer, Point>();
    private Point posicaoKhanMelhorias = null;
    private Map<String, Image> imgTributos = new HashMap<String, Image>();
    private Map<Integer, Point> posicoesTributos = new HashMap<Integer, Point>();
    private Map<String, Point> posicoesCidades = new HashMap<String, Point>();
    private Map<String, Image> imgCidades = new HashMap<String, Image>();
    private Map<String, Image> imgTesouros = new HashMap<String, Image>();
    private Map<String, Image> imgMelhorias = new HashMap<String, Image>();
    private Map<Integer, Point> posicoesMelhorias = new HashMap<Integer, Point>();
    private Map<String, Image> imgBonus = new HashMap<String, Image>();
    private Map<Integer, Point> posicoesBonus = new HashMap<Integer, Point>();
    private Map<Integer, Point> posicoesTrilha = new HashMap<Integer, Point>();        // casa do Kurultai → centro
    private Map<String, Image> fichasTrilha = new HashMap<String, Image>();            // cor → ficha desenhada
    private List<Integer> paraMedir = new ArrayList<Integer>();
    private List<String> cidadesParaMedir = new ArrayList<String>();
    private Map<String, Rectangle> areasDosPeoes = new HashMap<String, Rectangle>();
    private List<String> ordemDosPeoes = new ArrayList<String>();
    private int jogadorSelecionado = -1;
    private int peaoSelecionado = -1;
    private int proximoId = 0;

    public PainelTabuleiro() {
        calcularTamanho();
        carregarImagens();
        carregarCoordenadas();
        carregarPosicoesKhan();
        carregarPosicoesTributos();
        carregarPosicoesCidades();
        carregarPosicoesMelhorias();
        carregarPosicoesBonus();
        carregarPosicoesTrilha();
        calcularCentros();
        setPreferredSize(new Dimension(largura, altura));
        if (MODO_MEDICAO != NADA)
            addMouseListener(new MedidorClique());
        if (MODO_MEDICAO == MEDIR_KHAN || MODO_MEDICAO == MEDIR_TRIBUTOS
                || MODO_MEDICAO == MEDIR_CIDADES || MODO_MEDICAO == MEDIR_MELHORIAS
                || MODO_MEDICAO == MEDIR_BONUS || MODO_MEDICAO == MEDIR_TRILHA)
            prepararMedicao();

        ControladorJogo.getInstance().registra(this);
    }

    // ---------- OBSERVER ----------

    @Override
    public void notify(ObservadoIF o) {
        int evento = o.get(ModelFacade.EVENTO);
        if (evento == ModelFacade.ABRIR_CARTELA || evento == ModelFacade.ABRIR_CARTAS)
            return;
        if (evento == ModelFacade.ESTADO && !modelo.turnoEmAndamento())
            selecionarPeao(-1, -1);
        repaint();
    }

    // ---------- TAMANHO ----------

    private void calcularTamanho() {
        Rectangle tela = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();
        int larguraDisponivel = Math.min(LARGURA_MAXIMA, tela.width - LARGURA_LATERAL);
        int alturaDisponivel = Math.min(ALTURA_MAXIMA, tela.height - BARRA_DE_TITULO);
        escala = Math.min((double) larguraDisponivel / LARGURA_IMAGEM,
                          (double) alturaDisponivel / ALTURA_IMAGEM);
        largura = (int) (LARGURA_IMAGEM * escala);
        altura = (int) (ALTURA_IMAGEM * escala);
    }

    private int px(int tamanhoNaImagem) {
        return Math.max(10, (int) (tamanhoNaImagem * escala));
    }

    private Point naTela(Point naImagem) {
        return new Point((int) (naImagem.x * escala), (int) (naImagem.y * escala));
    }

    // ---------- CARREGAMENTO ----------

    private void carregarImagens() {
        Image original = Imagens.carregar("doc/img/tabuleiro.jpg");
        if (original != null)
            imgTabuleiro = original.getScaledInstance(largura, altura, Image.SCALE_SMOOTH);

        carregarTributo("moeda", IMG_MOEDA);
        carregarTributo("espada", IMG_ESPADA);
        carregarTributo("yurt", IMG_YURT);

        carregarCidade("Kiev", IMG_KIEV);
        carregarCidade("Moscou", IMG_MOSCOU);
        carregarCidade("Sarai", IMG_SARAI);
        carregarCidade("Bagda", IMG_BAGDA);
        carregarCidade("Samarcanda", IMG_SAMARCANDA);
        carregarCidade("Cabul", IMG_CABUL);
        carregarCidade("Pequim", IMG_PEQUIM);
        carregarCidade("Kaesong", IMG_KAESONG);
        carregarCidade("Cantao", IMG_CANTAO);

        carregarTesouro("pele", IMG_PELE);
        carregarTesouro("tecido", IMG_TECIDO);
        carregarTesouro("gado", IMG_GADO);
        carregarTesouro("ferro", IMG_FERRO);
        carregarTesouro("especiarias", IMG_ESPECIARIAS);

        carregarMelhoria("pegar_tributo:russia", IMG_MELHORIA_TRIBUTO_RUSSIA);
        carregarMelhoria("pegar_tributo:persia", IMG_MELHORIA_TRIBUTO_PERSIA);
        carregarMelhoria("pegar_tributo:china", IMG_MELHORIA_TRIBUTO_CHINA);
        carregarMelhoria("dois_movimentos:russia", IMG_MELHORIA_MOVIMENTOS_RUSSIA);
        carregarMelhoria("dois_movimentos:persia", IMG_MELHORIA_MOVIMENTOS_PERSIA);
        carregarMelhoria("dois_movimentos:china", IMG_MELHORIA_MOVIMENTOS_CHINA);
        carregarMelhoria("espada_virtual:russia", IMG_MELHORIA_ESPADA_RUSSIA);
        carregarMelhoria("espada_virtual:persia", IMG_MELHORIA_ESPADA_PERSIA);
        carregarMelhoria("espada_virtual:china", IMG_MELHORIA_ESPADA_CHINA);
        carregarMelhoria("yurt_virtual:russia", IMG_MELHORIA_YURT_RUSSIA);
        carregarMelhoria("yurt_virtual:persia", IMG_MELHORIA_YURT_PERSIA);
        carregarMelhoria("yurt_virtual:china", IMG_MELHORIA_YURT_CHINA);
        carregarMelhoria("moeda_virtual:russia", IMG_MELHORIA_MOEDA_RUSSIA);
        carregarMelhoria("moeda_virtual:persia", IMG_MELHORIA_MOEDA_PERSIA);
        carregarMelhoria("moeda_virtual:china", IMG_MELHORIA_MOEDA_CHINA);
        carregarMelhoria("tesouro_virtual:china:ferro", IMG_MELHORIA_TESOURO_CHINA_FERRO);
        carregarMelhoria("tesouro_virtual:russia:gado", IMG_MELHORIA_TESOURO_RUSSIA_GADO);
        carregarMelhoria("tesouro_virtual:russia:especiarias", IMG_MELHORIA_TESOURO_RUSSIA_ESPECIARIAS);
        carregarMelhoria("tesouro_virtual:persia:tecido", IMG_MELHORIA_TESOURO_PERSIA_TECIDO);
        carregarMelhoria("tesouro_virtual:persia:pele", IMG_MELHORIA_TESOURO_PERSIA_PELE);

        carregarBonus("cavalos", IMG_BONUS_CAVALOS);
        carregarBonus("voto", IMG_BONUS_VOTO);
        carregarBonus("yurts", IMG_BONUS_YURTS);
        carregarBonus("espadas", IMG_BONUS_ESPADAS);
        carregarBonus("tesouro", IMG_BONUS_TESOURO);
        carregarBonus("maos", IMG_BONUS_MAOS);
        carregarBonus("moedas", IMG_BONUS_MOEDAS);
    }

    private void carregarBonus(String tipo, String caminho) {
        Image original = Imagens.carregar(caminho);
        if (original != null)
            imgBonus.put(tipo, reduzir(original, px(BONUS_IMAGEM)));
    }

    private void carregarMelhoria(String descricao, String caminho) {
        int t = px(MELHORIA_IMAGEM);
        Image original = Imagens.carregar(caminho); 
        if (original != null)
            imgMelhorias.put(descricao, reduzir(original, t));
        else
            imgMelhorias.put(descricao, Desenhos.melhoria(descricao, t));
    }

    private void carregarCidade(String nome, String caminho) {
        Image original = Imagens.carregar(caminho);
        if (original != null)
            imgCidades.put(nome, reduzir(original, px(CIDADE_IMAGEM)));
    }

    private void carregarTesouro(String tipo, String caminho) {
        int t = px(TESOURO_IMAGEM);
        Image original = Imagens.carregar(caminho);
        if (original != null)
            imgTesouros.put(tipo, reduzir(original, t));
        else
            imgTesouros.put(tipo, Desenhos.tesouro(tipo, t)); 
    }

    private void carregarTributo(String tipo, String caminho) {
        int t = px(TRIBUTO_IMAGEM);
        Image original = Imagens.carregar(caminho);
        if (original != null)
            imgTributos.put(tipo, reduzir(original, t));
        else
            imgTributos.put(tipo, Desenhos.tributo(tipo, t)); 
    }

    private static Image reduzir(Image original, int tamanho) {
        int w = original.getWidth(null), h = original.getHeight(null);
        int lw = (w >= h) ? tamanho : tamanho * w / h;
        int lh = (h >= w) ? tamanho : tamanho * h / w;
        return original.getScaledInstance(lw, lh, Image.SCALE_SMOOTH);
    }

    private void carregarPosicoesKhan() {
        posicoesKhan.put(0, new Point(628, 617));      
        posicoesKhan.put(4, new Point(1742, 922));     
        posicoesKhan.put(7, new Point(805, 1738));     
        posicoesKhan.put(10, new Point(2133, 2061));  
        posicaoKhanMelhorias = new Point(105, 2795);
    }

    private void carregarCoordenadas() {
        coordenadas.put(0, new Point(128, 542));
        coordenadas.put(1, new Point(470, 542));
        coordenadas.put(2, new Point(489, 256));
        coordenadas.put(3, new Point(888, 211));
        coordenadas.put(4, new Point(854, 560));
        coordenadas.put(5, new Point(1083, 843));
        coordenadas.put(6, new Point(1275, 497));
        coordenadas.put(7, new Point(1565, 670));
        coordenadas.put(8, new Point(1753, 226));
        coordenadas.put(9, new Point(1892, 594));
        coordenadas.put(10, new Point(2238, 493));
        coordenadas.put(11, new Point(463, 888));
        coordenadas.put(12, new Point(839, 1189));
        coordenadas.put(13, new Point(959, 1557));
        coordenadas.put(14, new Point(572, 1411));
        coordenadas.put(15, new Point(211, 1493));
        coordenadas.put(16, new Point(181, 1915));
        coordenadas.put(17, new Point(448, 1640));
        coordenadas.put(18, new Point(718, 1892));
        coordenadas.put(19, new Point(1275, 1821));
        coordenadas.put(20, new Point(1215, 2231));
        coordenadas.put(21, new Point(1475, 2050));
        coordenadas.put(22, new Point(2494, 655));
        coordenadas.put(23, new Point(2765, 880));
        coordenadas.put(24, new Point(2524, 1072));
        coordenadas.put(25, new Point(2133, 1241));
        coordenadas.put(26, new Point(1892, 1599));
        coordenadas.put(27, new Point(2174, 1670));
        coordenadas.put(28, new Point(2550, 1535));
        coordenadas.put(29, new Point(1832, 2223));
        coordenadas.put(30, new Point(2238, 2231));
        coordenadas.put(31, new Point(2524, 2024));
        coordenadas.put(32, new Point(2588, 2400));
        coordenadas.put(33, new Point(1448, 1264));
    }

    private void calcularCentros() {
        for (int id : modelo.idsDasProvincias()) {
            Point c = centro(modelo.paradasDaProvincia(id));
            if (c != null)
                centrosProvincias.put(id, c);
        }
        centrosProvincias.putAll(posicoesTributos);   
        for (String nome : modelo.nomesDasCidades()) {
            Point c = centro(modelo.paradasDaCidade(nome));
            if (c != null)
                centrosCidades.put(nome, c);
        }
        centrosCidades.putAll(posicoesCidades);     
    }

    private Point centro(List<Integer> paradas) {
        int n = 0, sx = 0, sy = 0;
        for (int id : paradas) {
            Point p = coordenadas.get(id);
            if (p != null) {
                sx += p.x;
                sy += p.y;
                n++;
            }
        }
        return (n == 0) ? null : new Point(sx / n, sy / n);
    }

    private void carregarPosicoesTributos() {
        posicoesTributos.put(0, new Point(632, 421));
        posicoesTributos.put(1, new Point(854, 361));
        posicoesTributos.put(2, new Point(801, 940));
        posicoesTributos.put(3, new Point(1245, 933));
        posicoesTributos.put(4, new Point(1964, 756));
        posicoesTributos.put(5, new Point(2159, 1053));
        posicoesTributos.put(6, new Point(523, 1223));
        posicoesTributos.put(7, new Point(606, 1663));
        posicoesTributos.put(8, new Point(1520, 1629));
        posicoesTributos.put(9, new Point(1678, 1922));
        posicoesTributos.put(10, new Point(2061, 1828));
        posicoesTributos.put(11, new Point(2400, 1565));
    }

    private void carregarPosicoesCidades() {
    	posicoesCidades.put("Bagda", new Point(252, 1708));
    	posicoesCidades.put("Cabul", new Point(1264, 2054));
    	posicoesCidades.put("Cantao", new Point(2445, 2234));
    	posicoesCidades.put("Kaesong", new Point(2554, 846));
    	posicoesCidades.put("Kiev", new Point(286, 391));
    	posicoesCidades.put("Moscou", new Point(1065, 639));
    	posicoesCidades.put("Pequim", new Point(2099, 1471));
    	posicoesCidades.put("Samarcanda", new Point(790, 1396));
    	posicoesCidades.put("Sarai", new Point(1727, 444));
    }

    private void carregarPosicoesMelhorias() {
    	posicoesMelhorias.put(4, new Point(301, 2799));
    	posicoesMelhorias.put(3, new Point(568, 2799));
    	posicoesMelhorias.put(2, new Point(828, 2795));
    	posicoesMelhorias.put(1, new Point(1091, 2799));
    	posicoesMelhorias.put(0, new Point(1354, 2802));
    }

    // centro de cada casa da trilha do Kurultai, medido com MODO_MEDICAO = MEDIR_TRILHA.
    // 0 = a cadeira (todas começam nela); de 0 a 9 a fileira de cima (da esquerda para a direita),
    // de 10 a 16 a de baixo (da direita para a esquerda)
    private void carregarPosicoesTrilha() {
        posicoesTrilha.put(0, new Point(1150, 2630));
        posicoesTrilha.put(1, new Point(1330, 2630));
        posicoesTrilha.put(2, new Point(1515, 2630));
        posicoesTrilha.put(3, new Point(1695, 2630));
        posicoesTrilha.put(4, new Point(1880, 2630));
        posicoesTrilha.put(5, new Point(2065, 2630));
        posicoesTrilha.put(6, new Point(2250, 2630));
        posicoesTrilha.put(7, new Point(2435, 2630));
        posicoesTrilha.put(8, new Point(2615, 2630));
        posicoesTrilha.put(9, new Point(2800, 2630));
        posicoesTrilha.put(10, new Point(2720, 2795));
        posicoesTrilha.put(11, new Point(2540, 2795));
        posicoesTrilha.put(12, new Point(2355, 2795));
        posicoesTrilha.put(13, new Point(2165, 2795));
        posicoesTrilha.put(14, new Point(1985, 2795));
        posicoesTrilha.put(15, new Point(1790, 2795));
        posicoesTrilha.put(16, new Point(1610, 2795));
    }

    private void carregarPosicoesBonus() {
    	posicoesBonus.put(0, new Point(1381, 166));
    	posicoesBonus.put(1, new Point(2415, 286));
    	posicoesBonus.put(2, new Point(229, 1027));
    	posicoesBonus.put(3, new Point(1452, 1407));
    	posicoesBonus.put(4, new Point(2750, 1681));
    	posicoesBonus.put(5, new Point(715, 2080));
    	posicoesBonus.put(6, new Point(1693, 2389));
    }

    // ---------- CONSULTAS DO CONTROLLER (clique → elemento) ----------

    public boolean emModoMedicao() {
        return MODO_MEDICAO != NADA;
    }

    public int paradaEm(int x, int y) {
        return maisProximo(coordenadas, x, y, RAIO_PARADA);
    }

    // pergaminho de entrega de tesouros: o clique perto do espaço de bônus dele
    public int espacoBonusEm(int x, int y) {
        return maisProximo(posicoesBonus, x, y, BONUS_IMAGEM);
    }

    public int provinciaEm(int x, int y) {
        return maisProximo(centrosProvincias, x, y, RAIO_PROVINCIA);
    }

    public int posicaoKhanEm(int x, int y) {
        return maisProximo(posicoesKhan, x, y, RAIO_PROVINCIA);
    }

    public boolean khanMelhoriasEm(int x, int y) {
        return posicaoKhanMelhorias != null
                && naTela(posicaoKhanMelhorias).distance(x, y) <= RAIO_PROVINCIA * escala;
    }

    public String cidadeEm(int x, int y) {
        String melhor = null;
        double menor = RAIO_CIDADE * escala;
        for (String nome : centrosCidades.keySet()) {
            if (!modelo.cidadeRevelada(nome))
                continue;
            double d = naTela(centrosCidades.get(nome)).distance(x, y);
            if (d <= menor) {
                menor = d;
                melhor = nome;
            }
        }
        return melhor;
    }

    public int melhoriaEm(int x, int y) {
        int espaco = maisProximo(posicoesMelhorias, x, y, RAIO_MELHORIA);
        return (espaco < modelo.melhoriasExpostas().size()) ? espaco : -1;
    }

    private int maisProximo(Map<Integer, Point> pontos, int x, int y, int raio) {
        int melhor = -1;
        double menor = raio * escala;
        for (Integer id : pontos.keySet()) {
            double d = naTela(pontos.get(id)).distance(x, y);
            if (d <= menor) {
                menor = d;
                melhor = id;
            }
        }
        return melhor;
    }

    public int[] peaoEm(int x, int y) {
        for (int i = ordemDosPeoes.size() - 1; i >= 0; i--) {
            String chave = ordemDosPeoes.get(i);
            if (areasDosPeoes.get(chave).contains(x, y)) {
                String[] partes = chave.split(":");
                return new int[] {Integer.parseInt(partes[0]), Integer.parseInt(partes[1])};
            }
        }
        return null;
    }

    public void selecionarPeao(int jogador, int peao) {
        jogadorSelecionado = jogador;
        peaoSelecionado = peao;
        repaint();
    }

    public void mostrarMenuAtaque(String cidade, List<String> tesouros, int custo, int x, int y,
                                  ActionListener tratador) {
        JPopupMenu menu = new JPopupMenu();
        List<String> jaListados = new ArrayList<String>();
        for (String t : tesouros) {
            if (jaListados.contains(t))
                continue;
            jaListados.add(t);
            JMenuItem item = new JMenuItem("Tomar " + t + " (" + custo + (custo == 1 ? " espada)" : " espadas)"));
            item.setActionCommand("ATACAR:" + cidade + ":" + t);
            item.addActionListener(tratador);
            menu.add(item);
        }
        menu.show(this, x, y);
    }

    // ---------- DESENHO ----------

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.drawImage(imgTabuleiro, 0, 0, null);
        if (modelo.jogoIniciado()) {
            desenharTributos(g2);
            desenharCidades(g2);
            desenharMelhorias(g2);
            desenharBonus(g2);
            desenharYurts(g2);                         
            desenharPeoes(g2);
            desenharKhan(g2);
            desenharTrilha(g2);
        }
        if (MODO_MEDICAO != NADA)
            desenharPontosMedidos(g2);
    }

    private void desenharTributos(Graphics2D g2) {
        int t = px(TRIBUTO_IMAGEM);
        for (Integer id : centrosProvincias.keySet()) {
            int qtd = modelo.tributosNaProvincia(id);
            if (qtd == 0)
                continue;
            Point c = naTela(centrosProvincias.get(id));
            Image img = imgTributos.get(modelo.tipoDeTributoDaProvincia(id));
            g2.drawImage(img, c.x - img.getWidth(null) / 2, c.y - img.getHeight(null) / 2, null);
            if (qtd > 1)
                etiqueta(g2, "×" + qtd, c.x + t / 2 - 2, c.y + t / 2);
        }
    }

    private void desenharCidades(Graphics2D g2) {
        for (String nome : centrosCidades.keySet()) {
            if (!modelo.cidadeRevelada(nome))
                continue;
            Point c = naTela(centrosCidades.get(nome));

            Image img = imgCidades.get(nome);
            int w = (img != null) ? img.getWidth(null) : px(CIDADE_IMAGEM);
            int h = (img != null) ? img.getHeight(null) : px(CIDADE_IMAGEM);
            if (img != null)
                g2.drawImage(img, c.x - w / 2, c.y - h / 2, null);

            int dono = modelo.conquistadorDaCidade(nome);
            if (dono != -1) {
                int y = px(YURT_IMAGEM * 3 / 2);
                g2.drawImage(Desenhos.yurt(modelo.corDoJogador(dono), y), c.x - y / 2, c.y - y / 2, null);
                continue;
            }

            List<String> tesouros = modelo.tesourosNaCidade(nome);
            for (int k = 0; k < tesouros.size() && k < 4; k++) {
                Point q = centroDoQuadrante(c, w, h, k);
                Image t = imgTesouros.get(tesouros.get(k));
                g2.drawImage(t, q.x - t.getWidth(null) / 2, q.y - t.getHeight(null) / 2, null);
            }
        }
    }

    private void desenharMelhorias(Graphics2D g2) {
        List<String> expostas = modelo.melhoriasExpostas();
        for (int k = 0; k < expostas.size(); k++) {
            Point p = posicoesMelhorias.get(k);
            Image img = imgMelhorias.get(expostas.get(k));
            if (p == null || img == null)
                continue;
            Point c = naTela(p);
            g2.drawImage(img, c.x - img.getWidth(null) / 2, c.y - img.getHeight(null) / 2, null);
        }
    }

    private void desenharBonus(Graphics2D g2) {
        for (int k = 0; k < modelo.numeroDeEspacosBonus(); k++) {
            String tipo = modelo.pecaBonusNoEspaco(k);
            Point p = posicoesBonus.get(k);
            Image img = (tipo == null) ? null : imgBonus.get(tipo);
            if (p == null || img == null)
                continue;
            Point c = naTela(p);
            g2.drawImage(img, c.x - img.getWidth(null) / 2, c.y - img.getHeight(null) / 2, null);
        }
    }

    private Point centroDoQuadrante(Point c, int w, int h, int k) {
        int dx = (k % 2 == 0) ? -w / 4 : w / 4;
        int dy = (k < 2) ? -h / 4 : h / 4;
        return new Point(c.x + dx, c.y + dy);
    }

    // ---------- POSIÇÕES DENTRO DE UMA PARADA ----------

    private Point posicaoNaParada(Point base, int n, int total) {
        int tamPeao = px(PEAO_IMAGEM);
        int alturaPeao = tamPeao * 13 / 10;
        int naFileira = Math.min(3, total - (n / 3) * 3);
        double coluna = n % 3 - (naFileira - 1) / 2.0;
        return new Point(base.x + (int) (coluna * tamPeao * 0.8), base.y + (n / 3) * (alturaPeao / 3));
    }

    private Rectangle desenharEmPe(Graphics2D g2, Image img, Point chao) {
        int tamPeao = px(PEAO_IMAGEM);
        int alturaPeao = tamPeao * 13 / 10;
        int x = chao.x - tamPeao / 2;
        int y = chao.y - alturaPeao * 3 / 4;
        g2.drawImage(img, x, y, null);
        return new Rectangle(x, y, tamPeao, alturaPeao);
    }

    private void desenharYurts(Graphics2D g2) {
        int d = px(YURT_IMAGEM);
        for (Integer id : coordenadas.keySet()) {
            List<Integer> donos = modelo.yurtsNaParada(id);
            Point base = naTela(coordenadas.get(id));
            for (int k = 0; k < donos.size(); k++) {
                Point p = posicaoNaParada(base, k, donos.size());
                g2.drawImage(Desenhos.yurt(modelo.corDoJogador(donos.get(k)), d), p.x - d / 2, p.y - d / 2, null);
            }
        }
    }

    // trilha do Kurultai: uma ficha por jogador na casa dos seus votos e a ficha branca na casa do
    // total de votos (ficha neutra). Fichas na mesma casa são empilhadas, cada uma um pouco acima da
    // anterior, para todas ficarem visíveis. O marco de fim do número de jogadores ganha um anel vermelho.
    private void desenharTrilha(Graphics2D g2) {
        Point marco = posicoesTrilha.get(Math.min(modelo.limiteKurultai(), ULTIMA_CASA_TRILHA));
        if (marco != null) {
            Point m = naTela(marco);
            int r = (int) (RAIO_CASA_TRILHA * escala);
            g2.setColor(new Color(200, 20, 20));
            g2.setStroke(new BasicStroke(3f));
            g2.drawOval(m.x - r, m.y - r, 2 * r, 2 * r);
            g2.setStroke(new BasicStroke(1f));
        }

        Map<Integer, Integer> naCasa = new HashMap<Integer, Integer>();
        empilharNaTrilha(g2, "branco", modelo.fichaNeutra(), naCasa);    // a neutra fica embaixo
        for (int j = 0; j < modelo.numeroDeJogadores(); j++)
            empilharNaTrilha(g2, modelo.corDoJogador(j), modelo.votosDe(j), naCasa);
    }

    private void empilharNaTrilha(Graphics2D g2, String cor, int casa, Map<Integer, Integer> naCasa) {
        casa = Math.max(0, Math.min(casa, ULTIMA_CASA_TRILHA));   // além do fim, fica na última casa
        Point p = posicoesTrilha.get(casa);
        if (p == null)
            return;
        int n = naCasa.getOrDefault(casa, 0);
        naCasa.put(casa, n + 1);
        Point c = naTela(p);
        Image ficha = fichaTrilha(cor);
        int y = c.y - (int) (n * DESLOCAMENTO_PILHA * escala);
        g2.drawImage(ficha, c.x - ficha.getWidth(null) / 2, y - ficha.getHeight(null) / 2, null);
    }

    // ficha redonda vista de lado (um cilindro baixo), da cor do jogador ou branca para a neutra
    private Image fichaTrilha(String cor) {
        Image pronta = fichasTrilha.get(cor);
        if (pronta != null)
            return pronta;
        int d = px(FICHA_TRILHA);
        int alturaTopo = d * 3 / 5;
        int lado = Math.max(4, d / 6);
        java.awt.image.BufferedImage img = new java.awt.image.BufferedImage(d + 2, alturaTopo + lado + 2,
                java.awt.image.BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = img.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        Color base = Desenhos.cor(cor);
        // lateral
        g.setColor(base.darker());
        g.fillRect(1, alturaTopo / 2 + 1, d - 1, lado);
        g.fillOval(1, lado + 1, d - 1, alturaTopo);
        // topo
        g.setColor(base);
        g.fillOval(1, 1, d - 1, alturaTopo);
        g.setColor(Color.BLACK);
        g.setStroke(new BasicStroke(1.4f));
        g.drawOval(1, 1, d - 1, alturaTopo);
        g.drawArc(1, lado + 1, d - 1, alturaTopo, 180, 180);
        g.drawLine(1, alturaTopo / 2 + 1, 1, alturaTopo / 2 + lado + 1);
        g.drawLine(d, alturaTopo / 2 + 1, d, alturaTopo / 2 + lado + 1);
        g.dispose();
        fichasTrilha.put(cor, img);
        return img;
    }

    private void desenharKhan(Graphics2D g2) {
        Point ponto = null;
        if (modelo.khanNasMelhorias())
            ponto = posicaoKhanMelhorias;
        else if (modelo.provinciaDoKhan() != -1)
            ponto = posicoesKhan.get(modelo.provinciaDoKhan());
        if (ponto == null)
            return;   
        desenharEmPe(g2, Desenhos.peao("branco", px(PEAO_IMAGEM)), naTela(ponto));
    }

    private void desenharPeoes(Graphics2D g2) {
        int tamPeao = px(PEAO_IMAGEM);
        areasDosPeoes.clear();
        ordemDosPeoes.clear();

        Map<Integer, Integer> total = new HashMap<Integer, Integer>();
        for (int j = 0; j < modelo.numeroDeJogadores(); j++)
            for (int p = 0; p < modelo.numeroDePeoes(j); p++) {
                int id = modelo.posicaoDoPeao(j, p);
                total.put(id, total.getOrDefault(id, 0) + 1);
            }

        Map<Integer, Integer> desenhados = new HashMap<Integer, Integer>();
        for (int j = 0; j < modelo.numeroDeJogadores(); j++) {
            Image img = Desenhos.peao(modelo.corDoJogador(j), tamPeao);
            for (int p = 0; p < modelo.numeroDePeoes(j); p++) {
                int id = modelo.posicaoDoPeao(j, p);
                if (!coordenadas.containsKey(id))
                    continue;
                int n = desenhados.getOrDefault(id, 0);
                desenhados.put(id, n + 1);
                Point chao = posicaoNaParada(naTela(coordenadas.get(id)), n, total.get(id));

                if (j == jogadorSelecionado && p == peaoSelecionado) {
                    Image anel = Desenhos.selecao(tamPeao);
                    g2.drawImage(anel, chao.x - tamPeao / 2 - 6, chao.y - tamPeao * 13 / 10 * 3 / 4 - 6, null);
                }
                String chave = j + ":" + p;
                areasDosPeoes.put(chave, desenharEmPe(g2, img, chao));
                ordemDosPeoes.add(chave);
            }
        }
    }

    private void etiqueta(Graphics2D g2, String texto, int x, int y) {
        g2.setFont(new Font("SansSerif", Font.BOLD, 12));
        FontMetrics fm = g2.getFontMetrics();
        int w = fm.stringWidth(texto) + 6;
        g2.setColor(new Color(255, 255, 255, 220));
        g2.fillRoundRect(x - 3, y - fm.getAscent(), w, fm.getHeight(), 6, 6);
        g2.setColor(Color.BLACK);
        g2.drawString(texto, x, y);
    }

    private void desenharPontosMedidos(Graphics2D g2) {
        g2.setColor(Color.RED);
        Map<Integer, Point> pontos = coordenadas;
        if (MODO_MEDICAO == MEDIR_KHAN)
            pontos = posicoesKhan;
        else if (MODO_MEDICAO == MEDIR_TRIBUTOS)
            pontos = posicoesTributos;
        else if (MODO_MEDICAO == MEDIR_MELHORIAS)
            pontos = posicoesMelhorias;
        else if (MODO_MEDICAO == MEDIR_BONUS)
            pontos = posicoesBonus;
        else if (MODO_MEDICAO == MEDIR_TRILHA)
            pontos = posicoesTrilha;
        if (MODO_MEDICAO == MEDIR_CIDADES) {
            for (String nome : posicoesCidades.keySet()) {
                Point p = naTela(posicoesCidades.get(nome));
                g2.fillOval(p.x - 5, p.y - 5, 10, 10);
                g2.drawString(nome, p.x + 7, p.y - 7);
            }
            return;
        }
        for (Integer id : pontos.keySet()) {
            Point p = naTela(pontos.get(id));
            g2.fillOval(p.x - 5, p.y - 5, 10, 10);
            g2.drawString(String.valueOf(id), p.x + 7, p.y - 7);
        }
    }

    // ---------- MEDIÇÃO (só com MODO_MEDICAO diferente de NADA) ----------

    private class MedidorClique extends MouseAdapter {
        @Override
        public void mouseClicked(MouseEvent e) {
            int x = (int) Math.round(e.getX() / escala);
            int y = (int) Math.round(e.getY() / escala);
            if (MODO_MEDICAO == MEDIR_PARADAS) {
                System.out.println("coordenadas.put(" + proximoId + ", new Point(" + x + ", " + y + "));");
                proximoId++;
                return;
            }
            medir(x, y);
        }
    }

    private void prepararMedicao() {
        if (MODO_MEDICAO == MEDIR_BONUS || MODO_MEDICAO == MEDIR_TRILHA) {
            avisarProxima();
            return;
        }
        if (MODO_MEDICAO == MEDIR_MELHORIAS) {
            avisarProxima();
            return;
        }
        if (MODO_MEDICAO == MEDIR_CIDADES) {
            cidadesParaMedir.addAll(modelo.nomesDasCidades());
            Collections.sort(cidadesParaMedir);
            avisarProxima();
            return;
        }
        for (int id : modelo.idsDasProvincias())
            if (MODO_MEDICAO == MEDIR_TRIBUTOS || modelo.provinciaDoKhan(id))
                paraMedir.add(id);
        Collections.sort(paraMedir);
        avisarProxima();
    }

    private void medir(int x, int y) {
        if (MODO_MEDICAO == MEDIR_TRILHA) {
            if (proximoId <= ULTIMA_CASA_TRILHA)
                System.out.println("posicoesTrilha.put(" + proximoId + ", new Point(" + x + ", " + y + "));");
            proximoId++;
            avisarProxima();
            return;
        }
        if (MODO_MEDICAO == MEDIR_BONUS) {
            if (proximoId < modelo.numeroDeEspacosBonus())
                System.out.println("posicoesBonus.put(" + proximoId + ", new Point(" + x + ", " + y + "));");
            proximoId++;
            avisarProxima();
            return;
        }
        if (MODO_MEDICAO == MEDIR_MELHORIAS) {
            if (proximoId < MELHORIAS_EXPOSTAS)
                System.out.println("posicoesMelhorias.put(" + proximoId + ", new Point(" + x + ", " + y + "));");
            proximoId++;
            avisarProxima();
            return;
        }
        if (MODO_MEDICAO == MEDIR_CIDADES) {
            if (proximoId < cidadesParaMedir.size())
                System.out.println("posicoesCidades.put(\"" + cidadesParaMedir.get(proximoId) + "\", new Point("
                        + x + ", " + y + "));");
            proximoId++;
            avisarProxima();
            return;
        }
        String mapa = (MODO_MEDICAO == MEDIR_KHAN) ? "posicoesKhan" : "posicoesTributos";
        if (proximoId < paraMedir.size()) {
            int id = paraMedir.get(proximoId);
            System.out.println(mapa + ".put(" + id + ", new Point(" + x + ", " + y + "));    // província "
                    + (char) ('a' + id));
        } else if (MODO_MEDICAO == MEDIR_KHAN && proximoId == paraMedir.size()) {
            System.out.println("posicaoKhanMelhorias = new Point(" + x + ", " + y + ");");
        }
        proximoId++;
        avisarProxima();
    }

    private void avisarProxima() {
        if (MODO_MEDICAO == MEDIR_TRILHA) {
            if (proximoId <= ULTIMA_CASA_TRILHA)
                System.out.println("Agora clique no centro da casa " + proximoId + " da trilha do Kurultai"
                        + (proximoId == 0 ? " (a da cadeira)" : ""));
            else
                System.out.println("Pronto: copie as linhas acima");
            return;
        }
        if (MODO_MEDICAO == MEDIR_BONUS) {
            if (proximoId < modelo.numeroDeEspacosBonus())
                System.out.println("Agora clique no centro do espaço de bônus " + (proximoId + 1)
                        + " de " + modelo.numeroDeEspacosBonus());
            else
                System.out.println("Pronto: copie as linhas acima");
            return;
        }
        if (MODO_MEDICAO == MEDIR_MELHORIAS) {
            if (proximoId < MELHORIAS_EXPOSTAS)
                System.out.println("Agora clique no centro do espaço de melhoria " + (proximoId + 1)
                        + " de " + MELHORIAS_EXPOSTAS);
            else
                System.out.println("Pronto: copie as linhas acima");
            return;
        }
        if (MODO_MEDICAO == MEDIR_CIDADES) {
            if (proximoId < cidadesParaMedir.size())
                System.out.println("Agora clique no centro de " + cidadesParaMedir.get(proximoId));
            else
                System.out.println("Pronto: copie as linhas acima");
            return;
        }
        String oQue = (MODO_MEDICAO == MEDIR_KHAN) ? "o Khan fica" : "ficam os tributos";
        if (proximoId < paraMedir.size())
            System.out.println("Agora clique onde " + oQue + " na província " + (char) ('a' + paraMedir.get(proximoId)));
        else if (MODO_MEDICAO == MEDIR_KHAN && proximoId == paraMedir.size())
            System.out.println("Agora clique onde o Khan fica na área de melhorias");
        else
            System.out.println("Pronto: copie as linhas acima");
    }
}