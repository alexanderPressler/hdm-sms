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
	HTML welcomeText = new HTML("<h1>Wilkommen!</h1><br/><h1>Melden Sie sich mit Ihrem Google-Account im System an,<br/>um Zugriff zum gesamten Funktionsumfang der Applikation<br/>zu bekommen.</h1>");

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
		
		//Neu: MenuBar mit Mouse-Over Untermen�s
		
		//Das Men� von Bauteile erh�lt folgende Mouse-Over Untermen�s
	    MenuBar bauteilMenu = new MenuBar(true);
	    bauteilMenu.addItem("Bauteil anlegen", createBauteil);
	    bauteilMenu.addItem("Alle anzeigen", allBauteile);

	    //Das Men� von Baugruppen erh�lt folgende Mouse-Over Untermen�s
	    MenuBar baugruppeMenu = new MenuBar(true);
	    baugruppeMenu.addItem("Baugruppe anlegen", testCmd);
	    baugruppeMenu.addItem("Alle anzeigen", testCmd);

	    //Das Men� von Enderzeugnissen erh�lt folgende Mouse-Over Untermen�s
	    MenuBar enderzeugnisMenu = new MenuBar(true);
	    enderzeugnisMenu.addItem("Enderzeugnis anlegen", testCmd);
	    enderzeugnisMenu.addItem("Alle Anzeigen", testCmd);
	    
	    //Das Men� von St�cklisten erh�lt folgende Mouse-Over Untermen�s
	    MenuBar stuecklisteMenu = new MenuBar(true);
	    stuecklisteMenu.addItem("St�ckliste anlegen", createStueckliste);
	    stuecklisteMenu.addItem("Alle Anzeigen", allStuecklisten);

	    //Alle Untermen�s werden hier dem Hauptmen� zugeordnet
	    MenuBar mainMenu = new MenuBar();
	    mainMenu.setWidth("100%"); 
	    mainMenu.addItem("Bauteile", bauteilMenu);
	    mainMenu.addItem("Baugruppen", baugruppeMenu);
	    mainMenu.addItem("Enderzeugnisse", enderzeugnisMenu);
	    mainMenu.addItem("St�cklisten", stuecklisteMenu);
	    
	    //Der Default-Text, der beim Aufruf der Applikation angezeigt wird
	    
	    //Das Begr��ungsbild der Applikation
		welcomeImage.setUrl("./img/Welcome.jpg");
	    welcomeImage.setStyleName("initialPicture");
		    
	    //Hautpmen� schlie�lich dem RootPanel in den Men�-div Container zuordnen
	    RootPanel.get("head_wrap_right").add(mainMenu);
	    RootPanel.get("content_wrap").add(welcomeImage);
	    RootPanel.get("content_wrap").add(welcomeText);
		RootPanel.get("Impressum").add(new Impressum());

	}

}