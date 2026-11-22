package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

class Turno {
	private int numero;
	private Jogador jogador;
	private ColunaAcao coluna;
	private Tabuleiro tabuleiro;
	private int movimentosRestantes;
	private int tributosRestantes;
	private boolean khanUsado = false;
	private Map<Peao, List<Parada>> paradasVisitadas = new HashMap<Peao, List<Parada>>();
	private Map<Cidade, Integer> ataquesPorCidade = new HashMap<Cidade, Integer>();
	private Map<TipoTributo, Integer> virtuais = new HashMap<TipoTributo, Integer>();
	private List<Integer> entregasFeitas = new ArrayList<Integer>();
	// Altani tem 2 peões: no turno, só um age; ele é escolhido antes da 1ª ação com peão e fica até o fim
	private Peao peaoEscolhido = null;
	private boolean trocaDeChagataiFeita = false;   // 1 movimento → 1 espada, uma vez por turno
	private boolean tesouroDeOgedeiComprado = false; // 1 moeda → 1 tesouro, uma vez por turno
	// melhorias de tesouro da coluna ativada: cada uma vale 1 tesouro do seu tipo neste turno
	private List<TipoTesouro> tesourosVirtuais = new ArrayList<TipoTesouro>();
	// peça de bônus "baús": 3 tesouros tirados da pilha esperando o jogador escolher 1 (null = nada pendente)
	private List<TipoTesouro> bauParaEscolher = null;

	Turno(int numero, Jogador jogador, ColunaAcao coluna, Tabuleiro tabuleiro) {
		this.numero = numero;
		this.jogador = jogador;
		this.coluna = coluna;
		this.tabuleiro = tabuleiro;
		this.movimentosRestantes = coluna.movimentos(numero);
		this.tributosRestantes = coluna.acoesDeTributo(numero);
		for (TipoTributo t : TipoTributo.values())
			this.virtuais.put(t, coluna.virtuais(t, numero));
		this.tesourosVirtuais.addAll(coluna.tesourosVirtuais(numero));

		for (Peao p : jogador.getPeoes()) {
			List<Parada> visitadas = new ArrayList<Parada>();
			visitadas.add(p.getPosicao());
			this.paradasVisitadas.put(p, visitadas);
		}
	}

	Jogador getJogador() {
		return this.jogador;
	}

	int getMovimentosRestantes() {
		return this.movimentosRestantes;
	}

	int getTributosRestantes() {
		return this.tributosRestantes;
	}

	boolean isKhanUsado() {
		return this.khanUsado;
	}

	// moedas, espadas ou fichas de yurt virtuais que ainda restam neste turno (somem ao encerrar)
	int getVirtuais(TipoTributo tipo) {
		return this.virtuais.get(tipo);
	}


	// ---------- MOVER ----------
	// ---------- PEÃO QUE AGE (Altani) ----------

	boolean precisaEscolherPeao() {
		return this.jogador.getPeoes().size() > 1 && this.peaoEscolhido == null;
	}

	// escolhe o peão que vai agir no turno; só uma vez por turno
	boolean escolherPeao(Peao peao) {
		if (peao == null || peao.getDono() != this.jogador)
			return false;
		if (this.peaoEscolhido != null)
			return this.peaoEscolhido == peao;
		this.peaoEscolhido = peao;
		return true;
	}

	Peao getPeaoEscolhido() {
		return this.peaoQueAge();
	}

	boolean podeMover(Peao peao, List<Parada> caminho) {
		if (peao.getDono() != this.jogador || caminho.isEmpty() || caminho.contains(null))
			return false;
		Peao agente = this.peaoQueAge();
		if (agente != null && agente != peao)
			return false;                          // Altani: o outro peão não age neste turno
		int custo = this.custoDoCaminho(peao, caminho);
		if (custo < 0 || custo > this.movimentosRestantes)
			return false;
		Parada destino = caminho.get(caminho.size() - 1);
		return destino.podeTerminarAqui(peao);
	}

