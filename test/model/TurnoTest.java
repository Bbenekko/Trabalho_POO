package model;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

// Ações do turno: mover, pegar tributo e atacar cidades (manual, p. 7, 8 e 10)
public class TurnoTest {
	private static final int TIMEOUT = 2000;
	private static final int NUMERO_DO_TURNO = 1;
	private Tabuleiro tabuleiro;
	private Jogador jogador;
	private Peao peao;

	@Before
	public void montaCenario() {
		tabuleiro = new Tabuleiro(3);
		jogador = new Jogador(Cor.vermelho, Herdeiro.tolui);
		peao = jogador.getPeoes().get(0);
	}

	private Turno turnoNaParada(int idParada, TipoAcao... acoes) {
		peao.moverPara(tabuleiro.getParada(idParada));
		List<TipoAcao> lista = new ArrayList<TipoAcao>(Arrays.asList(acoes));
		return new Turno(NUMERO_DO_TURNO, jogador, new ColunaAcao(Regiao.russia, lista), tabuleiro);
	}

	private int paradaAoLadoDe(Cidade cidade) {
		for (int id = 0; id <= Tabuleiro.KARAKORUM; id++) {
			if (tabuleiro.getParada(id).getCidades().contains(cidade))
				return id;
		}
		throw new IllegalStateException("Cidade sem parada adjacente: " + cidade.getNome());
	}

	@Test(timeout = TIMEOUT)
	public void pularYurtProprioNaoGastaMovimento() {
		tabuleiro.getParada(1).construirYurt(jogador);
		Turno turno = turnoNaParada(0, TipoAcao.mover);
		List<Parada> caminho = Arrays.asList(tabuleiro.getParada(1), tabuleiro.getParada(2));
		assertTrue("Com 1 movimento, vai de 0 a 2 pulando o próprio yurt em 1", turno.mover(peao, caminho));
	}

	@Test(timeout = TIMEOUT)
	public void naoPegaTributoDeProvinciaLongeDasParadasVisitadas() {
		// a parada 0 não é adjacente a nenhuma província; a província 0 começa com 1 tributo
		Turno turno = turnoNaParada(0, TipoAcao.pegar_tributo);
		assertFalse("Província 0 não é adjacente à parada 0",
				turno.pegarTributo(tabuleiro.getProvincia(0)));
	}

	@Test(timeout = TIMEOUT)
	public void peaoDeAltaniQueNaoAgeNaoServeDeBaseParaPegarTributo() {
		// peão que age na parada 0 (sem províncias); o outro na parada 1, vizinha da província 0
		Jogador altani = new Jogador(Cor.azul, Herdeiro.altani);
		altani.getPeoes().get(0).moverPara(tabuleiro.getParada(0));
		altani.getPeoes().get(1).moverPara(tabuleiro.getParada(1));
		List<TipoAcao> acoes = new ArrayList<TipoAcao>(Arrays.asList(TipoAcao.pegar_tributo));
		Turno turno = new Turno(NUMERO_DO_TURNO, altani, new ColunaAcao(Regiao.russia, acoes), tabuleiro);
		assertFalse("Só as paradas do peão que age valem para pegar tributo (p. 20)",
				turno.pegarTributo(tabuleiro.getProvincia(0)));
	}

	@Test(timeout = TIMEOUT)
	public void doisAtaquesNaMesmaCidadeCustamTresEspadas() {
		Cidade cidade = tabuleiro.cidadesReveladas().get(0);
		Turno turno = turnoNaParada(paradaAoLadoDe(cidade));
		jogador.receberTributo(TipoTributo.espada, 3);
		turno.atacar(cidade, cidade.getTesouros().get(0));
		turno.atacar(cidade, cidade.getTesouros().get(0));
		assertEquals("1º tesouro custa 1 espada e o 2º custa 2", 0,
				jogador.quantidadeTributo(TipoTributo.espada));
	}
}
