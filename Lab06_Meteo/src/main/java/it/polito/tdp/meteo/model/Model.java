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
	List<String> s = new ArrayList<>();
	List<String> soluzione = new ArrayList<>();
	int costoMinimo = 100*15;
	// of course you can change the String output with what you think works best
	public List<String> trovaSequenza(int mese, List<Rilevamento> r, int contatore, int n_cambi) {
		
		if(contatore > NUMERO_GIORNI_TOTALI)
			return null;
		
		if(contatore == NUMERO_GIORNI_TOTALI) {
			int costo;
			for(int i=0; i<r.size(); i++)
			{
				for(int j=0; j<s.size(); j++)
				{
					if(r.get(i).getLocalita().equals(s.get(i)) && r.get(i).getData().getDay() == j)
						costo = costo + r.get(i).getUmidita(); 
				}
				
			}
			costo = costo + 100*n_cambi;
			if(costo < costoMinimo)
				soluzione = s;
			return s;
		}
		
	    this.contatoreGiorniPerCitta ++;
		contatore++;
		
		return s;
	}
	

}
