package model;

import java.util.List;

class TabuleiroJogador 
{
	private List<ColunaAcao> colunas;
	
	TabuleiroJogador(List<ColunaAcao> colunas)
	{
		this.colunas = colunas;
	}

	ColunaAcao getColuna(int i)
	{
		return this.colunas.get(i);
	}
	
	boolean ativar(int coluna)
	{
		return this.colunas.get(coluna).ativar();
	}
	
	void recuperarAtivações()
	{
		for(ColunaAcao coluna : colunas)
		{
			coluna.recuperaAtivacoes();
		}
	}
	
	boolean linhaCompleta(int linha)
	{
		for(int i =0; i < 3; i++)
		{
			if (this.colunas.get(i).getMelhorias().size() < linha) {return false;}
		}
		return true;
	}
	
	boolean adicionarMelhoria(Melhoria m, int coluna)
	{	

		return this.colunas.get(coluna).adicionaMelhoria(m);
	}
	
	int contarMelhorias(Regiao r)
	{

		for(ColunaAcao coluna : this.colunas)
		{
			if(coluna.getRegiao() == r)
			{
				return coluna.getMelhorias().size();
			}
		}
		
		return -1;
	}
}
