package de.hdm.gruppe1.client;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
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
		private final Label HeadlineLabel = new Label ("Stueckliste anlegen");
		private final Label SublineLabel = new Label ("Um eine Stückliste anzulegen, füllen Sie bitte alle Felder aus und bestätigen mit dem <anlegen>-Button ihre Eingabe.");
		private final Label NameFieldLabel = new Label ("Name");
		private final TextBox NameField = new TextBox ();
		private final Label BauteilLabel = new Label ("Bauteile hinzufügen");
		ListBox listBoxBauteile = new ListBox();
		private final Label BaugruppeLabel= new Label ("Baugruppen/Enderzeugnisse hinzufügen");
		ListBox listBoxBaugruppen = new ListBox();
		private final Button CreateStuecklisteButton = new Button ("anlegen");
		
		// Remote Service via ClientsideSettings
		SmsAsync stuecklistenVerwaltung = ClientsideSettings.getSmsVerwaltung();
		
		public CreateStueckliste(){

			//Da RPC-Methoden noch nicht implementiert sind, wird hier beispielhaft das Dropdown manuell befüllt
			listBoxBauteile.addItem("Bt1");
			listBoxBauteile.addItem("Bt2");
			listBoxBauteile.addItem("Bt3");
			listBoxBauteile.addItem("Bt4");
			listBoxBauteile.addItem("Bt5");
			
			listBoxBaugruppen.addItem("Bg1");
			listBoxBaugruppen.addItem("Bg2");
			listBoxBaugruppen.addItem("Bg3");
			listBoxBaugruppen.addItem("Bg4");
			listBoxBaugruppen.addItem("Bg5");
			
			this.add(HeadlineLabel);
			this.add(SublineLabel);
			this.add(NameFieldLabel);
			this.add(NameField);
			this.add(BauteilLabel);
			this.add(listBoxBauteile);
			this.add(BaugruppeLabel);
			this.add(listBoxBaugruppen);
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
//					stuecklistenVerwaltung.createStueckliste(name, new StuecklisteCallback());
					
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
