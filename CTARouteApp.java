package project;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Scanner;

// CTARouteApp is the application class of the program
// Functions: as described in the project design
// Author: Tien Tran
// Date: 25/Apr/2018

public class CTARouteApp {

	// cta will have 8 lines
	public static CTARoute[] cta = new CTARoute[8];
	static Scanner input = new Scanner(System.in);
	public static String[] label = {"Red", "Green", "Blue", "Brown", "Purple", "Pink", "Orange", "Yellow"};
	
	// This method read input from a file
	public static void readInput(String filename) {
		
		// Give each line a name using the label array
		for (int i = 0; i < cta.length; i++) {
			CTARoute route = new CTARoute(label[i]);
			cta[i] = route;
		}
		
		File f = new File(filename);
		try {
			Scanner in = new Scanner(f);
			// Skip the first 2 lines because no station information on that line
			String firstLine = in.nextLine();
			String secondLine = in.nextLine();
			while (in.hasNextLine()) {
				// Split the line into an array of strings then parse it to the suitable type
				String[] line = in.nextLine().split(",");
				CTAStation temp = new CTAStation();
				temp.setName(line[0]);
				temp.setLat(Double.parseDouble(line[1]));
				temp.setLong(Double.parseDouble(line[2]));
				temp.setLocation(line[3]);
				temp.setWheelchair(Boolean.parseBoolean(line[4]));
				int[] pos = new int[8];
				for (int i=0; i<8; i++) {
					pos[i] = Integer.parseInt(line[i+5]);
				}
				temp.setPos(pos);
				for (int i=0; i< cta.length; i++) {
					if (pos[i] >= 0) {
						cta[i].addStation(temp);
					}
				}
			}
			in.close();
			
		} catch(Exception e) {
			System.out.println(e.toString());
		}
	}
	
	// This method sort stations according to their position on each line
	public static void insertionSort() {
		// Sort all 8 cta lines
		for (int i=0; i<cta.length; i++) {
			ArrayList<CTAStation> line = cta[i].getStops();
			for (int j = 1; j < cta[i].length(); j++) {
				int k = j;
				while (k>0 && line.get(k).getPos()[i] < line.get(k-1).getPos()[i]) {
					CTAStation s1 = line.get(k);
					CTAStation s2 = line.get(k-1);
					cta[i].swapStation(s1, s2);
					k--;
				}
			}
		}
	}
	
	// Update the position of each route based on the current order on the ArrayList
	public static void updatePosition() {
		for (int i=0; i<cta.length; i++) {
			ArrayList<CTAStation> line = cta[i].getStops();
			for (CTAStation s:line) {
				if (s.getPos()[i] != line.indexOf(s)) {
					int[] newpos = s.getPos();
					newpos[i] = line.indexOf(s);
					s.setPos(newpos);
				}
			}
		}
	}
	
	// This method prints out the names of all stations
	public static void printStationName() {
		for (int i = 0; i < 8; i++) {
			ArrayList<CTAStation> line = cta[i].getStops();
			for (CTAStation s:line) {
				System.out.println(s.getName());
			}
		}
	}
	
	// This method prints information of one station
	public static void printStation(CTAStation s) {
		System.out.println("	"+s.getName()+" ("+s.getLat()+","+s.getLong()+")");
		System.out.println("	"+"Location: "+s.getLocation());
		System.out.println("	"+"Wheelchair: "+(s.getWheelchair()?"Yes":"No"));
	}
	
	// This method prints out information of all stations
	public static void printStationInfo() {
		for (int i = 0; i < 8; i++) {
			ArrayList<CTAStation> line = cta[i].getStops();
			String linename = cta[i].getName();
			System.out.println(linename+" Line");
			for (CTAStation s:line) {
				printStation(s);
			}
		}
	}
	
