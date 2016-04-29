package com.webapp.foodhub.client;

import java.util.ArrayList;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.maps.gwt.client.DirectionsRenderer;
import com.google.maps.gwt.client.DirectionsRequest;
import com.google.maps.gwt.client.DirectionsResult;
import com.google.maps.gwt.client.DirectionsService;
import com.google.maps.gwt.client.DirectionsStatus;
import com.google.maps.gwt.client.GoogleMap;
import com.google.maps.gwt.client.InfoWindow;
import com.google.maps.gwt.client.InfoWindowOptions;
import com.google.maps.gwt.client.LatLng;
import com.google.maps.gwt.client.LatLngBounds;
import com.google.maps.gwt.client.MapOptions;
import com.google.maps.gwt.client.Marker;
import com.google.maps.gwt.client.Marker.ClickHandler;
import com.google.maps.gwt.client.Marker.DblClickHandler;
import com.google.maps.gwt.client.MarkerOptions;
import com.google.maps.gwt.client.MouseEvent;
import com.google.maps.gwt.client.TravelMode;

public class MapController {
	
	private static MapController mapController;
	
	private GoogleMap map;
	private Element directions_panel;
	private Element map_panel;
	private ArrayList<Marker> markers = new ArrayList<Marker>();
	private ArrayList<InfoWindow> popUps = new ArrayList<InfoWindow>();
	
	private MapOptions mapOptions = MapOptions.create();
	private MarkerOptions markerOptions = MarkerOptions.create();
	private DirectionsRenderer renderer;
	private ResizeController resizeController;
	
	private MapController() {
		mapOptions.setCenter(LatLng.create(49.246292, -123.116226));
		mapOptions.setZoom(5); mapOptions.setDraggable(true);
		resizeController = ResizeController.getInstance();
	}
	
	public static MapController getInstance() {
		if (mapController == null) {
			mapController = new MapController();
		}
		return mapController;
	}
	
	public void drawExploreMap() {
		map_panel = Document.get().getElementById("explore_map");
		directions_panel = Document.get().getElementById("explore_dir_panel");
		resizeController.setMapHeight(map_panel);
		drawMap();
	}
	
	public void drawSearchMap() {
		map_panel = Document.get().getElementById("search_map");
		directions_panel = Document.get().getElementById("search_dir_panel");
		resizeController.setMapHeight(map_panel);
		drawMap();
	}
	
	public void drawMap() {
		ResizeController resizeController = ResizeController.getInstance();
		resizeController.resetMapAndDirWidth(map_panel, directions_panel);
		
		// Need to create the map again after resizing otherwise map is still its previous size
		// while the container size has changed
		map = GoogleMap.create(map_panel, mapOptions);
	}
	
	public void drawMarkers(FoodHubVenue[] venues) {
		for (FoodHubVenue venue: venues) {
			markerOptions.setPosition(venue.getLatLong());
			markerOptions.setTitle(venue.getName());
			final Marker marker = Marker.create(markerOptions);
			markers.add(marker);
			marker.setMap(map);
			
			InfoWindowOptions popUpOptions = InfoWindowOptions.create();
			popUpOptions.setContent(venue.getName() + "<br>" + venue.getAddress());
			final InfoWindow popUp = InfoWindow.create(popUpOptions);
			popUps.add(popUp);
			
			marker.addClickListener(new ClickHandler() {
				@Override
				public void handle(MouseEvent event) {
					popUp.open(map, marker);
				}
			});
			
			marker.addDblClickListener(new DblClickHandler() {
				@Override
				public void handle(MouseEvent event) {
					popUp.close();
				}	
			});
		}
		fitBounds();
	}
	
	public void triggerMarkerPopUp(String name) {
		for (Marker marker: markers) {
			if (marker.getTitle() == name) {
				marker.triggerClick();
				break;
			}
		}
	}
	
	public void closeMarkerPopUp(String name) {
		for (Marker marker: markers) {
			if (marker.getTitle() == name) {
				marker.triggerDblClick();
				break;
			}
		}
	}
	
	public void clearMarkers() {
		for (Marker marker: this.markers) {
			// Without casting to GoogleMap, since setMap is overloaded, when we pass in null, there
			// will be an ambiguity warning
			marker.setMap((GoogleMap)null);
			marker = null;
		}
		this.markers.clear();
		this.popUps.clear();
	}
	
	public void fitBounds() {
		LatLngBounds bounds = LatLngBounds.create();
		for (Marker marker: this.markers) {
			// Extends the bounds to include the given LatLng coordinate
			bounds.extend(marker.getPosition());
		}
		map.fitBounds(bounds);
	}
	
	// This function is called when 
	public void resetMap() {
		
		// Don't need to create a new map or resize it if containers didn't change size
		// Just need to clear the markers
		clearMarkers();
		
		if (directions_panel.getOffsetWidth() != 0) {
			clearDirections();
			drawMap();
		}
	}
	
	public void drawDirections(String origin, LatLng destination) {
		ResizeController resizeController = ResizeController.getInstance();
		resizeController.setDirectionsPanelHeight(directions_panel);
		
		if (directions_panel.getOffsetWidth() == 0) {
			resizeController.setMapAndDirWidth(map_panel, directions_panel);
			map = GoogleMap.create(map_panel, mapOptions);
		}
		
		if (renderer != null) {
			clearDirections();
		} else {
			renderer = DirectionsRenderer.create();
		}
		DirectionsService service = DirectionsService.create();
		DirectionsRequest request = DirectionsRequest.create();
		
		request.setOrigin(origin);
		request.setDestination(destination);
		request.setTravelMode(TravelMode.DRIVING);
		
		service.route(request, new DirectionsHandler());
	}
	
	public void clearDirections() {
		if (renderer != null) {
			renderer.setMap(null);
			renderer.setPanel(null);
		}
	}
	
	private class DirectionsHandler implements DirectionsService.Callback {
		@Override
		public void handle(DirectionsResult result, DirectionsStatus status) {
			if (status == DirectionsStatus.OK) {
				renderer.setDirections(result);
				renderer.setMap(map);
				renderer.setPanel(directions_panel);
			}
		}
	}
}
