package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

import model.ModelFacade;

public class PainelTabuleiro extends JPanel {
    private static final int LARGURA_IMAGEM = 2905;
    private static final int ALTURA_IMAGEM = 2919;

    private static final int LARGURA_MAXIMA = 1150;
    private static final int ALTURA_MAXIMA = 1000;
    private static final int LARGURA_LATERAL = 250;
    private static final int BARRA_DE_TITULO = 40;

    private static final int RAIO_IMAGEM = 75;
    private static final int PEAO_IMAGEM = 90;

    private static final boolean MODO_MEDICAO = false;

    private ModelFacade modelo;
    private double escala;
    private int largura;
    private int altura;
    private int tamPeao;
    private Image imgTabuleiro;
    private Map<Integer, Point> coordenadas = new HashMap<Integer, Point>();
    private Map<String, Image> imgPeoes = new HashMap<String, Image>();
    private int proximoId = 0; 

    PainelTabuleiro(ModelFacade modelo) {
        this.modelo = modelo;
        calcularTamanho();
        carregarImagens();
        carregarCoordenadas();
        setPreferredSize(new Dimension(largura, altura));

        if (MODO_MEDICAO)
            addMouseListener(new MedidorClique());
    }

    private void calcularTamanho() {
        Rectangle tela = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();
        int larguraDisponivel = Math.min(LARGURA_MAXIMA, tela.width - LARGURA_LATERAL);
        int alturaDisponivel = Math.min(ALTURA_MAXIMA, tela.height - BARRA_DE_TITULO);

        escala = Math.min((double) larguraDisponivel / LARGURA_IMAGEM,
                          (double) alturaDisponivel / ALTURA_IMAGEM);
        largura = (int) (LARGURA_IMAGEM * escala);
        altura = (int) (ALTURA_IMAGEM * escala);
        tamPeao = Math.max(16, (int) (PEAO_IMAGEM * escala));
    }

    private Point naTela(Point naImagem) {
        return new Point((int) (naImagem.x * escala), (int) (naImagem.y * escala));
    }

    private void carregarImagens() {
        Image original = Imagens.carregar("doc/img/tabuleiro.jpg");
        if (original != null)
            imgTabuleiro = original.getScaledInstance(largura, altura, Image.SCALE_SMOOTH);
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

    public boolean emModoMedicao() {
        return MODO_MEDICAO;
    }

    public int paradaEm(int x, int y) {
        int maisProxima = -1;
        double menorDistancia = RAIO_IMAGEM * escala;
        for (Integer id : coordenadas.keySet()) {
            double d = naTela(coordenadas.get(id)).distance(x, y);
            if (d <= menorDistancia) {
                menorDistancia = d;
                maisProxima = id;
            }
        }
        return maisProxima;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.drawImage(imgTabuleiro, 0, 0, null);
        desenharPeoes(g2);

        if (MODO_MEDICAO)
            desenharParadasMedidas(g2);
    }

    private void desenharPeoes(Graphics2D g2) {
        Map<Integer, Integer> naParada = new HashMap<Integer, Integer>();

        for (int j = 0; j < modelo.numeroDeJogadores(); j++) {
            Image img = imagemDoPeao(modelo.corDoJogador(j));

            for (int p = 0; p < modelo.numeroDePeoes(j); p++) {
                int id = modelo.posicaoDoPeao(j, p);
                if (!coordenadas.containsKey(id))  
                    continue;
                Point base = naTela(coordenadas.get(id));

                int n = naParada.getOrDefault(id, 0);
                naParada.put(id, n + 1);

                int dx = (n % 3 - 1) * (tamPeao * 2 / 3);
                int dy = (n / 3) * (tamPeao * 2 / 3);
                int x = base.x + dx - tamPeao / 2;
                int y = base.y + dy - tamPeao / 2;
                g2.drawImage(img, x, y, null);
            }
        }
    }

    private void desenharParadasMedidas(Graphics2D g2) {
        g2.setColor(Color.RED);
        for (Integer id : coordenadas.keySet()) {
            Point p = naTela(coordenadas.get(id));
            g2.fillOval(p.x - 5, p.y - 5, 10, 10);
            g2.drawString(String.valueOf(id), p.x + 7, p.y - 7);
        }
    }

    private Image imagemDoPeao(String cor) {
        Image img = imgPeoes.get(cor);
        if (img == null) {
            img = criarPeao(converterCor(cor));
            imgPeoes.put(cor, img);
        }
        return img;
    }

    private Image criarPeao(Color cor) {
        BufferedImage img = new BufferedImage(tamPeao, tamPeao, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = img.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setColor(cor);
        g.fillOval(2, 2, tamPeao - 4, tamPeao - 4);
        g.setColor(Color.BLACK);
        g.setStroke(new BasicStroke(2));
        g.drawOval(2, 2, tamPeao - 4, tamPeao - 4);
        g.dispose();
        return img;
    }

    static Color converterCor(String cor) {
        switch (cor) {
            case "vermelho": return Color.RED;
            case "azul":     return Color.BLUE;
            case "verde":    return new Color(0, 150, 0);
            case "amarelo":  return Color.YELLOW;
            case "preto":    return Color.BLACK;
            case "branco":   return Color.WHITE;
            case "roxo":     return new Color(128, 0, 128);
            default:         return Color.GRAY;
        }
    }

    private class MedidorClique extends MouseAdapter {
        @Override
        public void mouseClicked(MouseEvent e) {
            int x = (int) Math.round(e.getX() / escala);
            int y = (int) Math.round(e.getY() / escala);
            System.out.println("coordenadas.put(" + proximoId + ", new Point(" + x + ", " + y + "));");
            proximoId++;
        }
    }
}