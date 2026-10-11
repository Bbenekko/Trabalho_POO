package model;

class Melhoria {
	private TipoMelhoria tipo;
	private Regiao regiao;
	private TipoTesouro tesouro;
	private int turnoDaCompra = -1;

	Melhoria(TipoMelhoria tipo, Regiao regiao, TipoTesouro tesouro){
		this.tipo = tipo;
		this.regiao = regiao;
		this.tesouro = tesouro;
	}
	
	TipoMelhoria getTipo(){
		return this.tipo;
	}
	
	Regiao getRegiao(){
		return this.regiao;
	}
	
	TipoTesouro getTesouro()	{
		return this.tesouro;
	}
	
	void registrarCompra(int turno) {
		this.turnoDaCompra = turno;
	}
	
	boolean utilizavelNoTurno(int turno) { 
		return this.turnoDaCompra != turno;
	}
}
