
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
import de.hdm.gruppe1.client.BaugruppeGeneralView.DeleteBaugruppeCallback;
import de.hdm.gruppe1.shared.SmsAsync;
import de.hdm.gruppe1.shared.bo.Bauteil;
import de.hdm.gruppe1.shared.bo.Baugruppe;


/**
 * Mithilfe der Klasse CreateBauteil wird dem User der Applikation ermöglicht,
 * ein Bauteil-Objekt in der Datenbank anzulegen.
 * 
 */

public class CreateBaugruppe extends VerticalPanel {
	
	/**
	 * GUI-Elemente für CreateBaugruppe initialisieren
	 */
	
	//Elemente f�r CreateSt�ckliste initialisieren
		private final Label HeadlineLabel = new Label ("Baugruppe anlegen");
		private final Label SublineLabel = new Label ("Um eine Baugruppe anzulegen, füllen Sie bitte alle Felder aus und bestätigen mit dem <anlegen>-Button ihre Eingabe.");
		private final Label bauteilLabel = new Label("Bauteile für Baugruppe");
		private final Label baugruppeLabel = new Label("Baugruppen für Baugruppe");
		private final TextBox NameField = new TextBox ();
		private final Label BauteilLabel = new Label ("Gewünschte Anzahl von Bauteilen hinzufügen");
		private final Label BaugruppeLabel = new Label ("Gewünschte Anzahl von Baugruppen hinzufügen");
		private final TextBox amountBauteile = new TextBox();
		ListBox listBoxBauteile = new ListBox();
		private final Button collectBtButton = new Button("hinzufügen");
		private final TextBox amountBaugruppen = new TextBox();
		ListBox listBoxBaugruppen = new ListBox();
		private final Button collectBgButton = new Button("hinzufügen");
		private final Button CreateBaugruppeButton = new Button ("Baugruppeanlegen");
		private final Label deleteBauteilLabel = new Label("Markierte Bauteile entfernen: ");
		private final Label deleteBaugruppeLabel = new Label("Markierte Baugruppen entfernen: ");
		private final Button deleteBauteilButton = new Button("entfernen");
		private final Button deleteBaugruppeButton = new Button("entfernen");
		
	
	
	//Panels, um die hinzuf�gen-Buttons neben den Dropdowns zu platzieren
		HorizontalPanel btPanel = new HorizontalPanel();
		HorizontalPanel bgPanel = new HorizontalPanel();
		
		//Panels, um den entfernen-Button neben dem Text anzuzeigen
		HorizontalPanel deleteBauteilPanel = new HorizontalPanel();
		HorizontalPanel deleteBaugruppePanel = new HorizontalPanel();
		
		//TODO implementieren
		//Vektor wird mit allen Bauteilen bzw. Baugruppen aus der DB bef�llt
		Vector<Bauteil> allBauteile = new Vector<Bauteil>();
//		Vector<Baugruppe> allBaugruppen = new Vector<Baugruppe>();
		
		//TODO implementieren
		//Ein Bauteil und eine Baugruppe, die der zugeh�rigen �bersichtstabelle hinzugef�gt werden
		Bauteil bT = new Bauteil();
//		Baugruppe bG = new Baugruppe();
		
		//TODO implementieren
		//Vektoren, um die hinzugef�gten Bauteile/Baugruppen in einer �bersicht zu sammeln, bevor die St�ckliste gespeichert wird
		Vector<Bauteil> collectBauteile = new Vector<Bauteil>();
//		Vector<Baugruppe> collectBaugruppen = new Vector<Baugruppe>();
		
		//Tabellen, um in der GUI alle Bauteile/Baugruppen anzuzeigen, bevor die St�ckliste gespeichert wird
		FlexTable bauteilCollection = new FlexTable();
		FlexTable baugruppeCollection = new FlexTable();
		
		// Remote Service via ClientsideSettings
		SmsAsync baugruppenVerwaltung= ClientsideSettings.getSmsVerwaltung();

