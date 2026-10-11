package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

class TabuleiroJogador 
{
	// ordem fixa: 0 = amarela (russia), 1 = verde (persia), 2 = vermelha (china), 3 = branca
	private List<ColunaAcao> colunas = new ArrayList<ColunaAcao>();
	
	TabuleiroJogador(Herdeiro herdeiro)
	{
		List<TipoAcao> c0 = new ArrayList<TipoAcao>();
		List<TipoAcao> c1 = new ArrayList<TipoAcao>();
		List<TipoAcao> c2 = new ArrayList<TipoAcao>();
		List<TipoAcao> c3 = new ArrayList<TipoAcao>();
		
		if (herdeiro == Herdeiro.altani){
			c0.add(TipoAcao.mover);
			c0.add(TipoAcao.pegar_tributo);
			c0.add(TipoAcao.pegar_tributo);
			c0.add(TipoAcao.usar_khan);
			
			c1.add(TipoAcao.mover);
			c1.add(TipoAcao.mover);
			c1.add(TipoAcao.pegar_tributo);
			
			c2.add(TipoAcao.mover);
			c2.add(TipoAcao.pegar_tributo);
			c2.add(TipoAcao.moeda_virtual);
			
			c3.add(TipoAcao.mover);
			c3.add(TipoAcao.mover);
			c3.add(TipoAcao.usar_khan);
			c3.add(TipoAcao.recuperar_ativacoes);
		}
		else if (herdeiro == Herdeiro.chagatai) {
			c0.add(TipoAcao.mover);
			c0.add(TipoAcao.pegar_tributo);
			c0.add(TipoAcao.pegar_tributo);
			c0.add(TipoAcao.usar_khan);
			
			c1.add(TipoAcao.mover);
			c1.add(TipoAcao.pegar_tributo);
			c1.add(TipoAcao.moeda_virtual);
			
			c2.add(TipoAcao.mover);
			c2.add(TipoAcao.mover);
			c2.add(TipoAcao.pegar_tributo);
			
			c3.add(TipoAcao.mover);
			c3.add(TipoAcao.mover);
			c3.add(TipoAcao.usar_khan);
			c3.add(TipoAcao.recuperar_ativacoes);
		}
		else if (herdeiro == Herdeiro.jochi) {
			c0.add(TipoAcao.mover);
			c0.add(TipoAcao.pegar_tributo);
			c0.add(TipoAcao.moeda_virtual);
			
			c1.add(TipoAcao.mover);
			c1.add(TipoAcao.pegar_tributo);
			c1.add(TipoAcao.pegar_tributo);
			c1.add(TipoAcao.usar_khan);
			
			c2.add(TipoAcao.mover);
			c2.add(TipoAcao.mover);
			c2.add(TipoAcao.pegar_tributo);
			
			c3.add(TipoAcao.mover);
			c3.add(TipoAcao.mover);
			c3.add(TipoAcao.usar_khan);
			c3.add(TipoAcao.recuperar_ativacoes);
		}
		else if (herdeiro == Herdeiro.ogedei) {
			c0.add(TipoAcao.mover);
			c0.add(TipoAcao.mover);
			c0.add(TipoAcao.pegar_tributo);
			
			c1.add(TipoAcao.mover);
			c1.add(TipoAcao.pegar_tributo);
			c1.add(TipoAcao.moeda_virtual);
			
			c2.add(TipoAcao.mover);
			c2.add(TipoAcao.pegar_tributo);
			c2.add(TipoAcao.pegar_tributo);
			c2.add(TipoAcao.usar_khan);
			
			c3.add(TipoAcao.mover);
			c3.add(TipoAcao.mover);
			c3.add(TipoAcao.usar_khan);
			c3.add(TipoAcao.recuperar_ativacoes);
		}
		else if (herdeiro == Herdeiro.tolui) {
			c0.add(TipoAcao.mover);
			c0.add(TipoAcao.pegar_tributo);
			c0.add(TipoAcao.moeda_virtual);
			
			c1.add(TipoAcao.mover);
			c1.add(TipoAcao.mover);
			c1.add(TipoAcao.pegar_tributo);
			
			c2.add(TipoAcao.mover);
			c2.add(TipoAcao.pegar_tributo);
			c2.add(TipoAcao.pegar_tributo);
			c2.add(TipoAcao.usar_khan);
			
			c3.add(TipoAcao.mover);
			c3.add(TipoAcao.mover);
			c3.add(TipoAcao.usar_khan);
			c3.add(TipoAcao.recuperar_ativacoes);
		}
		else {
			throw new IllegalArgumentException("Escolha não permitida de Khan ");
		}
				
		this.colunas.add(new ColunaAcao(Regiao.russia, c0));
		this.colunas.add(new ColunaAcao(Regiao.persia, c1));
		this.colunas.add(new ColunaAcao(Regiao.china,  c2));
		this.colunas.add(new ColunaAcao(null,          c3));
	}

	ColunaAcao getColuna(int i) {
		return this.colunas.get(i);
	}
	
	List<ColunaAcao> getColunas() {
		return Collections.unmodifiableList(this.colunas);
	}
	
	boolean ativar(int coluna) {
		return this.colunas.get(coluna).ativar();
	}
	
	void recuperarAtivacoes() {              
		for (ColunaAcao coluna : this.colunas)
			coluna.desativar();
	}
	
	boolean adicionarMelhoria(Melhoria m, int coluna) {
		return this.colunas.get(coluna).adicionaMelhoria(m);
	}
	
	boolean linhaCompleta(int linha) {       
		for (int i = 0; i < 3; i++) {
			if (this.colunas.get(i).getMelhorias().size() < linha)
				return false;
		}
		return true;
	}
	
	int contarMelhorias(Regiao r) {         
		for (ColunaAcao coluna : this.colunas) {
			if (coluna.getRegiao() == r)
				return coluna.getMelhorias().size();
		}
		return 0;
	}
	
	boolean ganhaMelhoriaGratis(int linha) {       
		return this.colunas.get(3).getMelhorias().size() < linha;
	}
}
