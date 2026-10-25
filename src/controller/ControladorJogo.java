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

        this.observadorTabuleiro = new ObservadorTabuleiro(view, modelo);
        this.observadorAcoes = new ObservadorAcoes(view, modelo);

        registrarListeners();
    }

    private void registrarListeners() {
        this.view.getPainelTabuleiro().addMouseListener(this.observadorTabuleiro);

        this.view.getBtnAtivarColuna().setActionCommand(ObservadorAcoes.ATIVAR_COLUNA);
        this.view.getBtnAtivarColuna().addActionListener(this.observadorAcoes);

        this.view.getBtnEncerrarTurno().setActionCommand(ObservadorAcoes.ENCERRAR_TURNO);
        this.view.getBtnEncerrarTurno().addActionListener(this.observadorAcoes);
    }

    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                ModelFacade modelo = ModelFacade.getInstance();
                modelo.novoJogo(2);             
                JanelaJogo janela = new JanelaJogo(modelo);
                new ControladorJogo(janela, modelo);
                janela.setVisible(true);
            }
        });
    }
}
