package model;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

// Movimento do Khan entre as províncias do Khan (manual, p. 9)
public class KhanTest {
	private static final int TIMEOUT = 2000;
	private Khan khan;
	private Provincia doKhanA;

	@Before
	public void montaCenario() {
		khan = new Khan();
		doKhanA = new Provincia(1, TipoTributo.moeda, true);
	}

	@Test(timeout = TIMEOUT)
	public void khanNaoPodePermanecerNaMesmaProvincia() {
		khan.moverParaProvincia(doKhanA);
		assertFalse("Khan não pode ficar onde já está", khan.moverParaProvincia(doKhanA));
	}
}