package controller;

import model.ModelFacade;
import view.JanelaJogo;

public class ControladorJogo {
    private JanelaJogo view;
    private ModelFacade modelo;
    private ObservadorTabuleiro observadorTabuleiro;
    private ObservadorAcoes observadorAcoes;

    public ControladorJogo(JanelaJogo view, ModelFacade modelo) {
        this.view = view;
        this.modelo = modelo;

        // Instancia os observadores especializados
        this.observadorTabuleiro = new ObservadorTabuleiro(view, modelo);
        this.observadorAcoes = new ObservadorAcoes(view, modelo);

        registrarListeners();
    }

    private void registrarListeners() {
        // Vincula o listener de clique no painel do tabuleiro
        this.view.getPainelTabuleiro().addMouseListener(this.observadorTabuleiro);

        // Configura comandos de ação e vincula aos botões Swing
        this.view.getBtnAtivarColuna().setActionCommand(ObservadorAcoes.ATIVAR_COLUNA);
        this.view.getBtnAtivarColuna().addActionListener(this.observadorAcoes);

        this.view.getBtnEncerrarTurno().setActionCommand(ObservadorAcoes.ENCERRAR_TURNO);
        this.view.getBtnEncerrarTurno().addActionListener(this.observadorAcoes);
    }

    public static void main(String[] args) {
        // Inicialização padrão da janela Swing sem uso de expressões lambda
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                ModelFacade modelo = ModelFacade.getInstance();
                modelo.novoJogo(2);              // antes da janela: os paineis leem o jogo ao serem criados
                JanelaJogo janela = new JanelaJogo(modelo);
                new ControladorJogo(janela, modelo);
                janela.setVisible(true);
            }
        });
    }
}
