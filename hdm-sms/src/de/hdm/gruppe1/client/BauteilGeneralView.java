package de.hdm.gruppe1.client;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.Header;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HeaderPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import de.hdm.gruppe1.shared.bo.Bauteil;

/*
 * Die Klasse BauteilGeneralView liefert eine �bersicht mit allen vorhandenen Bauteilen im System
 * und bietet M�glichkeiten, diese anzulegen, zu editieren oder zu l�schen.
 */
public class BauteilGeneralView extends VerticalPanel {

	//Elemente f�r Bauteile initialisieren
	private final Label HeadlineLabel = new Label ("Bauteil�bersicht");
	private final Label SublineLabel = new Label ("In dieser �bersicht sehen Sie alle im System vorhandenen Bauteile. Um diese zu editieren oder l�schen, klicken Sie in der Tabelle auf den entsprechenden Button. Um ein neues Bauteil anzulegen, klicken Sie auf den <Neues Bauteil>-Button.");
	private final Button NewBauteilButton = new Button ("Neues Bauteil");
	private final Label OverviewTableLabel = new Label ("Diese Tabelle enth�lt eine �bersicht �ber alle Bauteile im System");
	Grid grid = new Grid(3, 8);
	CellTable table = new CellTable();

	public BauteilGeneralView() {

		this.add(HeadlineLabel);
		this.add(SublineLabel);
		this.add(NewBauteilButton);
		this.add(OverviewTableLabel);
		this.add(grid);
		this.add(table);
		
		NewBauteilButton.addClickHandler(new ClickHandler(){
			public void onClick(ClickEvent event) {
				RootPanel.get("content_wrap").clear();
				RootPanel.get("content_wrap").add(new CreateBauteil());
			    }

		});
		
		//Applikationsschicht liefert <Bauteil>-Vector.
		//Diesen mithilfe for-Schleife durchlaufen und angemessen darstellen.
		//Hier sind aktuell lediglich die Tabellen�berschriften definiert.

		//Testweise bis zur Anbindung zur Applikationsschicht wird hier ein Beispiel-Bauteil initialisiert
		Bauteil b = new Bauteil();
		
		b.setId(1);
		b.setName("Schraube");
		b.setMaterialBeschreibung("Eisen");
		b.setBauteilBeschreibung("Beispieltext");	
		
	    int numRows = grid.getRowCount();
//	    int numColumns = grid.getColumnCount();
	    
//	    grid.setText(0, 0, "ID");
//	    grid.setText(1, 0, "Name");
//	    grid.setText(2, 0, "Material");
//	    grid.setText(3, 0, "Beschreibung");
//	    grid.setText(4, 0, "Letzter �nderer");
//	    grid.setText(5, 0, "Letztes �nderungsdatum");
//	    grid.setText(6, 0, "Editieren");
//	    grid.setText(7, 0, "L�schen");
	    
	    for (int row = 0; row < numRows; row++) {
//	      for (int col = 0; col < numColumns; col++) {

	        Button editBtn = new Button("Edit");
	        Button deleteBtn = new Button("Delete");
	        
	        editBtn.addClickHandler(new ClickHandler(){
				public void onClick(ClickEvent event) {
					RootPanel.get("content_wrap").clear();
//					RootPanel.get("content_wrap").add(new EditBauteil());
					Window.alert("Platzhalter f�r EditBauteil GUI");
				    }

			});
	        
	        deleteBtn.addClickHandler(new DeleteClickHandler());
	    	
	        grid.setText(row, 0, "1");
	        grid.setText(row, 1, b.getName());
	        grid.setText(row, 2, b.getBauteilBeschreibung());
	        grid.setText(row, 3, b.getMaterialBeschreibung());
	        grid.setText(row, 4, "Mario");
	        grid.setText(row, 5, "02.05.2015, 18 Uhr");
	        grid.setWidget(row, 6, editBtn);
	        grid.setWidget(row, 7, deleteBtn);
	    	  
//	      }
	    }
	    
		RootPanel.get("content_wrap").add(this);
		
	}
	
	/*
	 * Click Handlers.
	 */
	
	/**
	  * Das L�schen eines Bauteils wird mithilfe der mitgelieferten Objekt-ID �ber die Applikationsschicht
	  * an den Server geschickt. Der User erh�lt eine entsprechende Hinweismeldung angezeigt und die
	  * Tabelle wird neu geladen.
	  * 
	  */
	 private class DeleteClickHandler implements ClickHandler {
	  @Override
	  public void onClick(ClickEvent event) {
//	   if (customerToDisplay != null) {

			RootPanel.get("content_wrap").clear();
			Window.alert("Bauteil wurde (nicht) gel�scht");
			RootPanel.get("content_wrap").add(new BauteilGeneralView());
		   
//	   } else {
//	    Window.alert("kein Kunde ausgew�hlt");
//	   }
	  }
	 }

}
