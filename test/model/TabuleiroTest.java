package model;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class TabuleiroTest {
	private static final int TIMEOUT = 2000;
	private Tabuleiro tabuleiro;
	private Jogador jogador;

	@Before
	public void montaCenario() {
		tabuleiro = new Tabuleiro(3);
		jogador = new Jogador(Cor.vermelho, Herdeiro.tolui);
	}

	private void conquistar(Cidade cidade) {
		for (TipoTesouro t : cidade.getTesouros().toArray(new TipoTesouro[0])) {
			cidade.tomarTesouro(jogador, t);
		}
	}

	private int cidadesAtacaveis() {
		int n = 0;
		for (Cidade c : tabuleiro.cidadesReveladas()) {
			if (c.podeSerAtacada())
				n++;
		}
		return n;
	}

	@Test(timeout = TIMEOUT)
	public void revelarCidadeAposConquistaMantemTresCidadesAtacaveis() {
		conquistar(tabuleiro.cidadesReveladas().get(0));
		tabuleiro.revelarProximaCidade();
		assertEquals("Depois da conquista uma nova cidade com tesouros é revelada", 3, cidadesAtacaveis());
	}
}
