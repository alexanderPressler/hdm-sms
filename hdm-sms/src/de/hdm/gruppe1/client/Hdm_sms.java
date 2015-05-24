package de.hdm.gruppe1.client;

import de.hdm.gruppe1.shared.FieldVerifier;
import de.hdm.gruppe1.shared.Sms;
import de.hdm.gruppe1.shared.SmsAsync;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.RootPanel;

public class Hdm_sms implements EntryPoint {

	Image welcomeImage = new Image();
	HTML welcomeText = new HTML("<h1>Wilkommen!</h1><br/><h2>Melden Sie sich mit Ihrem Google-Account im System an,<br/>um Zugriff zum gesamten Funktionsumfang der Applikation<br/>zu bekommen.</h2>");

	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {

		//Neu: MenuBar mit Commands (~ClickHandler)
		Command createBauteil = new Command() {
		      public void execute() {
			        RootPanel.get("content_wrap").clear();
			        RootPanel.get("content_wrap").add(new CreateBauteil());
		      }
		};
		
		Command allBauteile = new Command() {
		      public void execute() {
		        RootPanel.get("content_wrap").clear();
		        RootPanel.get("content_wrap").add(new BauteilGeneralView());
		      }
		};
		
		Command createStueckliste = new Command() {
		      public void execute() {
		    	  RootPanel.get("content_wrap").clear();
		    	  RootPanel.get("content_wrap").add(new CreateStueckliste());
		      }
		};
		
		Command allStuecklisten = new Command() {
		      public void execute() {
		    	  RootPanel.get("content_wrap").clear();
		    	  RootPanel.get("content_wrap").add(new StuecklisteGeneralView());
		      }
		};
		
		Command testCmd = new Command() {
		      public void execute() {
		    	  RootPanel.get("content_wrap").clear();
		    	  Window.alert("Platzhalter");
		      }
		};
		
		//Neu: MenuBar mit Mouse-Over Untermenüs
		
		//Das Menü von Bauteile erhält folgende Mouse-Over Untermenüs
	    MenuBar bauteilMenu = new MenuBar(true);
	    bauteilMenu.addItem("Bauteil anlegen", createBauteil);
	    bauteilMenu.addItem("Alle anzeigen", allBauteile);

	    //Das Menü von Baugruppen erhält folgende Mouse-Over Untermenüs
	    MenuBar baugruppeMenu = new MenuBar(true);
	    baugruppeMenu.addItem("Baugruppe anlegen", testCmd);
	    baugruppeMenu.addItem("Alle anzeigen", testCmd);

	    //Das Menü von Enderzeugnissen erhält folgende Mouse-Over Untermenüs
	    MenuBar enderzeugnisMenu = new MenuBar(true);
	    enderzeugnisMenu.addItem("Enderzeugnis anlegen", testCmd);
	    enderzeugnisMenu.addItem("Alle Anzeigen", testCmd);
	    
	    //Das Menü von Stücklisten erhält folgende Mouse-Over Untermenüs
	    MenuBar stuecklisteMenu = new MenuBar(true);
	    stuecklisteMenu.addItem("Stückliste anlegen", createStueckliste);
	    stuecklisteMenu.addItem("Alle Anzeigen", allStuecklisten);

	    //Alle Untermenüs werden hier dem Hauptmenü zugeordnet
	    MenuBar mainMenu = new MenuBar();
	    mainMenu.setWidth("100%"); 
	    mainMenu.addItem("Bauteile", bauteilMenu);
	    mainMenu.addItem("Baugruppen", baugruppeMenu);
	    mainMenu.addItem("Enderzeugnisse", enderzeugnisMenu);
	    mainMenu.addItem("Stücklisten", stuecklisteMenu);
	    
	    //Der Default-Text, der beim Aufruf der Applikation angezeigt wird
	    
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
