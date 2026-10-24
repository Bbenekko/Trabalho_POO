package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import model.ModelFacade;
import view.JanelaJogo;

// Trata os botões; o ActionCommand diz qual botão foi apertado
class ObservadorAcoes implements ActionListener {
    static final String ATIVAR_COLUNA = "ATIVAR_COLUNA";
    static final String ENCERRAR_TURNO = "ENCERRAR_TURNO";

    private JanelaJogo view;
    private ModelFacade modelo;

    ObservadorAcoes(JanelaJogo view, ModelFacade modelo) {
        this.view = view;
        this.modelo = modelo;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String comando = e.getActionCommand();

        if (ATIVAR_COLUNA.equals(comando)) {
            int coluna = view.getComboColunas().getSelectedIndex();
            if (modelo.ativarColuna(coluna))
                view.atualizar();
            else
                view.mostrarAviso("Coluna " + (coluna + 1) + " indisponível");
        } else if (ENCERRAR_TURNO.equals(comando)) {
            if (modelo.encerrarTurno())
                view.atualizar();
            else
                view.mostrarAviso("Ainda não é possível encerrar o turno");
        }
    }
}
