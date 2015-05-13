package de.hdm.gruppe1.client;

import java.util.Vector;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import de.hdm.gruppe1.shared.SmsAsync;
import de.hdm.gruppe1.shared.bo.Bauteil;

/*
 * Die Klasse BauteilGeneralView liefert eine Übersicht mit allen vorhandenen Bauteilen im System
 * und bietet Möglichkeiten, diese anzulegen, zu editieren oder zu löschen.
 */
public class BauteilGeneralView extends VerticalPanel {

	//Elemente für Bauteile initialisieren
	private final Label HeadlineLabel = new Label ("Bauteilübersicht");
	private final Label SublineLabel = new Label ("In dieser Übersicht sehen Sie alle im System vorhandenen Bauteile. Um diese zu editieren oder löschen, klicken Sie in der Tabelle auf den entsprechenden Button. Um ein neues Bauteil anzulegen, klicken Sie auf den <Neues Bauteil>-Button.");
	private final Button NewBauteilButton = new Button ("Neues Bauteil");
	private final Label OverviewTableLabel = new Label ("Diese Tabelle enthält eine Übersicht über alle Bauteile im System");
	private final FlexTable table = new FlexTable();
	
	Vector<Bauteil> allBauteile = new Vector<Bauteil>();
	
	// Remote Service via ClientsideSettings
	SmsAsync stuecklistenVerwaltung = ClientsideSettings.getSmsVerwaltung();
	
	public BauteilGeneralView() {

//		this.add(HeadlineLabel);
//		this.add(SublineLabel);
//		this.add(NewBauteilButton);
//		this.add(OverviewTableLabel);
//		this.add(table);
		
		HeadlineLabel.setStyleName("headline");
		SublineLabel.setStyleName("subline");
		NewBauteilButton.setStyleName("Button");
		OverviewTableLabel.setStyleName("subline");
		table.setStyleName("BauteilTable");
		
		NewBauteilButton.addClickHandler(new ClickHandler(){
			public void onClick(ClickEvent event) {
				RootPanel.get("content_wrap").clear();
				RootPanel.get("content_wrap").add(new CreateBauteil());
			    }

		});
		
		//Applikationsschicht liefert <Bauteil>-Vector.
		//Diesen mithilfe for-Schleife durchlaufen und angemessen darstellen.

		stuecklistenVerwaltung.getAllBauteile(new GetAllBauteileCallback());

	    table.setText(0, 0, "ID");
	    table.setText(0, 1, "Name");
	    table.setText(0, 2, "Material");
	    table.setText(0, 3, "Beschreibung");
	    table.setText(0, 4, "Letzter Änderer");
	    table.setText(0, 5, "Letztes Änderungsdatum");
	    table.setText(0, 6, "Editieren");
	    table.setText(0, 7, "Löschen");
	    
	    table.setStyleName("tableHead");
	    
	    for(int i = 0; i < allBauteile.size(); i++){
	    	System.out.println("Inhalt id: "+allBauteile.get(i).getId());
	    	System.out.println("Inhalt name: "+allBauteile.get(i).getName());
	    	System.out.println("Inhalt beschreibung: "+allBauteile.get(i).getMaterialBezeichnung());
	    }
	    
		this.add(HeadlineLabel);
		this.add(SublineLabel);
		this.add(NewBauteilButton);
		this.add(OverviewTableLabel);
		this.add(table);
	    
		RootPanel.get("content_wrap").add(this);
		
	}
	
	/*
	 * Click Handlers.
	 */
	
	/**
	  * Das Löschen eines Bauteils wird mithilfe der mitgelieferten Objekt-ID über die Applikationsschicht
	  * an den Server geschickt. Der User erhält eine entsprechende Hinweismeldung angezeigt und die
	  * Tabelle wird neu geladen.
	  * 
	  */
	 private class DeleteClickHandler implements ClickHandler {
	  @Override
	  public void onClick(ClickEvent event) {
//	   if (customerToDisplay != null) {

			RootPanel.get("content_wrap").clear();
			Window.alert("Bauteil wurde vielleich gelöscht");
			RootPanel.get("content_wrap").add(new BauteilGeneralView());
		   
//	   } else {
//	    Window.alert("kein Kunde ausgewählt");
//	   }
	  }
	 }
	 		
		class GetAllBauteileCallback implements AsyncCallback<Vector<Bauteil>> {

			@Override
			public void onFailure(Throwable caught) {
				Window.alert("Bauteile konnten nicht geladen werden");
			}

			@Override
			public void onSuccess(Vector<Bauteil> alleBauteile) {

//				Window.alert("Inhalt allBauteile: "+allBauteile);
				allBauteile = alleBauteile;
//				System.out.println("Inhalt allBauteile: "+allBauteile);
				
				for (int row = 1; row <= allBauteile.size(); row++) {
//				      for (int col = 0; col < numColumns; col++) {

				    	//Da die erste Reihe der Tabelle als Überschriften der Spalten dient, wird eine neue Variable benötigt,
				    	//die den Index 0 des Vectors auslesen kann.
				    	int i = row-1;
				    	
				        Button editBtn = new Button("");
				        Button deleteBtn = new Button("");
				        
				        editBtn.setStyleName("editButton");
				        deleteBtn.setStyleName("deleteButton");
				        
				        editBtn.addClickHandler(new ClickHandler(){
							public void onClick(ClickEvent event) {
								RootPanel.get("content_wrap").clear();
								RootPanel.get("content_wrap").add(new EditBauteil());
							    }

						});
				        
				        deleteBtn.addClickHandler(new DeleteClickHandler());
				    	
				        table.setText(row, 0, ""+allBauteile.get(i).getId());
				        table.setText(row, 1, allBauteile.get(i).getName());
				        table.setText(row, 2, allBauteile.get(i).getBeschreibung());
				        table.setText(row, 3, allBauteile.get(i).getMaterialBezeichnung());
				        table.setText(row, 4, "Mario");
				        table.setText(row, 5, "02.05.2015, 18 Uhr");
				        table.setWidget(row, 6, editBtn);
				        table.setWidget(row, 7, deleteBtn);
				    	  
//				      }
				    }
				
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
