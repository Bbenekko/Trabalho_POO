package view;

import javax.swing.*;
import java.awt.*;
import java.util.List;

import controller.ControladorJogo;
import model.ModelFacade;
import model.ObservadoIF;
import model.ObservadorIF;

// Janela de cartas de conselheiro. É um observador (Cap. 17): aparece quando a fachada
// notifica ABRIR_CARTAS e se redesenha quando uma carta aberta é retirada.
public class JanelaCartas extends JFrame implements ObservadorIF {
    private static final int LARGURA_CARTA = 160;
    private static final int ESPACO = 20;

    private ModelFacade modelo = ModelFacade.getInstance();

    public JanelaCartas() {
        setTitle("Cartas de conselheiro");
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        setResizable(false);
        setContentPane(new PainelCartas());
        pack();
        ControladorJogo.getInstance().registra(this);   // Cap. 17: registro pelo controlador
    }

    @Override
    public void notify(ObservadoIF o) {
        if (o.get(ModelFacade.EVENTO) == ModelFacade.ABRIR_CARTAS) {
            setLocationRelativeTo(null);
            setVisible(true);
            toFront();
        }
        if (isVisible())
            repaint();
    }

    private class PainelCartas extends JPanel {
        PainelCartas() {
            int largura = ESPACO + 5 * (LARGURA_CARTA + ESPACO) + ESPACO;
            setPreferredSize(new Dimension(largura, LARGURA_CARTA * 3 / 2 + 80));
            setBackground(new Color(60, 45, 35));
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (!modelo.jogoIniciado())
                return;
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            g2.setColor(Color.WHITE);
            g2.setFont(new Font("SansSerif", Font.BOLD, 14));

            // as 4 abertas: valem para todos
            g2.drawString("Cartas abertas (1 voto para o primeiro que cumprir)", ESPACO, 26);
            List<String> abertas = modelo.cartasAbertas();
            int x = ESPACO;
            for (int i = 0; i < abertas.size(); i++) {
                g2.drawImage(Desenhos.carta(abertas.get(i), LARGURA_CARTA, modelo.cartaAbertaRetirada(i)), x, 40, null);
                x += LARGURA_CARTA + ESPACO;
            }

            // a secreta só do jogador da vez
            int vez = modelo.jogadorDaVez();
            String secreta = modelo.cartaSecreta(vez);
            x += ESPACO;
            g2.setColor(Color.WHITE);
            g2.drawString("Secreta · jogador " + (vez + 1), x, 26);
            if (secreta != null)
                g2.drawImage(Desenhos.carta(secreta, LARGURA_CARTA, false), x, 40, null);
        }
    }
}