	// This method prints out stations with wheelchair access
	public static void printWheelchair() {
		for (int i = 0; i < 8; i++) {
			ArrayList<CTAStation> line = cta[i].getStops();
			String linename = cta[i].getName();
			System.out.println(linename+" Line");
			for (CTAStation s:line) {
				if (s.getWheelchair()) {
					printStation(s);
				}
			}
		}
	}
	
	// This method takes the nearestStation method from CTARoute, returns a CTAStation
	public static CTAStation findNearest(double latitude, double longitude) {
		CTAStation nearest = cta[0].nearestStation(latitude, longitude);
		double mindist = nearest.calcDistance(latitude, longitude);
		for (int i = 1; i < 8; i++) {
			if (mindist > cta[i].nearestStation(latitude, longitude).calcDistance(latitude, longitude)) {
				mindist = cta[i].nearestStation(latitude, longitude).calcDistance(latitude, longitude);
				nearest = cta[i].nearestStation(latitude, longitude);
			}
		}
		return nearest;
	}
	
	// This method prompts user for a location, then print out the nearest station
	public static void printNearest() {
		boolean running = true;
		do {
			try {
			System.out.print("Please enter the latitude of your location: ");
			double latitude = Double.parseDouble(input.nextLine());
			System.out.print("Please enter the longitude of your location: ");
			double longitude = Double.parseDouble(input.nextLine());
			running = false;
			CTAStation s = findNearest(latitude, longitude);
			printStation(s);
			} catch (Exception e) {
				System.out.println("Your input is invalid. Please try again.");
			}
		} while (running);
	}
	
	// This method find a station using its name
	public static void findStation() {
		System.out.print("Please enter the name of the station: ");
		
		// Use the method lookupStation from CTARoute
		String stationname = input.nextLine();
		boolean found = false;
		for (int i=0; i<cta.length; i++) {
			CTAStation s = cta[i].lookupStation(stationname);
			if (s != null) {
				found = true;
				String linename = cta[i].getName();
				System.out.println(linename+" Line");
				printStation(s);
			}
		}
		if (!found) {
			System.out.println("Station not found.");
		}
	}
	
	// This method takes the name and route of the station, then removes it
	public static void removeStation() {
		
		System.out.print("Please enter the name of the station: ");
		String name = input.nextLine();
		System.out.print("Please enter the route(s) it appears on, separated by a comma (,):");
		String[] line = input.nextLine().split(",");
		
		for (int i=0; i<line.length; i++) {
			for (int j=0; j<cta.length; j++) {
				if (line[i].trim().equalsIgnoreCase(cta[j].getName())) {
					CTAStation s = cta[j].lookupStation(name);
					if (s == null) {
						System.out.println(name+" is not found on the "+cta[j].getName()+" Line.");
					} else {
						String linename = cta[j].getName();
						System.out.println(linename+" Line");
						printStation(s);
						System.out.print("Are you sure you want to remove this station? (Y/N):");
						String choice = input.nextLine();
						if (choice.equalsIgnoreCase("N")) {
							System.out.println("No change was made.");
						} else if (choice.equalsIgnoreCase("Y")) {
							cta[j].removeStation(s);
							updatePosition();
							// Update position of every station
							System.out.println("Station removed successfully.");
						} else {
							System.out.println("Invalid input. Please try again.");
						}
					}
				}
			}
		}
	}
	
