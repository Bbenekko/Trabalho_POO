package model;

import java.util.HashMap;
import java.util.Map;

class TrilhaKurultai {
	private Map<Jogador, Integer> votos = new HashMap<Jogador, Integer>();
	private int fichaNeutra = 0;
	private int limite;
	
	TrilhaKurultai(int numJogadores) {
		// casa do marco impresso na trilha (a cadeira é a casa 0): a ficha neutra chegar nele dispara o fim
		switch (numJogadores) {
			case 2: this.limite = 9; break;
			case 3: this.limite = 13; break;
			case 4: this.limite = 15; break;
			case 5: this.limite = 16; break;
			default:
				throw new IllegalArgumentException("O jogo é para 2 a 5 jogadores: " + numJogadores);
		}
	}
	
	int getFichaNeutra(){
		return this.fichaNeutra;
	}
	
	int getLimite(){
		return this.limite;
	}

	void registrarVotos(Jogador j, int n){
		int atual = votos.getOrDefault(j, 0);
		this.votos.put(j, atual + n);
		this.fichaNeutra += n;
	}
	
	int votosDe(Jogador j){
		return this.votos.getOrDefault(j, 0);
	}
	
	boolean fimDisparado(){
		return this.fichaNeutra >= this.limite;
	}

	// recuperação de partida salva
	void restaurar(Jogador j, int votos) {
		this.votos.put(j, votos);
	}

	void definirFichaNeutra(int ficha) {
		this.fichaNeutra = ficha;
	}
}