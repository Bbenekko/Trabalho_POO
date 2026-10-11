package model;

import java.util.ArrayList;
import java.util.HashMap;
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

	Turno(int numero, Jogador jogador, ColunaAcao coluna, Tabuleiro tabuleiro) {
		this.numero = numero;
		this.jogador = jogador;
		this.coluna = coluna;
		this.tabuleiro = tabuleiro;
		this.movimentosRestantes = coluna.movimentos(numero);
		this.tributosRestantes = coluna.acoesDeTributo(numero);
		for (TipoTributo t : TipoTributo.values())
			this.virtuais.put(t, coluna.virtuais(t, numero));

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


	boolean mover(Peao peao, List<Parada> caminho) {
		if (peao.getDono() != this.jogador || caminho.isEmpty())
			return false;

		Parada anterior = peao.getPosicao();
		int custo = 0;
		List<Parada> novasVisitadas = new ArrayList<Parada>();

		for (int i = 0; i < caminho.size(); i++) {
			Parada p = caminho.get(i);
			if (!this.tabuleiro.saoAdjacentes(anterior, p))
				return false;
			boolean ultima = (i == caminho.size() - 1);
			if (!ultima && p.temYurtDe(this.jogador)) {
				
			} else {
				custo++;
				novasVisitadas.add(p);
			}
			anterior = p;
		}

		Parada destino = caminho.get(caminho.size() - 1);
		if (custo > this.movimentosRestantes)
			return false;
		if (!destino.podeTerminarAqui(peao))   
			return false;

		peao.moverPara(destino);
		this.movimentosRestantes -= custo;
		this.paradasVisitadas.get(peao).addAll(novasVisitadas);
		return true;
	}


	boolean pegarTributo(Provincia p) {
		if (this.tributosRestantes == 0)
			return false;
		if (!this.provinciaAlcancavel(p))
			return false;
		if (!p.retirarTributo())                  
			return false;
		this.jogador.receberTributo(p.getTipoTributo(), 1);
		this.tributosRestantes--;
		return true;
	}


	boolean usarKhan(Provincia p) {
		if (!this.coluna.exigeKhan() || this.khanUsado)
			return false;
		if (!this.tabuleiro.getKhan().moverParaProvincia(p))
			return false;
		p.receberKhan(this.tabuleiro.getEstoque());
		this.khanUsado = true;
		return true;
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
		return true;
	}

	int custoAtaque(Cidade c) {
		if (this.ataquesPorCidade.getOrDefault(c, 0) == 0)
			return 1;
		return 2;
	}

	boolean atacar(Cidade c, TipoTesouro t) {
		Peao agente = this.peaoQueAge();
		if (!c.podeSerAtacada() || !c.getTesouros().contains(t))
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
		return true;
	}


	boolean construirYurt(Parada p) {
		Peao agente = this.peaoQueAge();
		boolean alcanca = this.paradasVisitadas.get(agente).contains(p);
		if (!alcanca && this.jogador.getHerdeiro() == Herdeiro.tolui)
			alcanca = this.tabuleiro.saoAdjacentes(agente.getPosicao(), p); 

		if (!alcanca || !p.podeReceberYurt() || this.jogador.getYurtsRestantes() == 0)
			return false;
		if (!this.pagar(TipoTributo.yurt, 1))
			return false;

		this.jogador.usarYurt();
		p.construirYurt(this.jogador);
		return true;
	}

	boolean comprarMelhoria(int posicao, int coluna) {
		if (coluna < 0 || coluna > 2)             
			return false;
		AreaMelhorias area = this.tabuleiro.getAreaMelhorias();
		if (posicao < 0 || posicao >= area.getExpostas().size())
			return false;

		Melhoria m = area.getExpostas().get(posicao);
		ColunaAcao destino = this.jogador.getTabuleiro().getColuna(coluna);
		if (destino.estaCheia() || m.getRegiao() != destino.getRegiao())
			return false;                         
		if (!this.pagar(TipoTributo.moeda, 2))
			return false;

		area.comprar(posicao);
		m.registrarCompra(this.numero);
		this.jogador.getTabuleiro().adicionarMelhoria(m, coluna);
		return true;
	}

	boolean temMelhoriaGratis(int coluna) {
		TabuleiroJogador tj = this.jogador.getTabuleiro();
		int linha = tj.getColuna(coluna).getMelhorias().size();
		return tj.linhaCompleta(linha) && tj.ganhaMelhoriaGratis(linha);
	}


	boolean podeEncerrar() {
		return !this.coluna.exigeKhan() || this.khanUsado;
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

	private Peao peaoQueAge() {
		for (Peao p : this.jogador.getPeoes()) {
			if (p.podeAgir())
				return p;
		}
		return null;
	}

	private boolean provinciaAlcancavel(Provincia prov) {
		for (Parada p : this.paradasVisitadas.get(this.peaoQueAge())) {
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