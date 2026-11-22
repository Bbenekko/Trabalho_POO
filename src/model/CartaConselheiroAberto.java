package model;

import java.util.ArrayList;
import java.util.List;

class CartaConselheiroAberto extends CartaConselheiro {
	private TipoConselheiroAberto tipo;
	private boolean retirada = false;

	CartaConselheiroAberto(TipoConselheiroAberto tipo) {
		this.tipo = tipo;
	}

	TipoConselheiroAberto getTipo() {
		return this.tipo;
	}

	boolean isRetirada() {
		return this.retirada;
	}

	int meta() {
		switch (this.tipo) {
			case quatro_espadas:               return 4;
			case seis_yurts:                   return 6;
			case dez_yurts:                    return 10;
			case quatro_tesouros:              return 4;
			case cinco_tesouros_diferentes:    return 5;
			case um_yurt_cada_regiao:          return 1;
			case dois_yurts_cada_regiao:       return 2;
			case duas_pecas_bonus:             return 2;
			case tres_pecas_bonus:             return 3;
			case duas_cidades:                 return 2;
			case tres_cidades:                 return 3;
			case linha_de_melhorias:           return 1;
			case duas_colunas_com_duas:        return 2;
			default:                           return 4;
		}
	}

	@Override
	int progresso(Jogador j, Tabuleiro t) {
		TabuleiroJogador tj = j.getTabuleiro();
		switch (this.tipo) {
			case quatro_espadas:
				return j.quantidadeTributo(TipoTributo.espada);
			case seis_yurts:
			case dez_yurts:
				return this.yurtsNoMapa(j);
			case quatro_tesouros:
				return j.totalDeTesouros();
			case cinco_tesouros_diferentes:
				return this.tiposDeTesouro(j);
			case um_yurt_cada_regiao:
			case dois_yurts_cada_regiao:
				return this.menorQuantidadeDeYurtsPorRegiao(j, t);
			case duas_pecas_bonus:
			case tres_pecas_bonus:
				return j.getPecasBonus().size();
			case duas_cidades:
			case tres_cidades:
				return this.cidadesConquistadas(j, t);
			case linha_de_melhorias:
				return tj.linhaCompleta(1) ? 1 : 0;
			case duas_colunas_com_duas: {
				int colunas = 0;
				for (ColunaAcao c : tj.getColunas())
					if (c.getMelhorias().size() >= 2)
						colunas++;
				return colunas;
			}
			default: {       
				int maior = 0;
				for (ColunaAcao c : tj.getColunas())
					maior = Math.max(maior, c.getMelhorias().size());
				return maior;
			}
		}
	}

	// progresso contando também os tesouros virtuais disponíveis no turno (melhorias de tesouro
	// da coluna ativada), que valem para as cartas de tesouros
	int progresso(Jogador j, Tabuleiro t, List<TipoTesouro> virtuais) {
		if (this.tipo == TipoConselheiroAberto.quatro_tesouros)
			return j.totalDeTesouros() + virtuais.size();
		if (this.tipo == TipoConselheiroAberto.cinco_tesouros_diferentes) {
			int tipos = 0;
			for (TipoTesouro tt : TipoTesouro.values())
				if (j.quantidadeTesouro(tt) > 0 || virtuais.contains(tt))
					tipos++;
			return tipos;
		}
		return this.progresso(j, t);
	}

	boolean verificar(Jogador j, Tabuleiro t) {
		return this.verificar(j, t, new ArrayList<TipoTesouro>());
	}

	boolean verificar(Jogador j, Tabuleiro t, List<TipoTesouro> virtuais) {
		if (this.retirada)
			return false;
		if (this.progresso(j, t, virtuais) < this.meta())
			return false;
		t.getTrilha().registrarVotos(j, 1);
		this.retirada = true;
		return true;
	}

	void definirRetirada(boolean retirada) {
		this.retirada = retirada;
	}
}