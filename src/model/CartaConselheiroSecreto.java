package model;

class CartaConselheiroSecreto extends CartaConselheiro
{
	private TipoConselheiroSecreto tipo;
	private Jogador jogador;
	
	CartaConselheiroSecreto(TipoConselheiroSecreto tipo, Jogador jogador)
	{
		this.tipo = tipo;
		this.jogador = jogador;
	}
	
	TipoConselheiroSecreto getTipo()
	{
		return this.tipo;
	}
	
	Jogador getJogador()
	{
		return this.jogador;
	}
}
