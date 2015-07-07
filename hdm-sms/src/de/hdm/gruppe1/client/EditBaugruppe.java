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
 *  Mit der Klasse EditBaugruppe lassen sich Baugruppen Objekte editieren. 
 *  Die Klasse bietet eine Übersicht der bereits verwendeten Baugruppen und Bauteile im jeweiligen Collection Table. Baugruppen
 *  sowie Bauteile lassen sich dem Collection Table hinzufügen, ebenso können die Elemente wieder aus dem Table 
 *  entfernt werden. 
 * Die Klasse erbt von Vertical Panel.
 * 
 * @author Thiere, Theiler
 * @version 1.0
 */

public class EditBaugruppe extends VerticalPanel {

	/**
	 * GUI-Elemente für EditBaugruppe initialisieren
	 */
	private final Label HeadlineLabel = new Label("Baugruppe ändern");
	private final Label SublineLabel = new Label(
			"Um eine Baugruppe zu ändern, füllen Sie bitte alle Felder aus und bestätigen mit dem <editieren>-Button ihre Eingabe.");
	private final Label bauteilLabel = new Label("Bauteile für Baugruppe");
	private final Label baugruppeLabel = new Label("Baugruppen für Baugruppe");
	private final Label IdLabel = new Label("Id");
	private final TextBox IdField = new TextBox();
	private final Label sIdLabel = new Label("Zugehörige Stückliste");
	private final TextBox sIdField = new TextBox();
	private final TextBox sNameField = new TextBox();
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
	private final Button EditBaugruppeButton = new Button("ändern");

	/**
	 *  Horizontale Panels, um weitere Baugruppen oder Bauteil-Elemente der Baugruppe hinzuzufügen
	 */
	HorizontalPanel btPanel = new HorizontalPanel();
	HorizontalPanel bgPanel = new HorizontalPanel();
	
	/**
	 *  Horizontales Panel zur Anzeige der dazugehörigen Stückliste einer Baugruppe. Die Ausgabe kann nicht editiert werden
	 */
	HorizontalPanel stuecklistePanel = new HorizontalPanel();

	/**
	 * Vektor wird mit allen Bauteilen bzw. Baugruppen aus der DB befüllt
	 */
	Vector<Bauteil> allBauteile = new Vector<Bauteil>();
	Vector<Baugruppe> allBaugruppen = new Vector<Baugruppe>();

	/**
	 * Vektoren mit allen Bauteilen und Baugruppen, die einer Baugruppe zugeordnet sind oder
	 * neu hinzugefügt wurden
	 */

	Vector<ElementPaar> collectBauteile = new Vector<ElementPaar>();
	Vector<ElementPaar> collectBaugruppen = new Vector<ElementPaar>();

	/**
	 * Ausgabe aller enthaltenen Baugruppen/Bauteile eine Baugruppe mithilfe eines Flextables 
	 */

	FlexTable bauteilCollection = new FlexTable();
	FlexTable baugruppeCollection = new FlexTable();
	
	Baugruppe eBaugruppe= new Baugruppe();

	/**
	 * Remote Service via ClientsideSettings wird an dieser Stelle einmalig in
	 * der Klasse aufgerufen. Im Anschluss kann jederzeit darauf zugegriffen
	 * werden.
	 */
	SmsAsync stuecklistenVerwaltung = ClientsideSettings.getSmsVerwaltung();
	
	/**
	 * Methode, um Bauteile bzw. Baugruppen der Baugruppe hinzu zu fügen
	 */


