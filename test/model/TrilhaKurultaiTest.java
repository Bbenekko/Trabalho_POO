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
	public void fimDisparadoCom13VotosDistribuidosEm3Jogadores() {
		trilha.registrarVotos(vermelho, 7);
		trilha.registrarVotos(azul, 5);
		assertFalse("Com 12 votos a ficha neutra ainda não chegou ao marco de 3 jogadores", trilha.fimDisparado());
		trilha.registrarVotos(azul, 1);
		assertTrue("Ficha neutra no marco (casa 13) dispara o fim com 3 jogadores", trilha.fimDisparado());
	}
}