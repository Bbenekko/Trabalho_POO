package model;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

// Roda todos os testes do pacote model de uma vez (teste de regressão)
@RunWith(Suite.class)
@Suite.SuiteClasses({
	CidadeTest.class,
	ColunaAcaoTest.class,
	JogadorTest.class,
	KhanTest.class,
	ParadaTest.class,
	ProvinciaTest.class,
	TabuleiroJogadorTest.class,
	TabuleiroTest.class,
	TrilhaKurultaiTest.class
})
public class ModelTestSuite {}
