package model;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class ProvinciaTest {
	private static final int TIMEOUT = 2000;
	private Estoque estoque;
	private Provincia doKhan, alvo1, alvo2;

	@Before
	public void montaCenario() {
		estoque = new Estoque();
		doKhan = new Provincia(2, TipoTributo.espada, true);
		alvo1 = new Provincia(3, TipoTributo.yurt, false);
		alvo2 = new Provincia(4, TipoTributo.moeda, false);
		doKhan.adicionarAlvoDoKhan(alvo1);
		doKhan.adicionarAlvoDoKhan(alvo2);
	}

	@Test(timeout = TIMEOUT)
	public void khanEmProvinciaCheiaAindaAbasteceAlvos() {
		doKhan.receberDoEstoque(estoque);
		doKhan.receberDoEstoque(estoque);
		doKhan.receberDoEstoque(estoque);
		doKhan.receberKhan(estoque);
		assertEquals("Alvo deve receber tributo mesmo com a província do Khan cheia", 1,
				alvo1.getQuantidade());
	}
}
