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
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import de.hdm.gruppe1.client.BauteilGeneralView.GetAllBauteileCallback;
import de.hdm.gruppe1.client.CreateBauteil.CreateBauteilCallback;
import de.hdm.gruppe1.shared.SmsAsync;
import de.hdm.gruppe1.shared.bo.Baugruppe;
import de.hdm.gruppe1.shared.bo.Bauteil;
import de.hdm.gruppe1.shared.bo.ElementPaar;

/**
 * Die Klasse CreateStueckliste ermöglicht dem User, Objekte von Stückliste in der Datenbank anzulegen.
 * 
 * @author Mario Theiler
 * @version 1.0
 */
public class CreateStueckliste extends VerticalPanel {

	//Elemente für CreateStückliste initialisieren
	private final Label HeadlineLabel = new Label ("Stückliste anlegen");
	private final Label SublineLabel = new Label ("Um eine Stückliste anzulegen, füllen Sie bitte alle Felder aus und bestätigen mit dem <anlegen>-Button ihre Eingabe.");
	private final Label bauteilLabel = new Label("Bauteile für Stückliste");
	private final Label baugruppeLabel = new Label("Baugruppen für Stückliste");
	private final TextBox NameField = new TextBox ();
	private final Label BauteilLabel = new Label ("Gewünschte Anzahl von Bauteilen hinzufügen");
	private final Label BaugruppeLabel = new Label ("Gewünschte Anzahl von Baugruppen hinzufügen");
	private final TextBox amountBauteile = new TextBox();
	ListBox listBoxBauteile = new ListBox();
	private final Button collectBtButton = new Button("hinzufügen");
	private final TextBox amountBaugruppen = new TextBox();
	ListBox listBoxBaugruppen = new ListBox();
	private final Button collectBgButton = new Button("hinzufügen");
	private final Button CreateStuecklisteButton = new Button ("Stückliste anlegen");
	private final Label deleteBauteilLabel = new Label("Markierte Bauteile entfernen: ");
	private final Label deleteBaugruppeLabel = new Label("Markierte Baugruppen entfernen: ");
	private final Button deleteBauteilButton = new Button("entfernen");
	private final Button deleteBaugruppeButton = new Button("entfernen");
	
	//Panels, um die hinzufügen-Buttons neben den Dropdowns zu platzieren
	HorizontalPanel btPanel = new HorizontalPanel();
	HorizontalPanel bgPanel = new HorizontalPanel();
	
	//Panels, um den entfernen-Button neben dem Text anzuzeigen
	HorizontalPanel deleteBauteilPanel = new HorizontalPanel();
	HorizontalPanel deleteBaugruppePanel = new HorizontalPanel();
	
	//TODO implementieren
	//Vektor wird mit allen Bauteilen bzw. Baugruppen aus der DB befüllt
	Vector<Bauteil> allBauteile = new Vector<Bauteil>();
	Vector<Baugruppe> allBaugruppen = new Vector<Baugruppe>();
	
	//TODO implementieren
	//Ein Bauteil und eine Baugruppe, die der zugehörigen Übersichtstabelle hinzugefügt werden
	Bauteil bT = new Bauteil();
//	Baugruppe bG = new Baugruppe();
	
	//TODO implementieren
	//Vektoren, um die hinzugefügten Bauteile/Baugruppen in einer Übersicht zu sammeln, bevor die Stückliste gespeichert wird
	Vector<ElementPaar> collectBauteile = new Vector<ElementPaar>();
	Vector<ElementPaar> collectBaugruppen = new Vector<ElementPaar>();
	
	//Tabellen, um in der GUI alle Bauteile/Baugruppen anzuzeigen, bevor die Stückliste gespeichert wird
	FlexTable bauteilCollection = new FlexTable();
	FlexTable baugruppeCollection = new FlexTable();
	
	// Remote Service via ClientsideSettings
	SmsAsync stuecklistenVerwaltung = ClientsideSettings.getSmsVerwaltung();
		
