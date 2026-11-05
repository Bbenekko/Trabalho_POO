package model;

// Cap. 17: quem muda de estado e avisa os observadores registrados
public interface ObservadoIF {
	void add(ObservadorIF o);

	void remove(ObservadorIF o);

	int get(int i);
}
