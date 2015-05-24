package de.hdm.gruppe1.client;

import java.util.Vector;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

import de.hdm.gruppe1.client.CreateBauteil.CreateBauteilCallback;
import de.hdm.gruppe1.shared.SmsAsync;
import de.hdm.gruppe1.shared.bo.Bauteil;

/**
 * Die Klasse BauteilGeneralView liefert eine Übersicht mit allen vorhandenen
 * Bauteilen im System und bietet Möglichkeiten, diese zu editieren oder
 * löschen.
 * 
 * @author Mario Theiler
 * @version 1.0
 */
public class BauteilGeneralView extends VerticalPanel {

	/**
	 * Überschrift, um dem User eine Orientierung zu geben, in welchem Bereich
	 * der Applikation er sich befindet.
	 */
	private final Label HeadlineLabel = new Label("Bauteilübersicht");

	/**
	 * Buttons sollen nebeneinander angezeigt werden, nicht vertikal. Daher wird
	 * ein "vertikales Zwischen-Panel" benötigt.
	 */
	private HorizontalPanel editButtonPanel = new HorizontalPanel();
	private HorizontalPanel deleteButtonPanel = new HorizontalPanel();

	/**
	 * Den Buttons wird jeweils ein erklärender Text hinzugefügt.
	 */
	private final Label editLabel = new Label(
			"Wählen Sie in der Übersicht ein Bauteil aus, um es mithilfe dieses Buttons zu editieren: ");
	private final Label deleteLabel = new Label(
			"Wählen Sie in der Übersicht mindestens ein Bauteil aus, um es mithilfe dieses Buttons zu löschen: ");

	/**
	 * Die RadioButtons und CheckBoxen erhalten jeweils einen globalen edit-
	 * bzw. delete-Button. Dies entspricht dem neuesten Stand der
	 * Web-Programmierung.
	 */
	private final Button editBtn = new Button("");
	private final Button deleteBtn = new Button("");

	/**
	 * Tabelle, in der jegliche Bauteile inkl. edit- & delete-Buttons angezeigt
	 * werden.
	 */
	private final FlexTable table = new FlexTable();

	/**
	 * Bauteil, welches editiert werden soll.
	 */
	Bauteil editBauteil = null;

	/**
	 * Vektor, der mit allen Bauteilen aus der DB befüllt wird.
	 */
	Vector<Bauteil> allBauteile = new Vector<Bauteil>();
	
	Vector<Bauteil> deleteBauteile = new Vector<Bauteil>();
	
	/**
	 * Remote Service via ClientsideSettings Wird an dieser Stelle einmalig in
	 * der Klasse aufgerufen. Im Anschluss kann jederzeit darauf zugegriffen
	 * werden.
	 */
	SmsAsync stuecklistenVerwaltung = ClientsideSettings.getSmsVerwaltung();

	public BauteilGeneralView() {

		/**
		 * Damit die edit und delete Buttons horizontal angeordnet werden,
		 * müssen diese dem ButtonPanel zugeordnet werden.
		 */
		editButtonPanel.add(editLabel);
		editButtonPanel.add(editBtn);

		deleteBtn.addClickHandler(new deleteClickHandler());
		deleteButtonPanel.add(deleteLabel);
		deleteButtonPanel.add(deleteBtn);

		editBtn.setStyleName("editButton");
		deleteBtn.setStyleName("deleteButton");
		HeadlineLabel.setStyleName("headline");
		table.setStyleName("BauteilTable");

		/**
		 * RPC-Methode ausführen, die alle Bauteil-Objekte aus der Datenbank in
		 * einem Vektor zurückliefert. Dadurch wird der Klassen-Vektor
		 * "allBauteile" befüllt.
		 */
		stuecklistenVerwaltung.getAllBauteile(new GetAllBauteileCallback());

		/**
		 * Die erste Reihe der Tabelle wird mit Überschriften vordefiniert.
		 */
		table.setText(0, 0, "ID");
		table.setText(0, 1, "Name");
		table.setText(0, 2, "Material");
		table.setText(0, 3, "Beschreibung");
		table.setText(0, 4, "Letzter Änderer");
		table.setText(0, 5, "Letztes Änderungsdatum");
		table.setText(0, 6, "Editieren");
		table.setText(0, 7, "Löschen");

		/**
		 * Das FlexTable Widget unterstützt keine Headlines. Daher wird die
		 * erste Reihe über folgenden Umweg formatiert.
		 */
		table.getCellFormatter().addStyleName(0, 0, "tableHead");
		table.getCellFormatter().addStyleName(0, 1, "tableHead");
		table.getCellFormatter().addStyleName(0, 2, "tableHead");
		table.getCellFormatter().addStyleName(0, 3, "tableHead");
		table.getCellFormatter().addStyleName(0, 4, "tableHead");
		table.getCellFormatter().addStyleName(0, 5, "tableHead");
		table.getCellFormatter().addStyleName(0, 6, "tableHead");
		table.getCellFormatter().addStyleName(0, 7, "tableHead");

		/**
		 * Nachdem alle Elemente geladen sind, wird alles dem VerticalPanel
		 * zugeordnet, da diese Klasse von VerticalPanel erbt.
		 */
		this.add(HeadlineLabel);
		this.add(editButtonPanel);
		this.add(deleteButtonPanel);
		this.add(table);

		/**
		 * Abschließend wird alles dem RootPanel zugeordnet
		 */
		RootPanel.get("content_wrap").add(this);

	}

