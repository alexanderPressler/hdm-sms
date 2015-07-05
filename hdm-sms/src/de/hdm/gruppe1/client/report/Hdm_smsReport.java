package de.hdm.gruppe1.client.report;

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

import de.hdm.gruppe1.client.Impressum;
import de.hdm.gruppe1.shared.report.LoginInfo;
import de.hdm.gruppe1.shared.report.LoginService;
import de.hdm.gruppe1.shared.report.LoginServiceAsync;

public class Hdm_smsReport extends VerticalPanel implements EntryPoint {

	Image welcomeImage = new Image();
	HTML welcomeText = new HTML("<h1>Wilkommen im Report-Generator!</h1>");
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

		/**
		 * Kommandos für die MenuBar
		 */
		Command report1 = new Command() {
		      public void execute() {
		    	  RootPanel.get("content_wrap").clear();
		    	  RootPanel.get("content_wrap").add(new Strukturstuecklisten());
		      }
		};
		
		Command report2 = new Command() {
		      public void execute() {
		    	  RootPanel.get("content_wrap").clear();
		    	  RootPanel.get("content_wrap").add(new Materialbedarf());
		      }
		};
		
//	    //Menü für Report 1
//	    MenuBar report1Menu = new MenuBar(true);
//	    report1Menu.addItem("Strukturstücklisten", report1);
//	    
//	    //Menü für Report 2
//	    MenuBar report2Menu = new MenuBar(true);
//	    report2Menu.addItem("Materialbedarf", report2);

		//Alle Untermenüs werden hier dem Hauptmenü zugeordnet
	    MenuBar mainMenu = new MenuBar();
	    mainMenu.setWidth("100%");
//	    mainMenu.setAutoOpen(true);
	    mainMenu.addItem("Strukturstücklisten", report1);
	    mainMenu.addItem("Materialbedarf", report2);
	    
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

}