	boolean mover(Peao peao, List<Parada> caminho) {
		if (!this.podeMover(peao, caminho))
			return false;

		List<Parada> novasVisitadas = new ArrayList<Parada>();
		for (int i = 0; i < caminho.size(); i++) {
			Parada p = caminho.get(i);
			boolean ultima = (i == caminho.size() - 1);
			if (ultima || !p.temYurtDe(this.jogador))
				novasVisitadas.add(p);
		}

		this.escolherPeao(peao);                   // mover é uma ação: fixa o peão do turno
		Parada destino = caminho.get(caminho.size() - 1);
		this.movimentosRestantes -= this.custoDoCaminho(peao, caminho);
		peao.moverPara(destino);
		this.paradasVisitadas.get(peao).addAll(novasVisitadas);
		return this.concluir();
	}

	List<Parada> caminhoAte(Peao peao, Parada destino) {
		Parada origem = peao.getPosicao();
		if (destino == null || destino == origem)
			return null;

		Map<Parada, Integer> custo = new HashMap<Parada, Integer>();
		Map<Parada, Parada> veioDe = new HashMap<Parada, Parada>();
		LinkedList<Parada> fila = new LinkedList<Parada>();
		custo.put(origem, 0);
		fila.add(origem);

		while (!fila.isEmpty()) {
			Parada u = fila.removeFirst();
			for (Parada v : u.getVizinhas()) {
				if (v == destino || v == origem || !this.tabuleiro.saoAdjacentes(u, v))
					continue; 
				int passo = v.temYurtDe(this.jogador) ? 0 : 1;
				int novo = custo.get(u) + passo;
				if (!custo.containsKey(v) || novo < custo.get(v)) {
					custo.put(v, novo);
					veioDe.put(v, u);
					if (passo == 0)
						fila.addFirst(v);
					else
						fila.addLast(v);
				}
			}
		}

		Parada penultima = null;
		for (Parada u : destino.getVizinhas()) {
			if (custo.containsKey(u) && this.tabuleiro.saoAdjacentes(u, destino)
					&& (penultima == null || custo.get(u) < custo.get(penultima)))
				penultima = u;
		}
		if (penultima == null)
			return null;

		LinkedList<Parada> caminho = new LinkedList<Parada>();
		caminho.add(destino);
		for (Parada p = penultima; p != origem; p = veioDe.get(p))
			caminho.addFirst(p);
		return caminho;
	}

	private int custoDoCaminho(Peao peao, List<Parada> caminho) {
		Parada anterior = peao.getPosicao();
		int custo = 0;
		for (int i = 0; i < caminho.size(); i++) {
			Parada p = caminho.get(i);
			if (!this.tabuleiro.saoAdjacentes(anterior, p))
				return -1;
			boolean ultima = (i == caminho.size() - 1);
			if (ultima || !p.temYurtDe(this.jogador))
				custo++;
			anterior = p;
		}
		return custo;
	}

	// ---------- PEGAR TRIBUTO ----------

	boolean pegarTributo(Provincia p) {
		if (this.tributosRestantes == 0)
			return false;
		if (!this.provinciaAlcancavel(p))
			return false;
		if (!p.retirarTributo())                  
			return false;
		this.jogador.receberTributo(p.getTipoTributo(), 1);
		this.tributosRestantes--;
		return this.concluir();
	}


	boolean usarKhan(Provincia p) {
		if (!this.coluna.exigeKhan() || this.khanUsado)
			return false;
		if (!this.tabuleiro.getKhan().moverParaProvincia(p))
			return false;
		p.receberKhan(this.tabuleiro.getEstoque());
		this.khanUsado = true;
		return this.concluir();
	}

	boolean usarKhanNasMelhorias() {
		if (!this.coluna.exigeKhan() || this.khanUsado)
			return false;
		if (!this.tabuleiro.getKhan().moverParaMelhorias())
			return false;

		AreaMelhorias area = this.tabuleiro.getAreaMelhorias();
		Estoque estoque = this.tabuleiro.getEstoque();
		if (area.moedaAMostra() && estoque.retirar(TipoTributo.moeda))
			this.jogador.receberTributo(TipoTributo.moeda, 1);
		if (area.espadaAMostra() && estoque.retirar(TipoTributo.espada))
			this.jogador.receberTributo(TipoTributo.espada, 1);
		area.repor();

		this.khanUsado = true;
		return this.concluir();
	}

	int custoAtaque(Cidade c) {
		if (this.ataquesPorCidade.getOrDefault(c, 0) == 0)
			return 1;
		return 2;
	}

