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
import de.hdm.gruppe1.shared.bo.Bauteil;
import de.hdm.gruppe1.shared.bo.Baugruppe;
import de.hdm.gruppe1.shared.bo.Stueckliste;

/*
 * Die Klasse StuecklisteGeneralView liefert eine Übersicht mit allen vorhandenen Stücklisten im System
 * und bietet Möglichkeiten, diese zu editieren oder löschen.
 */
public class BaugruppeGeneralView extends VerticalPanel {

	//Elemente für Stücklisten initialisieren
	private final Label HeadlineLabel = new Label ("Baugruppennübersicht");
	
	//Buttons sollen nebeneinander angezeigt werden, nicht vertikal. Daher wird ein "vertikales Zwischen-Panel" benötigt
	private HorizontalPanel editButtonPanel = new HorizontalPanel();
	private HorizontalPanel deleteButtonPanel = new HorizontalPanel();
	
	//Den Buttons wird jeweils ein erklärender Text hinzugefügt
	private final Label editLabel = new Label("Wählen Sie in der Übersicht eine Baugruppe aus, um sie mithilfe dieses Buttons zu editieren: ");
	private final Label deleteLabel = new Label("Wählen Sie in der Übersicht mindestens eine Baugruppe aus, um sie mithilfe dieses Buttons zu löschen: ");
		
	//Neu: Single-Button Editieren
	private final Button editBtn = new Button("");
	//Neu: Single-Button Löschen
	private final Button deleteBtn = new Button("");
	
	private final FlexTable table = new FlexTable();
	
	//Baugruppe, die editiert werden soll
	Baugruppe editBaugruppe = null;
		
	//TODO implementieren
	//Vektor wird mit allen Bauteilen aus der DB befüllt
	Vector<Baugruppe> allBaugruppen = new Vector<Baugruppe>();
	
	//Vektor wird temporär mit zu löschenden Stücklisten befüllt, wenn CheckBoxen aus- bzw. abgewählt werden
	Vector<Baugruppe> deleteBaugruppen = new Vector<Baugruppe>();
		
	// Remote Service via ClientsideSettings
	SmsAsync stuecklistenVerwaltung = ClientsideSettings.getSmsVerwaltung();
	
	public BaugruppeGeneralView() {
		
		//Damit die edit und delete Buttons horizontal angeordnet werden, müssen diese dem ButtonPanel zugeordnet werden
		editButtonPanel.add(editLabel);
		editButtonPanel.add(editBtn);
				
		deleteButtonPanel.add(deleteLabel);
		deleteButtonPanel.add(deleteBtn);
				
		editBtn.setStyleName("editButton");
		deleteBtn.setStyleName("deleteButton");
		HeadlineLabel.setStyleName("headline");
		table.setStyleName("tableBody");
			
		stuecklistenVerwaltung.getAllBaugruppen(new GetAllBaugruppenCallback());

		//Die erste Reihe der Tabelle wird mit Überschriften vordefiniert
		table.setText(0, 0, "ID");
		table.setText(0, 1, "Name");
		table.setText(0, 2, "Erstellungsdatum");
		table.setText(0, 3, "Letzter Änderer");
		table.setText(0, 4, "Letztes Änderungsdatum");
		table.setText(0, 5, "Editieren");
		table.setText(0, 6, "Löschen");
			    
		//Das FlexTable Widget unterstützt keine Headlines. Daher wird die erste Reihe über folgenden Umweg formatiert
		table.getCellFormatter().addStyleName(0, 0, "tableHead");
		table.getCellFormatter().addStyleName(0, 1, "tableHead");
		table.getCellFormatter().addStyleName(0, 2, "tableHead");
		table.getCellFormatter().addStyleName(0, 3, "tableHead");
		table.getCellFormatter().addStyleName(0, 4, "tableHead");
		table.getCellFormatter().addStyleName(0, 5, "tableHead");
		table.getCellFormatter().addStyleName(0, 6, "tableHead");
			    
		this.add(HeadlineLabel);
		this.add(editButtonPanel);
		this.add(deleteButtonPanel);
		this.add(table);
			    
		RootPanel.get("content_wrap").add(this);
		
	}
	
	/*
	 * Click Handlers.
	 */
	
