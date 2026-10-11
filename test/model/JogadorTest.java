package model;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

// Entrega de tesouros aos conselheiros (manual, p. 12 e 13)
public class JogadorTest {
	private static final int TIMEOUT = 2000;
	private Jogador jogador;
	private List<TipoTesouro> duasPeles;

	@Before
	public void montaCenario() {
		jogador = new Jogador(Cor.vermelho, Herdeiro.tolui);
		jogador.receberTesouro(TipoTesouro.pele);
		duasPeles = new ArrayList<TipoTesouro>();
		duasPeles.add(TipoTesouro.pele);
		duasPeles.add(TipoTesouro.pele);
	}

	@Test(timeout = TIMEOUT)
	public void entregaRecusadaNaoRemoveNenhumTesouro() {
		jogador.entregarTesouros(duasPeles);
		assertEquals("Entrega recusada mantém a pele com o jogador", 1,
				jogador.quantidadeTesouro(TipoTesouro.pele));
	}
}
