package com.techelevator;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import javax.sql.DataSource;

import org.springframework.jdbc.InvalidResultSetAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

public class JDBCCampgroundDAO implements CampgroundDAO {
    
    private static final String ACADIA_OPTION_BLACKWOODS = "Blackwoods";
    private static final String ACADIA_OPTION_SCHOODIC_WOODS = "Schoodic Woods";
    private static final String ACADIA_OPTION_SEAWALL = "Seawall";
    private static final String[] ACADIA_OPTIONS = { ACADIA_OPTION_BLACKWOODS, ACADIA_OPTION_SCHOODIC_WOODS, 
                                                    ACADIA_OPTION_SEAWALL};
    
    private static final String ARCHES_OPTION_DEVILS_GARDEN = "Devil's Garden";
    private static final String ARCHES_OPTION_CANYON_WREN = "Canyon Wren Group Site";
    private static final String ARCHES_OPTION_JUNIPER = "Juniper Group Site";
    private static final String[] ARCHES_OPTIONS = { ARCHES_OPTION_DEVILS_GARDEN, ARCHES_OPTION_CANYON_WREN,
                                                        ARCHES_OPTION_JUNIPER };
    
    private static final String[] CUYAHOGA_OPTIONS = { "The Unnamed Primitive Campsites" };

    private JdbcTemplate jdbcTemplate;
    private JDBCCampsiteDAO siteDao;
    private JDBCReservationDAO reservationDao;
    public JDBCCampgroundDAO(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
        siteDao = new JDBCCampsiteDAO(dataSource);
        reservationDao = new JDBCReservationDAO(dataSource);
    }

    
    public Map<String, Campground> mapsCampgroundNameToCampground() {

        Map<String, Campground> campgrounds = new HashMap<String, Campground>();
        
        String sql = "SELECT * FROM campground";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql);
        
        while(results.next()) {
            Campground campground = new Campground();
            campground.setCampground_id(results.getLong("campground_id"));
            campground.setPark_id(results.getLong("park_id"));
            campground.setName(results.getString("name"));
            campground.setOpen_from_mm(results.getString("open_from_mm"));
            campground.setOpen_to_mm(results.getString("open_to_mm"));
            campground.setDaily_fee(results.getDouble("daily_fee"));
            
        
            campgrounds.put(campground.getName(), campground);
        }
        
        return campgrounds;
    }
    
    public void displayParkCampgrounds(String choice) {
        String sql = "SELECT * FROM campground " +
                        "JOIN park on park.park_id = campground.park_id "+ 
                        "WHERE park.name = ?;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, choice);
        
        System.out.println("\n" + choice + " National Park Campgrounds\n");
        System.out.println(String.format("%-5s%-32s%-10s%-10s%s", " ", "Name", "Open", "Close", "Daily Fee"));
        int number = 1;
        while (results.next()) {
            String parkName = results.getString("name");
            String open = results.getString("open_from_mm");
            String close = results.getString("open_to_mm");
            double dailyFee = results.getDouble("daily_fee");

            System.out.println(String.format("#%-4d%-32s%-10s%-10s$%#.2f", number, parkName, open, close, dailyFee));
            number++;
        }
    }
    
    @Override
    public boolean inquireReservation(String parkName) {
        Campground campground = null;
        Scanner input = new Scanner(System.in);
            int userChoice;
            while (campground == null) {
                System.out.println("\nPlease enter the campground number you would like to reserve. (enter 0 to cancel)");
                userChoice = input.nextInt();
                input.nextLine();
                if (userChoice == 0) {
                    return false;
                }
                if (parkName.equals("Acadia")) {
                    campground = mapsCampgroundNameToCampground().get(ACADIA_OPTIONS[userChoice - 1]);
                } else if (parkName.equals("Arches")) {
                    campground = mapsCampgroundNameToCampground().get(ARCHES_OPTIONS[userChoice - 1]);
                } else if (parkName.equals("Cuyahoga Valley")) {
                    campground = mapsCampgroundNameToCampground().get(CUYAHOGA_OPTIONS[userChoice - 1]);
                }
            }
        boolean validDates = false;
        Date arrivalDate = null;
        String arrivalMonth = "";
        String arrivalString = "";
        String departString = "";
        Date departDate = null;
        String departMonth = "";
        
        while (validDates == false) {
            System.out.println("What is the arrival date? Enter as yyyy-MM-dd");arrivalString = input.nextLine();
            
            try {
                arrivalDate = new SimpleDateFormat("yyyy-MM-dd").parse(arrivalString);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            arrivalMonth = arrivalString.substring(5, 7);

            System.out.println("What is the departure date? Enter as yyyy-MM-dd");
            departString = input.nextLine();
            try {
            	departDate = new SimpleDateFormat("yyyy-MM-dd").parse(departString);
            	} catch (ParseException e) {
            	e.printStackTrace();
            	}
            	departMonth = departString.substring(5, 7);

            	        if (arrivalDate.after(Calendar.getInstance().getTime()) && arrivalDate.before(departDate)) {
            	            validDates = true;
            	        } else {
            	            System.out.println("*** Please enter a valid date range. ***");
            	        }
            	        
            	        if (!isOpen(campground, arrivalMonth, departMonth)) {
            	            System.out.println("*** Campground is not open during the requested dates. ***");
            	            validDates = false;
            	        }
            	    }

            	    try {
            	        siteDao.displayOpenSites(campground, arrivalDate, departDate);
            	    } catch (Exception e) {
            	        e.printStackTrace();
            	    }
            	    reservationDao.setReservation(campground, arrivalDate, departDate);
            	    
            	    return true;
            	}

            	public boolean isOpen(Campground campground, String arrivalMonth, String departMonth) {
            	    if (Integer.parseInt(campground.getOpen_from_mm()) <= Integer.parseInt(arrivalMonth)
            	            && Integer.parseInt(campground.getOpen_to_mm()) >= Integer.parseInt(departMonth)) {
            	        return true;
            	    } else {
            	        return false;
            	    }
            	}

            	}
