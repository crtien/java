package project;

// CTAStation is a subclass of GeoLocation
// CTAStation stores all information of a station
// Author: Tien Tran
// Date: 25/Apr/2018

public class CTAStation extends GeoLocation {
	// This is a sub class of GeoLocation
	
	private String name;
	private String location;
	private boolean wheelchair;
	// Position of station is stored in the array pos
	private int[] pos;
	
	// Default constructor, using super class' constructor
	public CTAStation() {
		super();
		name = "";
		location = "";
		wheelchair = false;
		pos = new int[8];
	}
	
	// Non-default constructor, using super class' constructor
	public CTAStation(double lat, double lng) {
		super(lat,lng);
		name = "";
		location = "";
		wheelchair = false;
		pos = new int[8];
	}
	
	// Getters for variables
	public String getName() {
		return name;
	}
	
	public String getLocation() {
		return location;
	}
	
	public boolean getWheelchair() {
		return wheelchair;
	}
	
	public int[] getPos() {
		return pos;
	}

	
	// Setters for variables
	public void setName(String name) {
		this.name = name;
	}
	
	public void setLocation(String location) {
		this.location = location;
	}
	
	public void setWheelchair(boolean wheelchair) {
		this.wheelchair = wheelchair;
	}
	
	public void setPos(int[] pos) {
		this.pos = pos;
	}
	
	// toString method
	public String toString() {
		return name+", "+location+", Wheelchair: "+wheelchair;
	}
	
	// equals method, only use names to compare stations
	public boolean equals(CTAStation s) {
		if (s.getName().equalsIgnoreCase(name)) {
			return true;
		}
		return false;
	}
	
}