	boolean atacar(Cidade c, TipoTesouro t) {
		Peao agente = this.peaoQueAge();
		if (agente == null || !c.podeSerAtacada() || !c.getTesouros().contains(t))
			return false;
		if (!this.cidadeAlcancavel(agente, c))
			return false;
		if (!this.pagar(TipoTributo.espada, this.custoAtaque(c)))
			return false;

		c.tomarTesouro(this.jogador, t);
		this.jogador.receberTesouro(t);
		this.ataquesPorCidade.put(c, this.ataquesPorCidade.getOrDefault(c, 0) + 1);

		if (c.foiConquistada()) {                
			this.jogador.usarYurt();
			this.tabuleiro.revelarProximaCidade();
		}
		return this.concluir();
	}


	boolean construirYurt(Parada p) {
		Peao agente = this.peaoQueAge();
		if (agente == null)
			return false;
		boolean alcanca = this.paradasVisitadas.get(agente).contains(p);
		if (!alcanca && this.jogador.getHerdeiro() == Herdeiro.tolui)
			alcanca = this.tabuleiro.saoAdjacentes(agente.getPosicao(), p); 

		if (!alcanca || !p.podeReceberYurt() || this.jogador.getYurtsRestantes() == 0)
			return false;
		if (!this.pagar(TipoTributo.yurt, 1))
			return false;

		this.jogador.usarYurt();
		p.construirYurt(this.jogador);
		return this.concluir();
	}

	boolean comprarMelhoria(int posicao, int coluna) {
		if (coluna < 0 || coluna > 2)             
			return false;
		AreaMelhorias area = this.tabuleiro.getAreaMelhorias();
		if (posicao < 0 || posicao >= area.getExpostas().size())
			return false;

		Melhoria m = area.getExpostas().get(posicao);
		ColunaAcao destino = this.jogador.getTabuleiro().getColuna(coluna);
		boolean qualquerCor = this.jogador.getTabuleiro().aceitaQualquerCor();     // Jochi
		if (destino.estaCheia() || (!qualquerCor && m.getRegiao() != destino.getRegiao()))
			return false;                         
		if (!this.pagar(TipoTributo.moeda, 2))
			return false;

		area.comprar(posicao);
		m.registrarCompra(this.numero);
		this.jogador.getTabuleiro().adicionarMelhoria(m, coluna);
		return this.concluir();
	}

	// compra às cegas a melhoria do topo da pilha (2 moedas). Ela vai para a coluna da região dela;
	// se essa coluna estiver cheia, para a coluna branca. Sem lugar para ela, a compra é recusada.
	// colunaPreferida (0 a 2) só vale para Jochi, que pode pôr a melhoria em qualquer coluna
	boolean comprarMelhoriaDaPilha(int colunaPreferida) {
		AreaMelhorias area = this.tabuleiro.getAreaMelhorias();
		Melhoria topo = area.verTopo();
		if (topo == null)
			return false;
		TabuleiroJogador tj = this.jogador.getTabuleiro();
		int coluna = this.colunaDaRegiao(topo.getRegiao());
		if (tj.aceitaQualquerCor() && colunaPreferida >= 0 && colunaPreferida <= 2)
			coluna = colunaPreferida;
		if (coluna == -1 || tj.getColuna(coluna).estaCheia())
			coluna = 3;
		if (tj.getColuna(coluna).estaCheia())
			return false;
		if (!this.pagar(TipoTributo.moeda, 2))
			return false;

		Melhoria m = area.comprarDoTopo();
		m.registrarCompra(this.numero);
		tj.adicionarMelhoria(m, coluna);
		return this.concluir();
	}

	private int colunaDaRegiao(Regiao r) {
		List<ColunaAcao> colunas = this.jogador.getTabuleiro().getColunas();
		for (int c = 0; c < 3; c++)
			if (colunas.get(c).getRegiao() == r)
				return c;
		return -1;
	}

	boolean melhoriaGratisDisponivel() {
		TabuleiroJogador tj = this.jogador.getTabuleiro();
		int proximaLinha = tj.getColuna(3).getMelhorias().size() + 1;
		return tj.linhaCompleta(proximaLinha);
	}

	boolean pegarMelhoriaGratis(int posicao) {
		if (!this.melhoriaGratisDisponivel() || this.jogador.getTabuleiro().getColuna(3).estaCheia())
			return false;
		AreaMelhorias area = this.tabuleiro.getAreaMelhorias();
		if (posicao < 0 || posicao >= area.getExpostas().size())
			return false;
		Melhoria m = area.comprar(posicao);
		m.registrarCompra(this.numero);
		this.jogador.getTabuleiro().adicionarMelhoria(m, 3);
		return this.concluir();
	}

