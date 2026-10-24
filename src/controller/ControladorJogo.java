package controller;

import model.ModelFacade;
import view.JanelaJogo;

public class ControladorJogo 
{
    private JanelaJogo view;
    private ObservadorTabuleiro observadorTabuleiro;
    private ObservadorAcoes observadorAcoes;

    public ControladorJogo(JanelaJogo view) {
        this.view = view;
        
        // Instancia os observadores especializados
        this.observadorTabuleiro = new ObservadorTabuleiro(view);
        this.observadorAcoes = new ObservadorAcoes(view);

        registrarListeners();
    }

    private void registrarListeners() {
        // Vincula o listener de clique no painel do tabuleiro
        this.view.getPainelTabuleiro().addMouseListener(this.observadorTabuleiro);

        // Configura comandos de ação e vincula aos botões Swing
        this.view.getBtnAtivarColuna().setActionCommand("ATIVAR_COLUNA");
        this.view.getBtnAtivarColuna().addActionListener(this.observadorAcoes);

        this.view.getBtnEncerrarTurno().setActionCommand("ENCERRAR_TURNO");
        this.view.getBtnEncerrarTurno().addActionListener(this.observadorAcoes);
    }

    public static void main(String[] args) {
        // Inicialização padrão da janela Swing sem uso de expressões lambda
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                JanelaJogo janela = new JanelaJogo();
                new ControladorJogo(janela);
                janela.setVisible(true);
            }
        });
    }
}
