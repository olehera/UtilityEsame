package utilityesame;

import java.time.LocalDateTime;

public class Evento implements Comparable<Evento> {
	
	public enum TipoEvento {
		PRIMO,
		SECONDO
	}
	
	private LocalDateTime tempo;
	private TipoEvento tipo;
	private Event event;
	
	public Evento(LocalDateTime tempo, TipoEvento tipo, Event event) {
		this.tempo = tempo;
		this.tipo = tipo;
		this.event = event;
	}

	public LocalDateTime getTempo() {
		return tempo;
	}

	public TipoEvento getTipo() {
		return tipo;
	}

	public Event getEvent() {
		return event;
	}
	
	@Override
	public int compareTo(Evento ev) {
		return this.tempo.compareTo(ev.tempo);
	}

}
