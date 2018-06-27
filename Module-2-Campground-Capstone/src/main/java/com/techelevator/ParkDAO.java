package com.techelevator;

import java.util.List;


public interface ParkDAO {

	public List<Park> getAllAvailableParks();
	public List<Park> searchParksByName(String nameSearch);
	public List<Park> searchParkByLocation(String locationSearch); 
	public Park getParkById(Long id);
	public Park createPark(Park updatedPark);
	public void savePark(Park updatedPark);	
}
