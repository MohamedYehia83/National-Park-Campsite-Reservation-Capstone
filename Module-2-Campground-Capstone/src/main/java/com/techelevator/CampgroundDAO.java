package com.techelevator;

import java.util.Map;

public interface CampgroundDAO {

	 public Map<String, Campground> mapsCampgroundNameToCampground();

	    public void displayParkCampgrounds(String name);

	    public boolean inquireReservation(String parkName);

	    public boolean isOpen(Campground campground, String arrivalDate, String departDate);

	
	}
