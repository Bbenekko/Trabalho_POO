package model;

import java.util.*;

class Estoque {
	private Map<TipoTributo, Integer> tributos = new HashMap<TipoTributo, Integer>();
	private List<TipoTesouro> tesouros = new ArrayList<TipoTesouro>();
	
	Estoque(){
		this.tributos.put(TipoTributo.espada, 10);
		this.tributos.put(TipoTributo.yurt, 10);
		this.tributos.put(TipoTributo.moeda, 15);
		
		for(TipoTesouro t: TipoTesouro.values()) {
			for(int i = 0; i < 8 ; i++) {
				tesouros.add(t);
			}
		}
		Collections.shuffle(tesouros);
	}
	
	int quantidadeTributo(TipoTributo tributo) {
		return this.tributos.get(tributo);
	}

	boolean retirar(TipoTributo tributo) {
		int i = quantidadeTributo(tributo);
		if(i == 0)
			return false;
		this.tributos.put(tributo,i-1);
		return true;
	}
	
	boolean devolver(TipoTributo tributo) {
		int i = quantidadeTributo(tributo);
		if((tributo == TipoTributo.espada || tributo == TipoTributo.yurt) && i >= 10)
			return false;
		else if (tributo == TipoTributo.espada || tributo == TipoTributo.yurt) {
			this.tributos.put(tributo,i+1);
			return true;
		}
		else if (tributo == TipoTributo.moeda && i >= 15)
			return false;
		else if (tributo == TipoTributo.moeda) {
			this.tributos.put(tributo,i+1);
			return true;
		}
		return false;
	}
	
	TipoTesouro sortearTesouro() {
		if(this.tesouros.isEmpty())
			return null;
		TipoTesouro t = this.tesouros.get(0);
		this.tesouros.remove(0);
		return t;
	}
	
	
}
