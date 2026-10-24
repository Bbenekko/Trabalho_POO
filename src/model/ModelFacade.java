package model;

import java.util.ArrayList;
import java.util.List;

// Unica classe publica do model: View e Controller so falam com o jogo por aqui.
// Recebe e devolve apenas tipos simples (int, boolean, String e listas deles).
// Jogadores sao identificados pelo indice na ordem de jogo (0..n-1), peoes pelo indice no jogador,
// paradas e provincias pelo id do Tabuleiro, cidades pelo nome e enums pelo nome da constante.
public class ModelFacade {
	private static ModelFacade instancia = null;
	private Jogo jogo = null;

	private ModelFacade() {
	}

	public static ModelFacade getInstance() {
		if (instancia == null)
			instancia = new ModelFacade();
		return instancia;
	}

	// ===================== PARTIDA =====================

	public void novoJogo(List<String> cores, List<String> herdeiros) {
		List<Cor> c = new ArrayList<Cor>();
		for (String nome : cores)
			c.add(Cor.valueOf(nome));
		List<Herdeiro> h = new ArrayList<Herdeiro>();
		for (String nome : herdeiros)
			h.add(Herdeiro.valueOf(nome));
		this.jogo = new Jogo(c, h);
	}

	// usa as primeiras cores e herdeiros, na ordem dos enums
	public void novoJogo(int numJogadores) {
		if (numJogadores < 2 || numJogadores > 5)
			throw new IllegalArgumentException("O jogo e para 2 a 5 jogadores: " + numJogadores);
		List<String> cores = new ArrayList<String>();
		List<String> herdeiros = new ArrayList<String>();
		for (int i = 0; i < numJogadores; i++) {
			cores.add(Cor.values()[i].name());
			herdeiros.add(Herdeiro.values()[i].name());
		}
		this.novoJogo(cores, herdeiros);
	}

	public boolean jogoIniciado() {
		return this.jogo != null;
	}

	public boolean jogoTerminou() {
		return this.jogo().encerrado();
	}

	public boolean fimDisparado() {
		return this.jogo().isFimDisparado();
	}

	public int numeroDeJogadores() {
		return this.jogo().getJogadores().size();
	}

	public int jogadorDaVez() {
		return this.indiceDe(this.jogo().jogadorDaVez());
	}

	// ===================== ACOES DO TURNO =====================

	public boolean ativarColuna(int coluna) {
		return this.jogo().iniciarTurno(coluna);
	}

	public boolean turnoEmAndamento() {
		return this.jogo().getTurnoAtual() != null;
	}

	public boolean podeMover(int peao, int idParada) {
		return this.podeMover(peao, listaDe(idParada));
	}

	public boolean podeMover(int peao, List<Integer> caminho) {
		Turno turno = this.jogo().getTurnoAtual();
		Peao p = this.peaoDaVez(peao);
		if (turno == null || p == null)
			return false;
		return turno.podeMover(p, this.paradas(caminho));
	}

	public boolean mover(int peao, int idParada) {
		return this.mover(peao, listaDe(idParada));
	}

	public boolean mover(int peao, List<Integer> caminho) {
		Turno turno = this.jogo().getTurnoAtual();
		Peao p = this.peaoDaVez(peao);
		if (turno == null || p == null)
			return false;
		return turno.mover(p, this.paradas(caminho));
	}

	public boolean pegarTributo(int idProvincia) {
		Turno turno = this.jogo().getTurnoAtual();
		Provincia p = this.tabuleiro().getProvincia(idProvincia);
		if (turno == null || p == null)
			return false;
		return turno.pegarTributo(p);
	}

	public boolean usarKhan(int idProvincia) {
		Turno turno = this.jogo().getTurnoAtual();
		Provincia p = this.tabuleiro().getProvincia(idProvincia);
		if (turno == null || p == null)
			return false;
		return turno.usarKhan(p);
	}

	public boolean usarKhanNasMelhorias() {
		Turno turno = this.jogo().getTurnoAtual();
		if (turno == null)
			return false;
		return turno.usarKhanNasMelhorias();
	}

