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

import de.hdm.gruppe1.client.EnderzeugnisGeneralView.GetAllEnderzeugnisseCallback;
//import de.hdm.gruppe1.client.CreateEnderzeugnis.CreateEnderzeugnisCallback;
//import de.hdm.gruppe1.client.EnderzeugnisGeneralView.DeleteEnderzeugnisCallback;

import de.hdm.gruppe1.shared.FieldVerifier;
import de.hdm.gruppe1.shared.SmsAsync;
import de.hdm.gruppe1.shared.bo.Bauteil;
import de.hdm.gruppe1.shared.bo.Baugruppe;
import de.hdm.gruppe1.shared.bo.Stueckliste;
import de.hdm.gruppe1.shared.bo.Enderzeugnis;

/**
 * Mithilfe der Klasse CreateEnderzeugnis wird dem User der Applikation ermöglicht,
 * ein aus Baugruppen erzeugtes Enderzeugnis-Objekt in die Datenbank anzulegen.
 * 
 */

public class CreateEnderzeugnis extends VerticalPanel {
	
	/**
	 * GUI-Elemente für CreateEnderzeugnis zu initialisieren
	 */
	
	//Elemente für Create Enderzeugnis  initialisieren
		private final Label HeadlineLabel = new Label ("Enderzeugnis anlegen");
		private final Label SublineLabel = new Label ("Um ein Enderezugnis anzulegen, geben sie bitte einen Namen an und wählen sie die dazugehöhrige Baugruppe aus, die sie als Enderzeugnis festlegen möchten und bestätigen sie Ihre Eingabe mit dem <anlegen>-Button.");
		private final Label baugruppeLabel = new Label("bestehende Baugruppen für neues Enderzeugnis");
		private final TextBox NameField = new TextBox ();
			
			ListBox listBoxBaugruppen = new ListBox();
		private final Button collectBgButton = new Button("hinzufügen");
		
		private final Button CreateEnderzeugnisButton = new Button ("Enderzeugnis anlegen");
		private final Label deleteBaugruppeLabel = new Label("Markierte Baugruppen entfernen: ");
		private final Button deleteBaugruppeButton = new Button("entfernen");
		
	
	
	//Panels, um die hinzuf�gen-Buttons neben den Dropdowns zu platzieren
		HorizontalPanel bgPanel = new HorizontalPanel();
		
		//Panels, um den entfernen-Button neben dem Text anzuzeigen
		HorizontalPanel deleteBaugruppePanel = new HorizontalPanel();
		
		//TODO implementieren
		//Vektor wird mit allen Bauteilen bzw. Baugruppen aus der DB bef�llt
		Vector<Baugruppe> allBaugruppen = new Vector<Baugruppe>();
		
		//TODO implementieren
		//Ein Bauteil und eine Baugruppe, die der zugeh�rigen �bersichtstabelle hinzugef�gt werde
		Baugruppe bG = new Baugruppe();
		
		//TODO implementieren
		//Vektoren, um die hinzugef�gten Bauteile/Baugruppen in einer �bersicht zu sammeln, bevor die St�ckliste gespeichert wird
		Vector<Baugruppe> collectBaugruppen = new Vector<Baugruppe>();
		
		//Tabellen, um in der GUI alle Bauteile/Baugruppen anzuzeigen, bevor die Baugruppe gespeichert wird
		FlexTable baugruppeCollection = new FlexTable();
		
		// Remote Service via ClientsideSettings
		SmsAsync baugruppenVerwaltung= ClientsideSettings.getSmsVerwaltung();

