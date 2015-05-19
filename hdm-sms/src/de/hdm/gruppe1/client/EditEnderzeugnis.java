package de.hdm.gruppe1.client;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

import de.hdm.gruppe1.shared.bo.Enderzeugnis;

//Die Klasse EditEnderzeugnis liefert alle benötigten Elemente, um ein bestehendes Enderzeugnis im System zu ändern.
public class EditEnderzeugnis extends VerticalPanel {

	//Elemente für EditBauteil initialisieren
		private final Label HeadlineLabel = new Label ("Enderzeugnis ändern");
		private final Label SublineLabel = new Label ("Um ein Enderzeugnis zu ändern, füllen Sie bitte alle Felder aus und bestätigen mit dem <editieren>-Button ihre Eingabe.");
		private final Label NameFieldLabel = new Label ("Name");
		private final TextBox NameField = new TextBox ();
		private final Button EditEnderzeugnisButton = new Button ("ändern");
		
		public EditEnderzeugnis () {
			
			this.add(HeadlineLabel);
			this.add(SublineLabel);
			this.add(NameFieldLabel);
			this.add(NameField);
			this.add(EditEnderzeugnisButton);
			
			EditEnderzeugnisButton.setStyleName("Button");
			
			EditEnderzeugnisButton.addClickHandler(new EditClickHandler());
			
			//Testweise bis zur Anbindung zur Applikationsschicht wird hier ein Beispiel-Enderzeugnis initialisiert
			Enderzeugnis e = new Enderzeugnis();
			
			e.setName("Motor");

			
			NameField.setText(e.getName());
			
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
				RootPanel.get("content_wrap").add(new EnderzeugnisGeneralView());
			   
//		   } else {
//		    Window.alert("kein Kunde ausgewählt");
//		   }
		  }
		 }
	
}
