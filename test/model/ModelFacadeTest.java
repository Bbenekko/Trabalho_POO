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

	@Test(timeout = TIMEOUT)
	public void rodadaAvancaQuandoTodosJogam() {
		assertEquals(1, facade.rodadaAtual());
		assertTrue(facade.ativarColuna(1));        // altani: sem Khan
		assertEquals(1, facade.rodadaAtual());
		assertTrue(facade.encerrarTurno());
		assertEquals(1, facade.rodadaAtual());     // falta o 2º jogador
		assertTrue(facade.ativarColuna(1));        // chagatai: coluna 2 sem Khan
		assertTrue(facade.encerrarTurno());
		assertEquals(2, facade.rodadaAtual());
		assertEquals(facade.limiteKurultai() - facade.fichaNeutra(), facade.votosParaOFim());
	}

	@Test(timeout = TIMEOUT)
	public void altaniUsaSoUmPeaoPorTurno() {
		assertTrue(facade.ativarColuna(1));        // altani: 2 movimentos e 1 tributo
		assertTrue(facade.precisaEscolherPeao());
		assertEquals(-1, facade.peaoDoTurno());
		assertFalse("Sem escolher o peão, não há ação com peão", facade.pegarTributo(4));

		assertTrue(facade.escolherPeao(1));
		assertFalse(facade.precisaEscolherPeao());
		assertEquals(1, facade.peaoDoTurno());
		assertFalse("A escolha não muda no meio do turno", facade.escolherPeao(0));
		assertFalse("O outro peão não pode se mover", facade.mover(0, 7));
		assertTrue(facade.mover(1, 7));
		assertEquals(7, facade.posicaoDoPeao(0, 1));
		assertEquals(Tabuleiro.KARAKORUM, facade.posicaoDoPeao(0, 0));
		assertTrue(facade.encerrarTurno());
	}

	@Test(timeout = TIMEOUT)
	public void moverEscolheOPeaoDeAltaniEAEscolhaVaiAteOFimDoTurno() {
		assertTrue(facade.ativarColuna(1));
		assertTrue(facade.mover(0, 7));            // primeira ação: o peão 0 passa a ser o do turno
		assertEquals(0, facade.peaoDoTurno());
		assertFalse(facade.mover(1, 13));
		assertTrue(facade.encerrarTurno());
		assertTrue(facade.ativarColuna(1));        // chagatai (1 peão): nada a escolher
		assertFalse(facade.precisaEscolherPeao());
	}
}