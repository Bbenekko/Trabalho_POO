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
		this(cores, herdeiros, null);
	}

	// nomes == null: cada jogador fica com o nome do herdeiro
	Jogo(List<Cor> cores, List<Herdeiro> herdeiros, List<String> nomes) {
		if (cores.size() != herdeiros.size() || (nomes != null && nomes.size() != cores.size()))
			throw new IllegalArgumentException("Cada jogador precisa de uma cor e de um herdeiro");
		if (cores.size() < 2 || cores.size() > 5)
			throw new IllegalArgumentException("O jogo e para 2 a 5 jogadores: " + cores.size());

		for (int i = 0; i < cores.size(); i++) {
			if (cores.indexOf(cores.get(i)) != i || herdeiros.indexOf(herdeiros.get(i)) != i)
				throw new IllegalArgumentException("Cor ou herdeiro repetido");
			Jogador novo = new Jogador(cores.get(i), herdeiros.get(i));
			if (nomes != null && nomes.get(i) != null && !nomes.get(i).trim().isEmpty())
				novo.definirNome(nomes.get(i).trim());
			this.jogadores.add(novo);
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

	int pontuacaoFinal(Jogador j) {
		int total = this.tabuleiro.getTrilha().votosDe(j);
		if (j.getCartaSecreta() != null)
			total += j.getCartaSecreta().votos(j, this.tabuleiro);
		return total;
	}

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

	// ---------- usados pelo salvamento e pela recuperação (ArquivoJogo) ----------

	int getIndiceAtual() {
		return this.indiceAtual;
	}

	int getNumeroDoTurno() {
		return this.numeroDoTurno;
	}

	boolean isUltimaRodada() {
		return this.ultimaRodada;
	}

	void restaurar(int indiceAtual, int numeroDoTurno, boolean fimDisparado, boolean ultimaRodada,
			boolean encerrado) {
		if (indiceAtual < 0 || indiceAtual >= this.jogadores.size())
			throw new IllegalArgumentException("Jogador da vez inválido: " + indiceAtual);
		this.turnoAtual = null;
		this.indiceAtual = indiceAtual;
		this.numeroDoTurno = numeroDoTurno;
		this.fimDisparado = fimDisparado;
		this.ultimaRodada = ultimaRodada;
		this.encerrado = encerrado;
	}

	void definirOpcoesSecretas(Jogador j, List<CartaConselheiroSecreto> duas) {
		this.opcoesSecretas.put(j, new ArrayList<CartaConselheiroSecreto>(duas));
	}

	// rodada atual, começando em 1: numeroDoTurno conta os turnos já iniciados e cada rodada tem
	// um turno de cada jogador, sempre na mesma ordem
	int rodadaAtual() {
		int jogados = (this.turnoAtual != null) ? this.numeroDoTurno - 1 : this.numeroDoTurno;
		return jogados / this.jogadores.size() + 1;
	}
}