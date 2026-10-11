package model;

<<<<<<< HEAD
import java.util.List;
=======
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

class Cidade {
	private String nome;
	private Regiao regiao;
	private boolean revelada = false;
	private List<TipoTesouro> tesouros = new ArrayList<TipoTesouro>();
	private Jogador conquistador = null;
>>>>>>> refs/remotes/origin/Caio

<<<<<<< HEAD
class Cidade 
{
	private String nome;
	private Regiao regiao;
	private boolean revelada;
	private List<TipoTesouro> tesouros;
	private Jogador conquistador;
	
	String getNome()
	{
		return this.nome;
	}
	
	Regiao getRegiao()
	{
		return this.regiao;
	}
	
	List<TipoTesouro> getTesouros()
	{
		return this.tesouros;
	}
	
	Jogador getConquistador()
	{
		return this.conquistador;
	}
	
	// esse método é realmente necessário???
	boolean podeSerAtacada()
	{
		return !this.tesouros.isEmpty();
	}
	
	// porque é boolean??
	boolean tomarTesouro(TipoTesouro t)
	{
		this.tesouros.remove(t);
		return true;
	}
	
	// já não dá pra checar se foi conquistada pelo getconquistador???
	boolean foiConquistada()
	{
		return this.conquistador != null;
	}
	
	// parece estar faltando uma função de revelar
}
=======
	Cidade(String nome, Regiao regiao) {
		this.nome = nome;
		this.regiao = regiao;
	}

	String getNome() { 
		return this.nome; 
	}
	
	Regiao getRegiao() { 
		return this.regiao; 
	}
	
	boolean isRevelada() { 
		return this.revelada; 
	}
	
	Jogador getConquistador() { 
		return this.conquistador; 
	}

	List<TipoTesouro> getTesouros() {
		return Collections.unmodifiableList(this.tesouros);
	}

	void revelar() {
		this.revelada = true;
	}

	void receberTesouro(TipoTesouro t) {
		this.tesouros.add(t);
	}

	boolean podeSerAtacada() {
		return this.revelada && !this.tesouros.isEmpty();
	}

	boolean tomarTesouro(Jogador atacante, TipoTesouro t) {
		if (!this.podeSerAtacada())
			return false;
		if (!this.tesouros.remove(t))
			return false;                 
		if (this.tesouros.isEmpty())
			this.conquistador = atacante;
		return true;
	}

	boolean foiConquistada() {
		return this.conquistador != null;
	}
}
>>>>>>> refs/remotes/origin/Caio
