package de.hdm.gruppe1.client;

import de.hdm.gruppe1.shared.FieldVerifier;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

public class Hdm_sms implements EntryPoint {

	Image welcomeImage = new Image();

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
		
		Command createBaugruppe = new Command() {
		      public void execute() {
			        RootPanel.get("content_wrap").clear();
			        RootPanel.get("content_wrap").add(new CreateBaugruppe());
		      }
		};
		
		Command allBaugruppen = new Command() {
		      public void execute() {
		        RootPanel.get("content_wrap").clear();
		        RootPanel.get("content_wrap").add(new BaugruppeGeneralView());
		      }
		};
		
		Command testCmd = new Command() {
		      public void execute() {
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
	    baugruppeMenu.addItem("Baugruppe anlegen", createBaugruppe);
	    baugruppeMenu.addItem("Alle anzeigen", allBaugruppen);

	    //Das Menü von Enderzeugnissen erhält folgende Mouse-Over Untermenüs
	    MenuBar enderzeugnisMenu = new MenuBar(true);
	    enderzeugnisMenu.addItem("Enderzeugnis anlegen", testCmd);
	    enderzeugnisMenu.addItem("Alle Anzeigen", testCmd);
	    
	    //Das Menü von Stücklisten erhält folgende Mouse-Over Untermenüs
	    MenuBar stuecklisteMenu = new MenuBar(true);
	    stuecklisteMenu.addItem("Stückliste anlegen", testCmd);
	    stuecklisteMenu.addItem("Alle Anzeigen", testCmd);

	    //Alle Untermenüs werden hier dem Hauptmenü zugeordnet
	    MenuBar mainMenu = new MenuBar();
	    mainMenu.setWidth("100%"); 
	    mainMenu.addItem("Bauteile", bauteilMenu);
	    mainMenu.addItem("Baugruppen", baugruppeMenu);
	    mainMenu.addItem("Enderzeugnisse", enderzeugnisMenu);
	    mainMenu.addItem("Stücklisten", stuecklisteMenu);
	    
	    //Das Begrüßungsbild der Applikation
	    welcomeImage.setStyleName("initialPicture");
		    
	    //Hautpmenü schließlich dem RootPanel in den Menü-div Container zuordnen
	    RootPanel.get("head_wrap_right").add(mainMenu);
	    RootPanel.get("content_wrap").add(welcomeImage);
		RootPanel.get("Impressum").add(new Impressum());

	}

}