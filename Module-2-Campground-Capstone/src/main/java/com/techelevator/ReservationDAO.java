package com.techelevator;

import java.util.Date;

public interface ReservationDAO {
	
	public boolean setReservation(Campground campground, Date arrivalDate, Date departDate);
	
}
