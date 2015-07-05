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
import de.hdm.gruppe1.shared.bo.Baugruppe;

/**
 * Die Klasse BaugruppeGeneralView liefert eine Übersicht mit allen vorhandenen
 * Baugruppen im System. Baugruppen können aus der Übersicht ausgewählt werden, um sie zu editieren oder
 * zu löschen.
 * 
 * @author Katja Thiere, Mario Thieler
 * @version 1.0
 */
public class BaugruppeGeneralView extends VerticalPanel {

	/**
	 * Überschrift, um dem User eine Orientierung zu geben, in welchem Bereich
	 * der Applikation er sich befindet.
	 */
	private final Label HeadlineLabel = new Label("Baugruppenübersicht");

	/**
	 * Einige GUI-Elemente sollen nebeneinander angezeigt werden, nicht vertikal. Daher wird
	 * ein "horizontales Zwischen-Panel" benötigt.
	 */
	private HorizontalPanel editButtonPanel = new HorizontalPanel();
	private HorizontalPanel deleteButtonPanel = new HorizontalPanel();

	/**
	 * Erzeugen von Labels, um eine textuelle Beschreibung der dazugehörigen Buttons mitzuliefern
	 */
	private final Label editLabel = new Label(
			"Markierte Baugruppe editieren ");
	private final Label deleteLabel = new Label(
			"Markierte Baugruppe(n) löschen ");

	/**
	 * Die RadioButtons und CheckBoxen erhalten jeweils einen globalen edit-
	 * bzw. delete-Button. 
	 */
	private final Button editBtn = new Button("");
	private final Button deleteBtn = new Button("");

	/**
	 * Tabelle, in der jegliche Baugruppen inkl. edit- & delete-Buttons angezeigt
	 * werden.
	 */
	private final FlexTable table = new FlexTable();

	/**
	 * Baugruppe, welche editiert werden soll.
	 */
	Baugruppe editBaugruppe = null;

	/**
	 * Vektor, der mit allen Baugruppen aus der DB befüllt wird.
	 */
	Vector<Baugruppe> allBaugruppen = new Vector<Baugruppe>();

	/**
	 * Vektor, der alle zu löschenden Baugruppen zwischenspeichert. Dies erfolgt mithilfe von Aus- bzw. Abwählen
	 * der CheckBoxen. Im Anschluss werden diese Baugruppen nacheinander aus der DB gelöscht.
	 */
	Vector<Baugruppe> deleteBaugruppen = new Vector<Baugruppe>();

	/**
	 * Remote Service via ClientsideSettings wird an dieser Stelle einmalig in
	 * der Klasse aufgerufen. Im Anschluss kann jederzeit darauf zugegriffen
	 * werden.
	 */
	SmsAsync stuecklistenVerwaltung = ClientsideSettings.getSmsVerwaltung();