	public CreateEnderzeugnis(){
	

			NameField.getElement().setPropertyString("placeholder", "Hier bitte Namen eintragen");
			
			//Bauteil vor�bergehend statisch bef�llt
			
			baugruppeCollection.setText(0, 0, "ID");
			baugruppeCollection.setText(0, 1, "Anzahl");
			baugruppeCollection.setText(0, 2, "Name");
			baugruppeCollection.setText(0, 3, "Entfernen");
			
			//css f�r Tabelle definieren
			baugruppeCollection.setStyleName("tableBody");
		    
		    //Das FlexTable Widget unterst�tzt keine Headlines. Daher wird die erste Reihe �ber folgenden Umweg formatiert
			baugruppeCollection.getCellFormatter().addStyleName(0, 0, "tableHead");
			baugruppeCollection.getCellFormatter().addStyleName(0, 1, "tableHead");
			baugruppeCollection.getCellFormatter().addStyleName(0, 2, "tableHead");
			baugruppeCollection.getCellFormatter().addStyleName(0, 3, "tableHead");
			

			// Übergangsweise, bevor Merge mit Galina und Katja erfolgt ist,
			// erstelle ich mir an dieser Stelle eigene Objekte
			// von Baugruppen. An dieser Stelle muss nach dem Merge ein Vektor mit
			// allen Baugruppen aus der DB mithilfe eines
			// RPC angefragt werden.
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

			// TODO implementieren
			// Um das Dropdown mit Bauteilen aus der DB zu befüllen, wird dieser
			// RPC-Aufruf gestartet
			// stuecklistenVerwaltung.getAllBaugruppen(new
			// GetAllBaugruppenCallback());

			// Mithilfe des Hinzufügen-Buttons wird die BauteilCollection Tabelle
			// befüllt
			collectBgButton.addClickHandler(new ClickHandler() {
				public void onClick(ClickEvent event) {

					// Der index dient dazu, herauszufinden, welches Element im
					// DropDown ausgewählt wurde
					int index = listBoxBaugruppen.getSelectedIndex();


					// ListBox-Element, das hinzugefügt wurde, wird für doppeltes
					// Hinzufügen gesperrt
					listBoxBaugruppen.getElement().getElementsByTagName("option")
							.getItem(index).setAttribute("disabled", "disabled");

					// Die Übersichtstabelle, welche für den User eine hilfreiche
					// Übersicht aller hinzugefügten Baugruppen
					// bereitstellt, wird mithilfe dieser for-Schleife aufgebaut.
					// Die Schleife startet bei i = 1, da die
					// erste Reihe der Tabelle bereits mit den Überschriften befüllt
					// ist und diese nicht überschrieben werden
					// soll.
					for (int i = 1; i <= collectBaugruppen.size(); i++) {

						// Button, um in der BaugruppeCollection-Tabelle und
						// gleichzeitig dem Vektor eine Baugruppe
						// wieder zu entfernen
						final Button removeBgButton = new Button("x");

						// Der Wert von i muss final sein, damit sie im
						// nachfolgenden ClickHandler verwendet werden kann.
						// Daher wird sie mithilfe von int a finalisiert.
						final int b = i;

						// Die Tabelle befüllt sich aus allen Elementen, die im
						// collectBaugruppen-Vektor vorhanden sind.
						baugruppeCollection
								.setText(b, 0, ""
										+ collectBaugruppen.get(i - 1)
												.getId());
						baugruppeCollection.setText(b, 1, ""
								+ collectBaugruppen.get(i - 1));
						baugruppeCollection
								.setText(b, 2, collectBaugruppen.get(i - 1)
										.getName());
						baugruppeCollection.setWidget(b, 3, removeBgButton);

						// In jeder Reihe wird ein Entfernen-Button platziert, damit
						// der User schnell und unkompliziert
						// jederzeit ein ElementPaar von Baugruppe wieder entfernen
						// kann. Es ist ihm lediglich möglich,
						// gesamte ElementPaare von Baugruppen zu entfernen. Dies
						// muss er ebenfalls durchführen, wenn er
						// lediglich die Anzahl ändern möchte. Die Baugruppe mit der
						// gewünschten neuen Anzahl kann er
						// schnell erneutaus dem Dropdown hinzufügen.
						removeBgButton.addClickHandler(new ClickHandler() {
							public void onClick(ClickEvent event) {

								// Zum einen wird die entsprechende Reihe aus der
								// FlexTable entfernt.
								int rowIndex = baugruppeCollection.getCellForEvent(
										event).getRowIndex();
								baugruppeCollection.removeRow(rowIndex);

								// Zum anderen wird das ElementPaar von Baugruppe
								// aus dem collectBaugruppen Vektor entfernt
								
								//XXX

								// TODO implementieren
								// ListBox-Element, das hinzugefügt wurde, wird für
								// doppeltes Hinzufügen gesperrt
								// listBoxBaugruppen.getElement().setAttribute("enabled",
								// "enabled");

								listBoxBaugruppen.getElement()
										.getElementsByTagName("option")
										.getItem(rowIndex)
										.setAttribute("disabled", "disabled");

							}

						});
					}

				}

			});

			// Horizontales Anordnen von zugehörigen Bauteil-Widgets
					
					
					bgPanel.add(listBoxBaugruppen);
					bgPanel.add(collectBgButton);
					
					//Damit die edit und delete Buttons horizontal angeordnet werden, m�ssen diese folgenden Panels zugeordnet werden
					
					deleteBaugruppePanel.add(deleteBaugruppeLabel);
					deleteBaugruppePanel.add(deleteBaugruppeButton);
						
					this.add(HeadlineLabel);
					this.add(SublineLabel);
					this.add(NameField);
					// this.add(BaugruppeLabel);
					this.add(bgPanel);
					this.add(baugruppeLabel);
					this.add(baugruppeCollection);
					this.add(deleteBaugruppePanel);
					this.add(CreateEnderzeugnisButton);
						
					HeadlineLabel.setStyleName("headline");
					SublineLabel.setStyleName("subline");
					CreateEnderzeugnisButton.setStyleName("Button");
					
					CreateEnderzeugnisButton.addClickHandler(new CreateClickHandler());

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

							
					
				}
				
			
		
		




