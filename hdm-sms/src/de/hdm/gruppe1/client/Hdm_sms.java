<<<<<<< HEAD
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

	HorizontalPanel menuPanel = new HorizontalPanel();
	Button bauteilBtn = new Button("Bauteile");
	Button baugruppeBtn = new Button("Baugruppen");
	Button enderzeugnisBtn = new Button("Enderzeugnisse");
	Button stuecklistebtn = new Button("St�cklisten");

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
				// Window.alert("Platzhalter f�r Bauteil-GUI");
			}
		});

		baugruppeBtn.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				RootPanel.get("content_wrap").clear();
				// RootPanel.get("content_wrap").add(new XY());
				Window.alert("Platzhalter f�r Baugruppe-GUI");
			}
		});

		enderzeugnisBtn.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				RootPanel.get("content_wrap").clear();
				// RootPanel.get("content_wrap").add(new XY());
				Window.alert("Platzhalter f�r Enderzeugnis-GUI");
			}
		});

		stuecklistebtn.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				RootPanel.get("content_wrap").clear();
				// RootPanel.get("content_wrap").add(new XY());
				Window.alert("Platzhalter f�r St�ckliste-GUI");
			}
		});

		RootPanel.get("head_wrap_right").add(menuPanel);

	}

}
=======
package de.hdm.gruppe1.client;

import de.hdm.gruppe1.shared.FieldVerifier;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * Das ist ein Testeidu test
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Hdm_sms implements EntryPoint {
	/**
	 * The message displayed to the user when the server cannot be reached or
	 * returns an error.
	 */
	private static final String SERVER_ERROR = "An error occurred while "
			+ "attempting to contact the server. Please check your network "
			+ "connection and try again.";

	/**
	 * Create a remote service proxy to talk to the server-side Greeting service.
	 */
	private final GreetingServiceAsync greetingService = GWT
			.create(GreetingService.class);

	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {
		final Button sendButton = new Button("Send");
		final TextBox nameField = new TextBox();
		nameField.setText("GWT User");
		final Label errorLabel = new Label();

		// We can add style names to widgets
		sendButton.addStyleName("sendButton");

		// Add the nameField and sendButton to the RootPanel
		// Use RootPanel.get() to get the entire body element
		RootPanel.get("nameFieldContainer").add(nameField);
		RootPanel.get("sendButtonContainer").add(sendButton);
		RootPanel.get("errorLabelContainer").add(errorLabel);

		// Focus the cursor on the name field when the app loads
		nameField.setFocus(true);
		nameField.selectAll();

		// Create the popup dialog box
		final DialogBox dialogBox = new DialogBox();
		dialogBox.setText("Remote Procedure Call");
		dialogBox.setAnimationEnabled(true);
		final Button closeButton = new Button("Close");
		// We can set the id of a widget by accessing its Element
		closeButton.getElement().setId("closeButton");
		final Label textToServerLabel = new Label();
		final HTML serverResponseLabel = new HTML();
		VerticalPanel dialogVPanel = new VerticalPanel();
		dialogVPanel.addStyleName("dialogVPanel");
		dialogVPanel.add(new HTML("<b>Sending name to the server:</b>"));
		dialogVPanel.add(textToServerLabel);
		dialogVPanel.add(new HTML("<br><b>Server replies:</b>"));
		dialogVPanel.add(serverResponseLabel);
		dialogVPanel.setHorizontalAlignment(VerticalPanel.ALIGN_RIGHT);
		dialogVPanel.add(closeButton);
		dialogBox.setWidget(dialogVPanel);

		// Add a handler to close the DialogBox
		closeButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				dialogBox.hide();
				sendButton.setEnabled(true);
				sendButton.setFocus(true);
			}
		});

		// Create a handler for the sendButton and nameField
		class MyHandler implements ClickHandler, KeyUpHandler {
			/**
			 * Fired when the user clicks on the sendButton.
			 */
			public void onClick(ClickEvent event) {
				sendNameToServer();
			}

			/**
			 * Fired when the user types in the nameField.
			 */
			public void onKeyUp(KeyUpEvent event) {
				if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
					sendNameToServer();
				}
			}

			/**
			 * Send the name from the nameField to the server and wait for a response.
			 */
			private void sendNameToServer() {
				// First, we validate the input.
				errorLabel.setText("");
				String textToServer = nameField.getText();
				if (!FieldVerifier.isValidName(textToServer)) {
					errorLabel.setText("Please enter at least four characters");
					return;
				}

				// Then, we send the input to the server.
				sendButton.setEnabled(false);
				textToServerLabel.setText(textToServer);
				serverResponseLabel.setText("");
				greetingService.greetServer(textToServer,
						new AsyncCallback<String>() {
							public void onFailure(Throwable caught) {
								// Show the RPC error message to the user
								dialogBox
										.setText("Remote Procedure Call - Failure");
								serverResponseLabel
										.addStyleName("serverResponseLabelError");
								serverResponseLabel.setHTML(SERVER_ERROR);
								dialogBox.center();
								closeButton.setFocus(true);
							}

							public void onSuccess(String result) {
								dialogBox.setText("Remote Procedure Call");
								serverResponseLabel
										.removeStyleName("serverResponseLabelError");
								serverResponseLabel.setHTML(result);
								dialogBox.center();
								closeButton.setFocus(true);
							}
						});
			}
		}

		// Add a handler to send the name to the server
		MyHandler handler = new MyHandler();
		sendButton.addClickHandler(handler);
		nameField.addKeyUpHandler(handler);
	}
}
>>>>>>> branch 'Galina' of https://github.com/alexanderPressler/hdm-sms.git
