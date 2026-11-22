package model;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Test;

// habilidades escritas nas cartelas dos herdeiros
public class HerdeirosTest {
	private static final int TIMEOUT = 2000;
	private static final int VERMELHA = 0, VERDE = 1, AMARELA = 2;   // China, Pérsia, Rússia

	private Jogo jogo(Herdeiro primeiro) {
		Herdeiro outro = (primeiro == Herdeiro.tolui) ? Herdeiro.altani : Herdeiro.tolui;
		return new Jogo(Arrays.asList(Cor.vermelho, Cor.azul), Arrays.asList(primeiro, outro));
	}

	// põe na área exposta, na posição 0, uma melhoria da região pedida
	private void exporNaFrente(Jogo jogo, Regiao regiao) {
		AreaMelhorias area = jogo.getTabuleiro().getAreaMelhorias();
		java.util.List<Melhoria> expostas = new java.util.ArrayList<Melhoria>(area.getExpostas());
		expostas.set(0, new Melhoria(TipoMelhoria.pegar_tributo, regiao, null));
		area.restaurar(expostas, area.getPilha());
	}

	@Test(timeout = TIMEOUT)
	public void jochiPoeMelhoriaDeQualquerCorEmQualquerColuna() {
		Jogo jogo = jogo(Herdeiro.jochi);
		Jogador jochi = jogo.getJogadores().get(0);
		jochi.receberTributo(TipoTributo.moeda, 2);
		exporNaFrente(jogo, Regiao.russia);
		assertTrue(jogo.iniciarTurno(0));
		assertTrue("Melhoria amarela na coluna vermelha", jogo.getTurnoAtual().comprarMelhoria(0, VERMELHA));
		assertEquals(Regiao.russia, jochi.getTabuleiro().getColuna(VERMELHA).getMelhorias().get(0).getRegiao());
	}

	@Test(timeout = TIMEOUT)
	public void outrosHerdeirosSeguemACorDaColuna() {
		Jogo jogo = jogo(Herdeiro.ogedei);
		Jogador ogedei = jogo.getJogadores().get(0);
		ogedei.receberTributo(TipoTributo.moeda, 2);
		exporNaFrente(jogo, Regiao.russia);
		assertTrue(jogo.iniciarTurno(0));
		assertFalse(jogo.getTurnoAtual().comprarMelhoria(0, VERMELHA));
		assertTrue(jogo.getTurnoAtual().comprarMelhoria(0, AMARELA));
	}

	@Test(timeout = TIMEOUT)
	public void jochiEscolheAColunaDaMelhoriaDaPilha() {
		Jogo jogo = jogo(Herdeiro.jochi);
		Jogador jochi = jogo.getJogadores().get(0);
		jochi.receberTributo(TipoTributo.moeda, 2);
		Melhoria topo = jogo.getTabuleiro().getAreaMelhorias().verTopo();
		assertTrue(jogo.iniciarTurno(0));
		assertTrue(jogo.getTurnoAtual().comprarMelhoriaDaPilha(VERDE));
		assertTrue(jochi.getTabuleiro().getColuna(VERDE).getMelhorias().contains(topo));
	}

	@Test(timeout = TIMEOUT)
	public void chagataiTrocaUmMovimentoPorUmaEspadaUmaVezPorTurno() {
		Jogo jogo = jogo(Herdeiro.chagatai);
		Jogador chagatai = jogo.getJogadores().get(0);
		assertTrue(jogo.iniciarTurno(2));                       // 2 movimentos
		Turno turno = jogo.getTurnoAtual();
		int mov = turno.getMovimentosRestantes();
		assertTrue(turno.trocarMovimentoPorEspada());
		assertEquals(mov - 1, turno.getMovimentosRestantes());
		assertEquals(1, chagatai.quantidadeTributo(TipoTributo.espada));
		assertFalse("Só 1 vez por turno", turno.trocarMovimentoPorEspada());
	}

	@Test(timeout = TIMEOUT)
	public void soChagataiTrocaMovimentoPorEspada() {
		Jogo jogo = jogo(Herdeiro.ogedei);
		assertTrue(jogo.iniciarTurno(0));
		assertFalse(jogo.getTurnoAtual().trocarMovimentoPorEspada());
	}

	@Test(timeout = TIMEOUT)
	public void ogedeiCompraTesouroDeQualquerCidadeMasNaoOUltimo() {
		Jogo jogo = jogo(Herdeiro.ogedei);
		Jogador ogedei = jogo.getJogadores().get(0);
		ogedei.receberTributo(TipoTributo.moeda, 1);
		int moedas = ogedei.quantidadeTributo(TipoTributo.moeda);
		Cidade cidade = jogo.getTabuleiro().cidadesReveladas().get(0);
		TipoTesouro t = cidade.getTesouros().get(0);
		int antes = cidade.getTesouros().size();
		assertTrue(jogo.iniciarTurno(0));

		assertTrue(jogo.getTurnoAtual().comprarTesouroDeOgedei(cidade, t));
		assertEquals(moedas - 1, ogedei.quantidadeTributo(TipoTributo.moeda));
		assertEquals(1, ogedei.quantidadeTesouro(t));
		assertEquals(antes - 1, cidade.getTesouros().size());
		assertFalse("Só 1 vez por turno", jogo.getTurnoAtual().comprarTesouroDeOgedei(cidade, cidade.getTesouros().get(0)));
	}

	@Test(timeout = TIMEOUT)
	public void ogedeiNaoLevaOUltimoTesouro() {
		Jogo jogo = jogo(Herdeiro.ogedei);
		Jogador ogedei = jogo.getJogadores().get(0);
		ogedei.receberTributo(TipoTributo.moeda, 3);
		Cidade cidade = jogo.getTabuleiro().cidadesReveladas().get(0);
		TipoTesouro ultimo = cidade.getTesouros().get(0);
		cidade.restaurar(true, Arrays.asList(ultimo), null);
		assertTrue(jogo.iniciarTurno(0));
		assertFalse(jogo.getTurnoAtual().comprarTesouroDeOgedei(cidade, ultimo));
	}
}