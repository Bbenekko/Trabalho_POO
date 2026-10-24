package model;

import java.util.Arrays;

import org.junit.Test;

public class JogoTest {
	private static final int TIMEOUT = 2000;

	@Test(timeout = TIMEOUT, expected = IllegalArgumentException.class)
	public void jogoComCorRepetidaLancaExcecao() {
		new Jogo(Arrays.asList(Cor.vermelho, Cor.vermelho),
				Arrays.asList(Herdeiro.tolui, Herdeiro.jochi));
	}
}
