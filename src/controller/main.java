package controller;

import view.JanelaJogo;

public class main {
    public static void main(String[] args) {
        ControladorJogo controlador = new ControladorJogo();
        new JanelaJogo(controlador);
    }
}