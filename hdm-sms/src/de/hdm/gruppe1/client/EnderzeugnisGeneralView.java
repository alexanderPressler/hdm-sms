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
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

import de.hdm.gruppe1.client.BauteilGeneralView.DeleteBauteilCallback;
import de.hdm.gruppe1.client.BauteilGeneralView.GetAllBauteileCallback;
import de.hdm.gruppe1.shared.SmsAsync;
import de.hdm.gruppe1.shared.bo.Baugruppe;
import de.hdm.gruppe1.shared.bo.Bauteil;
import de.hdm.gruppe1.shared.bo.Enderzeugnis;
import de.hdm.gruppe1.shared.bo.Stueckliste;

/*
 * Die Klasse EnderzeugnisGeneralView liefert eine Übersicht mit allen vorhandenen Enderzeugnissen im System
 * und bietet Möglichkeiten, diese zu editieren oder löschen.
 */
public class EnderzeugnisGeneralView extends VerticalPanel {

	// Elemente für Enderzeugnis initialisieren
	private final Label HeadlineLabel = new Label("Enderzeugnisübersicht");

	// Buttons sollen nebeneinander angezeigt werden, nicht vertikal. Daher wird
	// ein "vertikales Zwischen-Panel" benötigt
	private HorizontalPanel editButtonPanel = new HorizontalPanel();
	private HorizontalPanel deleteButtonPanel = new HorizontalPanel();

	// Den Buttons wird jeweils ein erklärender Text hinzugefügt
	private final Label editLabel = new Label(
			"Wählen Sie in der Übersicht ein Enderzeugnis aus, um sie mithilfe dieses Buttons zu editieren: ");
	private final Label deleteLabel = new Label(
			"Wählen Sie in der Übersicht mindestens ein Enderzeugnis aus, um sie mithilfe dieses Buttons zu löschen: ");

	// Neu: Single-Button Editieren
	private final Button editBtn = new Button("");
	// Neu: Single-Button Löschen
	private final Button deleteBtn = new Button("");

	private final FlexTable table = new FlexTable();

	// Enderzeugnis, das editiert werden soll
	Enderzeugnis editEnderzeugnis = null;

	// Vektor wird mit allen Enderzeugnissen aus der DB befüllt
	Vector<Enderzeugnis> allEnderzeugnisse = new Vector<Enderzeugnis>();

	// Vektor wird temporär mit zu löschenden Enderzeugnissen befüllt, wenn
	// CheckBoxen aus- bzw. abgewählt werden
	Vector<Enderzeugnis> deleteEnderzeugnisse = new Vector<Enderzeugnis>();

	// Remote Service via ClientsideSettings
	SmsAsync stuecklistenVerwaltung = ClientsideSettings.getSmsVerwaltung();

	public EnderzeugnisGeneralView() {

		// Damit die edit und delete Buttons horizontal angeordnet werden,
		// müssen diese dem ButtonPanel zugeordnet werden
		editButtonPanel.add(editLabel);
		editButtonPanel.add(editBtn);

		deleteButtonPanel.add(deleteLabel);
		deleteButtonPanel.add(deleteBtn);

		editBtn.setStyleName("editButton");
		deleteBtn.setStyleName("deleteButton");
		HeadlineLabel.setStyleName("headline");
		table.setStyleName("tableBody");

		stuecklistenVerwaltung.getAllEnderzeugnis(new GetAllEnderzeugnisseCallback());

		// Die erste Reihe der Tabelle wird mit Überschriften vordefiniert
		table.setText(0, 0, "ID");
		table.setText(0, 1, "Name");
		table.setText(0, 2, "Letzter Änderer");
		table.setText(0, 3, "Letztes Änderungsdatum");
		table.setText(0, 4, "Editieren");
		table.setText(0, 5, "Löschen");

		// Das FlexTable Widget unterstützt keine Headlines. Daher wird die
		// erste Reihe über folgenden Umweg formatiert
		table.getCellFormatter().addStyleName(0, 0, "tableHead");
		table.getCellFormatter().addStyleName(0, 1, "tableHead");
		table.getCellFormatter().addStyleName(0, 2, "tableHead");
		table.getCellFormatter().addStyleName(0, 3, "tableHead");
		table.getCellFormatter().addStyleName(0, 4, "tableHead");
		table.getCellFormatter().addStyleName(0, 5, "tableHead");

		this.add(HeadlineLabel);
		this.add(editButtonPanel);
		this.add(deleteButtonPanel);
		this.add(table);

		RootPanel.get("content_wrap").add(this);

	}

	/*
	 * Click Handlers.
	 */

