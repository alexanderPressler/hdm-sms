package de.hdm.gruppe1.client;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

//Die Klasse CreateBauteil liefert alle benötigten Elemente, um ein neues Bauteil im System anzulegen.
public class CreateBauteil implements IsWidget {

	@Override
	public Widget asWidget() {
		return null;
	}
	
	//Vertikales Panel, um alle relevanten Elemente anzuordnen, um ein neues Bauteil anzulegen.
	private final VerticalPanel CreateBauteilPanel= new VerticalPanel ();
	
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
		
		CreateBauteilPanel.add(HeadlineLabel);
		CreateBauteilPanel.add(SublineLabel);
		CreateBauteilPanel.add(NameFieldLabel);
		CreateBauteilPanel.add(NameField);
		CreateBauteilPanel.add(MaterialFieldLabel);
		CreateBauteilPanel.add(MaterialField);
		CreateBauteilPanel.add(DescriptionFieldLabel);
		CreateBauteilPanel.add(DescriptionField);
		CreateBauteilPanel.add(CreateBauteilButton);
		
		CreateBauteilButton.addClickHandler(new ClickHandler(){
			public void onClick(ClickEvent event) {
				RootPanel.get("content_wrap").clear();
				Window.alert("Bauteil wurde (nicht) angelegt");
			    }

		});
		
		RootPanel.get("content_wrap").add(CreateBauteilPanel);
		
	}
	
}
