package com.webapp.foodhub.client;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.Window;

public class ResizeController {
	
	private static ResizeController controller;
	private double browserHeight;
	
	private ResizeController() {
		browserHeight = Window.getClientHeight();
	}
	
	public static ResizeController getInstance() {
		if (controller == null) {
			controller = new ResizeController();
		}
		return controller;
	}
	
	public void setExploreResultsHeight(Element scrollPanel) {
		browserHeight = Window.getClientHeight();
		Element exploreButton = Document.get().getElementById("explore_button");
		double buttonBottom = exploreButton.getAbsoluteTop() + exploreButton.getClientHeight() + 50;
		if (scrollPanel != null) {
			scrollPanel.getStyle().setHeight((browserHeight - buttonBottom), Style.Unit.PX);
		}
	}
	
	public void setSearchResultsHeight(Element scrollPanel) {
		browserHeight = Window.getClientHeight();
		Element searchButton = Document.get().getElementById("search_button");
		double buttonBottom = searchButton.getAbsoluteTop() + searchButton.getClientHeight() + 50;
		if (scrollPanel != null) {
			scrollPanel.getStyle().setHeight((browserHeight - buttonBottom), Style.Unit.PX);
		}
	}
	
	public void setDirectionsPanelHeight(Element directions_panel) {
		browserHeight = Window.getClientHeight();
		double panelTop = Document.get().getElementById("options_container").getAbsoluteTop() + 70;
		directions_panel.getStyle().setHeight((browserHeight - panelTop), Style.Unit.PX);
	}
	
	public void setMapHeight(Element mapContainer) {
		browserHeight = Window.getClientHeight();
		double panelTop = Document.get().getElementById("options_container").getAbsoluteTop() + 70;
		mapContainer.getStyle().setHeight((browserHeight - panelTop), Style.Unit.PX);
	}
	
	public void adjustExploreView() {
		Element scrollPanel = Document.get().getElementById("scrollPanel");
		setExploreResultsHeight(scrollPanel);
		Element dir_panel = Document.get().getElementById("explore_dir_panel");
		setDirectionsPanelHeight(dir_panel);
		Element explore_map = Document.get().getElementById("explore_map");
		setMapHeight(explore_map);
	}
	
	public void adjustSearchView() {
		Element scrollPanel = Document.get().getElementById("scrollPanel");
		setSearchResultsHeight(scrollPanel);
		Element dir_panel = Document.get().getElementById("search_dir_panel");
		setDirectionsPanelHeight(dir_panel);
		Element search_map = Document.get().getElementById("search_map");
		setMapHeight(search_map);
	}

}
