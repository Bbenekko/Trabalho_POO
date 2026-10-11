package model;

import java.util.List;
import java.util.Stack;
import java.util.ArrayList;
import java.util.Collections;

class AreaMelhorias {
	private List<Melhoria> expostas = new ArrayList<Melhoria>();
	private Stack<Melhoria> pilha = new Stack<Melhoria>();
	
	AreaMelhorias() {
		this.criar(TipoMelhoria.pegar_tributo, Regiao.china, 2);
		this.criar(TipoMelhoria.pegar_tributo, Regiao.russia, 2);
		this.criar(TipoMelhoria.pegar_tributo, Regiao.persia, 2);

		this.criar(TipoMelhoria.dois_movimentos, Regiao.china, 3);
		this.criar(TipoMelhoria.dois_movimentos, Regiao.russia, 3);
		this.criar(TipoMelhoria.dois_movimentos, Regiao.persia, 3);

		this.criar(TipoMelhoria.espada_virtual, Regiao.china, 3);
		this.criar(TipoMelhoria.espada_virtual, Regiao.russia, 3);
		this.criar(TipoMelhoria.espada_virtual, Regiao.persia, 2);

		this.criar(TipoMelhoria.yurt_virtual, Regiao.china, 2);
		this.criar(TipoMelhoria.yurt_virtual, Regiao.russia, 3);
		this.criar(TipoMelhoria.yurt_virtual, Regiao.persia, 3);

		this.criar(TipoMelhoria.moeda_virtual, Regiao.china, 3);
		this.criar(TipoMelhoria.moeda_virtual, Regiao.russia, 2);
		this.criar(TipoMelhoria.moeda_virtual, Regiao.persia, 3);

		this.pilha.push(new Melhoria(TipoMelhoria.tesouro_virtual, Regiao.china, TipoTesouro.ferro));
		this.pilha.push(new Melhoria(TipoMelhoria.tesouro_virtual, Regiao.russia, TipoTesouro.gado));
		this.pilha.push(new Melhoria(TipoMelhoria.tesouro_virtual, Regiao.russia, TipoTesouro.especiarias));
		this.pilha.push(new Melhoria(TipoMelhoria.tesouro_virtual, Regiao.persia, TipoTesouro.tecido));
		this.pilha.push(new Melhoria(TipoMelhoria.tesouro_virtual, Regiao.persia, TipoTesouro.pele));

		Collections.shuffle(this.pilha);
		this.repor();             
	}
	
	private void criar(TipoMelhoria tipo, Regiao regiao, int quantidade) {
		for (int n = 0; n < quantidade; n++) {
			this.pilha.push(new Melhoria(tipo, regiao, null));
		}
	}
	
	List<Melhoria> getExpostas() {
		return Collections.unmodifiableList(this.expostas);
	}
	
	int tamanhoDaPilha(){
		return this.pilha.size();
	}
	
	Melhoria comprar(int posicao) {
	    if (posicao < 0 || posicao >= this.expostas.size()) 
	        throw new IllegalArgumentException("Não há melhoria na posição " + posicao);
	    return this.expostas.remove(posicao);  
	}
	
	Melhoria comprarDoTopo(){
		if (this.pilha.isEmpty())
			return null;
		return this.pilha.pop();
	}
	
	boolean moedaAMostra() {
		return this.expostas.size() <= 3;
	}

	boolean espadaAMostra() {
		return this.expostas.size() <= 1;
	}

	void repor() {
		while (this.expostas.size() < 5 && !this.pilha.isEmpty()) {
			this.expostas.add(this.pilha.pop());
		}
	}
}
