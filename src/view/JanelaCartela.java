package view;

import javax.swing.*;
import java.awt.*;
import java.util.List;

import controller.ControladorJogo;
import model.ModelFacade;
import model.ObservadoIF;
import model.ObservadorIF;

// Janela da cartela (tabuleiro do jogador) com as 4 colunas. É um observador (Cap. 17):
// aparece quando a fachada notifica ABRIR_CARTELA e se redesenha a cada mudança de estado.
public class JanelaCartela extends JFrame implements ObservadorIF {
    private static final int LARGURA = 860;
    private static final int ALTURA = 560;
    private static final int LARGURA_COLUNA = 160;
    private static final int LARGURA_MELHORIA = 132;
    private static final int LARGURA_CARTA = 120;

    private ModelFacade modelo = ModelFacade.getInstance();
    private int jogador = 0;

    public JanelaCartela() {
        setTitle("Cartela");
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        setResizable(false);
        setContentPane(new PainelCartela());
        pack();
        ControladorJogo.getInstance().registra(this);   // Cap. 17: registro pelo controlador
    }

    @Override
    public void notify(ObservadoIF o) {
        int evento = o.get(ModelFacade.EVENTO);
        if (evento == ModelFacade.ABRIR_CARTELA) {
            jogador = o.get(ModelFacade.JOGADOR_DA_CARTELA);
            setTitle("Cartela do jogador " + (jogador + 1) + " (" + modelo.corDoJogador(jogador) + ")");
            setLocationRelativeTo(null);
            setVisible(true);
            toFront();
        }
        if (isVisible())
            repaint();
    }

    private class PainelCartela extends JPanel {
        PainelCartela() {
            setPreferredSize(new Dimension(LARGURA, ALTURA));
            setBackground(new Color(245, 238, 220));
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (!modelo.jogoIniciado())
                return;
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

            // as 4 colunas
            for (int c = 0; c < 4; c++) {
                int x = 16 + c * (LARGURA_COLUNA + 8);
                String regiao = modelo.regiaoDaColuna(jogador, c);
                g2.setColor(Desenhos.corDaRegiao(regiao));
                g2.fillRoundRect(x, 16, LARGURA_COLUNA, 36, 10, 10);
                g2.setColor(Color.BLACK);
                g2.drawRoundRect(x, 16, LARGURA_COLUNA, 36, 10, 10);
                g2.drawRoundRect(x, 16, LARGURA_COLUNA, 380, 10, 10);
                g2.setFont(new Font("SansSerif", Font.BOLD, 13));
                g2.drawString((c + 1) + " · " + Desenhos.nomeDaRegiao(regiao), x + 10, 39);

                boolean usada = modelo.colunaAtivada(jogador, c);
                g2.setFont(new Font("SansSerif", Font.PLAIN, 12));
                g2.setColor(usada ? new Color(170, 0, 0) : new Color(0, 110, 0));
                g2.drawString(usada ? "Ativada neste ciclo" : "Livre", x + 10, 72);

                List<String> melhorias = modelo.melhoriasDaColuna(jogador, c);
                int y = 84;
                for (String m : melhorias) {
                    g2.drawImage(Desenhos.melhoria(m, LARGURA_MELHORIA), x + (LARGURA_COLUNA - LARGURA_MELHORIA) / 2, y, null);
                    y += LARGURA_MELHORIA * 2 / 3 + 6;
                }
            }

            // recursos
            int y = 430;
            g2.setColor(Color.BLACK);
            g2.setFont(new Font("SansSerif", Font.BOLD, 14));
            g2.drawString("Recursos", 16, y);
            g2.setFont(new Font("SansSerif", Font.PLAIN, 13));
            int x = 16;
            y += 14;
            for (String t : new String[] {"moeda", "espada", "yurt"}) {
                g2.drawImage(Desenhos.tributo(t, 26), x, y, null);
                g2.drawString("× " + modelo.tributosDoJogador(jogador, t), x + 30, y + 18);
                x += 80;
            }
            x = 16;
            y += 40;
            for (String t : new String[] {"pele", "tecido", "gado", "ferro", "especiarias"}) {
                g2.drawImage(Desenhos.tesouro(t, 22), x, y, null);
                g2.drawString(t + " × " + modelo.tesourosDoJogador(jogador, t), x + 26, y + 16);
                x += 100;
            }
            g2.drawString("Yurts na reserva: " + modelo.yurtsRestantes(jogador)
                    + "  ·  Votos: " + modelo.votosDe(jogador), 16, y + 50);

            // carta secreta (a cartela só é aberta para o jogador da vez)
            String secreta = modelo.cartaSecreta(jogador);
            if (secreta != null) {
                g2.setFont(new Font("SansSerif", Font.BOLD, 13));
                int xc = 16 + 4 * (LARGURA_COLUNA + 8) + 8;
                g2.drawString("Carta secreta", xc, 30);
                g2.drawImage(Desenhos.carta(secreta, LARGURA_CARTA, false), xc, 40, null);
            }
        }
    }
}
