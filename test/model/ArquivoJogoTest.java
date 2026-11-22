package model;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ArquivoJogoTest {
	private static final int TIMEOUT = 4000;
	private ModelFacade facade;
	private File arquivo;
	private File copia;

	@Before
	public void setUp() throws IOException {
		this.facade = ModelFacade.getInstance();
		this.facade.novoJogo(Arrays.asList("Ana Lúcia", "Bruno", "Caio"),
				Arrays.asList("vermelho", "azul", "verde"),
				Arrays.asList("altani", "tolui", "jochi"), false);
		for (int j = 0; j < 3; j++)
			assertTrue(this.facade.escolherCartaSecreta(j, j % 2));
		this.arquivo = File.createTempFile("partida", ".txt");
		this.copia = File.createTempFile("partida2", ".txt");
	}

	@After
	public void tearDown() {
		this.arquivo.delete();
		this.copia.delete();
	}

	// joga dois turnos para a partida sair do estado inicial
	private void jogarDoisTurnos() {
		assertTrue(facade.ativarColuna(0));      // altani: mover, 2 tributos, khan
		assertTrue(facade.mover(0, 7));
		assertTrue(facade.usarKhan(4));
		facade.pegarTributo(4);
		assertTrue(facade.encerrarTurno());

		assertTrue(facade.ativarColuna(1));      // tolui: 2 movimentos, 1 tributo
		assertTrue(facade.mover(0, 13));
		facade.pegarTributo(3);
		assertTrue(facade.encerrarTurno());
	}

	@Test(timeout = TIMEOUT)
	public void salvarECarregarRecuperaOMesmoEstado() {
		jogarDoisTurnos();
		String antes = retrato();
		assertTrue(facade.salvarJogo(arquivo.getPath()));

		facade.novoJogo(2);                       // outra partida qualquer
		assertNotEquals(antes, retrato());

		assertTrue(facade.carregarJogo(arquivo.getPath()));
		assertEquals(antes, retrato());
		assertEquals("Ana Lúcia", facade.nomeDoJogador(0));
		assertEquals(2, facade.jogadorDaVez());
	}

	@Test(timeout = TIMEOUT)
	public void partidaCarregadaGravaArquivoIdentico() throws IOException {
		jogarDoisTurnos();
		assertTrue(facade.salvarJogo(arquivo.getPath()));
		assertTrue(facade.carregarJogo(arquivo.getPath()));
		assertTrue(facade.salvarJogo(copia.getPath()));
		// inclui o que não aparece na tela: pilhas de cidades, melhorias e tesouros do estoque
		assertEquals(Files.readAllLines(arquivo.toPath()), Files.readAllLines(copia.toPath()));
	}

	@Test(timeout = TIMEOUT)
	public void partidaCarregadaContinuaJogavel() {
		jogarDoisTurnos();
		assertTrue(facade.salvarJogo(arquivo.getPath()));
		assertTrue(facade.carregarJogo(arquivo.getPath()));
		assertTrue(facade.ativarColuna(0));      // jochi: mover, tributo, moeda virtual
		assertTrue(facade.turnoEmAndamento());
		assertTrue(facade.movimentosRestantes() > 0);
	}

	@Test(timeout = TIMEOUT)
	public void arquivoEhAsciiPuro() throws IOException {
		jogarDoisTurnos();
		assertTrue(facade.salvarJogo(arquivo.getPath()));
		for (byte b : Files.readAllBytes(arquivo.toPath()))
			assertTrue("byte fora do ASCII: " + b, b >= 0 && b < 128);
	}

	@Test(timeout = TIMEOUT)
	public void naoSalvaDepoisQueOJogadorComecou() {
		assertTrue(facade.podeSalvar());
		assertTrue(facade.ativarColuna(0));
		assertFalse(facade.podeSalvar());
		assertFalse(facade.salvarJogo(arquivo.getPath()));
	}

	@Test(timeout = TIMEOUT)
	public void arquivoInvalidoNaoTrocaAPartida() throws IOException {
		String antes = retrato();
		Files.write(arquivo.toPath(), Arrays.asList("isto nao e uma partida"));
		assertFalse(facade.carregarJogo(arquivo.getPath()));
		assertFalse(facade.carregarJogo(new File(arquivo.getPath() + "_inexistente").getPath()));
		assertEquals(antes, retrato());
	}

	@Test(timeout = TIMEOUT)
	public void pecasDeBonusETesourosSobrevivem() throws IOException {
		Jogo jogo = new Jogo(Arrays.asList(Cor.preto, Cor.azul), Arrays.asList(Herdeiro.tolui, Herdeiro.jochi));
		Jogador j = jogo.getJogadores().get(1);
		j.receberPecaBonus(TipoPecaBonus.cavalos);
		j.receberTesouro(TipoTesouro.pele);
		jogo.getTabuleiro().retirarPecaBonus(2);
		ArquivoJogo.gravar(jogo, arquivo.getPath());

		Jogo lido = ArquivoJogo.ler(arquivo.getPath());
		Jogador k = lido.getJogadores().get(1);
		assertEquals(Arrays.asList(TipoPecaBonus.cavalos), k.getPecasBonus());
		assertEquals(1, k.quantidadeTesouro(TipoTesouro.pele));
		assertNull(lido.getTabuleiro().pecaBonusNoEspaco(2));
		assertEquals(jogo.getTabuleiro().pecaBonusNoEspaco(0), lido.getTabuleiro().pecaBonusNoEspaco(0));
	}

	@Test(timeout = TIMEOUT)
	public void nomesComEspacoEAcentoSobrevivem() {
		assertEquals("Ana Lúcia", ArquivoJogo.decodificar(ArquivoJogo.codificar("Ana Lúcia")));
		assertFalse(ArquivoJogo.codificar("Ana Lúcia").contains(" "));
	}

	// tudo o que a fachada mostra da partida, numa string
	private String retrato() {
		StringBuilder sb = new StringBuilder();
		int n = facade.numeroDeJogadores();
		sb.append(n).append('|').append(facade.jogadorDaVez()).append('|').append(facade.fimDisparado())
				.append('|').append(facade.jogoTerminou()).append('\n');
		for (int j = 0; j < n; j++) {
			sb.append(facade.nomeDoJogador(j)).append(facade.corDoJogador(j)).append(facade.herdeiroDoJogador(j));
			for (int p = 0; p < facade.numeroDePeoes(j); p++)
				sb.append(" p").append(facade.posicaoDoPeao(j, p));
			for (String t : new String[] {"moeda", "espada", "yurt"})
				sb.append(' ').append(t).append(facade.tributosDoJogador(j, t));
			for (String t : new String[] {"pele", "tecido", "gado", "ferro", "especiarias"})
				sb.append(' ').append(t).append(facade.tesourosDoJogador(j, t));
			sb.append(" yr").append(facade.yurtsRestantes(j)).append(" v").append(facade.votosDe(j));
			sb.append(" b").append(facade.pecasBonusDoJogador(j));
			sb.append(' ').append(facade.cartaSecreta(j)).append(facade.opcoesSecretas(j));
			for (int c = 0; c < 4; c++)
				sb.append(" c").append(facade.colunaAtivada(j, c)).append(facade.melhoriasDaColuna(j, c));
			sb.append('\n');
		}
		for (int id : facade.idsDasProvincias())
			sb.append(facade.tributosNaProvincia(id)).append(',');
		sb.append('\n');
		List<String> cidades = facade.nomesDasCidades();
		java.util.Collections.sort(cidades);
		for (String c : cidades)
			sb.append(c).append(facade.cidadeRevelada(c)).append(facade.tesourosNaCidade(c))
					.append(facade.conquistadorDaCidade(c)).append(';');
		sb.append('\n');
		for (int id = 0; id <= Tabuleiro.KARAKORUM; id++)
			sb.append(facade.yurtsNaParada(id));
		sb.append('\n').append(facade.provinciaDoKhan()).append(facade.khanNasMelhorias())
				.append(facade.melhoriasExpostas()).append(facade.fichaNeutra()).append(facade.cartasAbertas());
		for (int i = 0; i < facade.cartasAbertas().size(); i++)
			sb.append(facade.cartaAbertaRetirada(i));
		for (int i = 0; i < facade.numeroDeEspacosBonus(); i++)
			sb.append(facade.pecaBonusNoEspaco(i));
		return sb.toString();
	}
}