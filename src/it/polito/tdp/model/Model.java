package it.polito.tdp.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import com.javadocmd.simplelatlng.LatLng;
import com.javadocmd.simplelatlng.LatLngTool;
import com.javadocmd.simplelatlng.util.LengthUnit;

import it.polito.tdp.db.EventsDao;

public class Model {
	
	private EventsDao edao ;
	private Graph<Integer,DefaultWeightedEdge> grafo ;
	private List<Integer> distretti;
	
	
	public Model() {
		edao = new EventsDao();
		grafo = new SimpleWeightedGraph<Integer,DefaultWeightedEdge>(DefaultWeightedEdge.class);
		
	}
	
	//metodo di creazione del grafo da lincare al controller
	public void creaGrafo(int anno) {
		for(int i = 1; i<8 ; i++) {
			grafo.addVertex(i);
		}
		
		//aggiungo archi
		for(int i = 1; i<8 ; i++) {
			for(int j = 1; j<8 ; j++) {
				if(i!=j) {
				grafo.addEdge(i, j);
				LatLng dist1 = new LatLng(edao.getAvgLat(anno,i),edao.getAvgLon(anno,i));
				LatLng dist2 = new LatLng(edao.getAvgLat(anno,j),edao.getAvgLon(anno,j));
				double peso = LatLngTool.distance(dist1,dist2,LengthUnit.KILOMETER);
				grafo.setEdgeWeight(i,j,peso);
				}
				
			}
		}	
	}
	
	public String vicinato() {
		String vici = "";
		for(int i = 1; i<8 ; i++) {
			vici = vici+"\n VICINI DI "+i+":";
			for(Vicino temp : getVicini(i)) {
				vici = vici+" "+temp.toString()+"; ";
			}
		}
		return vici;	
	}
	
	
	public List<Vicino> getVicini(int distretto) {
		List<Vicino> miei_vicini = new ArrayList<Vicino>();
		List<Integer> id_vicini = new ArrayList<Integer>(Graphs.neighborSetOf(grafo, distretto));
		for(Integer temp: id_vicini) {
			double distance = grafo.getEdgeWeight(grafo.getEdge(distretto,temp));
			Vicino vx = new Vicino(temp,distance);
			miei_vicini.add(vx);
		}
		Collections.sort(miei_vicini, new SortDistanza());
		return miei_vicini;
	}
}
