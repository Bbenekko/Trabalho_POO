package model;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class TrilhaKurultaiTest {
	private static final int TIMEOUT = 2000;
	private TrilhaKurultai trilha;
	private Jogador vermelho, azul;

	@Before
	public void montaCenario() {
		trilha = new TrilhaKurultai(3);
		vermelho = new Jogador(Cor.vermelho, Herdeiro.tolui);
		azul = new Jogador(Cor.azul, Herdeiro.ogedei);
	}

	@Test(timeout = TIMEOUT)
	public void fimDisparadoCom14VotosDistribuidosEm3Jogadores() {
		trilha.registrarVotos(vermelho, 7);
		trilha.registrarVotos(azul, 7);
		assertTrue("Ficha neutra em 14 dispara o fim com 3 jogadores", trilha.fimDisparado());
	}
}