	public BaugruppeGeneralView() {

		/**
		 * Damit die edit und delete Buttons horizontal angeordnet werden,
		 * müssen diese einem separaten horizontalen Panel zugeordnet werden.
		 */
		
		editButtonPanel.add(editLabel);
		editButtonPanel.add(editBtn);
		deleteButtonPanel.add(deleteLabel);
		deleteButtonPanel.add(deleteBtn);

		/**
		 * Diverse css-Formatierungen
		 */
		
		editBtn.setStyleName("editButton");
		deleteBtn.setStyleName("deleteButton");
		HeadlineLabel.setStyleName("headline");
		table.setStyleName("tableBody");

		/**
		 * RPC-Methode ausführen, die alle Baugruppen-Objekte aus der Datenbank in
		 * einem Vektor zurückliefert. Dadurch wird der Klassen-Vektor
		 * "allBaugruppen" befüllt.
		 */
		
		stuecklistenVerwaltung.getAllBaugruppen(new GetAllBaugruppenCallback());

		/**
		 * Die erste Reihe der Tabelle wird mit Überschriften vordefiniert.
		 */
		
		table.setText(0, 0, "ID");
		table.setText(0, 1, "Name");
		table.setText(0, 2, "Letzter Änderer");
		table.setText(0, 3, "Letztes Änderungsdatum");
		table.setText(0, 4, "Editieren");
		table.setText(0, 5, "Löschen");

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
	 * Click Handlers.
	 */

	/**
	 * Hiermit wird die RPC-Methode aufgerufen, die einen Vektor von allen in
	 * der DB vorhandenen Baugruppen liefert. Die Klasse ist eine nested-class
	 * und erlaubt daher, auf die Attribute der übergeordneten Klasse
	 * zuzugreifen.
	 * 
	 */
	class GetAllBaugruppenCallback implements AsyncCallback<Vector<Baugruppe>> {

		@Override
		public void onFailure(Throwable caught) {
			Window.alert("Baugruppen konnten nicht geladen werden.");
		}

		@Override
		public void onSuccess(Vector<Baugruppe> alleBaugruppen) {

			/**
			 * Der Baugruppen-Vektor allBaugruppen wird mit dem Ergebnis dieses RPC´s
			 * befüllt.
			 */
			allBaugruppen = alleBaugruppen;

			/**
			 * Abfangen eines leeren RPC-Vektors mithilfe eines Labels, das sich
			 * über die komplette Reihe erstreckt.
			 */
			if (allBaugruppen.isEmpty() == true) {

				table.getFlexCellFormatter().setColSpan(1, 0, 5);

				table.setWidget(1, 0, new Label("Es sind leider keine Daten in der Datenbank vorhanden."));

			}

			else {

				/**
				 * Die flexTable table wird mithilfe dieser for-Schleife Reihe
				 * um Reihe für jede Baugruppe befüllt.
				 */
				for (int row = 1; row <= allBaugruppen.size(); row++) {
					
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

					/**
					 * Dem Delete-Button wird der am Ende dieser Klasse erstellte deleteClickHandler zugeordnet.
					 */
					deleteBtn.addClickHandler(new deleteClickHandler());

					/**
					 * Pro Vektor-Index wird eine Reihe in die Tabelle
					 * geschrieben.
					 */
					table.setText(row, 0, "" + allBaugruppen.get(i).getId());
					table.setText(row, 1, allBaugruppen.get(i).getName());
					table.setText(row, 2, allBaugruppen.get(i).getEditUser().getName());
					table.setText(row, 3, allBaugruppen.get(i).getEditDate().toString().substring(0, 19));
					
					/**
					 * An dieser Stelle wird pro Schleifendurchlauf ein
					 * RadioButton Widget hinzugefügt. Mithilfe der Eigenschaft
					 * von "RadioGroup" kann jeweils nur ein RadioButton, nach
					 * vollständigem Befüllen der Tabelle, ausgewählt werden.
					 */
					table.setWidget(row, 4, radioButton);

					/**
					 * Dieser RadioButton wird pro Reihe mit einem
					 * ValueChangeHandler erweitert. Dieser erkennt, welcher
					 * RadioButton ausgewählt ist und befüllt das Klassen-Objekt
					 * editBaugruppe von "Baugruppe" mit dem Objekt der
					 * entsprechenden Tabellen-Reihe.
					 */
					radioButton.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
								@Override
								public void onValueChange(ValueChangeEvent<Boolean> e) {
									if (e.getValue() == true) {
										editBaugruppe = allBaugruppen.get(i);
									}
								}
							});

					/**
					 * An dieser Stelle wird pro Schleifendurchlauf ein CheckBox
					 * Widget hinzugefügt.
					 */
					table.setWidget(row, 5, checkBox);

					/**
					 * Basierende darauf, ob der ValueChangeHandler eine Aus- bzw. Abwahl der betroffenen
					 * CheckBox erkennt, wird dem globalen deleteBauteile-Vektor ein Element hinzugefügt
					 * bzw. daraus entfernt.
					 */
					checkBox.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
						@Override
						public void onValueChange(ValueChangeEvent<Boolean> e) {
							if (e.getValue() == true) {
								Baugruppe deleteBaugruppe = allBaugruppen.get(i);
								deleteBaugruppen.add(deleteBaugruppe);
							} else if (e.getValue() == false) {
								Baugruppe removeBaugruppe = allBaugruppen.get(i);
								deleteBaugruppen.remove(removeBaugruppe);
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
			 * ClickHandler für den Aufruf der Klasse editBaugruppe. Als Attribut
			 * wird das Baugruppen-Objekt aus der entsprechenden Tabellen-Reihe
			 * mitgeschickt.
			 */
			editBtn.addClickHandler(new ClickHandler() {
				public void onClick(ClickEvent event) {

					if (editBaugruppe == null) {
						Window.alert("Bitte wählen Sie eine Baugruppe zum editieren aus.");
					} else {
						RootPanel.get("content_wrap").clear();
						RootPanel.get("content_wrap").add(new EditBaugruppe(editBaugruppe));
					}

				}

			});

		}

	}

	/**
	 * Hiermit wird die RPC-Methode aufgerufen, die ein Baugruppen-Objekt löscht.
	 * 
	 */
	private class deleteClickHandler implements ClickHandler {
		@Override
		public void onClick(ClickEvent event) {

			if (deleteBaugruppen.isEmpty() == true) {
				Window.alert("Es wurde keine Baugruppe zum Löschen ausgewählt.");
			}

			else {
				for (int i = 0; i <= deleteBaugruppen.size(); i++) {
					Baugruppe b = new Baugruppe();
					b = deleteBaugruppen.get(i);
					/**
					 * Die konkrete RPC-Methode für den create-Befehl wird
					 * aufgerufen. Hierbei werden die gewünschten Werte
					 * mitgeschickt.
					 */
					stuecklistenVerwaltung.deleteBaugruppe(b, new DeleteBaugruppeCallback());
					RootPanel.get("content_wrap").clear();
					RootPanel.get("content_wrap").add(new BaugruppeGeneralView());
				}
			}
		}
	}

	/**
	 * Hiermit wird sichergestellt, dass beim (nicht) erfolgreichen
	 * Delete-Befehl eine entsprechende Hinweismeldung ausgegeben wird.
	 *
	 */
	class DeleteBaugruppeCallback implements AsyncCallback<Void> {

		@Override
		public void onFailure(Throwable caught) {
			Window.alert("Das Löschen der Baugruppe ist fehlgeschlagen!");
		}

		@Override
		public void onSuccess(Void result) {

			Window.alert("Die Baugruppe wurde erfolgreich gelöscht!");
		}
	}

}