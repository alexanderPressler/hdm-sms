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
import de.hdm.gruppe1.client.CreateBauteil.CreateBauteilCallback;
import de.hdm.gruppe1.shared.SmsAsync;
import de.hdm.gruppe1.shared.bo.Baugruppe;
import de.hdm.gruppe1.shared.bo.Stueckliste;

//Die Klasse CreateBaugruppe liefert alle benoetigten Elemente, um eine neue Baugruppe im System anzulegen.

public class CreateBaugruppe extends VerticalPanel {
	
	//Elemente fuer CreateBaugruppe zu initialisieren
	
	private final Label HeadlineLabel = new Label ("Baugruppe anlegen");
	private final Label SublineLabel = new Label ("Um eine Baugruppe anzulegen, f�llen Sie bitte alle Felder aus und best�tigen mit dem <anlegen>-Button ihre Eingabe.");
	private final Label NameFieldLabel = new Label ("Name");
	private final TextBox NameField = new TextBox ();
	private final Label StuecklisteFieldLabel = new Label("Stueckliste");

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
		
			if (NameField.getText().isEmpty() != true) {

				/**
				 * Die konkrete RPC-Methode f�r den create-Befehl wird
				 * aufgerufen. Hierbei werden die gew�nschten Werte
				 * mitgeschickt.
				 */
//				stuecklistenVerwaltung.createBaugruppe( name,stueckliste new CreateBauteilCallback());

			 RootPanel.get("content_wrap").clear();
			 RootPanel.get("content_wrap").add(new BaugruppeGeneralView());
			
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
			
			
			//TODO: Kl�ren ob das catvm gebraucht wird 
			// if (bauteil != null) {
			// Das erfolgreiche Hinzuf�gen eines Kunden wird an den
			// Kunden- und
			// Kontenbaum propagiert.
			// catvm.addCustomer(customer);
			// }
	
		
		}
	}

}}
	