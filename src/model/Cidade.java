package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

class Cidade {
	private String nome;
	private Regiao regiao;
	private boolean revelada = false;
	private List<TipoTesouro> tesouros = new ArrayList<TipoTesouro>();
	private Jogador conquistador = null;

	Cidade(String nome, Regiao regiao) {
		this.nome = nome;
		this.regiao = regiao;
	}

	String getNome() { 
		return this.nome; 
	}
	
	Regiao getRegiao() { 
		return this.regiao; 
	}
	
	boolean isRevelada() { 
		return this.revelada; 
	}
	
	Jogador getConquistador() { 
		return this.conquistador; 
	}

	List<TipoTesouro> getTesouros() {
		return Collections.unmodifiableList(this.tesouros);
	}

	void revelar() {
		this.revelada = true;
	}

	void receberTesouro(TipoTesouro t) {
		this.tesouros.add(t);
	}

	boolean podeSerAtacada() {
		return this.revelada && !this.tesouros.isEmpty();
	}

	boolean tomarTesouro(Jogador atacante, TipoTesouro t) {
		if (!this.podeSerAtacada())
			return false;
		if (!this.tesouros.remove(t))
			return false;                 
		if (this.tesouros.isEmpty())
			this.conquistador = atacante;
		return true;
	}

	boolean foiConquistada() {
		return this.conquistador != null;
	}
}