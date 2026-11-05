package view;

import javax.swing.*;
import java.awt.*;
import java.util.List;

import controller.ControladorJogo;
import model.ModelFacade;
import model.ObservadoIF;
import model.ObservadorIF;

// Placar final. É um observador (Cap. 17): aparece quando a fachada notifica FIM_DE_JOGO.
public class JanelaFim extends JFrame implements ObservadorIF {
    private ModelFacade modelo = ModelFacade.getInstance();
    private JPanel tabela = new JPanel();
    private JLabel lblVencedor = new JLabel(" ");

    public JanelaFim() {
        setTitle("Fim de jogo");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        lblVencedor.setFont(new Font("Serif", Font.BOLD, 22));
        lblVencedor.setBorder(BorderFactory.createEmptyBorder(16, 16, 8, 16));
        tabela.setBorder(BorderFactory.createEmptyBorder(8, 16, 16, 16));

        setLayout(new BorderLayout());
        add(lblVencedor, BorderLayout.NORTH);
        add(tabela, BorderLayout.CENTER);
        ControladorJogo.getInstance().registra(this);   // Cap. 17: registro pelo controlador
    }

    @Override
    public void notify(ObservadoIF o) {
        if (o.get(ModelFacade.EVENTO) != ModelFacade.FIM_DE_JOGO)
            return;
        int n = modelo.numeroDeJogadores();
        tabela.removeAll();
        tabela.setLayout(new GridLayout(n + 1, 5, 16, 6));
        String[] cabecalho = {"Jogador", "Herdeiro", "Kurultai", "Carta secreta", "Total"};
        for (String c : cabecalho) {
            JLabel l = new JLabel(c);
            l.setFont(new Font("SansSerif", Font.BOLD, 13));
            tabela.add(l);
        }
        for (int j = 0; j < n; j++) {
            int trilha = modelo.votosDe(j);
            int total = modelo.pontuacaoFinal(j);
            tabela.add(new JLabel((j + 1) + " (" + modelo.corDoJogador(j) + ")"));
            tabela.add(new JLabel(modelo.herdeiroDoJogador(j)));
            tabela.add(new JLabel(String.valueOf(trilha)));
            tabela.add(new JLabel(String.valueOf(total - trilha)));
            tabela.add(new JLabel(String.valueOf(total)));
        }

        List<Integer> vencedores = modelo.vencedores();
        StringBuilder texto = new StringBuilder(vencedores.size() == 1 ? "Vencedor: " : "Empate: ");
        for (int i = 0; i < vencedores.size(); i++) {
            int v = vencedores.get(i);
            if (i > 0)
                texto.append(" e ");
            texto.append("jogador ").append(v + 1).append(" (").append(modelo.corDoJogador(v)).append(")");
        }
        lblVencedor.setText(texto.toString());

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
        toFront();
    }
}
