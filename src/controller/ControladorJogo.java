package controller;

import java.util.List;

import model.ModelFacade;
import model.ObservadorIF;
import view.JanelaCartaSecreta;
import view.JanelaCartas;
import view.JanelaCartela;
import view.JanelaFim;
import view.JanelaInicial;
import view.JanelaJogo;
import view.PainelTabuleiro;

// Singleton (Cap. 17): organiza a sequência de uma partida e traduz cada evento da interface
// em uma chamada à fachada. Quem atualiza a tela é o Observer: a fachada notifica os painéis.
public class ControladorJogo {
    // modos de ação: o que um clique no mapa significa (mesma ordem do combo da View)
    public static final int MOVER = 0;
    public static final int TRIBUTO = 1;
    public static final int KHAN = 2;
    public static final int ATACAR = 3;
    public static final int YURT = 4;

    private static ControladorJogo instancia = null;

    private ModelFacade modelo = ModelFacade.getInstance();
    private JanelaInicial janelaInicial;
    private JanelaCartaSecreta janelaCartaSecreta;
    private JanelaJogo janela;
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

    // Cap. 17: o observador se registra pelo controlador, que repassa ao observado
    public void registra(ObservadorIF o) {
        modelo.add(o);
    }

    // ===================== SEQUÊNCIA DA PARTIDA =====================

    // 1. janela inicial: número de jogadores, cores e herdeiros
    void iniciar() {
        janelaInicial = new JanelaInicial();
        janelaInicial.getBtnComecar().setActionCommand(TratadorAcoes.COMECAR);
        janelaInicial.getBtnComecar().addActionListener(tratadorAcoes);
        janelaInicial.setVisible(true);
    }

    void comecarPartida() {
        List<String> cores = janelaInicial.getCores();
        List<String> herdeiros = janelaInicial.getHerdeiros();
        if (temRepetido(cores) || temRepetido(herdeiros)) {
            janelaInicial.mostrarAviso("Cada jogador precisa de uma cor e de um herdeiro diferentes");
            return;
        }
        modelo.novoJogo(cores, herdeiros);
        janelaInicial.dispose();

        // a janela principal e as janelas observadoras existem desde já, mas só aparecem quando notificadas
        janela = new JanelaJogo();
        new JanelaCartela();
        new JanelaCartas();
        new JanelaFim();
        registrarListenersDaJanela();

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
            // 3. começam os turnos
            janelaCartaSecreta.dispose();
            janela.setVisible(true);
        }
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
        if (!modelo.encerrarTurno())
            janela.mostrarAviso("Use o Khan antes de encerrar: esta coluna exige");
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
        modelo.pedirCartela(modelo.jogadorDaVez());   // a JanelaCartela abre ao ser notificada
    }

    void verCartas() {
        modelo.pedirCartas();                         // a JanelaCartas abre ao ser notificada
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
            default:      cliqueYurt(painel, x, y); break;
        }
    }

    private void cliqueMover(PainelTabuleiro painel, int x, int y) {
        // clicar num peão do jogador da vez o seleciona (a Altani tem 2)
        int[] peao = painel.peaoEm(x, y);
        if (peao != null && peao[0] == modelo.jogadorDaVez()) {
            peaoSelecionado = peao[1];
            painel.selecionarPeao(peao[0], peao[1]);
            janela.mostrarAviso("Peão " + (peao[1] + 1) + " selecionado");
            return;
        }
        int id = painel.paradaEm(x, y);
        if (id == -1)
            return;                                   // fora de qualquer parada: nada acontece
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
            prov = painel.provinciaEm(x, y);          // também aceita clicar nos tributos da província
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

    // a cidade abre um menu com os tesouros; a escolha volta por TratadorAcoes
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
        // sem lambda: classe anônima
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                ControladorJogo.getInstance().iniciar();
            }
        });
    }
}