	// This method prompts user for input, then add a station to the appropriate route
	public static void addStation() {
		
		System.out.print("Please enter the name of the station: ");
		String name = input.nextLine();
		System.out.print("Please enter the latitude of the station: ");
		double lat = Double.parseDouble(input.nextLine());
		System.out.print("Please enter the longitude of the station: ");
		double lng = Double.parseDouble(input.nextLine());
		System.out.print("Please enter the location of the station: ");
		String location = input.nextLine();
		
		boolean wheelchair = false;
		boolean stopped = false;
		do {
			System.out.print("Does the station have wheelchair access? (Y/N): ");
			String check = input.nextLine();
			if (check.equalsIgnoreCase("y")) {
				wheelchair = true;
			}
			if (check.equalsIgnoreCase("y") || check.equalsIgnoreCase("n")) {
				stopped = true;
			}
		} while (!stopped);
		CTAStation s = new CTAStation(lat,lng);
		s.setName(name);
		s.setLocation(location);
		s.setWheelchair(wheelchair);
		int[] pos = {-1,-1,-1,-1,-1,-1,-1,-1};
		s.setPos(pos);
		
		System.out.print("Please enter the route(s) it appears on, separated by a comma (,):");
		String[] line = input.nextLine().split(",");
		for (int i=0; i<line.length; i++) {
			for (int j=0; j<cta.length; j++) {
				if (line[i].trim().equalsIgnoreCase(cta[j].getName())) {
					System.out.println(cta[j].getName()+" Line");
					boolean running = true;
					do {
						System.out.print("Please enter the name of previous station: ");
						String name1 = input.nextLine();
						System.out.print("Please enter the name of the following station: ");
						String name2 = input.nextLine();
						CTAStation s1 = cta[j].lookupStation(name1);
						CTAStation s2 = cta[j].lookupStation(name2);
						if (cta[j].getStops().indexOf(s2) == 0) {
							int position = cta[j].getStops().indexOf(s2);
							cta[j].insertStation(position, s);
							System.out.println("Station added successfully.");
							running = false;
						} else if (cta[j].getStops().indexOf(s1) == cta[j].length()-1) {
							cta[j].addStation(s);
							System.out.println("Station added successfully.");
							running = false;
						} else if (cta[j].getStops().indexOf(s2)-cta[j].getStops().indexOf(s1) == 1) {
							int position = cta[j].getStops().indexOf(s2);
							cta[j].insertStation(position, s);
							System.out.println("Station added successfully.");
							running = false;
						} else {
							System.out.println("The input is not correct.");
						}
					} while (running);
				}
			}
		}
		updatePosition();
	}
	
	// This method modifies prompts user for a station, displays it, and modifies it if the user requires
	public static void modifyStation() {
		
		System.out.print("Please enter the name of the station: ");
		String name = input.nextLine();
		System.out.print("Please enter the route(s) it appears on, separated by a comma (,):");
		String[] line = input.nextLine().split(",");
		
		for (int i=0; i<line.length; i++) {
			for (int j=0; j<cta.length; j++) {
				if (line[i].trim().equalsIgnoreCase(cta[j].getName())) {
					CTAStation temp = cta[j].lookupStation(name);
					if (temp == null) {
						System.out.println(name+" is not found on the "+cta[j].getName()+" Line.");
					} else {
						String linename = cta[j].getName();
						System.out.println(linename+" Line");
						printStation(temp);
						System.out.print("Are you sure you want to modify this station? (Y/N):");
						String choice = input.nextLine();
						if (choice.equalsIgnoreCase("N")) {
							System.out.println("No change was made.");
						} else if (choice.equalsIgnoreCase("Y")) {
							System.out.print("Please enter the latitude of the station: ");
							double lat = Double.parseDouble(input.nextLine());
							System.out.print("Please enter the longitude of the station: ");
							double lng = Double.parseDouble(input.nextLine());
							System.out.print("Please enter the location of the station: ");
							String location = input.nextLine();
							
							boolean wheelchair = false;
							boolean stopped = false;
							do {
								System.out.print("Does the station have wheelchair access? (Y/N): ");
								String check = input.nextLine();
								if (check.equalsIgnoreCase("y")) {
									wheelchair = true;
								}
								if (check.equalsIgnoreCase("y") || check.equalsIgnoreCase("n")) {
									stopped = true;
								}
							} while (!stopped);

							CTAStation s = new CTAStation(lat,lng);
							s.setName(name);
							s.setLocation(location);
							s.setWheelchair(wheelchair);
							int[] pos = {-1,-1,-1,-1,-1,-1,-1,-1};
							s.setPos(pos);
							int position = cta[j].getStops().indexOf(temp);
							cta[j].insertStation(position, s);
							cta[j].removeStation(temp);
							System.out.println("Station modified successfully.");
						} else {
							System.out.println("Invalid input. Please try again.");
						}
					}
				}
			}
		}
		updatePosition();
	}
	
