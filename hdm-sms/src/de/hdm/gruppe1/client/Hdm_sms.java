package de.hdm.gruppe1.client;

import de.hdm.gruppe1.shared.FieldVerifier;
import de.hdm.gruppe1.shared.Sms;
import de.hdm.gruppe1.shared.SmsAsync;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.RootPanel;

public class Hdm_sms implements EntryPoint {
	
	//Test

	HorizontalPanel menuPanel = new HorizontalPanel();
	Button bauteilBtn = new Button("Bauteile");
	Button baugruppeBtn = new Button("Baugruppen");
	Button enderzeugnisBtn = new Button("Enderzeugnisse");
	Button stuecklistebtn = new Button("Stücklisten");

	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {

		menuPanel.add(bauteilBtn);
		menuPanel.add(baugruppeBtn);
		menuPanel.add(enderzeugnisBtn);
		menuPanel.add(stuecklistebtn);

		bauteilBtn.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				RootPanel.get("content_wrap").clear();
				RootPanel.get("content_wrap").add(new BauteilGeneralView());
				// Window.alert("Platzhalter für Bauteil-GUI");
			}
		});

		baugruppeBtn.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				RootPanel.get("content_wrap").clear();
				// RootPanel.get("content_wrap").add(new XY());
				Window.alert("Platzhalter für Baugruppe-GUI");
			}
		});

		enderzeugnisBtn.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				RootPanel.get("content_wrap").clear();
				// RootPanel.get("content_wrap").add(new XY());
				Window.alert("Platzhalter für Enderzeugnis-GUI");
			}
		});

		stuecklistebtn.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				RootPanel.get("content_wrap").clear();
				// RootPanel.get("content_wrap").add(new XY());
				Window.alert("Platzhalter für Stückliste-GUI");
			}
		});

		RootPanel.get("head_wrap_right").add(menuPanel);
		RootPanel.get("Impressum").add(new Impressum());

	}

}
