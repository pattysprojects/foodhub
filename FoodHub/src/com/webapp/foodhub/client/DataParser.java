package com.webapp.foodhub.client;

import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;

public class DataParser {
	
	private String response;
	private FoodHubVenue[] venues;
	private int venueCount = 0;
	
	public DataParser(String json) {
		this.response = json;
	}
	
	/** Note JSONArrays begin with square brackets ie. [{"name":"item 1"},{"name": "item2} ] 
	 *  vs a JSONObject {"name": "item1", "description":"a JSON object"}
	 *  
	 *  Also note that Objects have no order for properties so need a key for get() 
	 *  but arrays do so they take in an index instead of a key for get()
	 */
	
	/**
	 * This function parses the JSON response returned from the FourSquare API for an explore query
	 * The response has an array called groups, each group containing an array called items -- for each 
	 * item, we parse the Venue JSON object inside and make a corresponding FoodHubVenue object
	 */
	public FoodHubVenue[] parseExploreData() {
		
		venues = new FoodHubVenue[]{};
		venueCount = 0;
		
		JSONObject respObj = JSONParser.parseStrict(this.response).isObject();
		JSONArray groups = (respObj.get("response").isObject()).get("groups").isArray();
		
		for (int i=0; i < groups.size(); i++) {
			JSONObject currRecGroup = groups.get(i).isObject();
			JSONArray currItems = currRecGroup.get("items").isArray();
			
			for (int j=0; j < currItems.size(); j++) {
				JSONObject currRecObj = currItems.get(j).isObject();
				JSONObject currVenueObj = currRecObj.get("venue").isObject();
				
				FoodHubVenue venue = getVenueFromJSONObject(currVenueObj);
				venues[venueCount] = venue;
				venueCount++;
			}
		}
		return venues;
	}
	
	/** 
	 *  This function parses the JSON response returned from the FourSquare API for a search query
	 *  The response is an array of Venue objects -- each Venue has an id, a name, a location Object
	 *  which in turn contains an address and lat/lng values
	 */
	public FoodHubVenue[] parseSearchData() {
		
		venues = new FoodHubVenue[]{};
		venueCount = 0;
		
		JSONObject respObj = JSONParser.parseStrict(this.response).isObject();
		JSONArray venuesArray = (respObj.get("response").isObject()).get("venues").isArray();
		
		for (int i=0; i < venuesArray.size(); i++) {
			JSONObject currVenueObj = venuesArray.get(i).isObject();
			FoodHubVenue venue = getVenueFromJSONObject(currVenueObj);
			venues[i] = venue;
			venueCount++;
		}
		return venues;
	}
	
	/**
	 * This function takes in a Venue JSON Object and parses out the required fields
	 * for constructing a FoodHubVenue (id, name, address, lat, lng)
	 */
	
	public FoodHubVenue getVenueFromJSONObject(JSONObject venueObj) {
		
		JSONObject location = venueObj.get("location").isObject();
		/** All of these parameters are JSONValues, their actual types (ie. JSONStrings or JSONNumbers
		 *  are converted into raw String and raw double values in the FoodHubVenue constructor
		 */
		return new FoodHubVenue(venueObj.get("name"), venueObj.get("id"), location.get("address"),
				location.get("lat"), location.get("lng"));
	}
}
