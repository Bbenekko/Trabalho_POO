package model;

import java.util.ArrayList;
import java.util.List;

// Unica classe publica do model: View e Controller so falam com o jogo por aqui.
// Recebe e devolve apenas tipos simples (int, boolean, String e listas deles).
// Jogadores sao identificados pelo indice na ordem de jogo (0..n-1), peoes pelo indice no jogador,
// paradas e provincias pelo id do Tabuleiro, cidades pelo nome e enums pelo nome da constante.
public class ModelFacade implements ObservadoIF {
	// o que get(EVENTO) devolve depois de um notify
	public static final int EVENTO = 0;
	public static final int JOGADOR_DA_CARTELA = 1;      // get(JOGADOR_DA_CARTELA): de quem e a cartela pedida

	public static final int ESTADO = 1;          // algo mudou no tabuleiro ou nos jogadores
	public static final int MOVEU_PEAO = 2;
	public static final int ABRIR_CARTELA = 3;
	public static final int ABRIR_CARTAS = 4;
	public static final int FIM_DE_JOGO = 5;
	public static final int NOVO_JOGO = 6;

	private static ModelFacade instancia = null;
	private Jogo jogo = null;
	private List<ObservadorIF> observadores = new ArrayList<ObservadorIF>();
	private int ultimoEvento = 0;
	private int jogadorDaCartela = 0;

	private ModelFacade() {
	}

	public static ModelFacade getInstance() {
		if (instancia == null)
			instancia = new ModelFacade();
		return instancia;
	}

	// ===================== OBSERVER (Cap. 17) =====================

	public void add(ObservadorIF o) {
		if (!this.observadores.contains(o))
			this.observadores.add(o);
	}

	public void remove(ObservadorIF o) {
		this.observadores.remove(o);
	}

	public int get(int i) {
		if (i == EVENTO)
			return this.ultimoEvento;
		if (i == JOGADOR_DA_CARTELA)
			return this.jogadorDaCartela;
		return 0;
	}

	// avisa todos os observadores; eles consultam o que precisam pela fachada
	private void notificar(int evento) {
		this.ultimoEvento = evento;
		List<ObservadorIF> copia = new ArrayList<ObservadorIF>(this.observadores);
		for (ObservadorIF o : copia)
			o.notify(this);
	}

	private boolean depois(boolean ok, int evento) {
		if (ok)
			this.notificar(evento);
		return ok;
	}

	// pedidos de exibicao: a janela abre porque foi notificada (enunciado da 3a iteracao)
	public void pedirCartela(int jogador) {
		this.jogador(jogador);                       // confere o indice
		this.jogadorDaCartela = jogador;
		this.notificar(ABRIR_CARTELA);
	}

	public void pedirCartas() {
		this.notificar(ABRIR_CARTAS);
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
		this.notificar(NOVO_JOGO);
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
		return this.depois(this.jogo().iniciarTurno(coluna), ESTADO);
	}

	public boolean turnoEmAndamento() {
		return this.jogo().getTurnoAtual() != null;
	}

	// clique no mapa: so o destino; o Turno procura o caminho mais barato
	public boolean podeMover(int peao, int idParada) {
		List<Parada> caminho = this.caminhoAte(peao, idParada);
		return caminho != null && this.jogo().getTurnoAtual().podeMover(this.peaoDaVez(peao), caminho);
	}

	public boolean mover(int peao, int idParada) {
		List<Parada> caminho = this.caminhoAte(peao, idParada);
		return this.depois(caminho != null && this.jogo().getTurnoAtual().mover(this.peaoDaVez(peao), caminho), MOVEU_PEAO);
	}

	// caminho escolhido parada a parada (sem a de partida; a ultima e o destino)
	public boolean podeMover(int peao, List<Integer> caminho) {
		Turno turno = this.jogo().getTurnoAtual();
		Peao p = this.peaoDaVez(peao);
		List<Parada> paradas = this.paradas(caminho);
		if (turno == null || p == null || paradas == null)
			return false;
		return turno.podeMover(p, paradas);
	}

