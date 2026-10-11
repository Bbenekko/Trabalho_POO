package model;

import java.util.ArrayList;
import java.util.List;

class Provincia {
	private int id;
	private TipoTributo tipoTributo;
	private int quantidade = 0;
	private boolean provinciaDoKhan;
	private List<Provincia> alvosDoKhan = new ArrayList<Provincia>();
	
	Provincia(int id, TipoTributo tipoTributo, boolean provinciaDoKhan) {
		this.id = id;
		this.tipoTributo = tipoTributo;
		this.provinciaDoKhan = provinciaDoKhan;
	}
	
	int getId(){
		return this.id;
	}
	
	TipoTributo getTipoTributo() {
		return this.tipoTributo;
	}
	
	int getQuantidade() {
		return this.quantidade;
	}
	
	boolean retirarTributo() {
		if (this.estaVazia()) {
	   		return false;
	   	}
	   	this.quantidade--;
	   	return true;
	}
	
	boolean estaVazia() {
		return this.quantidade == 0;
	}
	
	boolean isProvinciaDoKhan() {
		return this.provinciaDoKhan;
	} 
	
	void adicionarAlvoDoKhan(Provincia alvo) {
		this.alvosDoKhan.add(alvo);
	}

	void receberKhan(Estoque estoque) {
		this.receberDoEstoque(estoque);
		for (Provincia alvo : this.alvosDoKhan) {
			alvo.receberDoEstoque(estoque);
		}
	}
	
	boolean receberDoEstoque(Estoque estoque) {
		if (this.quantidade >= 3)
			return false;                          
		if (!estoque.retirar(this.tipoTributo))
			return false;                       
		this.quantidade++;
		return true;
	}
	
}