	/*
	 * Click Handlers
	 */

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

			/**
			 * Die flexTable table wird mithilfe dieser for-Schleife Reihe um
			 * Reihe für jedes Bauteil befüllt.
			 */
			for (int row = 1; row <= allBauteile.size(); row++) {

				/**
				 * Da die flexTable in Reihen-Index 0 bereits mit den
				 * Tabellen-Überschriften belegt ist (Begründung siehe weiter
				 * oben im Code), wird eine "Hilfs-Variable" benötigt, die den
				 * Tabellen-Index für den Vektor-Index simuliert.
				 */
				final int i = row - 1;

				CheckBox checkBox = new CheckBox("");
				RadioButton radioButton = new RadioButton("editRadioGroup", "");

				/**
				 * Pro Vektor-Index wird eine Reihe in die Tabelle geschrieben.
				 */
				table.setText(row, 0, "" + allBauteile.get(i).getId());
				table.setText(row, 1, allBauteile.get(i).getName());
				table.setText(row, 2, allBauteile.get(i)
						.getMaterialBeschreibung());
				table.setText(row, 3, allBauteile.get(i)
						.getBauteilBeschreibung());
				table.setText(row, 4, "Mario");
				table.setText(row, 5, "02.05.2015, 18 Uhr");

				/**
				 * An dieser Stelle wird pro Schleifendurchlauf ein RadioButton
				 * Widget hinzugefügt. Mithilfe der Eigenschaft von "RadioGroup"
				 * kann jeweils nur ein RadioButton, nach vollständigem Befüllen
				 * der Tabelle, ausgewählt werden.
				 */
				table.setWidget(row, 6, radioButton);

				/**
				 * Dieser RadioButton wird pro Reihe mit einem
				 * ValueChangeHandler erweitert. Dieser erkennt, welcher
				 * RadioButton ausgewählt ist und befüllt das Klassen-Objekt
				 * editBauteil von "Bauteil" mit dem Objekt der entsprechenden
				 * Tabellen-Reihe.
				 */
				radioButton
						.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
							@Override
							public void onValueChange(
									ValueChangeEvent<Boolean> e) {
								if (e.getValue() == true) {
									editBauteil = allBauteile.get(i);
								}
							}
						});

				/**
				 * An dieser Stelle wird pro Schleifendurchlauf ein CheckBox
				 * Widget hinzugefügt.
				 */
				table.setWidget(row, 7, checkBox);
				
				checkBox.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
					@Override
					public void onValueChange(
							ValueChangeEvent<Boolean> e) {
						if (e.getValue() == true) {
							Bauteil deleteBauteil = allBauteile.get(i);
							deleteBauteile.add(deleteBauteil);
							Window.alert("Inhalt Vektor: "+deleteBauteile.toString());
						} else if (e.getValue() == false) {
							Bauteil removeBauteil = allBauteile.get(i);
							deleteBauteile.remove(removeBauteil);
							Window.alert("Gelöscht: "+deleteBauteile.toString());
						}
					}
				});

				/**
				 * Die Tabelle erhält ein css-Element für den Body, welches sich
				 * vom css-Element für die Überschriften unterscheidet.
				 */
				table.setStyleName("tableBody");

			}

			/**
			 * ClickHandler für den Aufruf der Klasse editBauteil. Als Attribut
			 * wird das Bauteil-Objekt aus der entsprechenden Tabellen-Reihe
			 * mitgeschickt.
			 */
			editBtn.addClickHandler(new ClickHandler() {
				public void onClick(ClickEvent event) {

					if (editBauteil == null) {
						Window.alert("Bitte wählen Sie ein Bauteil zum editieren aus.");
					} else {
						RootPanel.get("content_wrap").clear();
						RootPanel.get("content_wrap").add(
								new EditBauteil(editBauteil));
					}

				}

			});

		}
	}
	
	/**
	 * Hiermit wird die RPC-Methode aufgerufen, die ein Bauteil-Objekt löscht
	 * 
	 * @author Mario Alex
	 * 
	 */
	private class deleteClickHandler implements ClickHandler {
		@Override
		public void onClick(ClickEvent event) {
			
			if (deleteBauteile.isEmpty() == true){
				Window.alert("Es wurde kein Bauteil zum Löschen ausgewählt.");
			}
			
			else {
			for (int i=0;i<=deleteBauteile.size();i++) {
			Bauteil b = new Bauteil();
			b = deleteBauteile.get(i);
				/**
				 * Die konkrete RPC-Methode für den create-Befehl wird
				 * aufgerufen. Hierbei werden die gewünschten Werte
				 * mitgeschickt.
				 */
				stuecklistenVerwaltung.delete(b,new DeleteBauteilCallback());
				}
			}
		}
	}

	class DeleteBauteilCallback implements AsyncCallback<Void> {

		@Override
		public void onFailure(Throwable caught) {
			Window.alert("Das Loeschen des Bauteils ist fehlgeschlagen!");
		}

		@Override
		public void onSuccess(Void result) {

			Window.alert("Das Bauteil wurde erfolgreich geloescht.");
		}
	}
}