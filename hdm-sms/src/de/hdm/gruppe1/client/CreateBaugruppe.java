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
import de.hdm.gruppe1.shared.bo.Baugruppe;
import de.hdm.gruppe1.shared.bo.Stueckliste;

/**
 * Mithilfe der Klasse CreateBauteil wird dem User der Applikation ermöglicht,
 * ein Bauteil-Objekt in der Datenbank anzulegen.
 * 
 */

public class CreateBaugruppe extends VerticalPanel {
	
	/**
	 * GUI-Elemente für CreateBaugruppe initialisieren
	 */
	
	private final Label HeadlineLabel = new Label ("Baugruppe anlegen");
	private final Label SublineLabel = new Label ("Um eine Baugruppe anzulegen, füllen Sie bitte alle Felder aus und bestätigen mit dem <anlegen>-Button ihre Eingabe.");
	private final Label NameFieldLabel = new Label ("Name");
	private final TextBox NameField = new TextBox ();
	private final Button CreateBaugruppeButton = new Button ("anlegen");
	
	/**
	 * Remote Service via ClientsideSettings wird an dieser Stelle einmalig in
	 * der Klasse aufgerufen. Im Anschluss kann jederzeit darauf zugegriffen
	 * werden.
	 */
	SmsAsync stuecklistenVerwaltung = ClientsideSettings.getSmsVerwaltung();
	
	public CreateBaugruppe(){
		
		/**
		 * Bei Instantiierung der Klasse wird alles dem VerticalPanel
		 * zugeordnet, da diese Klasse von VerticalPanel erbt.
		 */

		this.add(HeadlineLabel);
		this.add(SublineLabel);
		this.add(NameFieldLabel);
		this.add(NameField);
		this.add(CreateBaugruppeButton);
		
		/**
		 * Diverse css-Formatierungen
		 */
		
		HeadlineLabel.setStyleName("headline");
		SublineLabel.setStyleName("subline");
		CreateBaugruppeButton.setStyleName("Button");
		
		/**
		 * Der Create-Button ruft die RPC-Methode auf, welche das Erstellen
		 * einer Baugruppe in der DB ermöglicht.
		 */

		CreateBaugruppeButton.addClickHandler(new CreateClickHandler());
		
		/**
		 * Abschließend wird alles dem RootPanel zugeordnet
		 */
		
		RootPanel.get("content_wrap").add(this);
		
	}
	
	/*
	 * Click Handlers.
	 */
	
	/**
	 * Hiermit wird die RPC-Methode aufgerufen, die ein Baugruppen-Objekt in der
	 * Datenbank anlegt.
	 */

	private class CreateClickHandler implements ClickHandler {
		@Override
		public void onClick(ClickEvent event) {

			String name = NameField.getText();
			
			/**
			 * Vor dem Aufruf der RPC-Methode create wird geprüft, ob alle
			 * notwendigen Felder befüllt sind.
			 */
			
			if (NameField.getText().isEmpty() != true ){

				/**
				 * Die konkrete RPC-Methode für den create-Befehl wird
				 * aufgerufen. Hierbei werden die gewünschten Werte
				 * mitgeschickt.
				 */
			
				//TODO KLÄREN WEGEN STUECKLISTE = null
				
			stuecklistenVerwaltung.createBaugruppe(name, null, new CreateBaugruppeCallback());
			
			/**
			 * Nachdem der Create-Vorgang durchgeführt wurde, soll die GUI
			 * zurück zur Übersichtstabelle weiterleiten.
			 */
			
			 RootPanel.get("content_wrap").clear();
			 RootPanel.get("content_wrap").add(new BaugruppeGeneralView());
			
		} else {

			Window.alert("Bitte alle Felder ausfüllen.");

		}

	}
}
	
	/**
	 * Hiermit wird sichergestellt, dass beim (nicht) erfolgreichen
	 * Create-Befehl eine entsprechende Hinweismeldung ausgegeben wird.
	 */
	
	class CreateBaugruppeCallback implements AsyncCallback<Baugruppe> {

		@Override
		public void onFailure(Throwable caught) {
			Window.alert("Das Anlegen der Baugruppe ist fehlgeschlagen!");
		}

		@Override
		public void onSuccess(Baugruppe baugruppe) {

			Window.alert("Die Baugruppe wurde erfolgreich angelegt.");
			
	
		
		}
	}

}
		
		
