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
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

import de.hdm.gruppe1.client.CreateBauteil.CreateBauteilCallback;
import de.hdm.gruppe1.shared.SmsAsync;
import de.hdm.gruppe1.shared.bo.Bauteil;

//Die Klasse CreateStueckliste liefert alle benötigten Elemente, um eine neue Stückliste im System anzulegen.
public class CreateStueckliste extends VerticalPanel {

	//Elemente für CreateStückliste initialisieren
	private final Label HeadlineLabel = new Label ("Stückliste anlegen");
	private final Label SublineLabel = new Label ("Um eine Stückliste anzulegen, füllen Sie bitte alle Felder aus und bestätigen mit dem <anlegen>-Button ihre Eingabe.");
	private final Label bauteilLabel = new Label("Bauteile für Stückliste");
	private final Label baugruppeLabel = new Label("Baugruppen für Stückliste");
	private final Label NameFieldLabel = new Label ("Name");
	private final TextBox NameField = new TextBox ();
	private final Label BauteilLabel = new Label ("Bauteile hinzufügen");
	ListBox listBoxBauteile = new ListBox();
	private final Button collectBtButton = new Button("hinzufügen");
	private final Label BaugruppeLabel= new Label ("Baugruppen/Enderzeugnisse hinzufügen");
	ListBox listBoxBaugruppen = new ListBox();
	private final Button collectBgButton = new Button("hinzufügen");
	private final Button CreateStuecklisteButton = new Button ("Stückliste anlegen");
	
	//Panels, um die Buttons neben den Dropdowns zu platzieren
	HorizontalPanel btPanel = new HorizontalPanel();
	HorizontalPanel bgPanel = new HorizontalPanel();
	
	//TODO implementieren
	//Ein Bauteil und eine Baugruppe, die der zugehörigen Übersichtstabelle hinzugefügt werden
	Bauteil bT = new Bauteil();
//	Baugruppe bG = new Baugruppe();
	
	//TODO implementieren
	//Vektoren, um die hinzugefügten Bauteile/Baugruppen in einer Übersicht zu sammeln, bevor die Stückliste gespeichert wird
	Vector<Bauteil> collectBauteile = new Vector<Bauteil>();
//	Vector<Baugruppe> collectBaugruppen = new Vector<Baugruppe>();
	
	//Tabellen, um in der GUI alle Bauteile/Baugruppen anzuzeigen, bevor die Stückliste gespeichert wird
	FlexTable bauteilCollection = new FlexTable();
	FlexTable baugruppeCollection = new FlexTable();
	
	// Remote Service via ClientsideSettings
	SmsAsync stuecklistenVerwaltung = ClientsideSettings.getSmsVerwaltung();
		
