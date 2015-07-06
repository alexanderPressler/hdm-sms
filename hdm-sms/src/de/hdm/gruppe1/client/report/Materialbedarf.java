package de.hdm.gruppe1.client.report;

import java.util.Date;
import java.util.Vector;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.shared.DateTimeFormat;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

import de.hdm.gruppe1.client.ClientsideSettings;
import de.hdm.gruppe1.shared.FieldVerifier;
import de.hdm.gruppe1.shared.bo.ElementPaar;
import de.hdm.gruppe1.shared.bo.Enderzeugnis;
import de.hdm.gruppe1.shared.bo.Stueckliste;
import de.hdm.gruppe1.shared.report.SmsReportAsync;

public class Materialbedarf extends VerticalPanel {

	/**
	 * GUI-Elemente für Materialbedarf initialisieren
	 */
	private final Label HeadlineLabel = new Label("Materialbedarf berechnen");
	private final Label SublineLabel = new Label("Um den gesamten Materialbedarf eines oder mehrerer Enderzeugnisse zu berechnen, wählen Sie zunächst ein Enderzeugnis im Dropdown und anschließend die gewünschte Menge aus.");
	private final Label BaugruppeLabel = new Label("Gewünschte Anzahl von Enderzeugnissen hinzufügen");
	private final TextBox amountEnderzeugnisse = new TextBox();
	ListBox listBoxEnderzeugnisse = new ListBox();
	private final Button createMaterialbedarfButton = new Button("erstellen");
	private final String headlineString = new String("Strukturstückliste und Materialbedarf für folgendes Enderzeugnis: ");
	private final String anzahlString = new String("Anzahl der Enderzeugnisse: ");
	Date date = new Date();
	private DateTimeFormat creationDate = DateTimeFormat.getFormat("dd.MM.yyyy HH:mm:ss");
	private final String reportCreationDateString = new String("Report erstellt am: "+creationDate.format(date));
	private final String stuecklistenCreationDateString = new String("Stückliste erstellt am: ");
	private final String stuecklistenIDString = new String("Stücklisten ID: ");
	private String impressumString = new String();
	private String headlineForSummedBauteile = new String("Summe der Bauteile");
	private String headlineForSummedBaugruppen = new String("Summe der Baugruppen");
	private String bauteilTableString = new String();
	private String baugruppeTableString = new String();
	
	// Panel, um das Baugruppen-Dropdown neben der Anzahl-TextBox zu platzieren
	HorizontalPanel eEPanel = new HorizontalPanel();
	
	// Remote Service via ClientsideSettings
	SmsReportAsync stuecklistenReportVerwaltung = ClientsideSettings.getReportGenerator();
	
	// Vektor wird mit allen Bauteilen bzw. Baugruppen aus der DB befüllt
	Vector<Enderzeugnis> allEnderzeugnisse = new Vector<Enderzeugnis>();
	
