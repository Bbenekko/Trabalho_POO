package model;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class ProvinciaTest {
	private static final int TIMEOUT = 2000;
	private Provincia comum;
	private Provincia doKhan, alvo1, alvo2;

	@Before
	public void montaCenario() {
		comum = new Provincia(1, TipoTributo.moeda, false);
		doKhan = new Provincia(2, TipoTributo.espada, true);
		alvo1 = new Provincia(3, TipoTributo.yurt, false);
		alvo2 = new Provincia(4, TipoTributo.moeda, false);
		doKhan.adicionarAlvoDoKhan(alvo1);
		doKhan.adicionarAlvoDoKhan(alvo2);
	}

	@Test(timeout = TIMEOUT)
	public void naoRecebeQuartoTributo() {
		comum.adicionarTributo();
		comum.adicionarTributo();
		comum.adicionarTributo();
		assertFalse("Província com 3 tributos não pode receber o 4º", comum.adicionarTributo());
	}

	@Test(timeout = TIMEOUT)
	public void naoRetiraTributoDeProvinciaVazia() {
		assertFalse("Província vazia não tem tributo para retirar", comum.retirarTributo());
	}

	@Test(timeout = TIMEOUT)
	public void khanDaTributoAosDoisAlvos() {
		doKhan.receberKhan();
		assertEquals("Cada alvo do Khan deveria ganhar 1 tributo", 2,
				alvo1.getQuantidade() + alvo2.getQuantidade());
	}

	@Test(timeout = TIMEOUT)
	public void khanEmProvinciaCheiaAindaAbasteceAlvos() {
		doKhan.adicionarTributo();
		doKhan.adicionarTributo();
		doKhan.adicionarTributo();
		doKhan.receberKhan();
		assertEquals("Alvo deve receber tributo mesmo com a província do Khan cheia", 1,
				alvo1.getQuantidade());
	}
}