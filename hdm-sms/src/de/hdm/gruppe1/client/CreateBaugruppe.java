package de.hdm.gruppe1.client;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import de.hdm.gruppe1.client.ClientsideSettings;
import de.hdm.gruppe1.shared.SmsAsync;
import de.hdm.gruppe1.shared.bo.Baugruppe;

//Die Klasse CreateBaugruppe liefert alle benoetigten Elemente, um eine neue Baugruppe im System anzulegen.

public class CreateBaugruppe extends VerticalPanel {
	
	//Elemente fuer CreateBaugruppe zu initialisieren
	
	private final Label HeadlineLabel = new Label ("Baugruppe anlegen");
	private final Label SublineLabel = new Label ("Um eine Baugruppe anzulegen, füllen Sie bitte alle Felder aus und bestätigen mit dem <anlegen>-Button ihre Eingabe.");
	private final Label NameFieldLabel = new Label ("Name");
	private final TextBox NameField = new TextBox ();
	private final Button CreateBaugruppeButton = new Button ("anlegen");
	
	// Remote Service via ClientsideSettings  X
	SmsAsync stuecklistenVerwaltung = ClientsideSettings.getSmsVerwaltung();
	
	public CreateBaugruppe(){

		this.add(HeadlineLabel);
		this.add(SublineLabel);
		this.add(NameFieldLabel);
		this.add(NameField);
		this.add(CreateBaugruppeButton);
		
		HeadlineLabel.setStyleName("headline");
		SublineLabel.setStyleName("subline");
		CreateBaugruppeButton.setStyleName("Button");

		CreateBaugruppeButton.addClickHandler(new CreateClickHandler());
		
		RootPanel.get("content_wrap").add(this);
		
	}
	
	/*
	 * Click Handlers.
	 */
	
	/**
	 * Die Anlage einer Baugruppe. 
	 * Es erfolgt der Aufruf der Service-Methode "create".
	 */

	private class CreateClickHandler implements ClickHandler {
		@Override
		public void onClick(ClickEvent event) {

			String name = NameField.getText();
			
			stuecklistenVerwaltung.createBaugruppe(name, new CreateBaugruppeCallback());
			
			 RootPanel.get("content_wrap").clear();
			 RootPanel.get("content_wrap").add(new BauteilGeneralView());
			
		}
	}
	
	class CreateBaugruppeCallback implements AsyncCallback<Baugruppe> {

		@Override
		public void onFailure(Throwable caught) {
			Window.alert("Das Anlegen der Baugruppe ist fehlgeschlagen!");
		}

		@Override
		public void onSuccess(Baugruppe baugruppe) {

			Window.alert("Die Baugruppe wurde erfolgreich angelegt.");
			
			Window.alert("Das Baugruppe wurde erfolgreich angelegt.");
			
			
			//TODO: Klären ob das catvm gebraucht wird 
			// if (bauteil != null) {
			// Das erfolgreiche Hinzufügen eines Kunden wird an den
			// Kunden- und
			// Kontenbaum propagiert.
			// catvm.addCustomer(customer);
			// }
	
		
		}
	}

}
		
		
