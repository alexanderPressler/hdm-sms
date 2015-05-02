package de.hdm.gruppe1.client;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

//Die Klasse CreateBauteil liefert alle benötigten Elemente, um ein neues Bauteil im System anzulegen.
public class CreateBauteil extends VerticalPanel {
	
	//Elemente für CreateBauteil initialisieren
	private final Label HeadlineLabel = new Label ("Bauteil anlegen");
	private final Label SublineLabel = new Label ("Um ein Bauteil anzulegen, füllen Sie bitte alle Felder aus und bestätigen mit dem <anlegen>-Button ihre Eingabe.");
	private final Label NameFieldLabel = new Label ("Bezeichnung");
	private final TextBox NameField = new TextBox ();
	private final Label MaterialFieldLabel = new Label ("Materialbezeichnung");
	private final TextBox MaterialField = new TextBox ();
	private final Label DescriptionFieldLabel = new Label ("Textuelle Beschreibung");
	private final TextBox DescriptionField = new TextBox ();
	private final Button CreateBauteilButton = new Button ("anlegen");
	
	public CreateBauteil(){
		
		this.add(HeadlineLabel);
		this.add(SublineLabel);
		this.add(NameFieldLabel);
		this.add(NameField);
		this.add(MaterialFieldLabel);
		this.add(MaterialField);
		this.add(DescriptionFieldLabel);
		this.add(DescriptionField);
		this.add(CreateBauteilButton);
		
		CreateBauteilButton.addClickHandler(new CreateClickHandler());
		
		RootPanel.get("content_wrap").add(this);
		
	}
	
	/*
	 * Click Handlers.
	 */
	
	/**
	  * Die Anlage eines Bauteils bezieht sich auf seinen Vor- und/oder
	  * Nachnamen. Es erfolgt der Aufruf der Service-Methode "create".
	  * 
	  */
	 private class CreateClickHandler implements ClickHandler {
	  @Override
	  public void onClick(ClickEvent event) {
//	   if (customerToDisplay != null) {

			RootPanel.get("content_wrap").clear();
			Window.alert("Bauteil wurde (nicht) angelegt");
			RootPanel.get("content_wrap").add(new BauteilGeneralView());
		   
//	   } else {
//	    Window.alert("kein Kunde ausgewählt");
//	   }
	  }
	 }
	
}
