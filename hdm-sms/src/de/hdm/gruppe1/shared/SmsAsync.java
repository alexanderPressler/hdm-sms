package de.hdm.gruppe1.shared;

import java.util.ArrayList;
import java.util.Vector;

import com.google.gwt.user.client.rpc.AsyncCallback;
import de.hdm.gruppe1.shared.bo.User;
import de.hdm.gruppe1.shared.bo.Baugruppe;
import de.hdm.gruppe1.shared.bo.Bauteil;
import de.hdm.gruppe1.shared.bo.Element;
import de.hdm.gruppe1.shared.bo.Stueckliste;
import de.hdm.gruppe1.shared.bo.ElementPaar;


/**
 * Das asynchrone Gegenstueck des Interface {@link Sms}. Es wird
 * semiautomatisch durch das Google Plugin erstellt und gepflegt. Daher erfolgt
 * hier keine weitere Dokumentation. Fuer weitere Informationen siehe das
 * synchrone Interface {@link Sms}.
 * 
 * @author thies& Schmidt
 */
public interface SmsAsync {
	void init(AsyncCallback<Void> callback);
	
	
	void createBauteil(String name, String bauteilBeschreibung,
			String materialBeschreibung, AsyncCallback<Bauteil> callback);

	void save(Bauteil b, AsyncCallback<Void> callback);

	void delete(Bauteil b, AsyncCallback<Void> callback);

	void getBauteilById(int id, AsyncCallback<Bauteil> callback);

	void getAllBauteile(AsyncCallback<Vector<Bauteil>> callback);

	/*
	 * Stuecklisten Async
	 */

	void createStueckliste(String name, Vector<ElementPaar> BauteilPaare,
			Vector<ElementPaar> BaugruppenPaare,
			AsyncCallback<Stueckliste> callback);

	void getAllStuecklisten(AsyncCallback<Vector<Stueckliste>> callback);

	void deleteStueckliste(Stueckliste s, AsyncCallback<Void> callback);

	void saveStueckliste(Stueckliste s, AsyncCallback<Void> callback);

	
	/*
	 * Baugruppen Async
	 */
	
	void createBaugruppe(String name, Stueckliste stueckliste,
			AsyncCallback<Baugruppe> callback);

	void editBaugruppe(Baugruppe baugruppe, AsyncCallback<Void> callback);
	
	void delete(Baugruppe baugruppe, AsyncCallback<Void> callback);

	void getBaugruppeByName(String name,
			AsyncCallback<Vector<Baugruppe>> callback);
	void getBaugruppeById(int id, AsyncCallback<Baugruppe> callback);
	void getAllBaugruppen(AsyncCallback<Vector<Baugruppe>> callback);
	
	



	

}
