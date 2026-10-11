package model;

import java.util.*;

class Parada {
	private int id;
	private TipoParada tipo;
	private Regiao regiao;
	private List<Parada> vizinhas = new ArrayList<Parada>();
	private List<Provincia> provincias = new ArrayList<Provincia>();
	private List<Cidade> cidades = new ArrayList<Cidade>();
	private List<Jogador> yurts = new ArrayList<Jogador>();
	private List<Peao> peoes = new ArrayList<Peao>();
	//private Conselheiro conselheiro;
	
	Parada(int id, TipoParada tipo, Regiao regiao) {
		this.id = id;
		this.tipo = tipo;
		this.regiao = regiao;
	}
	
	int getId() {
		return this.id;
	}
	
	TipoParada getTipo() {
		return this.tipo;
	}
	
	Regiao getRegiao() {
		return this.regiao;
	}
	
	List<Cidade> getCidades(){
		return Collections.unmodifiableList(this.cidades);
	}
	
	List<Provincia> getProvincias(){
		return Collections.unmodifiableList(this.provincias);
	}
	
	List<Parada> getVizinhas() {
		return Collections.unmodifiableList(this.vizinhas); 
	}
	
	List<Peao> getPeoes(){
		return Collections.unmodifiableList(this.peoes);
	}
	
	List<Jogador> getYurts(){
		return Collections.unmodifiableList(this.yurts);
	}

	void receberPeao(Peao p) {
		this.peoes.add(p);
	}
	
	void removerPeao(Peao p) {
		this.peoes.remove(p);
	}
	
	int capacidadePeoes() {
		if(this.tipo == TipoParada.simples)
			return 1;
		else if (this.tipo == TipoParada.dupla)
			return 2;
		else 
			return Integer.MAX_VALUE;
	}
	
	boolean podeTerminarAqui(Peao p) {
		return this.peoes.size() < this.capacidadePeoes();
	}
	
	boolean podeReceberYurt() {
		if (this.yurts.isEmpty() && this.tipo == TipoParada.simples)
			return true;
		else if (this.yurts.size() < 2 && this.tipo == TipoParada.dupla)
			return true;
		return false;
	}

	boolean temYurtDe(Jogador j) {
		if(this.yurts.contains(j))
			return true;
		return false;
	}
	
	boolean construirYurt(Jogador j) {
		if (!this.podeReceberYurt())
			return false;
		this.yurts.add(j);
		return true;
	}
	
	void adicionarVizinha(Parada p) {
		this.vizinhas.add(p);
	}

	void adicionarProvincia(Provincia p) {
		this.provincias.add(p);
	}

	void adicionarCidade(Cidade c) {
		this.cidades.add(c);
	}
	
}


