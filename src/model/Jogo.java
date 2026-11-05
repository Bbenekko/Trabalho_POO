package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class Jogo {
	private List<Jogador> jogadores = new ArrayList<Jogador>();
	private Tabuleiro tabuleiro;
	private Turno turnoAtual = null;     
	private int indiceAtual = 0;        
	private int numeroDoTurno = 0;
	private boolean fimDisparado = false;
	private boolean ultimaRodada = false;
	private boolean encerrado = false;
	private Map<Jogador, List<CartaConselheiroSecreto>> opcoesSecretas = new HashMap<Jogador, List<CartaConselheiroSecreto>>();

	Jogo(List<Cor> cores, List<Herdeiro> herdeiros) {
		if (cores.size() != herdeiros.size())
			throw new IllegalArgumentException("Cada jogador precisa de uma cor e de um herdeiro");
		if (cores.size() < 2 || cores.size() > 5)
			throw new IllegalArgumentException("O jogo e para 2 a 5 jogadores: " + cores.size());

		for (int i = 0; i < cores.size(); i++) {
			if (cores.indexOf(cores.get(i)) != i || herdeiros.indexOf(herdeiros.get(i)) != i)
				throw new IllegalArgumentException("Cor ou herdeiro repetido");
			this.jogadores.add(new Jogador(cores.get(i), herdeiros.get(i)));
		}

		this.tabuleiro = new Tabuleiro(cores.size());
		this.preparar();
		this.distribuirCartasSecretas();
	}

	private void preparar() {
		Parada karakorum = this.tabuleiro.getParada(Tabuleiro.KARAKORUM);
		Estoque estoque = this.tabuleiro.getEstoque();

		for (int i = 0; i < this.jogadores.size(); i++) {
			Jogador jogador = this.jogadores.get(i);

			for (Peao p : jogador.getPeoes())
				p.moverPara(karakorum);

			int moedas = (i < 2) ? 1 : 2;
			for (int m = 0; m < moedas; m++) {
				if (estoque.retirar(TipoTributo.moeda))
					jogador.receberTributo(TipoTributo.moeda, 1);
			}

			if (jogador.getHerdeiro() == Herdeiro.jochi) {
				Melhoria melhoria = this.tabuleiro.getAreaMelhorias().comprarDoTopo();
				jogador.getTabuleiro().adicionarMelhoria(melhoria, 3);
			}

			if (jogador.getHerdeiro() == Herdeiro.tolui) {
				jogador.usarYurt();
				karakorum.colocarYurtInicial(jogador);
			}
		}
	}

	List<Jogador> getJogadores() {
		return Collections.unmodifiableList(this.jogadores);
	}

	Tabuleiro getTabuleiro() {
		return this.tabuleiro;
	}

	Turno getTurnoAtual() {
		return this.turnoAtual;
	}

	Jogador jogadorDaVez() {
		return this.jogadores.get(this.indiceAtual);
	}

	boolean isFimDisparado() {
		return this.fimDisparado;
	}

	boolean encerrado() {
		return this.encerrado;
	}

	boolean iniciarTurno(int coluna) {
		if (this.encerrado || this.turnoAtual != null)
			return false;
		if (coluna < 0 || coluna > 3)
			return false;

		TabuleiroJogador tj = this.jogadorDaVez().getTabuleiro();
		if (!tj.ativar(coluna))                   
			return false;

		ColunaAcao escolhida = tj.getColuna(coluna);
		if (escolhida.recuperaAtivacoes())        
			tj.recuperarAtivacoes();

		this.numeroDoTurno++;
		this.turnoAtual = new Turno(this.numeroDoTurno, this.jogadorDaVez(), escolhida, this.tabuleiro);
		return true;
	}

	boolean encerrarTurno() {
		if (this.turnoAtual == null || !this.turnoAtual.podeEncerrar())
			return false;                        
		this.turnoAtual = null;

		// p. 13: cartas de conselheiro aberto cumpridas neste turno dao 1 voto
		for (CartaConselheiroAberto carta : this.tabuleiro.getCartasAbertas())
			carta.verificar(this.jogadorDaVez(), this.tabuleiro);

		if (!this.fimDisparado
				&& (this.tabuleiro.getTrilha().fimDisparado() || this.tabuleiro.todasCidadesConquistadas()))
			this.fimDisparado = true;

		this.indiceAtual = (this.indiceAtual + 1) % this.jogadores.size();

		if (this.indiceAtual == 0) {              
			if (this.ultimaRodada)
				this.encerrado = true;          
			else if (this.fimDisparado)
				this.ultimaRodada = true;        
		}
		return true;
	}

	// p. 19: cada jogador recebe 2 cartas secretas e escolhe 1 para manter
	private void distribuirCartasSecretas() {
		List<TipoConselheiroSecreto> tipos = new ArrayList<TipoConselheiroSecreto>();
		for (TipoConselheiroSecreto tipo : TipoConselheiroSecreto.values())
			tipos.add(tipo);
		Collections.shuffle(tipos);
		int proxima = 0;
		for (Jogador j : this.jogadores) {
			List<CartaConselheiroSecreto> duas = new ArrayList<CartaConselheiroSecreto>();
			duas.add(new CartaConselheiroSecreto(tipos.get(proxima++)));
			duas.add(new CartaConselheiroSecreto(tipos.get(proxima++)));
			this.opcoesSecretas.put(j, duas);
		}
	}

	List<CartaConselheiroSecreto> opcoesSecretas(Jogador j) {
		return Collections.unmodifiableList(this.opcoesSecretas.get(j));
	}

	boolean escolherCartaSecreta(Jogador j, int opcao) {
		if (j.getCartaSecreta() != null || opcao < 0 || opcao > 1)
			return false;
		j.escolherCartaSecreta(this.opcoesSecretas.get(j).get(opcao));
		return true;
	}

	// p. 16: votos da trilha do Kurultai + votos da carta secreta
	int pontuacaoFinal(Jogador j) {
		int total = this.tabuleiro.getTrilha().votosDe(j);
		if (j.getCartaSecreta() != null)
			total += j.getCartaSecreta().votos(j, this.tabuleiro);
		return total;
	}

	// jogadores com a maior pontuacao (mais de um em caso de empate)
	List<Jogador> vencedores() {
		List<Jogador> melhores = new ArrayList<Jogador>();
		int maior = -1;
		for (Jogador j : this.jogadores) {
			int pontos = this.pontuacaoFinal(j);
			if (pontos > maior) {
				maior = pontos;
				melhores.clear();
			}
			if (pontos == maior)
				melhores.add(j);
		}
		return melhores;
	}
}
