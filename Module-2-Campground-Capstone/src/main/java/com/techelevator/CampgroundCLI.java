package com.techelevator;

import javax.sql.DataSource;

import org.apache.commons.dbcp2.BasicDataSource;

public class CampgroundCLI {

	private static final String VIEW_PARKS_OPTION_ACADIA = "Acadia";
    private static final String VIEW_PARKS_OPTION_ARCHES = "Arches";
    private static final String VIEW_PARKS_OPTION_CUYAHOGA = "Cuyahoga Valley";
    private static final String VIEW_PARKS_OPTION_QUIT = "Quit";
    private static final String[] VIEW_PARKS_OPTIONS = { VIEW_PARKS_OPTION_ACADIA, 
                                                        VIEW_PARKS_OPTION_ARCHES,
                                                        VIEW_PARKS_OPTION_CUYAHOGA,
                                                        VIEW_PARKS_OPTION_QUIT };
    
    private static final String PARK_INFO_OPTION_VIEW_CAMPGROUNDS = "View Campgrounds";
    private static final String PARK_INFO_OPTION_SEARCH_FOR_RESERVATION = "Search for Reservation";
    private static final String RETURN_TO_PREVIOUS_SCREEN = "Return to Previous Screen";
    private static final String[] PARK_INFO_OPTIONS = { PARK_INFO_OPTION_VIEW_CAMPGROUNDS,
                                                        PARK_INFO_OPTION_SEARCH_FOR_RESERVATION,
                                                        RETURN_TO_PREVIOUS_SCREEN };
    
    private static final String PARK_CAMPGROUNDS_OPTION_SEARCH_FOR_AVAILABLE_RESERVATION = "Search for Available Reservation";
    private static final String[] PARK_CAMPGROUNDS_OPTIONS = { PARK_CAMPGROUNDS_OPTION_SEARCH_FOR_AVAILABLE_RESERVATION,
                                                                RETURN_TO_PREVIOUS_SCREEN };
                                                        
    
    private static BasicDataSource dataSource = new BasicDataSource();
    private static JDBCCampgroundDAO cgDao;
    private Menu menu;
	
	public static void main(String[] args) {
		BasicDataSource dataSource = new BasicDataSource();
		dataSource.setUrl("jdbc:postgresql://localhost:5432/nationalparkcampsites");
		dataSource.setUsername("postgres");
		dataSource.setPassword("postgres1");
		
		CampgroundCLI application = new CampgroundCLI(dataSource);
		application.run();
	}

	public CampgroundCLI(DataSource nationalparkcampsites) {
		cgDao = new JDBCCampgroundDAO(nationalparkcampsites);
        this.menu = new Menu(System.in, System.out);
	}
	
	public void run() {
		boolean isRunning = true;
        while(isRunning) {
            System.out.println("Select a park for further details:");
            String choice = (String)menu.getChoiceFromOptions(VIEW_PARKS_OPTIONS);
            
            boolean loop = true;
            while (!choice.equals(VIEW_PARKS_OPTION_QUIT) && loop) {
                displayParkInfoScreen(choice);
                System.out.println("\nSelect a Command:");
                String choice2 = (String)menu.getChoiceFromOptions(PARK_INFO_OPTIONS);
            
                if (choice2.equals(PARK_INFO_OPTION_VIEW_CAMPGROUNDS)) {
                    displayCampgroundsScreen(choice);
                    System.out.println("\nSelect a Command:");
                    String choice3 = (String)menu.getChoiceFromOptions(PARK_CAMPGROUNDS_OPTIONS);
                    
                    if (choice3.equals(PARK_CAMPGROUNDS_OPTION_SEARCH_FOR_AVAILABLE_RESERVATION)) {
                        cgDao.inquireReservation(choice);
                        loop = false;
                    }
                    
                } else if (choice2.equals(PARK_INFO_OPTION_SEARCH_FOR_RESERVATION)) {
                    displayCampgroundsScreen(choice);
                    cgDao.inquireReservation(choice);
                    loop = false;
                } else if (choice2.equals(RETURN_TO_PREVIOUS_SCREEN)) {
                    loop = false;
                }
            } 
            if (choice.equals(VIEW_PARKS_OPTION_QUIT)) {
                isRunning = false;
            }
            
        }
    }
    
    
    public void displayParkInfoScreen(String choice) {
        JDBCParkDAO park = new JDBCParkDAO(dataSource);
        if(choice.equals(VIEW_PARKS_OPTION_ACADIA)) {
            park.displayParkInfo(VIEW_PARKS_OPTION_ACADIA);
        } else if (choice.equals(VIEW_PARKS_OPTION_ARCHES)) {
            park.displayParkInfo(VIEW_PARKS_OPTION_ARCHES);
        } else if (choice.equals(VIEW_PARKS_OPTION_CUYAHOGA)) {
            park.displayParkInfo(VIEW_PARKS_OPTION_CUYAHOGA);
        }
    }
    
    public void displayCampgroundsScreen(String choice) {
        JDBCCampgroundDAO cgDao = new JDBCCampgroundDAO(dataSource);
        cgDao.displayParkCampgrounds(choice);
    }
    
    



	}

