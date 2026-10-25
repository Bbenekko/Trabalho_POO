package controller;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import model.ModelFacade;
import view.JanelaJogo;
import view.PainelTabuleiro;

class ObservadorTabuleiro extends MouseAdapter {
    private JanelaJogo view;
    private ModelFacade modelo;

    ObservadorTabuleiro(JanelaJogo view, ModelFacade modelo) {
        this.view = view;
        this.modelo = modelo;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        PainelTabuleiro painel = view.getPainelTabuleiro();
        if (painel.emModoMedicao())           
            return;

        int id = painel.paradaEm(e.getX(), e.getY());
        if (id == -1)                         
            return;

        if (modelo.mover(0, id))
            view.atualizar();
        else
            view.mostrarAviso("Não é possível ir para a parada " + id);
    }
}
