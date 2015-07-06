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
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import de.hdm.gruppe1.client.ClientsideSettings;
import de.hdm.gruppe1.shared.bo.Baugruppe;
import de.hdm.gruppe1.shared.bo.Stueckliste;
import de.hdm.gruppe1.shared.report.SmsReportAsync;

public class Strukturstuecklisten extends VerticalPanel {

	/**
	 * GUI-Elemente für Strukturstuecklisten initialisieren
	 */
	private final Label HeadlineLabel = new Label("Strukturstückliste anzeigen");
	private final Label SublineLabel = new Label("Um eine Strukturstückliste zu generieren, wählen Sie zunächst eine Baugruppe im Dropdown aus.");
	private final Label baugruppeLabel = new Label("Gewünschte Baugruppe auswählen");
	ListBox listBoxBaugruppen = new ListBox();
	private final Button CreateStrukturstuecklisteButton = new Button("erstellen");
	private final String headlineString = new String("Strukturstückliste für folgende Baugruppe: ");
	Date date = new Date();
	private DateTimeFormat creationDate = DateTimeFormat.getFormat("dd.MM.yyyy HH:mm:ss");
	private final String reportCreationDateString = new String("Report erstellt am: "+creationDate.format(date));
	private final String stuecklistenCreationDateString = new String("Stückliste erstellt am: ");
	private final String stuecklistenIDString = new String("Stücklisten ID: ");
	private String impressumString = new String();
	
	/**
	 * Remote Service via ClientsideSettings wird an dieser Stelle einmalig in
	 * der Klasse aufgerufen. Im Anschluss kann jederzeit darauf zugegriffen
	 * werden.
	 */
	SmsReportAsync stuecklistenReportVerwaltung = ClientsideSettings.getReportGenerator();
	
	// Vektor wird mit allen Baugruppen aus der DB befüllt
	Vector<Baugruppe> allBaugruppe = new Vector<Baugruppe>();
	
	// Konstruktor der Klasse Strukturstuecklisten. Gibt vor, dass bei jeder
	// Instantiierung die entsprechenden GUI-Elemente geladen werden.
	public Strukturstuecklisten() {
		
		// Um das Dropdown mit Enderzeugnissen aus der DB zu befüllen, wird dieser
		// RPC-Aufruf gestartet
		stuecklistenReportVerwaltung.getAllBaugruppen(new GetAllBaugruppenCallback());
		
		/**
		 * Nachdem alle Elemente geladen sind, wird alles dem VerticalPanel
		 * zugeordnet, da diese Klasse von VerticalPanel erbt.
		 */
		this.add(HeadlineLabel);
		this.add(SublineLabel);
		this.add(baugruppeLabel);
		this.add(listBoxBaugruppen);
		this.add(CreateStrukturstuecklisteButton);
		
		/**
		 * Diverse css-Formatierungen
		 */
		HeadlineLabel.setStyleName("headline");
		SublineLabel.setStyleName("subline");
		CreateStrukturstuecklisteButton.setStyleName("Button");
		
		/**
		 * Der Create-Button ruft die RPC-Methode auf, welche das Erstellen
		 * einer Strukturstückliste ermöglicht.
		 */
		CreateStrukturstuecklisteButton.addClickHandler(new CreateClickHandler());
		
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
			allBaugruppe = alleBaugruppen;

			if (allBaugruppe.isEmpty() == true) {

				Window.alert("Es sind leider keine Daten in der Datenbank vorhanden.");

			} else {

				/**
				 * Die Schleife durchläuft den kompletten Ergebnis-Vektor.
				 */
				for (int c = 0; c <= allBaugruppe.size(); c++) {

					/**
					 * Das DropDown wird mithilfe dieser for-Schleife für jede
					 * Baugruppe mit dessen Namen befüllt.
					 */
					listBoxBaugruppen.addItem(allBaugruppe.get(c).getName());

				}

			}

		}
	}
	
	/**
	 * Hiermit wird die RPC-Methode aufgerufen, die eine Strukturstückliste erstellt.
	 * 
	 * @author Mario
	 * 
	 */
	private class CreateClickHandler implements ClickHandler {
		@Override
		public void onClick(ClickEvent event) {
			
			final int index = listBoxBaugruppen.getSelectedIndex();
			Baugruppe baugruppe = allBaugruppe.get(index);
			Stueckliste baugruppenStueckliste = baugruppe.getStueckliste();
			
				/**
				 * Die konkrete RPC-Methode für den create-Befehl wird
				 * aufgerufen. Hierbei werden die gewünschten Werte
				 * mitgeschickt.
				 */
//			stuecklistenReportVerwaltung.createBaugruppenReport(BaugruppenStueckliste, new BaugruppenReportCallback());

			//Test für Tree im Client-Package
			TreeViewReport treeReport = new TreeViewReport(baugruppenStueckliste, 1);
			
			/**
			 * Eine Instanz der Klasse Impressum wird erstellt und an dieser Stelle dem report1-String hinzugefügt.
			 */
			ImpressumReport imp = new ImpressumReport();
			impressumString = imp.setImpressum();
			
			HTML reportHTML = new HTML("<h3>"+headlineString+baugruppe.getName()+"</h3>"+reportCreationDateString+"</p>"
			+stuecklistenIDString+baugruppenStueckliste.getId()+"<br>"+stuecklistenCreationDateString
			+creationDate.format(baugruppenStueckliste.getCreationDate())+"</p>"
			+treeReport.toString()+"<p>"+impressumString);
			
			RootPanel.get("content_wrap").clear();
			RootPanel.get("content_wrap").add(reportHTML);
			
		}
	}
	
}