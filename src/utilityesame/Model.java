package utilityesame;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.event.ConnectedComponentTraversalEvent;
import org.jgrapht.event.EdgeTraversalEvent;
import org.jgrapht.event.TraversalListener;
import org.jgrapht.event.VertexTraversalEvent;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.traverse.BreadthFirstIterator;

import it.polito.tdp.extflightdelays.model.Airport;
import it.polito.tdp.metroparis.model.Fermata;
import it.polito.tdp.model.Esame;

public class Model {
	
	private List<Integer> distretti;
	private List<Integer> anni;
	private EventsDao dao;
	private Graph<Integer, DefaultWeightedEdge> grafo;
	private Simulatore sim;
	
	public Model() {
		dao = new EventsDao();
		anni = new ArrayList<Integer>(dao.listAllYears());
		distretti = new ArrayList<Integer>(dao.listAllDistricts());
	}
	
	public void creaGrafo(int anno) {
		
		grafo = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		
		Graphs.addAllVertices(grafo, distretti);
		
		for (Integer partenza: distretti)
		for (Integer arrivo: distretti)
			if (!grafo.containsEdge(partenza, arrivo) && partenza != arrivo) {

				double peso = LatLngTool.distance(dao.centroDistretto(anno, partenza), dao.centroDistretto(anno, arrivo), LengthUnit.KILOMETER);
					
				Graphs.addEdge(grafo, partenza, arrivo, peso);
			}
		
		System.out.println("#vertici: " + grafo.vertexSet().size()+"\n");
		System.out.println("#archi: " + grafo.edgeSet().size()+"\n");
	}

	public List<Integer> getDistretti() {
		return distretti;
	}

	public Graph<Integer, DefaultWeightedEdge> getGrafo() {
		return grafo;
	}

	public List<Integer> getAnni() {
		return anni;
	}
	
	public List<Integer> getGiorni(int mese, int anno) {
		return dao.listOfDays(mese, anno);
	}
	
	
	
	
	
	public List<Integer> trovaAdiacenti(int distretto) {
		
		List<Integer> distretti = Graphs.neighborListOf(grafo, distretto);
		
		Collections.sort(distretti, new OrdinaDistretti(grafo, distretto));
		
		return distretti;
	}
	
	
	
	
	
	
	
	public List<Events> ottimizza(  ) {
		best = new ArrayList<>();
		
		List<Events> parziale = new ArrayList<>();
		parziale.add( );
		
		ricorsione(parziale);
		
		return best;
	}
	
	private void ricorsione(List<Condiment> parziale) {
		
		for ( Condiment condiment: grafo.vertexSet() ) 
			if ( !parziale.contains(condiment) && !grafo.containsEdge(condiment, parziale.get(parziale.size()-1)) ) {
				parziale.add(condiment);
					
				ricorsione(parziale);
					
				parziale.remove(condiment);
			}
		
		if ( sommaCal(parziale) > sommaCal(best) )
			best = new ArrayList<>(parziale);
		
	}
	
	
	
	
	
	
	
	public int simula(int n, int anno, LocalDate data) {
		
		sim = new Simulatore();
		sim.init(n, dao.centralePolizia(anno), dao.listEvents(data), grafo);
		sim.run();
		
		return sim.getEventiMalGestiti();
	}

	
	
	
	
	
	
	
	public List<Airport> trovaPercorso(Integer a1, Integer a2) {
		List<Airport> percorso = new ArrayList<Airport>();
		Airport partenza = aIdMap.get(a1);
		Airport destinazione = aIdMap.get(a2);
		
		BreadthFirstIterator<Airport, DefaultWeightedEdge> it = new BreadthFirstIterator<>(grafo, partenza);
		
		visita.put(partenza, null);
		
		it.addTraversalListener(new TraversalListener<Airport, DefaultWeightedEdge>() {
			
			@Override
			public void vertexTraversed(VertexTraversalEvent<Airport> arg0) {	
			}
			
			@Override
			public void vertexFinished(VertexTraversalEvent<Airport> arg0) {	
			}
			
			@Override
			public void edgeTraversed(EdgeTraversalEvent<DefaultWeightedEdge> ev) {	
				Airport sorgente = grafo.getEdgeSource(ev.getEdge());
				Airport destinazione = grafo.getEdgeTarget(ev.getEdge());
				
				if (!visita.containsKey(destinazione) && visita.containsKey(sorgente))
					visita.put(destinazione, sorgente);
				else if (!visita.containsKey(sorgente) && visita.containsKey(destinazione))
					visita.put(sorgente, destinazione);
			}
			
			@Override
			public void connectedComponentStarted(ConnectedComponentTraversalEvent arg0) {	
			}
			
			@Override
			public void connectedComponentFinished(ConnectedComponentTraversalEvent arg0) {	
			}
		});
		
		while (it.hasNext())
			it.next();
		
		if (!visita.containsKey(partenza) || !visita.containsKey(destinazione))
			return null;
		
		Airport step = destinazione;
		while (!step.equals(partenza)) {
			percorso.add(0, step);
			step = visita.get(step);
		}
		percorso.add(0, step); 
		
		return percorso;
	}

	
	
	
	
	
	
	public List<Fermata> trovaCamminoMinimo(Fermata partenza, Fermata arrivo) {
		DijkstraShortestPath<Fermata, DefaultWeightedEdge> dijkstra = new DijkstraShortestPath<>(grafo);
		
		GraphPath<Fermata, DefaultWeightedEdge> path = dijkstra.getPath(partenza, arrivo);
		
		return path.getVertexList();
	}
	
	
	
	
	
	
	public List<Esame> calcolaSottoinsiemeEsami(int numeroCrediti) {
		
		best = null;
		media_best = 0.0;
		Set<Esame> parziale = new HashSet<Esame>();
		
		cerca(parziale, 0, numeroCrediti);
		
		return best;
	}
	
	private void cerca(Set<Esame> parziale, int L, int m) {
		
		// casi terminali
		int crediti = sommaCrediti(parziale);
		
		if (crediti > m)
			return ;
		
		if (crediti == m) {
			double media = calcolaMedia(parziale);
			
			if (media > media_best) {
				best = new ArrayList<Esame>(parziale);
				media_best = media; 
				return ;
			} else 
				return ;
			
		}
		
		// di sicuro crediti < m
		if (L == esami.size())
			return ;
		
		
	/*     generiamo sotto-problemi
		   esami[L] è da aggiungere o no?
		   provo a non aggiungerlo          */
		cerca(parziale, L+1, m);
		
		// provo ad aggiungerlo
		parziale.add(esami.get(L));
		cerca(parziale, L+1, m);
		parziale.remove(esami.get(L));
			
	}
	
}
