package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import model.ModelFacade;

// Primeira janela: número de jogadores e, para cada um, nome, cor do peão e herdeiro.
// A ordem de jogo é a ordem das linhas, a não ser que "Sortear a ordem" esteja marcado.
// Também permite recuperar uma partida salva. Os listeners dos botões são do Controller.
public class JanelaInicial extends JFrame {
    private static final int MAX_JOGADORES = 5;
    private static final int COLUNAS_NOME = 12;

    private JComboBox<Integer> numero = new JComboBox<Integer>();
    private List<JTextField> nomes = new ArrayList<JTextField>();
    private List<JComboBox<String>> cores = new ArrayList<JComboBox<String>>();
    private List<JComboBox<String>> herdeiros = new ArrayList<JComboBox<String>>();
    private List<JLabel> rotulos = new ArrayList<JLabel>();
    private JCheckBox sortearOrdem = new JCheckBox("Sortear a ordem dos jogadores");
    private JButton comecar = new JButton("Começar a partida");
    private JButton carregar = new JButton("Carregar partida salva...");
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
        c.gridx = 0; c.gridy = 0; c.gridwidth = 4;
        conteudo.add(titulo, c);

        c.gridwidth = 1; c.gridy = 1;
        conteudo.add(new JLabel("Número de jogadores"), c);
        for (int n = 2; n <= MAX_JOGADORES; n++)
            numero.addItem(n);
        c.gridx = 1;
        conteudo.add(numero, c);

        c.gridy = 2;
        c.gridx = 1;
        conteudo.add(new JLabel("Nome"), c);
        c.gridx = 2;
        conteudo.add(new JLabel("Cor do peão"), c);
        c.gridx = 3;
        conteudo.add(new JLabel("Herdeiro"), c);

        for (int i = 0; i < MAX_JOGADORES; i++) {
            JLabel r = new JLabel((i + 1) + "º a jogar");
            JTextField nome = new JTextField("Jogador " + (i + 1), COLUNAS_NOME);
            JComboBox<String> cor = new JComboBox<String>();
            JComboBox<String> herdeiro = new JComboBox<String>();
            for (String s : opcoesCor)
                cor.addItem(s);
            for (String s : opcoesHerdeiro)
                herdeiro.addItem(s);
            cor.setSelectedIndex(i % opcoesCor.size());
            herdeiro.setSelectedIndex(i % opcoesHerdeiro.size());
            rotulos.add(r);
            nomes.add(nome);
            cores.add(cor);
            herdeiros.add(herdeiro);

            c.gridy = 3 + i;
            c.gridx = 0;
            conteudo.add(r, c);
            c.gridx = 1;
            conteudo.add(nome, c);
            c.gridx = 2;
            conteudo.add(cor, c);
            c.gridx = 3;
            conteudo.add(herdeiro, c);
        }

        c.gridy = 3 + MAX_JOGADORES; c.gridx = 0; c.gridwidth = 4;
        conteudo.add(sortearOrdem, c);

        lblAviso.setForeground(new Color(180, 0, 0));
        c.gridy++;
        conteudo.add(lblAviso, c);

        JPanel botoes = new JPanel(new GridLayout(1, 2, 12, 0));
        botoes.add(comecar);
        botoes.add(carregar);
        c.gridy++;
        conteudo.add(botoes, c);

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
            nomes.get(i).setEnabled(usado);
            cores.get(i).setEnabled(usado);
            herdeiros.get(i).setEnabled(usado);
        }
    }

    public int getNumeroDeJogadores() {
        return (Integer) numero.getSelectedItem();
    }

    public List<String> getNomes() {
        List<String> lista = new ArrayList<String>();
        for (int i = 0; i < getNumeroDeJogadores(); i++)
            lista.add(nomes.get(i).getText().trim());
        return lista;
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

    public boolean isSortearOrdem() {
        return sortearOrdem.isSelected();
    }

    public JButton getBtnComecar() {
        return comecar;
    }

    public JButton getBtnCarregar() {
        return carregar;
    }

    public void mostrarAviso(String texto) {
        lblAviso.setText(texto);
    }
}