	public CreateBaugruppe(){
	

			NameField.getElement().setPropertyString("placeholder", "Hier bitte Namen eintragen");
			amountBauteile.getElement().setPropertyString("placeholder", "Anzahl");
			amountBaugruppen.getElement().setPropertyString("placeholder", "Anzahl");
			
			//Bauteil vor�bergehend statisch bef�llt
			bT.setId(1);
			bT.setName("Schraube");
			
			//Die erste Reihe der Tabelle wird mit �berschriften vordefiniert
			bauteilCollection.setText(0, 0, "ID");
			bauteilCollection.setText(0, 1, "Anzahl");
			bauteilCollection.setText(0, 2, "Name");
			bauteilCollection.setText(0, 3, "Entfernen");
			
			baugruppeCollection.setText(0, 0, "ID");
			baugruppeCollection.setText(0, 1, "Anzahl");
			baugruppeCollection.setText(0, 2, "Name");
			baugruppeCollection.setText(0, 3, "Entfernen");
			
			//css f�r Tabelle definieren
			bauteilCollection.setStyleName("tableBody");
			baugruppeCollection.setStyleName("tableBody");
		    
		    //Das FlexTable Widget unterst�tzt keine Headlines. Daher wird die erste Reihe �ber folgenden Umweg formatiert
			bauteilCollection.getCellFormatter().addStyleName(0, 0, "tableHead");
			bauteilCollection.getCellFormatter().addStyleName(0, 1, "tableHead");
			bauteilCollection.getCellFormatter().addStyleName(0, 2, "tableHead");
			bauteilCollection.getCellFormatter().addStyleName(0, 3, "tableHead");
			
			baugruppeCollection.getCellFormatter().addStyleName(0, 0, "tableHead");
			baugruppeCollection.getCellFormatter().addStyleName(0, 1, "tableHead");
			baugruppeCollection.getCellFormatter().addStyleName(0, 2, "tableHead");
			baugruppeCollection.getCellFormatter().addStyleName(0, 3, "tableHead");
			
			baugruppenVerwaltung.getAllBauteile(new GetAllBauteileCallback());
			
			collectBtButton.addClickHandler(new ClickHandler() {
				public void onClick(ClickEvent event) {

					final int index = listBoxBauteile.getSelectedIndex();
					
					final CheckBox removeBtCheckBox = new CheckBox();			
					
					bauteilCollection.setText(index+1, 0, ""+allBauteile.get(index).getId());
					bauteilCollection.setText(index+1, 1, "Zahl");
					bauteilCollection.setText(index+1, 2, allBauteile.get(index).getName());
					bauteilCollection.setWidget(index+1, 3, removeBtCheckBox);
					
					//ListBox-Element, das hinzugef�gt wurde, wird f�r doppeltes Hinzuf�gen gesperrt
					listBoxBauteile.getElement().getElementsByTagName("option").getItem(index).setAttribute("disabled", "disabled");
					
					//TODO fehlerhaft!
					//Dem globalen Remove-Button wird ein ClickHandler hinzugef�gt, der alle markierten Bauteile entfernt
					deleteBauteilButton.addClickHandler(new ClickHandler(){
						public void onClick(ClickEvent event) {
							
							for(int i = 0; i <= bauteilCollection.getRowCount(); i++){
								if (removeBtCheckBox.getValue() == true){
									bauteilCollection.removeRow(i+1);
									
									//ListBox-Element, das entfernt wurde, wird f�r erneutes Hinzuf�gen wieder angezeigt
									listBoxBauteile.getElement().getElementsByTagName("option").getItem(index).setAttribute("enabled", "enabled");
									
								} else {
									Window.alert("In else: "+bauteilCollection.getRowFormatter().getElement(index+1).getId());
								}
								
							}

						}

					});
				}

			});
					
					listBoxBaugruppen.addItem("Baugruppe 1");
					listBoxBaugruppen.addItem("Baugruppe 2");
					listBoxBaugruppen.addItem("Baugruppe 3");
					listBoxBaugruppen.addItem("Baugruppe 4");
					listBoxBaugruppen.addItem("Baugruppe 5");
					
					btPanel.add(amountBauteile);
					btPanel.add(listBoxBauteile);
					btPanel.add(collectBtButton);
					
					bgPanel.add(amountBaugruppen);
					bgPanel.add(listBoxBaugruppen);
					bgPanel.add(collectBgButton);
					
					//Damit die edit und delete Buttons horizontal angeordnet werden, m�ssen diese folgenden Panels zugeordnet werden
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
					this.add(CreateBaugruppeButton);
						
					HeadlineLabel.setStyleName("headline");
					SublineLabel.setStyleName("subline");
					CreateBaugruppeButton.setStyleName("Button");
					
					CreateBaugruppeButton.addClickHandler(new CreateClickHandler());

					RootPanel.get("content_wrap").add(this);
	}
				

				/*
				 * Click Handlers.
				 */
				
				/**
				 * Die Anlage einer St�ckliste. 
				 * Es erfolgt der Aufruf der Service-Methode "create".
				 */
				private class CreateClickHandler implements ClickHandler {
					@Override
					public void onClick(ClickEvent event) {
						
						String name = NameField.getText();

						if(NameField.getText().isEmpty() != true){
								
							//TODO callback implementieren
//							stuecklistenVerwaltung.createStueckliste(name, new StuecklisteCallback());
							
							//Vector wird erstellt und an die Applikationsschicht �bergeben, inkl. dem Namen
							Vector<Bauteil> createBauteile = new Vector<Bauteil>();
							//TODO implementieren, sobald Tabelle aus globalem collection-Vector bef�llt wird
							//bisher noch nicht m�glich, da die Tabelle nicht mit einem Vector bef�llt wird
//							for(int i = 0; i<bauteilCollection.getRowCount(); i++){
//								createBauteile.addElement(bauteilCollection.getElement());
//							}
							
							RootPanel.get("content_wrap").clear();
							RootPanel.get("content_wrap").add(new StuecklisteGeneralView());
								 
						}
							
						else {
								
							Window.alert("Bitte alle Felder ausfüllen.");
								
						}
							
					}
				}
		/**
		 * Bei Instantiierung der Klasse wird alles dem VerticalPanel
		 * zugeordnet, da diese Klasse von VerticalPanel erbt.
		 */

				class CreateBauteilCallback implements AsyncCallback<Bauteil> {

					@Override
					public void onFailure(Throwable caught) {
						Window.alert("Das Anlegen der Baugruppe ist fehlgeschlagen!");
					}

					@Override
					public void onSuccess(Bauteil bauteil) {

						Window.alert("Die Baugruppe wurde erfolgreich angelegt.");
						//TODO: Kl�ren ob das catvm gebraucht wird 
						// if (bauteil != null) {
						// Das erfolgreiche Hinzuf�gen eines Kunden wird an den
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

						//Bef�llung des noch leeren Klassen-Vektors mit Bauteil-Vektor aus RPC-Callback
						allBauteile = alleBauteile;
						
						//Schleife durchl�uft kompletten Vektor
						for(int c = 0; c <=allBauteile.size(); c++){
							
							//Dropdown listBoxBauteile wird mit Bauteil-Objekten (nur Name) bef�llt
							listBoxBauteile.addItem(allBauteile.get(c).getName());
							
						}
						
					}
				}
				
			}