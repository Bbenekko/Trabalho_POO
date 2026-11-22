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
		return this.tesourosAEntregar(j, new ArrayList<TipoTesouro>(), new ArrayList<TipoTesouro>());
	}

	// Com tesouros virtuais (melhorias de tesouro da coluna ativada): cada um cobre um tesouro daquele
	// tipo e é usado antes dos reais. Devolve os tesouros REAIS a entregar (ou null) e põe em "usados"
	// os virtuais consumidos.
	List<TipoTesouro> tesourosAEntregar(Jogador j, List<TipoTesouro> virtuais, List<TipoTesouro> usados) {
		List<TipoTesouro> sobra = new ArrayList<TipoTesouro>(virtuais);
		List<TipoTesouro> exigidosReais = new ArrayList<TipoTesouro>();
		List<TipoTesouro> virtuaisUsados = new ArrayList<TipoTesouro>();
		for (TipoTesouro t : this.exigidos) {
			if (sobra.remove(t))
				virtuaisUsados.add(t);
			else
				exigidosReais.add(t);
		}
		int livres = this.quaisquer;
		while (livres > 0 && !sobra.isEmpty()) {         // tesouros de qualquer tipo: virtuais primeiro
			virtuaisUsados.add(sobra.remove(0));
			livres--;
		}
		List<TipoTesouro> lista = this.reaisAEntregar(j, exigidosReais, livres);
		if (lista != null)
			usados.addAll(virtuaisUsados);
		return lista;
	}

	private List<TipoTesouro> reaisAEntregar(Jogador j, List<TipoTesouro> exigidosReais, int quaisquer) {
		List<TipoTesouro> lista = new ArrayList<TipoTesouro>(exigidosReais);
		for (TipoTesouro t : TipoTesouro.values()) {
			if (j.quantidadeTesouro(t) < Collections.frequency(lista, t))
				return null;
		}
		for (int n = 0; n < quaisquer; n++) {
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