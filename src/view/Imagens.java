package view;

import java.awt.Image;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

class Imagens {
    static Image carregar(String caminho) {
        try {
            return ImageIO.read(new File(caminho));
        } catch (IOException e) {
            System.out.println("Imagem não encontrada: " + caminho);
            return null;
        }
    }
}
