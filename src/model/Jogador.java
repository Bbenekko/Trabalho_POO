package model;

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
    	return this.peoes;
    }
    
    TabuleiroJogador getTabuleiro()
    {
    	return this.tabuleiro;
    }
    
    int getYurtsRestantes()
    {
    	return this.yurtsRestantes;
    }
    
    int getQuantidadeTesouro(TipoTesouro t)
    {
    	//return this.tesouros.
    }
}
