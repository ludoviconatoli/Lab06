package it.polito.tdp.meteo.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

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

	public String getAllRilevamentiLocalitaMese(int mese/*, String localita*/) {
		String sql = "SELECT s.Localita, AVG(s.Umidita) AS media "
				+ "FROM situazione s "
				+ "WHERE MONTH(s.`Data`) = ? "
				+ "GROUP BY s.Localita";
		
		String s = new String();
		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, mese);
			
			ResultSet rs = st.executeQuery();
			while(rs.next()) {
				s = s + rs.getString("Localita") +" " + String.valueOf(rs.getDouble("media")) +"\n";
			}
			rs.close();
			st.close();
			conn.close();
		}catch(SQLException e) {
			System.out.println("Errore nella query");
		}
		
		return s;
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
}