	public boolean atacarCidade(String nomeCidade, String tesouro) {
		Turno turno = this.jogo().getTurnoAtual();
		Cidade c = this.tabuleiro().getCidade(nomeCidade);
		TipoTesouro t = enumOuNull(TipoTesouro.class, tesouro);
		if (turno == null || c == null || t == null)
			return false;
		return turno.atacar(c, t);
	}

	// espadas que o proximo ataque a esta cidade custaria no turno atual
	public int custoAtaque(String nomeCidade) {
		Turno turno = this.jogo().getTurnoAtual();
		Cidade c = this.tabuleiro().getCidade(nomeCidade);
		if (turno == null || c == null)
			return 0;
		return turno.custoAtaque(c);
	}

	public boolean construirYurt(int idParada) {
		Turno turno = this.jogo().getTurnoAtual();
		Parada p = this.tabuleiro().getParada(idParada);
		if (turno == null || p == null)
			return false;
		return turno.construirYurt(p);
	}

	public boolean comprarMelhoria(int posicao, int coluna) {
		Turno turno = this.jogo().getTurnoAtual();
		if (turno == null)
			return false;
		return turno.comprarMelhoria(posicao, coluna);
	}

	public boolean podeEncerrarTurno() {
		Turno turno = this.jogo().getTurnoAtual();
		return turno != null && turno.podeEncerrar();
	}

	public boolean encerrarTurno() {
		return this.jogo().encerrarTurno();
	}

	public int movimentosRestantes() {
		Turno turno = this.jogo().getTurnoAtual();
		return (turno == null) ? 0 : turno.getMovimentosRestantes();
	}

	public int tributosRestantes() {
		Turno turno = this.jogo().getTurnoAtual();
		return (turno == null) ? 0 : turno.getTributosRestantes();
	}

	public boolean khanUsado() {
		Turno turno = this.jogo().getTurnoAtual();
		return turno != null && turno.isKhanUsado();
	}

	// ===================== JOGADORES =====================

	public String corDoJogador(int jogador) {
		return this.jogador(jogador).getCor().name();
	}

	public String herdeiroDoJogador(int jogador) {
		return this.jogador(jogador).getHerdeiro().name();
	}

	public int numeroDePeoes(int jogador) {
		return this.jogador(jogador).getPeoes().size();
	}

	public int posicaoDoPeao(int jogador, int peao) {
		return this.jogador(jogador).getPeoes().get(peao).getPosicao().getId();
	}

	public boolean peaoPodeAgir(int jogador, int peao) {
		return this.jogador(jogador).getPeoes().get(peao).podeAgir();
	}

	public int tributosDoJogador(int jogador, String tipo) {
		return this.jogador(jogador).quantidadeTributo(TipoTributo.valueOf(tipo));
	}

	public int tesourosDoJogador(int jogador, String tipo) {
		return this.jogador(jogador).quantidadeTesouro(TipoTesouro.valueOf(tipo));
	}

	public int yurtsRestantes(int jogador) {
		return this.jogador(jogador).getYurtsRestantes();
	}

	public int votosDe(int jogador) {
		return this.tabuleiro().getTrilha().votosDe(this.jogador(jogador));
	}

	public boolean colunaAtivada(int jogador, int coluna) {
		return this.jogador(jogador).getTabuleiro().getColuna(coluna).isAtivada();
	}

	public List<String> melhoriasDaColuna(int jogador, int coluna) {
		return descrever(this.jogador(jogador).getTabuleiro().getColuna(coluna).getMelhorias());
	}

	// ===================== TABULEIRO =====================

	public List<String> cidadesReveladas() {
		List<String> nomes = new ArrayList<String>();
		for (Cidade c : this.tabuleiro().cidadesReveladas())
			nomes.add(c.getNome());
		return nomes;
	}

	public List<String> tesourosNaCidade(String nomeCidade) {
		List<String> tesouros = new ArrayList<String>();
		for (TipoTesouro t : this.cidade(nomeCidade).getTesouros())
			tesouros.add(t.name());
		return tesouros;
	}

