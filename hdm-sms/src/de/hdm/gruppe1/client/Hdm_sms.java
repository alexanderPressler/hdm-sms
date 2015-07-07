package de.hdm.gruppe1.client;

import de.hdm.gruppe1.shared.LoginInfo;
import de.hdm.gruppe1.shared.LoginService;
import de.hdm.gruppe1.shared.LoginServiceAsync;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * Diese Klasse implementiert den EntryPoint für das Modul "Editor" der Applikation.
 * Hier wird sowohl der Login, als auch das Menü im HTML-div "head_wrap" initialisiert.
 * 
 * @author Mario Theiler, Katja Thiere
 *
 */
public class Hdm_sms extends VerticalPanel implements EntryPoint {

	/**
	 * Das Initiale Bild beim Aufruf der Applikation und die Überschrift in Form von HTML-h1-Tags werden an dieser
	 * Stelle initialisiert.
	 */
	Image welcomeImage = new Image();
	HTML welcomeText = new HTML("<h1>Wilkommen im Editor!</h1>");
	
	/**
	 * Mithilfe dieses Abschnitts wird eine User-Identifikation mithilfe eines Google-Logins ermöglicht.
	 */
	private LoginInfo loginInfo = null;
	  private VerticalPanel loginPanel = new VerticalPanel();
	  private Label loginLabel = new Label(
	      "Please sign in to your Google Account to access the Hdm_SmS application.");
	  private Anchor signInLink = new Anchor("Sign In");

	/**
	 * Die onModuleLoad-Methode wird initial bei Aufruf des Editor-Moduls aufgerufen.
	 */
	public void onModuleLoad() {
	
		/**
		 *  Check login status using login service (Autor: Google Dokumentation).
		*/
	    LoginServiceAsync loginService = GWT.create(LoginService.class);
	    loginService.getUserInfo(GWT.getHostPageBaseURL(), new AsyncCallback<LoginInfo>() {
	      public void onFailure(Throwable error) {
	      }

	      public void onSuccess(LoginInfo result) {
	        loginInfo = result;
	        if(loginInfo.isLoggedIn()) {
	        	
	        	/**
	        	 * Sofern der User bereits eingeloggt ist, wird ihm das Menü angezeigt.
	        	 */
	        	ClientsideSettings.getSmsVerwaltung().setLoginInfo(loginInfo, new LoginInfoCallback());
	          loadMenu();
	        } else {
	        	
	        /**
	         * Sofern der User noch nicht eingeloggt ist, wird die Login-Methode aufgerufen.
	         */
	          loadLogin();
	        }
	      }
	    });
	}

	/**
	 * Die Login-Methode setzt unter anderem das GUI-Element in den div-Container "head_wrap_right".
	 */
	  private void loadLogin() 
	  
	  /**
	   * Bei erfolgreichem Login wird folgende Methode geladen und dem Benutzer das Menü angezeigt.
	   */
	  private void loadMenu(){
		
		/**
		 * Die folgenden Commands definieren, was beim jeweiligen Aufruf der Menü-Punkte passieren soll.
		 */
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
		
		/**
		* Das Menü von Bauteile erhält folgende Mouse-Over Untermenüs.
		*/
	    MenuBar bauteilMenu = new MenuBar(true);
	    bauteilMenu.addItem("Bauteil anlegen", createBauteil);
	    bauteilMenu.addItem("Alle anzeigen", allBauteile);

	    /**
	     * Das Menü von Baugruppen erhält folgende Mouse-Over Untermenüs.
	     */
	    MenuBar baugruppeMenu = new MenuBar(true);
	    baugruppeMenu.addItem("Baugruppe anlegen", createBaugruppe);
	    baugruppeMenu.addItem("Alle anzeigen", allBaugruppen);

	    /**
	     * Das Menü von Enderzeugnissen erhält folgende Mouse-Over Untermenüs.
	     */
	    MenuBar enderzeugnisMenu = new MenuBar(true);
	    enderzeugnisMenu.addItem("Enderzeugnis anlegen", createEnderzeugnis);
	    enderzeugnisMenu.addItem("Alle Anzeigen", allEnderzeugnisse);
	    
	    /**
	     * Das Menü von Stücklisten erhält folgende Mouse-Over Untermenüs.
	     */
	    MenuBar stuecklisteMenu = new MenuBar(true);
	    stuecklisteMenu.addItem("Stückliste anlegen", createStueckliste);
	    stuecklisteMenu.addItem("Alle Anzeigen", allStuecklisten);

	    /**
	     * Die Untermenüs werden hier dem Hauptmenü zugeordnet.
	     */
	    MenuBar mainMenu = new MenuBar();
	    mainMenu.setWidth("100%");
	    mainMenu.setAutoOpen(true);
	    mainMenu.addItem("Bauteile", bauteilMenu);
	    mainMenu.addItem("Baugruppen", baugruppeMenu);
	    mainMenu.addItem("Enderzeugnisse", enderzeugnisMenu);
	    mainMenu.addItem("Stücklisten", stuecklisteMenu);
	    
	    /**
	     * Das Begrüßungsbild der Applikation wird hier gezogen und bekommt einen css-StyleName.
	     */
		welcomeImage.setUrl("./img/Welcome.jpg");
	    welcomeImage.setStyleName("initialPicture");
	    
	  /**
	  * Assemble signOutLink (Autor: Google Dokumentation)
	  */
	    Anchor signOutLink = new Anchor("signout");
	    signOutLink.setHref(loginInfo.getLogoutUrl());
		    
	    /**
	     * Das Hautpmenü wird schließlich dem RootPanel in den Menü-div Container zugeordnet.
	     */
	    RootPanel.get("head_wrap_right").add(mainMenu);
	    RootPanel.get("head_wrap_right").add(signOutLink);
	    RootPanel.get("content_wrap").add(welcomeImage);
	    RootPanel.get("content_wrap").add(welcomeText);
		RootPanel.get("Impressum").add(new Impressum());

	}

	  /**
	   * Der LoginInfoCallback ermöglicht über die Impl dieser Applikation eine Kommunikation mit den Google-Login
	   * Servern.
	   * 
	   * @author Mario Theiler
	   *
	   */	  
class LoginInfoCallback implements AsyncCallback<Void>{

	@Override
	public void onFailure(Throwable caught) {
		loginInfo.setLoggedIn(false);
	}

	@Override
	public void onSuccess(Void result) {
		
	}
	
}

}
