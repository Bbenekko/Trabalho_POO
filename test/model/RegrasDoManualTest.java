package model;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

// Regras do manual que faltavam: influência nas regiões, desempate, melhoria de tesouro virtual,
// cartas de conselheiro aberto a qualquer momento do turno, baús e melhoria grátis da pilha.
public class RegrasDoManualTest {
	private static final int TIMEOUT = 2000;
	private static final int ESPACO_PELES = 2;          // pergaminho de 2 peles, na parada dupla 11
	private static final int PARADA_PELES = 11;

	private Jogo tresJogadores() {
		return new Jogo(Arrays.asList(Cor.vermelho, Cor.azul, Cor.amarelo),
				Arrays.asList(Herdeiro.chagatai, Herdeiro.ogedei, Herdeiro.tolui));
	}

	private Jogo doisJogadores() {
		return new Jogo(Arrays.asList(Cor.vermelho, Cor.azul), Arrays.asList(Herdeiro.chagatai, Herdeiro.ogedei));
	}

	private void melhorias(Jogador j, int coluna, Regiao r, int quantidade) {
		for (int i = 0; i < quantidade; i++)
			assertTrue(j.getTabuleiro().adicionarMelhoria(new Melhoria(TipoMelhoria.dois_movimentos, r, null), coluna));
	}

	// ---------- influência ----------

	@Test(timeout = TIMEOUT)
	public void maisYurtsESegundoMaisRecebemVotosPelaTabela() {
		Jogo jogo = tresJogadores();
		Tabuleiro t = jogo.getTabuleiro();
		Jogador a = jogo.getJogadores().get(0), b = jogo.getJogadores().get(1), c = jogo.getJogadores().get(2);
		t.getParada(0).construirYurt(a);
		t.getParada(1).construirYurt(a);
		t.getParada(2).construirYurt(b);
		melhorias(a, 2, Regiao.russia, 3);     // coluna 2 = Rússia
		melhorias(b, 2, Regiao.russia, 1);

		assertEquals("1º com 3 melhorias: 2 votos", 2, jogo.votosDeInfluencia(a, Regiao.russia));
		assertEquals("2º com 1 melhoria: 1 voto", 1, jogo.votosDeInfluencia(b, Regiao.russia));
		assertEquals("Sem yurts na região, nada", 0, jogo.votosDeInfluencia(c, Regiao.russia));
		assertEquals(2, jogo.votosDeInfluencia(a));
	}

	@Test(timeout = TIMEOUT)
	public void empateEmPrimeiroNinguemFicaEmSegundo() {
		Jogo jogo = tresJogadores();
		Tabuleiro t = jogo.getTabuleiro();
		Jogador a = jogo.getJogadores().get(0), b = jogo.getJogadores().get(1), c = jogo.getJogadores().get(2);
		t.getParada(12).construirYurt(a);
		t.getParada(13).construirYurt(a);
		t.getParada(14).construirYurt(b);
		t.getParada(15).construirYurt(b);
		t.getParada(16).construirYurt(c);
		melhorias(c, 1, Regiao.persia, 4);

		assertEquals("Empatados em 1º, 0 melhorias: 1 voto cada", 1, jogo.votosDeInfluencia(a, Regiao.persia));
		assertEquals(1, jogo.votosDeInfluencia(b, Regiao.persia));
		assertEquals("Com empate em 1º ninguém fica em 2º", 0, jogo.votosDeInfluencia(c, Regiao.persia));
	}

	@Test(timeout = TIMEOUT)
	public void empateEmSegundoTodosFicamEmSegundo() {
		Jogo jogo = tresJogadores();
		Tabuleiro t = jogo.getTabuleiro();
		Jogador a = jogo.getJogadores().get(0), b = jogo.getJogadores().get(1), c = jogo.getJogadores().get(2);
		t.getParada(22).construirYurt(a);
		t.getParada(23).construirYurt(a);
		t.getParada(24).construirYurt(b);
		t.getParada(25).construirYurt(c);
		melhorias(b, 0, Regiao.china, 3);      // coluna 0 = China
		melhorias(c, 0, Regiao.china, 2);
		assertEquals(2, jogo.votosDeInfluencia(b, Regiao.china));
		assertEquals(1, jogo.votosDeInfluencia(c, Regiao.china));
	}

