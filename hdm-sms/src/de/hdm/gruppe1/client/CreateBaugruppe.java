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

/**
 * Die Klasse CreateStueckliste erm�glicht dem User, Objekte von St�ckliste in
 * der Datenbank anzulegen.
 * 
 * @author Mario Theiler
 * @version 1.0
 */
public class CreateBaugruppe extends VerticalPanel {

	/**
	 * GUI-Elemente f�r CreateBauruppe initialisieren.
	 */
	private final Label HeadlineLabel = new Label("Baugruppe anlegen");
	private final Label SublineLabel = new Label(
			"Um eine Baugruppe anzulegen, f�llen Sie bitte alle Felder aus und best�tigen mit dem <anlegen>-Button ihre Eingabe.");
	private final Label bauteilLabel = new Label("Bauteile f�r Baugruppe");
	private final Label baugruppeLabel = new Label("Baugruppen f�r Baugruppe");
	private final TextBox NameField = new TextBox();
	private final Label BauteilLabel = new Label(
			"Gew�nschte Anzahl von Bauteilen hinzuf�gen");
	private final Label BaugruppeLabel = new Label(
			"Gew�nschte Anzahl von Baugruppen hinzuf�gen");
	private final TextBox amountBauteile = new TextBox();
	ListBox listBoxBauteile = new ListBox();
	private final Button collectBtButton = new Button("hinzuf�gen");
	private final TextBox amountBaugruppen = new TextBox();
	ListBox listBoxBaugruppen = new ListBox();
	private final Button collectBgButton = new Button("hinzuf�gen");
	private final Button CreateBaugruppeButton = new Button(
			"Baugruppe anlegen");

	/**
	 * Einige GUI-Elemente sollen nebeneinander angezeigt werden, nicht vertikal. Daher wird
	 * ein "horizontales Zwischen-Panel" ben�tigt.
	 */
	HorizontalPanel btPanel = new HorizontalPanel();
	HorizontalPanel bgPanel = new HorizontalPanel();

	/**
	 *  Vektor wird mit allen Bauteilen bzw. Baugruppen aus der DB bef�llt.
	 */
	Vector<Bauteil> allBauteile = new Vector<Bauteil>();
	Vector<Baugruppe> allBaugruppen = new Vector<Baugruppe>();
	
	/**
	 *  Vektoren, um die hinzugef�gten Bauteile/Baugruppen in einer �bersicht zu
	 *  sammeln, bevor die Baugruppe gespeichert wird.
	 */
	Vector<ElementPaar> collectBauteile = new Vector<ElementPaar>();
	Vector<ElementPaar> collectBaugruppen = new Vector<ElementPaar>();

	/**
	 *  Tabellen, um in der GUI alle Bauteile/Baugruppen anzuzeigen, bevor die
	 *  Baugruppe gespeichert wird.
	 */
	FlexTable bauteilCollection = new FlexTable();
	FlexTable baugruppeCollection = new FlexTable();

	/**
	 * Remote Service via ClientsideSettings wird an dieser Stelle einmalig in
	 * der Klasse aufgerufen. Im Anschluss kann jederzeit darauf zugegriffen
	 * werden.
	 */
	SmsAsync stuecklistenVerwaltung = ClientsideSettings.getSmsVerwaltung();
	
	int c = 0;
	
