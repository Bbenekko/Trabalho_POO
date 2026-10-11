package model;

class Khan {
	private Provincia provincia = null;
	private boolean naAreaDeMelhorias = false;
	
	Provincia getProvincia() {
		return this.provincia;
	}
	
	boolean isNaAreaDeMelhorias() {
		return this.naAreaDeMelhorias;
	}
	
	boolean podeIrParaProvincia(Provincia p) {
		if(this.provincia != p && p.isProvinciaDoKhan())
			return true;
		return false;	
	}
	
	boolean podeIrParaMelhorias() {
		if(this.naAreaDeMelhorias != false)
			return false;
		return true;
	}
	
	boolean moverParaProvincia(Provincia p) {
		if(this.podeIrParaProvincia(p))
		{
			this.provincia = p;
			this.naAreaDeMelhorias = false;
			return true;
		}	
		return false;
	}
	
	boolean moverParaMelhorias() {
		if(this.podeIrParaMelhorias())
		{
			this.naAreaDeMelhorias = true;
			this.provincia = null;
			return true;
		}
		return false;
	}
	
}
