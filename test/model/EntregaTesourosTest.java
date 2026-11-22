package model;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class EntregaTesourosTest {
	private static final int TIMEOUT = 2000;
	private static final int ESPACO_KARAKORUM = 3;
	private Jogo jogo;
	private Jogador primeiro;

	@Before
	public void setUp() {
		this.jogo = new Jogo(Arrays.asList(Cor.vermelho, Cor.azul), Arrays.asList(Herdeiro.chagatai, Herdeiro.ogedei));
		this.primeiro = this.jogo.getJogadores().get(0);
		// peças fixas: em Karakorum (espaço 3) ficam as moedas
		this.jogo.getTabuleiro().restaurarPecasBonus(Arrays.asList(TipoPecaBonus.cavalos, TipoPecaBonus.voto,
				TipoPecaBonus.yurts, TipoPecaBonus.moedas, TipoPecaBonus.espadas, TipoPecaBonus.tesouro,
				TipoPecaBonus.maos));
	}

	@Test(timeout = TIMEOUT)
	public void entregaEmKarakorumDaVotosEPecaDeBonus() {
		primeiro.receberTesouro(TipoTesouro.ferro);
		primeiro.receberTesouro(TipoTesouro.pele);
		primeiro.receberTesouro(TipoTesouro.gado);
		assertTrue(jogo.iniciarTurno(0));
		int moedas = primeiro.quantidadeTributo(TipoTributo.moeda);

		assertTrue(jogo.getTurnoAtual().entregarTesouros(ESPACO_KARAKORUM));
		assertEquals(0, primeiro.totalDeTesouros());
		assertEquals(2, jogo.getTabuleiro().getTrilha().votosDe(primeiro));
		assertEquals(Arrays.asList(TipoPecaBonus.moedas), primeiro.getPecasBonus());
		assertEquals("A peça fica guardada até ser usada", moedas, primeiro.quantidadeTributo(TipoTributo.moeda));
		assertNull(jogo.getTabuleiro().pecaBonusNoEspaco(ESPACO_KARAKORUM));

		assertTrue(jogo.getTurnoAtual().usarPecaBonus(0));
		assertEquals(moedas + 2, primeiro.quantidadeTributo(TipoTributo.moeda));
		assertFalse("Cada peça só pode ser usada uma vez", jogo.getTurnoAtual().usarPecaBonus(0));
		assertEquals("Usada, continua contando para as cartas", 1, primeiro.getPecasBonus().size());
	}

	@Test(timeout = TIMEOUT)
	public void cavalosDaoMovimentosETributoNoTurno() {
		primeiro.receberPecaBonus(TipoPecaBonus.cavalos);
		assertTrue(jogo.iniciarTurno(0));
		int mov = jogo.getTurnoAtual().getMovimentosRestantes();
		int trib = jogo.getTurnoAtual().getTributosRestantes();
		assertTrue(jogo.getTurnoAtual().usarPecaBonus(0));
		assertEquals(mov + 3, jogo.getTurnoAtual().getMovimentosRestantes());
		assertEquals(trib + 1, jogo.getTurnoAtual().getTributosRestantes());
	}

	@Test(timeout = TIMEOUT)
	public void compraDaPilhaVaiParaAColunaDaRegiao() {
		primeiro.receberTributo(TipoTributo.moeda, 2);
		int moedas = primeiro.quantidadeTributo(TipoTributo.moeda);
		AreaMelhorias area = jogo.getTabuleiro().getAreaMelhorias();
		Melhoria topo = area.verTopo();
		int pilha = area.tamanhoDaPilha();
		assertTrue(jogo.iniciarTurno(0));

		assertTrue(jogo.getTurnoAtual().comprarMelhoriaDaPilha(-1));
		assertEquals(moedas - 2, primeiro.quantidadeTributo(TipoTributo.moeda));
		assertEquals(pilha - 1, area.tamanhoDaPilha());
		boolean achou = false;
		for (ColunaAcao c : primeiro.getTabuleiro().getColunas())
			if (c.getMelhorias().contains(topo)) {
				achou = true;
				assertEquals(topo.getRegiao(), c.getRegiao());
			}
		assertTrue(achou);
	}

	@Test(timeout = TIMEOUT)
	public void compraDaPilhaSemMoedasEhRecusada() {
		while (primeiro.quantidadeTributo(TipoTributo.moeda) > 0)
			primeiro.gastar(TipoTributo.moeda, 1);
		int pilha = jogo.getTabuleiro().getAreaMelhorias().tamanhoDaPilha();
		assertTrue(jogo.iniciarTurno(1));                    // chagatai coluna 2: tem moeda virtual
		int virtuais = jogo.getTurnoAtual().getVirtuais(TipoTributo.moeda);
		boolean ok = jogo.getTurnoAtual().comprarMelhoriaDaPilha(-1);
		assertEquals(virtuais >= 2, ok);
		if (!ok)
			assertEquals(pilha, jogo.getTabuleiro().getAreaMelhorias().tamanhoDaPilha());
	}

	@Test(timeout = TIMEOUT)
	public void semOsTesourosOuForaDaParadaARecusa() {
		primeiro.receberTesouro(TipoTesouro.tecido);
		primeiro.receberTesouro(TipoTesouro.tecido);
		assertTrue(jogo.iniciarTurno(0));
		assertFalse(jogo.getTurnoAtual().entregarTesouros(ESPACO_KARAKORUM));  // faltam ferro, pele e gado
		assertFalse(jogo.getTurnoAtual().entregarTesouros(0));                 // tecido: só na parada 3
		assertFalse(jogo.getTurnoAtual().entregarTesouros(99));
		assertEquals(2, primeiro.quantidadeTesouro(TipoTesouro.tecido));
	}

	@Test(timeout = TIMEOUT)
	public void cadaPergaminhoUmaVezPorTurno() {
		for (int i = 0; i < 2; i++) {
			primeiro.receberTesouro(TipoTesouro.ferro);
			primeiro.receberTesouro(TipoTesouro.pele);
			primeiro.receberTesouro(TipoTesouro.gado);
		}
		assertTrue(jogo.iniciarTurno(0));
		assertTrue(jogo.getTurnoAtual().entregarTesouros(ESPACO_KARAKORUM));
		assertFalse(jogo.getTurnoAtual().entregarTesouros(ESPACO_KARAKORUM));
		assertEquals(3, primeiro.totalDeTesouros());
	}

	@Test(timeout = TIMEOUT)
	public void pergaminhoDosBausUsaOsTesourosMaisAbundantes() {
		primeiro.receberTesouro(TipoTesouro.gado);
		primeiro.receberTesouro(TipoTesouro.gado);
		primeiro.receberTesouro(TipoTesouro.pele);
		EntregaTesouros baus = jogo.getTabuleiro().getEntrega(6);
		List<TipoTesouro> lista = baus.tesourosAEntregar(primeiro);
		assertEquals(3, lista.size());
		assertEquals(2, java.util.Collections.frequency(lista, TipoTesouro.gado));
		primeiro.receberTesouro(TipoTesouro.ferro);
		assertNull(jogo.getTabuleiro().getEntrega(0).tesourosAEntregar(primeiro));   // sem tecido
	}

	@Test(timeout = TIMEOUT)
	public void pecaDeBonusContaParaACartaAberta() {
		primeiro.receberPecaBonus(TipoPecaBonus.moedas);
		primeiro.receberPecaBonus(TipoPecaBonus.espadas);
		CartaConselheiroAberto carta = new CartaConselheiroAberto(TipoConselheiroAberto.duas_pecas_bonus);
		assertEquals(2, carta.progresso(primeiro, jogo.getTabuleiro()));
		assertTrue(carta.verificar(primeiro, jogo.getTabuleiro()));
	}
}