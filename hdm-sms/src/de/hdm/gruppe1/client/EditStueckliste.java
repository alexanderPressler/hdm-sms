package de.hdm.gruppe1.client;

import java.util.Vector;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import de.hdm.gruppe1.shared.FieldVerifier;
import de.hdm.gruppe1.shared.SmsAsync;
import de.hdm.gruppe1.shared.bo.Baugruppe;
import de.hdm.gruppe1.shared.bo.Bauteil;
import de.hdm.gruppe1.shared.bo.ElementPaar;
import de.hdm.gruppe1.shared.bo.Stueckliste;

/**
 * Die Klasse EditStueckliste erhält bei Aufruf ein zuvor ausgewähltes
 * Stücklisten-Objekt. Dieses kann dann mithilfe dieser Klasse editiert werden.
 * 
 * @author Mario Theiler
 * @version 1.0
 */
public class EditStueckliste extends VerticalPanel {

	/**
	 * GUI-Elemente für EditStueckliste initialisieren
	 */
	private final Label HeadlineLabel = new Label("Stückliste ändern");
	private final Label SublineLabel = new Label(
			"Um eine Stückliste zu ändern, füllen Sie bitte alle Felder aus und bestätigen mit dem <editieren>-Button ihre Eingabe.");
	private final Label bauteilLabel = new Label("Bauteile für Stückliste");
	private final Label baugruppeLabel = new Label("Baugruppen für Stückliste");
	private final Label IdLabel = new Label("Id");
	private final TextBox IdField = new TextBox();
	private final Label NameFieldLabel = new Label("Bezeichnung");
	private final TextBox NameField = new TextBox();
	private final Label BauteilLabel = new Label(
			"Gewünschte Anzahl von Bauteilen hinzufügen");
	private final Label BaugruppeLabel = new Label(
			"Gewünschte Anzahl von Baugruppen hinzufügen");
	private final TextBox amountBauteile = new TextBox();
	ListBox listBoxBauteile = new ListBox();
	private final Button collectBtButton = new Button("hinzufügen");
	private final TextBox amountBaugruppen = new TextBox();
	ListBox listBoxBaugruppen = new ListBox();
	private final Button collectBgButton = new Button("hinzufügen");
	private final Button EditStuecklisteButton = new Button("ändern");

	// Panels, um die hinzufügen-Buttons neben den Dropdowns zu platzieren
	HorizontalPanel btPanel = new HorizontalPanel();
	HorizontalPanel bgPanel = new HorizontalPanel();

	// Vektor wird mit allen Bauteilen bzw. Baugruppen aus der DB befüllt
	Vector<Bauteil> allBauteile = new Vector<Bauteil>();
	Vector<Baugruppe> allBaugruppen = new Vector<Baugruppe>();

	// Vektoren, um die hinzugefügten Bauteile/Baugruppen in einer Übersicht zu
	// sammeln, bevor die Stückliste gespeichert wird
	Vector<ElementPaar> collectBauteile = new Vector<ElementPaar>();
	Vector<ElementPaar> collectBaugruppen = new Vector<ElementPaar>();

	// Tabellen, um in der GUI alle Bauteile/Baugruppen anzuzeigen, bevor die
	// Stückliste gespeichert wird
	FlexTable bauteilCollection = new FlexTable();
	FlexTable baugruppeCollection = new FlexTable();

	/**
	 * Remote Service via ClientsideSettings wird an dieser Stelle einmalig in
	 * der Klasse aufgerufen. Im Anschluss kann jederzeit darauf zugegriffen
	 * werden.
	 */
	SmsAsync stuecklistenVerwaltung = ClientsideSettings.getSmsVerwaltung();

