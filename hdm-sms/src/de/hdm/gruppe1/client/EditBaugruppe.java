package de.hdm.gruppe1.client;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

import de.hdm.gruppe1.shared.bo.Baugruppe;

//Die Klasse EditBaugruppe liefert alle ben�tigten Elemente, um ein bestehendes Baugruppe im System zu �ndern.
public class EditBaugruppe extends VerticalPanel {

	//Elemente f�r EditBaugruppe initialisieren
		private final Label HeadlineLabel = new Label ("Baugruppe �ndern");
		private final Label SublineLabel = new Label ("Um ein Baugruppe zu �ndern, fb�llen Sie bitte alle Felder aus und best�tigen mit dem <editieren>-Button ihre Eingabe.");
		private final Label NameFieldLabel = new Label ("Bezeichnung");
		private final TextBox NameField = new TextBox ();
		private final TextBox DescriptionField = new TextBox ();
		private final Button EditBaugruppeButton = new Button ("�ndern");
		
		public EditBaugruppe () {
			
			this.add(HeadlineLabel);
			this.add(SublineLabel);
			this.add(NameFieldLabel);
			this.add(NameField);
			this.add(DescriptionField);
			this.add(EditBaugruppeButton);
			
			DescriptionField.setStyleName("DescriptionFieldText");
			EditBaugruppeButton.setStyleName("Button");
			
			EditBaugruppeButton.addClickHandler(new EditClickHandler());
			
			// Testweise bis zur Anbindung zur Applikationsschicht wird hier ein Beispiel Baugruppe initialisiert
			Baugruppe b = new Baugruppe();
			
			b.setName("Motor");
			
			NameField.setText(b.getName());
			
			
			RootPanel.get("content_wrap").add(this);
			
		}
		
		/*
		 * Click Handlers.
		 */
		
		/**
		  * Das �ndern einer Baugruppe ruft die Service-Methode "edit" auf.
		  * 
		  */
		 private class EditClickHandler implements ClickHandler {
		  @Override
		  public void onClick(ClickEvent event) {
//		   if (customerToDisplay != null) {

				RootPanel.get("content_wrap").clear();
				Window.alert("Baugruppe wurde (nicht) ge�ndert");
				RootPanel.get("content_wrap").add(new BaugruppeGeneralView());
			   
//		   } else {
//		    Window.alert("kein Kunde ausgew�hlt");
//		   }
		  }
		 }
	
}