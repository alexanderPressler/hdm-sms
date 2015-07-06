package de.hdm.gruppe1.client;

import com.google.appengine.api.users.UserServiceFactory;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

import de.hdm.gruppe1.client.CreateBauteil.CreateBauteilCallback;
import de.hdm.gruppe1.shared.bo.User;

public class Login extends VerticalPanel {
	
	private final Label HeadlineLabel = new Label("Login");
	private final HorizontalPanel loginEditorPanel = new HorizontalPanel();
	private final HorizontalPanel loginReportPanel = new HorizontalPanel();
	private final Label loginEditorLabel = new Label("Editor: ");
	private final Label loginReportLabel = new Label("Report: ");
	private final Button loginEditorBtn = new Button ("Login");
	private final Button loginReportBtn = new Button ("Login");
	
	UserService userService = UserServiceFactory.getUserService();
	com.google.appengine.api.users.User user = userService.getCurrentUser();
	private Anchor signInLink = new Anchor("Anmelden");

	
	public Login(){
		
		loginEditorPanel.add(loginEditorLabel);
		loginEditorPanel.add(loginEditorBtn);
		loginReportPanel.add(loginReportLabel);
		loginReportPanel.add(loginReportBtn);
		
		HeadlineLabel.setStyleName("headline");
		
		loginEditorBtn.addClickHandler(new LoginEditorClickHandler());
		loginReportBtn.addClickHandler(new LoginReportClickHandler());
		
		this.add(HeadlineLabel);
		this.add(loginEditorPanel);
		this.add(loginReportPanel);
		
		RootPanel.get("content_wrap").add(this);
		
	}
	
	private class LoginEditorClickHandler implements ClickHandler {
		@Override
		public void onClick(ClickEvent event) {

			Window.alert("Login Editor erfolgreich.");
			
		}
	}
	
	private class LoginReportClickHandler implements ClickHandler {
		@Override
		public void onClick(ClickEvent event) {

			Window.alert("Login Editor erfolgreich.");
			
		}
	}

}