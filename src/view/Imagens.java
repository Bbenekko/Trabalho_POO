package view;

import java.awt.Image;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

class Imagens {
    private Imagens() {
    }

    static Image carregar(String caminho) {
        try {
            return ImageIO.read(new File(caminho));
        } catch (IOException e) {
            System.out.println("Imagem não encontrada: " + caminho);
            return null;
        }
    }

    // para imagens opcionais: sem arquivo, devolve null sem avisar
    static Image carregarSeExistir(String caminho) {
        File f = new File(caminho);
        if (!f.isFile())
            return null;
        try {
            return ImageIO.read(f);
        } catch (IOException e) {
            return null;
        }
    }
}
