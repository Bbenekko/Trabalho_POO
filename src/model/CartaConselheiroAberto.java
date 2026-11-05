package model;

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
			default:                           return 4;   // quatro_melhorias_numa_coluna
		}
	}

	@Override
	int progresso(Jogador j, Tabuleiro t) {
		TabuleiroJogador tj = j.getTabuleiro();
		switch (this.tipo) {
			case quatro_espadas:
				return j.quantidadeTributo(TipoTributo.espada);   // nao precisa gastar
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
				return 0;                                         // TODO: quando existir PecaBonus
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
			default: {                                            // quatro_melhorias_numa_coluna
				int maior = 0;
				for (ColunaAcao c : tj.getColunas())
					maior = Math.max(maior, c.getMelhorias().size());
				return maior;
			}
		}
	}

	// p. 13 e 18: o primeiro a cumprir ganha 1 voto e a carta sai do jogo
	boolean verificar(Jogador j, Tabuleiro t) {
		if (this.retirada)
			return false;
		if (this.progresso(j, t) < this.meta())
			return false;
		t.getTrilha().registrarVotos(j, 1);
		this.retirada = true;
		return true;
	}
}
