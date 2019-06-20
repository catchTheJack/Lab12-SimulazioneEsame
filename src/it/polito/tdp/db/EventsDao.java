package it.polito.tdp.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import it.polito.tdp.model.Event;


public class EventsDao {
	
	public List<Event> listAllEventsByDate(int anno,int mese,int giorno){
		String sql = "SELECT * FROM events WHERE YEAR(reported_date)= ? AND MONTH(reported_date)=? AND DAY(reported_date)=?" ;
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			List<Event> list = new ArrayList<>() ;
			
			st.setInt(1, anno);
			st.setInt(2, mese);
			st.setInt(3, giorno);
			
			ResultSet res = st.executeQuery() ;
			
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
			return list ;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null ;
		}
	}
	
	//____________________________________
	//METODI PER LA RESTITUZIONE DELLA MEDIA DELLE LATITUDINI E LONGITUDINI
	
	public Double getAvgLat(int anno, int id){
		String sql = "SELECT AVG(geo_lat) AS latavg  "
				+ "FROM events "
				+ "WHERE district_id=? AND YEAR(reported_date)=? AND is_crime=1";
		try {
			Connection conn = DBConnect.getConnection() ;
			
		
			
			PreparedStatement st = conn.prepareStatement(sql) ;
			st.setInt(1, id);
			st.setInt(2, anno);
			ResultSet res = st.executeQuery() ;
			
			if(res.next()) {
				try {
					conn.close();
					return res.getDouble("latavg");
					
				} catch (Throwable t) {
					t.printStackTrace();
					System.out.println(res.getInt("id"));
				}
			}
			
			conn.close();
			return null ;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null ;
		}
	}
	
	
	public Double getAvgLon(int anno, int id){
		String sql = "SELECT AVG(geo_lon) AS lonavg  "
				+ "FROM events "
				+ "WHERE district_id=? AND YEAR(reported_date)=? AND is_crime=1";
		try {
			Connection conn = DBConnect.getConnection() ;
			
			
			PreparedStatement st = conn.prepareStatement(sql) ;
			st.setInt(1, id);
			st.setInt(2, anno);
			ResultSet res = st.executeQuery() ;
			
			if(res.next()) {
				try {
					conn.close();
					return res.getDouble("lonavg");
					
				} catch (Throwable t) {
					t.printStackTrace();
					System.out.println(res.getInt("id"));
				}
			}
			
			conn.close();
			return null;
			

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null ;
		}
	}

	public Integer getPoliceStation(int anno) {
		
		String sql = "SELECT district_id, COUNT(*) AS conteggio "
				+ "FROM events "
				+ "WHERE YEAR(reported_date)=? "
				+ "GROUP BY district_id "
				+ "ORDER BY COUNT(*) asc";
		try {
			Connection conn = DBConnect.getConnection() ;
			
			
			PreparedStatement st = conn.prepareStatement(sql) ;
			st.setInt(1, anno);
			ResultSet res = st.executeQuery() ;
			
			if(res.next()) {
				try {
					conn.close();
					return res.getInt("district_id");
					
				} catch (Throwable t) {
					t.printStackTrace();
					System.out.println(res.getInt("id"));
				}
			}
			
			conn.close();
			return null;
			

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null ;
		}
		
	}
}
