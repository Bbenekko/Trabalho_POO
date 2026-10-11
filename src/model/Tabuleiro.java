package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

class Tabuleiro {
	private Map<Integer, Parada> paradas = new HashMap<Integer, Parada>();
	private Map<Integer, Provincia> provincias = new HashMap<Integer, Provincia>();
	private Map<String, Cidade> cidades = new HashMap<String, Cidade>();
	private Stack<Cidade> pilhaCidades = new Stack<Cidade>();
	private Estoque estoque;
	private Khan khan;
	private AreaMelhorias areaMelhorias;
	private TrilhaKurultai trilha;
	//private List<CartasConselheiroAberto> cartasAbertas = new ArrayList<CartasConselheiroAberto>();
	static final int KARAKORUM = 33;

	
	Tabuleiro(int numJogadores) {
		this.khan = new Khan();
		this.estoque = new Estoque();
		this.areaMelhorias = new AreaMelhorias();
		this.trilha = new TrilhaKurultai(numJogadores);
		this.criarMapa();       
		this.abastecerProvincias(); 
		this.prepararCidades();   
	}
	
	private void criarMapa() {
		
		// Para ajudar a visualização veja a imagem em https://github.com/Bbenekko/Trabalho_POO/blob/main/doc/img/Mapa_identidades.png
		
		// Cidades
		Cidade kiev = new Cidade("Kiev", Regiao.russia);
		Cidade moscou = new Cidade("Moscou", Regiao.russia);
		Cidade sarai = new Cidade("Sarai", Regiao.russia);
		Cidade bagda = new Cidade("Bagda", Regiao.persia);
		Cidade samarcanda = new Cidade("Samarcanda", Regiao.persia);
		Cidade cabul = new Cidade("Cabul", Regiao.persia);
		Cidade pequim = new Cidade("Pequim", Regiao.china);
		Cidade kaesong = new Cidade("Kaesong", Regiao.china);
		Cidade cantao = new Cidade("Cantao",Regiao.china);
		this.cidades.put("Kiev",kiev);
		this.cidades.put("Moscou",moscou);
		this.cidades.put("Sarai",sarai);
		this.cidades.put("Bagda",bagda);
		this.cidades.put("Samarcanda",samarcanda);
		this.cidades.put("Cabul",cabul);
		this.cidades.put("Pequim",pequim);
		this.cidades.put("Kaesong",kaesong);
		this.cidades.put("Cantao",cantao);
		
		// Provincias do canto superior esquerdo
		Provincia a = new Provincia(0,TipoTributo.yurt, true);
		Provincia b = new Provincia(1,TipoTributo.espada, false);
		Provincia c = new Provincia(2,TipoTributo.moeda, false);
		a.adicionarAlvoDoKhan(b);
		a.adicionarAlvoDoKhan(c);	
		this.provincias.put(0, a);
		this.provincias.put(1, b);
		this.provincias.put(2, c);
		
		//Provincias do canto superior direito
		Provincia d = new Provincia(3,TipoTributo.yurt, false);
		Provincia e = new Provincia(4,TipoTributo.moeda, true);
		Provincia f = new Provincia(5,TipoTributo.espada, false);
		e.adicionarAlvoDoKhan(d);
		e.adicionarAlvoDoKhan(f);	
		this.provincias.put(3, d);
		this.provincias.put(4, e);
		this.provincias.put(5, f);
		
		//Provincias do canto inferior esquerdo
		Provincia g = new Provincia(6,TipoTributo.espada, false);
		Provincia h = new Provincia(7,TipoTributo.moeda, true);
		Provincia i = new Provincia(8,TipoTributo.yurt, false);
		h.adicionarAlvoDoKhan(g);
		h.adicionarAlvoDoKhan(i);	
		this.provincias.put(6, g);
		this.provincias.put(7, h);
		this.provincias.put(8, i);
		
		//Provincias do canto inferior direito
		Provincia j = new Provincia(9,TipoTributo.espada, false);
		Provincia k = new Provincia(10,TipoTributo.yurt, true);
		Provincia l = new Provincia(11,TipoTributo.moeda, false);
		k.adicionarAlvoDoKhan(j);
		k.adicionarAlvoDoKhan(l);	
		this.provincias.put(9, j);
		this.provincias.put(10, k);
		this.provincias.put(11, l);
		
		//Paradas da regiao da Russia começando na ordem de criacao (Kiev - dupla - moscou - sarai - dupla) da esquerda para a direita e de cima para baixo
		
		Parada p00 = new Parada(0,TipoParada.simples,Regiao.russia);
		Parada p01 = new Parada(1,TipoParada.simples,Regiao.russia);
		Parada p02 = new Parada(2,TipoParada.simples,Regiao.russia);
		
		Parada p03 = new Parada(3,TipoParada.dupla,Regiao.russia);
		
		Parada p04 = new Parada(4,TipoParada.simples,Regiao.russia);
		Parada p05 = new Parada(5,TipoParada.simples,Regiao.russia);
		Parada p06 = new Parada(6,TipoParada.simples,Regiao.russia);
		
		Parada p07 = new Parada(7,TipoParada.simples,Regiao.russia);
		Parada p08 = new Parada(8,TipoParada.simples,Regiao.russia);
		Parada p09 = new Parada(9,TipoParada.simples,Regiao.russia);
		
		Parada p10 = new Parada(10,TipoParada.dupla,Regiao.russia);
		
		//Paradas da regiao da Persia começando na ordem de criacao (dupla - samarcanda - bagda - dupla - cabul) da esquerda para a direita e de cima para baixo
		
		Parada p11 = new Parada(11,TipoParada.dupla,Regiao.persia);

		Parada p12 = new Parada(12,TipoParada.simples,Regiao.persia);
		Parada p13 = new Parada(13,TipoParada.simples,Regiao.persia);
		Parada p14 = new Parada(14,TipoParada.simples,Regiao.persia);

		Parada p15 = new Parada(15,TipoParada.simples,Regiao.persia);
		Parada p16 = new Parada(16,TipoParada.simples,Regiao.persia);
		Parada p17 = new Parada(17,TipoParada.simples,Regiao.persia);
		
		Parada p18 = new Parada(18,TipoParada.dupla,Regiao.persia);
		
		Parada p19 = new Parada(19,TipoParada.simples,Regiao.persia);
		Parada p20 = new Parada(20,TipoParada.simples,Regiao.persia);
		Parada p21 = new Parada(21,TipoParada.simples,Regiao.persia);
		
		
		//Paradas da regiao da China começando na ordem de criacao (Kaesong - Pequim - dupla - dupla - cantao) da esquerda para a direita e de cima para baixo

		Parada p22 = new Parada(22,TipoParada.simples,Regiao.china);
		Parada p23 = new Parada(23,TipoParada.simples,Regiao.china);
		Parada p24 = new Parada(24,TipoParada.simples,Regiao.china);

		Parada p25 = new Parada(25,TipoParada.simples,Regiao.china);
		Parada p26 = new Parada(26,TipoParada.simples,Regiao.china);
		Parada p27 = new Parada(27,TipoParada.simples,Regiao.china);
		
		Parada p28 = new Parada(28,TipoParada.dupla,Regiao.china);

		Parada p29 = new Parada(29,TipoParada.dupla,Regiao.china);

		Parada p30 = new Parada(30,TipoParada.simples,Regiao.china);
		Parada p31 = new Parada(31,TipoParada.simples,Regiao.china);
		Parada p32 = new Parada(32,TipoParada.simples,Regiao.china);
		
		//Parada do Karakorum
		
		Parada karakorum = new Parada(KARAKORUM,TipoParada.karakorum,null);

		//Conexões entre as paradas, cidades e provincias
		
		p00.adicionarVizinha(p01);
		p00.adicionarVizinha(p02);
		p00.adicionarCidade(kiev);
		
		p01.adicionarVizinha(p00);
		p01.adicionarVizinha(p02);
		p01.adicionarVizinha(p11);
		p01.adicionarCidade(kiev);
		p01.adicionarProvincia(a);
		
		p02.adicionarVizinha(p00);
		p02.adicionarVizinha(p01);
		p02.adicionarVizinha(p03);
		p02.adicionarVizinha(p04);
		p02.adicionarCidade(kiev);
		p02.adicionarProvincia(a);
		p02.adicionarProvincia(b);
		
		p03.adicionarVizinha(p02);
		p03.adicionarVizinha(p06);
		p03.adicionarProvincia(b);
		
		p04.adicionarVizinha(p02);
		p04.adicionarVizinha(p05);
		p04.adicionarVizinha(p06);
		p04.adicionarVizinha(p11);
		p04.adicionarCidade(moscou);
		p04.adicionarProvincia(a);
		p04.adicionarProvincia(b);
		p04.adicionarProvincia(c);
		
		p05.adicionarVizinha(p04);
		p05.adicionarVizinha(p06);
		p05.adicionarVizinha(p12);
		p05.adicionarCidade(moscou);
		p05.adicionarProvincia(c);
		p05.adicionarProvincia(d);
		
		p06.adicionarVizinha(p03);
		p06.adicionarVizinha(p04);
		p06.adicionarVizinha(p05);
		p06.adicionarVizinha(p07);
		p06.adicionarCidade(moscou);
		p06.adicionarProvincia(b);
		p06.adicionarProvincia(d);
		
		p07.adicionarVizinha(p06);
		p07.adicionarVizinha(p08);
		p07.adicionarVizinha(p09);
		p07.adicionarVizinha(karakorum);
		p07.adicionarCidade(sarai);
		p07.adicionarProvincia(d);
		p07.adicionarProvincia(e);
		
		p08.adicionarVizinha(p07);
		p08.adicionarVizinha(p09);
		p08.adicionarCidade(sarai);
		
		p09.adicionarVizinha(p07);
		p09.adicionarVizinha(p08);
		p09.adicionarVizinha(p10);
		p09.adicionarCidade(sarai);
		p09.adicionarProvincia(e);
		
		p10.adicionarVizinha(p09);
		p10.adicionarVizinha(p22);
		p10.adicionarVizinha(karakorum);
		p10.adicionarProvincia(e);
		p10.adicionarProvincia(f);
		
		p11.adicionarVizinha(p01);
		p11.adicionarVizinha(p04);
		p11.adicionarVizinha(p12);
		p11.adicionarVizinha(p15);
		p11.adicionarProvincia(a);
		p11.adicionarProvincia(c);
		p11.adicionarProvincia(g);
		
		p12.adicionarVizinha(p05);
		p12.adicionarVizinha(p11);
		p12.adicionarVizinha(p13);
		p12.adicionarVizinha(p14);
		p12.adicionarCidade(samarcanda);
		p12.adicionarProvincia(c);
		p12.adicionarProvincia(d);
		p12.adicionarProvincia(g);
		
		p13.adicionarVizinha(p12);
		p13.adicionarVizinha(p14);
		p13.adicionarVizinha(p19);
		p13.adicionarVizinha(karakorum);
		p13.adicionarCidade(samarcanda);
		p13.adicionarProvincia(d);
		p13.adicionarProvincia(h);
		p13.adicionarProvincia(i);
		
		p14.adicionarVizinha(p12);
		p14.adicionarVizinha(p13);
		p14.adicionarVizinha(p15);
		p14.adicionarCidade(samarcanda);
		p14.adicionarProvincia(g);
		p14.adicionarProvincia(h);
		
		p15.adicionarVizinha(p11);
		p15.adicionarVizinha(p14);
		p15.adicionarVizinha(p16);
		p15.adicionarVizinha(p17);
		p15.adicionarCidade(bagda);
		p15.adicionarProvincia(g);
		p15.adicionarProvincia(h);
		
		p16.adicionarVizinha(p15);
		p16.adicionarVizinha(p17);
		p16.adicionarCidade(bagda);
		
		p17.adicionarVizinha(p15);		
		p17.adicionarVizinha(p16);		
		p17.adicionarVizinha(p18);
		p17.adicionarCidade(bagda);
		p17.adicionarProvincia(h);
		
		p18.adicionarVizinha(p17);
		p18.adicionarVizinha(p19);
		p18.adicionarProvincia(h);
		
		p19.adicionarVizinha(p13);
		p19.adicionarVizinha(p18);
		p19.adicionarVizinha(p20);
		p19.adicionarVizinha(p21);
		p19.adicionarVizinha(p26);
		p19.adicionarCidade(cabul);
		p19.adicionarProvincia(h);
		p19.adicionarProvincia(i);
		p19.adicionarProvincia(j);
		
		p20.adicionarVizinha(p19);
		p20.adicionarVizinha(p21);
		p20.adicionarCidade(cabul);
		
		p21.adicionarVizinha(p19);
		p21.adicionarVizinha(p20);
		p21.adicionarVizinha(p29);
		p21.adicionarCidade(cabul);
		p21.adicionarProvincia(j);
		
		p22.adicionarVizinha(p10);
		p22.adicionarVizinha(p23);
		p22.adicionarVizinha(p24);
		p22.adicionarCidade(kaesong);
		p22.adicionarProvincia(f);
		
		p23.adicionarVizinha(p22);
		p23.adicionarVizinha(p24);
		p23.adicionarCidade(kaesong);
	
		p24.adicionarVizinha(p22);
		p24.adicionarVizinha(p23);
		p24.adicionarVizinha(p25);
		p24.adicionarVizinha(p28);
		p24.adicionarCidade(kaesong);
		p24.adicionarProvincia(f);
		p24.adicionarProvincia(l);
		
		p25.adicionarVizinha(p24);
		p25.adicionarVizinha(p26);
		p25.adicionarVizinha(p27);
		p25.adicionarVizinha(karakorum);
		p25.adicionarCidade(pequim);
		p25.adicionarProvincia(f);
		p25.adicionarProvincia(i);
		p25.adicionarProvincia(l);
		
		p26.adicionarVizinha(p19);
		p26.adicionarVizinha(p25);
		p26.adicionarVizinha(p27);
		p26.adicionarVizinha(p29);
		p26.adicionarCidade(pequim);
		p26.adicionarProvincia(i);
		p26.adicionarProvincia(j);
		p26.adicionarProvincia(k);
		
		p27.adicionarVizinha(p25);
		p27.adicionarVizinha(p26);
		p27.adicionarVizinha(p31);
		p27.adicionarCidade(pequim);
		p27.adicionarProvincia(k);
		p27.adicionarProvincia(l);
		
		p28.adicionarVizinha(p24);
		p28.adicionarVizinha(p31);
		p28.adicionarProvincia(l);
		
		p29.adicionarVizinha(p21);
		p29.adicionarVizinha(p26);
		p29.adicionarVizinha(p30);
		p29.adicionarProvincia(j);
		p29.adicionarProvincia(k);
		
		p30.adicionarVizinha(p29);
		p30.adicionarVizinha(p31);
		p30.adicionarVizinha(p32);
		p30.adicionarCidade(cantao);
		p30.adicionarProvincia(k);
		
		p31.adicionarVizinha(p27);
		p31.adicionarVizinha(p28);
		p31.adicionarVizinha(p30);
		p31.adicionarVizinha(p32);
		p31.adicionarCidade(cantao);
		p31.adicionarProvincia(k);
		p31.adicionarProvincia(l);
		
		p32.adicionarVizinha(p30);
		p32.adicionarVizinha(p31);
		p32.adicionarCidade(cantao);
		
		karakorum.adicionarVizinha(p07);
		karakorum.adicionarVizinha(p10);
		karakorum.adicionarVizinha(p13);
		karakorum.adicionarVizinha(p25);
		karakorum.adicionarProvincia(d);
		karakorum.adicionarProvincia(e);
		karakorum.adicionarProvincia(f);
		karakorum.adicionarProvincia(i);
		
		Parada[] todas = {
				p00, p01, p02, p03, p04, p05, p06, p07, p08, p09,
				p10, p11, p12, p13, p14, p15, p16, p17, p18, p19,
				p20, p21, p22, p23, p24, p25, p26, p27, p28, p29,
				p30, p31, p32, karakorum
		};

		for (Parada p : todas) {
			this.paradas.put(p.getId(), p);
		}
	}
	
