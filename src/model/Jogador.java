package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class Jogador 
{
	private Cor cor;
	private Herdeiro herdeiro;
	private List<Peao> peoes = new ArrayList<Peao>();
	private TabuleiroJogador tabuleiro;         
	private Map<TipoTributo, Integer> tributos = new HashMap<TipoTributo, Integer>();
	private Map<TipoTesouro, Integer> tesouros = new HashMap<TipoTesouro, Integer>();
	private int yurtsRestantes = 12;
	//private List<PecaBonus> bonus = new ArrayList<PecaBonus>();   
	//private CartaConselheiroSecreto cartaSecreta;                
    
	Jogador(Cor cor, Herdeiro herdeiro) {
		this.cor = cor;
		this.herdeiro = herdeiro;
		this.tabuleiro = new TabuleiroJogador(herdeiro);

		this.peoes.add(new Peao(this, true));
		if (herdeiro == Herdeiro.altani) 
			this.peoes.add(new Peao(this, false));

		for (TipoTributo t : TipoTributo.values())
			this.tributos.put(t, 0);
		for (TipoTesouro t : TipoTesouro.values())
			this.tesouros.put(t, 0);
	}

	Cor getCor(){
    	return this.cor;
    }

    Herdeiro getHerdeiro(){
    	return this.herdeiro;
    }
    
    int getYurtsRestantes(){
    	return this.yurtsRestantes;
    }
    
    List<Peao> getPeoes()
    {
    	return Collections.unmodifiableList(this.peoes);
    }
    
    void receberTributo(TipoTributo t, int n)
    {
    	int atual = tributos.get(t);
		this.tributos.put(t, atual + n);
    }
    
    boolean gastar(TipoTributo t, int n) {
		int atual = this.tributos.get(t);
		if (atual < n)
			return false;
		this.tributos.put(t, atual - n);
		return true;
	}
    
    int quantidadeTributo(TipoTributo t){
    	return this.tributos.get(t);
    }
    
    void  receberTesouro(TipoTesouro t){
    	int atual = tesouros.get(t);
		this.tesouros.put(t, atual + 1);
    }
    
    int quantidadeTesouro(TipoTesouro t){
    	return this.tesouros.get(t);
    }
    
    boolean entregarTesouros(List<TipoTesouro> lista) {
		for (TipoTesouro t : TipoTesouro.values()) {
			int pedidos = 0;
			for (TipoTesouro x : lista)
				if (x == t) pedidos++;
			if (pedidos > this.tesouros.get(t))
				return false;                         
		}
		for (TipoTesouro t : lista)
			this.tesouros.put(t, this.tesouros.get(t) - 1);
		return true;
    }
    
    int totalDeTesouros() {                           
		int total = 0;
		for (int qtd : this.tesouros.values())
			total += qtd;
		return total;
	}
    
    boolean usarYurt() {                              
		if (this.yurtsRestantes == 0)
			return false;
		this.yurtsRestantes--;
		return true;
	}
    
    TabuleiroJogador getTabuleiro() {
    	return this.tabuleiro;
    }
}
