package de.hdm.gruppe1.client;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import de.hdm.gruppe1.client.ClientsideSettings;
import de.hdm.gruppe1.shared.SmsAsync;
import de.hdm.gruppe1.shared.bo.Bauteil;

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
	private final TextArea DescriptionField = new TextArea ();
	private final Button CreateBauteilButton = new Button ("anlegen");
	
	// Remote Service via ClientsideSettings
	SmsAsync stuecklistenVerwaltung = ClientsideSettings.getSmsVerwaltung();
	
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
		
		HeadlineLabel.setStyleName("headline");
		SublineLabel.setStyleName("subline");
		DescriptionField.setStyleName("DescriptionFieldText");
		CreateBauteilButton.setStyleName("Button");

		CreateBauteilButton.addClickHandler(new CreateClickHandler());

		RootPanel.get("content_wrap").add(this);

	}

	/*
	 * Click Handlers.
	 */
	
	/**
	 * Die Anlage eines Bauteils. 
	 * Es erfolgt der Aufruf der Service-Methode "create".
	 */
	private class CreateClickHandler implements ClickHandler {
		@Override
		public void onClick(ClickEvent event) {

			String name = NameField.getText();
			String bauteilBeschreibung = DescriptionField.getText();
			String materialBeschreibung = MaterialField.getText();

			stuecklistenVerwaltung.createBauteil(name, bauteilBeschreibung,
					materialBeschreibung, new CreateBauteilCallback());
			
			 RootPanel.get("content_wrap").clear();
			 RootPanel.get("content_wrap").add(new BauteilGeneralView());
			 
		}
	}
	
	class CreateBauteilCallback implements AsyncCallback<Bauteil> {

		@Override
		public void onFailure(Throwable caught) {
			Window.alert("Das Anlegen des Bauteils ist fehlgeschlagen!");
		}

		@Override
		public void onSuccess(Bauteil bauteil) {

			Window.alert("Das Bauteil wurde erfolgreich angelegt.");
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
