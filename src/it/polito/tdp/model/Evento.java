package it.polito.tdp.model;

import java.time.LocalDateTime;

public class Evento implements Comparable<Evento>{
		
	public enum tipoEvento {
		CRIMINE,
		ARRIVO_AGENTE,
		CRIMINE_GESTITO,	
	}
	
	private tipoEvento tipo;
	private LocalDateTime data;
	private Event evento;
	
	
	public Evento(tipoEvento tipo, LocalDateTime data, Event evento) {
		super();
		this.tipo = tipo;
		this.data = data;
		this.evento = evento;
	}
	

	public tipoEvento getTipo() {
		return tipo;
	}


	public void setTipo(tipoEvento tipo) {
		this.tipo = tipo;
	}

	public LocalDateTime getData() {
		return data;
	}

	public void setData(LocalDateTime data) {
		this.data = data;
	}

	public Event getEvento() {
		return evento;
	}

	public void setEvento(Event evento) {
		this.evento = evento;
	}

	//metodo di ordinamento per gli eventi in coda
	@Override
	public int compareTo(Evento o) {
		// TODO Auto-generated method stub
		return this.data.compareTo(o.getData());
	}
}
