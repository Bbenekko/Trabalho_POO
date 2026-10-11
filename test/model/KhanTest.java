package model;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

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
