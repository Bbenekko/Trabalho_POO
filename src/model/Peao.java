package model;

class Peao {
	private Jogador dono;
	private Parada posicao;
	private boolean podeAgir;
	
	Peao(Jogador dono, boolean podeAgir) {
		this.dono = dono;
		this.podeAgir = podeAgir;
	}
	
	Jogador getDono() {
		return this.dono;
	}
	
	Parada getPosicao() {
		return this.posicao;
	}
	
	boolean podeAgir() {
		return this.podeAgir;
	}
	
	void moverPara(Parada nova) {
		if (this.posicao != null) this.posicao.removerPeao(this);
		this.posicao = nova;
		nova.receberPeao(this);
	}
}
