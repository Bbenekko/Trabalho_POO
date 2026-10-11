package model;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class EstoqueTest {
	private static final int TIMEOUT = 2000;
	private Estoque estoque;

	@Before
	public void montaCenario() {
		estoque = new Estoque();
	}

	@Test(timeout = TIMEOUT)
	public void comecaCom15Moedas() {
		assertEquals("Reserva inicial de moedas", 15, estoque.quantidadeTributo(TipoTributo.moeda));
	}

	@Test(timeout = TIMEOUT)
	public void naoRetiraDepoisDeAcabarAsEspadas() {
		for (int i = 0; i < 10; i++) {
			estoque.retirar(TipoTributo.espada);
		}
		assertFalse("Não há 11ª espada no estoque", estoque.retirar(TipoTributo.espada));
	}

	@Test(timeout = TIMEOUT)
	public void naoDevolvePecaAlemDoTotal() {
		assertFalse("Estoque cheio não aceita mais yurts", estoque.devolver(TipoTributo.yurt));
	}

	@Test(timeout = TIMEOUT)
	public void pilhaDeTesourosAcabaDepoisDe40() {
		for (int i = 0; i < 40; i++) {
			estoque.sortearTesouro();
		}
		assertNull("Depois de 40 sorteios não há mais tesouros", estoque.sortearTesouro());
	}
}