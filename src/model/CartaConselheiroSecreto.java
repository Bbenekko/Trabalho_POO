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
			default:           return new int[] {2, 4, 6}; 
		}
	}

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
			default:           return this.paradasDuplasComYurt(j, t);
		}
	}

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

	private int paresDeSimbolos(Jogador j) {
		int pares = 0;
		Map<String, Integer> contagem = this.simbolos(j);
		for (int qtd : contagem.values())
			pares += qtd / 2;
		return pares;
	}

	private int regioesGovernadas(Jogador j, Tabuleiro t) {
		int n = 0;
		for (Regiao r : Regiao.values()) {
			if (t.contarYurts(j, r) >= 2 && j.getTabuleiro().contarMelhorias(r) >= 2)
				n++;
		}
		return n;
	}

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

	private int melhoriasDeCavaloOuPunho(Jogador j) {
		int n = 0;
		for (Melhoria m : this.todasAsMelhorias(j)) {
			if (m.getTipo() == TipoMelhoria.dois_movimentos || m.getTipo() == TipoMelhoria.pegar_tributo)
				n++;
		}
		return n;
	}

	private int paradasDuplasComYurt(Jogador j, Tabuleiro t) {
		int n = 0;
		for (Parada p : t.getParadas()) {
			if (p.getTipo() == TipoParada.dupla && p.temYurtDe(j))
				n++;
		}
		return n;
	}
}
