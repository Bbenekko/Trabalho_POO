package controller;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import model.ModelFacade;
import view.JanelaJogo;
import view.PainelTabuleiro;

// Trata os cliques no mapa: transforma o (x, y) numa parada e pede o movimento ao Model
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
        if (painel.emModoMedicao())            // medindo coordenadas: o clique não é jogada
            return;

        int id = painel.paradaEm(e.getX(), e.getY());
        if (id == -1)                          // clique fora de qualquer parada: nada acontece
            return;

        // peao 0: a Altani, que tem 2, ainda precisa de um jeito de escolher qual move
        if (modelo.mover(0, id))
            view.atualizar();
        else
            view.mostrarAviso("Não é possível ir para a parada " + id);
    }
}
