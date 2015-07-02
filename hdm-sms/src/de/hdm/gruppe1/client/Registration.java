package de.hdm.gruppe1.client;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

import de.hdm.gruppe1.client.CreateBauteil.CreateBauteilCallback;
import de.hdm.gruppe1.shared.SmsAsync;
import de.hdm.gruppe1.shared.bo.User;

public class Registration extends VerticalPanel {
	
	private final Label HeadlineLabel = new Label("Registrierung");
	private final TextBox googleIdBox = new TextBox();
	private final TextBox nicknameBox = new TextBox();
	private final Button registrateButton = new Button("anlegen");
	
	/**
	 * Remote Service via ClientsideSettings wird an dieser Stelle einmalig in
	 * der Klasse aufgerufen. Im Anschluss kann jederzeit darauf zugegriffen
	 * werden.
	 */
	SmsAsync stuecklistenVerwaltung = ClientsideSettings.getSmsVerwaltung();
	
	public Registration(){
		
		//Testweise Befüllen eines Users
		User u = new User();
		u.setGoogleID("123");
		
		googleIdBox.setText("Google-ID: "+u.getGoogleID());
		
		this.add(HeadlineLabel);
		this.add(googleIdBox);
		this.add(nicknameBox);
		this.add(registrateButton);
		
		HeadlineLabel.setStyleName("headline");
		googleIdBox.setReadOnly(true);
		
		/**
		 * TextBoxen werden mit Text vorbefüllt, der ausgeblendet wird, sobald
		 * die TextBox vom User fokussiert wird.
		 */
		nicknameBox.getElement().setPropertyString("placeholder", "Nickname");
		
		registrateButton.addClickHandler(new RegistrateClickHandler());
		
		RootPanel.get("content_wrap").add(this);
		
	}
	
	private class RegistrateClickHandler implements ClickHandler {
		@Override
		public void onClick(ClickEvent event) {

			if(nicknameBox.getText().isEmpty() == true){
				Window.alert("Bitte einen Nickname eintragen.");
			}
			else {
				Window.alert("Nutzer "+nicknameBox.getText()+" erfolgreich angelegt.");
				
				/**
				 * Die konkrete RPC-Methode für den create-Befehl wird
				 * aufgerufen. Hierbei werden die gewünschten Werte
				 * mitgeschickt.
				 */
//				stuecklistenVerwaltung.createUser(googleIdBox.getText(), nicknameBox.getText(), new CreateBauteilCallback());
				
			}
			
		}
	}

}
