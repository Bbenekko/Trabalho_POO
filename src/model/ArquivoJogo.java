package model;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;

// Salvamento e recuperação de uma partida em arquivo texto (Cap. 16).
// Gravação: PrintWriter sobre BufferedWriter/FileWriter, uma informação por linha.
// Leitura: Scanner sobre BufferedReader/FileReader, que separa o arquivo em tokens.
// O arquivo é ASCII puro: só nomes de enums, números, true/false e os nomes dos
// jogadores codificados (espaços e acentos viram barra-u seguida de 4 hexadecimais).
// A partida só é salva entre dois turnos, então não há Turno em andamento para gravar.
class ArquivoJogo {
	private static final String CABECALHO = "HERDEIROS_DE_KHAN";
	private static final int VERSAO = 1;
	private static final String NULO = "-";
	private static final String USADA = "@usada";
	private static final String GUARDADA = "@guardada";

	private ArquivoJogo() {
	}

	// ===================== GRAVAÇÃO =====================

	static void gravar(Jogo jogo, String caminho) throws IOException {
		if (jogo.getTurnoAtual() != null)
			throw new IllegalStateException("Só é possível salvar antes de um jogador começar a jogar");
		PrintWriter out = null;
		try {
			out = new PrintWriter(new BufferedWriter(new FileWriter(caminho)));
			escrever(jogo, out);
			if (out.checkError())
				throw new IOException("Erro ao gravar " + caminho);
		} finally {
			if (out != null)
				out.close();
		}
	}

	private static void escrever(Jogo jogo, PrintWriter out) {
		List<Jogador> jogadores = jogo.getJogadores();
		Tabuleiro t = jogo.getTabuleiro();

		out.println(CABECALHO + " " + VERSAO);

		// quem joga, na ordem de jogo
		out.println("JOGADORES " + jogadores.size());
		for (Jogador j : jogadores)
			out.println("JOGADOR " + codificar(j.getNome()) + " " + j.getCor().name() + " " + j.getHerdeiro().name());

		out.println("PARTIDA " + jogo.getIndiceAtual() + " " + jogo.getNumeroDoTurno() + " "
				+ jogo.isFimDisparado() + " " + jogo.isUltimaRodada() + " " + jogo.encerrado());

		// cada jogador
		for (int i = 0; i < jogadores.size(); i++) {
			Jogador j = jogadores.get(i);
			out.println("DADOS_JOGADOR " + i);

			out.print("TRIBUTOS " + TipoTributo.values().length);
			for (TipoTributo tt : TipoTributo.values())
				out.print(" " + tt.name() + " " + j.quantidadeTributo(tt));
			out.println();

			out.print("TESOUROS " + TipoTesouro.values().length);
			for (TipoTesouro tt : TipoTesouro.values())
				out.print(" " + tt.name() + " " + j.quantidadeTesouro(tt));
			out.println();

			out.println("YURTS_RESTANTES " + j.getYurtsRestantes());

			out.print("PEOES " + j.getPeoes().size());
			for (Peao p : j.getPeoes())
				out.print(" " + p.getPosicao().getId());
			out.println();

			List<CartaConselheiroSecreto> opcoes = jogo.opcoesSecretas(j);
			out.print("CARTAS_SECRETAS " + opcoes.size());
			for (CartaConselheiroSecreto c : opcoes)
				out.print(" " + c.getTipo().name());
			out.println(" " + opcoes.indexOf(j.getCartaSecreta()));

			out.println("VOTOS " + t.getTrilha().votosDe(j));

			out.print("PECAS_BONUS " + j.getPecasBonus().size());
			for (int b = 0; b < j.getPecasBonus().size(); b++)
				out.print(" " + j.getPecasBonus().get(b).name() + (j.bonusUsado(b) ? USADA : GUARDADA));
			out.println();

			List<ColunaAcao> colunas = j.getTabuleiro().getColunas();
			for (int c = 0; c < colunas.size(); c++) {
				ColunaAcao col = colunas.get(c);
				out.println("COLUNA " + c + " " + col.isAtivada() + " " + col.getMelhorias().size());
				for (Melhoria m : col.getMelhorias())
					escreverMelhoria(out, m);
			}
		}

		// tabuleiro central
		out.println("TRILHA " + t.getTrilha().getFichaNeutra());

		Estoque e = t.getEstoque();
		out.print("ESTOQUE_TRIBUTOS " + TipoTributo.values().length);
		for (TipoTributo tt : TipoTributo.values())
			out.print(" " + tt.name() + " " + e.quantidadeTributo(tt));
		out.println();
		out.print("ESTOQUE_TESOUROS " + e.getTesouros().size());
		for (TipoTesouro tt : e.getTesouros())
			out.print(" " + tt.name());
		out.println();

		out.print("PROVINCIAS " + t.getProvincias().size());
		for (int id = 0; id < t.getProvincias().size(); id++)
			out.print(" " + id + " " + t.getProvincia(id).getQuantidade());
		out.println();

		out.println("CIDADES " + t.getCidades().size());
		for (Cidade c : t.getCidades()) {
			Jogador dono = c.getConquistador();
			out.print("CIDADE " + c.getNome() + " " + c.isRevelada() + " "
					+ (dono == null ? -1 : jogadores.indexOf(dono)) + " " + c.getTesouros().size());
			for (TipoTesouro tt : c.getTesouros())
				out.print(" " + tt.name());
			out.println();
		}

		out.print("PILHA_CIDADES " + t.getPilhaCidades().size());
		for (Cidade c : t.getPilhaCidades())
			out.print(" " + c.getNome());
		out.println();

		Khan k = t.getKhan();
		out.println("KHAN " + (k.getProvincia() == null ? -1 : k.getProvincia().getId()) + " " + k.isNaAreaDeMelhorias());

		AreaMelhorias area = t.getAreaMelhorias();
		out.println("MELHORIAS_EXPOSTAS " + area.getExpostas().size());
		for (Melhoria m : area.getExpostas())
			escreverMelhoria(out, m);
		out.println("MELHORIAS_PILHA " + area.getPilha().size());
		for (Melhoria m : area.getPilha())
			escreverMelhoria(out, m);

		out.print("CARTAS_ABERTAS " + t.getCartasAbertas().size());
		for (CartaConselheiroAberto c : t.getCartasAbertas())
			out.print(" " + c.getTipo().name() + " " + c.isRetirada());
		out.println();

		out.print("BONUS " + t.numeroDeEspacosBonus());
		for (int i = 0; i < t.numeroDeEspacosBonus(); i++) {
			TipoPecaBonus b = t.pecaBonusNoEspaco(i);
			out.print(" " + (b == null ? NULO : b.name()));
		}
		out.println();

		out.println("PARADAS " + t.getParadas().size());
		for (int id = 0; id < t.getParadas().size(); id++) {
			List<Jogador> yurts = t.getParada(id).getYurts();
			out.print("YURTS " + id + " " + yurts.size());
			for (Jogador dono : yurts)
				out.print(" " + jogadores.indexOf(dono));
			out.println();
		}

		out.println("FIM");
	}

