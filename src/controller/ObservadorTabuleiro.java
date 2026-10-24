package controller;

import model.ModelFacade;
import view.JanelaJogo;

import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

class ObservadorTabuleiro extends MouseAdapter 
{
    private ModelFacade model;
    private JanelaJogo view;

    ObservadorTabuleiro(JanelaJogo view) {
        this.model = ModelFacade.getInstancia();
        this.view = view;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        Point pontoClique = e.getPoint();
        
        // Identifica o ID da parada clicada através do método do painel
        Integer idParadaDestino = view.getPainelTabuleiro().identificarParadaClicada(pontoClique);

        // Se clicou fora de qualquer parada válida, simplesmente ignora
        if (idParadaDestino == null) {
            return;
        }

        // Descobre se é o peão 0 ou 1 (caso da Altani)
        int indicePeao = view.getPeaoSelecionado();

        // Tenta realizar o movimento na fachada
        boolean sucesso = model.moverPeao(indicePeao, idParadaDestino);

        // Se o movimento for válido, redesenha a tela
        if (sucesso) {
            view.repaint();
        }
    }
}