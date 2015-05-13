package de.hdm.gruppe1.client;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

import de.hdm.gruppe1.shared.bo.Bauteil;

//Die Klasse EditBauteil liefert alle benötigten Elemente, um ein bestehendes Bauteil im System zu ändern.
public class EditBauteil extends VerticalPanel {

	//Elemente für EditBauteil initialisieren
		private final Label HeadlineLabel = new Label ("Bauteil ändern");
		private final Label SublineLabel = new Label ("Um ein Bauteil zu ändern, füllen Sie bitte alle Felder aus und bestätigen mit dem <editieren>-Button ihre Eingabe.");
		private final Label NameFieldLabel = new Label ("Bezeichnung");
		private final TextBox NameField = new TextBox ();
		private final Label MaterialFieldLabel = new Label ("Materialbezeichnung");
		private final TextBox MaterialField = new TextBox ();
		private final Label DescriptionFieldLabel = new Label ("Textuelle Beschreibung");
		private final TextBox DescriptionField = new TextBox ();
		private final Button EditBauteilButton = new Button ("ändern");
		
		public EditBauteil () {
			
			this.add(HeadlineLabel);
			this.add(SublineLabel);
			this.add(NameFieldLabel);
			this.add(NameField);
			this.add(MaterialFieldLabel);
			this.add(MaterialField);
			this.add(DescriptionFieldLabel);
			this.add(DescriptionField);
			this.add(EditBauteilButton);
			
			DescriptionField.setStyleName("DescriptionFieldText");
			EditBauteilButton.setStyleName("Button");
			
			EditBauteilButton.addClickHandler(new EditClickHandler());
			
			//Testweise bis zur Anbindung zur Applikationsschicht wird hier ein Beispiel-Bauteil initialisiert
			Bauteil b = new Bauteil();
			
			b.setName("Schraube");
			b.setMaterialBeschreibung("Eisen");
			b.setBauteilBeschreibung("Beispieltext");
			
			NameField.setText(b.getName());
			MaterialField.setText(b.getMaterialBeschreibung());
			DescriptionField.setText(b.getBauteilBeschreibung());
			
			RootPanel.get("content_wrap").add(this);
			
		}
		
		/*
		 * Click Handlers.
		 */
		
		/**
		  * Das Ändern eines Bauteils ruft die Service-Methode "edit" auf.
		  * 
		  */
		 private class EditClickHandler implements ClickHandler {
		  @Override
		  public void onClick(ClickEvent event) {
//		   if (customerToDisplay != null) {

				RootPanel.get("content_wrap").clear();
				Window.alert("Bauteil wurde (nicht) geändert");
				RootPanel.get("content_wrap").add(new BauteilGeneralView());
			   
//		   } else {
//		    Window.alert("kein Kunde ausgewählt");
//		   }
		  }
		 }
	
}
