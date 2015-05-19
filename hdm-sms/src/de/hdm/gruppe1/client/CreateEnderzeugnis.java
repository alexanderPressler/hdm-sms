package de.hdm.gruppe1.client;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

import de.hdm.gruppe1.client.ClientsideSettings;
import de.hdm.gruppe1.client.CreateEnderzeugnis.CreateEnderzeugnisCallback;
import de.hdm.gruppe1.client.CreateEnderzeugnis.CreateClickHandler;
import de.hdm.gruppe1.shared.SmsAsync;
import de.hdm.gruppe1.shared.bo.Enderzeugnis;

//Die Klasse CreateEnderzeugnis liefert alle benötigten Elemente, um ein neues Enderzeugnis im System anzulegen.
public class CreateEnderzeugnis extends VerticalPanel {
	
	//Elemente für CreateEnderzeugnis initialisieren
		private final Label HeadlineLabel = new Label ("Enderzeugnis anlegen");
		private final Label SublineLabel = new Label ("Um ein Enderzeugnis anzulegen, füllen Sie bitte alle Felder aus und bestätigen mit dem <anlegen>-Button ihre Eingabe.");
		private final Label NameFieldLabel = new Label ("Name");
		private final TextBox NameField = new TextBox ();
		private final Button CreateEnderzeugnisButton = new Button ("anlegen");
		
		
		// Remote Service via ClientsideSettings
		SmsAsync stuecklistenVerwaltung = ClientsideSettings.getSmsVerwaltung();
		
		public CreateEnderzeugnis(){

			this.add(HeadlineLabel);
			this.add(SublineLabel);
			this.add(NameFieldLabel);
			this.add(NameField);
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
		 * Die Anlage eines Enderzeugnisses. 
		 * Es erfolgt der Aufruf der Service-Methode "create".
		 */
		private class CreateClickHandler implements ClickHandler {
			@Override
			public void onClick(ClickEvent event) {

				String name = NameField.getText();
				

				stuecklistenVerwaltung.createEnderzeugnis(name, new CreateEnderzeugnisCallback());
				
				 RootPanel.get("content_wrap").clear();
				 RootPanel.get("content_wrap").add(new EnderzeugnisGeneralView());
				 
			}
		}
		
		class CreateEnderzeugnisCallback implements AsyncCallback<Enderzeugnis> {

			@Override
			public void onFailure(Throwable caught) {
				Window.alert("Das Anlegen des Enderzeugnisses ist fehlgeschlagen!");
			}

			@Override
			public void onSuccess(Enderzeugnis enderzeugnis) {

				Window.alert("Das Enderzeugnis wurde erfolgreich angelegt.");
		
				// }
			}
		}

	}



