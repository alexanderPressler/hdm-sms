package de.hdm.gruppe1.client;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import de.hdm.gruppe1.client.EditBauteil.SaveCallback;
import de.hdm.gruppe1.shared.SmsAsync;
import de.hdm.gruppe1.shared.bo.Bauteil;

//Die Klasse EditStueckliste liefert alle benötigten Elemente, um eine bestehende Stückliste im System zu ändern.
public class EditStueckliste extends VerticalPanel {
	
	//Elemente für EditStueckliste initialisieren
	private final Label HeadlineLabel = new Label ("Stückliste ändern");
	private final Label SublineLabel = new Label ("Um eine Stückliste zu ändern, füllen Sie bitte alle Felder aus und bestätigen mit dem <editieren>-Button ihre Eingabe.");
	private final Label NameFieldLabel = new Label ("Bezeichnung");
	private final TextBox NameField = new TextBox ();
	private final Button EditStuecklisteButton = new Button ("ändern");
			
	// Remote Service via ClientsideSettings
	SmsAsync stuecklistenVerwaltung = ClientsideSettings.getSmsVerwaltung();
	
	//TODO implementieren
//	public EditStueckliste (Stueckliste editStueckliste) {
//		
//		this.add(HeadlineLabel);
//		this.add(SublineLabel);
//		this.add(NameFieldLabel);
//		this.add(NameField);
//		this.add(EditStuecklisteButton);
//		
//		EditStuecklisteButton.setStyleName("Button");
//		
//		EditStuecklisteButton.addClickHandler(new EditClickHandler());
//		
//		NameField.setText(editStueckliste.getName());
//		
//		RootPanel.get("content_wrap").add(this);
//		
//	}
//	
	/*
	 * Click Handlers.
	 */
	
	/**
	  * Das Ändern einer Stückliste ruft die Service-Methode "edit" auf.
	  * 
	  */
	
	//TODO implementieren
//	 private class EditClickHandler implements ClickHandler {
//	  @Override
//	  public void onClick(ClickEvent event) {
//
////		  if(){
////			  
////		  }
//		  
//		  Stueckliste s = new Stueckliste();
//		  s.setName(NameField.getText());
//		  
//		  stuecklistenVerwaltung.save(s, new SaveCallback());
//			
//		  RootPanel.get("content_wrap").clear();
//		  RootPanel.get("content_wrap").add(new StuecklisteGeneralView());
//		   
//	  }
//	 }
//	 
	 class SaveCallback implements AsyncCallback<Void> {

		 @Override
		 public void onFailure(Throwable caught) {
			 Window.alert("Die Stückliste wurde nicht editiert.");
			}

			@Override
			public void onSuccess(Void result) {
				Window.alert("Die Stückliste wurde erfolgreich editiert.");
				
				
				
			}
		}

}
