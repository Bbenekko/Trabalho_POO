package model;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;

class Jogador 
{
	private Cor cor;
	private Herdeiro herdeiro;
	private List<Peao> peoes;
	private TabuleiroJogador tabuleiro;
	private HashMap<TipoTributo, Integer> tributos;
	private HashMap<TipoTesouro, Integer> tesouros;
    private int yurtsRestantes;
    private List<PecaBonus> bonus;
    private CartaConselheiroSecreto cartaSecreta;
    
    public Jogador(Cor cor, Herdeiro herdeiro, List<Peao> peoes, TabuleiroJogador tabuleiro) 
    {
		this.cor = cor;
		this.herdeiro = herdeiro;
		this.peoes = peoes;
		this.tabuleiro = tabuleiro;

		HashMap<TipoTributo, Integer> tributos = new HashMap<>();
		for (TipoTributo tributo : TipoTributo.values()) 
		{
            tributos.put(tributo, 0);
        }
		this.tributos = tributos;
		
		HashMap<TipoTesouro, Integer> tesouros = new HashMap<>();
		for (TipoTesouro tesouro : TipoTesouro.values()) 
		{
            tesouros.put(tesouro, 0);
        }
		this.tesouros = tesouros;
		
		this.yurtsRestantes = 0;
        this.bonus = new List<PecaBonus>();
        this.cartaSecreta = null;
	}

	Cor getCor()
    {
    	return this.cor;
    }

    Herdeiro getHerdeiro()
    {
    	return this.herdeiro;
    }
    
    List<Peao> getPeoes()
    {
    	return Collections.unmodifiableList(this.peoes);
    }
    
    TabuleiroJogador getTabuleiro()
    {
    	return this.tabuleiro;
    }
    
    int getYurtsRestantes()
    {
    	return this.yurtsRestantes;
    }
    
    int quantidadeTesouro(TipoTesouro t)
    {
    	return this.tesouros.get(t);
    }
    
    void receberTributo(TipoTributo t, int n)
    {
    	int atual = tributos.get(t);
		this.tributos.put(t, atual + n);
    }
    
    boolean gastar(TipoTributo t, int n)
    {
    	int atual = tributos.get(t);
    	
    	if (atual >= n)
    	{
    		this.tributos.put(t, atual-n);
    		return true;
    	}
    	else
    	{
    		return false;
    	}
    }
    
    int quantidadeTributo(TipoTributo t)
    {
    	return this.tributos.get(t);
    }
    
    void  receberTesouro(TipoTesouro t)
    {
    	int atual = tesouros.get(t);
		this.tesouros.put(t, atual + 1);
    }
    
    boolean entregarTesouro(List<TipoTesouro> listaTesouros)
    {
    	for (TipoTesouro tesouro : listaTesouros)
    	{
    		int atual = this.tesouros.get(tesouro);
    		if (atual == 0) {return false;}
    		else {this.tesouros.put(tesouro, atual - 1);
    	}
    		
    	return true;
    }
    	
    int totalDeTesouros()
    {
    	 return this.tesouros.values().stream()
    			 .mapToInt(Integer::intValue)
    			 .sum();
    }
    
    void escolherCartaSecreta(CartaConselheiroSecreto ccs)
    {
    	this.cartaSecreta = ccs;
    }
    
    CartaConselheiroSecreto getConselheiroSecreto()
    {
    	return this.cartaSecreta;
    }
}
