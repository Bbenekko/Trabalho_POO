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
		this.votos.replace(j, n);
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