	private void abastecerProvincias() {
		for(int cont = 0; cont < provincias.size();cont++) {
			provincias.get(cont).receberDoEstoque(this.estoque);
		}
	}
	
	private void prepararCidades() {
		pilhaCidades.push(this.cidades.get("Kiev"));
		pilhaCidades.push(this.cidades.get("Moscou"));
		pilhaCidades.push(this.cidades.get("Sarai"));
		pilhaCidades.push(this.cidades.get("Bagda"));
		pilhaCidades.push(this.cidades.get("Samarcanda"));
		pilhaCidades.push(this.cidades.get("Cabul"));
		pilhaCidades.push(this.cidades.get("Pequim"));
		pilhaCidades.push(this.cidades.get("Kaesong"));
		pilhaCidades.push(this.cidades.get("Cantao"));
		Collections.shuffle(pilhaCidades);
		
		for (int n = 0; n < 3; n++) {
			this.revelarProximaCidade();
		}
	}
	
	Parada getParada(int id) {
		return this.paradas.get(id);
	}
	
	Provincia getProvincia(int id) {
		return this.provincias.get(id);
	}
	
	Cidade getCidade(String nome) {
		return this.cidades.get(nome);
	}
	
	Khan getKhan() {
		return this.khan;
	}
	
	AreaMelhorias getAreaMelhorias() {
		return this.areaMelhorias;
	}
	
