package com.techelevator;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface CampgroundDAO {

	public List <Campground> getCampgroundById(Long id);
	public List <Campground> searchCampgroundByName(String nameSearch);
	public List <Campground> searchCampgroundByOpenMonth(LocalDate date);
	public List <Campground> searchCampgroundByClosingMonth(LocalDate date);
	public List <Campground> searchCampgroundByDailyFee(BigDecimal dailyFee);
	
	}
