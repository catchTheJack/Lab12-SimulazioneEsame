package it.polito.tdp.model;

import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Random;

import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import it.polito.tdp.db.EventsDao;
import it.polito.tdp.model.Evento.tipoEvento;


public class Simulatore {
	
	Model m ;
	private PriorityQueue<Evento> eventi;
	private Map<Integer,Integer> agenti_disponibili;
	private int gestiti_correttamente;
	private int mal_gestiti;
	private EventsDao edao;
	private int totale_gestiti;
	private int selezionato;
	
	public Simulatore() {
		
	}
	
	//INIZIALIZZO IL SIMULATORE
	public void init(int anno, int mese , int giorno , int numero_agenti ) {
		
		m  = new Model();
		edao = new EventsDao();
		eventi = new PriorityQueue<Evento>();
		agenti_disponibili = new HashMap<Integer,Integer>();
		gestiti_correttamente = 0;
		mal_gestiti = 0;
		totale_gestiti = 0;
		
		//generare la mappa degli agenti disponibili
		int police_station = edao.getPoliceStation(anno);
		agenti_disponibili.put(police_station, numero_agenti);
		selezionato = police_station;
		
		///carico gli eventi criminosi per caricare la coda
		for(Event ex : edao.listAllEventsByDate(anno,mese,giorno)) {
			eventi.add(new Evento(tipoEvento.CRIMINE,ex.getReported_date(),ex));
		}
	}
	
	public void run() {
		
		while (!eventi.isEmpty()) {
			Evento ev = eventi.poll();
			
			switch (ev.getTipo()) {
				
			case CRIMINE:
				long tempo_viaggio=0;
				//se gli agenti non ci sono aumento solo il contatore dei mal gestiti
				if(disponibili()==0) {
					mal_gestiti++;
				}
				//caso in cui ci sia un agente nel distretto dell'evento.
				if(agentInDistrict(ev.getEvento().getDistrict_id())==true) {
					tempo_viaggio = 0;
					agenti_disponibili.put(ev.getEvento().getDistrict_id(),
							this.agenti_disponibili.get(ev.getEvento().getDistrict_id())-1);
				}
				//caso di ricerca dell'agente piu vicino
				else {
					int id_arrivo=ev.getEvento().getDistrict_id();
					tempo_viaggio=(long) ((minDistance(id_arrivo)/60)*60*60);
					
				}
				
				eventi.add(new Evento(tipoEvento.ARRIVO_AGENTE,
						ev.getEvento().getReported_date().plusSeconds(tempo_viaggio),ev.getEvento()));
				agenti_disponibili.put(selezionato,agenti_disponibili.get(selezionato)-1);
				
				
				break;
				
			case ARRIVO_AGENTE:
				
				long duration = 0;
				if(ev.getEvento().getOffense_category_id().compareTo("all_other_crimes")==0) {
					if(getProbabilità()<=5)
							duration = 1*60*60;
					else
							duration = 2*60*60;
					}
				else
					duration = 2*60*60;
				
				if(ev.getData().isAfter(ev.getEvento().getReported_date().plusMinutes(15)))
					mal_gestiti ++;
				else
					gestiti_correttamente ++;
				
				eventi.add(new Evento(tipoEvento.CRIMINE_GESTITO,ev.getData().plusSeconds(duration),ev.getEvento()));
				break;
				
			case CRIMINE_GESTITO:
				///libero l'agente
				totale_gestiti++;
				System.out.print("cevento criminoso in "+ev.getEvento().getIncident_address()+"gestito!!");
				agenti_disponibili.put(ev.getEvento().getDistrict_id(),
						this.agenti_disponibili.get(ev.getEvento().getDistrict_id())+1);
				break;
			}
			
		}	
	}

	private double minDistance(int id_arrivo) {
		DijkstraShortestPath<Integer, DefaultWeightedEdge> dijstra = new DijkstraShortestPath<>(m.getGrafo()) ;
		double minima = 999999;
		
		//variabile per sapere da dove parte l'agente
		this.selezionato = 0;
		for(int i=1;i<8;i++) {
			if(id_arrivo!=i && dijstra.getPath(i,id_arrivo).getWeight()<minima) {
				minima = dijstra.getPath(i,id_arrivo).getWeight();
				this.selezionato = i;
			}
		}
		return minima;
	}

	private boolean agentInDistrict(int id) {
		boolean bx = false;
		if(agenti_disponibili.get(id)>=1)
			bx = true;
		
		return bx;
	}

	private int getProbabilità() {
		// TODO Auto-generated method stub
		Random r = new Random();
		return r.nextInt(11);
	}
	
	
	private int disponibili(){
		
		int disponibili=0;
		for(Integer temp: agenti_disponibili.values()) {
			
		 disponibili= disponibili+temp;
		}
		
		return disponibili;
	}
	
	public String getResult() {
		String s = "EVENTI GESTITI= "+totale_gestiti+"\n MAL_GESTITI= "+mal_gestiti+"\n BEN_GESTITI= "+gestiti_correttamente;
		return s;
	}
}



