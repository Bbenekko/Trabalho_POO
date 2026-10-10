
public class Teste {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Vetor v=new Vetor();
		
		// Neste ponto deveriam ser obtidas as coordenadas do vetor, via teclado.
		// Como ainda n�o se sabe como ler dados do teclado, ser�o criadas
		// coordenadas arbitr�rias.
		
		v.x=3.0;
		v.y=4.0;
		
		// O passo seguinte � calcular o m�dulo do vetor.
		
		double mod=Math.sqrt(Math.pow(v.x,2.0)+Math.pow(v.y,2.0));
		
		System.out.printf("M�dulo=%.1f",mod);
	}

}
