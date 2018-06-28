package com.techelevator;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.activation.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;


public class JDBCParkDAO implements ParkDAO {

	private JdbcTemplate jdbcTemplate;

	public JDBCParkDAO(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	@Override
	public Map<String, Park> getParks() {
		Map<String, Park> parks = new HashMap<String, Park>();
		
		String sqlGetAllAvailableParks = "SELECT * FROM park ORDER BY name";
		SqlRowSet results = jdbcTemplate.queryForRowSet(sqlGetAllAvailableParks );
		while(results.next()) {
            Park park = new Park();
            park.setPark_id(results.getLong("park_id"));
            park.setName(results.getString("name"));
            park.setLocation(results.getString("location"));
            park.setEstablish_date(results.getDate("establish_date").toString());
            park.setArea(results.getInt("area"));
            park.setVisitors(results.getInt("visitors"));
            park.setDescription(results.getString("description"));
            
        
            parks.put(park.getName(), park);
        }
        
        return parks;

	}

	@Override
	public void displayParkInfo(String choice) {
		String sql = "SELECT * FROM park WHERE name = ?;";
		SqlRowSet results = jdbcTemplate.queryForRowSet(sql, choice);
		Park park = getParks().get(choice);

		System.out.println("\n\n");
		System.out.println("PARK INFORMATION:\n");
		System.out.println(park.getName() + " National Park");
		System.out.println(String.format("%-20s%s", "Location:", park.getLocation()));
		System.out.println(String.format("%-20s%s", "Established:", park.getEstablish_date()));
		System.out.println(String.format("%-20s%s sq km", "Area:", park.getArea()));
		System.out.println(String.format("%-20s%s", "Annual Visitors:", park.getVisitors()));
		System.out.println("\n" + park.getDescription());

	}

	private Park mapRowToPark(SqlRowSet results) {
		Park newPark;
		newPark = new Park();
		newPark.setPark_id(results.getLong("park_id"));
		newPark.setName(results.getString("name"));

		return newPark;
	}
}
