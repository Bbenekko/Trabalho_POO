package model;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class KhanTest {
	private static final int TIMEOUT = 2000;
	private Khan khan;
	private Provincia doKhanA, doKhanB, comum;

	@Before
	public void montaCenario() {
		khan = new Khan();
		doKhanA = new Provincia(1, TipoTributo.moeda, true);
		doKhanB = new Provincia(2, TipoTributo.espada, true);
		comum = new Provincia(3, TipoTributo.yurt, false);
	}

	@Test(timeout = TIMEOUT)
	public void khanNaoPodePermanecerNaMesmaProvincia() {
		khan.moverParaProvincia(doKhanA);
		assertFalse("Khan não pode ficar onde já está", khan.moverParaProvincia(doKhanA));
	}

	@Test(timeout = TIMEOUT)
	public void khanNaoVaiParaProvinciaSemIcone() {
		assertFalse("Só províncias do Khan são destinos válidos", khan.moverParaProvincia(comum));
	}

	@Test(timeout = TIMEOUT)
	public void khanNaoPodePermanecerNaAreaDeMelhorias() {
		khan.moverParaMelhorias();
		assertFalse("Khan não pode ficar na área de melhorias", khan.moverParaMelhorias());
	}

	@Test(timeout = TIMEOUT)
	public void khanSaiDaAreaDeMelhoriasParaOutraProvincia() {
		khan.moverParaProvincia(doKhanA);
		khan.moverParaMelhorias();
		khan.moverParaProvincia(doKhanB);
		assertSame("Khan deveria terminar na província B", doKhanB, khan.getProvincia());
	}
}