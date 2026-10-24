package controller;

import model.ModelFacade;
import view.JanelaJogo;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class ObservadorAcoes implements ActionListener 
{
    private ModelFacade model;
    private JanelaJogo view;

    ObservadorAcoes(JanelaJogo view) {
        this.model = ModelFacade.getInstancia();
        this.view = view;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String comando = e.getActionCommand();

        if ("ATIVAR_COLUNA".equals(comando)) {
            int coluna = view.getColunaSelecionada();
            if (model.iniciarTurno(coluna)) {
                view.repaint();
            }
        } 
        else if ("ENCERRAR_TURNO".equals(comando)) {
            if (model.encerrarTurno()) {
                view.atualizarIndicadores();
                view.repaint();
            }
        }
    }
}