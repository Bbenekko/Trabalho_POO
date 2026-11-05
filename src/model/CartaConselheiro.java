package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// Base das cartas: cada carta mede um numero ("progresso") e compara com a meta.
// Os metodos de contagem ficam aqui porque as duas subclasses usam varios deles.
abstract class CartaConselheiro {

	abstract int progresso(Jogador j, Tabuleiro t);

	// yurts no mapa = os 12 do jogador menos os que ainda tem (inclui cidades e Karakorum, p. 20)
	int yurtsNoMapa(Jogador j) {
		return 12 - j.getYurtsRestantes();
	}

	int menorQuantidadeDeYurtsPorRegiao(Jogador j, Tabuleiro t) {
		int menor = Integer.MAX_VALUE;
		for (Regiao r : Regiao.values())
			menor = Math.min(menor, t.contarYurts(j, r));
		return menor;
	}

	int tiposDeTesouro(Jogador j) {
		int tipos = 0;
		for (TipoTesouro tt : TipoTesouro.values()) {
			if (j.quantidadeTesouro(tt) > 0)
				tipos++;
		}
		return tipos;
	}

	int cidadesConquistadas(Jogador j, Tabuleiro t) {
		int n = 0;
		for (Cidade c : t.getCidades()) {
			if (c.getConquistador() == j)
				n++;
		}
		return n;
	}

	List<Melhoria> todasAsMelhorias(Jogador j) {
		List<Melhoria> todas = new ArrayList<Melhoria>();
		for (ColunaAcao c : j.getTabuleiro().getColunas())
			todas.addAll(c.getMelhorias());
		return todas;
	}

	// "simbolo" da melhoria: o tipo; no tesouro virtual, cada tesouro e um simbolo diferente (p. 19)
	Map<String, Integer> simbolos(Jogador j) {
		Map<String, Integer> contagem = new HashMap<String, Integer>();
		for (Melhoria m : this.todasAsMelhorias(j)) {
			String chave = m.getTipo().name();
			if (m.getTipo() == TipoMelhoria.tesouro_virtual)
				chave = chave + "_" + m.getTesouro().name();
			contagem.put(chave, contagem.getOrDefault(chave, 0) + 1);
		}
		return contagem;
	}

	// p. 20: o yurt do Tolui em Karakorum so ajuda no movimento e nas cartas de 6 e 10 yurts
	int yurtsDoJogador(Parada p, Jogador j) {
		if (p.getTipo() == TipoParada.karakorum)
			return 0;
		return Collections.frequency(p.getYurts(), j);
	}
}
