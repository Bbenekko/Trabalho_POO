package model;

// Cap. 17: quem quer ser avisado das mudancas de estado de uma partida
public interface ObservadorIF {
	void notify(ObservadoIF o);
}