	private static void escreverMelhoria(PrintWriter out, Melhoria m) {
		out.println("MELHORIA " + m.getTipo().name() + " " + m.getRegiao().name() + " "
				+ (m.getTesouro() == null ? NULO : m.getTesouro().name()) + " " + m.getTurnoDaCompra());
	}

	// ===================== LEITURA =====================

	static Jogo ler(String caminho) throws IOException {
		Scanner s = null;
		try {
			s = new Scanner(new BufferedReader(new FileReader(caminho)));
			return montar(s);
		} catch (NoSuchElementException | IllegalArgumentException | IndexOutOfBoundsException e) {
			// fim inesperado, token de tipo errado (InputMismatchException) ou valor inválido
			throw new IOException("Arquivo de partida inválido: " + e.getMessage(), e);
		} finally {
			if (s != null)
				s.close();
		}
	}

	private static Jogo montar(Scanner s) {
		esperar(s, CABECALHO);
		int versao = s.nextInt();
		if (versao != VERSAO)
			throw new IllegalArgumentException("versão de arquivo desconhecida " + versao);

		// recria a partida com os mesmos jogadores; depois todo o estado é sobrescrito com o do arquivo
		esperar(s, "JOGADORES");
		int n = s.nextInt();
		List<String> nomes = new ArrayList<String>();
		List<Cor> cores = new ArrayList<Cor>();
		List<Herdeiro> herdeiros = new ArrayList<Herdeiro>();
		for (int i = 0; i < n; i++) {
			esperar(s, "JOGADOR");
			nomes.add(decodificar(s.next()));
			cores.add(Cor.valueOf(s.next()));
			herdeiros.add(Herdeiro.valueOf(s.next()));
		}
		Jogo jogo = new Jogo(cores, herdeiros, nomes);
		List<Jogador> jogadores = jogo.getJogadores();
		Tabuleiro t = jogo.getTabuleiro();

		esperar(s, "PARTIDA");
		int indiceAtual = s.nextInt();
		int numeroDoTurno = s.nextInt();
		boolean fimDisparado = s.nextBoolean();
		boolean ultimaRodada = s.nextBoolean();
		boolean encerrado = s.nextBoolean();
		jogo.restaurar(indiceAtual, numeroDoTurno, fimDisparado, ultimaRodada, encerrado);

		for (int i = 0; i < n; i++) {
			esperar(s, "DADOS_JOGADOR");
			Jogador j = jogadores.get(s.nextInt());

			esperar(s, "TRIBUTOS");
			int qtd = s.nextInt();
			for (int k = 0; k < qtd; k++)
				j.definirTributo(TipoTributo.valueOf(s.next()), s.nextInt());

			esperar(s, "TESOUROS");
			qtd = s.nextInt();
			for (int k = 0; k < qtd; k++)
				j.definirTesouro(TipoTesouro.valueOf(s.next()), s.nextInt());

			esperar(s, "YURTS_RESTANTES");
			j.definirYurtsRestantes(s.nextInt());

			esperar(s, "PEOES");
			qtd = s.nextInt();
			if (qtd != j.getPeoes().size())
				throw new IllegalArgumentException("número de peões diferente do herdeiro");
			for (Peao p : j.getPeoes())
				p.moverPara(parada(t, s.nextInt()));

			esperar(s, "CARTAS_SECRETAS");
			qtd = s.nextInt();
			List<CartaConselheiroSecreto> opcoes = new ArrayList<CartaConselheiroSecreto>();
			for (int k = 0; k < qtd; k++)
				opcoes.add(new CartaConselheiroSecreto(TipoConselheiroSecreto.valueOf(s.next())));
			jogo.definirOpcoesSecretas(j, opcoes);
			int escolhida = s.nextInt();
			j.escolherCartaSecreta(escolhida < 0 ? null : jogo.opcoesSecretas(j).get(escolhida));

			esperar(s, "VOTOS");
			t.getTrilha().restaurar(j, s.nextInt());

			esperar(s, "PECAS_BONUS");
			qtd = s.nextInt();
			List<TipoPecaBonus> pecas = new ArrayList<TipoPecaBonus>();
			List<Boolean> usadas = new ArrayList<Boolean>();
			for (int k = 0; k < qtd; k++) {
				String b = s.next();
				// sem sufixo (arquivo antigo): a peça já tinha sido aplicada ao ser pega, então conta como usada
				boolean guardada = b.endsWith(GUARDADA);
				if (guardada || b.endsWith(USADA))
					b = b.substring(0, b.lastIndexOf('@'));
				pecas.add(TipoPecaBonus.valueOf(b));
				usadas.add(!guardada);
			}
			j.definirPecasBonus(pecas, usadas);

			for (int c = 0; c < j.getTabuleiro().getColunas().size(); c++) {
				esperar(s, "COLUNA");
				int coluna = s.nextInt();
				boolean ativada = s.nextBoolean();
				int quantas = s.nextInt();
				List<Melhoria> melhorias = new ArrayList<Melhoria>();
				for (int k = 0; k < quantas; k++)
					melhorias.add(lerMelhoria(s));
				j.getTabuleiro().getColuna(coluna).restaurar(ativada, melhorias);
			}
		}

		esperar(s, "TRILHA");
		t.getTrilha().definirFichaNeutra(s.nextInt());

		Estoque e = t.getEstoque();
		esperar(s, "ESTOQUE_TRIBUTOS");
		int qtd = s.nextInt();
		for (int k = 0; k < qtd; k++)
			e.definirTributo(TipoTributo.valueOf(s.next()), s.nextInt());
		esperar(s, "ESTOQUE_TESOUROS");
		qtd = s.nextInt();
		List<TipoTesouro> tesouros = new ArrayList<TipoTesouro>();
		for (int k = 0; k < qtd; k++)
			tesouros.add(TipoTesouro.valueOf(s.next()));
		e.definirTesouros(tesouros);

		esperar(s, "PROVINCIAS");
		qtd = s.nextInt();
		for (int k = 0; k < qtd; k++) {
			Provincia p = t.getProvincia(s.nextInt());
			if (p == null)
				throw new IllegalArgumentException("província inexistente");
			p.definirQuantidade(s.nextInt());
		}

		esperar(s, "CIDADES");
		qtd = s.nextInt();
		for (int k = 0; k < qtd; k++) {
			esperar(s, "CIDADE");
			Cidade c = cidade(t, s.next());
			boolean revelada = s.nextBoolean();
			int dono = s.nextInt();
			int quantos = s.nextInt();
			List<TipoTesouro> naCidade = new ArrayList<TipoTesouro>();
			for (int m = 0; m < quantos; m++)
				naCidade.add(TipoTesouro.valueOf(s.next()));
			c.restaurar(revelada, naCidade, dono < 0 ? null : jogadores.get(dono));
		}

		esperar(s, "PILHA_CIDADES");
		qtd = s.nextInt();
		List<Cidade> pilha = new ArrayList<Cidade>();
		for (int k = 0; k < qtd; k++)
			pilha.add(cidade(t, s.next()));
		t.restaurarPilhaCidades(pilha);

		esperar(s, "KHAN");
		int provKhan = s.nextInt();
		t.getKhan().restaurar(provKhan < 0 ? null : t.getProvincia(provKhan), s.nextBoolean());

		esperar(s, "MELHORIAS_EXPOSTAS");
		qtd = s.nextInt();
		List<Melhoria> expostas = new ArrayList<Melhoria>();
		for (int k = 0; k < qtd; k++)
			expostas.add(lerMelhoria(s));
		esperar(s, "MELHORIAS_PILHA");
		qtd = s.nextInt();
		List<Melhoria> pilhaMelhorias = new ArrayList<Melhoria>();
		for (int k = 0; k < qtd; k++)
			pilhaMelhorias.add(lerMelhoria(s));
		t.getAreaMelhorias().restaurar(expostas, pilhaMelhorias);

		esperar(s, "CARTAS_ABERTAS");
		qtd = s.nextInt();
		List<CartaConselheiroAberto> abertas = new ArrayList<CartaConselheiroAberto>();
		for (int k = 0; k < qtd; k++) {
			CartaConselheiroAberto c = new CartaConselheiroAberto(TipoConselheiroAberto.valueOf(s.next()));
			c.definirRetirada(s.nextBoolean());
			abertas.add(c);
		}
		t.restaurarCartasAbertas(abertas);

		esperar(s, "BONUS");
		qtd = s.nextInt();
		List<TipoPecaBonus> bonus = new ArrayList<TipoPecaBonus>();
		for (int k = 0; k < qtd; k++) {
			String b = s.next();
			bonus.add(NULO.equals(b) ? null : TipoPecaBonus.valueOf(b));
		}
		t.restaurarPecasBonus(bonus);

		esperar(s, "PARADAS");
		qtd = s.nextInt();
		for (int k = 0; k < qtd; k++) {
			esperar(s, "YURTS");
			Parada p = parada(t, s.nextInt());
			int quantos = s.nextInt();
			List<Jogador> donos = new ArrayList<Jogador>();
			for (int m = 0; m < quantos; m++)
				donos.add(jogadores.get(s.nextInt()));
			p.restaurarYurts(donos);
		}

		esperar(s, "FIM");
		return jogo;
	}