	public EditBaugruppe(Baugruppe editBaugruppe) {
		
		eBaugruppe=editBaugruppe;
		/**
		 * Textbox mit Eingabehilfe-String "Anzahl" der benötigten Bauteile bzw Baugruppen 
		 */
		amountBauteile.getElement().setPropertyString("placeholder", "Anzahl");
		amountBaugruppen.getElement().setPropertyString("placeholder", "Anzahl");

		/**
		 * ClickHandler um zu prüfen, ob die Texteingabe numerisch ist
		 */
		collectBtButton.addClickHandler(new numericBtHandler());
		collectBgButton.addClickHandler(new numericBgHandler());

		/**
		 * Flextable, Ausgabe enthaltende Elemente der Baugruppe
		 */
		bauteilCollection.setText(0, 0, "ID");
		bauteilCollection.setText(0, 1, "Anzahl");
		bauteilCollection.setText(0, 2, "Name");
		bauteilCollection.setText(0, 3, "Entfernen");

		baugruppeCollection.setText(0, 0, "ID");
		baugruppeCollection.setText(0, 1, "Anzahl");
		baugruppeCollection.setText(0, 2, "Name");
		baugruppeCollection.setText(0, 3, "Entfernen");

		
		bauteilCollection.setStyleName("tableBody");
		baugruppeCollection.setStyleName("tableBody");

		/**
		 * Das FlexTable Widget unterstützt keine Headlines. Daher wird die
		 * erste Reihe über folgenden Umweg formatiert
		 */
		bauteilCollection.getCellFormatter().addStyleName(0, 0, "tableHead");
		bauteilCollection.getCellFormatter().addStyleName(0, 1, "tableHead");
		bauteilCollection.getCellFormatter().addStyleName(0, 2, "tableHead");
		bauteilCollection.getCellFormatter().addStyleName(0, 3, "tableHead");

		baugruppeCollection.getCellFormatter().addStyleName(0, 0, "tableHead");
		baugruppeCollection.getCellFormatter().addStyleName(0, 1, "tableHead");
		baugruppeCollection.getCellFormatter().addStyleName(0, 2, "tableHead");
		baugruppeCollection.getCellFormatter().addStyleName(0, 3, "tableHead");
		
		for(int i = 0; i<editBaugruppe.getStueckliste().getBauteilPaare().size(); i++) {
			
			/**
			 * Der Tabelle wird ein Objekt von ElementPaar hinzugefügt,
			 * welches in den folgenden Zeilen befüllt wird
			 */

			ElementPaar bauteilPaar = new ElementPaar();
			bauteilPaar.setAnzahl(editBaugruppe.getStueckliste().getBauteilPaare().get(i).getAnzahl());
			bauteilPaar.setElement(editBaugruppe.getStueckliste().getBauteilPaare().get(i).getElement());

			/**
			 * Dem Vektor aller Bauteile der Stückliste wird das soeben erstellte ElementPaar mit der jeweiligen Anzahl & Objekt hinzugefügt
			 */
			collectBauteile.add(bauteilPaar);
			
		
			/**
			 * Erzeugen eines Remove Buttons, um ein Bauteil aus dem Vektor in der Tabelle wieder entfernen zu können
			 */
			final Button removeBtButton = new Button("x");
			
			bauteilCollection.setText(i+1, 0, ""+ editBaugruppe.getStueckliste().getBauteilPaare().get(i).getElement().getId());
			bauteilCollection.setText(i+1, 1, ""+ editBaugruppe.getStueckliste().getBauteilPaare().get(i).getAnzahl());
			bauteilCollection.setText(i+1, 2, editBaugruppe.getStueckliste().getBauteilPaare().get(i).getElement().getName());
			bauteilCollection.setWidget(i+1, 3, removeBtButton);

			
			/**
			 * Durch den Aufruf des Clickhandler removeBtButton 
			 * lassen sich Elementpaare aus der BauteilCollection Tabelle wieder entfernen.
			 * Anmerkung - Wenn lediglich die Anzahl eines Bauteiles verändert werden soll, 
			 * muss das gesamte ElementPaar von der BauteilCollection Tabelle entfernt und erneut 
			 * mit der richtigen Anzahl des Bauteiles hinzugefügt werden.
			 */

			removeBtButton.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {

					/**
					 * Entfernen des ElementPaares Bauteil aus dem
					 * FlexTable.
					 */

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
				}
			});
			
		}
		
		
		/**
		 * Baugruppen Collection Table wird mit den Baugruppen Paaren befüllt
		 */
		
		for(int i = 0; i<editBaugruppe.getStueckliste().getBaugruppenPaare().size(); i++) {
			
		/**
			 * Der Tabelle wird ein Objekt von ElementPaar hinzugefügt, welches in den folgenden Zeilen befüllt wird
			 */
			ElementPaar baugruppenPaar = new ElementPaar();
			baugruppenPaar.setAnzahl(editBaugruppe.getStueckliste().getBaugruppenPaare().get(i).getAnzahl());
			baugruppenPaar.setElement(editBaugruppe.getStueckliste().getBaugruppenPaare().get(i).getElement());

		/** 
			 * Dem Vektor aller Baugruppen der Baugruppe wird das soeben
			 *erstellte ElementPaar hinzugefügt
			 */
			collectBaugruppen.add(baugruppenPaar);
			
			/** 
			 * Button, zum Entfernen von Baugruppen im BaugruppeCollection-Tabelle sowie
			 * gleichzeitig auch im Vektor 
			 */
			final Button removeBgButton = new Button("x");
			
			baugruppeCollection.setText(i+1, 0, ""+ editBaugruppe.getStueckliste().getBaugruppenPaare().get(i).getElement().getId());
			baugruppeCollection.setText(i+1, 1, ""+ editBaugruppe.getStueckliste().getBaugruppenPaare().get(i).getAnzahl());
			baugruppeCollection.setText(i+1, 2, editBaugruppe.getStueckliste().getBaugruppenPaare().get(i).getElement().getName());
			baugruppeCollection.setWidget(i+1, 3, removeBgButton);
			
			/**
			 * Durch den Aufruf des Clickhandler removeBtButton 
			 * lassen sich Elementpaare aus der BauGruppenCollection Tabelle wieder entfernen.
			 * Anmerkung - Wenn lediglich die Anzahl eines Baugruppen Objektes verändert werden soll, 
			 * muss das gesamte ElementPaar von der BaugruppenCollection Tabelle entfernt und erneut 
			 * mit der richtigen Anzahl der Baugruppe dem Collection Table wieder hinzugefügt werden.
			 */

			removeBgButton.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {

					/**
					 *  Zum einen wird die entsprechende Reihe aus der
					 *  im FlexTable entfernt
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
				}
			});
			
		}

		/**
		 *  Um das Dropdown mit Bauteilen aus der DB zu befüllen, wird dieser
		 *  RPC-Aufruf gestartet.
		 */
		stuecklistenVerwaltung.getAllBauteile(new GetAllBauteileCallback());

		
		/**
		 * ClickHandler BauteilCollection Table 
		 * 
		 * Hinzufügen von neuen Bauteilen 
		 */
		collectBtButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {

			/**
				 *  Der index dient dazu, herauszufinden, welches Element im
				 *  im DropDown Menu ausgewählt wurde
				 */
				int index = listBoxBauteile.getSelectedIndex();

				/** 
				 * Umwandlung des Datentyps amountBauteile in int
				 */

				Integer anzahl = Integer.parseInt(amountBauteile.getText());

				/**
				 * Der Tabelle wird ein Objekt von ElementPaar hinzugefügt,
				 * welches befüllt wird				 
				 */
				ElementPaar bauteilPaar = new ElementPaar();
				bauteilPaar.setAnzahl(anzahl);
				bauteilPaar.setElement(allBauteile.get(index));

				/**
				 *  Dem Vektor aller Bauteile der Baugruppe wird das soeben
				 *  erstellte ElementPaar hinzugefügt
				 */
				collectBauteile.add(bauteilPaar);

				/**
				 *  ListBox-Element, dass das Hinzufügen von doppelten Elementpaaren spert (Elementpaar wird in der Anzeige ausgegraut)
				 *  
				 */

				listBoxBauteile.getElement().getElementsByTagName("option").getItem(index).setAttribute("disabled", "disabled");

				/**
				 * Die Übersichtstabelle, welche für den User eine hilfreiche
				 * Übersicht aller hinzugefügten Bauteile
				 * bereitstellt, wird mithilfe dieser for-Schleife aufgebaut.
				 * Die Schleife startet bei i = 1, da die
				 * erste Reihe der Tabelle bereits mit den Überschriften befüllt
				 * ist und diese nicht überschrieben werden
				 * soll.
				 */
				for (int i = 1; i <= collectBauteile.size(); i++) {

					/**
					 * Erzeugen eines Buttons, um in der BauteilCollection-Tabelle und
					 * gleichzeitig dem Vektor ein Bauteil wieder zu entfernen
					 */
					final Button removeBtButton = new Button("x");

					/**
					 *  Der Wert von i muss final sein, damit sie im
					 * nachfolgenden ClickHandler verwendet werden kann.
					 * Daher wird sie mithilfe von int a finalisiert.
					 */
					final int a = i;

					/**
					 * Die Tabelle befüllt sich aus allen Elementen, die im
					 * collectBauteile Vektor vorhanden sind
					 */
					bauteilCollection.setText(a, 0, ""+ collectBauteile.get(i - 1).getElement().getId());
					bauteilCollection.setText(a, 1, "" + collectBauteile.get(i - 1).getAnzahl());
					bauteilCollection.setText(a, 2, collectBauteile.get(i - 1).getElement().getName());
					bauteilCollection.setWidget(a, 3, removeBtButton);

					/**
					 * Durch den Aufruf des Clickhandler removeBtButton 
					 * lassen sich Elementpaare aus der BauteilCollection Tabelle wieder entfernen.
					 * Anmerkung - Wenn lediglich die Anzahl eines Bauteil Objekt verändert werden soll, 
					 * muss das gesamte ElementPaar von der BauteilCollection Tabelle entfernt und erneut 
					 * mit der richtigen Anzahl der Bauteil dem Collection Table wieder hinzugefügt werden.
					 */
					removeBtButton.addClickHandler(new ClickHandler() {
						@Override
						public void onClick(ClickEvent event) {

							/**
							 * Entfernen des ElementPaares Bauteil aus dem
							 * FlexTable.
							 */
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
						}
					});
				}

			}

		});

		/**
		 *  Um das Dropdown mit Baugruppen aus der DB zu befüllen, wird dieser
		 *  RPC-Aufruf gestartet
		 */
		 stuecklistenVerwaltung.getAllBaugruppen(new GetAllBaugruppenCallback());

		/**
		 * ClickHandler BaugrupenCollection Table 
		 * 
		 *  Hinzufügen von neuen Baugruppen
		 */

		collectBgButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {

			/**
				 *  Der index dient dazu, herauszufinden, welches Element im
				 *  im DropDown Menu ausgewählt wurde
				 */
				int index = listBoxBaugruppen.getSelectedIndex();

				/** 
				 * Umwandlung des Datentyps amountBaugruppen (Textbox) in int
				 */
				Integer anzahl = Integer.parseInt(amountBaugruppen.getText());

				/**
				 *  Der Tabelle wird ein Objekt von ElementPaar hinzugefügt,
				 *  welches befüllt wird
				 */
				ElementPaar baugruppePaar = new ElementPaar();
				baugruppePaar.setAnzahl(anzahl);
				baugruppePaar.setElement(allBaugruppen.get(index));

				/**
				 *  Dem Vektor aller Baugruppen der Baugruppe wird das soeben
				 *  erstellte ElementPaar hinzugefügt
				 */
				collectBaugruppen.add(baugruppePaar);

				/**
				 *  ListBox-Element, dass das Hinzufügen von doppelten, identischen Elementpaaren untersagt
				 *  
				 */
				listBoxBaugruppen.getElement().getElementsByTagName("option")
						.getItem(index).setAttribute("disabled", "disabled");

				/**
				 * Die Übersichtstabelle, welche für den User eine hilfreiche
				 * Übersicht aller hinzugefügten Baugruppen
				 * bereitstellt, wird mithilfe dieser for-Schleife aufgebaut.
				 * Die Schleife startet bei i = 1, da die
				 * erste Reihe der Tabelle bereits mit den Überschriften befüllt
				 * ist und diese nicht überschrieben werden
				 * soll.
				 */
				for (int i = 1; i <= collectBaugruppen.size(); i++) {

					/** Button, um in der BaugruppeCollection-Tabelle und
					 * gleichzeitig dem Vektor eine Baugruppe
					 * wieder zu entfernen
					 * */
					final Button removeBgButton = new Button("x");

					/**
					 * Der Wert von i muss final sein, damit sie im
					 * nachfolgenden ClickHandler verwendet werden kann.
					 * Daher wird sie mithilfe von int b finalisiert.
					 */
					final int b = i;

					/**
					 *  Die Tabelle befüllt sich aus allen Elementen, die im
					 *  collectBAugruppen Vektor vorhanden sind
					 */
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

					/**
					 * Durch den Aufruf des Clickhandler removeBgButton 
					 * lassen sich Elementpaare aus der BaugruppenCollection Tabelle wieder entfernen.
					 * Anmerkung - Wenn lediglich die Anzahl eines Baugruppen Objekt verändert werden soll, 
					 * muss das gesamte ElementPaar von der BaugruppenCollection Tabelle entfernt und erneut 
					 * mit der richtigen Anzahl der Baugruppen dem Collection Table wieder hinzugefügt werden.
					 */

					removeBgButton.addClickHandler(new ClickHandler() {
						public void onClick(ClickEvent event) {

							/**
							 * Entfernen des ElementPaares Baugruppe aus dem
							 * FlexTable.
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
						}

					});
				}

			}

		});

		/**
		 * Horizontales Anordnen von zugehörigen Stücklisten-Widgets
		 */
		stuecklistePanel.add(sIdField);
		stuecklistePanel.add(sNameField);
		
		/**
		 * Horizontales Anordnen von zugehörigen Bauteil-Widgets
		 */
		btPanel.add(amountBauteile);
		btPanel.add(listBoxBauteile);
		btPanel.add(collectBtButton);

		/**
		 *  Horizontales Anordnen von zugehörigen Baugruppe-Widgets
		 */
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
		this.add(sIdLabel);
		this.add(stuecklistePanel);
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
		this.add(EditBaugruppeButton);
		
		/**
		 * Das Id-Textfeld darf nicht verändert werden und wird daher auf
		 * "ReadOnly" gesetzt.
		 */
		IdField.setReadOnly(true);
		sIdField.setReadOnly(true);
		sNameField.setReadOnly(true);

		/**
		 * Diverse css-Formatierungen
		 */
		HeadlineLabel.setStyleName("headline");
		SublineLabel.setStyleName("subline");
		sIdField.setStyleName("numericInput");
		amountBauteile.setStyleName("numericInput");
		amountBaugruppen.setStyleName("numericInput");
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
		Integer sId = new Integer(editBaugruppe.getStueckliste().getId());

		/**
		 * Mithilfe des an diese Klasse übergebenen Baugruppen-Objektes werden
		 * die Textfelder befüllt.
		 */
		IdField.setText(iD.toString());
		NameField.setText(editBaugruppe.getName());
		sIdField.setText(sId.toString());
		sNameField.setText(editBaugruppe.getStueckliste().getName());

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
	 * mitgeschickten Baugruppen-Objektes das bestehende Baugruppen-Objekt in
	 * der Datenbank ändert. Hierbei ist wichtig, dass keine neue Id vergeben
	 * wird, da es sich sonst um eine Neuanlage und nicht um einen
	 * Editier-Vorgang handeln würde.
	 * 
	 */
	private class EditClickHandler implements ClickHandler {
		@Override
		public void onClick(ClickEvent event) {

			
			
			/**
			 * Vor dem Aufruf der RPC-Methode create wird geprüft, ob alle
			 * notwendigen Felder befüllt sind.
			 */
			if (NameField.getText().isEmpty() == false) {
				
				FieldVerifier umlaut = new FieldVerifier();
				String input = umlaut.changeUmlaut(NameField.getText());
				Baugruppe b = new Baugruppe();
				Stueckliste s = new Stueckliste();
				/**
				 * Aus einem Textfeld kann kein Integer-Wert ausgelesen werden,
				 * daher ist dieser Zwischenschritt notwendig: Auslesen des Id-Werts
				 * mithilfe Integer, da Integer die toString-Methode unterstützt.
				 */
				b.setId(Integer.parseInt(IdField.getText()));
				b.setName(input);
				s.setId(Integer.parseInt(sIdField.getText()));
				s.setName(sNameField.getText());
				s.setBauteilPaare(collectBauteile);
				s.setBaugruppenPaare(collectBaugruppen);
				b.setStueckliste(s);
				/**
				 * Die konkrete RPC-Methode für den editier-Befehl wird
				 * aufgerufen. Hierbei wird das vorab befüllte Baugruppen-Objekt
				 * mit den gewünschten Werten mitgeschickt.
				 */
				stuecklistenVerwaltung.saveBaugruppe(b, new SaveCallback());

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
	 * 
	 */
	class SaveCallback implements AsyncCallback<Void> {

		@Override
		public void onFailure(Throwable caught) {
			Window.alert(caught.getMessage());
		}

		@Override
		public void onSuccess(Void result) {
			Window.alert("Die Baugruppe wurde erfolgreich editiert.");

		}
	}

	/**
	 * Hiermit wird die RPC-Methode aufgerufen, die einen Vektor von allen in
	 * der DB vorhandenen Bauteilen liefert. Die Klasse ist eine nested-class
	 * und erlaubt daher, auf die Attribute der übergeordneten Klasse
	 * zuzugreifen.
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
					for (int i=0; i<eBaugruppe.getStueckliste().getBauteilPaare().size();i++){
						if(allBauteile.get(c).getId()==eBaugruppe.getStueckliste().getBauteilPaare().get(i).getElement().getId()){
							listBoxBauteile.getElement().getElementsByTagName("*")
							.getItem(c).setAttribute("disabled", "disabled");
						}
					}
					

				}

			}

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
					for (int i=0; i<eBaugruppe.getStueckliste().getBaugruppenPaare().size();i++){
						if(allBaugruppen.get(c).getId()==eBaugruppe.getStueckliste().getBaugruppenPaare().get(i).getElement().getId()){
							listBoxBaugruppen.getElement().getElementsByTagName("*")
							.getItem(c).setAttribute("disabled", "disabled");
						}
					}

				}

			}

		}
	}

	/**
	 * Handler prüft zum einen, ob das Anzahl-Feld leer ist. Falls ja erscheint
	 * eine Hinweismeldung.
	 * Ist das Feld befüllt, wird mithilfe der Methode "istZahl" aus der Klasse
	 * FieldVerifier geprüft,
	 * ob im Textfeld eine Zahl eingetragen wurde. Falls nicht, erscheint
	 * ebenfalls eine Hinweismeldung.
	 */
	private class numericBtHandler implements ClickHandler {
		@Override
		public void onClick(ClickEvent event) {

			if (amountBauteile.getText().isEmpty() == true) {
				Window.alert("Bitte die gewünschte Anzahl eintragen.");
			} else if (FieldVerifier.istZahl(amountBauteile.getText()) == false) {
				Window.alert("Bitte nur Zahlen eintragen.");
			}

		}
	}

/**
	 * Handler prüft zum einen, ob das Anzahl-Feld leer ist. Falls ja erscheint
	 * eine Hinweismeldung.
	 * Ist das Feld befüllt, wird mithilfe der Methode "istZahl" aus der Klasse
	 * FieldVerifier geprüft,
	 * ob im Textfeld eine Zahl eingetragen wurde. Falls nicht, erscheint
	 * ebenfalls eine Hinweismeldung.
	 */
	private class numericBgHandler implements ClickHandler {
		@Override
		public void onClick(ClickEvent event) {

			if (amountBaugruppen.getText().isEmpty() == true) {
				Window.alert("Bitte die gewünschte Anzahl eintragen.");
			} else if (FieldVerifier.istZahl(amountBaugruppen.getText()) == false) {
				Window.alert("Bitte nur Zahlen eintragen.");
			}

		}
	}

}
