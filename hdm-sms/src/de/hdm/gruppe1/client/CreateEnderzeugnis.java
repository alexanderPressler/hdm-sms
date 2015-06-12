package de.hdm.gruppe1.client;

import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * Die Klasse CreateEnderzeugnis ermöglicht dem User, Objekte von Baugruppen in
 * der Datenbank als Enderzeugnisse mit entsprechender Referenz zueinander abzuspeichern.
 * 
 * @author Mario Theiler
 * @version 1.0
 */
public class CreateEnderzeugnis extends VerticalPanel {
	
	/**
	 * GUI-Elemente für CreateStueckliste initialisieren
	 */
	private final Label HeadlineLabel = new Label("Enderzeugnis anlegen");
	private final Label SublineLabel = new Label(
			"Um ein Enderzeugnis anzulegen, wählen Sie bitte eine zugehörige Baugruppe aus. Außerdem müssen Sie " +
			"dem Enderzeugnis einen Namen vergeben.");
	private final Label nameLabel = new Label("Name eintragen");
	private final TextBox NameField = new TextBox();
	private final Label BaugruppeLabel = new Label("Gewünschte Baugruppe hinzufügen");
	ListBox listBoxBaugruppen = new ListBox();
	
	CreateEnderzeugnis() {
		
	}

}
