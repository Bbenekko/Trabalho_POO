package model;

import java.util.HashMap;
import java.util.List;

class Turno 
{
	private int numero;
	private Jogador jogador;
	private ColunaAcao coluna;
	private int movimentosRestantes;
	private int tributosRestantes;
	private boolean khanUsado;
	private List<Parada> paradasVisitadas;
	private HashMap<Cidade, Integer> ataquesPorCidade;
	private HashMap<TipoTributo, Integer> virtuais;
	private Khan khan;
	private boolean primeiroAtaqueRealizado;
	private AreaMelhorias areaMelhorias;
	
	Jogador getjogador()
	{
		return this.jogador;
	}
	
	int getMovimentosRestantes()
	{
		return this.movimentosRestantes;
	}
	
	int getTributosRestantes()
	{
		return this.tributosRestantes;
	}
	
	boolean isKhanUsado()
	{
		return this.khanUsado;
	}
	
	boolean mover(Peao peao, Parada destino)
	{
		// n sei fazer
	}
	
	boolean pegarTributo(Provincia p)
	{
		if (p.getQuantidade() > 0)
		{
			this.jogador.receberTributo(p.getTipoTributo(), 1);
			return p.retirarTributo();
		}
		return false;
	}
	
	boolean usarKhan(Provincia p)
	{
		if(khan.moverParaProvincia(p))
		{
			return this.khanUsado = true;
		}
		return false;
	}
	
	boolean usarKhanNasMelhorias()
	{
		if(khan.moverParaMelhorias())
		{
			return this.khanUsado = true;
		}
		return false;
	}
	
	// assumi q ele quis dizer a quantidade de tesouros, logo troquei para custoAtaque para nTesouros
	int nTesouros(Cidade c)
	{
		return c.getTesouros().size();
	}
	
	
	boolean atacar(Cidade c, TipoTesouro t)
	{
		if(primeiroAtaqueRealizado)
		{
			if(jogador.quantidadeTributo(TipoTributo.espada) <2) {return false;}
			
			jogador.gastar(TipoTributo.espada, 2);
			c.tomarTesouro(jogador, t);
			jogador.receberTesouro(t);
			
			return true;
		}
		else
		{
			if(jogador.quantidadeTributo(TipoTributo.espada) <1) {return false;}
			
			jogador.gastar(TipoTributo.espada, 2);
			c.tomarTesouro(jogador, t);
			jogador.receberTesouro(t);
			
			return true;
		}
	}
	
	boolean contruirYurt(Parada p)
	{
		return p.construirYurt(this.jogador);
	}
	
	boolean comprarMelhoria(int posicao, int coluna)
	{
		Melhoria m = this.areaMelhorias.comprar(posicao);
		m.registrarCompra(this.numero);
		
		TabuleiroJogador tabuleiro = jogador.getTabuleiro();
		return tabuleiro.adicionarMelhoria(m, coluna);
	}
	
	boolean entregar(Conselheiro c, List<TipoTesouro> listaT)
	{
		// falta classe conselheiro
	}
	
	boolean usarBonus(PecaBonus b)
	{
		// falta peça bonus
	}
	
	boolean podeEncerrar()
	{
		// n sei o q isso quis dizer
	}
}
