package it.polito.tdp.meteo.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import it.polito.tdp.meteo.model.Citta;
import it.polito.tdp.meteo.model.Rilevamento;

public class MeteoDAO {
	
	public List<Rilevamento> getAllRilevamenti() {

		final String sql = "SELECT Localita, Data, Umidita FROM situazione ORDER BY data ASC";

		List<Rilevamento> rilevamenti = new ArrayList<Rilevamento>();

		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);

			ResultSet rs = st.executeQuery();

			while (rs.next()) {

				Rilevamento r = new Rilevamento(rs.getString("Localita"), rs.getDate("Data"), rs.getInt("Umidita"));
				rilevamenti.add(r);
			}

			conn.close();
			return rilevamenti;

		} catch (SQLException e) {

			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	public List<Rilevamento> getAllRilevamentiLocalitaMese(int mese, Citta localita) {
		String sql = "SELECT s.Localita, s.Data, s.Umidita "
				+ "FROM situazione s "
				+ "WHERE MONTH(s.`Data`) = ? and s.localita = ? "
				+ "GROUP BY data ASC";
		
		List<Rilevamento> rilevamenti = new ArrayList<Rilevamento>();
		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1, Integer.toString(mese));
			st.setString(2, localita.getNome());
			
			ResultSet rs = st.executeQuery();
			while(rs.next()) {
				Rilevamento r = new Rilevamento(rs.getString("s.Localita"), rs.getDate("s.Data"), rs.getInt("s.Umidita"));
				rilevamenti.add(r);
			}
			
			rs.close();
			st.close();
			conn.close();
			return rilevamenti;
		}catch(SQLException e) {
			System.out.println("Errore nella query");
		}
		
		return null;
	}

	public List<Rilevamento> misurazionePrimiGiorni(int mese){
		String sql ="SELECT s.Localita, s.`Data`, s.Umidita "
				+ "FROM situazione s "
				+ "WHERE MONTH(s.`Data`) = ? AND DAY(s.`Data`) <= 15";
		
		List<Rilevamento> r = new LinkedList<>();
		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, mese);
			
			ResultSet rs = st.executeQuery();
			while(rs.next()) {
				r.add(new Rilevamento(rs.getString("Localita"), rs.getDate("Data"), rs.getInt("Umidita"))) ;
			}
			rs.close();
			st.close();
			conn.close();
		}catch(SQLException e) {
			System.out.println("Errore nella query");
		}
		
		return r;
	}
	
	public Double getUmiditaMedia(int mese, Citta citta) {

		final String sql = "SELECT AVG(Umidita) AS U FROM situazione " +
						   "WHERE localita=? AND MONTH(data)=? ";

		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			
			st.setString(1, citta.getNome());
			//st.setString(2, mese.getValue()); se fosse un oggetto month
			st.setString(2, Integer.toString(mese)); 

			ResultSet rs = st.executeQuery();

			rs.next(); // si posiziona sulla prima (ed unica) riga
			Double u = rs.getDouble("U");

			conn.close();
			return u;
			
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		
	}
	
	public List<Citta> getAllCitta() {

		final String sql = "SELECT DISTINCT localita FROM situazione ORDER BY localita";

		List<Citta> result = new ArrayList<Citta>();

		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);

			ResultSet rs = st.executeQuery();

			while (rs.next()) {

				Citta c = new Citta(rs.getString("localita"));
				result.add(c);
			}

			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		
	}
}
