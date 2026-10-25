package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import model.ModelFacade;
import view.JanelaJogo;

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
            if (modelo.ativarColuna(coluna)) {
                view.atualizar();
            } else {
                view.mostrarAviso("Coluna " + (coluna + 1) + " indisponível");
            }
        } else if (ENCERRAR_TURNO.equals(comando)) {
            if (!modelo.turnoEmAndamento()) {
                view.mostrarAviso("Ative a coluna antes de encerrar");
                return;
            }

            if (!modelo.podeEncerrarTurno()) {
                boolean ok = modelo.usarKhanNasMelhorias();

                if (!ok) {
                    int[] provinciasDoKhan = {0, 4, 7, 10};
                    for (int idProv : provinciasDoKhan) {
                        if (modelo.usarKhan(idProv)) {
                            ok = true;
                            break;
                        }
                    }
                }
            }

            // Tenta encerrar o turno
            if (modelo.encerrarTurno()) {
                view.atualizar();
            } else {
                view.mostrarAviso("Não foi possível encerrar o turno");
            }
        }
    }
}