package view;

import java.util.HashMap;
import java.util.Map;

// Título e linhas de texto de cada carta de conselheiro, pelo nome do enum (acesso em tempo constante).
class TextosCartas {
    private static Map<String, String[]> textos = new HashMap<String, String[]>();

    static {
        // abertas (p. 18): o primeiro a cumprir ganha 1 voto
        textos.put("quatro_espadas", new String[] {"4 espadas", "Ter 4 espadas", "1 voto"});
        textos.put("seis_yurts", new String[] {"6 yurts", "6 yurts no mapa", "1 voto"});
        textos.put("dez_yurts", new String[] {"10 yurts", "10 yurts no mapa", "1 voto"});
        textos.put("quatro_tesouros", new String[] {"4 tesouros", "Ter 4 tesouros", "1 voto"});
        textos.put("cinco_tesouros_diferentes", new String[] {"5 tesouros", "5 tipos diferentes", "1 voto"});
        textos.put("um_yurt_cada_regiao", new String[] {"Regiões", "1 yurt em cada região", "1 voto"});
        textos.put("dois_yurts_cada_regiao", new String[] {"Regiões", "2 yurts em cada região", "1 voto"});
        textos.put("duas_pecas_bonus", new String[] {"Bônus", "2 peças de bônus", "1 voto"});
        textos.put("tres_pecas_bonus", new String[] {"Bônus", "3 peças de bônus", "1 voto"});
        textos.put("duas_cidades", new String[] {"2 cidades", "Conquistar 2 cidades", "1 voto"});
        textos.put("tres_cidades", new String[] {"3 cidades", "Conquistar 3 cidades", "1 voto"});
        textos.put("linha_de_melhorias", new String[] {"Linha", "Completar 1 linha", "de melhorias", "1 voto"});
        textos.put("duas_colunas_com_duas", new String[] {"Colunas", "2 colunas com", "2 melhorias", "1 voto"});
        textos.put("quatro_melhorias_numa_coluna", new String[] {"Coluna", "4 melhorias numa", "coluna", "1 voto"});

        // secretas (p. 19): de 0 a 3 votos no fim do jogo
        textos.put("viajante", new String[] {"Viajante", "Maior rede de yurts", "3 / 6 / 8"});
        textos.put("soldado", new String[] {"Soldado", "Melhorias", "4 / 7 / 10"});
        textos.put("colecionador", new String[] {"Colecionador", "Símbolos diferentes", "4 / 6 / 7"});
        textos.put("sacerdote", new String[] {"Sacerdote", "Pares de símbolos", "1 / 2 / 4"});
        textos.put("governador", new String[] {"Governador", "Regiões governadas", "1 / 2 / 3"});
        textos.put("bajulador", new String[] {"Bajulador", "Províncias do Khan", "com 2 yurts", "2 / 3 / 4"});
        textos.put("prefeito", new String[] {"Prefeito", "Cidades com 2 yurts", "em volta", "2 / 3 / 4"});
        textos.put("cidadao", new String[] {"Cidadão", "Cidades com 1 yurt", "em volta", "3 / 6 / 8"});
        textos.put("cavaleiro", new String[] {"Cavaleiro", "Melhorias de cavalo", "ou punho", "2 / 4 / 5"});
        textos.put("mensageiro", new String[] {"Mensageiro", "Paradas duplas", "com yurt", "2 / 4 / 6"});
    }

    private TextosCartas() {
    }

    static String[] texto(String tipo) {
        String[] t = textos.get(tipo);
        return (t == null) ? new String[] {tipo} : t;
    }

    static String titulo(String tipo) {
        return texto(tipo)[0];
    }
}
