package project;

// GeoLocation is the superclass of CTAStation
// Has methods to calculate distance between 2 locations
// Author: Tien Tran
// Date: 25/Apr/2018

public class GeoLocation {

	// Create 2 instance variables
	private double latitude;
	private double longitude;
	
	// Default constructor
	public GeoLocation() {
		latitude = 0.0;
		longitude = 0.0;
	}
	
	// Non-default constructor
	public GeoLocation(double latitude, double longitude) {
		this.latitude = 0.0;
		this.longitude = 0.0;
		setLat(latitude);
		setLong(longitude);
	}
	
	// Getter for latitude
	public double getLat() {
		return latitude;
	}
	
	// Getter for longitude
	public double getLong() {
		return longitude;
	}
	
	// Setter for latitude
	public void setLat(double latitude) {
		this.latitude = latitude;
	}
	
	// Setter for longitude
	public void setLong(double longitude) {
		this.longitude = longitude;
	}
	
	// toString method
	public String toString() {
		return "(*" + getLat() + "*, *" + getLong()+"*)";
	}
	
	// This method will return true if the latitude is between -90 and +90.
	public boolean checkLat() {
		if (getLat() <= 90 && getLat() >= -90) {
			return true;
		} else return false;
	}
	
	// This method will return true if the longitude is between -180 and +180.
	public boolean checkLong() {
		if (getLong() <= 180 && getLong() >= -180) {
			return true;
		} else return false;
	}
	
	public static GeoLocation takeString(String line) {
		String[] input = line.split(",");
		int l1 = input[0].length();
		int l2 = input[1].length();
		// Split the initial string into 2 strings, one contains latitude and the other contains longitude.
		// Store them in an array named "input".
		
		String temp1 = input[0].substring(2, l1-1);
		double latitude = Double.parseDouble(temp1);
		// Parse data from the first string to double type to get latitude.
		
		String temp2 = input[1].substring(2, l2-2);
		double longitude = Double.parseDouble(temp2);
		// Parse data from the second string to double type to get longitude.
		
		GeoLocation fresh = new GeoLocation(latitude, longitude);
		return fresh;
		// Create and return the new GeoLocation with the latitude and longitude using the non-default constructor.
	}
	
	// Calculate distance between 2 instances of GeoLocation
	public double calcDistance(GeoLocation g) {
		double lat1 = this.latitude;
		double long1 = this.longitude;
		double lat2 = g.getLat();
		double long2 = g.getLong();
		return Math.sqrt(Math.pow(lat1-lat2,2)+Math.pow(long1-long2,2));
	}
	
	// Calculate distance between a GeoLocation and an arbitrary point
	public double calcDistance(double lat2, double long2) {
		double lat1 = this.latitude;
		double long1 = this.longitude;
		return Math.sqrt(Math.pow(lat1-lat2,2)+Math.pow(long1-long2,2));
	}
	
}
