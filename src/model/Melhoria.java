package model;

class Melhoria 
{
	private TipoMelhoria tipo;
	private Regiao regiao;
	private TipoTesouro tesouro;
	private int turnoDaCompra;

	Melhoria(TipoMelhoria tipo, Regiao regiao, TipoTesouro tesouro)
	{
		this.tipo = tipo;
		this.regiao = regiao;
		this.tesouro = tesouro;
		this.turnoDaCompra = 0;
	}
	
	TipoMelhoria getTipo()
	{
		return this.tipo;
	}
	
	Regiao getRegiao()
	{
		return this.regiao;
	}
	
	// acho necessário
	TipoTesouro getTesouro()
	{
		return this.tesouro;
	}
	
	boolean utilizavelNoTurno(int turno)
	{
		return this.turnoDaCompra != turno;
	}
}