	// indice do jogador que conquistou a cidade, ou -1
	public int conquistadorDaCidade(String nomeCidade) {
		Jogador j = this.cidade(nomeCidade).getConquistador();
		return (j == null) ? -1 : this.indiceDe(j);
	}

	public int tributosNaProvincia(int idProvincia) {
		return this.provincia(idProvincia).getQuantidade();
	}

	public String tipoDeTributoDaProvincia(int idProvincia) {
		return this.provincia(idProvincia).getTipoTributo().name();
	}

	// indices dos jogadores com yurt na parada (um por yurt)
	public List<Integer> yurtsNaParada(int idParada) {
		List<Integer> donos = new ArrayList<Integer>();
		for (Jogador j : this.parada(idParada).getYurts())
			donos.add(this.indiceDe(j));
		return donos;
	}

	// id da provincia onde esta o Khan, ou -1 se ele esta fora do mapa
	public int provinciaDoKhan() {
		Provincia p = this.tabuleiro().getKhan().getProvincia();
		return (p == null) ? -1 : p.getId();
	}

	public boolean khanNasMelhorias() {
		return this.tabuleiro().getKhan().isNaAreaDeMelhorias();
	}

	public List<String> melhoriasExpostas() {
		return descrever(this.tabuleiro().getAreaMelhorias().getExpostas());
	}

	public int fichaNeutra() {
		return this.tabuleiro().getTrilha().getFichaNeutra();
	}

	public int limiteKurultai() {
		return this.tabuleiro().getTrilha().getLimite();
	}

	// ===================== AUXILIARES =====================

	private Jogo jogo() {
		if (this.jogo == null)
			throw new IllegalStateException("Nenhum jogo iniciado");
		return this.jogo;
	}

	private Tabuleiro tabuleiro() {
		return this.jogo().getTabuleiro();
	}

	private Jogador jogador(int indice) {
		return this.jogo().getJogadores().get(indice);
	}

	private int indiceDe(Jogador j) {
		return this.jogo().getJogadores().indexOf(j);
	}

	private Peao peaoDaVez(int peao) {
		List<Peao> peoes = this.jogo().jogadorDaVez().getPeoes();
		if (peao < 0 || peao >= peoes.size())
			return null;
		return peoes.get(peao);
	}

	private Cidade cidade(String nome) {
		Cidade c = this.tabuleiro().getCidade(nome);
		if (c == null)
			throw new IllegalArgumentException("Cidade inexistente: " + nome);
		return c;
	}

	private Provincia provincia(int id) {
		Provincia p = this.tabuleiro().getProvincia(id);
		if (p == null)
			throw new IllegalArgumentException("Provincia inexistente: " + id);
		return p;
	}

	private Parada parada(int id) {
		Parada p = this.tabuleiro().getParada(id);
		if (p == null)
			throw new IllegalArgumentException("Parada inexistente: " + id);
		return p;
	}

	private List<Parada> paradas(List<Integer> ids) {
		List<Parada> caminho = new ArrayList<Parada>();
		for (int id : ids)
			caminho.add(this.tabuleiro().getParada(id));   // id invalido vira null e o Turno recusa
		return caminho;
	}

	private static List<Integer> listaDe(int id) {
		List<Integer> lista = new ArrayList<Integer>();
		lista.add(id);
		return lista;
	}

	// "tipo:regiao" ou "tesouro_virtual:regiao:tesouro"
	private static List<String> descrever(List<Melhoria> melhorias) {
		List<String> descricoes = new ArrayList<String>();
		for (Melhoria m : melhorias) {
			String d = m.getTipo().name() + ":" + m.getRegiao().name();
			if (m.getTesouro() != null)
				d += ":" + m.getTesouro().name();
			descricoes.add(d);
		}
		return descricoes;
	}

	private static <E extends Enum<E>> E enumOuNull(Class<E> tipo, String nome) {
		try {
			return Enum.valueOf(tipo, nome);
		} catch (IllegalArgumentException | NullPointerException e) {
			return null;
		}
	}
}
