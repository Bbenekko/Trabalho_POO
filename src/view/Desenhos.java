package view;

import java.awt.*;
import java.awt.geom.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

// Gera, uma única vez, as imagens das peças que não têm arquivo próprio.
// Quem desenha na tela só usa drawImage com estas imagens prontas.
class Desenhos {
    private static Map<String, Image> cache = new HashMap<String, Image>();

    // ---------- IMAGENS DAS CARTAS (ajuste os caminhos; sem arquivo, usa a carta gerada) ----------

    // cartas de conselheiro aberto
    private static final String IMG_CARTA_QUATRO_ESPADAS = "doc/img/cartas/cartas1/cartas1-1.jpg";
    private static final String IMG_CARTA_SEIS_YURTS = "doc/img/cartas/cartas1/cartas1-2.jpg";
    private static final String IMG_CARTA_DEZ_YURTS = "doc/img/cartas/cartas1/cartas1-3.jpg";
    private static final String IMG_CARTA_QUATRO_TESOUROS = "doc/img/cartas/cartas1/cartas1-4.jpg";
    private static final String IMG_CARTA_DOIS_YURTS_CADA_REGIAO = "doc/img/cartas/cartas1/cartas1-5.jpg";
    private static final String IMG_CARTA_UM_YURT_CADA_REGIAO = "doc/img/cartas/cartas1/cartas1-6.jpg";
    private static final String IMG_CARTA_DUAS_PECAS_BONUS = "doc/img/cartas/cartas1/cartas1-7.jpg";
    private static final String IMG_CARTA_TRES_PECAS_BONUS = "doc/img/cartas/cartas1/cartas1-8.jpg";
    private static final String IMG_CARTA_CINCO_TESOUROS_DIFERENTES = "doc/img/cartas/cartas1/cartas1-9.jpg";
    
    private static final String IMG_CARTA_TRES_CIDADES = "doc/img/cartas/cartas2/cartas2-1.jpg";
    private static final String IMG_CARTA_DUAS_CIDADES = "doc/img/cartas/cartas2/cartas2-2.jpg";
    private static final String IMG_CARTA_LINHA_DE_MELHORIAS = "doc/img/cartas/cartas2/cartas2-3.jpg";

    private static final String IMG_CARTA_QUATRO_MELHORIAS_NUMA_COLUNA = "doc/img/cartas/cartas3/cartas3-1.jpg";
    private static final String IMG_CARTA_DUAS_COLUNAS_COM_DUAS = "doc/img/cartas/cartas3/cartas3-2.jpg";


    // cartas de conselheiro secreto
    
    private static final String IMG_CARTA_SACERDOTE = "doc/img/cartas/cartas4/cartas4-1.jpg";
    private static final String IMG_CARTA_COLECIONADOR = "doc/img/cartas/cartas4/cartas4-2.jpg";
    
    private static final String IMG_CARTA_VIAJANTE = "doc/img/cartas/cartas5/cartas5-1.jpg";
    private static final String IMG_CARTA_SOLDADO = "doc/img/cartas/cartas5/cartas5-2.jpg";
    
    private static final String IMG_CARTA_CIDADAO = "doc/img/cartas/cartas6/cartas6-1.jpg";
    private static final String IMG_CARTA_CAVALEIRO = "doc/img/cartas/cartas6/cartas6-2.jpg";
    
    private static final String IMG_CARTA_MENSAGEIRO = "doc/img/cartas/cartas7/cartas7-1.jpg";
    private static final String IMG_CARTA_GOVERNADOR = "doc/img/cartas/cartas7/cartas7-2.jpg";
    
    private static final String IMG_CARTA_PREFEITO = "doc/img/cartas/cartas8/cartas8-1.jpg";
    private static final String IMG_CARTA_BAJULADOR = "doc/img/cartas/cartas8/cartas8-2.jpg";

    // tipo da carta (nome do enum no Model) → caminho da imagem
    private static Map<String, String> arquivosCartas = new HashMap<String, String>();

