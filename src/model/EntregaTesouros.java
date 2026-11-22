package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

// Um pergaminho do tabuleiro: ligado a uma parada dupla (ou a Karakorum), troca tesouros por votos
// no Kurultai e dá a peça de bônus que estiver no espaço ao lado dele.
class EntregaTesouros {
	private int idParada;
	private int votos;
	private List<TipoTesouro> exigidos;    // tesouros de tipo definido
	private int quaisquer;                  // quantos tesouros de qualquer tipo (o pergaminho dos baús)

	EntregaTesouros(int idParada, int votos, List<TipoTesouro> exigidos, int quaisquer) {
		this.idParada = idParada;
		this.votos = votos;
		this.exigidos = new ArrayList<TipoTesouro>(exigidos);
		this.quaisquer = quaisquer;
	}

	int getIdParada() {
		return this.idParada;
	}

	int getVotos() {
		return this.votos;
	}

	List<TipoTesouro> getExigidos() {
		return Collections.unmodifiableList(this.exigidos);
	}

	int getQuaisquer() {
		return this.quaisquer;
	}

	// tesouros que o jogador entregaria aqui, ou null se não tem o suficiente.
	// Para "quaisquer", usa primeiro os tipos de que o jogador tem mais.
	List<TipoTesouro> tesourosAEntregar(Jogador j) {
		List<TipoTesouro> lista = new ArrayList<TipoTesouro>(this.exigidos);
		for (TipoTesouro t : TipoTesouro.values()) {
			if (j.quantidadeTesouro(t) < Collections.frequency(lista, t))
				return null;
		}
		for (int n = 0; n < this.quaisquer; n++) {
			TipoTesouro melhor = null;
			int sobra = 0;
			for (TipoTesouro t : TipoTesouro.values()) {
				int resta = j.quantidadeTesouro(t) - Collections.frequency(lista, t);
				if (resta > sobra) {
					sobra = resta;
					melhor = t;
				}
			}
			if (melhor == null)
				return null;
			lista.add(melhor);
		}
		return lista;
	}
}