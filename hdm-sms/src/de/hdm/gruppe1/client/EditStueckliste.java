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
	 * GUI-Elemente für EditStueckliste initialisieren.
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

	/**
	 * Einige GUI-Elemente sollen nebeneinander angezeigt werden, nicht vertikal. Daher wird
	 * ein horizontales "Zwischen-Panel" benötigt.
	 */
	HorizontalPanel btPanel = new HorizontalPanel();
	HorizontalPanel bgPanel = new HorizontalPanel();

	/**
	 *  Diese Vektoren werden mit allen Bauteilen bzw. Baugruppen aus der DB befüllt.
	 */
	Vector<Bauteil> allBauteile = new Vector<Bauteil>();
	Vector<Baugruppe> allBaugruppen = new Vector<Baugruppe>();

	/**
	 *  Vektoren, um die hinzugefügten Bauteile/Baugruppen in einer Übersicht zu
	 *  sammeln, bevor die Stückliste gespeichert wird.
	 */
	Vector<ElementPaar> collectBauteile = new Vector<ElementPaar>();
	Vector<ElementPaar> collectBaugruppen = new Vector<ElementPaar>();

	/**
	 *  Tabellen, um in der GUI alle Bauteile/Baugruppen anzuzeigen, bevor die
	 *  Stückliste gespeichert wird.
	 */
	FlexTable bauteilCollection = new FlexTable();
	FlexTable baugruppeCollection = new FlexTable();
	
	/**
	 * Diese Stückliste wird außerhalb des Konstruktors initialisiert, da sie in der Klasse GetAllBauteileCallback
	 * und GetAllBaugruppen benötigt wird.
	 */
	Stueckliste eStueckliste = new Stueckliste();

	/**
	 * Remote Service via ClientsideSettings wird an dieser Stelle einmalig in
	 * der Klasse aufgerufen. Im Anschluss kann jederzeit darauf zugegriffen
	 * werden.
	 */
	SmsAsync stuecklistenVerwaltung = ClientsideSettings.getSmsVerwaltung();

	public EditStueckliste(Stueckliste editStueckliste) {
		
		/**
		 * Hiermit kann der Inhalt der zu editierenden Stückliste in der Klasse GetAllBauteileCallback und
		 * GetAllBaugruppen verwendet werden.
		 */
		eStueckliste=editStueckliste;

		/**
		 * TextBoxen werden mit Text vorbefüllt, der ausgeblendet wird, sobald
		 * die TextBox vom User fokussiert wird. Dadurch wird eine klare Übersicht
		 * für den Benutzer geschaffen.
		 */
		amountBauteile.getElement().setPropertyString("placeholder", "Anzahl");
		amountBaugruppen.getElement().setPropertyString("placeholder", "Anzahl");

		/**
		 *  ClickHandler um zu prüfen, ob die Texteingabe numerisch ist.
		 */
		collectBtButton.addClickHandler(new numericBtHandler());
		collectBgButton.addClickHandler(new numericBgHandler());

		/**
		 * Die erste Reihe der Tabelle wird mit Überschriften vordefiniert. Aus diesem Grund wird in allen
		 * nachfolgenden Funktionen, in denen die Tabelle befüllt wird, jeweils der Reihen-Index +1 gesetzt.
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
		 * Diverse css-Formatierungen für die Tabelle.
		 */
		bauteilCollection.setStyleName("tableBody");
		baugruppeCollection.setStyleName("tableBody");

		/**
		 * Das FlexTable Widget unterstützt keine Headlines. Daher wird die
		 * erste Reihe über folgenden Umweg formatiert.
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
		 * Das editStueckliste-Objekt, welches in diese Klasse beim Aufruf geliefert wird, kann bereits
		 * Bauteile beinhalten. Diese werden vorab in die ZwischenTabelle (collectBauteile) geschrieben.
		 */
		for(int i = 0; i<editStueckliste.getBauteilPaare().size(); i++) {
			
			/**
			 *  Dem Vektor collectBauteile wird ein Objekt von ElementPaar hinzugefügt,
			 *  welches in den folgenden Zeilen befüllt wird.
			 */
			ElementPaar bauteilPaar = new ElementPaar();
			bauteilPaar.setAnzahl(editStueckliste.getBauteilPaare().get(i).getAnzahl());
			bauteilPaar.setElement(editStueckliste.getBauteilPaare().get(i).getElement());

			/**
			 *  Dem Vektor aller Bauteile der Stückliste wird das soeben erstellte ElementPaar hinzugefügt.
			 */
			collectBauteile.add(bauteilPaar);
			
			/**
			 *  Button, um in der BauteilCollection-Tabelle und
			 *  gleichzeitig dem Vektor ein Bauteil wieder zu entfernen.
			 */
			final Button removeBtButton = new Button("x");
			
			bauteilCollection.setText(i+1, 0, ""+ editStueckliste.getBauteilPaare().get(i).getElement().getId());
			bauteilCollection.setText(i+1, 1, ""+ editStueckliste.getBauteilPaare().get(i).getAnzahl());
			bauteilCollection.setText(i+1, 2, editStueckliste.getBauteilPaare().get(i).getElement().getName());
			bauteilCollection.setWidget(i+1, 3, removeBtButton);
			
			/**
			 *  In jeder Reihe wird ein Entfernen-Button platziert, damit der User schnell und unkompliziert
			 *  jederzeit ein ElementPaar von Bauteil wieder entfernen kann. Es ist ihm lediglich möglich,
			 *  gesamte ElementPaare von Bauteilen zu entfernen. Dies muss er ebenfalls durchführen, wenn er
			 *  lediglich die Anzahl ändern möchte. Das Bauteil mit der gewünschten neuen Anzahl kann er
			 *  schnell erneut aus dem Dropdown hinzufügen.
			 */
			removeBtButton.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {

					/**
					 * Mithilfe der Id wird in dieser for-Schleife das entsprechende Bauteil im
					 * collectBauteile-Vektor entfernt. Wurde es gefunden, bricht die Schleife ab.
					 */
					int rowIndex = bauteilCollection.getCellForEvent(event).getRowIndex();
					Integer id = new Integer(bauteilCollection.getText(rowIndex, 0));
					
					/**
					 * Mithilfe der Id wird in dieser for-Schleife das entsprechende Bauteil im
					 * collectBauteile-Vektor entfernt. Wurde es gefunden, bricht die Schleife ab.
					 */
					for(int i=0; i<collectBauteile.size(); i++){
						if(collectBauteile.get(i).getElement().getId()==id){
							collectBauteile.remove(i);
							break;
						}
					}
					
					/**
					 * Zudem wird auch das Bauteil (ebenfalls mithilfe seiner Id) im Dropdown
					 * ausgegraut und für weiteres Hinzufügen gesperrt.
					 */
					for(int i=0; i<allBauteile.size();i++){
						if(allBauteile.get(i).getId()==id){
							listBoxBauteile.getElement().getElementsByTagName("*").getItem(i).removeAttribute("disabled");
							break;
						}
					}
					
					/**
					 * Hiermit erfolgt das Entfernen der zugehörigen Reihe in der FlexTable.
					 */
					bauteilCollection.removeRow(rowIndex);
					
				}
			});
			
		}
		
		/**
		 * Das editStueckliste-Objekt, welches in diese Klasse beim Aufruf geliefert wird, kann bereits
		 * Baugruppen beinhalten. Diese werden vorab in die ZwischenTabelle (collectBauteile) geschrieben.
		 */
		for(int i = 0; i<editStueckliste.getBaugruppenPaare().size(); i++) {
			
			/**
			 *  Dem Vektor collectBaugruppen wird ein Objekt von ElementPaar hinzugefügt,
			 *  welches in den folgenden Zeilen befüllt wird.
			 */
			ElementPaar baugruppenPaar = new ElementPaar();
			baugruppenPaar.setAnzahl(editStueckliste.getBaugruppenPaare().get(i).getAnzahl());
			baugruppenPaar.setElement(editStueckliste.getBaugruppenPaare().get(i).getElement());

			/**
			 *  Dem Vektor aller Baugruppen der Stückliste wird das soeben erstellte ElementPaar hinzugefügt.
			 */
			collectBaugruppen.add(baugruppenPaar);
			
			/**
			 *  Button, um in der BaugruppeCollection-Tabelle und
			 *  gleichzeitig dem Vektor eine Baugruppe wieder zu entfernen.
			 */
			final Button removeBgButton = new Button("x");
			
			baugruppeCollection.setText(i+1, 0, ""+ editStueckliste.getBaugruppenPaare().get(i).getElement().getId());
			baugruppeCollection.setText(i+1, 1, ""+ editStueckliste.getBaugruppenPaare().get(i).getAnzahl());
			baugruppeCollection.setText(i+1, 2, editStueckliste.getBaugruppenPaare().get(i).getElement().getName());
			baugruppeCollection.setWidget(i+1, 3, removeBgButton);
			
			/**
			 *  In jeder Reihe wird ein Entfernen-Button platziert, damit der User schnell und unkompliziert
			 *  jederzeit ein ElementPaar von Baugruppe wieder entfernen kann. Es ist ihm lediglich möglich,
			 *  gesamte ElementPaare von Baugruppen zu entfernen. Dies muss er ebenfalls durchführen, wenn er
			 *  lediglich die Anzahl ändern möchte. Die Baugruppe mit der gewünschten neuen Anzahl kann er
			 *  schnell erneut aus dem Dropdown hinzufügen.
			 */
			removeBgButton.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {

					/**
					 * Mithilfe der Id wird in dieser for-Schleife die entsprechende Baugruppe im
					 * collectBaugruppen-Vektor entfernt. Wurde es gefunden, bricht die Schleife ab.
					 */
					int rowIndex = baugruppeCollection.getCellForEvent(event).getRowIndex();
					Integer id = new Integer(baugruppeCollection.getText(rowIndex, 0));
					
					/**
					 * Mithilfe der Id wird in dieser for-Schleife die entsprechende Baugruppe im
					 * collectBaugruppen-Vektor entfernt. Wurde es gefunden, bricht die Schleife ab.
					 */
					for(int i=0; i<collectBaugruppen.size(); i++){
						if(collectBaugruppen.get(i).getElement().getId()==id){
							collectBaugruppen.remove(i);
							break;
						}
					}
					
					/**
					 * Zudem wird auch die Baugruppe (ebenfalls mithilfe seiner Id) im Dropdown
					 * ausgegraut und für weiteres Hinzufügen gesperrt.
					 */
					for(int i=0; i<allBaugruppen.size();i++){
						if(allBaugruppen.get(i).getId()==id){
							listBoxBaugruppen.getElement().getElementsByTagName("*").getItem(i).removeAttribute("disabled");
							break;
						}
					}
					
					/**
					 * Hiermit erfolgt das Entfernen der zugehörigen Reihe in der FlexTable.
					 */
					baugruppeCollection.removeRow(rowIndex);
					
				}
			});
			
		}

		/**
		 * RPC-Methode ausführen, die alle Bauteil-Objekte aus der Datenbank in
		 * einem Vektor zurückliefert. Dadurch wird der Klassen-Vektor
		 * "allBauteile" befüllt.
		 */
		stuecklistenVerwaltung.getAllBauteile(new GetAllBauteileCallback());

		/**
		 *  Mithilfe des Hinzufügen-Buttons wird die BauteilCollection Tabelle befüllt.
		 */
		collectBtButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {

				/**
				 *  Der index dient dazu, herauszufinden, welches Element im DropDown ausgewählt wurde.
				 */
				int index = listBoxBauteile.getSelectedIndex();

				/**
				 *  amountBauteile ist eine TextBox. Dessen Inhalt wird hiermit in einen int-Wert umgewandelt.
				 */
				Integer anzahl = Integer.parseInt(amountBauteile.getText());

				/**
				 *  Dem Vektor collectBauteile wird ein Objekt von ElementPaar hinzugefügt,
				 *  welches in den folgenden Zeilen befüllt wird.
				 */
				ElementPaar bauteilPaar = new ElementPaar();
				bauteilPaar.setAnzahl(anzahl);
				bauteilPaar.setElement(allBauteile.get(index));

				/**
				 *  Dem Vektor aller Bauteile der Stückliste wird das soeben erstellte ElementPaar hinzugefügt.
				 */
				collectBauteile.add(bauteilPaar);

				/**
				 * Das ListBox-Element, welches hinzugefügt wurde, wird für doppeltes Hinzufügen ausgegraut.
				 */
				listBoxBauteile.getElement().getElementsByTagName("option").getItem(index).setAttribute("disabled", "disabled");

				/**
				 *  Die Übersichtstabelle, welche für den User eine hilfreiche
				 *  Übersicht aller hinzugefügten Bauteile
				 *  bereitstellt, wird mithilfe dieser for-Schleife aufgebaut.
				 *  Die Schleife startet bei i = 1, da die
				 *  erste Reihe der Tabelle bereits mit den Überschriften befüllt
				 *  ist und diese nicht überschrieben werden soll.
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
					 *  Die Tabelle befüllt sich aus allen Elementen, die im
					 *  collectBauteile-Vektor vorhanden sind.
					 */
					bauteilCollection.setText(a, 0, ""+ collectBauteile.get(i - 1).getElement().getId());
					bauteilCollection.setText(a, 1, "" + collectBauteile.get(i - 1).getAnzahl());
					bauteilCollection.setText(a, 2, collectBauteile.get(i - 1).getElement().getName());
					bauteilCollection.setWidget(a, 3, removeBtButton);

					/**
					 *  In jeder Reihe wird ein Entfernen-Button platziert, damit der User schnell und unkompliziert
					 *  jederzeit ein ElementPaar von Bauteil wieder entfernen kann. Es ist ihm lediglich möglich,
					 *  gesamte ElementPaare von Bauteilen zu entfernen. Dies muss er ebenfalls durchführen, wenn er
					 *  lediglich die Anzahl ändern möchte. Das Bauteil mit der gewünschten neuen Anzahl kann er
					 *  schnell erneut aus dem Dropdown hinzufügen.
					 */
					removeBtButton.addClickHandler(new ClickHandler() {
						@Override
						public void onClick(ClickEvent event) {

							/**
							 * Mithilfe dieses Abschnitts wird die aktuell angeklickte Reihe ermittelt.
							 */
							int rowIndex = bauteilCollection.getCellForEvent(event).getRowIndex();
							
							/**
							 * Anschließend kann mithilfe dieses Row-Indexes die ID des zugehörigen
							 * Bauteils ausgelesen werden.
							 */
							Integer id = new Integer(bauteilCollection.getText(rowIndex, 0));
							
							/**
							 * Mithilfe der Id wird in dieser for-Schleife das entsprechende Bauteil im
							 * collectBauteile-Vektor entfernt. Wurde es gefunden, bricht die Schleife ab.
							 */
							for(int i=0; i<collectBauteile.size(); i++){
								if(collectBauteile.get(i).getElement().getId()==id){
									collectBauteile.remove(i);
									break;
								}
							}
							
							/**
							 * Zudem wird auch das Bauteil (ebenfalls mithilfe seiner Id) im Dropdown
							 * ausgegraut und für weiteres Hinzufügen gesperrt.
							 */
							for(int i=0; i<allBauteile.size();i++){
								if(allBauteile.get(i).getId()==id){
									listBoxBauteile.getElement().getElementsByTagName("*").getItem(i).removeAttribute("disabled");
									break;
								}
							}
							
							/**
							 * Hiermit erfolgt das Entfernen der zugehörigen Reihe in der FlexTable.
							 */
							bauteilCollection.removeRow(rowIndex);
							
						}
					});
				}

			}

		});

		/**
		 * RPC-Methode ausführen, die alle Baugruppen-Objekte aus der Datenbank in
		 * einem Vektor zurückliefert. Dadurch wird der Klassen-Vektor
		 * "allBaugruppen" befüllt.
		 */
		 stuecklistenVerwaltung.getAllBaugruppen(new GetAllBaugruppenCallback());

			/**
			 *  Mithilfe des Hinzufügen-Buttons wird die BaugruppenCollection Tabelle befüllt.
			 */
		collectBgButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {

				/**
				 *  Der index dient dazu, herauszufinden, welches Element im DropDown ausgewählt wurde.
				 */
				int index = listBoxBaugruppen.getSelectedIndex();

				/**
				 *  amountBaugruppen ist eine TextBox. Diese wird hiermit in einen int-Wert umgewandelt.
				 */
				Integer anzahl = Integer.parseInt(amountBaugruppen.getText());

				/**
				 *  Dem Vektor collectBauteile wird ein Objekt von ElementPaar hinzugefügt,
				 *  welches in den folgenden Zeilen befüllt wird.
				 */
				ElementPaar baugruppePaar = new ElementPaar();
				baugruppePaar.setAnzahl(anzahl);
				baugruppePaar.setElement(allBaugruppen.get(index));

				/**
				 *  Dem Vektor aller Baugruppen der Stückliste wird das soeben erstellte ElementPaar hinzugefügt.
				 */
				collectBaugruppen.add(baugruppePaar);

				/**
				 * Das ListBox-Element, welches hinzugefügt wurde, wird für doppeltes Hinzufügen ausgegraut.
				 */
				listBoxBaugruppen.getElement().getElementsByTagName("option")
						.getItem(index).setAttribute("disabled", "disabled");

				/**
				 *  Die Übersichtstabelle, welche für den User eine hilfreiche
				 *  Übersicht aller hinzugefügten Bauteile
				 *  bereitstellt, wird mithilfe dieser for-Schleife aufgebaut.
				 *  Die Schleife startet bei i = 1, da die
				 *  erste Reihe der Tabelle bereits mit den Überschriften befüllt
				 *  ist und diese nicht überschrieben werden soll.
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
					 *  Die Tabelle befüllt sich aus allen Elementen, die im
					 *  collectBaugruppen-Vektor vorhanden sind.
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
					 *  In jeder Reihe wird ein Entfernen-Button platziert, damit der User schnell und unkompliziert
					 *  jederzeit ein ElementPaar von Baugruppe wieder entfernen kann. Es ist ihm lediglich möglich,
					 *  gesamte ElementPaare von Baugruppen zu entfernen. Dies muss er ebenfalls durchführen, wenn er
					 *  lediglich die Anzahl ändern möchte. Die Baugruppe mit der gewünschten neuen Anzahl kann er
					 *  schnell erneut aus dem Dropdown hinzufügen.
					 */
					removeBgButton.addClickHandler(new ClickHandler() {
						public void onClick(ClickEvent event) {

							/**
							 * Mithilfe der Id wird in dieser for-Schleife die entsprechende Baugruppe im
							 * collectBaugruppen-Vektor entfernt. Wurde es gefunden, bricht die Schleife ab.
							 */
							int rowIndex = baugruppeCollection.getCellForEvent(event).getRowIndex();
							Integer id = new Integer(baugruppeCollection.getText(rowIndex, 0));
							
							/**
							 * Mithilfe der Id wird in dieser for-Schleife die entsprechende Baugruppe im
							 * collectBaugruppen-Vektor entfernt. Wurde es gefunden, bricht die Schleife ab.
							 */
							for(int i=0; i<collectBaugruppen.size(); i++){
								if(collectBaugruppen.get(i).getElement().getId()==id){
									collectBaugruppen.remove(i);
									break;
								}
							}
							
							/**
							 * Zudem wird auch die Baugruppe (ebenfalls mithilfe seiner Id) im Dropdown
							 * ausgegraut und für weiteres Hinzufügen gesperrt.
							 */
							for(int i=0; i<allBaugruppen.size();i++){
								if(allBaugruppen.get(i).getId()==id){
									listBoxBaugruppen.getElement().getElementsByTagName("*").getItem(i).removeAttribute("disabled");
									break;
								}
							}
							
							/**
							 * Hiermit erfolgt das Entfernen der zugehörigen Reihe in der FlexTable.
							 */
							baugruppeCollection.removeRow(rowIndex);
							
						}

					});
				}

			}

		});

		/**
		 *  Horizontales Anordnen von zugehörigen Bauteil-Widgets.
		 */
		btPanel.add(amountBauteile);
		btPanel.add(listBoxBauteile);
		btPanel.add(collectBtButton);

		/**
		 *  Horizontales Anordnen von zugehörigen Baugruppe-Widgets.
		 */
		bgPanel.add(amountBaugruppen);
		bgPanel.add(listBoxBaugruppen);
		bgPanel.add(collectBgButton);

		/**
		 * Nachdem alle Elemente geladen sind, wird alles dem VerticalPanel
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
		 * Diverse css-Formatierungen.
		 */
		HeadlineLabel.setStyleName("headline");
		SublineLabel.setStyleName("subline");
		amountBauteile.setStyleName("numericInput");
		amountBaugruppen.setStyleName("numericInput");
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
		 *  Abschließend wird die Klasse dem RootPanel zugeordnet.
		 */
		RootPanel.get("content_wrap").add(this);

	}

	/**
	 * Hiermit wird die RPC-Methode aufgerufen, die mithilfe eines
	 * mitgeschickten Stücklisten-Objektes das bestehende Stücklisten-Objekt in
	 * der Datenbank ändert. Hierbei ist wichtig, dass keine neue Id vergeben
	 * wird, da es sich sonst um eine Neuanlage und nicht um einen
	 * Editier-Vorgang handeln würde.
	 * 
	 * @author Mario Theiler
	 * 
	 */
	private class EditClickHandler implements ClickHandler {
		@Override
		public void onClick(ClickEvent event) {

			/**
			 * Vor dem Aufruf der RPC-Methode edit wird geprüft, ob alle
			 * notwendigen Felder befüllt sind.
			 */
			if (NameField.getText().isEmpty() != true) {
				
				/**
				 * Dieses Stücklisten-Objekt wird erstellt, um es in den darauffolgenden Zeilen
				 * mit Inhalten aus den individuellen User-Eingaben in den Textfeldern zu
				 * befüllen und dieses anschließend dem RPC mit zu geben.
				 */
				Stueckliste s = new Stueckliste();
				/**
				 * Aus einem Textfeld kann kein Integer-Wert ausgelesen werden,
				 * daher ist dieser Zwischenschritt notwendig: Auslesen des Id-Werts
				 * mithilfe Integer, da Integer die toString-Methode unterstützt.
				 */
				s.setId(Integer.parseInt(IdField.getText()));
				s.setName(NameField.getText());
				s.setBauteilPaare(collectBauteile);
				s.setBaugruppenPaare(collectBaugruppen);
				
				/**
				 * Die konkrete RPC-Methode für den editier-Befehl wird
				 * aufgerufen. Hierbei wird das vorab befüllte Stuecklisten-Objekt
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

		/**
		 * Nach einem nicht erfolgreichen RPC wird folgende Hinweismeldung ausgegeben.
		 */
		@Override
		public void onFailure(Throwable caught) {
			Window.alert("Die Stückliste wurde nicht editiert.");
		}

		/**
		 * Nach einem erfolgreichen RPC wird folgendes ausgeführt.
		 */
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
	 * @author Mario Theiler
	 * 
	 */
	class GetAllBauteileCallback implements AsyncCallback<Vector<Bauteil>> {

		/**
		 * Nach einem nicht erfolgreichen RPC wird folgende Hinweismeldung ausgegeben.
		 */
		@Override
		public void onFailure(Throwable caught) {
			Window.alert("Bauteile konnten nicht geladen werden");
		}

		/**
		 * Nach einem erfolgreichen RPC wird folgendes ausgeführt.
		 */
		@Override
		public void onSuccess(Vector<Bauteil> alleBauteile) {

			/**
			 * Der Bauteil-Vektor allBauteile wird mit dem Ergebnis dieses RPC´s
			 * befüllt.
			 */
			allBauteile = alleBauteile;

			/**
			 * Abfangen eines leeren RPC-Vektors mithilfe dieser Hinweismeldung. Der User erkennt außerdem,
			 * dass es sich nicht um ein Datenbankverbindungs-Problem handelt, da in solchen Fällen eine
			 * andere Hinweismeldung ausgegeben wird.
			 */
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
					
					/**
					 * Die initial im editStueckliste-Objekt vorhandenen Bauteile werden an dieser Stelle
					 * im Dropdown ausgegraut.
					 */
					for (int i=0; i<eStueckliste.getBauteilPaare().size();i++){
						if(allBauteile.get(c).getId()==eStueckliste.getBauteilPaare().get(i).getElement().getId()){
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
	 * @author Mario Theiler
	 * 
	 */
	class GetAllBaugruppenCallback implements AsyncCallback<Vector<Baugruppe>> {

		/**
		 * Nach einem nicht erfolgreichen RPC wird folgende Hinweismeldung ausgegeben.
		 */
		@Override
		public void onFailure(Throwable caught) {
			Window.alert("Baugruppen konnten nicht geladen werden");
		}

		/**
		 * Nach einem erfolgreichen RPC wird folgendes ausgeführt.
		 */
		@Override
		public void onSuccess(Vector<Baugruppe> alleBaugruppen) {

			/**
			 * Der Baugruppen-Vektor allBaugruppen wird mit dem Ergebnis dieses RPC´s
			 * befüllt.
			 */
			allBaugruppen = alleBaugruppen;

			/**
			 * Abfangen eines leeren RPC-Vektors mithilfe dieser Hinweismeldung. Der User erkennt außerdem,
			 * dass es sich nicht um ein Datenbankverbindungs-Problem handelt, da in solchen Fällen eine
			 * andere Hinweismeldung ausgegeben wird.
			 */
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
					
					/**
					 * Die initial im editStueckliste-Objekt vorhandenen Baugruppen werden an dieser Stelle
					 * im Dropdown ausgegraut.
					 */
					for (int i=0; i<eStueckliste.getBaugruppenPaare().size();i++){
						if(allBaugruppen.get(c).getId()==eStueckliste.getBaugruppenPaare().get(i).getElement().getId()){
							listBoxBaugruppen.getElement().getElementsByTagName("*")
							.getItem(c).setAttribute("disabled", "disabled");
						}
					}

				}

			}

		}
	}

	/**
	 * Dieser Handler prüft zum einen, ob das Anzahl-Feld leer ist. Falls ja, erscheint eine Hinweismeldung.
	 * Ist das Feld befüllt, wird mithilfe der Methode "istZahl" aus der Klasse FieldVerifier geprüft, ob
	 * im Textfeld eine Zahl eingetragen wurde. Falls nicht, erscheint ebenfalls eine entsprechende Hinweismeldung.
	 * 
	 * @author Mario Theiler
	 *
	 */
	private class numericBtHandler implements ClickHandler {
		@Override
		public void onClick(ClickEvent event) {

			/**
			 * Prüfen, ob Inhalt im Textfeld vorhanden ist.
			 */
			if (amountBauteile.getText().isEmpty() == true) {
				Window.alert("Bitte die gewünschte Anzahl eintragen.");
			}
			
			/**
			 * Prüfen, ob es sich um eine numerische Eingabe handelt.
			 */
			else if (FieldVerifier.istZahl(amountBauteile.getText()) == false) {
				Window.alert("Bitte nur Zahlen eintragen.");
			}

		}
	}

	/**
	 * Dieser Handler prüft zum einen, ob das Anzahl-Feld leer ist. Falls ja, erscheint eine Hinweismeldung.
	 * Ist das Feld befüllt, wird mithilfe der Methode "istZahl" aus der Klasse FieldVerifier geprüft, ob
	 * im Textfeld eine Zahl eingetragen wurde. Falls nicht, erscheint ebenfalls eine entsprechende Hinweismeldung.
	 * 
	 * @author Mario Theiler
	 *
	 */
	private class numericBgHandler implements ClickHandler {
		@Override
		public void onClick(ClickEvent event) {

			/**
			 * Prüfen, ob Inhalt im Textfeld vorhanden ist.
			 */
			if (amountBaugruppen.getText().isEmpty() == true) {
				Window.alert("Bitte die gewünschte Anzahl eintragen.");
			}
			
			/**
			 * Prüfen, ob es sich um eine numerische Eingabe handelt.
			 */
			else if (FieldVerifier.istZahl(amountBaugruppen.getText()) == false) {
				Window.alert("Bitte nur Zahlen eintragen.");
			}

		}
	}

}