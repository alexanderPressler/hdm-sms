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

/**
 * Mithilfe der Klasse CreateBauteil wird dem User der Applikation erm�glicht,
 * ein Bauteil-Objekt in der Datenbank anzulegen.
 * 
 * @author Mario Theiler
 * @version 1.0
 */
public class CreateBauteil extends VerticalPanel {

	/**
	 * GUI-Elemente f�r CreateBauteil initialisieren
	 */
	private final Label HeadlineLabel = new Label("Bauteil anlegen");
	private final Label SublineLabel = new Label(
			"Um ein Bauteil anzulegen, f�llen Sie bitte alle Felder aus und best�tigen mit dem <anlegen>-Button ihre Eingabe.");
	private final Label NameFieldLabel = new Label("Name");
	private final TextBox NameField = new TextBox();
	private final Label MaterialFieldLabel = new Label("Materialbezeichnung");
	private final TextBox MaterialField = new TextBox();
	private final Label DescriptionFieldLabel = new Label(
			"Textuelle Beschreibung");
	private final TextArea DescriptionField = new TextArea();
	private final Button CreateBauteilButton = new Button("anlegen");

	/**
	 * Remote Service via ClientsideSettings wird an dieser Stelle einmalig in
	 * der Klasse aufgerufen. Im Anschluss kann jederzeit darauf zugegriffen
	 * werden.
	 */
	SmsAsync stuecklistenVerwaltung = ClientsideSettings.getSmsVerwaltung();

	public CreateBauteil() {

		/**
		 * Bei Instantiierung der Klasse wird alles dem VerticalPanel
		 * zugeordnet, da diese Klasse von VerticalPanel erbt.
		 */
		this.add(HeadlineLabel);
		this.add(SublineLabel);
		this.add(NameFieldLabel);
		this.add(NameField);
		this.add(MaterialFieldLabel);
		this.add(MaterialField);
		this.add(DescriptionFieldLabel);
		this.add(DescriptionField);
		this.add(CreateBauteilButton);

		/**
		 * Diverse css-Formatierungen
		 */
		HeadlineLabel.setStyleName("headline");
		SublineLabel.setStyleName("subline");
		DescriptionField.setStyleName("DescriptionFieldText");
		CreateBauteilButton.setStyleName("Button");

		/**
		 * Der Create-Button ruft die RPC-Methode auf, welche das Erstellen
		 * eines Bauteils in der DB erm�glicht.
		 */
		CreateBauteilButton.addClickHandler(new CreateClickHandler());

		/**
		 * Abschlie�end wird alles dem RootPanel zugeordnet
		 */
		RootPanel.get("content_wrap").add(this);

	}

	/*
	 * Click Handlers.
	 */

	/**
	 * Hiermit wird die RPC-Methode aufgerufen, die ein Bauteil-Objekt in der
	 * Datenbank anlegt.
	 * 
	 * @author Mario
	 * 
	 */
	private class CreateClickHandler implements ClickHandler {
		@Override
		public void onClick(ClickEvent event) {

			String name = NameField.getText();
			String bauteilBeschreibung = DescriptionField.getText();
			String materialBeschreibung = MaterialField.getText();

			/**
			 * Vor dem Aufruf der RPC-Methode create wird gepr�ft, ob alle
			 * notwendigen Felder bef�llt sind.
			 */
			if (NameField.getText().isEmpty() != true
					&& DescriptionField.getText().isEmpty() != true
					&& MaterialField.getText().isEmpty() != true) {

				/**
				 * Die konkrete RPC-Methode f�r den create-Befehl wird
				 * aufgerufen. Hierbei werden die gew�nschten Werte
				 * mitgeschickt.
				 */
				stuecklistenVerwaltung.createBauteil(name, bauteilBeschreibung,
						materialBeschreibung, new CreateBauteilCallback());

				/**
				 * Nachdem der Create-Vorgang durchgef�hrt wurde, soll die GUI
				 * zur�ck zur �bersichtstabelle weiterleiten.
				 */
				RootPanel.get("content_wrap").clear();
				RootPanel.get("content_wrap").add(new BauteilGeneralView());

			}

			else {

				Window.alert("Bitte alle Felder ausfüllen.");

			}

		}
	}

	/**
	 * Hiermit wird sichergestellt, dass beim (nicht) erfolgreichen
	 * Create-Befehl eine entsprechende Hinweismeldung ausgegeben wird.
	 * 
	 * @author Mario
	 * 
	 */
	class CreateBauteilCallback implements AsyncCallback<Bauteil> {

		@Override
		public void onFailure(Throwable caught) {
			Window.alert("Das Anlegen des Bauteils ist fehlgeschlagen!");
		}

		@Override
		public void onSuccess(Bauteil bauteil) {

			Window.alert("Das Bauteil wurde erfolgreich angelegt.");
		}
	}

}