	public CreateBaugruppe() {

		/**
		 * TextBoxen werden mit Text vorbef�llt, der ausgeblendet wird, sobald
		 * die TextBox vom User fokussiert wird.
		 */
		NameField.getElement().setPropertyString("placeholder", "Name");
		amountBauteile.getElement().setPropertyString("placeholder", "Anzahl");
		amountBaugruppen.getElement().setPropertyString("placeholder", "Anzahl");

		/**
		 *  ClickHandler um zu pr�fen, ob die Texteingabe numerisch ist.
		 */
		collectBtButton.addClickHandler(new numericBtHandler());
		collectBgButton.addClickHandler(new numericBgHandler());

		/**
		 * Die erste Reihe der Tabellen wird mit �berschriften vordefiniert.
		 */
		bauteilCollection.setText(0, 0, "ID");
		bauteilCollection.setText(0, 1, "Anzahl");
		bauteilCollection.setText(0, 2, "Name");
		bauteilCollection.setText(0, 3, "Entfernen");

		baugruppeCollection.setText(0, 0, "ID");
		baugruppeCollection.setText(0, 1, "Anzahl");
		baugruppeCollection.setText(0, 2, "Name");
		baugruppeCollection.setText(0, 3, "Entfernen");

		/**
		 * Diverse css-Formatierungen f�r die Tabelle
		 */
		bauteilCollection.setStyleName("tableBody");
		baugruppeCollection.setStyleName("tableBody");

		/**
		 * Das FlexTable Widget unterst�tzt keine Headlines. Daher wird die
		 * erste Reihe �ber folgenden Umweg formatiert.
		 */
		bauteilCollection.getCellFormatter().addStyleName(0, 0, "tableHead");
		bauteilCollection.getCellFormatter().addStyleName(0, 1, "tableHead");
		bauteilCollection.getCellFormatter().addStyleName(0, 2, "tableHead");
		bauteilCollection.getCellFormatter().addStyleName(0, 3, "tableHead");

		baugruppeCollection.getCellFormatter().addStyleName(0, 0, "tableHead");
		baugruppeCollection.getCellFormatter().addStyleName(0, 1, "tableHead");
		baugruppeCollection.getCellFormatter().addStyleName(0, 2, "tableHead");
		baugruppeCollection.getCellFormatter().addStyleName(0, 3, "tableHead");

		/**
		 * RPC-Methode ausf�hren, die alle Bauteil- bzw. Baugruppen-Objekte aus der Datenbank in
		 * einem Vektor zur�ckliefert. Dadurch wird der Klassen-Vektor
		 * "allBauteile" & "allBaugruppen" bef�llt.
		 */
		stuecklistenVerwaltung.getAllBauteile(new GetAllBauteileCallback());
		stuecklistenVerwaltung.getAllBaugruppen(new GetAllBaugruppenCallback());

		/**
		 *  Mithilfe des Hinzuf�gen-Buttons wird die BauteilCollection Tabelle bef�llt.
		 */
		collectBtButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {

				/**
				 *  Der index dient dazu, herauszufinden, welches Element im DropDown ausgew�hlt wurde.
				 */
				final int index = listBoxBauteile.getSelectedIndex();
				
				/**
				 *  amountBauteile ist eine TextBox. Diese wird hiermit in einen int-Wert umgewandelt.
				 */
				Integer anzahl = Integer.parseInt(amountBauteile.getText());

				/**
				 *  Der Vektor collectBauteile wird ein Objekt von ElementPaar hinzugef�gt,
				 *  welches in den folgenden Zeilen bef�llt wird.
				 */
				ElementPaar bauteilPaar = new ElementPaar();
				bauteilPaar.setAnzahl(anzahl);
				bauteilPaar.setElement(allBauteile.get(index));
				

				/**
				 *  Dem Vektor aller Bauteile der Baugruppe wird das soeben erstellte ElementPaar hinzugef�gt.
				 */
				collectBauteile.add(bauteilPaar);

				/**
				 * Das ListBox-Element, welches hinzugef�gt wurde, wird f�r doppeltes Hinzuf�gen ausgegraut.
				 */
				listBoxBauteile.getElement().getElementsByTagName("*").getItem(index).setAttribute("disabled", "disabled");

				/**
				 *  Die �bersichtstabelle, welche f�r den User eine hilfreiche
				 *  �bersicht aller hinzugef�gten Bauteile
				 *  bereitstellt, wird mithilfe dieser for-Schleife aufgebaut.
				 *  Die Schleife startet bei i = 1, da die
				 *  erste Reihe der Tabelle bereits mit den �berschriften bef�llt
				 *  ist und diese nicht �berschrieben werden soll.
				 */
				for (int i = 1; i <= collectBauteile.size(); i++) {

					/**
					 *  Button, um in der BauteilCollection-Tabelle und
					 *  gleichzeitig dem Vektor ein Bauteil wieder zu entfernen.
					 */
					final Button removeBtButton = new Button("x");

					/**
					 *  Der Wert von i muss final sein, damit sie im
					 *  nachfolgenden ClickHandler verwendet werden kann.
					 *  Daher wird sie mithilfe von int a finalisiert.
					 */
					final int a = i;

					/**
					 *  Die Tabelle bef�llt sich aus allen Elementen, die im
					 *  collectBauteile-Vektor vorhanden sind.
					 */
					bauteilCollection.setText(a, 0, ""+ collectBauteile.get(i - 1).getElement().getId());
					bauteilCollection.setText(a, 1, "" + collectBauteile.get(i - 1).getAnzahl());
					bauteilCollection.setText(a, 2, collectBauteile.get(i - 1).getElement().getName());
					bauteilCollection.setWidget(a, 3, removeBtButton);

					/**
					 *  In jeder Reihe wird ein Entfernen-Button platziert, damit der User schnell und unkompliziert
					 *  jederzeit ein ElementPaar von Bauteil wieder entfernen kann. Es ist ihm lediglich m�glich,
					 *  gesamte ElementPaare von Bauteilen zu entfernen. Dies muss er ebenfalls durchf�hren, wenn er
					 *  lediglich die Anzahl �ndern m�chte. Das Bauteil mit der gew�nschten neuen Anzahl kann er
					 *  schnell erneut aus dem Dropdown hinzuf�gen.
					 */
					removeBtButton.addClickHandler(new ClickHandler() {
						@Override
						public void onClick(ClickEvent event) {

							
							/**
							 *  Zum anderen wird das ElementPaar von Bauteil aus dem collectBauteile Vektor entfernt.
							 */
							
							//Test
							int rowIndex = bauteilCollection.getCellForEvent(event).getRowIndex();
							Integer id = new Integer(bauteilCollection.getText(rowIndex, 0));
							for(int i=0; i<collectBauteile.size(); i++){
								if(collectBauteile.get(i).getElement().getId()==id){
									collectBauteile.remove(i);
									break;
								}
							}
							for(int i=0; i<allBauteile.size();i++){
								if(allBauteile.get(i).getId()==id){
									listBoxBauteile.getElement().getElementsByTagName("*").getItem(i).removeAttribute("disabled");
									break;
								}
							}
							bauteilCollection.removeRow(rowIndex);
							String message = new String("Folgende Bauteile sind noch im Vektor: ");
							for(int i=0; i<collectBauteile.size(); i++){
								message= message+collectBauteile.get(i).getElement().getName()+" , ";
							}
							Window.alert(message);
							
//							Bauteil b = (Bauteil) collectBauteile.get(x).getElement();
//							int vektorIndex = allBauteile.indexOf(b);
//							
//							listBoxBauteile.getElement().getElementsByTagName("*").getItem(vektorIndex).removeAttribute("disabled");
//							
//							collectBauteile.remove(x);
							
							
						}
					});
				}

			}

		});

		/**
		 *  Mithilfe des Hinzuf�gen-Buttons wird die BaugruppenCollection Tabelle bef�llt.
		 */
		collectBgButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {

				/**
				 *  Der index dient dazu, herauszufinden, welches Element im DropDown ausgew�hlt wurde.
				 */
				int index = listBoxBaugruppen.getSelectedIndex();

				/**
				 *  amountBaugruppen ist eine TextBox. Diese wird hiermit in einen int-Wert umgewandelt.
				 */
				Integer anzahl = Integer.parseInt(amountBaugruppen.getText());

				/**
				 *  Der Vektor collectBaugruppen wird ein Objekt von ElementPaar hinzugef�gt,
				 *  welches in den folgenden Zeilen bef�llt wird.
				 */
				ElementPaar baugruppePaar = new ElementPaar();
				baugruppePaar.setAnzahl(anzahl);
				baugruppePaar.setElement(allBaugruppen.get(index));

				/**
				 *  Dem Vektor aller Baugruppen der Baugruppe wird das soeben erstellte ElementPaar hinzugef�gt.
				 */
				collectBaugruppen.add(baugruppePaar);

				/**
				 * Das ListBox-Element, welches hinzugef�gt wurde, wird f�r doppeltes Hinzuf�gen ausgegraut.
				 */
				listBoxBaugruppen.getElement().getElementsByTagName("*")
						.getItem(index).setAttribute("disabled", "disabled");

				/**
				 *  Die �bersichtstabelle, welche f�r den User eine hilfreiche
				 *  �bersicht aller hinzugef�gten Baugruppen
				 *  bereitstellt, wird mithilfe dieser for-Schleife aufgebaut.
				 *  Die Schleife startet bei i = 1, da die
				 *  erste Reihe der Tabelle bereits mit den �berschriften bef�llt
				 *  ist und diese nicht �berschrieben werden soll.
				 */
				for (int i = 1; i <= collectBaugruppen.size(); i++) {

					/**
					 *  Button, um in der BaugruppenCollection-Tabelle und
					 *  gleichzeitig dem Vektor eine Baugruppe wieder zu entfernen.
					 */
					final Button removeBgButton = new Button("x");

					/**
					 *  Der Wert von i muss final sein, damit sie im
					 *  nachfolgenden ClickHandler verwendet werden kann.
					 *  Daher wird sie mithilfe von int b finalisiert.
					 */
					final int b = i;

					/**
					 *  Die Tabelle bef�llt sich aus allen Elementen, die im
					 *  collectBaugruppenVektor vorhanden sind.
					 */
					baugruppeCollection.setText(b, 0, ""+ collectBaugruppen.get(i - 1).getElement().getId());
					baugruppeCollection.setText(b, 1, ""+ collectBaugruppen.get(i - 1).getAnzahl());
					baugruppeCollection.setText(b, 2, collectBaugruppen.get(i - 1).getElement().getName());
					baugruppeCollection.setWidget(b, 3, removeBgButton);

					/**
					 *  In jeder Reihe wird ein Entfernen-Button platziert, damit der User schnell und unkompliziert
					 *  jederzeit ein ElementPaar von Baugruppe wieder entfernen kann. Es ist ihm lediglich m�glich,
					 *  gesamte ElementPaare von Baugruppen zu entfernen. Dies muss er ebenfalls durchf�hren, wenn er
					 *  lediglich die Anzahl �ndern m�chte. Die Baugruppe mit der gew�nschten neuen Anzahl kann er
					 *  schnell erneut aus dem Dropdown hinzuf�gen.
					 */
					removeBgButton.addClickHandler(new ClickHandler() {
						public void onClick(ClickEvent event) {

							/**
							 *  Zum einen wird die entsprechende Reihe aus der FlexTable entfernt.
							 */
							int rowIndex = baugruppeCollection.getCellForEvent(event).getRowIndex();
							Integer id = new Integer(baugruppeCollection.getText(rowIndex, 0));
							for(int i=0; i<collectBaugruppen.size(); i++){
								if(collectBaugruppen.get(i).getElement().getId()==id){
									collectBaugruppen.remove(i);
									break;
								}
							}
							for(int i=0; i<allBaugruppen.size();i++){
								if(allBaugruppen.get(i).getId()==id){
									listBoxBaugruppen.getElement().getElementsByTagName("*").getItem(i).removeAttribute("disabled");
									break;
								}
							}
							baugruppeCollection.removeRow(rowIndex);
							String message = new String("Folgende Baugruppen sind noch im Vektor: ");
							for(int i=0; i<collectBaugruppen.size(); i++){
								message= message+collectBaugruppen.get(i).getElement().getName()+" , ";
							}
							Window.alert(message);
							
						}

					});
				}

			}

		});

		// Horizontales Anordnen von zugeh�rigen Bauteil-Widgets
		btPanel.add(amountBauteile);
		btPanel.add(listBoxBauteile);
		btPanel.add(collectBtButton);

		// Horizontales Anordnen von zugeh�rigen Baugruppe-Widgets
		bgPanel.add(amountBaugruppen);
		bgPanel.add(listBoxBaugruppen);
		bgPanel.add(collectBgButton);

		/**
		 * Nachdem alle Elemente geladen sind, wird alles dem VerticalPanel
		 * zugeordnet, da diese Klasse von VerticalPanel erbt.
		 */
		this.add(HeadlineLabel);
		this.add(SublineLabel);
		this.add(NameField);
		this.add(BauteilLabel);
		this.add(btPanel);
		this.add(BaugruppeLabel);
		this.add(bgPanel);
		this.add(bauteilLabel);
		this.add(bauteilCollection);
		this.add(baugruppeLabel);
		this.add(baugruppeCollection);
		this.add(CreateBaugruppeButton);

		/**
		 * Diverse css-Formatierungen
		 */
		HeadlineLabel.setStyleName("headline");
		SublineLabel.setStyleName("subline");
		amountBauteile.setStyleName("numericInput");
		amountBaugruppen.setStyleName("numericInput");
		CreateBaugruppeButton.setStyleName("Button");

		/**
		 * Der Create-Button ruft die RPC-Methode auf, welche das Erstellen
		 * einer Baugruppe in der DB erm�glicht.
		 */
		CreateBaugruppeButton.addClickHandler(new CreateClickHandler());

		/**
		 * Abschlie�end wird alles dem RootPanel zugeordnet
		 */
		RootPanel.get("content_wrap").add(this);

	}

	/*
	 * Click Handlers.
	 */

	/**
	 * Hiermit wird die RPC-Methode aufgerufen, die einen Vektor von allen in
	 * der DB vorhandenen Bauteilen liefert. Die Klasse ist eine nested-class
	 * und erlaubt daher, auf die Attribute der �bergeordneten Klasse
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
			 * Der Bauteil-Vektor allBauteile wird mit dem Ergebnis dieses RPC�s
			 * bef�llt.
			 */
			allBauteile = alleBauteile;
			
			if (allBauteile.isEmpty() == true) {

				Window.alert("Es sind leider keine Daten in der Datenbank vorhanden.");

			} else {

				/**
				 * Die Schleife durchl�uft den kompletten Ergebnis-Vektor.
				 */
				for (int c = 0; c <= allBauteile.size(); c++) {

					/**
					 * Das DropDown wird mithilfe dieser for-Schleife f�r jedes
					 * Bauteil mit dessen Namen bef�llt.
					 */
					listBoxBauteile.addItem(allBauteile.get(c).getName());

				}

			}

		}
	}
	
	/**
	 * Hiermit wird die RPC-Methode aufgerufen, die einen Vektor von allen in
	 * der DB vorhandenen Baugruppen liefert. Die Klasse ist eine nested-class
	 * und erlaubt daher, auf die Attribute der �bergeordneten Klasse
	 * zuzugreifen.
	 * 
	 * @author Mario
	 * 
	 */
	class GetAllBaugruppenCallback implements AsyncCallback<Vector<Baugruppe>> {

		@Override
		public void onFailure(Throwable caught) {
			Window.alert("Bauteile konnten nicht geladen werden");
		}

		@Override
		public void onSuccess(Vector<Baugruppe> alleBaugruppen) {

			/**
			 * Der Baugruppen-Vektor allBaugruppen wird mit dem Ergebnis dieses RPC�s
			 * bef�llt.
			 */
			allBaugruppen = alleBaugruppen;

			if (allBaugruppen.isEmpty() == true) {

				Window.alert("Es sind leider keine Daten in der Datenbank vorhanden.");

			} else {

				/**
				 * Die Schleife durchl�uft den kompletten Ergebnis-Vektor.
				 */
				for (int c = 0; c <= allBaugruppen.size(); c++) {

					/**
					 * Das DropDown wird mithilfe dieser for-Schleife f�r jede
					 * Baugruppe mit dessen Namen bef�llt.
					 */
					listBoxBaugruppen.addItem(allBaugruppen.get(c).getName());

				}

			}

		}
	}

	// Handler pr�ft zum einen, ob das Anzahl-Feld leer ist. Falls ja erscheint
	// eine Hinweismeldung.
	// Ist das Feld bef�llt, wird mithilfe der Methode "istZahl" aus der Klasse
	// FieldVerifier gepr�ft,
	// ob im Textfeld eine Zahl eingetragen wurde. Falls nicht, erscheint
	// ebenfalls eine Hinweismeldung.
	private class numericBtHandler implements ClickHandler {
		@Override
		public void onClick(ClickEvent event) {

			if (amountBauteile.getText().isEmpty() == true) {
				Window.alert("Bitte die gew�nschte Anzahl eintragen.");
			} else if (FieldVerifier.istZahl(amountBauteile.getText()) == false) {
				Window.alert("Bitte nur Zahlen eintragen.");
			}

		}
	}

	// Handler pr�ft zum einen, ob das Anzahl-Feld leer ist. Falls ja erscheint
	// eine Hinweismeldung.
	// Ist das Feld bef�llt, wird mithilfe der Methode "istZahl" aus der Klasse
	// FieldVerifier gepr�ft,
	// ob im Textfeld eine Zahl eingetragen wurde. Falls nicht, erscheint
	// ebenfalls eine Hinweismeldung.
	private class numericBgHandler implements ClickHandler {
		@Override
		public void onClick(ClickEvent event) {

			if (amountBaugruppen.getText().isEmpty() == true) {
				Window.alert("Bitte die gew�nschte Anzahl eintragen.");
			} else if (FieldVerifier.istZahl(amountBaugruppen.getText()) == false) {
				Window.alert("Bitte nur Zahlen eintragen.");
			}

		}
	}

	/**
	 * Hiermit wird die RPC-Methode aufgerufen, die ein Baugruppen-Objekt in
	 * der Datenbank anlegt.
	 * 
	 * @author Mario
	 * 
	 */
	private class CreateClickHandler implements ClickHandler {
		@Override
		public void onClick(ClickEvent event) {

			/**
			 * Vor dem Aufruf der RPC-Methode create wird gepr�ft, ob alle
			 * notwendigen Felder bef�llt sind.
			 */
			if (NameField.getText().isEmpty() != true) {
				
				FieldVerifier umlaut = new FieldVerifier();
				String input = umlaut.changeUmlaut(NameField.getText());

				/**
				 * Die konkrete RPC-Methode f�r den create-Befehl wird
				 * aufgerufen. Hierbei werden die gew�nschten Werte
				 * mitgeschickt.
				 */
				String nameStueckliste = input;
				stuecklistenVerwaltung.createBaugruppe(nameStueckliste,
						collectBauteile, collectBaugruppen,
						new CreateBaugruppeCallback());

				/**
				 * Nachdem der Create-Vorgang durchgef�hrt wurde, soll die GUI
				 * zur�ck zur �bersichtstabelle weiterleiten.
				 */
				RootPanel.get("content_wrap").clear();
				RootPanel.get("content_wrap").add(new BaugruppeGeneralView());

			}

			else {

				Window.alert("Bitte Namensfeld ausf�llen.");

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
	class CreateBaugruppeCallback implements AsyncCallback<Baugruppe> {

		@Override
		public void onFailure(Throwable caught) {
			Window.alert(caught.getMessage());
		}

		@Override
		public void onSuccess(Baugruppe baugruppe) {

			Window.alert("Die Baugruppe wurde erfolgreich angelegt.");
		}
	}

}
