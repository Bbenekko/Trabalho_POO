package model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ModelFacade implements ObservadoIF {
	public static final int EVENTO = 0;
	public static final int JOGADOR_DA_CARTELA = 1;

	public static final int ESTADO = 1;
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

	// ===================== OBSERVER =====================

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

	public void pedirCartela(int jogador) {
		this.jogador(jogador);
		this.jogadorDaCartela = jogador;
		this.notificar(ABRIR_CARTELA);
	}

	public void pedirCartas() {
		this.notificar(ABRIR_CARTAS);
	}

	// ===================== PARTIDA =====================

	public void novoJogo(List<String> cores, List<String> herdeiros) {
		this.novoJogo(null, cores, herdeiros, false);
	}

	// nomes pode ser null (cada um fica com o nome do herdeiro); sortearOrdem embaralha a ordem de jogo,
	// senão a ordem é a das listas recebidas
	public void novoJogo(List<String> nomes, List<String> cores, List<String> herdeiros, boolean sortearOrdem) {
		List<Integer> ordem = new ArrayList<Integer>();
		for (int i = 0; i < cores.size(); i++)
			ordem.add(i);
		if (sortearOrdem)
			Collections.shuffle(ordem);

		List<Cor> c = new ArrayList<Cor>();
		List<Herdeiro> h = new ArrayList<Herdeiro>();
		List<String> n = (nomes == null) ? null : new ArrayList<String>();
		for (int i : ordem) {
			c.add(Cor.valueOf(cores.get(i)));
			h.add(Herdeiro.valueOf(herdeiros.get(i)));
			if (n != null)
				n.add(nomes.get(i));
		}
		this.jogo = new Jogo(c, h, n);
		this.notificar(NOVO_JOGO);
	}

	// ===================== SALVAMENTO E RECUPERAÇÃO =====================

	// só antes de o jogador da vez começar a jogar (nenhuma coluna ativada ainda)
	public boolean podeSalvar() {
		return this.jogo != null && !this.jogo.encerrado() && this.jogo.getTurnoAtual() == null
				&& this.todosEscolheramCartaSecreta();
	}

	// grava a partida no arquivo texto escolhido pelo usuário; devolve false se não pôde
	public boolean salvarJogo(String caminho) {
		if (!this.podeSalvar())
			return false;
		try {
			ArquivoJogo.gravar(this.jogo, caminho);
			return true;
		} catch (IOException e) {
			System.out.println("Erro ao salvar: " + e.getMessage());
			return false;
		}
	}

	// lê a partida do arquivo; em caso de erro a partida atual (se houver) continua intacta
	public boolean carregarJogo(String caminho) {
		try {
			this.jogo = ArquivoJogo.ler(caminho);
		} catch (IOException e) {
			System.out.println("Erro ao carregar: " + e.getMessage());
			return false;
		}
		return true;
	}

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

	public boolean podeMover(int peao, int idParada) {
		List<Parada> caminho = this.caminhoAte(peao, idParada);
		return caminho != null && this.jogo().getTurnoAtual().podeMover(this.peaoDaVez(peao), caminho);
	}

	public boolean mover(int peao, int idParada) {
		List<Parada> caminho = this.caminhoAte(peao, idParada);
		return this.depois(caminho != null && this.jogo().getTurnoAtual().mover(this.peaoDaVez(peao), caminho), MOVEU_PEAO);
	}

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

	// recursos virtuais (moeda, espada ou yurt) que ainda restam no turno em andamento; 0 fora de turno
	public int virtuaisNoTurno(String tipo) {
		Turno turno = this.jogo().getTurnoAtual();
		TipoTributo t = enumOuNull(TipoTributo.class, tipo);
		return (turno == null || t == null) ? 0 : turno.getVirtuais(t);
	}

	public boolean khanUsado() {
		Turno turno = this.jogo().getTurnoAtual();
		return turno != null && turno.isKhanUsado();
	}

	// ===================== JOGADORES =====================

	public String corDoJogador(int jogador) {
		return this.jogador(jogador).getCor().name();
	}

	public String nomeDoJogador(int jogador) {
		return this.jogador(jogador).getNome();
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
		// no turno em andamento, só o peão escolhido (Altani) age; fora de turno, qualquer um pode vir a agir
		Turno turno = this.jogo().getTurnoAtual();
		if (turno == null || jogador != this.jogadorDaVez())
			return true;
		Peao p = this.jogador(jogador).getPeoes().get(peao);
		return turno.getPeaoEscolhido() == null || turno.getPeaoEscolhido() == p;
	}

	// Altani: o turno começou e ainda falta escolher qual dos 2 peões vai agir
	public boolean precisaEscolherPeao() {
		Turno turno = this.jogo().getTurnoAtual();
		return turno != null && turno.precisaEscolherPeao();
	}

	public boolean escolherPeao(int peao) {
		Turno turno = this.jogo().getTurnoAtual();
		Peao p = this.peaoDaVez(peao);
		return turno != null && p != null && this.depois(turno.escolherPeao(p), ESTADO);
	}

	// índice do peão que age no turno em andamento, ou -1 (sem turno ou Altani ainda não escolheu)
	public int peaoDoTurno() {
		Turno turno = this.jogo().getTurnoAtual();
		if (turno == null || turno.getPeaoEscolhido() == null)
			return -1;
		return this.jogo().jogadorDaVez().getPeoes().indexOf(turno.getPeaoEscolhido());
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

	public List<Integer> yurtsNaParada(int idParada) {
		List<Integer> donos = new ArrayList<Integer>();
		for (Jogador j : this.parada(idParada).getYurts())
			donos.add(this.indiceDe(j));
		return donos;
	}

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

	public int rodadaAtual() {
		return this.jogo().rodadaAtual();
	}

	// quantos votos ainda faltam para a ficha neutra chegar ao marco de fim (0 se já chegou)
	public int votosParaOFim() {
		return Math.max(0, this.limiteKurultai() - this.fichaNeutra());
	}

	public int limiteKurultai() {
		return this.tabuleiro().getTrilha().getLimite();
	}

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

	// entrega no pergaminho ligado ao espaço de bônus (0 a numeroDeEspacosBonus() - 1)
	public boolean entregarTesouros(int espaco) {
		Turno turno = this.jogo().getTurnoAtual();
		return turno != null && this.depois(turno.entregarTesouros(espaco), ESTADO);
	}

	public int paradaDaEntrega(int espaco) {
		return this.entrega(espaco).getIdParada();
	}

	public int votosDaEntrega(int espaco) {
		return this.entrega(espaco).getVotos();
	}

	// tesouros pedidos no pergaminho; "qualquer" para cada tesouro de tipo livre
	public List<String> tesourosDaEntrega(int espaco) {
		EntregaTesouros e = this.entrega(espaco);
		List<String> nomes = new ArrayList<String>();
		for (TipoTesouro t : e.getExigidos())
			nomes.add(t.name());
		for (int i = 0; i < e.getQuaisquer(); i++)
			nomes.add("qualquer");
		return nomes;
	}

	// usa a peça de bônus de índice "peca" (na lista de pecasBonusDoJogador) do jogador da vez
	public boolean usarPecaBonus(int peca) {
		Turno turno = this.jogo().getTurnoAtual();
		return turno != null && this.depois(turno.usarPecaBonus(peca), ESTADO);
	}

	// baús: os tesouros tirados da pilha esperando a escolha do jogador da vez (vazia se nada pendente)
	public List<String> tesourosDoBau() {
		List<String> nomes = new ArrayList<String>();
		Turno turno = this.jogo().getTurnoAtual();
		if (turno != null)
			for (TipoTesouro t : turno.getTesourosDoBau())
				nomes.add(t.name());
		return nomes;
	}

	public boolean escolherTesouroDoBau(int indice) {
		Turno turno = this.jogo().getTurnoAtual();
		return turno != null && this.depois(turno.escolherTesouroDoBau(indice), ESTADO);
	}

	// tesouros virtuais (melhorias de tesouro) ainda disponíveis no turno em andamento
	public List<String> tesourosVirtuaisNoTurno() {
		List<String> nomes = new ArrayList<String>();
		Turno turno = this.jogo().getTurnoAtual();
		if (turno != null)
			for (TipoTesouro t : turno.getTesourosVirtuais())
				nomes.add(t.name());
		return nomes;
	}

	public boolean pecaBonusUsada(int jogador, int peca) {
		return this.jogador(jogador).bonusUsado(peca);
	}

	// compra às cegas a melhoria do topo da pilha (2 moedas); vai para a coluna da região dela
	public boolean comprarMelhoriaDaPilha() {
		return this.comprarMelhoriaDaPilha(-1);
	}

	// colunaPreferida (0 a 2) só é usada por Jochi, que não precisa seguir a cor das melhorias
	public boolean comprarMelhoriaDaPilha(int colunaPreferida) {
		Turno turno = this.jogo().getTurnoAtual();
		return turno != null && this.depois(turno.comprarMelhoriaDaPilha(colunaPreferida), ESTADO);
	}

	// Jochi pode pôr qualquer melhoria em qualquer coluna
	public boolean melhoriaEmQualquerColuna(int jogador) {
		return this.jogador(jogador).getTabuleiro().aceitaQualquerCor();
	}

	// ----- Chagatai: trocar 1 movimento por 1 espada (1x por turno) -----

	public boolean podeTrocarMovimentoPorEspada() {
		Turno turno = this.jogo().getTurnoAtual();
		return turno != null && turno.podeTrocarMovimentoPorEspada();
	}

	public boolean trocarMovimentoPorEspada() {
		Turno turno = this.jogo().getTurnoAtual();
		return turno != null && this.depois(turno.trocarMovimentoPorEspada(), ESTADO);
	}

	// ----- Ogedei: pagar 1 moeda por 1 tesouro de qualquer cidade (1x por turno) -----

	public boolean podeComprarTesouroDeOgedei() {
		Turno turno = this.jogo().getTurnoAtual();
		return turno != null && turno.podeComprarTesouroDeOgedei();
	}

	public boolean comprarTesouroDeOgedei(String nomeCidade, String tesouro) {
		Turno turno = this.jogo().getTurnoAtual();
		Cidade c = this.tabuleiro().getCidade(nomeCidade);
		TipoTesouro t = enumOuNull(TipoTesouro.class, tesouro);
		if (turno == null || c == null || t == null)
			return false;
		return this.depois(turno.comprarTesouroDeOgedei(c, t), ESTADO);
	}

	public int melhoriasNaPilha() {
		return this.tabuleiro().getAreaMelhorias().tamanhoDaPilha();
	}

	public List<String> pecasBonusDoJogador(int jogador) {
		List<String> nomes = new ArrayList<String>();
		for (TipoPecaBonus p : this.jogador(jogador).getPecasBonus())
			nomes.add(p.name());
		return nomes;
	}

	private EntregaTesouros entrega(int espaco) {
		EntregaTesouros e = this.tabuleiro().getEntrega(espaco);
		if (e == null)
			throw new IllegalArgumentException("Espaço de bônus inexistente: " + espaco);
		return e;
	}

	public int numeroDeEspacosBonus() {
		return this.tabuleiro().numeroDeEspacosBonus();
	}

	public String pecaBonusNoEspaco(int espaco) {
		TipoPecaBonus t = this.tabuleiro().pecaBonusNoEspaco(espaco);
		return (t == null) ? null : t.name();
	}

	// ===================== FIM DE JOGO =====================

	public int pontuacaoFinal(int jogador) {
		return this.jogo().pontuacaoFinal(this.jogador(jogador));
	}

	// votos de final de jogo pela maioria de yurts nas 3 regiões
	public int votosDeInfluencia(int jogador) {
		return this.jogo().votosDeInfluencia(this.jogador(jogador));
	}

	public int votosDeInfluencia(int jogador, String regiao) {
		Regiao r = enumOuNull(Regiao.class, regiao);
		return (r == null) ? 0 : this.jogo().votosDeInfluencia(this.jogador(jogador), r);
	}

	public int votosDaCartaSecreta(int jogador) {
		return this.jogo().votosDaCartaSecreta(this.jogador(jogador));
	}

	// em ordem de desempate: mais votos, mais tesouros não entregues, mais perto de Karakorum
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

	public List<Integer> paradasDaProvincia(int idProvincia) {
		Provincia prov = this.provincia(idProvincia);
		List<Integer> ids = new ArrayList<Integer>();
		for (Parada p : this.tabuleiro().getParadas())
			if (p.getProvincias().contains(prov))
				ids.add(p.getId());
		return ids;
	}

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

	// a melhoria grátis pode ser a do topo da pilha, às cegas
	public boolean pegarMelhoriaGratisDaPilha() {
		Turno turno = this.jogo().getTurnoAtual();
		return turno != null && this.depois(turno.pegarMelhoriaGratisDaPilha(), ESTADO);
	}

	public String regiaoDaColuna(int jogador, int coluna) {
		Regiao r = this.jogador(jogador).getTabuleiro().getColuna(coluna).getRegiao();
		return (r == null) ? "branca" : r.name();
	}

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

	private List<Parada> caminhoAte(int peao, int idParada) {
		Turno turno = this.jogo().getTurnoAtual();
		Peao p = this.peaoDaVez(peao);
		Parada destino = this.tabuleiro().getParada(idParada);
		if (turno == null || p == null || destino == null)
			return null;
		return turno.caminhoAte(p, destino);
	}

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