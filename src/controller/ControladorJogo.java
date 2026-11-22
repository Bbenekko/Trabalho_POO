package controller;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import model.ModelFacade;
import model.ObservadorIF;
import view.JanelaCartaSecreta;
import view.JanelaCartas;
import view.JanelaCartela;
import view.JanelaFim;
import view.JanelaInicial;
import view.JanelaJogo;
import view.PainelTabuleiro;
import view.SeletorArquivo;

public class ControladorJogo {
    public static final int MOVER = 0;
    public static final int TRIBUTO = 1;
    public static final int KHAN = 2;
    public static final int ATACAR = 3;
    public static final int YURT = 4;
    public static final int ENTREGAR = 5;

    private static ControladorJogo instancia = null;

    private ModelFacade modelo = ModelFacade.getInstance();
    private JanelaInicial janelaInicial;
    private JanelaCartaSecreta janelaCartaSecreta;
    private JanelaJogo janela;
    private JanelaCartela janelaCartela;
    private JanelaCartas janelaCartas;
    private JanelaFim janelaFim;
    private List<ObservadorIF> observadores = new ArrayList<ObservadorIF>();   // os da partida atual
    private TratadorAcoes tratadorAcoes = new TratadorAcoes();
    private int peaoSelecionado = 0;
    private int jogadorEscolhendoCarta = 0;

    private ControladorJogo() {
    }

    public static ControladorJogo getInstance() {
        if (instancia == null)
            instancia = new ControladorJogo();
        return instancia;
    }

    public void registra(ObservadorIF o) {
        modelo.add(o);
        observadores.add(o);
    }

    // ===================== SEQUÊNCIA DA PARTIDA =====================

    // 1. janela inicial: nova partida (nomes, cores, herdeiros e ordem) ou partida salva
    void iniciar() {
        janelaInicial = new JanelaInicial();
        registrarBotao(janelaInicial.getBtnComecar(), TratadorAcoes.COMECAR);
        registrarBotao(janelaInicial.getBtnCarregar(), TratadorAcoes.CARREGAR);
        janelaInicial.setVisible(true);
    }

    void comecarPartida() {
        List<String> nomes = janelaInicial.getNomes();
        List<String> cores = janelaInicial.getCores();
        List<String> herdeiros = janelaInicial.getHerdeiros();
        if (nomes.contains("")) {
            janelaInicial.mostrarAviso("Todos os jogadores precisam de um nome");
            return;
        }
        if (temRepetido(nomes) || temRepetido(cores) || temRepetido(herdeiros)) {
            janelaInicial.mostrarAviso("Cada jogador precisa de nome, cor e herdeiro diferentes");
            return;
        }
        modelo.novoJogo(nomes, cores, herdeiros, janelaInicial.isSortearOrdem());
        janelaInicial.dispose();
        criarJanelasDaPartida();

        // 2. cada jogador escolhe a carta secreta, um de cada vez
        jogadorEscolhendoCarta = 0;
        janelaCartaSecreta = new JanelaCartaSecreta();
        janelaCartaSecreta.getBtnOpcao(0).setActionCommand(TratadorAcoes.CARTA_0);
        janelaCartaSecreta.getBtnOpcao(1).setActionCommand(TratadorAcoes.CARTA_1);
        janelaCartaSecreta.getBtnOpcao(0).addActionListener(tratadorAcoes);
        janelaCartaSecreta.getBtnOpcao(1).addActionListener(tratadorAcoes);
        janelaCartaSecreta.mostrar(jogadorEscolhendoCarta);
    }

    void escolherCartaSecreta(int opcao) {
        if (!modelo.escolherCartaSecreta(jogadorEscolhendoCarta, opcao))
            return;
        jogadorEscolhendoCarta++;
        if (jogadorEscolhendoCarta < modelo.numeroDeJogadores()) {
            janelaCartaSecreta.mostrar(jogadorEscolhendoCarta);
        } else {
            janelaCartaSecreta.dispose();
            janela.setVisible(true);
        }
    }

    // recuperação: a partida volta exatamente no ponto em que foi salva (antes de um jogador começar)
    void carregarPartida() {
        String caminho = SeletorArquivo.escolherParaAbrir(janelaInicial);
        if (caminho == null)
            return;
        if (!modelo.carregarJogo(caminho)) {
            janelaInicial.mostrarAviso("Não foi possível ler a partida desse arquivo");
            return;
        }
        janelaInicial.dispose();
        criarJanelasDaPartida();
        janela.setVisible(true);
    }

