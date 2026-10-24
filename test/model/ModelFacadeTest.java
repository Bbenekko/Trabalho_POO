package model;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;

public class ModelFacadeTest {
	private static final int TIMEOUT = 2000;
	private ModelFacade facade;

	@Before
	public void setUp() {
		this.facade = ModelFacade.getInstance();
		this.facade.novoJogo(Arrays.asList("vermelho", "azul"), Arrays.asList("altani", "chagatai"));
	}

	@Test(timeout = TIMEOUT)
	public void getInstanceDevolveSempreAMesmaInstancia() {
		assertSame(ModelFacade.getInstance(), ModelFacade.getInstance());
	}

	@Test(timeout = TIMEOUT)
	public void novoJogoPreparaJogadoresEmKarakorum() {
		assertEquals(2, facade.numeroDeJogadores());
		assertEquals(0, facade.jogadorDaVez());
		assertEquals("vermelho", facade.corDoJogador(0));
		assertEquals("altani", facade.herdeiroDoJogador(0));
		assertEquals(2, facade.numeroDePeoes(0));
		assertEquals(Tabuleiro.KARAKORUM, facade.posicaoDoPeao(0, 1));
		assertEquals(1, facade.tributosDoJogador(1, "moeda"));
		assertEquals(3, facade.cidadesReveladas().size());
		assertEquals(5, facade.melhoriasExpostas().size());
		assertFalse(facade.turnoEmAndamento());
	}

	@Test(timeout = TIMEOUT)
	public void turnoCompletoPelaFacade() {
		assertTrue(facade.ativarColuna(0));       // altani: mover, 2 tributos, khan
		assertEquals(1, facade.movimentosRestantes());
		assertTrue(facade.podeMover(0, 7));
		assertFalse(facade.podeMover(0, 0));      // nao adjacente a Karakorum
		assertTrue(facade.mover(0, 7));
		assertEquals(7, facade.posicaoDoPeao(0, 0));
		assertEquals(0, facade.movimentosRestantes());

		assertFalse(facade.encerrarTurno());       // falta usar o Khan
		assertTrue(facade.usarKhan(4));
		assertEquals(4, facade.provinciaDoKhan());
		assertTrue(facade.encerrarTurno());

		assertEquals(1, facade.jogadorDaVez());
		assertTrue(facade.colunaAtivada(0, 0));
	}

	@Test(timeout = TIMEOUT)
	public void acoesSemTurnoOuComIdsInvalidosSaoRecusadas() {
		assertFalse(facade.mover(0, 7));
		assertFalse(facade.pegarTributo(0));
		assertTrue(facade.ativarColuna(1));
		assertFalse(facade.pegarTributo(99));
		assertFalse(facade.mover(5, 7));
		assertFalse(facade.mover(0, 99));
		assertFalse(facade.atacarCidade("Atlantida", "ouro"));
	}

	@Test(timeout = TIMEOUT, expected = IllegalArgumentException.class)
	public void corInexistenteLancaExcecao() {
		facade.novoJogo(Arrays.asList("rosa", "azul"), Arrays.asList("altani", "jochi"));
	}
}
