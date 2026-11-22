package view;

import java.awt.Component;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

// Escolha do arquivo de uma partida salva com o JFileChooser (exigência da 4ª iteração).
// Nome e pasta são livres; só o tipo (.txt) é pré-definido.
public class SeletorArquivo {
    private static final String EXTENSAO = "txt";
    private static File ultimaPasta = null;      // o próximo diálogo abre onde o anterior parou

    private SeletorArquivo() {
    }

    // caminho escolhido para gravar (com .txt no fim), ou null se o usuário cancelar
    public static String escolherParaSalvar(Component pai) {
        JFileChooser seletor = criar("Salvar partida");
        if (seletor.showSaveDialog(pai) != JFileChooser.APPROVE_OPTION)
            return null;
        File escolhido = seletor.getSelectedFile();
        ultimaPasta = escolhido.getParentFile();
        String caminho = escolhido.getPath();
        if (!caminho.toLowerCase().endsWith("." + EXTENSAO))
            caminho += "." + EXTENSAO;
        return caminho;
    }

    // caminho do arquivo a ler, ou null se o usuário cancelar
    public static String escolherParaAbrir(Component pai) {
        JFileChooser seletor = criar("Carregar partida salva");
        if (seletor.showOpenDialog(pai) != JFileChooser.APPROVE_OPTION)
            return null;
        File escolhido = seletor.getSelectedFile();
        ultimaPasta = escolhido.getParentFile();
        return escolhido.getPath();
    }

    private static JFileChooser criar(String titulo) {
        JFileChooser seletor = new JFileChooser(ultimaPasta);
        seletor.setDialogTitle(titulo);
        seletor.setFileFilter(new FileNameExtensionFilter("Partida de Herdeiros de Khan (*.txt)", EXTENSAO));
        return seletor;
    }
}