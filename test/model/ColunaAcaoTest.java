package model;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

// Melhoria comprada não pode ser usada no mesmo turno (manual, p. 12)
public class ColunaAcaoTest {
	private static final int TIMEOUT = 2000;
	private static final int TURNO = 3;
	private ColunaAcao amarela;

	@Before
	public void montaCenario() {
		List<TipoAcao> acoes = new ArrayList<TipoAcao>();
		acoes.add(TipoAcao.mover);
		acoes.add(TipoAcao.pegar_tributo);
		amarela = new ColunaAcao(Regiao.russia, acoes);
	}

	@Test(timeout = TIMEOUT)
	public void cavalosCompradosNoTurnoNaoContamComoMovimento() {
		Melhoria cavalos = new Melhoria(TipoMelhoria.dois_movimentos, Regiao.russia, null);
		cavalos.registrarCompra(TURNO);
		amarela.adicionaMelhoria(cavalos);
		assertEquals("Só o movimento inicial vale no turno da compra", 1, amarela.movimentos(TURNO));
	}
}