	TrilhaKurultai getTrilha() {
		return this.trilha;
	}
	
	Estoque getEstoque() {
		return this.estoque;
	}
	
	boolean saoAdjacentes(Parada a, Parada b) {
		List<Parada> a_vizinhos = new ArrayList<Parada>();
		List<Parada> b_vizinhos = new ArrayList<Parada>();
		a_vizinhos = a.getVizinhas();
		b_vizinhos = b.getVizinhas();
		if(a_vizinhos.contains(b) && b_vizinhos.contains(a)) {
			return true;
		}
		return false;
	}
	
	List<Cidade> cidadesReveladas() {
		List<Cidade> reveladas = new ArrayList<Cidade>();
		for (Cidade c : this.cidades.values()) {
			if (c.isRevelada())
				reveladas.add(c);
		}
		return reveladas;
	}
	
	void revelarProximaCidade() {
		if (this.pilhaCidades.isEmpty())
			return;                           
		Cidade c = this.pilhaCidades.pop();
		c.revelar();
		for (int n = 0; n < 4; n++) {
			TipoTesouro t = this.estoque.sortearTesouro();
			if (t != null)
				c.receberTesouro(t);
		}
	}
	
	int contarYurts(Jogador j, Regiao r) {
		int qtd = 0;
		for(int cont2 = 0; cont2 < paradas.size(); cont2++) {
			Parada p = this.paradas.get(cont2);
			if(p.getRegiao() == r) {
				List<Jogador> yurt = new ArrayList<Jogador>();
				yurt = p.getYurts();
				for(Jogador y: yurt) {
					if(y ==j)
						qtd++;
				}
			}
		}
		for (Cidade c : this.cidades.values()) {
			if (c.getRegiao() == r && c.getConquistador() == j)
				qtd++;
		}
		return qtd;	
	}
	
}
