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
import de.hdm.gruppe1.shared.SmsAsync;
import de.hdm.gruppe1.shared.bo.Stueckliste;

/**
 * Die Klasse BaugruppeGeneralView liefert eine Übersicht mit allen vorhandenen
 * Baugruppen im System und bietet Möglichkeiten, diese zu editieren oder
 * löschen.
 * 
 * @author Mario Theiler
 * @version 1.0
 */
public class StuecklisteGeneralView extends VerticalPanel {

	/**
	 * Überschrift, um dem User eine Orientierung zu geben, in welchem Bereich
	 * der Applikation er sich befindet.
	 */
	private final Label HeadlineLabel = new Label("Stücklistenübersicht");

	/**
	 * Einige GUI-Elemente sollen nebeneinander angezeigt werden, nicht vertikal. Daher wird
	 * ein "vertikales Zwischen-Panel" benötigt.
	 */
	private HorizontalPanel editButtonPanel = new HorizontalPanel();
	private HorizontalPanel deleteButtonPanel = new HorizontalPanel();
	private HorizontalPanel treeViewPanel = new HorizontalPanel();

	/**
	 * Den Buttons wird jeweils ein erklärender Text hinzugefügt.
	 */
	private final Label editLabel = new Label(
			"Markierte Stückliste editieren ");
	private final Label deleteLabel = new Label(
			"Markierte Stückliste(n) löschen ");
	private final Label treeViewLabel = new Label (
			"Markierte Stückliste als Strukturstückliste anzeigen ");

	/**
	 * Die RadioButtons und CheckBoxen erhalten jeweils einen globalen edit-
	 * bzw. delete-Button. Dies entspricht dem neuesten Stand der
	 * Web-Programmierung.
	 */
	private final Button editBtn = new Button("");
	private final Button deleteBtn = new Button("");
	private final Button treeViewBtn = new Button("");

	/**
	 * Tabelle, in der jegliche Stücklisten inkl. edit- & delete-Buttons angezeigt
	 * werden.
	 */
	private final FlexTable table = new FlexTable();

	/**
	 * Stückliste, welche editiert werden soll.
	 */
	Stueckliste editStueckliste = null;
	
	/**
	 * Stückliste, deren Baumstruktur angezeigt werden soll.
	 */
	Stueckliste treeViewStueckliste = null;

	/**
	 * Vektor, der mit allen Stücklisten aus der DB befüllt wird.
	 */
	Vector<Stueckliste> allStuecklisten = new Vector<Stueckliste>();

	/**
	 * Vektor, der alle zu löschenden Stücklisten zwischenspeichert. Dies erfolgt mithilfe von Aus- bzw. Abwählen
	 * der CheckBoxen. Im Anschluss werden diese Stücklisten nacheinander aus der DB gelöscht.
	 */
	Vector<Stueckliste> deleteStuecklisten = new Vector<Stueckliste>();

	/**
	 * Remote Service via ClientsideSettings wird an dieser Stelle einmalig in
	 * der Klasse aufgerufen. Im Anschluss kann jederzeit darauf zugegriffen
	 * werden.
	 */
	SmsAsync stuecklistenVerwaltung = ClientsideSettings.getSmsVerwaltung();

