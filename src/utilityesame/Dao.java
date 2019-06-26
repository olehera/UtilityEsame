package utilityesame;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import it.polito.tdp.extflightdelays.model.Airport;
import it.polito.tdp.extflightdelays.model.Rotta;

public class Dao {
	
	/**
	 * Lista di tutti gli eventi che si sono verificati in una certa data, ordinata per data crescente
	 * 
	 * @param data
	 * @return lista di Event
	 */
	public List<Event> listEvents(LocalDate data) {
		
		String sql = "SELECT * FROM events WHERE date(reported_date) = ? ORDER BY reported_date";
		
		List<Event> list = new ArrayList<>();
		
		try {
			Connection conn = DBConnect.getConnection();

			PreparedStatement st = conn.prepareStatement(sql);
			
			st.setDate(1, Date.valueOf(data));
			
			ResultSet res = st.executeQuery();
			
			while(res.next()) {
				try {
					list.add(new Event(res.getLong("incident_id"),
							res.getInt("offense_code"),
							res.getInt("offense_code_extension"), 
							res.getString("offense_type_id"), 
							res.getString("offense_category_id"),
							res.getTimestamp("reported_date").toLocalDateTime(),
							res.getString("incident_address"),
							res.getDouble("geo_lon"),
							res.getDouble("geo_lat"),
							res.getInt("district_id"),
							res.getInt("precinct_id"), 
							res.getString("neighborhood_id"),
							res.getInt("is_crime"),
							res.getInt("is_traffic")));
				} catch (Throwable t) {
					t.printStackTrace();
					System.out.println(res.getInt("id"));
				}
			}
			
			conn.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return list;
	}
	
	/**
	 *  Lista degli id di tutti i distretti presenti
	 */
	public List<Integer> listAllDistricts() {
		
		String sql = "SELECT DISTINCT district_id FROM events";
		
		List<Integer> list = new ArrayList<>();
		
		try {
			Connection conn = DBConnect.getConnection();

			PreparedStatement st = conn.prepareStatement(sql);
			
			ResultSet res = st.executeQuery();
			
			while(res.next()) {
				list.add(res.getInt("district_id"));
			}
			
			conn.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return list;
	}
	
	/**
	 *  Lista di tutti gli anni presenti 
	 */
	public List<Integer> listAllYears() {
		
		String sql = "SELECT DISTINCT year(reported_date) as anno FROM events ORDER BY anno";
		
		List<Integer> list = new ArrayList<>();
		
		try {
			Connection conn = DBConnect.getConnection();

			PreparedStatement st = conn.prepareStatement(sql);
			
			ResultSet res = st.executeQuery();
			
			while(res.next()) {
				list.add(res.getInt("anno"));
			}
			
			conn.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return list;
	}
	
	/**
	 * Dato il mese e l'anno torna la lista dei giorni
	 * 
	 * @param mese
	 * @param anno
	 * @return lista di giorni
	 */
	public List<Integer> listOfDays(int mese, int anno) {
		
		String sql = "SELECT DISTINCT day(reported_date) AS giorno FROM events " +
				     "WHERE month(reported_date) = ? AND year(reported_date) = ? ORDER BY giorno";
		
		List<Integer> list = new ArrayList<>();
		
		try {
			Connection conn = DBConnect.getConnection();

			PreparedStatement st = conn.prepareStatement(sql);
			
			st.setInt(1, mese);
			st.setInt(2, anno);
			
			ResultSet res = st.executeQuery();
			
			while(res.next()) {
				list.add(res.getInt("giorno"));
			}
			
			conn.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return list;
	}
	
	/**
	 * Dato l'anno ed il distretto calcola il centro geografico dei crimini del distretto 
	 * 
	 * @param anno
	 * @param district_id
	 * @return LatLng centro del distretto
	 */
	public LatLng centroDistretto(int anno, int id) {
		
		String sql = "SELECT AVG(geo_lon) as lon, AVG(geo_lat) as lat " + 
				     "FROM events " + 
				     "WHERE year(reported_date) = ? AND district_id = ? " + 
				     "GROUP BY district_id";
		
		LatLng centro = null;
		
		try {
			Connection conn = DBConnect.getConnection();

			PreparedStatement st = conn.prepareStatement(sql);
			
			st.setInt(1, anno);
			st.setInt(2, id);
			
			ResultSet res = st.executeQuery();
			
			if (res.next()) {
				centro = new LatLng(res.getDouble("lat"), res.getDouble("lon"));
			}
			
			conn.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return centro;
	}
	
	/**
	 * Dato l'anno trova il distretto con minore criminalità
	 *   
	 * @param anno
	 * @return district_id
	 */
	public int centralePolizia(int anno) {
		
		String sql = "SELECT district_id, COUNT(*) AS cnt FROM events " + 
				     "WHERE year(reported_date) = ? GROUP BY district_id";
		
		int centrale = 0;
		int cnt = 0;
		
		try {
			Connection conn = DBConnect.getConnection();

			PreparedStatement st = conn.prepareStatement(sql);
			
			st.setInt(1, anno);
			
			ResultSet res = st.executeQuery();
			
			if (res.next()) {
				centrale = res.getInt("district_id");
				cnt = res.getInt("cnt");
			}
			
			while (res.next()) {
				if (res.getInt("cnt") < cnt) {
					centrale = res.getInt("district_id");
					cnt = res.getInt("cnt");
				}	
			}
			
			conn.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return centrale;
	}

	
	while (rs.next()) {
		Airport partenza = aIdMap.get(rs.getInt("id1"));
		Airport destinazione = aIdMap.get(rs.getInt("id2"));
		
		if (partenza == null || destinazione == null) {
			throw new RuntimeException("partenza o destinazione non trovata");
		}
		
		result.add(new Rotta(partenza, destinazione, rs.getDouble("avgg")));
	}

}
