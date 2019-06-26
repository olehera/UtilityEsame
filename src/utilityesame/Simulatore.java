package utilityesame;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Random;

public class Simulatore {
	
private PriorityQueue<Evento> queue;
	
	private Graph<Integer, DefaultWeightedEdge> grafo;
	private Random rand;

	public void init(List<Event> eventi, Graph<Integer, DefaultWeightedEdge> grafo) {
		
		queue = new PriorityQueue<>();
		rand = new Random();
		
		this.grafo = grafo;
		
		this.queue.clear();
				
		for (Event e: eventi) 
			queue.add(new Evento(e.getReported_date(), TipoEvento.CRIME, e));
		
	}

	public void run() {
		
		while ( !queue.isEmpty() ) {     
			Evento ev = queue.poll();
			
			switch (ev.getTipo()) {
			
			case PRIMO:
					
					queue.add(new Evento(ev.getTempo().plusMinutes((long)(tempoImpiegato*60)), TipoEvento.FREE, a));
				
				break;
				
			case SECONDO:
				
				break;
			}
		}
		
	}
	
	public double distanza(int d1, int d2) {
		if (d1 != d2)
			return grafo.getEdgeWeight(grafo.getEdge(d1, d2));
		else
			return 0;
	}

}