    static {
        arquivosCartas.put("quatro_espadas", IMG_CARTA_QUATRO_ESPADAS);
        arquivosCartas.put("seis_yurts", IMG_CARTA_SEIS_YURTS);
        arquivosCartas.put("dez_yurts", IMG_CARTA_DEZ_YURTS);
        arquivosCartas.put("quatro_tesouros", IMG_CARTA_QUATRO_TESOUROS);
        arquivosCartas.put("cinco_tesouros_diferentes", IMG_CARTA_CINCO_TESOUROS_DIFERENTES);
        arquivosCartas.put("um_yurt_cada_regiao", IMG_CARTA_UM_YURT_CADA_REGIAO);
        arquivosCartas.put("dois_yurts_cada_regiao", IMG_CARTA_DOIS_YURTS_CADA_REGIAO);
        arquivosCartas.put("duas_pecas_bonus", IMG_CARTA_DUAS_PECAS_BONUS);
        arquivosCartas.put("tres_pecas_bonus", IMG_CARTA_TRES_PECAS_BONUS);
        arquivosCartas.put("duas_cidades", IMG_CARTA_DUAS_CIDADES);
        arquivosCartas.put("tres_cidades", IMG_CARTA_TRES_CIDADES);
        arquivosCartas.put("linha_de_melhorias", IMG_CARTA_LINHA_DE_MELHORIAS);
        arquivosCartas.put("duas_colunas_com_duas", IMG_CARTA_DUAS_COLUNAS_COM_DUAS);
        arquivosCartas.put("quatro_melhorias_numa_coluna", IMG_CARTA_QUATRO_MELHORIAS_NUMA_COLUNA);
        arquivosCartas.put("viajante", IMG_CARTA_VIAJANTE);
        arquivosCartas.put("soldado", IMG_CARTA_SOLDADO);
        arquivosCartas.put("colecionador", IMG_CARTA_COLECIONADOR);
        arquivosCartas.put("sacerdote", IMG_CARTA_SACERDOTE);
        arquivosCartas.put("governador", IMG_CARTA_GOVERNADOR);
        arquivosCartas.put("bajulador", IMG_CARTA_BAJULADOR);
        arquivosCartas.put("prefeito", IMG_CARTA_PREFEITO);
        arquivosCartas.put("cidadao", IMG_CARTA_CIDADAO);
        arquivosCartas.put("cavaleiro", IMG_CARTA_CAVALEIRO);
        arquivosCartas.put("mensageiro", IMG_CARTA_MENSAGEIRO);
    }

    private Desenhos() {
    }

    // ---------- CORES ----------

    static Color cor(String nome) {
        switch (nome) {
            case "vermelho": return new Color(210, 30, 30);
            case "azul":     return new Color(30, 70, 200);
            case "verde":    return new Color(0, 140, 40);
            case "amarelo":  return new Color(240, 200, 0);
            case "preto":    return new Color(45, 45, 45);
            case "branco":   return new Color(245, 245, 240);   // o Khan
            default:         return Color.GRAY;
        }
    }

    static Color corDaRegiao(String regiao) {
        switch (regiao) {
            case "russia": return new Color(214, 170, 40);
            case "persia": return new Color(40, 140, 70);
            case "china":  return new Color(200, 60, 50);
            default:       return new Color(235, 235, 235);   // coluna branca
        }
    }

    static String nomeDaRegiao(String regiao) {
        switch (regiao) {
            case "russia": return "Rússia";
            case "persia": return "Pérsia";
            case "china":  return "China";
            default:       return "Branca";
        }
    }

    private static boolean escura(Color c) {
        return c.getRed() + c.getGreen() + c.getBlue() < 200;
    }

    private static Graphics2D preparar(BufferedImage img) {
        Graphics2D g = img.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
        return g;
    }

    // ---------- PEÃO (silhueta de xadrez) ----------

    static Image peao(String cor, int largura) {
        String chave = "peao:" + cor + ":" + largura;
        Image img = cache.get(chave);
        if (img == null) {
            img = criarPeao(cor(cor), largura);
            cache.put(chave, img);
        }
        return img;
    }

