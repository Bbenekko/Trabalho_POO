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
	public void estoqueComecaCom15Moedas() {
		assertEquals("O estoque começa com 15 moedas", 15, estoque.quantidadeTributo(TipoTributo.moeda));
	}

	@Test(timeout = TIMEOUT)
	public void naoRetiraEspadaDepoisQueAsDezAcabam() {
		for (int i = 0; i < 10; i++)
			estoque.retirar(TipoTributo.espada);
		assertFalse("Depois de retirar as 10 espadas não há mais nenhuma", estoque.retirar(TipoTributo.espada));
	}

	@Test(timeout = TIMEOUT)
	public void naoDevolvePecaAlemDoTotalInicial() {
		assertFalse("O estoque cheio não aceita uma 11ª peça de yurt", estoque.devolver(TipoTributo.yurt));
	}

	@Test(timeout = TIMEOUT)
	public void pilhaDeTesourosAcabaDepoisDe40() {
		for (int i = 0; i < 40; i++)
			estoque.sortearTesouro();
		assertNull("Depois de 40 tesouros a pilha está vazia", estoque.sortearTesouro());
	}
}