	class GetAllEnderzeugnisseCallback implements
			AsyncCallback<Vector<Enderzeugnis>> {

		@Override
		public void onFailure(Throwable caught) {
			Window.alert("Enderzeugnisse konnten nicht geladen werden");
		}

		@Override
		public void onSuccess(Vector<Enderzeugnis> result) {

			allEnderzeugnisse = result;

			if (allEnderzeugnisse.isEmpty() == true) {

				table.getFlexCellFormatter().setColSpan(1, 0, 6);

				table.setWidget(1, 0, new Label("Es sind leider keine Daten in der Datenbank vorhanden."));

			}

			else {

				for (int row = 1; row <= allEnderzeugnisse.size(); row++) {
					// for (int col = 0; col < numColumns; col++) {

					// Da die erste Reihe der Tabelle als Überschriften der
					// Spalten dient, wird eine neue Variable benötigt,
					// die den Index 0 des Vectors auslesen kann.
					final int i = row - 1;

					RadioButton radioButton = new RadioButton("editRadioGroup",
							"");
					CheckBox checkBox = new CheckBox("");

					deleteBtn.addClickHandler(new deleteClickHandler());

					// Pro Vektor-Index wird eine Reihe in die Tabelle
					// geschrieben
					table.setText(row, 0, "" + allEnderzeugnisse.get(i).getId());
					table.setText(row, 1, allEnderzeugnisse.get(i).getName());
					table.setText(row, 2, allEnderzeugnisse.get(i).getEditUser().getName());
					table.setText(row, 3, allEnderzeugnisse.get(i).getEditDate().toString());
					
					// RadioButton Widget für Single editieren-Button
					table.setWidget(row, 4, radioButton);

					// Pro Reihe wird dem radioButton ein ValueChangeHandler
					// hinzugefügt
					radioButton.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
								@Override
								public void onValueChange(ValueChangeEvent<Boolean> e) {
									if (e.getValue() == true) {
										editEnderzeugnis = allEnderzeugnisse.get(i);
									}
								}
							});

					table.setWidget(row, 5, checkBox);

					checkBox.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
						@Override
						public void onValueChange(ValueChangeEvent<Boolean> e) {
							if (e.getValue() == true) {
								Enderzeugnis deleteEnderzeugnis = allEnderzeugnisse.get(i);
								deleteEnderzeugnisse.add(deleteEnderzeugnis);
							} else if (e.getValue() == false) {
								Enderzeugnis removeEnderzeugnis = allEnderzeugnisse.get(i);
								deleteEnderzeugnisse.remove(removeEnderzeugnis);
							}
						}
					});

					table.setStyleName("tableBody");

				}

			}

			// ClickHandler für Aufruf der Klasse editEnderzeugnis
			editBtn.addClickHandler(new ClickHandler() {
				public void onClick(ClickEvent event) {

					if (editEnderzeugnis == null) {
						Window.alert("Bitte wählen Sie ein Enderzeugnis zum editieren aus.");
					} else {
						
						Window.alert("Name editEnderzeugnis: "+editEnderzeugnis.getName()+ " Vektor Bauteile: "+ editEnderzeugnis.getBaugruppe().getStueckliste().getBauteilPaare().capacity()+ " Vektor Baugruppen: "+editEnderzeugnis.getBaugruppe().getStueckliste().getBaugruppenPaare().capacity());
						
						RootPanel.get("content_wrap").clear();
						RootPanel.get("content_wrap").add(new EditStueckliste(editEnderzeugnis.getBaugruppe().getStueckliste()));
					}

				}

			});

		}

	}

	/**
	 * Hiermit wird die RPC-Methode aufgerufen, die ein Enderzeugnis-Objekt löscht
	 * 
	 * @author Mario Alex
	 * 
	 */
	private class deleteClickHandler implements ClickHandler {
		@Override
		public void onClick(ClickEvent event) {

			if (deleteEnderzeugnisse.isEmpty() == true) {
				Window.alert("Es wurde kein Enderzeugnis zum Löschen ausgewählt.");
			}

			else {
				for (int i = 0; i <= deleteEnderzeugnisse.size(); i++) {
					Enderzeugnis e = new Enderzeugnis();
					e = deleteEnderzeugnisse.get(i);
					/**
					 * Die konkrete RPC-Methode für den create-Befehl wird
					 * aufgerufen. Hierbei werden die gewünschten Werte
					 * mitgeschickt.
					 */
					stuecklistenVerwaltung.deleteEnderzeugnis(e, new DeleteEnderzeugnisCallback());
					RootPanel.get("content_wrap").clear();
					RootPanel.get("content_wrap").add(new EnderzeugnisGeneralView());
				}
			}
		}
	}

	class DeleteEnderzeugnisCallback implements AsyncCallback<Void> {

		@Override
		public void onFailure(Throwable caught) {
			Window.alert("Das Löschen des Enderzeugnisses ist fehlgeschlagen!");
		}

		@Override
		public void onSuccess(Void result) {

			Window.alert("Das Enderzeugnis wurde erfolgreich gelöscht.");
		}
	}

}