package model;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

// Ativação de colunas e linha completa de melhorias (manual, p. 6, 9 e 12)
public class TabuleiroJogadorTest {
	private static final int TIMEOUT = 2000;
	private static final int AMARELA = 0, VERDE = 1, VERMELHA = 2;
	private TabuleiroJogador tabuleiro;

	@Before
	public void montaCenario() {
		tabuleiro = new TabuleiroJogador(Herdeiro.tolui);
	}

	private void adicionar(int coluna, Regiao regiao) {
		tabuleiro.adicionarMelhoria(new Melhoria(TipoMelhoria.pegar_tributo, regiao, null), coluna);
	}

	@Test(timeout = TIMEOUT)
	public void colunaJaAtivadaNaoPodeSerAtivadaDeNovo() {
		tabuleiro.ativar(AMARELA);
		assertFalse("Coluna ocupada por peça de ativação não pode ser usada", tabuleiro.ativar(AMARELA));
	}

	@Test(timeout = TIMEOUT)
	public void recuperarAtivacoesLiberaColunaUsada() {
		tabuleiro.ativar(AMARELA);
		tabuleiro.recuperarAtivacoes();
		assertTrue("Depois de recuperar, a coluna pode ser ativada de novo", tabuleiro.ativar(AMARELA));
	}

	@Test(timeout = TIMEOUT)
	public void completarPrimeiraLinhaDaMelhoriaGratis() {
		adicionar(AMARELA, Regiao.russia);
		adicionar(VERDE, Regiao.persia);
		adicionar(VERMELHA, Regiao.china);
		assertTrue("Linha completa com a coluna branca vazia dá melhoria grátis",
				tabuleiro.linhaCompleta(1) && tabuleiro.ganhaMelhoriaGratis(1));
	}
}
