package de.hdm.gruppe1.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class Hdm_smsReport extends VerticalPanel implements EntryPoint {

	Image welcomeImage = new Image();
	HTML welcomeText = new HTML("<h1>Wilkommen im Report-Generator!</h1>");

	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {

		/**
		 * Kommandos für die MenuBar
		 */
		Command report1 = new Command() {
		      public void execute() {
		    	  RootPanel.get("content_wrap").clear();
		    	  RootPanel.get("content_wrap").add(new Strukturstuecklisten());
		      }
		};
		
		Command report2 = new Command() {
		      public void execute() {
		    	  RootPanel.get("content_wrap").clear();
		    	  RootPanel.get("content_wrap").add(new Materialbedarf());
		      }
		};
		
		/**
		 * Das Menü enthält keine Untermenüs und beinhaltet 2 Haupt-Menüpunkte für die beiden Reports
		 */

		//Alle Untermenüs werden hier dem Hauptmenü zugeordnet
	    MenuBar mainMenu = new MenuBar();
	    mainMenu.setWidth("100%"); 
	    mainMenu.addItem("Strukturstücklisten", report1);
	    mainMenu.addItem("Materialbedarf", report2);
	    
	    //Das Begrüßungsbild der Applikation
		welcomeImage.setUrl("./img/Welcome.jpg");
	    welcomeImage.setStyleName("initialPicture");
		    
	    //Hautpmenü schließlich dem RootPanel in den Menü-div Container zuordnen
	    RootPanel.get("head_wrap_right").add(mainMenu);
	    RootPanel.get("content_wrap").add(welcomeImage);
	    RootPanel.get("content_wrap").add(welcomeText);
		RootPanel.get("Impressum").add(new Impressum());
		
	}

}