	private static Melhoria lerMelhoria(Scanner s) {
		esperar(s, "MELHORIA");
		TipoMelhoria tipo = TipoMelhoria.valueOf(s.next());
		Regiao regiao = Regiao.valueOf(s.next());
		String tesouro = s.next();
		Melhoria m = new Melhoria(tipo, regiao, NULO.equals(tesouro) ? null : TipoTesouro.valueOf(tesouro));
		m.registrarCompra(s.nextInt());
		return m;
	}

	private static void esperar(Scanner s, String rotulo) {
		String lido = s.next();
		if (!lido.equals(rotulo))
			throw new IllegalArgumentException("esperava " + rotulo + " e encontrou " + lido);
	}

	private static Parada parada(Tabuleiro t, int id) {
		Parada p = t.getParada(id);
		if (p == null)
			throw new IllegalArgumentException("parada inexistente " + id);
		return p;
	}

	private static Cidade cidade(Tabuleiro t, String nome) {
		Cidade c = t.getCidade(nome);
		if (c == null)
			throw new IllegalArgumentException("cidade inexistente " + nome);
		return c;
	}

	// ===================== NOMES EM ASCII =====================

	// um nome vira um único token ASCII: espaço, barra invertida e acentos viram barra-u seguida de 4 hexadecimais
	static String codificar(String texto) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < texto.length(); i++) {
			char c = texto.charAt(i);
			if (c > ' ' && c < 127 && c != '\\')
				sb.append(c);
			else
				sb.append(String.format("\\u%04x", (int) c));
		}
		return sb.toString();
	}

	static String decodificar(String token) {
		StringBuilder sb = new StringBuilder();
		int i = 0;
		while (i < token.length()) {
			char c = token.charAt(i);
			if (c == '\\' && i + 6 <= token.length() && token.charAt(i + 1) == 'u') {
				sb.append((char) Integer.parseInt(token.substring(i + 2, i + 6), 16));
				i += 6;
			} else {
				sb.append(c);
				i++;
			}
		}
		return sb.toString();
	}
}