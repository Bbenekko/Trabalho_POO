package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import model.ModelFacade;

// Primeira janela: quantos jogadores e a cor e o herdeiro de cada um.
// O botão "Começar" é tratado pelo Controller.
public class JanelaInicial extends JFrame {
    private static final int MAX_JOGADORES = 5;

    private JComboBox<Integer> numero = new JComboBox<Integer>();
    private List<JComboBox<String>> cores = new ArrayList<JComboBox<String>>();
    private List<JComboBox<String>> herdeiros = new ArrayList<JComboBox<String>>();
    private List<JLabel> rotulos = new ArrayList<JLabel>();
    private JButton comecar = new JButton("Começar a partida");
    private JLabel lblAviso = new JLabel(" ");

    public JanelaInicial() {
        ModelFacade modelo = ModelFacade.getInstance();
        List<String> opcoesCor = modelo.coresDisponiveis();
        List<String> opcoesHerdeiro = modelo.herdeirosDisponiveis();

        setTitle("Herdeiros de Khan · nova partida");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        JPanel conteudo = new JPanel(new GridBagLayout());
        conteudo.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(4, 6, 4, 6);
        c.anchor = GridBagConstraints.WEST;
        c.fill = GridBagConstraints.HORIZONTAL;

        JLabel titulo = new JLabel("Herdeiros de Khan");
        titulo.setFont(new Font("Serif", Font.BOLD, 22));
        c.gridx = 0; c.gridy = 0; c.gridwidth = 3;
        conteudo.add(titulo, c);

        c.gridwidth = 1; c.gridy = 1;
        conteudo.add(new JLabel("Número de jogadores"), c);
        for (int n = 2; n <= MAX_JOGADORES; n++)
            numero.addItem(n);
        c.gridx = 1;
        conteudo.add(numero, c);

        c.gridy = 2; c.gridx = 1;
        conteudo.add(new JLabel("Cor"), c);
        c.gridx = 2;
        conteudo.add(new JLabel("Herdeiro"), c);

        for (int i = 0; i < MAX_JOGADORES; i++) {
            JLabel r = new JLabel("Jogador " + (i + 1));
            JComboBox<String> cor = new JComboBox<String>();
            JComboBox<String> herdeiro = new JComboBox<String>();
            for (String s : opcoesCor)
                cor.addItem(s);
            for (String s : opcoesHerdeiro)
                herdeiro.addItem(s);
            cor.setSelectedIndex(i % opcoesCor.size());           // começa sem repetição
            herdeiro.setSelectedIndex(i % opcoesHerdeiro.size());
            rotulos.add(r);
            cores.add(cor);
            herdeiros.add(herdeiro);

            c.gridy = 3 + i;
            c.gridx = 0;
            conteudo.add(r, c);
            c.gridx = 1;
            conteudo.add(cor, c);
            c.gridx = 2;
            conteudo.add(herdeiro, c);
        }

        lblAviso.setForeground(new Color(180, 0, 0));
        c.gridy = 3 + MAX_JOGADORES; c.gridx = 0; c.gridwidth = 3;
        conteudo.add(lblAviso, c);
        c.gridy++;
        conteudo.add(comecar, c);

        // listener interno da View: só habilita as linhas usadas (sem lambda)
        numero.addActionListener(new TratadorNumero());
        habilitarLinhas();

        setContentPane(conteudo);
        pack();
        setLocationRelativeTo(null);
    }

    private class TratadorNumero implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            habilitarLinhas();
        }
    }

    private void habilitarLinhas() {
        int n = getNumeroDeJogadores();
        for (int i = 0; i < MAX_JOGADORES; i++) {
            boolean usado = i < n;
            rotulos.get(i).setEnabled(usado);
            cores.get(i).setEnabled(usado);
            herdeiros.get(i).setEnabled(usado);
        }
    }

    public int getNumeroDeJogadores() {
        return (Integer) numero.getSelectedItem();
    }

    public List<String> getCores() {
        List<String> lista = new ArrayList<String>();
        for (int i = 0; i < getNumeroDeJogadores(); i++)
            lista.add((String) cores.get(i).getSelectedItem());
        return lista;
    }

    public List<String> getHerdeiros() {
        List<String> lista = new ArrayList<String>();
        for (int i = 0; i < getNumeroDeJogadores(); i++)
            lista.add((String) herdeiros.get(i).getSelectedItem());
        return lista;
    }

    public JButton getBtnComecar() {
        return comecar;
    }

    public void mostrarAviso(String texto) {
        lblAviso.setText(texto);
    }
}