	public boolean mover(int peao, List<Integer> caminho) {
		Turno turno = this.jogo().getTurnoAtual();
		Peao p = this.peaoDaVez(peao);
		List<Parada> paradas = this.paradas(caminho);
		if (turno == null || p == null || paradas == null)
			return false;
		return this.depois(turno.mover(p, paradas), MOVEU_PEAO);
	}

	public boolean pegarTributo(int idProvincia) {
		Turno turno = this.jogo().getTurnoAtual();
		Provincia p = this.tabuleiro().getProvincia(idProvincia);
		if (turno == null || p == null)
			return false;
		return this.depois(turno.pegarTributo(p), ESTADO);
	}

	public boolean usarKhan(int idProvincia) {
		Turno turno = this.jogo().getTurnoAtual();
		Provincia p = this.tabuleiro().getProvincia(idProvincia);
		if (turno == null || p == null)
			return false;
		return this.depois(turno.usarKhan(p), ESTADO);
	}

	public boolean usarKhanNasMelhorias() {
		Turno turno = this.jogo().getTurnoAtual();
		if (turno == null)
			return false;
		return this.depois(turno.usarKhanNasMelhorias(), ESTADO);
	}

	public boolean atacarCidade(String nomeCidade, String tesouro) {
		Turno turno = this.jogo().getTurnoAtual();
		Cidade c = this.tabuleiro().getCidade(nomeCidade);
		TipoTesouro t = enumOuNull(TipoTesouro.class, tesouro);
		if (turno == null || c == null || t == null)
			return false;
		return this.depois(turno.atacar(c, t), ESTADO);
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
		return this.depois(turno.construirYurt(p), ESTADO);
	}

	public boolean comprarMelhoria(int posicao, int coluna) {
		Turno turno = this.jogo().getTurnoAtual();
		if (turno == null)
			return false;
		return this.depois(turno.comprarMelhoria(posicao, coluna), ESTADO);
	}

	public boolean podeEncerrarTurno() {
		Turno turno = this.jogo().getTurnoAtual();
		return turno != null && turno.podeEncerrar();
	}