	// This method return the line that both 2 stations are on, return -1 if they don't be on any same line
	public static int sameLine(CTAStation s1, CTAStation s2) {
		for (int i=0; i<cta.length; i++) {
			if (s1.getPos()[i] >=0 && s2.getPos()[i] >=0) {
				return i;
			}
		}
		return -1;
	}
	
	// This method returns true if a station is on multiple lines, returns false otherwise
	public static boolean multipleLine(CTAStation s) {
		int count = 0;
		for (int i=0; i<cta.length; i++) {
			if (s.getPos()[i] >= 0) {
				count++;
			}
		}
		if (count > 1) {
			return true;
		} else {
			return false;
		}
	}
	
	// Prompt the users for 2 stations, then generate routes using following methods
	public static void findRoute() {
		CTAStation s1 = new CTAStation();
		CTAStation s2 = new CTAStation();
		System.out.print("Please enter your initial station: ");
		String name1 = input.nextLine();
		for (int m=0; m<cta.length; m++) {
			if (cta[m].lookupStation(name1) != null) {
				s1 = cta[m].lookupStation(name1);
				break;
			}
		}
		System.out.print("Please enter your terminal station: ");
		String name2 = input.nextLine();
		for (int m=0; m<cta.length; m++) {
			if (cta[m].lookupStation(name2) != null) {
				s2 = cta[m].lookupStation(name2);
				break;
			}
		}
		if (sameLine(s1,s2)>=0) {
			generateRoute(s1,s2);
		} else {
			transferRoute(s1,s2);
		}
	}
	
	// Generate and print out a route between 2 stations on the same CTA line
	public static void generateRoute(CTAStation s1, CTAStation s2) {
		int i = sameLine(s1,s2);
		ArrayList<CTAStation> line = cta[i].getStops();
		String linename = cta[i].getName();
			
		System.out.println(linename+" Line");
		int start = s1.getPos()[i];
		int end = s2.getPos()[i];
		if (start < end) {
			for (int j = start; j<=end; j++) {
				printStation(line.get(j));
			}
		} else {
			for (int j = start; j>=end; j--) {
				printStation(line.get(j));
			}
		}
	}
	
	// This method find a route between 2 stations and print out the route
	// May generate route up to 3 transfers 
	public static void transferRoute(CTAStation s1, CTAStation s2) {
		boolean stop = false;
		do {
			for (int i=0; i<cta.length; i++) {
				int[] pos1 = s1.getPos();
				if (pos1[i] >= 0) {
					for (CTAStation s: cta[i].getStops()) {
						if (sameLine(s,s2)>=0 && multipleLine(s)) {
							generateRoute(s1,s);
							generateRoute(s,s2);
							return;
						}
					}
					for (CTAStation s: cta[i].getStops()) {
						if (multipleLine(s)) {
							int[] pos2 = s2.getPos();
							for (int j=0; j<cta.length; j++) {
								if (pos2[j] >=0) {
									for (CTAStation s3: cta[j].getStops()) {
										if (sameLine(s,s3)>=0 && multipleLine(s3)) {
											generateRoute(s1,s);
											generateRoute(s,s3);
											generateRoute(s3,s2);
											return;
										}
									}
									for (CTAStation s3: cta[j].getStops()) {
										if (multipleLine(s3)) {
											for (int k = cta.length-1; k>=j; k--) {
												for (CTAStation s4: cta[k].getStops()) {
													if (sameLine(s,s4)>=0 && sameLine(s4,s3)>=0 && multipleLine(s4)) {
														generateRoute(s1,s);
														generateRoute(s,s4);
														generateRoute(s4,s3);
														generateRoute(s3,s2);
														return;
													}
												}
											}
										}
									}
								}
							}
						}
					}
				}
			}
		} while (!stop);
	}
	