	 class GetAllBaugruppenCallback implements AsyncCallback<Vector<Baugruppe>> {

			@Override
			public void onFailure(Throwable caught) {
				Window.alert("Baugruppe konnten nicht geladen werden");
			}

			@Override
			public void onSuccess(Vector<Baugruppe> result) {

				allBaugruppen = result;
				
				if (allBaugruppen.isEmpty() == true){
					
					table.getFlexCellFormatter().setColSpan(1, 0, 6);

					table.setWidget(1, 0, new Label("Es sind leider keine Daten in der Datenbank vorhanden."));
					
				}
				
				else {
				
				for (int row = 1; row <= allBaugruppen.size(); row++) {
						//for (int col = 0; col < numColumns; col++) {

				    	//Da die erste Reihe der Tabelle als Überschriften der Spalten dient, wird eine neue Variable benötigt,
				    	//die den Index 0 des Vectors auslesen kann.
				    	final int i = row - 1;
				    	
				        RadioButton radioButton = new RadioButton("editRadioGroup", "");
				        CheckBox checkBox = new CheckBox("");

				        deleteBtn.addClickHandler(new deleteClickHandler());
				    	
				        //Pro Vektor-Index wird eine Reihe in die Tabelle geschrieben
				        table.setText(row, 0, ""+allBaugruppen.get(i).getId());
				        table.setText(row, 1, allBaugruppen.get(i).getName());
				        table.setText(row, 2, "01.01.2015, 11 Uhr");
				        table.setText(row, 2, allBaugruppen.get(i).getEditUser().getName());
						table.setText(row, 3, allBaugruppen.get(i).getEditDate().toString());
				        
				        //RadioButton Widget für Single editieren-Button
				        table.setWidget(row, 4, radioButton);
				        
				        //Pro Reihe wird dem radioButton ein ValueChangeHandler hinzugefügt
						radioButton.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
				            @Override
				            public void onValueChange(ValueChangeEvent<Boolean> e) {
				                if(e.getValue() == true)
				                {
				                    editBaugruppe = allBaugruppen.get(i);
				                }
				            }
				        }); 
				        
				        table.setWidget(row, 5, checkBox);
				        
						checkBox.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
							@Override
							public void onValueChange(ValueChangeEvent<Boolean> e) {
								if (e.getValue() == true) {
								Baugruppe deleteBaugruppe = allBaugruppen.get(i);
									deleteBaugruppen.add(deleteBaugruppe);
								} else if (e.getValue() == false) {
									Baugruppe removeBaugruppen = allBaugruppen.get(i);
									deleteBaugruppen.remove(removeBaugruppen);
								}
							}
						});

				        
				        table.setStyleName("tableBody");
				        
				    }
				
				}
				
				//ClickHandler für Aufruf der Klasse editBauteil
		        editBtn.addClickHandler(new ClickHandler() {
					public void onClick(ClickEvent event) {
						
						if(editBaugruppe==null){
							Window.alert("Bitte wählen Sie eine Baugruppe zum editieren aus.");
						} else {
							Window.alert("Name editBaugruppe: "+ editBaugruppe.getName()
									+ " Vektor Bauteile: "+ editBaugruppe.getStueckliste().getBauteilPaare().capacity()
									+ " Vektor Baugruppen: "+ editBaugruppe.getStueckliste().getBaugruppenPaare().capacity());
							
							RootPanel.get("content_wrap").clear();
							RootPanel.get("content_wrap").add(new EditBaugruppe(editBaugruppe));
						}

					}

				});

			}

		

		/**
		 * Hiermit wird die RPC-Methode aufgerufen, die ein Baugruppen-Objekt löscht
		 * 
		 * @author Mario Alex
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

		class DeleteBaugruppeCallback implements AsyncCallback<Void> {

			@Override
			public void onFailure(Throwable caught) {
				Window.alert("Das Löschen der Baugruppe ist fehlgeschlagen!");
			}

			@Override
			public void onSuccess(Void result) {

				Window.alert("Die Baugruppe wurde erfolgreich gelöscht.");
			}
		}

	

	
			}

		}
	 /**
		 * Hiermit wird die RPC-Methode aufgerufen, die ein Bauteil-Objekt löscht
		 * 
		 * @author Mario Alex
		 * 
		 */