	public boolean encerrarTurno() {
		if (!this.jogo().encerrarTurno())
			return false;
		this.notificar(this.jogo().encerrado() ? FIM_DE_JOGO : ESTADO);
		return true;
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

	// nomes aceitos por novoJogo, na ordem dos enums
	public List<String> coresDisponiveis() {
		List<String> nomes = new ArrayList<String>();
		for (Cor c : Cor.values())
			nomes.add(c.name());
		return nomes;
	}

	public List<String> herdeirosDisponiveis() {
		List<String> nomes = new ArrayList<String>();
		for (Herdeiro h : Herdeiro.values())
			nomes.add(h.name());
		return nomes;
	}

	// ===================== CARTAS DE CONSELHEIRO =====================

	// tipos das 4 cartas abertas, na ordem do tabuleiro
	public List<String> cartasAbertas() {
		List<String> tipos = new ArrayList<String>();
		for (CartaConselheiroAberto c : this.tabuleiro().getCartasAbertas())
			tipos.add(c.getTipo().name());
		return tipos;
	}

	public boolean cartaAbertaRetirada(int i) {
		return this.tabuleiro().getCartasAbertas().get(i).isRetirada();
	}

	public List<String> opcoesSecretas(int jogador) {
		List<String> tipos = new ArrayList<String>();
		for (CartaConselheiroSecreto c : this.jogo().opcoesSecretas(this.jogador(jogador)))
			tipos.add(c.getTipo().name());
		return tipos;
	}

	public boolean escolherCartaSecreta(int jogador, int opcao) {
		return this.depois(this.jogo().escolherCartaSecreta(this.jogador(jogador), opcao), ESTADO);
	}

	// tipo da carta secreta do jogador, ou null se ainda nao escolheu
	public String cartaSecreta(int jogador) {
		CartaConselheiroSecreto c = this.jogador(jogador).getCartaSecreta();
		return (c == null) ? null : c.getTipo().name();
	}

	public boolean todosEscolheramCartaSecreta() {
		for (Jogador j : this.jogo().getJogadores())
			if (j.getCartaSecreta() == null)
				return false;
		return true;
	}

	// ===================== PEÇAS DE BÔNUS =====================

	public int numeroDeEspacosBonus() {
		return this.tabuleiro().numeroDeEspacosBonus();
	}

	// tipo da peça de bônus no espaço (0 a numeroDeEspacosBonus() - 1), ou null se o espaço está vazio
	public String pecaBonusNoEspaco(int espaco) {
		TipoPecaBonus t = this.tabuleiro().pecaBonusNoEspaco(espaco);
		return (t == null) ? null : t.name();
	}

	// ===================== FIM DE JOGO =====================

	public int pontuacaoFinal(int jogador) {
		return this.jogo().pontuacaoFinal(this.jogador(jogador));
	}

	public List<Integer> vencedores() {
		List<Integer> indices = new ArrayList<Integer>();
		for (Jogador j : this.jogo().vencedores())
			indices.add(this.indiceDe(j));
		return indices;
	}

	// ===================== GEOMETRIA (para a View achar provincias e cidades) =====================

	public List<Integer> idsDasProvincias() {
		List<Integer> ids = new ArrayList<Integer>();
		for (Provincia p : this.tabuleiro().getProvincias())
			ids.add(p.getId());
		return ids;
	}

	public List<String> nomesDasCidades() {
		List<String> nomes = new ArrayList<String>();
		for (Cidade c : this.tabuleiro().getCidades())
			nomes.add(c.getNome());
		return nomes;
	}

	// paradas em volta da provincia
	public List<Integer> paradasDaProvincia(int idProvincia) {
		Provincia prov = this.provincia(idProvincia);
		List<Integer> ids = new ArrayList<Integer>();
		for (Parada p : this.tabuleiro().getParadas())
			if (p.getProvincias().contains(prov))
				ids.add(p.getId());
		return ids;
	}

	// paradas ligadas a cidade
	public List<Integer> paradasDaCidade(String nomeCidade) {
		Cidade c = this.cidade(nomeCidade);
		List<Integer> ids = new ArrayList<Integer>();
		for (Parada p : this.tabuleiro().getParadas())
			if (p.getCidades().contains(c))
				ids.add(p.getId());
		return ids;
	}

	public boolean cidadeRevelada(String nomeCidade) {
		return this.cidade(nomeCidade).isRevelada();
	}

	public boolean provinciaDoKhan(int idProvincia) {
		return this.provincia(idProvincia).isProvinciaDoKhan();
	}

	public boolean melhoriaGratisDisponivel() {
		Turno turno = this.jogo().getTurnoAtual();
		return turno != null && turno.melhoriaGratisDisponivel();
	}

	public boolean pegarMelhoriaGratis(int posicao) {
		Turno turno = this.jogo().getTurnoAtual();
		return turno != null && this.depois(turno.pegarMelhoriaGratis(posicao), ESTADO);
	}

	// regiao da coluna (russia, persia, china) ou "branca" para a 4a
	public String regiaoDaColuna(int jogador, int coluna) {
		Regiao r = this.jogador(jogador).getTabuleiro().getColuna(coluna).getRegiao();
		return (r == null) ? "branca" : r.name();
	}

	// depois de comprar, a linha completa pode dar uma melhoria gratis (p. 12)
	public boolean temMelhoriaGratis(int coluna) {
		Turno turno = this.jogo().getTurnoAtual();
		return turno != null && turno.temMelhoriaGratis(coluna);
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

	// null se algum id nao existir
	private List<Parada> paradas(List<Integer> ids) {
		List<Parada> caminho = new ArrayList<Parada>();
		for (int id : ids) {
			Parada p = this.tabuleiro().getParada(id);
			if (p == null)
				return null;
			caminho.add(p);
		}
		return caminho;
	}

	// caminho mais barato do peao da vez ate a parada, ou null (sem turno, peao ou parada invalidos)
	private List<Parada> caminhoAte(int peao, int idParada) {
		Turno turno = this.jogo().getTurnoAtual();
		Peao p = this.peaoDaVez(peao);
		Parada destino = this.tabuleiro().getParada(idParada);
		if (turno == null || p == null || destino == null)
			return null;
		return turno.caminhoAte(p, destino);
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