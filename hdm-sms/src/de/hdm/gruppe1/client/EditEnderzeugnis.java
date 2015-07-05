package de.hdm.gruppe1.client;

import java.util.Vector;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

import de.hdm.gruppe1.client.CreateStueckliste.CreateStuecklisteCallback;
import de.hdm.gruppe1.client.EditBaugruppe.SaveCallback;
import de.hdm.gruppe1.shared.FieldVerifier;
import de.hdm.gruppe1.shared.SmsAsync;
import de.hdm.gruppe1.shared.bo.Baugruppe;
import de.hdm.gruppe1.shared.bo.Bauteil;
import de.hdm.gruppe1.shared.bo.ElementPaar;
import de.hdm.gruppe1.shared.bo.Enderzeugnis;
import de.hdm.gruppe1.shared.bo.Stueckliste;

/**
 * Die Klasse CreateEnderzeugnis ermöglicht dem User, Objekte von Baugruppen in
 * der Datenbank als Enderzeugnisse mit entsprechender Referenz zueinander abzuspeichern.
 * 
 * @author Mario Theiler
 * @version 1.0
 */
public class EditEnderzeugnis extends VerticalPanel {
	
	/**
	 * GUI-Elemente für EditStueckliste initialisieren
	 */
	private final Label HeadlineLabel = new Label("Enderzeugnis ändern");
	private final Label SublineLabel = new Label(
			"Um ein Enderzeugnis zu ändern, ändern Sie bitte den Namen und bestätigen mit dem <editieren>-Button ihre Eingabe. Den Inhalt des Enderzeugnisses <p>müssen Sie innerhalb der zugehörigen Baugruppe ändern.");
	private final Label IdLabel = new Label("Id");
	private final TextBox IdField = new TextBox();
	private final Label BgId = new Label("Zugehörige Baugruppe");
	private final TextBox BgIdField = new TextBox();
	private final TextBox BgNameField = new TextBox();
	private final Label nameLabel = new Label("Name eintragen");
	private final TextBox NameField = new TextBox();
	private final Label BaugruppeLabel = new Label("Gewünschte Baugruppe ändern");
	private final Button EditEnderzeugnisButton = new Button("ändern");
	
	// Vektor wird mit allen Baugruppen aus der DB befüllt
	Vector<Baugruppe> allBaugruppen = new Vector<Baugruppe>();
	
	//Horizontales Anordnen der Bauteil-Id mit Bauteil-Name
	HorizontalPanel baugruppePanel = new HorizontalPanel();
	
	// Remote Service via ClientsideSettings
	SmsAsync stuecklistenVerwaltung = ClientsideSettings.getSmsVerwaltung();
	
	public EditEnderzeugnis(Enderzeugnis editEnderzeugnis) {
		
		baugruppePanel.add(BgIdField);
		baugruppePanel.add(BgNameField);
		
		/**
		 * Nachdem alle Elemente geladen sind, wird alles dem VerticalPanel
		 * zugeordnet, da diese Klasse von VerticalPanel erbt.
		 */
		this.add(HeadlineLabel);
		this.add(SublineLabel);
		this.add(IdLabel);
		this.add(IdField);
		this.add(BgId);
		this.add(baugruppePanel);
		this.add(nameLabel);
		this.add(NameField);
		this.add(BaugruppeLabel);
		this.add(EditEnderzeugnisButton);
		
		/**
		 * Das Id-Textfeld darf nicht verändert werden und wird daher auf
		 * "ReadOnly" gesetzt.
		 */
		IdField.setReadOnly(true);
		BgIdField.setReadOnly(true);
		BgNameField.setReadOnly(true);
		
		/**
		 * Diverse css-Formatierungen
		 */
		HeadlineLabel.setStyleName("headline");
		SublineLabel.setStyleName("subline");
		EditEnderzeugnisButton.setStyleName("Button");
		BgIdField.setWidth("50px");
		
		/**
		 * Der Create-Button ruft die RPC-Methode auf, welche das Editieren
		 * eines Enderzeugnisses in der DB ermöglicht.
		 */
		EditEnderzeugnisButton.addClickHandler(new EditClickHandler());
		
		/**
		 * In ein Textfeld kann nur ein Text geladen werden, kein int. Daher ist
		 * dieser Zwischenschritt notwendig: Zwischenspeichern des Werts
		 * mithilfe Integer, da Integer die toString-Methode unterstützt, ein
		 * einfacher int jedoch nicht.
		 * 
		 */
		Integer iD = new Integer(editEnderzeugnis.getId());
		Integer bGiD = new Integer(editEnderzeugnis.getBaugruppe().getId());

		/**
		 * Mithilfe des an diese Klasse übergebenen Stücklisten-Objektes werden
		 * die Textfelder befüllt.
		 */
		IdField.setText(iD.toString());
		NameField.setText(editEnderzeugnis.getName());
		BgIdField.setText(bGiD.toString());
		BgNameField.setText(editEnderzeugnis.getBaugruppe().getName());
		
		/**
		 * Abschließend wird alles dem RootPanel zugeordnet
		 */
		RootPanel.get("content_wrap").add(this);
		
	}
	
	/*
	 * Click Handlers.
	 */
	
	/**
	 * Hiermit wird die RPC-Methode aufgerufen, die mithilfe eines
	 * mitgeschickten Enderzeugnis-Objektes das bestehende Enderzeugnis-Objekt in
	 * der Datenbank ändert. Hierbei ist wichtig, dass keine neue Id vergeben
	 * wird, da es sich sonst um eine Neuanlage und nicht um einen
	 * Editier-Vorgang handeln würde.
	 * 
	 * @author Mario
	 * 
	 */
	private class EditClickHandler implements ClickHandler {
		@Override
		public void onClick(ClickEvent event) {

			/**
			 * Vor dem Aufruf der RPC-Methode create wird geprüft, ob alle
			 * notwendigen Felder befüllt sind.
			 */
			if (NameField.getText().isEmpty() != true) {
				
				FieldVerifier umlaut = new FieldVerifier();
				String input = umlaut.changeUmlaut(NameField.getText());

				Enderzeugnis e = new Enderzeugnis();
				/**
				 * Aus einem Textfeld kann kein Integer-Wert ausgelesen werden,
				 * daher ist dieser Zwischenschritt notwendig: Auslesen des Id-Werts
				 * mithilfe Integer, da Integer die toString-Methode unterstützt.
				 */
				e.setId(Integer.parseInt(IdField.getText()));
				e.setName(input);
				
				Baugruppe b = new Baugruppe();
				b.setId(Integer.parseInt(BgIdField.getText()));
				b.setName(BgNameField.getText());
				
				e.setBaugruppe(b);
				
					/**
					 * Die konkrete RPC-Methode für den Update-Befehl wird
					 * aufgerufen. Hierbei wird das gewünschte Objekt
					 * mitgeschickt.
					 */
					
					//TODO implementieren
					stuecklistenVerwaltung.saveEnderzeugnis(e, new SaveCallback());
					
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
	 * Update-Befehl eine entsprechende Hinweismeldung ausgegeben wird.
	 * 
	 * @author Mario
	 * 
	 */
	class SaveCallback implements AsyncCallback<Void> {

		@Override
		public void onFailure(Throwable caught) {
			Window.alert("Das Enderzeugnis wurde nicht editiert.");
		}

		@Override
		public void onSuccess(Void result) {

			Window.alert("Das Enderzeugnis wurde erfolgreich editiert.");
		}
	}

}
