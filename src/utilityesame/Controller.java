package utilityesame;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import it.polito.tdp.borders.model.Country;
import it.polito.tdp.corsi.model.Corso;

public class Controller {
	
	"abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ._" // Stringa compilatore

	private Model model;
	
	public void setModel(Model model) {
    	this.model = model;
    	box.getItems().addAll( );
    }
	
	int n = 0;
	int mese = 0;
	int giorno = 0;
	LocalDate data;

	try {
		n = Integer.parseInt(txtN.getText().trim());
	} catch(NullPointerException npe) {
		txtResult.setText("Inserisci un numero intero tra 1 e 10");
		txtN.clear();
		return ;
	}
	
	if ( n < 1 || n > 10 ) {
		txtResult.setText("Inserisci numero tra 1 e 10");
		return ;
	}
	
	Country partenza = boxNazione.getValue();
	
	if (partenza == null) {
		txtResult.setText("Devi selezionare uno stato!");
		return ;
	}
	
	try {
		data = LocalDate.of(anno, mese, giorno);
	} catch (DateTimeException e) {
		txtResult.setText("Errore nella Data selezionata!");
		return ;
	}
	
	box.setDisable(false) --> abilita

	box.setDisable(true) --> disabilita

	
	long start = System.nanoTime();
	long stop = System.nanoTime();
	System.out.format("Tempo trascorso: %f s\n", (stop-start)/1e9);

	
}