package de.hdm.gruppe1.client;


import java.util.Vector;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
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

/*
 * Die Klasse BaugruppeGeneralView liefert eine Übersicht mit allen vorhandenen Baugruppen im System
 * und bietet Möglichkeiten, diese anzulegen, zu editieren oder zu löschen.
 */

public class BaugruppeGeneralView extends VerticalPanel {

	//Elemente für Baugruppe initialisieren
	private final Label HeadlineLabel = new Label ("Baugruppeübersicht");
	private final Label SublineLabel = new Label ("In dieser Übersicht sehen Sie alle im System vorhandenen Baugruppen. Um diese zu editieren oder löschen, klicken Sie in der Tabelle auf den entsprechenden Button. Um ein neues Baugruppe anzulegen, klicken Sie auf den <Neues Bauteil>-Button.");
	
	//Buttons sollen nebeneinander angezeigt werden, nicht vertikal. Daher wird ein "vertikales Zwischen-Panel" benötigt
	private HorizontalPanel editButtonPanel = new HorizontalPanel();
	private HorizontalPanel deleteButtonPanel = new HorizontalPanel();
	
	//Den Buttons wird jeweils ein erklärender Text hinzugefügt
	private final Label editLabel = new Label("Wählen Sie in der Übersicht eine Baugruppe aus, um es mithilfe dieses Buttons zu editieren: ");
	private final Label deleteLabel = new Label("Wählen Sie in der Übersicht mindestens eine Baugruppe aus, um es mithilfe dieses Buttons zu löschen: ");
	
	private final Button NewBaugruppeButton = new Button ("Neue Baugruppe");
	
	private final Button editBtn = new Button("");
	private final Button deleteBtn = new Button("");
	
	private final FlexTable table = new FlexTable();
	
	// Applikationsschicht liefert <Baugruppe>-Vector.
	// Diesen mithilfe for-Schleife durchlaufen und angemessen darstellen.
	
	Vector<Baugruppe> allBaugruppen = new Vector<Baugruppe>();
	
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
//		SublineLabel.setStyleName("subline");
		table.setStyleName("BaugruppeTable");	
		

		stuecklistenVerwaltung.getAllBaugruppen(new GetAllBaugruppenCallback());

		//Die erste Reihe der Tabelle wird mit Überschriften vordefiniert
	    table.setText(0, 0, "ID");
	    table.setText(0, 1, "Name");
	    table.setText(0, 2, "Letzter Änderer");
	    table.setText(0, 3, "Letztes Änderungsdatum");
	    table.setText(0, 4, "Editieren");
	    table.setText(0, 5, "Löschen");
	    
	    //Das FlexTable Widget unterstützt keine Headlines. Daher wird die erste Reihe über folgenden Umweg formatiert
	    table.getCellFormatter().addStyleName(0, 0, "tableHead");
	    table.getCellFormatter().addStyleName(0, 1, "tableHead");
	    table.getCellFormatter().addStyleName(0, 2, "tableHead");
	    table.getCellFormatter().addStyleName(0, 3, "tableHead");
	    table.getCellFormatter().addStyleName(0, 4, "tableHead");
	    table.getCellFormatter().addStyleName(0, 5, "tableHead");
	    table.getCellFormatter().addStyleName(0, 6, "tableHead");
	    table.getCellFormatter().addStyleName(0, 7, "tableHead");
	    
	    
		this.add(HeadlineLabel);
	//	this.add(SublineLabel);
		this.add(editButtonPanel);
		this.add(deleteButtonPanel);
		this.add(table);
	    
		RootPanel.get("content_wrap").add(this);
		
		
	}
	
	
	/*
	 * Click Handlers.
	 */
	
	/**
	  * Das Löschen einer Baugruppe wird mithilfe der mitgelieferten Objekt-ID über die Applikationsschicht
	  * an den Server geschickt. Der User erhält eine entsprechende Hinweismeldung angezeigt und die
	  * Tabelle wird neu geladen.
	  * 
	  */
	 private class DeleteClickHandler implements ClickHandler {
	  @Override
	  public void onClick(ClickEvent event) {
//	   if (customerToDisplay != null) {

			RootPanel.get("content_wrap").clear();
			Window.alert("Baugruppe wurde vielleicht gelöscht");
			RootPanel.get("content_wrap").add(new BauteilGeneralView());
		   
//	   } else {
//	    Window.alert("kein Kunde ausgewählt");
//	   }
	  }
	 }
	 		
		class GetAllBaugruppenCallback implements AsyncCallback<Vector<Baugruppe>> {

			@Override
			public void onFailure(Throwable caught) {
				Window.alert("Baugruppe konnten nicht geladen werden");
			}

			@Override
			public void onSuccess(Vector<Baugruppe> alleBaugruppen) {

//				Window.alert("Inhalt allBaugruppe: "+allBaugruppe);
				allBaugruppen = alleBaugruppen;
//				System.out.println("Inhalt allBaugruppe: "+allBaugruppe);
				
				for (int row = 1; row <= allBaugruppen.size(); row++) {
//				      for (int col = 0; col < numColumns; col++) {

				    	//Da die erste Reihe der Tabelle als Überschriften der Spalten dient, wird eine neue Variable benötigt,
				    	//die den Index 0 des Vectors auslesen kann.
				    	int i = row-1;
				    	
//				        Button deleteBtn = new Button("");
				        RadioButton radioButton = new RadioButton("editRadioGroup", "");
				        CheckBox checkBox = new CheckBox("");

				        
//				        editBtn.addClickHandler(new ClickHandler(){
//							public void onClick(ClickEvent event) {
//								RootPanel.get("content_wrap").clear();
//								RootPanel.get("content_wrap").add(new EditBaugruppe());
//							    }
//
//						});
				        
				        deleteBtn.addClickHandler(new DeleteClickHandler());
				    	
				        //Pro Vektor-Index wird eine Reihe in die Tabelle geschrieben
				        table.setText(row, 0, ""+allBaugruppen.get(i).getId());
				        table.setText(row, 1, allBaugruppen.get(i).getName());
				        table.setText(row, 4, "Katja");
				        table.setText(row, 5, "07.05.2015, 21 Uhr");
//				        table.setWidget(row, 6, editBtn);
				        
				        //RadioButton Widget für Single editieren-Button
				        table.setWidget(row, 6, radioButton);
				        
				        table.setWidget(row, 7, checkBox);
				        
				        table.setStyleName("BaugruppeTable");
				        
				        editBtn.addClickHandler(new ClickHandler(){
							public void onClick(ClickEvent event) {
								RootPanel.get("content_wrap").clear();
								RootPanel.get("content_wrap").add(new EditBauteil());
							    }

						});
				    	  
//				      }
				    }
				
				
				//TODO: siehe Text
				//Methode implementieren, die prüft, welcher RadioButton ausgewählt ist und die Id des Bauteil-Objektes
				//per RPC an die Datenbank schickt. Daraufhin wird das Bauteil-Objekt an die EditBauteil-Klasse geschickt.

				
				
				//TODO: Klären ob das catvm gebraucht wird 
				// if (bauteil != null) {
				// Das erfolgreiche Hinzufügen eines Kunden wird an den
				// Kunden- und
				// Kontenbaum propagiert.
				// catvm.addCustomer(customer);
				// }
			}
		}

		
}
	


