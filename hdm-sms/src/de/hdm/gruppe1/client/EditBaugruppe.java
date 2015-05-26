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
import de.hdm.gruppe1.shared.SmsAsync;
import de.hdm.gruppe1.shared.bo.Baugruppe;

/**
 * Die Klasse EditBaugruppe erhält bei Aufruf ein zuvor ausgewähltes
 * Baugruppen-Objekt. Dieses kann dann mithilfe dieser Klasse editiert werden.
 */

public class EditBaugruppe extends VerticalPanel {

	/**
	 * GUI-Elemente um EditBaugruppe initialisieren
	 */
		private final Label HeadlineLabel = new Label ("Baugruppe ändern");
		private final Label SublineLabel = new Label ("Um ein Baugruppe zu ändern, fbüllen Sie bitte alle Felder aus und bestätigen mit dem <editieren>-Button ihre Eingabe.");
		private final Label IdLabel = new Label("Id");
		private final TextBox IdField = new TextBox();
		private final Label NameFieldLabel = new Label ("Bezeichnung");
		private final TextBox NameField = new TextBox ();
		private final TextBox DescriptionField = new TextBox ();
		private final Button EditBaugruppeButton = new Button ("ändern");
		
		/**
		 * Remote Service via ClientsideSettings wird an dieser Stelle einmalig in
		 * der Klasse aufgerufen. Im Anschluss kann jederzeit darauf zugegriffen
		 * werden.
		 */
		SmsAsync stuecklistenVerwaltung = ClientsideSettings.getSmsVerwaltung();
		
		public EditBaugruppe (Baugruppe editBaugruppe) {
			
			/**
			 * Bei Instantiierung der Klasse wird alles dem VerticalPanel
			 * zugeordnet, da diese Klasse von VerticalPanel erbt.
			 */
			
			this.add(HeadlineLabel);
			this.add(SublineLabel);
			this.add(IdLabel);
			this.add(IdField);
			this.add(NameFieldLabel);
			this.add(NameField);
			this.add(DescriptionField);
			this.add(EditBaugruppeButton);
			
			/**
			 * Das Id-Textfeld darf nicht verändert werden und wird daher auf
			 * "ReadOnly" gesetzt.
			 */
			IdField.setReadOnly(true);

			/**
			 * Diverse css-Formatierungen
			 */
			
			DescriptionField.setStyleName("DescriptionFieldText");
			EditBaugruppeButton.setStyleName("Button");
			
			/**
			 * Der Editieren-Button ruft die RPC-Methode auf, welche das Editieren
			 * einer Baugruppe in der DB ermöglicht.
			 */
			
			EditBaugruppeButton.addClickHandler(new EditClickHandler());
			
			/**
			 * In ein Textfeld kann nur ein Text geladen werden, kein int. Daher ist
			 * dieser Zwischenschritt notwendig: Zwischenspeichern des Werts
			 * mithilfe Integer, da Integer die toString-Methode unterstützt, ein
			 * einfacher int jedoch nicht.
			 * 
			 */
			
			Integer iD = new Integer(editBaugruppe.getId());

			/**
			 * Mithilfe des an diese Klasse übergebenen Baugruppen-Objektes werden die
			 * Textfelder befüllt.
			 */
			
			IdField.setText(iD.toString());
			NameField.setText(editBaugruppe.getName());

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
		 * mitgeschickten Baugruppen-Objektes das bestehende Baugruppen-Objekt in der
		 * Datenbank ändert. Hierbei ist wichtig, dass keine neue Id vergeben wird,
		 * da es sich sonst um eine Neuanlage und nicht um einen Editier-Vorgang
		 * handeln würde.
		 */
		
		
		 private class EditClickHandler implements ClickHandler {
		  @Override
		  public void onClick(ClickEvent event) {

			  Baugruppe bg = new Baugruppe();
			
				bg.setId(Integer.parseInt(IdField.getText()));
				bg.setName(NameField.getText());

				/**
				 * Vor dem Aufruf der RPC-Methode create wird geprüft, ob alle notwendigen Felder befüllt sind.
				 */
				if (NameField.getText().isEmpty() != true) {

					/**
					 * Die konkrete RPC-Methode für den editier-Befehl wird aufgerufen.
					 * Hierbei wird das vorab befüllte Baugruppen-Objekt mit den
					 * gewünschten Werten mitgeschickt.
					 */
					stuecklistenVerwaltung.save(bg, new SaveCallback());

					/**
					 * Nachdem der Editier-Vorgang durchgeführt wurde, soll die GUI
					 * zurück zur Übersichtstabelle weiterleiten.
					 */
					RootPanel.get("content_wrap").clear();
					RootPanel.get("content_wrap").add(new BaugruppeGeneralView());

				}
				
				else {

					Window.alert("Bitte alle Felder ausfüllen.");

				}

			}
		}

		/**
		 * Hiermit wird sichergestellt, dass beim (nicht) erfolgreichen
		 * Update-Befehl eine entsprechende Hinweismeldung ausgegeben wird.
		 */

		 class SaveCallback implements AsyncCallback<Void> {

				@Override
				public void onFailure(Throwable caught) {
					Window.alert("Das Baugruppe wurde nicht editiert.");
				}

				@Override
				public void onSuccess(Void result) {
					Window.alert("Das Baugruppe wurde erfolgreich editiert.");

				}
			}

		}