	@Test(timeout = TIMEOUT)
	public void cidadeConquistadaContaEYurtDeKarakorumNao() {
		Jogo jogo = tresJogadores();
		Jogador a = jogo.getJogadores().get(0), tolui = jogo.getJogadores().get(2);
		jogo.getTabuleiro().getCidade("Kiev").restaurar(true, Arrays.<TipoTesouro>asList(), a);
		assertEquals("O yurt no meio da cidade conquistada conta para a Rússia", 1, jogo.votosDeInfluencia(a, Regiao.russia));
		assertEquals("O yurt inicial de Tolui em Karakorum não dá influência", 0, jogo.votosDeInfluencia(tolui));
	}

	@Test(timeout = TIMEOUT)
	public void pontuacaoFinalSomaTrilhaInfluenciaECartaSecreta() {
		Jogo jogo = doisJogadores();
		Jogador a = jogo.getJogadores().get(0);
		jogo.getTabuleiro().getTrilha().registrarVotos(a, 3);
		jogo.getTabuleiro().getParada(0).construirYurt(a);
		assertEquals(3 + 1, jogo.pontuacaoFinal(a));
	}

	// ---------- desempate ----------

	@Test(timeout = TIMEOUT)
	public void empateDecididoPorTesourosNaoEntregues() {
		Jogo jogo = doisJogadores();
		Jogador a = jogo.getJogadores().get(0), b = jogo.getJogadores().get(1);
		jogo.getTabuleiro().getTrilha().registrarVotos(a, 2);
		jogo.getTabuleiro().getTrilha().registrarVotos(b, 2);
		b.receberTesouro(TipoTesouro.gado);
		assertEquals(Arrays.asList(b), jogo.vencedores());
	}

	@Test(timeout = TIMEOUT)
	public void depoisVenceQuemEstaMaisPertoDeKarakorum() {
		Jogo jogo = doisJogadores();
		Jogador a = jogo.getJogadores().get(0), b = jogo.getJogadores().get(1);
		assertEquals("Mesma pontuação, tesouros e posição: vitória compartilhada", 2, jogo.vencedores().size());
		b.getPeoes().get(0).moverPara(jogo.getTabuleiro().getParada(0));
		assertTrue(jogo.distanciaAKarakorum(b) > 0);
		assertEquals(0, jogo.distanciaAKarakorum(a));
		assertEquals(Arrays.asList(a), jogo.vencedores());
	}

	// ---------- melhoria de tesouro virtual ----------

	@Test(timeout = TIMEOUT)
	public void tesouroVirtualPermiteEntregarComUmTesouroAMenos() {
		Jogo jogo = doisJogadores();
		Jogador a = jogo.getJogadores().get(0);
		a.getTabuleiro().adicionarMelhoria(new Melhoria(TipoMelhoria.tesouro_virtual, Regiao.persia, TipoTesouro.pele), 1);
		a.receberTesouro(TipoTesouro.pele);
		a.getPeoes().get(0).moverPara(jogo.getTabuleiro().getParada(PARADA_PELES));

		assertTrue(jogo.iniciarTurno(1));                 // coluna da Pérsia, com a melhoria
		assertEquals(Arrays.asList(TipoTesouro.pele), jogo.getTurnoAtual().getTesourosVirtuais());
		assertTrue(jogo.getTurnoAtual().entregarTesouros(ESPACO_PELES));
		assertEquals("Só 1 pele real foi entregue", 0, a.quantidadeTesouro(TipoTesouro.pele));
		assertEquals(1, jogo.getTabuleiro().getTrilha().votosDe(a));
		assertTrue("O virtual foi gasto", jogo.getTurnoAtual().getTesourosVirtuais().isEmpty());
	}

	@Test(timeout = TIMEOUT)
	public void tesouroVirtualSoValeNaColunaAtivada() {
		Jogo jogo = doisJogadores();
		Jogador a = jogo.getJogadores().get(0);
		a.getTabuleiro().adicionarMelhoria(new Melhoria(TipoMelhoria.tesouro_virtual, Regiao.persia, TipoTesouro.pele), 1);
		a.receberTesouro(TipoTesouro.pele);
		a.getPeoes().get(0).moverPara(jogo.getTabuleiro().getParada(PARADA_PELES));
		assertTrue(jogo.iniciarTurno(0));
		assertFalse(jogo.getTurnoAtual().entregarTesouros(ESPACO_PELES));
	}

	// ---------- cartas de conselheiro aberto a qualquer momento ----------