	boolean pegarMelhoriaGratisDaPilha() {
		if (!this.melhoriaGratisDisponivel() || this.jogador.getTabuleiro().getColuna(3).estaCheia())
			return false;
		Melhoria m = this.tabuleiro.getAreaMelhorias().comprarDoTopo();
		if (m == null)
			return false;
		m.registrarCompra(this.numero);
		this.jogador.getTabuleiro().adicionarMelhoria(m, 3);
		return this.concluir();
	}

	boolean temMelhoriaGratis(int coluna) {
		TabuleiroJogador tj = this.jogador.getTabuleiro();
		int linha = tj.getColuna(coluna).getMelhorias().size();
		return tj.linhaCompleta(linha) && tj.ganhaMelhoriaGratis(linha);
	}


	// ---------- ENTREGAR TESOUROS (pergaminhos) ----------

	// o peão que age precisa ter passado pela parada do pergaminho neste turno; cada pergaminho
	// aceita uma entrega por turno. Dá os votos e, se ainda estiver lá, a peça de bônus.
	boolean entregarTesouros(int espaco) {
		EntregaTesouros e = this.tabuleiro.getEntrega(espaco);
		if (e == null || this.entregasFeitas.contains(espaco))
			return false;
		Parada p = this.tabuleiro.getParada(e.getIdParada());
		Peao agente = this.peaoQueAge();
		if (agente == null || !this.paradasVisitadas.get(agente).contains(p))
			return false;
		List<TipoTesouro> usados = new ArrayList<TipoTesouro>();
		List<TipoTesouro> lista = e.tesourosAEntregar(this.jogador, this.tesourosVirtuais, usados);
		if (lista == null || !this.jogador.entregarTesouros(lista))
			return false;
		for (TipoTesouro t : usados)
			this.tesourosVirtuais.remove(t);

		this.tabuleiro.getTrilha().registrarVotos(this.jogador, e.getVotos());
		this.entregasFeitas.add(espaco);
		TipoPecaBonus peca = this.tabuleiro.retirarPecaBonus(espaco);
		if (peca != null)
			this.jogador.receberPecaBonus(peca);     // fica guardada; o efeito vem quando ela for usada
		return this.concluir();
	}

	// usa a peça de bônus i do jogador da vez (uma vez só por peça)
	boolean usarPecaBonus(int i) {
		TipoPecaBonus peca = this.jogador.usarPecaBonus(i);
		if (peca == null)
			return false;
		this.aplicarBonus(peca);
		return this.concluir();
	}

	// efeito da peça na hora em que é usada
	private void aplicarBonus(TipoPecaBonus peca) {
		switch (peca) {
			case cavalos:
				this.movimentosRestantes += 3;
				this.tributosRestantes += 1;
				break;
			case maos:
				this.tributosRestantes += 2;
				break;
			case voto:
				this.tabuleiro.getTrilha().registrarVotos(this.jogador, 1);
				break;
			case yurts:
				this.receberDoEstoque(TipoTributo.yurt, 2);
				break;
			case espadas:
				this.receberDoEstoque(TipoTributo.espada, 2);
				break;
			case moedas:
				this.receberDoEstoque(TipoTributo.moeda, 2);
				break;
			default: {      // baús: tira 3 tesouros da pilha; o jogador escolhe 1 e os outros são descartados
				List<TipoTesouro> tirados = new ArrayList<TipoTesouro>();
				for (int n = 0; n < 3; n++) {
					TipoTesouro t = this.tabuleiro.getEstoque().sortearTesouro();
					if (t != null)
						tirados.add(t);
				}
				if (tirados.size() == 1)
					this.jogador.receberTesouro(tirados.get(0));
				else if (tirados.size() > 1)
					this.bauParaEscolher = tirados;
			}
		}
	}

	private void receberDoEstoque(TipoTributo tipo, int quantidade) {
		for (int i = 0; i < quantidade; i++)
			if (this.tabuleiro.getEstoque().retirar(tipo))
				this.jogador.receberTributo(tipo, 1);
	}

	// ---------- HABILIDADES DOS HERDEIROS ----------

