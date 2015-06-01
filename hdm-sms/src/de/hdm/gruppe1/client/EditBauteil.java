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
import de.hdm.gruppe1.shared.bo.Bauteil;

/**
 * Die Klasse EditBauteil erh�lt bei Aufruf ein zuvor ausgew�hltes
 * Bauteil-Objekt. Dieses kann dann mithilfe dieser Klasse editiert werden.
 * 
 * @author Mario Theiler
 * @version 1.0
 */
public class EditBauteil extends VerticalPanel {

	/**
	 * GUI-Elemente f�r EditBauteil initialisieren
	 */
	private final Label HeadlineLabel = new Label("Bauteil ändern");
	private final Label SublineLabel = new Label(
			"Um ein Bauteil zu �ndern, füllen Sie bitte alle Felder aus und bestätigen mit dem <editieren>-Button ihre Eingabe.");
	private final Label IdLabel = new Label("Id");
	private final TextBox IdField = new TextBox();
	private final Label NameFieldLabel = new Label("Bezeichnung");
	private final TextBox NameField = new TextBox();
	private final Label MaterialFieldLabel = new Label("Materialbezeichnung");
	private final TextBox MaterialField = new TextBox();
	private final Label DescriptionFieldLabel = new Label(
			"Textuelle Beschreibung");
	private final TextBox DescriptionField = new TextBox();
	private final Button EditBauteilButton = new Button("ändern");

	/**
	 * Remote Service via ClientsideSettings wird an dieser Stelle einmalig in
	 * der Klasse aufgerufen. Im Anschluss kann jederzeit darauf zugegriffen
	 * werden.
	 */
	SmsAsync stuecklistenVerwaltung = ClientsideSettings.getSmsVerwaltung();

	public EditBauteil(Bauteil editBauteil) {

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
		this.add(MaterialFieldLabel);
		this.add(MaterialField);
		this.add(DescriptionFieldLabel);
		this.add(DescriptionField);
		this.add(EditBauteilButton);

		/**
		 * Das Id-Textfeld darf nicht ver�ndert werden und wird daher auf
		 * "ReadOnly" gesetzt.
		 */
		IdField.setReadOnly(true);

		/**
		 * Diverse css-Formatierungen
		 */
		DescriptionField.setStyleName("DescriptionFieldText");
		EditBauteilButton.setStyleName("Button");

		/**
		 * Der Editieren-Button ruft die RPC-Methode auf, welche das Editieren
		 * eines Bauteils in der DB erm�glicht.
		 */
		EditBauteilButton.addClickHandler(new EditClickHandler());

		/**
		 * In ein Textfeld kann nur ein Text geladen werden, kein int. Daher ist
		 * dieser Zwischenschritt notwendig: Zwischenspeichern des Werts
		 * mithilfe Integer, da Integer die toString-Methode unterst�tzt, ein
		 * einfacher int jedoch nicht.
		 * 
		 */
		Integer iD = new Integer(editBauteil.getId());

		/**
		 * Mithilfe des an diese Klasse �bergebenen Bauteil-Objektes werden die
		 * Textfelder bef�llt.
		 */
		IdField.setText(iD.toString());
		NameField.setText(editBauteil.getName());
		MaterialField.setText(editBauteil.getMaterialBeschreibung());
		DescriptionField.setText(editBauteil.getBauteilBeschreibung());

		/**
		 * Abschlie�end wird alles dem RootPanel zugeordnet
		 */
		RootPanel.get("content_wrap").add(this);

	}

	/*
	 * Click Handlers.
	 */

	/**
	 * Hiermit wird die RPC-Methode aufgerufen, die mithilfe eines
	 * mitgeschickten Bauteil-Objektes das bestehende Bauteil-Objekt in der
	 * Datenbank �ndert. Hierbei ist wichtig, dass keine neue Id vergeben wird,
	 * da es sich sonst um eine Neuanlage und nicht um einen Editier-Vorgang
	 * handeln w�rde.
	 * 
	 * @author Mario
	 * 
	 */
	private class EditClickHandler implements ClickHandler {
		@Override
		public void onClick(ClickEvent event) {

			Bauteil b = new Bauteil();
			/**
			 * Aus einem Textfeld kann kein Integer-Wert ausgelesen werden,
			 * daher ist dieser Zwischenschritt notwendig: Auslesen des Id-Werts
			 * mithilfe Integer, da Integer die toString-Methode unterst�tzt.
			 */
			b.setId(Integer.parseInt(IdField.getText()));
			b.setName(NameField.getText());
			b.setBauteilBeschreibung(DescriptionField.getText());
			b.setMaterialBeschreibung(MaterialField.getText());

			/**
			 * Vor dem Aufruf der RPC-Methode create wird gepr�ft, ob alle notwendigen Felder bef�llt sind.
			 */
			if (NameField.getText().isEmpty() != true
					&& DescriptionField.getText().isEmpty() != true
					&& MaterialField.getText().isEmpty() != true) {

				/**
				 * Die konkrete RPC-Methode f�r den editier-Befehl wird aufgerufen.
				 * Hierbei wird das vorab bef�llte Bauteil-Objekt mit den
				 * gew�nschten Werten mitgeschickt.
				 */
				stuecklistenVerwaltung.save(b, new SaveCallback());

				/**
				 * Nachdem der Editier-Vorgang durchgef�hrt wurde, soll die GUI
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
	 * Update-Befehl eine entsprechende Hinweismeldung ausgegeben wird.
	 * 
	 * @author Mario
	 * 
	 */
	class SaveCallback implements AsyncCallback<Void> {

		@Override
		public void onFailure(Throwable caught) {
			Window.alert("Das Bauteil wurde nicht editiert.");
		}

		@Override
		public void onSuccess(Void result) {
			Window.alert("Das Bauteil wurde erfolgreich editiert.");

		}
	}

}