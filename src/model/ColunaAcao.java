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

	int movimentos(int turnoAtual)
	{
		int nMovi = 0;
		
		for (TipoAcao acao : acoesiniciais) 
		{
        	if (acao == TipoAcao.mover) 
		        {
		        	nMovi += 1;
		        }
		}
		
		for (Melhoria melhoria : melhorias)
		{
			if (melhoria.getTipo() == TipoMelhoria.dois_movimentos && melhoria.utilizavelNoTurno(turnoAtual))
			{
				nMovi += 2;
			}
		}
		
		return nMovi;
	}
	
	int acoesDeTributo(int turno)
	{
		int nPega = 0;
		
		for (TipoAcao acao : acoesiniciais) 
		{
        	if (acao == TipoAcao.pegar_tributo) 
		        {
		        	nPega += 1;
		        }
		}
		
		for (Melhoria melhoria : melhorias)
		{
			if (melhoria.getTipo() == TipoMelhoria.pegar_tributo && melhoria.utilizavelNoTurno(turnoAtual))
			{
				nPega += 1;
			}
		}
		
		return nPega;
	}
	
	boolean exigeKhan()
	{
		for (TipoAcao acao : acoesiniciais)
		{
			if (acao == TipoAcao.usar_khan)
			{
				return true;
			}
		}
		
		return false;
	}
	
	boolean recuperaAtivacoes() // não entendi
	{
		ativada = false;
		return true;
	}
	
	//metodo novo
	boolean ativar()
	{
		return ativada = true;
	}
	
	boolean estaCheia()
	{
		return melhorias.size() == 4;
	}
	
	// metodo novo
	boolean adicionaMelhoria(Melhoria melhoria)
	{
		if (this.estaCheia()) {return false;}
		else
		{
			return this.melhorias.add(melhoria);
		}
	}
}