package model;

import java.util.List;
import java.util.Stack;

class AreaMelhorias 
{
	private List<Melhoria>expostas;
	private Stack<Melhoria> pilha;
	
	
	List<Melhoria> getExpostas()
	{
		return this.expostas;
	}
	
	int tamanhoDaPilha()
	{
		return this.pilha.size();
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
		return this.pilha.pop();
	}
	
	void repor()
	{
		if (this.expostas.size() <=3)
		{
			// TODO Jogador alvo ganha moeda
		}
		if (this.expostas.size() <= 1)
		{
			// TODO Jogador alvo ganha moeda e espada
		}
		
		while (this.expostas.size() < 5)
		{
			expostas.add(comprarDoTopo());
		}
	}
}