	// Konstruktor der Klasse Materialbedarf. Gibt vor, dass bei jeder
	// Instantiierung die entsprechenden GUI-Elemente geladen werden.
	public Materialbedarf() {
		
		stuecklistenReportVerwaltung.getAllEnderzeugnis(new GetAllEnderzeugnisseCallback());
		// TextBox wird mit Text vorbefüllt, der ausgeblendet wird, sobald
		// die TextBox vom User fokussiert wird
		amountEnderzeugnisse.getElement().setPropertyString("placeholder", "Anzahl");
		
		// Um das Dropdown mit Baugruppen aus der DB zu befüllen, wird dieser
		// RPC-Aufruf gestartet
//		stuecklistenReportVerwaltung.getAllEnderzeugnis(new GetAllEnderzeugnisseCallback());
		
		// Horizontales Anordnen von zugehörigen Bauteil-Widgets
		eEPanel.add(amountEnderzeugnisse);
		eEPanel.add(listBoxEnderzeugnisse);
		
		/**
		 * Nachdem alle Elemente geladen sind, wird alles dem VerticalPanel
		 * zugeordnet, da diese Klasse von VerticalPanel erbt.
		 */
		this.add(HeadlineLabel);
		this.add(SublineLabel);
		this.add(BaugruppeLabel);
		this.add(eEPanel);
		this.add(createMaterialbedarfButton);
		
		/**
		 * Diverse css-Formatierungen
		 */
		HeadlineLabel.setStyleName("headline");
		SublineLabel.setStyleName("subline");
		amountEnderzeugnisse.setStyleName("numericInput");
		createMaterialbedarfButton.setStyleName("Button");
		
		/**
		 * Der Create-Button ruft die RPC-Methode auf, welche das Erstellen
		 * einer Stückliste in der DB ermöglicht.
		 */
		createMaterialbedarfButton.addClickHandler(new CreateClickHandler());
		
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
	 * der DB vorhandenen Enderzeugnissen liefert. Die Klasse ist eine nested-class
	 * und erlaubt daher, auf die Attribute der übergeordneten Klasse
	 * zuzugreifen.
	 * 
	 * @author Mario
	 * 
	 */
	class GetAllEnderzeugnisseCallback implements AsyncCallback<Vector<Enderzeugnis>> {

		@Override
		public void onFailure(Throwable caught) {
			Window.alert("Enderzeugnisse konnten nicht geladen werden");
		}

		@Override
		public void onSuccess(Vector<Enderzeugnis> alleEnderzeugnisse) {

			/**
			 * Der Enderzeugnis-Vektor allEnderzeugnisse wird mit dem Ergebnis dieses RPC´s
			 * befüllt.
			 */
			allEnderzeugnisse = alleEnderzeugnisse;

			if (allEnderzeugnisse.isEmpty() == true) {

				Window.alert("Es sind leider keine Daten in der Datenbank vorhanden.");

			} else {

				/**
				 * Die Schleife durchläuft den kompletten Ergebnis-Vektor.
				 */
				for (int c = 0; c <= allEnderzeugnisse.size(); c++) {

					/**
					 * Das DropDown wird mithilfe dieser for-Schleife für jedes
					 * Enderzeugnis mit dessen Namen befüllt.
					 */
					listBoxEnderzeugnisse.addItem(allEnderzeugnisse.get(c).getName());

				}

			}

		}
	}
	
		/**
		 * Hiermit wird die RPC-Methode aufgerufen, die einen Materialbedarf berechnet und anzeigt.
		 * 
		 * @author Mario
		 * 
		 */
		private class CreateClickHandler implements ClickHandler {
			@Override
			public void onClick(ClickEvent event) {

				// Handler prüft zum einen, ob das Anzahl-Feld leer ist. Falls ja erscheint
				// eine Hinweismeldung.
				// Ist das Feld befüllt, wird mithilfe der Methode "istZahl" aus der Klasse
				// FieldVerifier geprüft, ob im Textfeld eine Zahl eingetragen wurde. Falls nicht, erscheint
				// ebenfalls eine Hinweismeldung.
				if (amountEnderzeugnisse.getText().isEmpty() == true) {
					Window.alert("Bitte die gewünschte Anzahl eintragen.");
				} else if (FieldVerifier.istZahl(amountEnderzeugnisse.getText()) == false) {
					Window.alert("Bitte nur Zahlen eintragen.");
					
				//Sofern beide Bedingungen nicht erfüllt sind, wird der CreateCallback ausgeführt
				} else {
					
					final int index = listBoxEnderzeugnisse.getSelectedIndex();
					Enderzeugnis enderzeugnis = allEnderzeugnisse.get(index);
					Stueckliste enderzeugnisStueckliste = enderzeugnis.getBaugruppe().getStueckliste();
					
					/**
					 * Eine Instanz der Klasse Impressum wird erstellt und an dieser Stelle dem report1-String hinzugefügt.
					 */
					ImpressumReport imp = new ImpressumReport();
					impressumString = imp.setImpressum();
					
					Integer anzahl =  Integer.parseInt(amountEnderzeugnisse.getText());
					TreeViewReport treeReport = new TreeViewReport(enderzeugnisStueckliste,anzahl);
					/**
					 * Mithilfe des treeReport-Objektes wird an dieser Stelle der Ergebnis-Vektor aller Bauteile
					 * herangezogen, um ihn anschließend aufzubereiten und als HTML anzuzeigen.
					 */
					Vector<ElementPaar> bauteileAsHtml = treeReport.addiereBauteile();
					
					/**
					 * Diese for-Schleife schreibt für jedes Element im Bauteil-Vektor einen Eintrag und trennt diese
					 * Einträge mithilfe eines HTML-Tags.
					 */
					for(int b = 0; b<bauteileAsHtml.size(); b++) {
						bauteilTableString=bauteilTableString+bauteileAsHtml.get(b).getAnzahl()+" * "+bauteileAsHtml.get(b).getElement().getName()+"</br>";
					}
					
					/**
					 * Mithilfe des treeReport-Objektes wird an dieser Stelle der Ergebnis-Vektor aller Baugruppen
					 * herangezogen, um ihn anschließend aufzubereiten und als HTML anzuzeigen.
					 */
					Vector<ElementPaar> baugruppenAsHtml = treeReport.addiereBaugruppen();
					
					/**
					 * Diese for-Schleife schreibt für jedes Element im Baugruppen-Vektor einen Eintrag und trennt diese
					 * Einträge mithilfe eines HTML-Tags.
					 */
					for(int c = 0; c<baugruppenAsHtml.size(); c++) {
						baugruppeTableString=baugruppeTableString+baugruppenAsHtml.get(c).getAnzahl()+" * "+baugruppenAsHtml.get(c).getElement().getName()+"</br>";
					}
					
					/**
					 * Das HTML-Widget ist in der Lage HTML-Tags zu interpretieren und eignet sich daher ideal für eine
					 * Anzeige des Reports in statischem HTML.
					 */
					HTML reportHTML = new HTML("<h3>"+headlineString+enderzeugnis.getName()+"</h3>"+reportCreationDateString+"</p>"
							+stuecklistenIDString+enderzeugnisStueckliste.getId()+"<br>"+stuecklistenCreationDateString
							+creationDate.format(enderzeugnisStueckliste.getCreationDate())+"</p>"
							+anzahlString+anzahl+"</br>"+"</p>"+treeReport.toString()+"<p><b>"+headlineForSummedBauteile+"</b><p>"
							+bauteilTableString+"<p><b>"+headlineForSummedBaugruppen+"</b><p>"+baugruppeTableString+"<p>"+impressumString);
										
					RootPanel.get("content_wrap").clear();
					RootPanel.get("content_wrap").add(reportHTML);
				}
				
			}
		}
	
}