    private static Image criarPeao(Color cor, int largura) {
        int altura = largura * 13 / 10;
        BufferedImage img = new BufferedImage(largura, altura, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = preparar(img);
        g.scale(largura / 100.0, altura / 130.0);   // medidas de um peão de 100 x 130

        Area peao = new Area(new Ellipse2D.Double(32, 6, 36, 36));
        peao.add(new Area(new RoundRectangle2D.Double(30, 44, 40, 9, 8, 8)));
        Path2D corpo = new Path2D.Double();
        corpo.moveTo(40, 52);
        corpo.curveTo(40, 78, 30, 96, 20, 106);
        corpo.lineTo(80, 106);
        corpo.curveTo(70, 96, 60, 78, 60, 52);
        corpo.closePath();
        peao.add(new Area(corpo));
        peao.add(new Area(new RoundRectangle2D.Double(10, 104, 80, 20, 12, 12)));

        Color contorno = escura(cor) ? new Color(210, 210, 210) : Color.BLACK;
        g.setPaint(new GradientPaint(15, 0, cor.brighter(), 85, 0, cor.darker()));
        g.fill(peao);
        g.setColor(new Color(255, 255, 255, 110));
        g.fill(new Ellipse2D.Double(39, 12, 12, 12));
        g.setColor(contorno);
        g.setStroke(new BasicStroke(4f));
        g.draw(peao);
        g.dispose();
        return img;
    }

    // anel em volta do peão selecionado (Altani)
    static Image selecao(int largura) {
        String chave = "selecao:" + largura;
        Image img = cache.get(chave);
        if (img == null) {
            int t = largura + 12;
            BufferedImage b = new BufferedImage(t, t * 13 / 10, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g = preparar(b);
            g.setColor(new Color(255, 255, 255, 200));
            g.setStroke(new BasicStroke(3f));
            g.drawOval(2, 2, t - 4, t * 13 / 10 - 4);
            g.dispose();
            img = b;
            cache.put(chave, img);
        }
        return img;
    }

    // ---------- YURT (círculo da cor do jogador) ----------

    static Image yurt(String cor, int diametro) {
        String chave = "yurt:" + cor + ":" + diametro;
        Image img = cache.get(chave);
        if (img == null) {
            Color c = cor(cor);
            BufferedImage b = new BufferedImage(diametro, diametro, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g = preparar(b);
            g.setPaint(new GradientPaint(0, 0, c.brighter(), diametro, diametro, c.darker()));
            g.fillOval(2, 2, diametro - 4, diametro - 4);
            g.setColor(escura(c) ? new Color(210, 210, 210) : Color.BLACK);
            g.setStroke(new BasicStroke(2f));
            g.drawOval(2, 2, diametro - 4, diametro - 4);
            g.dispose();
            img = b;
            cache.put(chave, img);
        }
        return img;
    }

    // ---------- FICHA DE TRIBUTO (moeda, espada, yurt) ----------

    static Image tributo(String tipo, int largura) {
        String chave = "tributo:" + tipo + ":" + largura;
        Image img = cache.get(chave);
        if (img == null) {
            BufferedImage b = new BufferedImage(largura, largura, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g = preparar(b);
            Color fundo;
            switch (tipo) {
                case "moeda":  fundo = new Color(230, 180, 40); break;
                case "espada": fundo = new Color(150, 160, 175); break;
                default:       fundo = new Color(170, 110, 60); break;   // yurt
            }
            g.setColor(fundo);
            g.fillOval(1, 1, largura - 3, largura - 3);
            g.setColor(Color.BLACK);
            g.setStroke(new BasicStroke(1.5f));
            g.drawOval(1, 1, largura - 3, largura - 3);
            g.setFont(new Font("SansSerif", Font.BOLD, Math.max(9, largura * 6 / 10)));
            String letra = tipo.substring(0, 1).toUpperCase();
            FontMetrics fm = g.getFontMetrics();
            g.drawString(letra, (largura - fm.stringWidth(letra)) / 2, (largura + fm.getAscent()) / 2 - 2);
            g.dispose();
            img = b;
            cache.put(chave, img);
        }
        return img;
    }

    // ---------- TESOURO (losango colorido) ----------

    static Color corDoTesouro(String tipo) {
        switch (tipo) {
            case "pele":        return new Color(140, 90, 50);
            case "tecido":      return new Color(170, 60, 160);
            case "gado":        return new Color(90, 60, 30);
            case "ferro":       return new Color(110, 120, 130);
            default:            return new Color(220, 120, 20);   // especiarias
        }
    }

    static Image tesouro(String tipo, int largura) {
        String chave = "tesouro:" + tipo + ":" + largura;
        Image img = cache.get(chave);
        if (img == null) {
            BufferedImage b = new BufferedImage(largura, largura, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g = preparar(b);
            Path2D losango = new Path2D.Double();
            losango.moveTo(largura / 2.0, 1);
            losango.lineTo(largura - 1, largura / 2.0);
            losango.lineTo(largura / 2.0, largura - 1);
            losango.lineTo(1, largura / 2.0);
            losango.closePath();
            g.setColor(corDoTesouro(tipo));
            g.fill(losango);
            g.setColor(Color.WHITE);
            g.setStroke(new BasicStroke(1.5f));
            g.draw(losango);
            g.dispose();
            img = b;
            cache.put(chave, img);
        }
        return img;
    }

    // ---------- MELHORIA (peça quadrada da cor da região) ----------

    static String nomeDaMelhoria(String descricao) {
        String[] partes = descricao.split(":");
        String nome;
        switch (partes[0]) {
            case "moeda_virtual":    nome = "Moeda"; break;
            case "espada_virtual":   nome = "Espada"; break;
            case "yurt_virtual":     nome = "Yurt"; break;
            case "tesouro_virtual":  nome = "Tesouro"; break;
            case "pegar_tributo":    nome = "Tributo"; break;
            default:                 nome = "2 movim."; break;
        }
        if (partes.length > 2)
            nome += " (" + partes[2] + ")";
        return nome;
    }

    static Image melhoria(String descricao, int largura) {
        String chave = "melhoria:" + descricao + ":" + largura;
        Image img = cache.get(chave);
        if (img == null) {
            String[] partes = descricao.split(":");
            Color c = corDaRegiao(partes.length > 1 ? partes[1] : "");
            BufferedImage b = new BufferedImage(largura, largura * 2 / 3, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g = preparar(b);
            int h = largura * 2 / 3;
            g.setColor(c);
            g.fillRoundRect(1, 1, largura - 3, h - 3, 10, 10);
            g.setColor(Color.BLACK);
            g.setStroke(new BasicStroke(1.5f));
            g.drawRoundRect(1, 1, largura - 3, h - 3, 10, 10);
            g.setColor(escura(c) ? Color.WHITE : Color.BLACK);
            g.setFont(new Font("SansSerif", Font.BOLD, Math.max(9, largura / 7)));
            String nome = nomeDaMelhoria(descricao);
            FontMetrics fm = g.getFontMetrics();
            g.drawString(nome, Math.max(3, (largura - fm.stringWidth(nome)) / 2), (h + fm.getAscent()) / 2 - 2);
            g.dispose();
            img = b;
            cache.put(chave, img);
        }
        return img;
    }

    // ---------- CARTA (usa o arquivo se existir; senão gera uma carta de pergaminho) ----------

    static Image carta(String tipo, int largura, boolean retirada) {
        String chave = "carta:" + tipo + ":" + largura + ":" + retirada;
        Image img = cache.get(chave);
        if (img == null) {
            int altura = largura * 3 / 2;
            BufferedImage b = new BufferedImage(largura, altura, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g = preparar(b);
            String caminho = arquivosCartas.get(tipo);
            Image arquivo = (caminho == null) ? null : Imagens.carregar(caminho);   // avisa no Console se faltar
            if (arquivo != null) {
                g.drawImage(arquivo, 0, 0, largura, altura, null);
            } else {
                g.setColor(new Color(240, 225, 185));
                g.fillRoundRect(2, 2, largura - 4, altura - 4, 16, 16);
                g.setColor(new Color(120, 80, 30));
                g.setStroke(new BasicStroke(3f));
                g.drawRoundRect(2, 2, largura - 4, altura - 4, 16, 16);
                String[] texto = TextosCartas.texto(tipo);
                g.setFont(new Font("Serif", Font.BOLD, Math.max(11, largura / 9)));
                escreverCentralizado(g, texto[0], largura, altura / 5);
                g.setFont(new Font("SansSerif", Font.PLAIN, Math.max(10, largura / 13)));
                int y = altura * 2 / 5;
                for (int i = 1; i < texto.length; i++) {
                    escreverCentralizado(g, texto[i], largura, y);
                    y += g.getFontMetrics().getHeight();
                }
            }
            if (retirada) {
                g.setColor(new Color(0, 0, 0, 140));
                g.fillRoundRect(2, 2, largura - 4, altura - 4, 16, 16);
                g.setColor(Color.WHITE);
                g.setFont(new Font("SansSerif", Font.BOLD, Math.max(11, largura / 8)));
                escreverCentralizado(g, "RETIRADA", largura, altura / 2);
            }
            g.dispose();
            img = b;
            cache.put(chave, img);
        }
        return img;
    }

    private static void escreverCentralizado(Graphics2D g, String s, int largura, int y) {
        FontMetrics fm = g.getFontMetrics();
        g.drawString(s, Math.max(4, (largura - fm.stringWidth(s)) / 2), y);
    }
}