	public EditStueckliste(Stueckliste editStueckliste) {

		// TextBoxen werden mit Text vorbefüllt, der ausgeblendet wird, sobald
		// die TextBox vom User fokussiert wird
		amountBauteile.getElement().setPropertyString("placeholder", "Anzahl");
		amountBaugruppen.getElement()
				.setPropertyString("placeholder", "Anzahl");

		// ClickHandler um zu prüfen, ob die Texteingabe numerisch ist
		collectBtButton.addClickHandler(new numericBtHandler());
		collectBgButton.addClickHandler(new numericBgHandler());

		// Die erste Reihe der Tabelle wird mit Überschriften vordefiniert
		bauteilCollection.setText(0, 0, "ID");
		bauteilCollection.setText(0, 1, "Anzahl");
		bauteilCollection.setText(0, 2, "Name");
		bauteilCollection.setText(0, 3, "Entfernen");

		baugruppeCollection.setText(0, 0, "ID");
		baugruppeCollection.setText(0, 1, "Anzahl");
		baugruppeCollection.setText(0, 2, "Name");
		baugruppeCollection.setText(0, 3, "Entfernen");

		// css für Tabelle definieren
		bauteilCollection.setStyleName("tableBody");
		baugruppeCollection.setStyleName("tableBody");

		// Das FlexTable Widget unterstützt keine Headlines. Daher wird die
		// erste Reihe über folgenden Umweg formatiert
		bauteilCollection.getCellFormatter().addStyleName(0, 0, "tableHead");
		bauteilCollection.getCellFormatter().addStyleName(0, 1, "tableHead");
		bauteilCollection.getCellFormatter().addStyleName(0, 2, "tableHead");
		bauteilCollection.getCellFormatter().addStyleName(0, 3, "tableHead");

		baugruppeCollection.getCellFormatter().addStyleName(0, 0, "tableHead");
		baugruppeCollection.getCellFormatter().addStyleName(0, 1, "tableHead");
		baugruppeCollection.getCellFormatter().addStyleName(0, 2, "tableHead");
		baugruppeCollection.getCellFormatter().addStyleName(0, 3, "tableHead");

		// Um das Dropdown mit Bauteilen aus der DB zu befüllen, wird dieser
		// RPC-Aufruf gestartet
		stuecklistenVerwaltung.getAllBauteile(new GetAllBauteileCallback());

		// Mithilfe des Hinzufügen-Buttons wird die BauteilCollection Tabelle
		// befüllt
		collectBtButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {

				// Der index dient dazu, herauszufinden, welches Element im
				// DropDown ausgewählt wurde
				int index = listBoxBauteile.getSelectedIndex();

				// amountBauteile ist eine TextBox. Diese wird hiermit in einen
				// int-Wert umgewandelt
				Integer anzahl = Integer.parseInt(amountBauteile.getText());

				// Der Tabelle wird ein Objekt von ElementPaar hinzugefügt,
				// welches in den folgenden Zeilen befüllt wird
				ElementPaar bauteilPaar = new ElementPaar();
				bauteilPaar.setAnzahl(anzahl);
				bauteilPaar.setElement(allBauteile.get(index));

				// Dem Vektor aller Bauteile der Stückliste wird das soeben
				// erstellte ElementPaar hinzugefügt
				collectBauteile.add(bauteilPaar);

				// ListBox-Element, das hinzugefügt wurde, wird für doppeltes
				// Hinzufügen gesperrt
				listBoxBauteile.getElement().getElementsByTagName("option")
						.getItem(index).setAttribute("disabled", "disabled");

				// Die Übersichtstabelle, welche für den User eine hilfreiche
				// Übersicht aller hinzugefügten Bauteile
				// bereitstellt, wird mithilfe dieser for-Schleife aufgebaut.
				// Die Schleife startet bei i = 1, da die
				// erste Reihe der Tabelle bereits mit den Überschriften befüllt
				// ist und diese nicht überschrieben werden
				// soll.
				for (int i = 1; i <= collectBauteile.size(); i++) {

					// Button, um in der BauteilCollection-Tabelle und
					// gleichzeitig dem Vektor ein Bauteil wieder zu entfernen
					final Button removeBtButton = new Button("x");

					// Der Wert von i muss final sein, damit sie im
					// nachfolgenden ClickHandler verwendet werden kann.
					// Daher wird sie mithilfe von int a finalisiert.
					final int a = i;

					// Die Tabelle befüllt sich aus allen Elementen, die im
					// collectBauteile-Vektor vorhanden sind.
					bauteilCollection.setText(a, 0,
							""
									+ collectBauteile.get(i - 1).getElement()
											.getId());
					bauteilCollection.setText(a, 1,
							"" + collectBauteile.get(i - 1).getAnzahl());
					bauteilCollection.setText(a, 2, collectBauteile.get(i - 1)
							.getElement().getName());
					bauteilCollection.setWidget(a, 3, removeBtButton);

					// In jeder Reihe wird ein Entfernen-Button platziert, damit
					// der User schnell und unkompliziert
					// jederzeit ein ElementPaar von Bauteil wieder entfernen
					// kann. Es ist ihm lediglich möglich,
					// gesamte ElementPaare von Bauteilen zu entfernen. Dies
					// muss er ebenfalls durchführen, wenn er
					// lediglich die Anzahl ändern möchte. Das Bauteil mit der
					// gewünschten neuen Anzahl kann er
					// schnell erneutaus dem Dropdown hinzufügen.
					removeBtButton.addClickHandler(new ClickHandler() {
						@Override
						public void onClick(ClickEvent event) {

							// Zum einen wird die entsprechende Reihe aus der
							// FlexTable entfernt.
							int rowIndex = bauteilCollection.getCellForEvent(
									event).getRowIndex();
							bauteilCollection.removeRow(rowIndex);

							// Zum anderen wird das ElementPaar von Bauteil aus
							// dem collectBauteile Vektor entfernt
							int x = a - 1;
							collectBauteile.remove(x);
							// TODO implementieren
							// ListBox-Element, das hinzugefügt wurde, wird für
							// doppeltes Hinzufügen gesperrt
							listBoxBauteile.getElement()
									.getElementsByTagName("option")
									.getItem(rowIndex)
									.setAttribute("enabled", "enabled");

						}
					});
				}

			}

		});

		// Übergangsweise, bevor Merge mit Galina und Katja erfolgt ist,
		// erstelle ich mir an dieser Stelle eigene Objekte
		// von Baugruppen. An dieser Stelle muss nach dem Merge ein Vektor mit
		// allen Baugruppen aus der DB mithilfe eines
		// RPC angefragt werden.
		Baugruppe a = new Baugruppe();
		Baugruppe b = new Baugruppe();
		Baugruppe c = new Baugruppe();

		a.setId(1);
		b.setId(2);
		c.setId(3);

		a.setName("Name1");
		b.setName("Name2");
		c.setName("Name3");

		allBaugruppen.add(a);
		allBaugruppen.add(b);
		allBaugruppen.add(c);

		listBoxBaugruppen.addItem(a.getName());
		listBoxBaugruppen.addItem(b.getName());
		listBoxBaugruppen.addItem(c.getName());

		// TODO implementieren
		// Um das Dropdown mit Bauteilen aus der DB zu befüllen, wird dieser
		// RPC-Aufruf gestartet
		// stuecklistenVerwaltung.getAllBaugruppen(new
		// GetAllBaugruppenCallback());

		// Mithilfe des Hinzufügen-Buttons wird die BauteilCollection Tabelle
		// befüllt
		collectBgButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {

				// Der index dient dazu, herauszufinden, welches Element im
				// DropDown ausgewählt wurde
				int index = listBoxBaugruppen.getSelectedIndex();

				// amountBauteile ist eine TextBox. Diese wird hiermit in einen
				// int-Wert umgewandelt
				Integer anzahl = Integer.parseInt(amountBaugruppen.getText());

				// Der Tabelle wird ein Objekt von ElementPaar hinzugefügt,
				// welches in den folgenden Zeilen befüllt wird
				ElementPaar baugruppePaar = new ElementPaar();
				baugruppePaar.setAnzahl(anzahl);
				baugruppePaar.setElement(allBaugruppen.get(index));

				// Dem Vektor aller Baugruppen der Stückliste wird das soeben
				// erstellte ElementPaar hinzugefügt
				collectBaugruppen.add(baugruppePaar);

				// ListBox-Element, das hinzugefügt wurde, wird für doppeltes
				// Hinzufügen gesperrt
				listBoxBaugruppen.getElement().getElementsByTagName("option")
						.getItem(index).setAttribute("disabled", "disabled");

				// Die Übersichtstabelle, welche für den User eine hilfreiche
				// Übersicht aller hinzugefügten Baugruppen
				// bereitstellt, wird mithilfe dieser for-Schleife aufgebaut.
				// Die Schleife startet bei i = 1, da die
				// erste Reihe der Tabelle bereits mit den Überschriften befüllt
				// ist und diese nicht überschrieben werden
				// soll.
				for (int i = 1; i <= collectBaugruppen.size(); i++) {

					// Button, um in der BaugruppeCollection-Tabelle und
					// gleichzeitig dem Vektor eine Baugruppe
					// wieder zu entfernen
					final Button removeBgButton = new Button("x");

					// Der Wert von i muss final sein, damit sie im
					// nachfolgenden ClickHandler verwendet werden kann.
					// Daher wird sie mithilfe von int a finalisiert.
					final int b = i;

					// Die Tabelle befüllt sich aus allen Elementen, die im
					// collectBaugruppen-Vektor vorhanden sind.
					baugruppeCollection
							.setText(b, 0, ""
									+ collectBaugruppen.get(i - 1).getElement()
											.getId());
					baugruppeCollection.setText(b, 1, ""
							+ collectBaugruppen.get(i - 1).getAnzahl());
					baugruppeCollection
							.setText(b, 2, collectBaugruppen.get(i - 1)
									.getElement().getName());
					baugruppeCollection.setWidget(b, 3, removeBgButton);

					// In jeder Reihe wird ein Entfernen-Button platziert, damit
					// der User schnell und unkompliziert
					// jederzeit ein ElementPaar von Baugruppe wieder entfernen
					// kann. Es ist ihm lediglich möglich,
					// gesamte ElementPaare von Baugruppen zu entfernen. Dies
					// muss er ebenfalls durchführen, wenn er
					// lediglich die Anzahl ändern möchte. Die Baugruppe mit der
					// gewünschten neuen Anzahl kann er
					// schnell erneutaus dem Dropdown hinzufügen.
					removeBgButton.addClickHandler(new ClickHandler() {
						public void onClick(ClickEvent event) {

							// Zum einen wird die entsprechende Reihe aus der
							// FlexTable entfernt.
							int rowIndex = baugruppeCollection.getCellForEvent(
									event).getRowIndex();
							baugruppeCollection.removeRow(rowIndex);

							// Zum anderen wird das ElementPaar von Baugruppe
							// aus dem collectBaugruppen Vektor entfernt
							int x = b - 1;
							collectBauteile.remove(x);

							// TODO implementieren
							// ListBox-Element, das hinzugefügt wurde, wird für
							// doppeltes Hinzufügen gesperrt
							// listBoxBaugruppen.getElement().setAttribute("enabled",
							// "enabled");

							listBoxBaugruppen.getElement()
									.getElementsByTagName("option")
									.getItem(rowIndex)
									.setAttribute("disabled", "disabled");

						}

					});
				}

			}

		});

		// Horizontales Anordnen von zugehörigen Bauteil-Widgets
		btPanel.add(amountBauteile);
		btPanel.add(listBoxBauteile);
		btPanel.add(collectBtButton);

		// Horizontales Anordnen von zugehörigen Baugruppe-Widgets
		bgPanel.add(amountBaugruppen);
		bgPanel.add(listBoxBaugruppen);
		bgPanel.add(collectBgButton);

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
		this.add(BauteilLabel);
		this.add(btPanel);
		this.add(BaugruppeLabel);
		this.add(bgPanel);
		this.add(bauteilLabel);
		this.add(bauteilCollection);
		this.add(baugruppeLabel);
		this.add(baugruppeCollection);
		this.add(EditStuecklisteButton);

		/**
		 * Das Id-Textfeld darf nicht verändert werden und wird daher auf
		 * "ReadOnly" gesetzt.
		 */
		IdField.setReadOnly(true);

		/**
		 * Diverse css-Formatierungen
		 */
		HeadlineLabel.setStyleName("headline");
		SublineLabel.setStyleName("subline");
		EditStuecklisteButton.setStyleName("Button");

		/**
		 * Der Editieren-Button ruft die RPC-Methode auf, welche das Editieren
		 * einer Stückliste in der DB ermöglicht.
		 */
		EditStuecklisteButton.addClickHandler(new EditClickHandler());

		/**
		 * In ein Textfeld kann nur ein Text geladen werden, kein int. Daher ist
		 * dieser Zwischenschritt notwendig: Zwischenspeichern des Werts
		 * mithilfe Integer, da Integer die toString-Methode unterstützt, ein
		 * einfacher int jedoch nicht.
		 * 
		 */
		Integer iD = new Integer(editStueckliste.getId());

		/**
		 * Mithilfe des an diese Klasse übergebenen Stücklisten-Objektes werden
		 * die Textfelder befüllt.
		 */
		IdField.setText(iD.toString());
		NameField.setText(editStueckliste.getName());

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
	 * mitgeschickten Stücklisten-Objektes das bestehende Stücklisten-Objekt in
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

			Stueckliste s = new Stueckliste();
			/**
			 * Aus einem Textfeld kann kein Integer-Wert ausgelesen werden,
			 * daher ist dieser Zwischenschritt notwendig: Auslesen des Id-Werts
			 * mithilfe Integer, da Integer die toString-Methode unterstützt.
			 */
			s.setId(Integer.parseInt(IdField.getText()));
			s.setName(NameField.getText());

			/**
			 * Vor dem Aufruf der RPC-Methode create wird geprüft, ob alle
			 * notwendigen Felder befüllt sind.
			 */
			if (NameField.getText().isEmpty() != true) {
				/**
				 * Die konkrete RPC-Methode für den editier-Befehl wird
				 * aufgerufen. Hierbei wird das vorab befüllte Bauteil-Objekt
				 * mit den gewünschten Werten mitgeschickt.
				 */
				stuecklistenVerwaltung.saveStueckliste(s, new SaveCallback());

				/**
				 * Nachdem der Editier-Vorgang durchgeführt wurde, soll die GUI
				 * zurück zur Übersichtstabelle weiterleiten.
				 */
				RootPanel.get("content_wrap").clear();
				RootPanel.get("content_wrap").add(new StuecklisteGeneralView());
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
			Window.alert("Die Stückliste wurde nicht editiert.");
		}

		@Override
		public void onSuccess(Void result) {
			Window.alert("Die Stückliste wurde erfolgreich editiert.");

		}
	}

	/**
	 * Hiermit wird die RPC-Methode aufgerufen, die einen Vektor von allen in
	 * der DB vorhandenen Bauteilen liefert. Die Klasse ist eine nested-class
	 * und erlaubt daher, auf die Attribute der übergeordneten Klasse
	 * zuzugreifen.
	 * 
	 * @author Mario
	 * 
	 */
	class GetAllBauteileCallback implements AsyncCallback<Vector<Bauteil>> {

		@Override
		public void onFailure(Throwable caught) {
			Window.alert("Bauteile konnten nicht geladen werden");
		}

		@Override
		public void onSuccess(Vector<Bauteil> alleBauteile) {

			/**
			 * Der Bauteil-Vektor allBauteile wird mit dem Ergebnis dieses RPC´s
			 * befüllt.
			 */
			allBauteile = alleBauteile;

			if (allBauteile.isEmpty() == true) {

				Window.alert("Es sind leider keine Daten in der Datenbank vorhanden.");

			} else {

				/**
				 * Die Schleife durchläuft den kompletten Ergebnis-Vektor.
				 */
				for (int c = 0; c <= allBauteile.size(); c++) {

					/**
					 * Das DropDown wird mithilfe dieser for-Schleife für jedes
					 * Bauteil mit dessen Namen befüllt.
					 */
					listBoxBauteile.addItem(allBauteile.get(c).getName());

				}

			}

		}
	}

	// Handler prüft zum einen, ob das Anzahl-Feld leer ist. Falls ja erscheint
	// eine Hinweismeldung.
	// Ist das Feld befüllt, wird mithilfe der Methode "istZahl" aus der Klasse
	// FieldVerifier geprüft,
	// ob im Textfeld eine Zahl eingetragen wurde. Falls nicht, erscheint
	// ebenfalls eine Hinweismeldung.
	private class numericBtHandler implements ClickHandler {
		@Override
		public void onClick(ClickEvent event) {

			if (amountBauteile.getText().isEmpty() == true) {
				Window.alert("Bitte die gewünschte Anzahl eintragen.");
			} else if (FieldVerifier.istZhal(amountBauteile.getText()) == false) {
				Window.alert("Bitte nur Zahlen eintragen.");
			}

		}
	}

	// Handler prüft zum einen, ob das Anzahl-Feld leer ist. Falls ja erscheint
	// eine Hinweismeldung.
	// Ist das Feld befüllt, wird mithilfe der Methode "istZahl" aus der Klasse
	// FieldVerifier geprüft,
	// ob im Textfeld eine Zahl eingetragen wurde. Falls nicht, erscheint
	// ebenfalls eine Hinweismeldung.
	private class numericBgHandler implements ClickHandler {
		@Override
		public void onClick(ClickEvent event) {

			if (amountBaugruppen.getText().isEmpty() == true) {
				Window.alert("Bitte die gewünschte Anzahl eintragen.");
			} else if (FieldVerifier.istZhal(amountBaugruppen.getText()) == false) {
				Window.alert("Bitte nur Zahlen eintragen.");
			}

		}
	}

}