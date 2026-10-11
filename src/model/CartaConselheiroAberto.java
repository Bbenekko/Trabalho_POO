package model;

class CartaConselheiroAberto extends CartaConselheiro
{
	private TipoConselheiroAberto tipo;
	private boolean retirada;

	CartaConselheiroAberto(TipoConselheiroAberto tipo)
	{
		this.tipo = tipo;
		this.retirada = false;
	}
	
	TipoConselheiroAberto getTipo()
	{
		return this.tipo;
	}
	
	boolean getRetirada()
	{
		return this.retirada;
	}
	
	boolean cumpridaPor(Jogador j, Tabuleiro t)
	{
		t.getTrilha().registrarVotos(j, 1);
		return this.retirada = true;
	}
}