	// Chagatai: 1x por turno, deixa de usar 1 cavalo (movimento) para ganhar 1 espada
	boolean podeTrocarMovimentoPorEspada() {
		return this.jogador.getHerdeiro() == Herdeiro.chagatai && !this.trocaDeChagataiFeita
				&& this.movimentosRestantes > 0 && this.tabuleiro.getEstoque().quantidadeTributo(TipoTributo.espada) > 0;
	}

	boolean trocarMovimentoPorEspada() {
		if (!this.podeTrocarMovimentoPorEspada() || !this.tabuleiro.getEstoque().retirar(TipoTributo.espada))
			return false;
		this.movimentosRestantes--;
		this.jogador.receberTributo(TipoTributo.espada, 1);
		this.trocaDeChagataiFeita = true;
		return this.concluir();
	}

	// Ogedei: 1x por turno, paga 1 moeda e pega 1 tesouro de qualquer cidade revelada do mapa,
	// desde que não seja o último tesouro dela
	boolean podeComprarTesouroDeOgedei() {
		return this.jogador.getHerdeiro() == Herdeiro.ogedei && !this.tesouroDeOgedeiComprado;
	}

	boolean comprarTesouroDeOgedei(Cidade c, TipoTesouro t) {
		if (!this.podeComprarTesouroDeOgedei() || c == null || !c.isRevelada())
			return false;
		if (c.getTesouros().size() < 2 || !c.getTesouros().contains(t))
			return false;
		if (!this.pagar(TipoTributo.moeda, 1))
			return false;
		c.tomarTesouro(this.jogador, t);
		this.jogador.receberTesouro(t);
		this.tesouroDeOgedeiComprado = true;
		return this.concluir();
	}

	boolean podeEncerrar() {
		return (!this.coluna.exigeKhan() || this.khanUsado) && this.bauParaEscolher == null;
	}

	// ---------- BAÚS: escolha de 1 dos 3 tesouros ----------

	List<TipoTesouro> getTesourosDoBau() {
		return (this.bauParaEscolher == null) ? new ArrayList<TipoTesouro>()
				: Collections.unmodifiableList(this.bauParaEscolher);
	}

	boolean escolherTesouroDoBau(int i) {
		if (this.bauParaEscolher == null || i < 0 || i >= this.bauParaEscolher.size())
			return false;
		this.jogador.receberTesouro(this.bauParaEscolher.get(i));
		this.bauParaEscolher = null;           // os outros ficam fora do jogo
		return this.concluir();
	}

	List<TipoTesouro> getTesourosVirtuais() {
		return Collections.unmodifiableList(this.tesourosVirtuais);
	}

	// as cartas de conselheiro aberto valem "a qualquer momento do turno": confere depois de cada ação
	void verificarCartasAbertas() {
		for (CartaConselheiroAberto carta : this.tabuleiro.getCartasAbertas())
			carta.verificar(this.jogador, this.tabuleiro, this.tesourosVirtuais);
	}

	private boolean concluir() {
		this.verificarCartasAbertas();
		return true;
	}

	private boolean pagar(TipoTributo tipo, int quantidade) {
		int virtual = this.virtuais.get(tipo);
		int real = this.jogador.quantidadeTributo(tipo);
		if (virtual + real < quantidade)
			return false;

		int usarVirtual = Math.min(virtual, quantidade);
		this.virtuais.put(tipo, virtual - usarVirtual);
		int resto = quantidade - usarVirtual;
		if (resto > 0) {
			this.jogador.gastar(tipo, resto);
			for (int i = 0; i < resto; i++)
				this.tabuleiro.getEstoque().devolver(tipo);
		}
		return true;
	}

	// o peão que faz as ações do turno; null se o jogador (Altani) ainda não escolheu
	private Peao peaoQueAge() {
		if (this.peaoEscolhido != null)
			return this.peaoEscolhido;
		if (this.jogador.getPeoes().size() == 1)
			return this.jogador.getPeoes().get(0);
		return null;
	}

	private boolean provinciaAlcancavel(Provincia prov) {
		Peao agente = this.peaoQueAge();
		if (agente == null)
			return false;
		for (Parada p : this.paradasVisitadas.get(agente)) {
			if (p.getProvincias().contains(prov))
				return true;
		}
		return false;
	}

	private boolean cidadeAlcancavel(Peao peao, Cidade c) {
		for (Parada p : this.paradasVisitadas.get(peao)) {
			if (p.getCidades().contains(c))
				return true;
		}
		return false;
	}
}