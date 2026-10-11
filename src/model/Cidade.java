package model;

import java.util.List;

class Cidade 
{
	private String nome;
	private Regiao regiao;
	private boolean revelada;
	private List<TipoTesouro> tesouros;
	private Jogador conquistador;
	
	String getNome()
	{
		return this.nome;
	}
	
	Regiao getRegiao()
	{
		return this.regiao;
	}
	
	List<TipoTesouro> getTesouros()
	{
		return this.tesouros;
	}
	
	Jogador getConquistador()
	{
		return this.conquistador;
	}
	
	// esse método é realmente necessário???
	boolean podeSerAtacada()
	{
		return !this.tesouros.isEmpty();
	}
	
	// porque é boolean??
	boolean tomarTesouro(TipoTesouro t)
	{
		this.tesouros.remove(t);
		return true;
	}
	
	// já não dá pra checar se foi conquistada pelo getconquistador???
	boolean foiConquistada()
	{
		return this.conquistador != null;
	}
	
	// parece estar faltando uma função de revelar
}
