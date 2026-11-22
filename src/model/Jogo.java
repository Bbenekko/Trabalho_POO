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
		this.turnoAtual.verificarCartasAbertas();   // a demanda pode já estar cumprida no início do turno
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

	// votos de final de jogo: trilha do Kurultai + influência nas 3 regiões + carta secreta
	int pontuacaoFinal(Jogador j) {
		return this.tabuleiro.getTrilha().votosDe(j) + this.votosDeInfluencia(j) + this.votosDaCartaSecreta(j);
	}

	int votosDaCartaSecreta(Jogador j) {
		return (j.getCartaSecreta() == null) ? 0 : j.getCartaSecreta().votos(j, this.tabuleiro);
	}

	// ---------- influência sobre as 3 regiões (manual, pág. 14-15) ----------

	// tabela impressa acima da área de melhorias; o índice é o nº de melhorias na coluna da região (0 a 4)
	private static final int[] VOTOS_MAIS_YURTS = {1, 1, 2, 2, 3};
	private static final int[] VOTOS_SEGUNDO_MAIS_YURTS = {0, 1, 1, 2, 2};

	int votosDeInfluencia(Jogador j) {
		int total = 0;
		for (Regiao r : Regiao.values())
			total += this.votosDeInfluencia(j, r);
		return total;
	}

	// quem tem mais yurts na região e quem tem o 2º maior número ganham votos conforme as melhorias
	// na coluna dessa região. Empate em 1º: todos ficam em 1º e ninguém fica em 2º.
	// Empate em 2º: todos ficam em 2º. Yurts em cidades conquistadas contam; o de Karakorum não.
	int votosDeInfluencia(Jogador j, Regiao r) {
		int meus = this.tabuleiro.contarYurts(j, r);
		if (meus == 0)
			return 0;
		int maior = 0;
		int noTopo = 0;
		for (Jogador q : this.jogadores) {
			int y = this.tabuleiro.contarYurts(q, r);
			if (y > maior) {
				maior = y;
				noTopo = 1;
			} else if (y == maior) {
				noTopo++;
			}
		}
		int melhorias = Math.min(4, j.getTabuleiro().contarMelhorias(r));
		if (meus == maior)
			return VOTOS_MAIS_YURTS[melhorias];
		if (noTopo > 1)
			return 0;
		int segundo = 0;
		for (Jogador q : this.jogadores) {
			int y = this.tabuleiro.contarYurts(q, r);
			if (y < maior && y > segundo)
				segundo = y;
		}
		return (meus == segundo) ? VOTOS_SEGUNDO_MAIS_YURTS[melhorias] : 0;
	}

	// mais votos vence. Desempate (manual, pág. 16): mais tesouros não entregues; depois, quem está
	// mais perto de Karakorum. Se ainda houver empate, a vitória é compartilhada.
	List<Jogador> vencedores() {
		List<Jogador> melhores = new ArrayList<Jogador>(this.jogadores);
		melhores = this.manterMelhores(melhores, 0);
		melhores = this.manterMelhores(melhores, 1);
		melhores = this.manterMelhores(melhores, 2);
		return melhores;
	}

	// criterio 0: votos (maior); 1: tesouros não entregues (maior); 2: distância a Karakorum (menor)
	private List<Jogador> manterMelhores(List<Jogador> candidatos, int criterio) {
		List<Jogador> melhores = new ArrayList<Jogador>();
		int melhor = Integer.MIN_VALUE;
		for (Jogador j : candidatos) {
			int valor;
			if (criterio == 0)
				valor = this.pontuacaoFinal(j);
			else if (criterio == 1)
				valor = j.totalDeTesouros();
			else
				valor = -this.distanciaAKarakorum(j);
			if (valor > melhor) {
				melhor = valor;
				melhores.clear();
			}
			if (valor == melhor)
				melhores.add(j);
		}
		return melhores;
	}

	// menor número de paradas entre um peão do jogador e Karakorum (Altani: o peão mais perto)
	int distanciaAKarakorum(Jogador j) {
		Parada karakorum = this.tabuleiro.getParada(Tabuleiro.KARAKORUM);
		int menor = Integer.MAX_VALUE;
		for (Peao p : j.getPeoes())
			if (p.getPosicao() != null)
				menor = Math.min(menor, this.tabuleiro.distancia(p.getPosicao(), karakorum));
		return menor;
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