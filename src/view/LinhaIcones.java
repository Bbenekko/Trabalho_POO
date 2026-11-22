package view;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

// Uma linha da lateral com ícones e números lado a lado (ex.: moeda × 2, espada × 1).
// Tudo é desenhado com drawImage/drawString; um item "virtual" ganha um anel tracejado roxo,
// para distinguir os recursos que só valem no turno atual.
class LinhaIcones extends JPanel {
    static final int ICONE = 22;
    private static final int ALTURA = 26;
    private static final int ESPACO = 10;
    private static final Color COR_VIRTUAL = new Color(120, 60, 170);

    private List<Image> icones = new ArrayList<Image>();
    private List<String> textos = new ArrayList<String>();
    private List<Boolean> virtuais = new ArrayList<Boolean>();
    private List<Color> cores = new ArrayList<Color>();

    LinhaIcones(int largura) {
        Dimension d = new Dimension(largura, ALTURA);
        setPreferredSize(d);
        setMaximumSize(d);
        setMinimumSize(d);
        setOpaque(false);
    }

    // linha vazia some da lateral (o BoxLayout ignora componentes invisíveis) e volta ao receber um item
    void limpar() {
        icones.clear();
        textos.clear();
        virtuais.clear();
        cores.clear();
        setVisible(false);
    }

    void item(Image icone, String texto) {
        adicionar(icone, texto, false, Color.BLACK);
    }

    void itemVirtual(Image icone, String texto) {
        adicionar(icone, texto, true, COR_VIRTUAL);
    }

    void texto(String texto, Color cor) {
        adicionar(null, texto, false, cor);
    }

    private void adicionar(Image icone, String texto, boolean virtual, Color cor) {
        icones.add(icone);
        textos.add(texto);
        virtuais.add(virtual);
        cores.add(cor);
        setVisible(true);
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2.setFont(new Font("SansSerif", Font.PLAIN, 12));
        FontMetrics fm = g2.getFontMetrics();
        int y = (ALTURA - ICONE) / 2;
        int base = ALTURA / 2 + fm.getAscent() / 2 - 1;
        int x = 0;
        for (int i = 0; i < textos.size(); i++) {
            Image icone = icones.get(i);
            if (icone != null) {
                g2.drawImage(icone, x + (ICONE - icone.getWidth(null)) / 2, y + (ICONE - icone.getHeight(null)) / 2, null);
                if (virtuais.get(i)) {
                    g2.setColor(COR_VIRTUAL);
                    g2.setStroke(new BasicStroke(1.6f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 1f,
                            new float[] {3f, 2f}, 0f));
                    g2.drawOval(x - 2, y - 2, ICONE + 3, ICONE + 3);
                    g2.setStroke(new BasicStroke(1f));
                }
                x += ICONE + 4;
            }
            g2.setColor(cores.get(i));
            g2.drawString(textos.get(i), x, base);
            x += fm.stringWidth(textos.get(i)) + ESPACO;
        }
    }
}