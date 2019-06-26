package utilityesame;

import java.util.Comparator;

public class Ordina implements Comparator<Integer> {
	
	private Graph<Integer, DefaultWeightedEdge> grafo;
	private int distretto;
	
	public OrdinaDistretti(Graph<Integer, DefaultWeightedEdge> grafo, int distretto) {
		this.grafo = grafo;
		this.distretto = distretto;
	}

	/**
	 *  Ordina in base alla distanza crescente 
	 */
	@Override
	public int compare(Integer d1, Integer d2) {
		return (int)(grafo.getEdgeWeight(grafo.getEdge(distretto, d1)) - grafo.getEdgeWeight(grafo.getEdge(distretto, d2)));
	}

}
