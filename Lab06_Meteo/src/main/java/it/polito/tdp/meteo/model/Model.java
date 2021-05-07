package it.polito.tdp.meteo.model;

import java.util.ArrayList;
import java.util.List;

import it.polito.tdp.meteo.DAO.MeteoDAO;

public class Model {
	
	private final static int COST = 100;
	private final static int NUMERO_GIORNI_CITTA_CONSECUTIVI_MIN = 3;
	private final static int NUMERO_GIORNI_CITTA_MAX = 6;
	private final static int NUMERO_GIORNI_TOTALI = 15;

	MeteoDAO md = new MeteoDAO();
	public Model() {

	}

	// of course you can change the String output with what you think works best
	public String getUmiditaMedia(int mese) {
		String s = "";
		
		s = md.getAllRilevamentiLocalitaMese(mese);
				
		return s;
	}
	
	int contatore = 0;
	int contatoreGiorniPerCitta = 0;
	List<String> s ;
	List<String> soluzioneMigliore ;
	int costoMinimo = 100*15;
	
	// of course you can change the String output with what you think works best
	public List<String> trovaSequenza(int mese, List<Rilevamento> r, int contatore, int costo) {
		
		s = new ArrayList<>();
		soluzioneMigliore = new ArrayList<>();
		List<Rilevamento> sol = new ArrayList<>();
		
		cerca(r, sol, contatore, costo);
		
		return soluzioneMigliore;
	}
	
	public void cerca(List<Rilevamento> r, List<Rilevamento> parziale, int contatore, int costo) {
		if(contatore > NUMERO_GIORNI_TOTALI)
			return ;
		
		if(contatore == NUMERO_GIORNI_TOTALI) {
			
			for(int i=1; i<parziale.size(); i++)
			{
				costo += parziale.get(i-1).getUmidita();
				if(!parziale.get(i-1).getLocalita().equals(parziale.get(i).getLocalita()))
					costo += 100;
			}
			
			if(costo < costoMinimo) {
				costoMinimo = costo;
				for(Rilevamento ri: parziale) {
					s.add(ri.getLocalita());
				}
				soluzioneMigliore.addAll(s);
			}
			
		}
		
		//ricorsione
		for(Rilevamento ri: r) {
			if(!parziale.contains(ri) && contatore > this.NUMERO_GIORNI_CITTA_CONSECUTIVI_MIN
					&& contatore < this.NUMERO_GIORNI_CITTA_MAX) {
				parziale.add(ri);
				contatore++;
				if(ri.getUmidita() < 100) {
					//cambio citta
				}
					//se no rimango e incremento ancora il contatore
			}
		}
	}
}
