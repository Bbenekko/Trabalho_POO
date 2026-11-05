package model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

class CartaConselheiroSecreto extends CartaConselheiro {
	private TipoConselheiroSecreto tipo;

	CartaConselheiroSecreto(TipoConselheiroSecreto tipo) {
		this.tipo = tipo;
	}

	TipoConselheiroSecreto getTipo() {
		return this.tipo;
	}

	// p. 19: os 3 valores que dao 1, 2 e 3 votos
	int[] limiares() {
		switch (this.tipo) {
			case viajante:     return new int[] {3, 6, 8};
			case soldado:      return new int[] {4, 7, 10};
			case colecionador: return new int[] {4, 6, 7};
			case sacerdote:    return new int[] {1, 2, 4};
			case governador:   return new int[] {1, 2, 3};
			case bajulador:    return new int[] {2, 3, 4};
			case prefeito:     return new int[] {2, 3, 4};
			case cidadao:      return new int[] {3, 6, 8};
			case cavaleiro:    return new int[] {2, 4, 5};
			default:           return new int[] {2, 4, 6};   // mensageiro
		}
	}

	// p. 16: no fim do jogo, de 0 a 3 votos
	int votos(Jogador j, Tabuleiro t) {
		int valor = this.progresso(j, t);
		int votos = 0;
		for (int limiar : this.limiares()) {
			if (valor >= limiar)
				votos++;
		}
		return votos;
	}

	@Override
	int progresso(Jogador j, Tabuleiro t) {
		switch (this.tipo) {
			case viajante:     return this.maiorRedeDeYurts(j, t);
			case soldado:      return this.todasAsMelhorias(j).size();
			case colecionador: return this.simbolos(j).size();
			case sacerdote:    return this.paresDeSimbolos(j);
			case governador:   return this.regioesGovernadas(j, t);
			case bajulador:    return this.provinciasDoKhanCom2Yurts(j, t);
			case prefeito:     return this.cidadesComYurtsAdjacentes(j, t, 2);
			case cidadao:      return this.cidadesComYurtsAdjacentes(j, t, 1);
			case cavaleiro:    return this.melhoriasDeCavaloOuPunho(j);
			default:           return this.paradasDuplasComYurt(j, t);   // mensageiro
		}
	}

	// Viajante: maior grupo de yurts ligados por estradas (a rede pode ter ramificacoes)
	private int maiorRedeDeYurts(Jogador j, Tabuleiro t) {
		List<Parada> jaVistas = new ArrayList<Parada>();
		int maior = 0;
		for (Parada inicio : t.getParadas()) {
			if (jaVistas.contains(inicio) || this.yurtsDoJogador(inicio, j) == 0)
				continue;
			int yurtsNaRede = 0;
			List<Parada> aVisitar = new ArrayList<Parada>();
			aVisitar.add(inicio);
			jaVistas.add(inicio);
			while (!aVisitar.isEmpty()) {
				Parada p = aVisitar.remove(0);
				yurtsNaRede += this.yurtsDoJogador(p, j);
				for (Parada v : p.getVizinhas()) {
					if (!jaVistas.contains(v) && this.yurtsDoJogador(v, j) > 0) {
						jaVistas.add(v);
						aVisitar.add(v);
					}
				}
			}
			maior = Math.max(maior, yurtsNaRede);
		}
		return maior;
	}

	// Sacerdote: cada 2 melhorias com o mesmo simbolo formam 1 par
	private int paresDeSimbolos(Jogador j) {
		int pares = 0;
		Map<String, Integer> contagem = this.simbolos(j);
		for (int qtd : contagem.values())
			pares += qtd / 2;
		return pares;
	}

	// Governador: regioes com 2+ yurts e 2+ melhorias na coluna da regiao
	private int regioesGovernadas(Jogador j, Tabuleiro t) {
		int n = 0;
		for (Regiao r : Regiao.values()) {
			if (t.contarYurts(j, r) >= 2 && j.getTabuleiro().contarMelhorias(r) >= 2)
				n++;
		}
		return n;
	}

	// Bajulador: provincias do Khan com 2+ yurts do jogador nas paradas em volta
	private int provinciasDoKhanCom2Yurts(Jogador j, Tabuleiro t) {
		int n = 0;
		for (Provincia prov : t.getProvincias()) {
			if (!prov.isProvinciaDoKhan())
				continue;
			int yurts = 0;
			for (Parada p : t.getParadas()) {
				if (p.getProvincias().contains(prov))
					yurts += this.yurtsDoJogador(p, j);
			}
			if (yurts >= 2)
				n++;
		}
		return n;
	}

	// Prefeito (minimo 2) e Cidadao (minimo 1): yurts DENTRO da cidade nao contam
	private int cidadesComYurtsAdjacentes(Jogador j, Tabuleiro t, int minimo) {
		int n = 0;
		for (Cidade c : t.getCidades()) {
			int yurts = 0;
			for (Parada p : t.getParadas()) {
				if (p.getCidades().contains(c))
					yurts += this.yurtsDoJogador(p, j);
			}
			if (yurts >= minimo)
				n++;
		}
		return n;
	}

	// Cavaleiro: melhorias com cavalo (dois movimentos) ou punho (pegar tributo)
	private int melhoriasDeCavaloOuPunho(Jogador j) {
		int n = 0;
		for (Melhoria m : this.todasAsMelhorias(j)) {
			if (m.getTipo() == TipoMelhoria.dois_movimentos || m.getTipo() == TipoMelhoria.pegar_tributo)
				n++;
		}
		return n;
	}

	// Mensageiro: paradas duplas com pelo menos 1 yurt do jogador (Karakorum nao conta)
	private int paradasDuplasComYurt(Jogador j, Tabuleiro t) {
		int n = 0;
		for (Parada p : t.getParadas()) {
			if (p.getTipo() == TipoParada.dupla && p.temYurtDe(j))
				n++;
		}
		return n;
	}
}
