package de.hdm.gruppe1.shared;

import java.util.ArrayList;
import java.util.Vector;

import com.google.gwt.user.client.rpc.AsyncCallback;

import de.hdm.gruppe1.shared.bo.Baugruppe;
import de.hdm.gruppe1.shared.bo.Bauteil;
import de.hdm.gruppe1.shared.bo.Element;

/**
 * Das asynchrone Gegenstueck des Interface {@link Sms}. Es wird
 * semiautomatisch durch das Google Plugin erstellt und gepflegt. Daher erfolgt
 * hier keine weitere Dokumentation. Fuer weitere Informationen siehe das
 * synchrone Interface {@link Sms}.
 * 
 * @author thies& Schmidt
 */
public interface SmsAsync {
	
	void createBauteil(String bauteilBeschreibung,
			String materialBeschreibung, AsyncCallback<Bauteil> callback);

	void save(Bauteil b, AsyncCallback<Bauteil> callback);

	void delete(Bauteil b, AsyncCallback<Bauteil> callback);

	void init(AsyncCallback<Bauteil> callback);

	void getAllBauteile(AsyncCallback<Vector<Bauteil>> callback);
	
	
	
	
	
	void createBaugruppe(String name, ArrayList<Element> element,
			AsyncCallback<Baugruppe> callback);
	
	void editBaugruppe(Baugruppe baugruppe, AsyncCallback<Baugruppe> callback);
	
	void deleteBaugruppe(Baugruppe baugruppe, AsyncCallback<Baugruppe> callback);

	void getBaugruppeByName(String name, AsyncCallback<Baugruppe> callback);

	void getBaugruppeById(int id, AsyncCallback<Baugruppe> callback);

	void getAllBaugruppen(ArrayList<Baugruppe> baugruppe,
			AsyncCallback<Baugruppe> callback);
	
}