	public CreateStueckliste(){

		//Bauteil vorübergehend statisch befüllt
		bT.setId(1);
		bT.setName("Schraube");
		
		//Die erste Reihe der Tabelle wird mit Überschriften vordefiniert
		bauteilCollection.setText(0, 0, "ID");
		bauteilCollection.setText(0, 1, "Name");
		bauteilCollection.setText(0, 2, "Entfernen");
		
		baugruppeCollection.setText(0, 0, "ID");
		baugruppeCollection.setText(0, 1, "Name");
		baugruppeCollection.setText(0, 2, "Entfernen");
	    
	    //Das FlexTable Widget unterstützt keine Headlines. Daher wird die erste Reihe über folgenden Umweg formatiert
		bauteilCollection.getCellFormatter().addStyleName(0, 0, "tableHead");
		bauteilCollection.getCellFormatter().addStyleName(0, 1, "tableHead");
		bauteilCollection.getCellFormatter().addStyleName(0, 2, "tableHead");
		
		baugruppeCollection.getCellFormatter().addStyleName(0, 0, "tableHead");
		baugruppeCollection.getCellFormatter().addStyleName(0, 1, "tableHead");
		baugruppeCollection.getCellFormatter().addStyleName(0, 2, "tableHead");
		
		//Da RPC-Methoden noch nicht implementiert sind, wird hier beispielhaft das Dropdown manuell befüllt
		listBoxBauteile.addItem(bT.getName()+" 1");
		listBoxBauteile.addItem(bT.getName()+" 2");
		listBoxBauteile.addItem(bT.getName()+" 3");
		listBoxBauteile.addItem(bT.getName()+" 4");
		listBoxBauteile.addItem(bT.getName()+" 5");
		
		collectBtButton.addClickHandler(new ClickHandler(){
			public void onClick(ClickEvent event) {

				int index = listBoxBauteile.getSelectedIndex();
				String myValue = listBoxBauteile.getValue(index);
				
				CheckBox removeBtBtn = new CheckBox();			
				
				bauteilCollection.setText(index+1, 0, ""+bT.getId());
				bauteilCollection.setText(index+1, 1, bT.getName());
				bauteilCollection.setWidget(index+1, 2, removeBtBtn);
				
		        //Pro Reihe wird dem radioButton ein ValueChangeHandler hinzugefügt
				removeBtBtn.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
		            @Override
		            public void onValueChange(ValueChangeEvent<Boolean> e) {
		                if(e.getValue() == true)
		                {
//		                    editBauteil = allBauteile.get(i);
		                	Window.alert("Ausgewählt: "+bT.getName());
		                }
		            }
		        }); 

			}

		});
		
		listBoxBaugruppen.addItem("Bg1");
		listBoxBaugruppen.addItem("Bg2");
		listBoxBaugruppen.addItem("Bg3");
		listBoxBaugruppen.addItem("Bg4");
		listBoxBaugruppen.addItem("Bg5");
		
		btPanel.add(listBoxBauteile);
		btPanel.add(collectBtButton);
		
		bgPanel.add(listBoxBaugruppen);
		bgPanel.add(collectBgButton);
			
		this.add(HeadlineLabel);
		this.add(SublineLabel);
		this.add(bauteilLabel);
		this.add(bauteilCollection);
		this.add(baugruppeLabel);
		this.add(baugruppeCollection);
		this.add(NameFieldLabel);
		this.add(NameField);
		this.add(BauteilLabel);
		this.add(btPanel);
		this.add(bgPanel);
		this.add(CreateStuecklisteButton);
			
		HeadlineLabel.setStyleName("headline");
		SublineLabel.setStyleName("subline");
		CreateStuecklisteButton.setStyleName("Button");
		
		CreateStuecklisteButton.addClickHandler(new CreateClickHandler());

		RootPanel.get("content_wrap").add(this);

	}

	/*
	 * Click Handlers.
	 */
	
	/**
	 * Das Hinzufügen eines Bauteils zur Tabelle. 
	 */
	private class collectBtClickHandler implements ClickHandler {
		
		public collectBtClickHandler(String myValue) {
		}

		@Override
		public void onClick(ClickEvent event) {
			
			CheckBox removeBtBtn = new CheckBox();			
			int index = bauteilCollection.getRowCount()+1;
			
			bauteilCollection.setText(index, 0, ""+bT.getId());
			bauteilCollection.setText(index, 1, bT.getName());
			bauteilCollection.setWidget(index, 2, removeBtBtn);
			
	        //Pro Reihe wird dem radioButton ein ValueChangeHandler hinzugefügt
			removeBtBtn.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
	            @Override
	            public void onValueChange(ValueChangeEvent<Boolean> e) {
	                if(e.getValue() == true)
	                {
//	                    editBauteil = allBauteile.get(i);
	                	Window.alert("Ausgewählt: "+bT.getName());
	                }
	            }
	        }); 
		}
	}
		
	/**
	 * Die Anlage einer Stückliste. 
	 * Es erfolgt der Aufruf der Service-Methode "create".
	 */
	private class CreateClickHandler implements ClickHandler {
		@Override
		public void onClick(ClickEvent event) {
			
			String name = NameField.getText();

			if(NameField.getText().isEmpty() != true){
					
				//TODO callback implementieren
//				stuecklistenVerwaltung.createStueckliste(name, new StuecklisteCallback());
					
				RootPanel.get("content_wrap").clear();
				RootPanel.get("content_wrap").add(new StuecklisteGeneralView());
					 
			}
				
			else {
					
				Window.alert("Bitte alle Felder ausfüllen.");
					
			}
				
		}
	}
		
	class CreateBauteilCallback implements AsyncCallback<Bauteil> {

		@Override
		public void onFailure(Throwable caught) {
			Window.alert("Das Anlegen der Stückliste ist fehlgeschlagen!");
		}

		@Override
		public void onSuccess(Bauteil bauteil) {

			Window.alert("Die Stückliste wurde erfolgreich angelegt.");
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
