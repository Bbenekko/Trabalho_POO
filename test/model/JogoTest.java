package model;

import java.util.Arrays;

import org.junit.Test;

// Cada jogador escolhe uma cor e um herdeiro diferentes (manual, p. 3)
public class JogoTest {
	private static final int TIMEOUT = 2000;

	@Test(timeout = TIMEOUT, expected = IllegalArgumentException.class)
	public void jogoComCorRepetidaLancaExcecao() {
		new Jogo(Arrays.asList(Cor.vermelho, Cor.vermelho),
				Arrays.asList(Herdeiro.tolui, Herdeiro.jochi));
	}
}
