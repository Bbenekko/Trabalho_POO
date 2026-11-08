package controller;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

class TratadorCliqueTabuleiro extends MouseAdapter {
    @Override
    public void mouseClicked(MouseEvent e) {
        ControladorJogo.getInstance().cliqueNoTabuleiro(e.getX(), e.getY());
    }
}
