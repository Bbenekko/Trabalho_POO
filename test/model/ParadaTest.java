package model;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

// Regras de ocupação de paradas por peões e yurts (manual, p. 7 e 11)
public class ParadaTest {
	private static final int TIMEOUT = 2000;
	private Parada simples, dupla, karakorum;
	private Jogador vermelho, azul;

	@Before
	public void montaCenario() {
		simples = new Parada(1, TipoParada.simples, Regiao.russia);
		dupla = new Parada(2, TipoParada.dupla, Regiao.russia);
		karakorum = new Parada(Tabuleiro.KARAKORUM, TipoParada.karakorum, null);
		vermelho = new Jogador(Cor.vermelho, Herdeiro.tolui);
		azul = new Jogador(Cor.azul, Herdeiro.ogedei);
	}

	@Test(timeout = TIMEOUT)
	public void naoTerminaMovimentoEmParadaSimplesOcupada() {
		simples.receberPeao(new Peao(azul, true));
		assertFalse("Parada simples ocupada não recebe outro peão",
				simples.podeTerminarAqui(new Peao(vermelho, true)));
	}

	@Test(timeout = TIMEOUT)
	public void karakorumComportaTodosOsPeoes() {
		for (int i = 0; i < 6; i++) {
			karakorum.receberPeao(new Peao(azul, true));
		}
		assertTrue("Em Karakorum cabem todos os peões", karakorum.podeTerminarAqui(new Peao(vermelho, true)));
	}

	@Test(timeout = TIMEOUT)
	public void paradaDuplaAceitaDoisYurtsDoMesmoJogador() {
		dupla.construirYurt(vermelho);
		assertTrue("Parada dupla aceita 2 yurts, mesmo do mesmo jogador", dupla.construirYurt(vermelho));
	}

	@Test(timeout = TIMEOUT)
	public void karakorumNaoAceitaYurts() {
		assertFalse("Karakorum não pode ter yurts", karakorum.construirYurt(vermelho));
	}
}
