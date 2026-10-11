package model;

import java.util.List;

import org.hamcrest.core.IsInstanceOf;

class ColunaAcao 
{
	private Regiao regiao; // imagino q se região for nula, é a coluna branca
	private List<TipoAcao> acoesiniciais;
	private List<Melhoria> melhorias;
	private boolean ativada;
	
	Regiao getRegiao()
	{
		return this.regiao;
	}
	
	List<Melhoria> getMelhorias()
	{
		return this.melhorias;
	}
	
	boolean isAtivada()
	{
		return this.ativada;
	}

	int movimentos()
	{
		int nMovi = 0;
		
		for (TipoAcao acao : acoesiniciais) 
		{
        	if (acao == mover) 
		        {
		        	nMovi += 2;
		        }
		}
		
		for (Melhoria melhoria : melhorias)
		{
			if (melhoria.)
		}
	}