	public StuecklisteGeneralView() {

		/**
		 * Damit die edit und delete Buttons horizontal angeordnet werden,
		 * müssen diese einem separaten horizontalen Panel zugeordnet werden.
		 */
		editButtonPanel.add(editLabel);
		editButtonPanel.add(editBtn);
		deleteButtonPanel.add(deleteLabel);
		deleteButtonPanel.add(deleteBtn);
		treeViewPanel.add(treeViewLabel);
		treeViewPanel.add(treeViewBtn);

		/**
		 * Diverse css-Formatierungen
		 */
		editBtn.setStyleName("editButton");
		deleteBtn.setStyleName("deleteButton");
		treeViewBtn.setStyleName("showButton");
		HeadlineLabel.setStyleName("headline");
		table.setStyleName("tableBody");

		/**
		 * RPC-Methode ausführen, die alle Stücklisten-Objekte aus der Datenbank in
		 * einem Vektor zurückliefert. Dadurch wird der Klassen-Vektor
		 * "allStücklisten" befüllt.
		 */
		stuecklistenVerwaltung.getAllStuecklisten(new GetAllStuecklistenCallback());

		/**
		 * Die erste Reihe der Tabelle wird mit Überschriften vordefiniert.
		 */
		table.setText(0, 0, "ID");
		table.setText(0, 1, "Name");
		table.setText(0, 2, "Erstellungsdatum");
		table.setText(0, 3, "Letzter Änderer");
		table.setText(0, 4, "Letztes Änderungsdatum");
		table.setText(0, 5, "Editieren");
		table.setText(0, 6, "Löschen");
		table.setText(0,7, "Strukturstückliste");

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
		this.add(treeViewPanel);
		this.add(table);

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
	 * der DB vorhandenen Stücklisten liefert. Die Klasse ist eine nested-class
	 * und erlaubt daher, auf die Attribute der übergeordneten Klasse
	 * zuzugreifen.
	 * 
	 * @author Mario Alex
	 * 
	 */
	class GetAllStuecklistenCallback implements AsyncCallback<Vector<Stueckliste>> {

		@Override
		public void onFailure(Throwable caught) {
			Window.alert("Stücklisten konnten nicht geladen werden.");
		}

		@Override
		public void onSuccess(Vector<Stueckliste> alleStuecklisten) {

			/**
			 * Der Stücklisten-Vektor allStuecklisten wird mit dem Ergebnis dieses RPC´s
			 * befüllt.
			 */
			allStuecklisten = alleStuecklisten;

			/**
			 * Abfangen eines leeren RPC-Vektors mithilfe eines Labels, das sich
			 * über die komplette Reihe erstreckt.
			 */
			if (allStuecklisten.isEmpty() == true) {

				table.getFlexCellFormatter().setColSpan(1, 0, 7);

				table.setWidget(1, 0, new Label("Es sind leider keine Daten in der Datenbank vorhanden."));

			}

			else {

				/**
				 * Die flexTable table wird mithilfe dieser for-Schleife Reihe
				 * um Reihe für jede Stückliste befüllt.
				 */
				for (int row = 1; row <= allStuecklisten.size(); row++) {

					/**
					 * Da die flexTable in Reihen-Index 0 bereits mit den
					 * Tabellen-Überschriften belegt ist (Begründung siehe
					 * weiter oben im Code), wird eine "Hilfs-Variable"
					 * benötigt, die den Tabellen-Index für den Vektor-Index
					 * simuliert.
					 */
					final int i = row - 1;

					RadioButton radioButton = new RadioButton("editRadioGroup", "");
					CheckBox checkBox = new CheckBox("");
					RadioButton treeViewButton = new RadioButton("treeViewGroup", "");

					/**
					 * Dem Delete-Button wird der am Ende dieser Klasse erstellte deleteClickHandler zugeordnet.
					 */
					deleteBtn.addClickHandler(new deleteClickHandler());

					/**
					 * Pro Vektor-Index wird eine Reihe in die Tabelle
					 * geschrieben.
					 */
					table.setText(row, 0, "" + allStuecklisten.get(i).getId());
					table.setText(row, 1, allStuecklisten.get(i).getName());
					table.setText(row, 2, allStuecklisten.get(i).getCreationDate().toString().substring(0, 19));
					table.setText(row, 3, allStuecklisten.get(i).getEditUser().getName());
					table.setText(row, 4, allStuecklisten.get(i).getEditDate().toString().substring(0, 19));
					
					/**
					 * An dieser Stelle wird pro Schleifendurchlauf ein
					 * RadioButton Widget hinzugefügt. Mithilfe der Eigenschaft
					 * von "RadioGroup" kann jeweils nur ein RadioButton, nach
					 * vollständigem Befüllen der Tabelle, ausgewählt werden.
					 */
					table.setWidget(row, 5, radioButton);

					/**
					 * Dieser RadioButton wird pro Reihe mit einem
					 * ValueChangeHandler erweitert. Dieser erkennt, welcher
					 * RadioButton ausgewählt ist und befüllt das Klassen-Objekt
					 * editStueckliste von "Stueckliste" mit dem Objekt der
					 * entsprechenden Tabellen-Reihe.
					 */
					radioButton.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
								@Override
								public void onValueChange(
										ValueChangeEvent<Boolean> e) {
									if (e.getValue() == true) {
										editStueckliste = allStuecklisten
												.get(i);
									}
								}
							});

					/**
					 * An dieser Stelle wird pro Schleifendurchlauf ein CheckBox
					 * Widget hinzugefügt.
					 */
					table.setWidget(row, 6, checkBox);

					/**
					 * Basierende darauf, ob der ValueChangeHandler eine Aus- bzw. Abwahl der betroffenen
					 * CheckBox erkennt, wird dem globalen deleteBauteile-Vektor ein Element hinzugefügt
					 * bzw. daraus entfernt.
					 */
					checkBox.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
						@Override
						public void onValueChange(ValueChangeEvent<Boolean> e) {
							if (e.getValue() == true) {
								Stueckliste deleteStueckliste = allStuecklisten
										.get(i);
								deleteStuecklisten.add(deleteStueckliste);
							} else if (e.getValue() == false) {
								Stueckliste removeStueckliste = allStuecklisten
										.get(i);
								deleteStuecklisten.remove(removeStueckliste);
							}
						}
					});
					
					/**
					 * An dieser Stelle wird pro Schleifendurchlauf ein
					 * RadioButton Widget hinzugefügt. Mithilfe der Eigenschaft
					 * von "treeViewGroup" kann jeweils nur ein RadioButton, nach
					 * vollständigem Befüllen der Tabelle, ausgewählt werden.
					 */
					table.setWidget(row, 7, treeViewButton);

					/**
					 * Dieser RadioButton wird pro Reihe mit einem
					 * ValueChangeHandler erweitert. Dieser erkennt, welcher
					 * RadioButton ausgewählt ist und befüllt das Klassen-Objekt
					 * treeViewStueckliste von "Stueckliste" mit dem Objekt der
					 * entsprechenden Tabellen-Reihe.
					 */
					treeViewButton.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
								@Override
								public void onValueChange(
										ValueChangeEvent<Boolean> e) {
									if (e.getValue() == true) {
										treeViewStueckliste = allStuecklisten.get(i);
									}
								}
							});

					/**
					 * Die Tabelle erhält ein css-Element für den Body, welches
					 * sich vom css-Element für die Überschriften unterscheidet.
					 */
					table.setStyleName("tableBody");

				}

			}

			/**
			 * ClickHandler für den Aufruf der Klasse editStueckliste. Als Attribut
			 * wird das Stücklisten-Objekt aus der entsprechenden Tabellen-Reihe
			 * mitgeschickt.
			 */
			editBtn.addClickHandler(new ClickHandler() {
				public void onClick(ClickEvent event) {

					if (editStueckliste == null) {
						Window.alert("Bitte wählen Sie eine Stückliste zum editieren aus.");
					} else {
						RootPanel.get("content_wrap").clear();
						RootPanel.get("content_wrap").add(new EditStueckliste(editStueckliste));
					}

				}

			});
			
			/**
			 * ClickHandler für den Aufruf der Klasse treeView. Als Attribut
			 * wird das Stücklisten-Objekt aus der entsprechenden Tabellen-Reihe
			 * mitgeschickt.
			 */
			treeViewBtn.addClickHandler(new ClickHandler() {
				public void onClick(ClickEvent event) {

					if (treeViewStueckliste == null) {
						Window.alert("Bitte wählen Sie eine Stückliste zum anzeigen aus.");
					} else {
						RootPanel.get("content_wrap").clear();
						RootPanel.get("content_wrap").add(new TreeView(treeViewStueckliste));
					}

				}

			});

		}

	}

	/**
	 * Hiermit wird die RPC-Methode aufgerufen, die ein Stuecklisten-Objekt löscht.
	 * 
	 * @author Mario Alex
	 * 
	 */
	private class deleteClickHandler implements ClickHandler {
		@Override
		public void onClick(ClickEvent event) {

			if (deleteStuecklisten.isEmpty() == true) {
				Window.alert("Es wurde keine Stückliste zum Löschen ausgewählt.");
			}

			else {
				for (int i = 0; i <= deleteStuecklisten.size(); i++) {
					Stueckliste s = new Stueckliste();
					s = deleteStuecklisten.get(i);
					/**
					 * Die konkrete RPC-Methode für den create-Befehl wird
					 * aufgerufen. Hierbei werden die gewünschten Werte
					 * mitgeschickt.
					 */
					stuecklistenVerwaltung.deleteStueckliste(s, new DeleteStuecklisteCallback());
					RootPanel.get("content_wrap").clear();
					RootPanel.get("content_wrap").add(new StuecklisteGeneralView());
				}
			}
		}
	}

	/**
	 * Hiermit wird sichergestellt, dass beim (nicht) erfolgreichen
	 * Delete-Befehl eine entsprechende Hinweismeldung ausgegeben wird.
	 * 
	 * @author Mario Alex
	 *
	 */
	class DeleteStuecklisteCallback implements AsyncCallback<Void> {

		@Override
		public void onFailure(Throwable caught) {
			Window.alert("Das Löschen der Stueckliste ist fehlgeschlagen!");
		}

		@Override
		public void onSuccess(Void result) {

			Window.alert("Die Stueckliste wurde erfolgreich gelöscht.");
		}
	}

}