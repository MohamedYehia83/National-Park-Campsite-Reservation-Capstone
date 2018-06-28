package com.techelevator;


import java.util.Map;


public interface ParkDAO {

	public Map<String, Park> getParks();
	public void displayParkInfo(String choice);
}