	// This method saves any changes to an output file entered by the user
	public static void printToFile() {
		
		boolean running = true;
		do {
			
			System.out.print("Do you want to save your changes to a file? (Y/N): ");
			String choice = input.nextLine();
			if (choice.equalsIgnoreCase("N")) {
				System.out.println("Your changes were not saved.");
				System.out.println("Goodbye!");
				running = false;
			} else if (choice.equalsIgnoreCase("Y")) {
				
				System.out.print("Please enter your output file: ");
				String filename = input.nextLine();
				try {
					
					FileWriter f = new FileWriter(filename);
					f.write("Name,Latitude,Longitude,Location,Wheelchair,Red,Green,Blue,Brown,Purple,Pink,Orange,Yellow"+"\n");
					f.write("Null,Null,Null,Null,Null,Null,Null,Null,Merchandise Mart,Merchandise Mart,Clinton,Roosevelt,Null"+"\n");
					for (int i=0; i<cta.length; i++) {
						for (CTAStation s:cta[i].getStops()){
							int[] pos = s.getPos();
							int first = 0;
							for (int j=0; j<pos.length; j++) {
								if (pos[j]>=0) {
									first = j;
									break;
								}
							}
							if (!multipleLine(s)) {
								f.write(s.getName());
								f.write(","+s.getLat());
								f.write(","+s.getLong());
								f.write(","+s.getLocation());
								f.write(","+s.getWheelchair());
								for (int j=0; j<pos.length; j++) {
									f.write(","+pos[j]);
								}
								f.write("\n");
							} 
							// If s is on multipleLine only print information from the first line it is on
							else if (multipleLine(s) && i == first ) {
								f.write(s.getName());
								f.write(","+s.getLat());
								f.write(","+s.getLong());
								f.write(","+s.getLocation());
								f.write(","+s.getWheelchair());
								for (int j=0; j<pos.length; j++) {
									f.write(","+pos[j]);
								}
								f.write("\n");
							}
						}
					}
					f.flush();
					f.close();
				} catch (Exception e) {
					System.out.println(e.toString());
				}
				System.out.println("Saved successfully.");
				System.out.println("Goodbye!");
				running = false;
				
			} else {
				System.out.println("Your input was invalid. Please try again.");
		}
		} while (running);
	}
	
	// Main method
	public static void main(String[] args) {
		readInput("project/CTAStops.csv");
		insertionSort();
		boolean exit = false;
		do {
			System.out.println("********MENU*********");
			System.out.println("1. Display name of all stations");
			System.out.println("2. Display information of stations with wheelchair access");
			System.out.println("3. Search for a station");
			System.out.println("4. Add a new station");
			System.out.println("5. Modify an existing station");
			System.out.println("6. Delete an existing station");
			System.out.println("7. Find the nearest station to a location");
			System.out.println("8. Find a route between two stations");
			System.out.println("9. Exit ");
			System.out.print("Please enter your choice: ");
			try {
				int choice = Integer.parseInt(input.nextLine());
				switch (choice) {
				case 1:
					printStationName();
					break;
				case 2:
					printWheelchair();
					break;
				case 3:
					findStation();
					break;
				case 4:
					addStation();
					break;
				case 5:
					modifyStation();
					break;
				case 6:
					removeStation();
					break;
				case 7:
					printNearest();
					break;
				case 8:
					findRoute();
					break;
				case 9:
					exit = true;
					printToFile();
					break;
				default:
					System.out.println("Sorry, I do not understand your input, please try again.");
				}
		} catch(Exception e) {
			System.out.println("Sorry, I do not understand your input, please try again.");
		}
		} while (!exit);
		input.close();

	}

}
