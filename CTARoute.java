package project;

import java.util.ArrayList;

// CTARoute manages stations on a CTALine (e.g. Red Line, Green Line, Blue Line)
// The variable stops of CTARoute is an ArrayList of CTAStation
// CTAStation and CTARoute have associate relationship
// Author: Tien Tran
// Date: 25/Apr/2018

public class CTARoute {

	private String name;
	private ArrayList<CTAStation> stops;
	
	// Default constructor
	public CTARoute() {
		name = "";
		stops = new ArrayList<CTAStation>();
	}
	
	// Non-default constructor
	public CTARoute(String name) {
		this.name = name;
		stops = new ArrayList<CTAStation>();
	}
	
	public CTARoute(String name, ArrayList<CTAStation> stops) {
		this.name = name;
		this.stops = stops;
	}
	
	// Getters
	public String getName() {
		return name;
	}
	
	public ArrayList<CTAStation> getStops() {
		return stops;
	}
	
	public int length() {
		return stops.size();
	}
	
	// Setters
	public void setName(String name) {
		this.name = name;
	}
	
	public void setStops(ArrayList<CTAStation> stops) {
		this.stops = stops;
	}
	
	// toString method
	public String toString() {
		String result = "";
		for (CTAStation i:stops) {
			result = result+i.toString()+"/n";
		}
		return result;
	}
	
	public void addStation(CTAStation s) {
		stops.add(s);
	}
	
	public void removeStation(CTAStation s) {
		stops.remove(s);
	}
	
	public void insertStation(int position, CTAStation s) {
		stops.add(position, s);
	}
	
	// This method look up station using name
	public CTAStation lookupStation(String name) {
		for (CTAStation s:stops) {
			if (s.getName().equalsIgnoreCase(name)){
				return s;
			}
		}
		return null;
	}
	
	// Find the nearest station to a specific location
	// Use the calcDistance method from CTAStation, inherited from GeoLocation class
	public CTAStation nearestStation(double latitude, double longitude) {
		double mindist = stops.get(0).calcDistance(latitude, longitude);
		CTAStation min = stops.get(0);
		for (CTAStation s:stops) {
			if (s.calcDistance(latitude, longitude) < mindist) {
				mindist = s.calcDistance(latitude, longitude);
				min = s;
			}
		}
		return min;
	}
	
	// Find the nearest station to a GeoLocation
	public CTAStation nearestStation(GeoLocation location) {
		double mindist = stops.get(0).calcDistance(location);
		CTAStation min = stops.get(0);
		for (CTAStation s:stops) {
			if (s.calcDistance(location) < mindist) {
				mindist = s.calcDistance(location);
				min = s;
			}
		}
		return min;
	}
	
	// Swap positions of 2 stations on a line
	public void swapStation(CTAStation s1, CTAStation s2) {
		int pos1 = stops.indexOf(s1);
		int pos2 = stops.indexOf(s2);
		CTAStation temp = s1;
		stops.add(pos1, s2);
		stops.remove(s1);
		stops.add(pos2, temp);
		stops.remove(s2);
	}
	
}
