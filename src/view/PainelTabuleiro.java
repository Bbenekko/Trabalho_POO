package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

import controller.ControladorJogo;

class PainelTabuleiro extends JPanel {
    private static final int LARGURA = 995;
    private static final int ALTURA = 1000;
    private static final int RAIO = 25; 
    private static final int TAM_PEAO = 30;

    // true: clique imprime coordenadas e mostra as paradas medidas
    // false: jogo normal (deixe false na entrega)
    private static final boolean MODO_MEDICAO = true;

    private ControladorJogo controlador;
    private Image imgTabuleiro;
    private Map<Integer, Point> coordenadas = new HashMap<Integer, Point>();
    private Map<String, Image> imgPeoes = new HashMap<String, Image>();
    private int proximoId = 0; 

    PainelTabuleiro(ControladorJogo c) {
        this.controlador = c;
        carregarImagens();
        carregarCoordenadas();
        setPreferredSize(new Dimension(LARGURA, ALTURA));
        addMouseListener(new TratadorClique());
    }

    private void carregarImagens() {
        Image original = Imagens.carregar("doc/img/tabuleiro.jpg");
        if (original != null)
            imgTabuleiro = original.getScaledInstance(LARGURA, ALTURA, Image.SCALE_SMOOTH);
    }

    // cole aqui as 34 linhas impressas no Console pelo modo de medição
    private void carregarCoordenadas() {
        // coordenadas.put(0, new Point(132, 88));
        // ...
        // coordenadas.put(33, new Point(500, 500));
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

        for (int j = 0; j < controlador.numeroDeJogadores(); j++) {
            Image img = imagemDoPeao(controlador.corDoJogador(j));

            for (int p = 0; p < controlador.numeroDePeoes(j); p++) {
                int id = controlador.posicaoDoPeao(j, p);
                Point base = coordenadas.get(id);
                if (base == null)
                    continue;

                int n = naParada.getOrDefault(id, 0);
                naParada.put(id, n + 1);

                int dx = (n % 3 - 1) * (TAM_PEAO / 2);
                int dy = (n / 3) * (TAM_PEAO / 2);
                int x = base.x + dx - TAM_PEAO / 2;
                int y = base.y + dy - TAM_PEAO / 2;
                g2.drawImage(img, x, y, null);
            }
        }
    }

    private void desenharParadasMedidas(Graphics2D g2) {
        g2.setColor(Color.RED);
        for (Integer id : coordenadas.keySet()) {
            Point p = coordenadas.get(id);
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
        BufferedImage img = new BufferedImage(TAM_PEAO, TAM_PEAO, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = img.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setColor(cor);
        g.fillOval(2, 2, TAM_PEAO - 4, TAM_PEAO - 4);
        g.setColor(Color.BLACK);
        g.setStroke(new BasicStroke(2));
        g.drawOval(2, 2, TAM_PEAO - 4, TAM_PEAO - 4);
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

    private int paradaEm(int x, int y) {
        int maisProxima = -1;
        double menorDistancia = RAIO;
        for (Integer id : coordenadas.keySet()) {
            double d = coordenadas.get(id).distance(x, y);
            if (d <= menorDistancia) {
                menorDistancia = d;
                maisProxima = id;
            }
        }
        return maisProxima;
    }

    private class TratadorClique extends MouseAdapter {
        @Override
        public void mouseClicked(MouseEvent e) {
            if (MODO_MEDICAO) {
                System.out.println("coordenadas.put(" + proximoId + ", new Point("
                        + e.getX() + ", " + e.getY() + "));");
                proximoId++;
                return;
            }
            int id = paradaEm(e.getX(), e.getY());
            if (id != -1)
                controlador.cliqueNaParada(id);
        }
    }
}