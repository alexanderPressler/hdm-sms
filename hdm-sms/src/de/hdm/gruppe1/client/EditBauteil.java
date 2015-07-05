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

import de.hdm.gruppe1.shared.FieldVerifier;
import de.hdm.gruppe1.shared.SmsAsync;
import de.hdm.gruppe1.shared.bo.Bauteil;

/**
 * Die Klasse EditBauteil erhält bei Aufruf ein zuvor ausgewähltes
 * Bauteil-Objekt. Dieses kann dann mithilfe dieser Klasse editiert werden.
 * 
 * @author Mario Theiler
 * @version 1.0
 */
public class EditBauteil extends VerticalPanel {

	/**
	 * GUI-Elemente für EditBauteil initialisieren.
	 */
	private final Label HeadlineLabel = new Label("Bauteil ändern");
	private final Label SublineLabel = new Label(
			"Um ein Bauteil zu ändern, füllen Sie bitte alle Felder aus und bestätigen mit dem <editieren>-Button ihre Eingabe.");
	private final Label IdLabel = new Label("Id");
	private final TextBox IdField = new TextBox();
	private final Label NameFieldLabel = new Label("Name");
	private final TextBox NameField = new TextBox();
	private final Label MaterialFieldLabel = new Label("Materialbezeichnung");
	private final TextBox MaterialField = new TextBox();
	private final Label DescriptionFieldLabel = new Label(
			"Textuelle Beschreibung");
	private final TextArea DescriptionField = new TextArea();
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
		 * Das Id-Textfeld darf nicht verändert werden und wird daher auf
		 * "ReadOnly" gesetzt.
		 */
		IdField.setReadOnly(true);

		/**
		 * Diverse css-Formatierungen.
		 */
		HeadlineLabel.setStyleName("headline");
		SublineLabel.setStyleName("subline");
		EditBauteilButton.setStyleName("Button");

		/**
		 * Der Editieren-Button ruft die RPC-Methode auf, welche das Editieren
		 * eines Bauteils in der DB ermöglicht.
		 */
		EditBauteilButton.addClickHandler(new EditClickHandler());

		/**
		 * In ein Textfeld kann nur ein Text geladen werden, kein Inhalt einer int-Variable. Daher ist
		 * dieser Zwischenschritt notwendig: Zwischenspeichern des Werts mithilfe Integer, da Integer
		 * die toString-Methode unterstützt, ein einfacher int jedoch nicht.
		 * 
		 */
		Integer iD = new Integer(editBauteil.getId());

		/**
		 * Mithilfe des an diese Klasse übergebenen Bauteil-Objektes werden die
		 * Textfelder vorbefüllt.
		 */
		IdField.setText(iD.toString());
		NameField.setText(editBauteil.getName());
		MaterialField.setText(editBauteil.getMaterialBeschreibung());
		DescriptionField.setText(editBauteil.getBauteilBeschreibung());

		/**
		 * Abschließend wird die Klasse dem RootPanel zugeordnet.
		 */
		RootPanel.get("content_wrap").add(this);

	}

	/**
	 * Hiermit wird die RPC-Methode aufgerufen, die mithilfe eines
	 * mitgeschickten Bauteil-Objektes das bestehende Bauteil-Objekt in der
	 * Datenbank ändert. Hierbei ist wichtig, dass keine neue Id vergeben wird,
	 * da es sich sonst um eine Neuanlage und nicht um einen Editier-Vorgang
	 * handeln würde.
	 * 
	 * @author Mario Theiler
	 * 
	 */
	private class EditClickHandler implements ClickHandler {
		@Override
		public void onClick(ClickEvent event) {

			/**
			 * Vor dem Aufruf der RPC-Methode create wird geprüft, ob alle notwendigen Felder befüllt sind.
			 */
			if (NameField.getText().isEmpty() != true
					&& DescriptionField.getText().isEmpty() != true
					&& MaterialField.getText().isEmpty() != true) {
				
				FieldVerifier umlaut = new FieldVerifier();
				String inputName = umlaut.changeUmlaut(NameField.getText());
				String inputDescription = umlaut.changeUmlaut(DescriptionField.getText());
				String inputMaterial = umlaut.changeUmlaut(MaterialField.getText());
				
				/**
				 * Dieses Bauteil-Objekt wird erstellt, um es in den darauffolgenden Zeilen
				 * mit Inhalten aus den individuellen User-Eingaben in den Textfeldern zu
				 * befüllen und dieses anschließend dem RPC mit zu geben.
				 */
				Bauteil b = new Bauteil();
				/**
				 * Aus einem Textfeld kann kein Integer-Wert ausgelesen werden,
				 * daher ist dieser Zwischenschritt notwendig: Auslesen des Id-Werts
				 * mithilfe Integer, da Integer die toString-Methode unterstützt.
				 */
				b.setId(Integer.parseInt(IdField.getText()));
				b.setName(inputName);
				b.setBauteilBeschreibung(inputDescription);
				b.setMaterialBeschreibung(inputMaterial);

				/**
				 * Die konkrete RPC-Methode für den editier-Befehl wird aufgerufen.
				 * Hierbei wird das vorab befüllte Bauteil-Objekt mit den
				 * gewünschten Werten mitgeschickt.
				 */
				stuecklistenVerwaltung.save(b, new SaveCallback());

				/**
				 * Nachdem der Editier-Vorgang durchgeführt wurde, soll die GUI
				 * zurück zur Übersichtstabelle weiterleiten.
				 */
				RootPanel.get("content_wrap").clear();
				RootPanel.get("content_wrap").add(new BauteilGeneralView());

			}

			/**
			 * Falls nicht alle Felder ordnungsgemäß befüllt sind, wird dem User
			 * folgende Hinweismeldung angezeigt.
			 */
			else {

				Window.alert("Bitte alle Felder ausfüllen.");

			}

		}
	}

	/**
	 * Hiermit wird sichergestellt, dass beim (nicht) erfolgreichen
	 * Update-Befehl eine entsprechende Hinweismeldung ausgegeben wird.
	 * 
	 * @author Mario Theiler
	 * 
	 */
	class SaveCallback implements AsyncCallback<Void> {

		@Override
		public void onFailure(Throwable caught) {
			Window.alert(caught.getMessage());
		}

		@Override
		public void onSuccess(Void result) {
			Window.alert("Das Bauteil wurde erfolgreich editiert.");

		}
	}

}
