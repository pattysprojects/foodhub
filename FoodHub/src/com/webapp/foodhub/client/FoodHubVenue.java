package com.webapp.foodhub.client;

import com.google.gwt.json.client.JSONValue;
import com.google.maps.gwt.client.LatLng;

public class FoodHubVenue {
	
	private String name = "";
	private String id = "";
	private String address = "";
	private double lat = 0;
	private double lng = 0;
	
	public FoodHubVenue(JSONValue name, JSONValue id, JSONValue addr, JSONValue lt, JSONValue ln) {
		
		// stringValue() and doubleValue() get the raw String and double values without JSON string quotation marks
		this.name = name.isString().stringValue();
		this.id = id.isString().stringValue();
		
		// Venue Response always returns an id, name and location field but the location field might have one
		// some or all of address, lat and long -- so have to do a null check for the values below
		if (addr != null) {
			this.address = addr.isString().stringValue();
		}
		if (lt != null) {
			this.lat = lt.isNumber().doubleValue();
		}
		if (ln != null) {
			this.lng = ln.isNumber().doubleValue();
		}
	}
	
	public String getName() {
		return this.name;
	}
	
	public String getId() {
		return this.id;
	}
	
	public String getAddress() {
		return this.address;
	}
	
	public LatLng getLatLong() {
		return LatLng.create(this.lat, this.lng);
	}

}
