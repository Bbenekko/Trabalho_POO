package model;

import java.util.HashMap;

class TrilhaKurultai 
{
	private HashMap<Jogador, Integer>votos;
	private int fichaNeutra;
	private int limite;
	
	int getFichaNeutra()
	{
		return this.fichaNeutra;
	}
	
	int getLimite()
	{
		return this.limite;
	}

	void registrarVotos(Jogador j, int n)
	{
		int atual = votos.getOrDefault(j, 0);
		
		this.votos.put(j, atual + n);
		this.fichaNeutra += n;
		
		// detecta fim??
	}
	
	int votosDe(Jogador j)
	{
		return this.votos.get(j);
	}
	
	boolean fimDisparado()
	{
		return fichaNeutra >= limite;
	}
}
