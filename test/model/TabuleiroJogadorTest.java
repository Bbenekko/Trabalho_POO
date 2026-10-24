package model;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class TabuleiroJogadorTest {
	private static final int TIMEOUT = 2000;
	private static final int AMARELA = 0, VERDE = 1, VERMELHA = 2, BRANCA = 3;
	private TabuleiroJogador tabuleiro;

	@Before
	public void montaCenario() {
		tabuleiro = new TabuleiroJogador(Herdeiro.tolui);
	}

	private void completarPrimeiraLinha(TabuleiroJogador tj) {
		tj.adicionarMelhoria(new Melhoria(TipoMelhoria.pegar_tributo, Regiao.russia, null), AMARELA);
		tj.adicionarMelhoria(new Melhoria(TipoMelhoria.pegar_tributo, Regiao.persia, null), VERDE);
		tj.adicionarMelhoria(new Melhoria(TipoMelhoria.pegar_tributo, Regiao.china, null), VERMELHA);
	}

	@Test(timeout = TIMEOUT)
	public void recuperarAtivacoesLiberaColunaUsada() {
		tabuleiro.ativar(AMARELA);
		tabuleiro.recuperarAtivacoes();
		assertTrue("Depois de recuperar, a coluna pode ser ativada de novo", tabuleiro.ativar(AMARELA));
	}

	@Test(timeout = TIMEOUT)
	public void umaMelhoriaEmCadaColunaColoridaCompletaAPrimeiraLinha() {
		completarPrimeiraLinha(tabuleiro);
		assertTrue("Uma melhoria em cada coluna colorida completa a 1ª linha", tabuleiro.linhaCompleta(1));
	}

	@Test(timeout = TIMEOUT)
	public void primeiraLinhaComColunaBrancaVaziaDaMelhoriaGratis() {
		completarPrimeiraLinha(tabuleiro);
		assertTrue("Com o 1º espaço da coluna branca livre, a 1ª linha dá melhoria grátis",
				tabuleiro.ganhaMelhoriaGratis(1));
	}

	@Test(timeout = TIMEOUT)
	public void jochiNaoGanhaMelhoriaGratisNaPrimeiraLinha() {
		// p. 20: o Jochi começa com uma melhoria no 1º espaço da coluna branca
		TabuleiroJogador jochi = new TabuleiroJogador(Herdeiro.jochi);
		jochi.adicionarMelhoria(new Melhoria(TipoMelhoria.dois_movimentos, Regiao.china, null), BRANCA);
		completarPrimeiraLinha(jochi);
		assertFalse("Com o 1º espaço da coluna branca ocupado, a 1ª linha não dá melhoria grátis",
				jochi.ganhaMelhoriaGratis(1));
	}
}