package de.hdm.gruppe1.client;

import java.util.Vector;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

import de.hdm.gruppe1.shared.FieldVerifier;
import de.hdm.gruppe1.shared.SmsAsync;
import de.hdm.gruppe1.shared.bo.Baugruppe;
import de.hdm.gruppe1.shared.bo.Enderzeugnis;

/**
 * Die Klasse CreateEnderzeugnis ermöglicht dem User, Objekte von Baugruppen in
 * der Datenbank als Enderzeugnisse mit entsprechender Referenz zueinander abzuspeichern.
 * 
 * @author Mario Theiler
 * @version 1.0
 */
public class CreateEnderzeugnis extends VerticalPanel {
	
	/**
	 * GUI-Elemente für CreateEnderzeugnis initialisieren.
	 */
	private final Label HeadlineLabel = new Label("Enderzeugnis anlegen");
	private final Label SublineLabel = new Label(
			"Um ein Enderzeugnis anzulegen, wählen Sie bitte eine zugehörige Baugruppe aus. Außerdem müssen Sie " +
			"dem Enderzeugnis einen Namen vergeben.");
	private final Label nameLabel = new Label("Name eintragen");
	private final TextBox NameField = new TextBox();
	private final Label BaugruppeLabel = new Label("Gewünschte Baugruppe hinzufügen");
	ListBox listBoxBaugruppen = new ListBox();
	private final Button CreateEnderzeugnisButton = new Button("Enderzeugnis anlegen");
	
	/**
	 *  Vektor wird mit allen Bauteilen bzw. Baugruppen aus der DB befüllt.
	 */
	Vector<Baugruppe> allBaugruppen = new Vector<Baugruppe>();
	
	/**
	 * Remote Service via ClientsideSettings wird an dieser Stelle einmalig in
	 * der Klasse aufgerufen. Im Anschluss kann jederzeit darauf zugegriffen
	 * werden.
	 */
	SmsAsync stuecklistenVerwaltung = ClientsideSettings.getSmsVerwaltung();
	
	public CreateEnderzeugnis() {
		
		/**
		 * TextBox wird mit Text vorbefüllt, der ausgeblendet wird, sobald
		 * die TextBox vom User fokussiert wird.
		 */
		NameField.getElement().setPropertyString("placeholder", "Name");
		
		/**
		 * RPC-Methode ausführen, die alle Baugruppen-Objekte aus der Datenbank in
		 * einem Vektor zurückliefert. Dadurch wird der Klassen-Vektor
		 * "allBaugruppen" befüllt.
		 */
		stuecklistenVerwaltung.getAllBaugruppen(new GetAllBaugruppenCallback());
		
		/**
		 * Nachdem alle Elemente geladen sind, wird alles dem VerticalPanel
		 * zugeordnet, da diese Klasse von VerticalPanel erbt.
		 */
		this.add(HeadlineLabel);
		this.add(SublineLabel);
		this.add(nameLabel);
		this.add(NameField);
		this.add(BaugruppeLabel);
		this.add(listBoxBaugruppen);
		this.add(CreateEnderzeugnisButton);
		
		/**
		 * Diverse css-Formatierungen
		 */
		HeadlineLabel.setStyleName("headline");
		SublineLabel.setStyleName("subline");
		CreateEnderzeugnisButton.setStyleName("Button");
		
		/**
		 * Der Create-Button ruft die RPC-Methode auf, welche das Erstellen
		 * eines Enderzeugnisses in der DB ermöglicht.
		 */
		CreateEnderzeugnisButton.addClickHandler(new CreateClickHandler());
		
		/**
		 * Abschließend wird alles dem RootPanel zugeordnet
		 */
		RootPanel.get("content_wrap").add(this);
		
	}
	
	/*
	 * Click Handlers.
	 */
	
	/**
	 * Hiermit wird die RPC-Methode aufgerufen, die einen Vektor von allen in
	 * der DB vorhandenen Baugruppen liefert. Die Klasse ist eine nested-class
	 * und erlaubt daher, auf die Attribute der übergeordneten Klasse
	 * zuzugreifen.
	 * 
	 * @author Mario
	 * 
	 */
	class GetAllBaugruppenCallback implements AsyncCallback<Vector<Baugruppe>> {

		@Override
		public void onFailure(Throwable caught) {
			Window.alert("Baugruppen konnten nicht geladen werden");
		}

		@Override
		public void onSuccess(Vector<Baugruppe> alleBaugruppen) {

			/**
			 * Der Baugruppen-Vektor allBaugruppen wird mit dem Ergebnis dieses RPC´s
			 * befüllt.
			 */
			allBaugruppen = alleBaugruppen;

			if (allBaugruppen.isEmpty() == true) {

				Window.alert("Es sind leider keine Daten in der Datenbank vorhanden.");

			} else {

				/**
				 * Die Schleife durchläuft den kompletten Ergebnis-Vektor.
				 */
				for (int c = 0; c <= allBaugruppen.size(); c++) {

					/**
					 * Das DropDown wird mithilfe dieser for-Schleife für jede
					 * Baugruppe mit dessen Namen befüllt.
					 */
					listBoxBaugruppen.addItem(allBaugruppen.get(c).getName());

				}

			}

		}
	}
	
	/**
	 * Hiermit wird die RPC-Methode aufgerufen, die ein Enderzeugnis-Objekt in
	 * der Datenbank anlegt.
	 * 
	 * @author Mario
	 * 
	 */
	private class CreateClickHandler implements ClickHandler {
		@Override
		public void onClick(ClickEvent event) {

			/**
			 * Vor dem Aufruf der RPC-Methode create wird geprüft, ob alle
			 * notwendigen Felder befüllt sind.
			 */
			if (NameField.getText().isEmpty() != true) {
				
				FieldVerifier umlaut = new FieldVerifier();
				String input = umlaut.changeUmlaut(NameField.getText());
				
				/**
				 * Die konkrete RPC-Methode für den create-Befehl wird
				 * aufgerufen. Hierbei werden die gewünschten Werte
				 * mitgeschickt.
				 */
				String nameEnderzeugnis = input;
				
				// Der index dient dazu, herauszufinden, welches Element im
				// DropDown ausgewählt wurde
				final int index = listBoxBaugruppen.getSelectedIndex();
				
				// Dem Enderzeugnis wird ein Objekt von Baugruppe hinzugefügt,
				// welches in den folgenden Zeilen mit einer Stückliste befüllt wird
				Baugruppe b = new Baugruppe();
				b.setId(allBaugruppen.get(index).getId());

				stuecklistenVerwaltung.createEnderzeugnis(nameEnderzeugnis, b, new CreateEnderzeugnisCallback());

				/**
				 * Nachdem der Create-Vorgang durchgeführt wurde, soll die GUI
				 * zurück zur Übersichtstabelle weiterleiten.
				 */
				RootPanel.get("content_wrap").clear();
				RootPanel.get("content_wrap").add(new EnderzeugnisGeneralView());

			}

			else {

				Window.alert("Bitte Namensfeld ausfüllen.");

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
	class CreateEnderzeugnisCallback implements AsyncCallback<Enderzeugnis> {

		@Override
		public void onFailure(Throwable caught) {
			Window.alert(caught.getMessage());
		}

		@Override
		public void onSuccess(Enderzeugnis enderzeugnis) {

			Window.alert("Das Enderzeugnis wurde erfolgreich angelegt.");
		}
	}

}