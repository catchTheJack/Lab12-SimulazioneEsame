package it.polito.tdp.model;

public class Vicino {

	private int id_vicino;
	private double distanza;
	
	public Vicino(int id_vicino, double distanza) {
		super();
		this.id_vicino = id_vicino;
		this.distanza = distanza;
	}

	public int getId_vicino() {
		return id_vicino;
	}

	public void setId_vicino(int id_vicino) {
		this.id_vicino = id_vicino;
	}

	public double getDistanza() {
		return distanza;
	}

	public void setDistanza(double distanza) {
		this.distanza = distanza;
	}

	@Override
	public String toString() {
		return String.format("Vicino [id_vicino=%s, distanza=%s]", id_vicino, distanza);
	}
	
	
	
	
}