	@Test(timeout = TIMEOUT)
	public void cartaAbertaValeNoMeioDoTurnoMesmoSemManterAsEspadas() {
		Jogo jogo = doisJogadores();
		Jogador a = jogo.getJogadores().get(0);
		CartaConselheiroAberto carta = new CartaConselheiroAberto(TipoConselheiroAberto.quatro_espadas);
		jogo.getTabuleiro().restaurarCartasAbertas(Arrays.asList(carta));
		a.receberTributo(TipoTributo.espada, 2);
		a.receberPecaBonus(TipoPecaBonus.espadas);

		assertTrue(jogo.iniciarTurno(1));                 // coluna sem Khan, para poder encerrar
		assertFalse(carta.isRetirada());
		assertTrue(jogo.getTurnoAtual().usarPecaBonus(0));
		assertTrue("Com 4 espadas o voto vem na hora", carta.isRetirada());
		assertEquals(1, jogo.getTabuleiro().getTrilha().votosDe(a));
		a.gastar(TipoTributo.espada, 4);
		assertTrue(jogo.encerrarTurno());
		assertEquals("Gastar depois não tira o voto", 1, jogo.getTabuleiro().getTrilha().votosDe(a));
	}

	@Test(timeout = TIMEOUT)
	public void tesouroVirtualContaParaCartaDeQuatroTesouros() {
		Jogo jogo = doisJogadores();
		Jogador a = jogo.getJogadores().get(0);
		CartaConselheiroAberto carta = new CartaConselheiroAberto(TipoConselheiroAberto.quatro_tesouros);
		jogo.getTabuleiro().restaurarCartasAbertas(Arrays.asList(carta));
		a.getTabuleiro().adicionarMelhoria(new Melhoria(TipoMelhoria.tesouro_virtual, Regiao.china, TipoTesouro.ferro), 0);
		for (int i = 0; i < 3; i++)
			a.receberTesouro(TipoTesouro.gado);
		assertTrue(jogo.iniciarTurno(0));
		assertTrue(carta.isRetirada());
	}

	// ---------- baús ----------

	@Test(timeout = TIMEOUT)
	public void bausTiramTresTesourosEOJogadorEscolheUm() {
		Jogo jogo = doisJogadores();
		Jogador a = jogo.getJogadores().get(0);
		Estoque estoque = jogo.getTabuleiro().getEstoque();
		a.receberPecaBonus(TipoPecaBonus.tesouro);
		int naPilha = estoque.getTesouros().size();

		assertTrue(jogo.iniciarTurno(1));
		Turno turno = jogo.getTurnoAtual();
		assertTrue(turno.usarPecaBonus(0));
		List<TipoTesouro> opcoes = turno.getTesourosDoBau();
		assertEquals(3, opcoes.size());
		assertEquals(naPilha - 3, estoque.getTesouros().size());
		assertFalse("Precisa escolher antes de encerrar", turno.podeEncerrar());

		TipoTesouro escolhido = opcoes.get(1);
		assertTrue(turno.escolherTesouroDoBau(1));
		assertEquals(1, a.totalDeTesouros());
		assertEquals(1, a.quantidadeTesouro(escolhido));
		assertEquals("Os outros 2 são descartados", naPilha - 3, estoque.getTesouros().size());
		assertTrue(turno.getTesourosDoBau().isEmpty());
		assertTrue(turno.podeEncerrar());
	}

	// ---------- melhoria grátis do topo da pilha ----------

	@Test(timeout = TIMEOUT)
	public void melhoriaGratisPodeVirDoTopoDaPilha() {
		Jogo jogo = doisJogadores();
		Jogador a = jogo.getJogadores().get(0);
		melhorias(a, 0, Regiao.china, 1);
		melhorias(a, 1, Regiao.persia, 1);
		melhorias(a, 2, Regiao.russia, 1);
		AreaMelhorias area = jogo.getTabuleiro().getAreaMelhorias();
		Melhoria topo = area.verTopo();
		int pilha = area.tamanhoDaPilha();

		assertTrue(jogo.iniciarTurno(1));
		assertTrue(jogo.getTurnoAtual().melhoriaGratisDisponivel());
		assertTrue(jogo.getTurnoAtual().pegarMelhoriaGratisDaPilha());
		assertEquals(pilha - 1, area.tamanhoDaPilha());
		assertEquals(Arrays.asList(topo), a.getTabuleiro().getColuna(3).getMelhorias());
		assertFalse("Só uma por linha completa", jogo.getTurnoAtual().pegarMelhoriaGratisDaPilha());
	}
}
