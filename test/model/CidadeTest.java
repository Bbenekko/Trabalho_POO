package model;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

// Regras de ataque e conquista de cidades (manual, p. 10)
public class CidadeTest {
	private static final int TIMEOUT = 2000;
	private Cidade samarcanda;
	private Jogador atacante;

	@Before
	public void montaCenario() {
		samarcanda = new Cidade("Samarcanda", Regiao.persia);
		atacante = new Jogador(Cor.vermelho, Herdeiro.tolui);
	}

	@Test(timeout = TIMEOUT)
	public void tomarOUltimoTesouroTornaOAtacanteConquistador() {
		samarcanda.revelar();
		samarcanda.receberTesouro(TipoTesouro.pele);
		samarcanda.tomarTesouro(atacante, TipoTesouro.pele);
		assertSame("Quem toma o último tesouro conquista a cidade", atacante, samarcanda.getConquistador());
	}
}
