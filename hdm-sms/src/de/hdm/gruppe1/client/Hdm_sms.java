package de.hdm.gruppe1.client;

import de.hdm.gruppe1.shared.FieldVerifier;
import de.hdm.gruppe1.shared.LoginInfo;
import de.hdm.gruppe1.shared.LoginService;
import de.hdm.gruppe1.shared.LoginServiceAsync;
import de.hdm.gruppe1.shared.Sms;
import de.hdm.gruppe1.shared.SmsAsync;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class Hdm_sms extends VerticalPanel implements EntryPoint {

	Image welcomeImage = new Image();
	HTML welcomeText = new HTML("<h1>Wilkommen im Editor!</h1>");
	private LoginInfo loginInfo = null;
	  private VerticalPanel loginPanel = new VerticalPanel();
	  private Label loginLabel = new Label(
	      "Please sign in to your Google Account to access the Hdm_SmS application.");
	  private Anchor signInLink = new Anchor("Sign In");

	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {
		// Check login status using login service.
	    LoginServiceAsync loginService = GWT.create(LoginService.class);
	    loginService.getUserInfo(GWT.getHostPageBaseURL(), new AsyncCallback<LoginInfo>() {
	      public void onFailure(Throwable error) {
	      }

	      public void onSuccess(LoginInfo result) {
	        loginInfo = result;
	        if(loginInfo.isLoggedIn()) {
	        	ClientsideSettings.getSmsVerwaltung().setLoginInfo(loginInfo, new LoginInfoCallback());
	          loadMenu();
	        } else {
	          loadLogin();
	        }
	      }
	    });
	}

	  private void loadLogin() {
	    // Assemble login panel.
	    signInLink.setHref(loginInfo.getLoginUrl());
	    loginPanel.add(loginLabel);
	    loginPanel.add(signInLink);
	    RootPanel.get("head_wrap_right").add(loginPanel);
	  }
	  private void loadMenu(){
		//Neu: MenuBar mit Commands (~ClickHandler)
		Command createBauteil = new Command() {
		      public void execute() {
			        RootPanel.get("content_wrap").clear();
			        RootPanel.get("content_wrap").add(new CreateBauteil());
		      }
		};
		
		Command allBauteile = new Command() {
		      public void execute() {
		        RootPanel.get("content_wrap").clear();
		        RootPanel.get("content_wrap").add(new BauteilGeneralView());
		      }
		};
		
		Command createBaugruppe = new Command() {
		      public void execute() {
			        RootPanel.get("content_wrap").clear();
			        RootPanel.get("content_wrap").add(new CreateBaugruppe());
		      }
		};
		
		Command allBaugruppen = new Command() {
		      public void execute() {
		        RootPanel.get("content_wrap").clear();
		        RootPanel.get("content_wrap").add(new BaugruppeGeneralView());
		      }
		};
		
		Command createEnderzeugnis = new Command() {
		      public void execute() {
			        RootPanel.get("content_wrap").clear();
			        RootPanel.get("content_wrap").add(new CreateEnderzeugnis());
		      }
		};
		
		Command allEnderzeugnisse = new Command() {
		      public void execute() {
		        RootPanel.get("content_wrap").clear();
		        RootPanel.get("content_wrap").add(new EnderzeugnisGeneralView());
		      }
		};
		
		Command createStueckliste = new Command() {
		      public void execute() {
		    	  RootPanel.get("content_wrap").clear();
		    	  RootPanel.get("content_wrap").add(new CreateStueckliste());
		      }
		};
		
		Command allStuecklisten = new Command() {
		      public void execute() {
		    	  RootPanel.get("content_wrap").clear();
		    	  RootPanel.get("content_wrap").add(new StuecklisteGeneralView());
		      }
		};
		
		
		//Neu: MenuBar mit Mouse-Over Untermenüs
		
		//Das Menü von Bauteile erhält folgende Mouse-Over Untermenüs
	    MenuBar bauteilMenu = new MenuBar(true);
	    bauteilMenu.addItem("Bauteil anlegen", createBauteil);
	    bauteilMenu.addItem("Alle anzeigen", allBauteile);

	    //Das Menü von Baugruppen erhält folgende Mouse-Over Untermenüs
	    MenuBar baugruppeMenu = new MenuBar(true);
	    baugruppeMenu.addItem("Baugruppe anlegen", createBaugruppe);
	    baugruppeMenu.addItem("Alle anzeigen", allBaugruppen);

	    //Das Menü von Enderzeugnissen erhält folgende Mouse-Over Untermenüs
	    MenuBar enderzeugnisMenu = new MenuBar(true);
	    enderzeugnisMenu.addItem("Enderzeugnis anlegen", createEnderzeugnis);
	    enderzeugnisMenu.addItem("Alle Anzeigen", allEnderzeugnisse);
	    
	    //Das Menü von Stücklisten erhält folgende Mouse-Over Untermenüs
	    MenuBar stuecklisteMenu = new MenuBar(true);
	    stuecklisteMenu.addItem("Stückliste anlegen", createStueckliste);
	    stuecklisteMenu.addItem("Alle Anzeigen", allStuecklisten);
	   

	    //Alle Untermenüs werden hier dem Hauptmenü zugeordnet
	    MenuBar mainMenu = new MenuBar();
	    mainMenu.setWidth("100%");
	    mainMenu.setAutoOpen(true);
	    mainMenu.addItem("Bauteile", bauteilMenu);
	    mainMenu.addItem("Baugruppen", baugruppeMenu);
	    mainMenu.addItem("Enderzeugnisse", enderzeugnisMenu);
	    mainMenu.addItem("Stücklisten", stuecklisteMenu);
	    
	    //Der Default-Text, der beim Aufruf der Applikation angezeigt wird
	    
	    //Das Begrüßungsbild der Applikation
		welcomeImage.setUrl("./img/Welcome.jpg");
	    welcomeImage.setStyleName("initialPicture");
	    
	    Anchor signOutLink = new Anchor("signout");
		// Assemble Main panel
	    signOutLink.setHref(loginInfo.getLogoutUrl());
	    

		    
	    //Hautpmenü schließlich dem RootPanel in den Menü-div Container zuordnen
	    RootPanel.get("head_wrap_right").add(mainMenu);
	    RootPanel.get("head_wrap_right").add(signOutLink);
	    RootPanel.get("content_wrap").add(welcomeImage);
	    RootPanel.get("content_wrap").add(welcomeText);
		RootPanel.get("Impressum").add(new Impressum());

	}
	  
class LoginInfoCallback implements AsyncCallback<Void>{

	@Override
	public void onFailure(Throwable caught) {
		loginInfo.setLoggedIn(false);
	}

	@Override
	public void onSuccess(Void result) {
		// TODO Auto-generated method stub
		
	}
	
}

}