	//Konstruktor der Klasse CreateStueckliste. Gibt vor, dass bei jeder Instantiierung die entsprechenden GUI-Elemente
	//geladen werden.
	public CreateStueckliste(){

		NameField.getElement().setPropertyString("placeholder", "Hier bitte Namen eintragen");
		amountBauteile.getElement().setPropertyString("placeholder", "Anzahl");
		amountBaugruppen.getElement().setPropertyString("placeholder", "Anzahl");
		
		//Die erste Reihe der Tabelle wird mit Überschriften vordefiniert
		bauteilCollection.setText(0, 0, "ID");
		bauteilCollection.setText(0, 1, "Anzahl");
		bauteilCollection.setText(0, 2, "Name");
		bauteilCollection.setText(0, 3, "Entfernen");
		
		baugruppeCollection.setText(0, 0, "ID");
		baugruppeCollection.setText(0, 1, "Anzahl");
		baugruppeCollection.setText(0, 2, "Name");
		baugruppeCollection.setText(0, 3, "Entfernen");
		
		//css für Tabelle definieren
		bauteilCollection.setStyleName("tableBody");
		baugruppeCollection.setStyleName("tableBody");
	    
	    //Das FlexTable Widget unterstützt keine Headlines. Daher wird die erste Reihe über folgenden Umweg formatiert
		bauteilCollection.getCellFormatter().addStyleName(0, 0, "tableHead");
		bauteilCollection.getCellFormatter().addStyleName(0, 1, "tableHead");
		bauteilCollection.getCellFormatter().addStyleName(0, 2, "tableHead");
		bauteilCollection.getCellFormatter().addStyleName(0, 3, "tableHead");
		
		baugruppeCollection.getCellFormatter().addStyleName(0, 0, "tableHead");
		baugruppeCollection.getCellFormatter().addStyleName(0, 1, "tableHead");
		baugruppeCollection.getCellFormatter().addStyleName(0, 2, "tableHead");
		baugruppeCollection.getCellFormatter().addStyleName(0, 3, "tableHead");
		
		stuecklistenVerwaltung.getAllBauteile(new GetAllBauteileCallback());
		
		collectBtButton.addClickHandler(new ClickHandler(){
			public void onClick(ClickEvent event) {

				int index = listBoxBauteile.getSelectedIndex();
				
//				final CheckBox removeBtCheckBox = new CheckBox();	
				Button removeButton = new Button();
				
				ElementPaar bauteilPaar = new ElementPaar();
				
				Integer anzahl = Integer.parseInt(amountBauteile.getText());
				
				bauteilPaar.setAnzahl(anzahl);
				bauteilPaar.setElement(allBauteile.get(index));
				
				collectBauteile.add(bauteilPaar);
				
				Window.alert("Anzahl: "+bauteilPaar.getAnzahl()+ " Bauteil: "+bauteilPaar.getElement().getName());
				
				//ListBox-Element, das hinzugefügt wurde, wird für doppeltes Hinzufügen gesperrt
				listBoxBauteile.getElement().getElementsByTagName("option").getItem(index).setAttribute("disabled", "disabled");
				
				for(int i = 0; i<= collectBauteile.size(); i++){
					bauteilCollection.setText(i+1, 0, ""+collectBauteile.get(i).getElement().getId());
					bauteilCollection.setText(i+1, 1, ""+collectBauteile.get(i).getAnzahl());
					bauteilCollection.setText(i+1, 2, collectBauteile.get(i).getElement().getName());
					bauteilCollection.setWidget(i+1, 3, removeButton);
					
					removeButton.addClickHandler(new ClickHandler(){
						public void onClick(ClickEvent event) {
							
							Window.alert("Inhalt Bauteil Vektor: "+collectBauteile.toString());
							
//							int a= i;
//							collectBauteile.remove(0);
							
//							bauteilCollection.removeRow(i+1);
							
							//ListBox-Element, das entfernt wurde, wird für erneutes Hinzufügen wieder angezeigt
//							listBoxBauteile.getElement().getElementsByTagName("option").getItem(index+1).setAttribute("enabled", "enabled");
							

						}

					});
				}
				
//				bauteilCollection.setText(index+1, 0, ""+allBauteile.get(index).getId());
//				bauteilCollection.setText(index+1, 1, "Zahl");
//				bauteilCollection.setText(index+1, 2, allBauteile.get(index).getName());
//				bauteilCollection.setWidget(index+1, 3, removeButton);
				
				//ListBox-Element, das hinzugefügt wurde, wird für doppeltes Hinzufügen gesperrt
//				listBoxBauteile.getElement().getElementsByTagName("option").getItem(index).setAttribute("disabled", "disabled");
				
				//TODO fehlerhaft!
				//Dem globalen Remove-Button wird ein ClickHandler hinzugefügt, der alle markierten Bauteile entfernt
//				removeButton.addClickHandler(new ClickHandler(){
//					public void onClick(ClickEvent event) {
//						
//						Window.alert("Inhalt Bauteil Vektor: "+collectBauteile.toString());
////						collectBauteile.remove(index);
//						
//						bauteilCollection.removeRow(index+1);
//						
//						//ListBox-Element, das entfernt wurde, wird für erneutes Hinzufügen wieder angezeigt
////						listBoxBauteile.getElement().getElementsByTagName("option").getItem(index+1).setAttribute("enabled", "enabled");
//						
//
//					}
//
//				});
				
		        //Pro Reihe wird dem radioButton ein ValueChangeHandler hinzugefügt
//				removeBtBtn.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
//		            @Override
//		            public void onValueChange(ValueChangeEvent<Boolean> e) {
//		                if(e.getValue() == true)
//		                {
////		                    editBauteil = allBauteile.get(i);
//		                	bauteilCollection.removeRow(index+1);
//		                }
//		            }
//		        }); 
				
			}

		});
		
		Baugruppe a = new Baugruppe();
		Baugruppe b = new Baugruppe();
		Baugruppe c = new Baugruppe();
		
		a.setId(1);
		b.setId(2);
		c.setId(3);
		
		a.setName("Name1");
		b.setName("Name2");
		c.setName("Name3");
		
		allBaugruppen.add(a);
		allBaugruppen.add(b);
		allBaugruppen.add(c);
		
		listBoxBaugruppen.addItem(a.getName());
		listBoxBaugruppen.addItem(b.getName());
		listBoxBaugruppen.addItem(c.getName());
		
		collectBgButton.addClickHandler(new ClickHandler(){
			public void onClick(ClickEvent event) {

				int index = listBoxBaugruppen.getSelectedIndex();
				
				Button removeBgButton = new Button();
				
				ElementPaar baugruppePaar = new ElementPaar();
				
				Integer anzahl = Integer.parseInt(amountBaugruppen.getText());
				
				baugruppePaar.setAnzahl(anzahl);
				baugruppePaar.setElement(allBaugruppen.get(index));
				
				collectBaugruppen.add(baugruppePaar);
				
				Window.alert("Anzahl: "+baugruppePaar.getAnzahl()+ " Bauteil: "+baugruppePaar.getElement().getName());
				
				//ListBox-Element, das hinzugefügt wurde, wird für doppeltes Hinzufügen gesperrt
				listBoxBaugruppen.getElement().getElementsByTagName("option").getItem(index).setAttribute("disabled", "disabled");
				
				for(int i = 0; i<= collectBaugruppen.size(); i++){
					baugruppeCollection.setText(i+1, 0, ""+collectBaugruppen.get(i).getElement().getId());
					baugruppeCollection.setText(i+1, 1, ""+collectBaugruppen.get(i).getAnzahl());
					baugruppeCollection.setText(i+1, 2, collectBaugruppen.get(i).getElement().getName());
					baugruppeCollection.setWidget(i+1, 3, removeBgButton);
					
					removeBgButton.addClickHandler(new ClickHandler(){
						public void onClick(ClickEvent event) {
							
							Window.alert("Inhalt Baugruppe Vektor: "+collectBaugruppen.toString());
							

						}

					});
				}
				
			}

		});
		
		btPanel.add(amountBauteile);
		btPanel.add(listBoxBauteile);
		btPanel.add(collectBtButton);
		
		bgPanel.add(amountBaugruppen);
		bgPanel.add(listBoxBaugruppen);
		bgPanel.add(collectBgButton);
		
		//Damit die edit und delete Buttons horizontal angeordnet werden, müssen diese folgenden Panels zugeordnet werden
		deleteBauteilPanel.add(deleteBauteilLabel);
		deleteBauteilPanel.add(deleteBauteilButton);
		
		deleteBaugruppePanel.add(deleteBaugruppeLabel);
		deleteBaugruppePanel.add(deleteBaugruppeButton);
			
		this.add(HeadlineLabel);
		this.add(SublineLabel);
		this.add(NameField);
		this.add(BauteilLabel);
		this.add(btPanel);
		this.add(BaugruppeLabel);
		this.add(bgPanel);
		this.add(bauteilLabel);
		this.add(bauteilCollection);
		this.add(deleteBauteilPanel);
		this.add(baugruppeLabel);
		this.add(baugruppeCollection);
		this.add(deleteBaugruppePanel);
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
				
				//Vector wird erstellt und an die Applikationsschicht übergeben, inkl. dem Namen
				Vector<Bauteil> createBauteile = new Vector<Bauteil>();
				//TODO implementieren, sobald Tabelle aus globalem collection-Vector befüllt wird
				//bisher noch nicht möglich, da die Tabelle nicht mit einem Vector befüllt wird
//				for(int i = 0; i<bauteilCollection.getRowCount(); i++){
//					createBauteile.addElement(bauteilCollection.getElement());
//				}
				
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
	
	class GetAllBauteileCallback implements AsyncCallback<Vector<Bauteil>> {

		@Override
		public void onFailure(Throwable caught) {
			Window.alert("Bauteile konnten nicht geladen werden");
		}

		@Override
		public void onSuccess(Vector<Bauteil> alleBauteile) {

			//Befüllung des noch leeren Klassen-Vektors mit Bauteil-Vektor aus RPC-Callback
			allBauteile = alleBauteile;
			
			//Schleife durchläuft kompletten Vektor
			for(int c = 0; c <=allBauteile.size(); c++){
				
				//Dropdown listBoxBauteile wird mit Bauteil-Objekten (nur Name) befüllt
				listBoxBauteile.addItem(allBauteile.get(c).getName());
				
			}
			
		}
	}
	
}
