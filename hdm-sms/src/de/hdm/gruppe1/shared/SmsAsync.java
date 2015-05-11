package de.hdm.gruppe1.shared;

import java.util.Vector;

import com.google.gwt.user.client.rpc.AsyncCallback;

import de.hdm.gruppe1.shared.bo.Bauteil;
import de.hdm.gruppe1.shared.bo.Baugruppe;
import de.hdm.gruppe1.shared.bo.Stueckliste;

/**
 * Das asynchrone Gegenstück des Interface {@link BankAdministration}. Es wird
 * semiautomatisch durch das Google Plugin erstellt und gepflegt. Daher erfolgt
 * hier keine weitere Dokumentation. Für weitere Informationen siehe das
 * synchrone Interface {@link BankAdministration}.
 * 
 * @author thies
 */
public interface SmsAsync {
	
	void init(AsyncCallback<Void> callback);
	
	
	void createBauteil(String name, String bauteilBeschreibung,
			String materialBeschreibung, AsyncCallback<Bauteil> callback);

	void save(Bauteil b, AsyncCallback<Void> callback);

	void delete(Bauteil b, AsyncCallback<Void> callback);

	void getAllBauteile(AsyncCallback<Vector<Bauteil>> callback);
	
	void getBauteilById(int id, AsyncCallback<Bauteil> callback);

	
	
	void createBaugruppe(String name, AsyncCallback<Baugruppe> callback);
	
	void saveBaugruppe(Baugruppe bg, AsyncCallback<Void> callback);
	
	void deleteBaugruppe(Baugruppe bg, AsyncCallback<Void> callback);
	
	void getAllBaugruppen(AsyncCallback<Vector<Baugruppe>> callback);
	
	void getBaugruppeById(int id, AsyncCallback<Baugruppe> callback);

	// void getBaugruppeByName(String name, AsyncCallback<Baugruppe> callback);

	
	
	void createStueckliste(String name, AsyncCallback<Stueckliste> callback);

	void saveStueckliste(Stueckliste s, AsyncCallback<Void> callback);

	void deleteStueckliste(Stueckliste s, AsyncCallback<Void> callback);

	void getAllStuecklisten(AsyncCallback<Vector<Stueckliste>> callback);

	void getStuecklisteById(int id, AsyncCallback<Stueckliste> callback);
	
}