    // a janela principal e as janelas observadoras existem desde já, mas só aparecem quando notificadas
    private void criarJanelasDaPartida() {
        janela = new JanelaJogo();
        janelaCartela = new JanelaCartela();
        janelaCartas = new JanelaCartas();
        janelaFim = new JanelaFim();
        registrarListenersDaJanela();
    }

    // 4. fim: o vencedor já está na JanelaFim; pergunta se querem jogar de novo
    private void perguntarNovaPartida() {
        int resposta = JOptionPane.showConfirmDialog(janelaFim,
                "A partida terminou. Desejam iniciar uma nova partida?",
                "Fim de jogo", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (resposta == JOptionPane.YES_OPTION) {
            fecharJanelasDaPartida();
            iniciar();                                 // volta à escolha dos personagens
        } else {
            System.exit(0);
        }
    }

    private void fecharJanelasDaPartida() {
        for (ObservadorIF o : observadores)
            modelo.remove(o);
        observadores.clear();
        JFrame[] janelas = {janela, janelaCartela, janelaCartas, janelaFim};
        for (JFrame f : janelas)
            if (f != null)
                f.dispose();
    }

    void salvarPartida() {
        if (!modelo.podeSalvar()) {
            janela.mostrarAviso("Só é possível salvar antes de o jogador da vez começar a jogar");
            return;
        }
        String caminho = SeletorArquivo.escolherParaSalvar(janela);
        if (caminho == null)
            return;
        if (modelo.salvarJogo(caminho))
            janela.mostrarAviso("Partida salva em " + new java.io.File(caminho).getName());
        else
            janela.mostrarAviso("Não foi possível salvar nesse arquivo");
    }

    private static boolean temRepetido(List<String> lista) {
        for (int i = 0; i < lista.size(); i++)
            if (lista.indexOf(lista.get(i)) != i)
                return true;
        return false;
    }

    private void registrarListenersDaJanela() {
        janela.getPainelTabuleiro().addMouseListener(new TratadorCliqueTabuleiro());

        registrarBotao(janela.getBtnAtivarColuna(), TratadorAcoes.ATIVAR_COLUNA);
        registrarBotao(janela.getBtnEncerrarTurno(), TratadorAcoes.ENCERRAR_TURNO);
        registrarBotao(janela.getBtnKhanMelhorias(), TratadorAcoes.KHAN_MELHORIAS);
        registrarBotao(janela.getBtnComprar(), TratadorAcoes.COMPRAR);
        registrarBotao(janela.getBtnMelhoriaGratis(), TratadorAcoes.MELHORIA_GRATIS);
        registrarBotao(janela.getBtnVerCartela(), TratadorAcoes.VER_CARTELA);
        registrarBotao(janela.getBtnVerCartas(), TratadorAcoes.VER_CARTAS);
        registrarBotao(janela.getBtnSalvar(), TratadorAcoes.SALVAR);
    }

    private void registrarBotao(javax.swing.AbstractButton b, String comando) {
        b.setActionCommand(comando);
        b.addActionListener(tratadorAcoes);
    }

    // ===================== AÇÕES DOS BOTÕES =====================

    void ativarColuna() {
        int coluna = janela.getComboColunas().getSelectedIndex();
        peaoSelecionado = 0;
        janela.getPainelTabuleiro().selecionarPeao(-1, -1);
        if (!modelo.ativarColuna(coluna))
            janela.mostrarAviso("Coluna " + (coluna + 1) + " indisponível (já usada ou turno em andamento)");
    }

    void encerrarTurno() {
        if (!modelo.turnoEmAndamento()) {
            janela.mostrarAviso("Ative uma coluna antes de encerrar o turno");
            return;
        }
        if (!modelo.encerrarTurno()) {
            janela.mostrarAviso("Use o Khan antes de encerrar: esta coluna exige");
            return;
        }
        if (modelo.jogoTerminou())
            perguntarNovaPartida();
    }

    void usarKhanNasMelhorias() {
        if (!modelo.usarKhanNasMelhorias())
            janela.mostrarAviso("Não é possível levar o Khan às melhorias agora");
    }

    void comprarMelhoria() {
        int posicao = janela.getComboMelhorias().getSelectedIndex();
        int coluna = janela.getComboColunaDestino().getSelectedIndex();
        if (posicao < 0 || !modelo.comprarMelhoria(posicao, coluna)) {
            janela.mostrarAviso("Compra recusada: precisa de 2 moedas e da coluna da mesma região");
            return;
        }
        if (modelo.melhoriaGratisDisponivel())
            janela.mostrarAviso("Linha completa! Escolha uma melhoria e clique em \"Pegar grátis\"");
    }

    void pegarMelhoriaGratis() {
        int posicao = janela.getComboMelhorias().getSelectedIndex();
        if (posicao < 0 || !modelo.pegarMelhoriaGratis(posicao))
            janela.mostrarAviso("Nenhuma melhoria grátis disponível agora");
    }

    void verCartela() {
        modelo.pedirCartela(modelo.jogadorDaVez()); 
    }

    void verCartas() {
        modelo.pedirCartas();                  
    }

    // ===================== CLIQUES NO MAPA =====================

    void cliqueNoTabuleiro(int x, int y) {
        if (!modelo.jogoIniciado() || modelo.jogoTerminou())
            return;
        PainelTabuleiro painel = janela.getPainelTabuleiro();
        if (painel.emModoMedicao())
            return;

        switch (janela.getComboModo().getSelectedIndex()) {
            case MOVER:   cliqueMover(painel, x, y); break;
            case TRIBUTO: cliqueTributo(painel, x, y); break;
            case KHAN:    cliqueKhan(painel, x, y); break;
            case ATACAR:  cliqueAtacar(painel, x, y); break;
            case YURT:    cliqueYurt(painel, x, y); break;
            default:      cliqueEntregar(painel, x, y); break;
        }
    }

    private void cliqueMover(PainelTabuleiro painel, int x, int y) {
        int[] peao = painel.peaoEm(x, y);
        if (peao != null && peao[0] == modelo.jogadorDaVez()) {
            peaoSelecionado = peao[1];
            painel.selecionarPeao(peao[0], peao[1]);
            janela.mostrarAviso("Peão " + (peao[1] + 1) + " selecionado");
            return;
        }
        int id = painel.paradaEm(x, y);
        if (id == -1)
            return;  
        if (!modelo.turnoEmAndamento())
            janela.mostrarAviso("Ative uma coluna antes de mover");
        else if (!modelo.mover(peaoSelecionado, id))
            janela.mostrarAviso("Não é possível ir para a parada " + id);
    }

    private void cliqueTributo(PainelTabuleiro painel, int x, int y) {
        int prov = painel.provinciaEm(x, y);
        if (prov != -1 && !modelo.pegarTributo(prov))
            janela.mostrarAviso("Não é possível pegar tributo dessa província");
    }

    private void cliqueKhan(PainelTabuleiro painel, int x, int y) {
        if (painel.khanMelhoriasEm(x, y)) {
            usarKhanNasMelhorias();
            return;
        }
        int prov = painel.posicaoKhanEm(x, y);
        if (prov == -1)
            prov = painel.provinciaEm(x, y);  
        if (prov == -1)
            janela.mostrarAviso("Clique numa das 4 posições do Khan ou na área de melhorias");
        else if (!modelo.usarKhan(prov))
            janela.mostrarAviso("O Khan não pode ir para essa província agora");
    }

    private void cliqueYurt(PainelTabuleiro painel, int x, int y) {
        int id = painel.paradaEm(x, y);
        if (id != -1 && !modelo.construirYurt(id))
            janela.mostrarAviso("Não é possível construir yurt na parada " + id);
    }

    // pergaminho: entrega os tesouros pedidos em troca de votos e da peça de bônus
    private void cliqueEntregar(PainelTabuleiro painel, int x, int y) {
        int espaco = painel.espacoBonusEm(x, y);
        if (espaco == -1) {
            janela.mostrarAviso("Clique num dos pergaminhos de entrega de tesouros");
            return;
        }
        if (!modelo.turnoEmAndamento()) {
            janela.mostrarAviso("Ative uma coluna antes de entregar tesouros");
            return;
        }
        if (!modelo.entregarTesouros(espaco))
            janela.mostrarAviso("Entrega recusada: passe pela parada " + modelo.paradaDaEntrega(espaco)
                    + " neste turno e tenha " + String.join(", ", modelo.tesourosDaEntrega(espaco))
                    + " (cada pergaminho, 1 vez por turno)");
    }

    private void cliqueAtacar(PainelTabuleiro painel, int x, int y) {
        String cidade = painel.cidadeEm(x, y);
        if (cidade == null)
            return;
        List<String> tesouros = modelo.tesourosNaCidade(cidade);
        if (tesouros.isEmpty()) {
            janela.mostrarAviso(cidade + " não tem tesouros para atacar");
            return;
        }
        painel.mostrarMenuAtaque(cidade, tesouros, modelo.custoAtaque(cidade), x, y, tratadorAcoes);
    }

    void atacar(String cidade, String tesouro) {
        if (!modelo.atacarCidade(cidade, tesouro))
            janela.mostrarAviso("Ataque recusado: confira as espadas e se a cidade está ao alcance");
    }

    // ===================== INÍCIO DO PROGRAMA =====================

    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                ControladorJogo.getInstance().iniciar();
            }
        });
    }
}