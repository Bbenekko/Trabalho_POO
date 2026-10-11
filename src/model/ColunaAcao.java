package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

class ColunaAcao 
{
	private Regiao regiao;
	private List<TipoAcao> acoesIniciais;
	private List<Melhoria> melhorias = new ArrayList<Melhoria>();
	private boolean ativada = false;
	
	ColunaAcao(Regiao regiao, List<TipoAcao> acoesIniciais) {
		this.regiao = regiao;
		this.acoesIniciais = new ArrayList<TipoAcao>(acoesIniciais);
	}
	
	Regiao getRegiao() {
		return this.regiao;
	}
	
	List<Melhoria> getMelhorias() {
		return Collections.unmodifiableList(this.melhorias);
	}
	
	boolean isAtivada() {
		return this.ativada;
	}
	
	boolean ativar() {
		if (this.ativada)
			return false;
		this.ativada = true;
		return true;
	}

	void desativar() {                 
		this.ativada = false;
	}
	
	int movimentos(int turnoAtual) {
		int n = 0;
		for (TipoAcao acao : this.acoesIniciais) {
			if (acao == TipoAcao.mover)
				n += 1;                        
		}
		for (Melhoria m : this.melhorias) {
			if (m.getTipo() == TipoMelhoria.dois_movimentos && m.utilizavelNoTurno(turnoAtual))
				n += 2;                       
		}
		return n;
	}

	int acoesDeTributo(int turnoAtual) {
		int n = 0;
		for (TipoAcao acao : this.acoesIniciais) {
			if (acao == TipoAcao.pegar_tributo)
				n += 1;                        
		}
		for (Melhoria m : this.melhorias) {
			if (m.getTipo() == TipoMelhoria.pegar_tributo && m.utilizavelNoTurno(turnoAtual))
				n += 1;                       
		}
		return n;
	}
	
	boolean exigeKhan() {
		return this.acoesIniciais.contains(TipoAcao.usar_khan);
	}

	boolean recuperaAtivacoes() {          
		return this.acoesIniciais.contains(TipoAcao.recuperar_ativacoes);
	}

	boolean estaCheia() {
		return this.melhorias.size() >= 4;
	}

	boolean adicionaMelhoria(Melhoria melhoria) {   // p. 12
		if (this.estaCheia())
			return false;
		if (this.regiao != null && melhoria.getRegiao() != this.regiao)
			return false;            
		this.melhorias.add(melhoria);
		return true;
	}
}