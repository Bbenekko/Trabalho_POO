package model;

import java.util.List;
import java.util.Stack;

class AreaMelhorias 
{
	private List<Melhoria>expostas;
	private Stack<Melhoria> pilha;
	private boolean moedaDisponivel;
	private boolean espadaDisponivel;
	
	
	List<Melhoria> getExpostas()
	{
		return null;
	}
	
	int tamanhoDaPilha()
	{
		return pilha.size();
	}
	
	Melhoria comprar(int posicao) 
	{
	    if (posicao < 0 || posicao >= expostas.size()) 
	    {
	        throw new IllegalArgumentException("Não há melhoria na posição " + posicao);
	    }
	    return expostas.remove(posicao);   
	}
	
	Melhoria comprarDoTopo()
	{
		return null;
	}
	
	void repor()
	{

	}
}
