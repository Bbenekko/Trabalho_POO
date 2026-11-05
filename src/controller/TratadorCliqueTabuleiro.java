package controller;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

// Listener do Swing (não é o Observer do Cap. 17): repassa o clique no mapa ao ControladorJogo.
class TratadorCliqueTabuleiro extends MouseAdapter {
    @Override
    public void mouseClicked(MouseEvent e) {
        ControladorJogo.getInstance().cliqueNoTabuleiro(e.getX(), e.getY());
    }
}
