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
import de.hdm.gruppe1.client.BauteilGeneralView.GetAllBauteileCallback;
import de.hdm.gruppe1.shared.SmsAsync;
import de.hdm.gruppe1.shared.bo.Bauteil;

/*
 * Die Klasse StuecklisteGeneralView liefert eine �bersicht mit allen vorhandenen St�cklisten im System
 * und bietet M�glichkeiten, diese zu editieren oder l�schen.
 */
public class StuecklisteGeneralView extends VerticalPanel {

	//Elemente f�r St�cklisten initialisieren
	private final Label HeadlineLabel = new Label ("St�cklisten�bersicht");
	
	//Buttons sollen nebeneinander angezeigt werden, nicht vertikal. Daher wird ein "vertikales Zwischen-Panel" ben�tigt
	private HorizontalPanel editButtonPanel = new HorizontalPanel();
	private HorizontalPanel deleteButtonPanel = new HorizontalPanel();
	
	//Den Buttons wird jeweils ein erkl�render Text hinzugef�gt
	private final Label editLabel = new Label("W�hlen Sie in der �bersicht eine St�ckliste aus, um sie mithilfe dieses Buttons zu editieren: ");
	private final Label deleteLabel = new Label("W�hlen Sie in der �bersicht mindestens eine St�ckliste aus, um sie mithilfe dieses Buttons zu l�schen: ");
		
	//Neu: Single-Button Editieren
	private final Button editBtn = new Button("");
	//Neu: Single-Button L�schen
	private final Button deleteBtn = new Button("");
	
	private final FlexTable table = new FlexTable();
	
	//TODO imlementieren
	//St�ckliste, die editiert werden soll
//	Stueckliste editStueckliste = null;
		
	//TODO implementieren
	//Vektor wird mit allen Bauteilen aus der DB bef�llt
//	Vector<Stueckliste> allStuecklisten = new Vector<Stueckliste>();
		
	// Remote Service via ClientsideSettings
	SmsAsync stuecklistenVerwaltung = ClientsideSettings.getSmsVerwaltung();
	
	public StuecklisteGeneralView() {
		
		//Damit die edit und delete Buttons horizontal angeordnet werden, m�ssen diese dem ButtonPanel zugeordnet werden
		editButtonPanel.add(editLabel);
		editButtonPanel.add(editBtn);
				
		deleteButtonPanel.add(deleteLabel);
		deleteButtonPanel.add(deleteBtn);
				
		editBtn.setStyleName("editButton");
		deleteBtn.setStyleName("deleteButton");
		HeadlineLabel.setStyleName("headline");
		table.setStyleName("BauteilTable");
				
		//Applikationsschicht liefert <Stueckliste>-Vector.
		//Diesen mithilfe for-Schleife durchlaufen und angemessen darstellen.

		//TODO implementieren
//		stuecklistenVerwaltung.getAllBauteile(new GetAllStuecklistenCallback());

		//Die erste Reihe der Tabelle wird mit �berschriften vordefiniert
		table.setText(0, 0, "ID");
		table.setText(0, 1, "Name");
		table.setText(0, 2, "Erstellungsdatum");
		table.setText(0, 3, "Letzter �nderer");
		table.setText(0, 4, "Letztes �nderungsdatum");
		table.setText(0, 5, "Editieren");
		table.setText(0, 6, "L�schen");
			    
		//Das FlexTable Widget unterst�tzt keine Headlines. Daher wird die erste Reihe �ber folgenden Umweg formatiert
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
	
	/**
	  * Das L�schen einer St�ckliste wird mithilfe der mitgelieferten Objekt-ID �ber die Applikationsschicht
	  * an den Server geschickt. Der User erh�lt eine entsprechende Hinweismeldung angezeigt und die
	  * Tabelle wird neu geladen.
	  * 
	  */
	 private class DeleteClickHandler implements ClickHandler {
	  @Override
	  public void onClick(ClickEvent event) {
//	   if (customerToDisplay != null) {

			RootPanel.get("content_wrap").clear();
			Window.alert("St�ckliste wurde vielleicht gel�scht");
			RootPanel.get("content_wrap").add(new StuecklisteGeneralView());
		   
//	   } else {
//	    Window.alert("kein Kunde ausgew�hlt");
//	   }
	  }
	 }
	 
	 //TODO implementieren
//	 class GetAllBauteileCallback implements AsyncCallback<Vector<Bauteil>> {
//
//			@Override
//			public void onFailure(Throwable caught) {
//				Window.alert("Bauteile konnten nicht geladen werden");
//			}
//
//			@Override
//			public void onSuccess(Vector<Stueckliste> alleStuecklisten) {
//
//				allStuecklisten = alleStuecklisten;
//				
//				for (int row = 1; row <= allStuecklisten.size(); row++) {
////				      for (int col = 0; col < numColumns; col++) {
//
//				    	//Da die erste Reihe der Tabelle als �berschriften der Spalten dient, wird eine neue Variable ben�tigt,
//				    	//die den Index 0 des Vectors auslesen kann.
//				    	final int i = row-1;
//				    	
//				        RadioButton radioButton = new RadioButton("editRadioGroup", "");
//				        CheckBox checkBox = new CheckBox("");
//
//				        deleteBtn.addClickHandler(new DeleteClickHandler());
//				    	
//				        //Pro Vektor-Index wird eine Reihe in die Tabelle geschrieben
//				        table.setText(row, 0, ""+allStuecklisten.get(i).getId());
//				        table.setText(row, 1, allStuecklisten.get(i).getName());
//				        table.setText(row, 4, "Mario");
//				        table.setText(row, 5, "02.05.2015, 18 Uhr");
//				        
//				        //RadioButton Widget f�r Single editieren-Button
//				        table.setWidget(row, 6, radioButton);
//				        
//				        //Pro Reihe wird dem radioButton ein ValueChangeHandler hinzugef�gt
//						radioButton.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
//				            @Override
//				            public void onValueChange(ValueChangeEvent<Boolean> e) {
//				                if(e.getValue() == true)
//				                {
//				                    editStueckliste = allStuecklisten.get(i);
//				                }
//				            }
//				        }); 
//				        
//				        table.setWidget(row, 7, checkBox);
//				        
//				        table.setStyleName("StuecklisteTable");
//				        
//				    }
//				
//				//ClickHandler f�r Aufruf der Klasse editBauteil
//		        editBtn.addClickHandler(new ClickHandler(){
//					public void onClick(ClickEvent event) {
//						
//						if(editStueckliste==null){
//							Window.alert("Bitte w�hlen Sie eine St�ckliste zum editieren aus.");
//						} else {
//							RootPanel.get("content_wrap").clear();
//							RootPanel.get("content_wrap").add(new EditStueckliste(editStueckliste));
//						}
//
//					}
//
//				});
//				
//				
//				//TODO: Kl�ren ob das catvm gebraucht wird 
//				// if (bauteil != null) {
//				// Das erfolgreiche Hinzuf�gen eines Kunden wird an den
//				// Kunden- und
//				// Kontenbaum propagiert.
//				// catvm.addCustomer(customer);
//				// }
//			}
//		}
//	
}