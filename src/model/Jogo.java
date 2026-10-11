package model;

import java.util.List;

class Jogo 
{
	private List<Jogador> jogadores;
	private Tabuleiro tabuleiro;
	private Turno turnoAtual;
	private int indiceAtual;
	private int numeroDoTurno;
	private boolean fimDisparado;
	private boolean ultimaRodada;
	
	// acho que o construtor é vazio, pois a função preparar cria tudo
	//Jogo(List<Jogador> jogadores, Tabuleiro tabuleiro)
	//{
	//	this.jogadores = jogadores;
	//	this.tabuleiro = tabuleiro;
	//	// talvez seja necessário iniciar o jogo com um turno, mas n garanto isso ainda
	//}
	
	List<Jogador> getJogadores()
	{
		return this.jogadores;
	}
	
	Tabuleiro getTabuleiro()
	{
		return this.tabuleiro;
	}
	
	Turno getTurnoAtual()
	{
		return this.turnoAtual;
	}
	
	void preparar(int njogadores)
	{
		// muitas dúvidas, n é legal ja sabermos qual jogador é qual herdeiro antes do jogo??? não sei se receber o int é correto, mas talvez seria melhor receber uma lista com os jogadores direto
	}

	boolean iniciarTurno(int coluna)
	{
		this.jogadores.get(indiceAtual).getTabuleiro();
	}
}
