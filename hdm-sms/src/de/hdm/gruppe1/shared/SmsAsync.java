package de.hdm.gruppe1.shared;

import java.util.Vector;

import com.google.gwt.user.client.rpc.AsyncCallback;

import de.hdm.gruppe1.shared.bo.Baugruppe;
import de.hdm.gruppe1.shared.bo.Bauteil;
import de.hdm.gruppe1.shared.bo.ElementPaar;
import de.hdm.gruppe1.shared.bo.Enderzeugnis;
import de.hdm.gruppe1.shared.bo.Stueckliste;
import de.hdm.gruppe1.shared.bo.User;

/**
 * Das asynchrone Gegenstück des Interface {@link BankAdministration}. Es wird
 * semiautomatisch durch das Google Plugin erstellt und gepflegt. Daher erfolgt
 * hier keine weitere Dokumentation. Für weitere Informationen siehe das
 * synchrone Interface {@link Sms}.
 * 
 * @author Thies, Schmidt & Pressler
 */
public interface SmsAsync {
	
	void createBauteil(String name, String bauteilBeschreibung,
			String materialBeschreibung, AsyncCallback<Bauteil> callback);

	void save(Bauteil b, AsyncCallback<Void> callback);

	void delete(Bauteil b, AsyncCallback<Void> callback);

	void init(AsyncCallback<Void> callback);

	void getAllBauteile(AsyncCallback<Vector<Bauteil>> callback);

	void getBauteilById(int id, AsyncCallback<Bauteil> callback);

	void createStueckliste(String name, Vector<ElementPaar> BauteilPaare,
			Vector<ElementPaar> BaugruppenPaare,
			AsyncCallback<Stueckliste> callback);

	void getAllStuecklisten(AsyncCallback<Vector<Stueckliste>> callback);

	void deleteStueckliste(Stueckliste s, AsyncCallback<Void> callback);

	void saveStueckliste(Stueckliste s, AsyncCallback<Void> callback);

	void createUser(String googleID, String name, AsyncCallback<User> callback);

	void createBaugruppe(String name, Vector<ElementPaar> BauteilPaare,
			Vector<ElementPaar> BaugruppenPaare,
			AsyncCallback<Baugruppe> callback);

	void deleteBaugruppe(Baugruppe b, AsyncCallback<Void> callback);

	void saveBaugruppe(Baugruppe b, AsyncCallback<Void> callback);

	void getAllBaugruppen(AsyncCallback<Vector<Baugruppe>> callback);

	void createEnderzeugnis(String name, Baugruppe baugruppe,
			AsyncCallback<Enderzeugnis> callback);

	void deleteEnderzeugnis(Enderzeugnis e, AsyncCallback<Void> callback);

	void saveEnderzeugnis(Enderzeugnis e, AsyncCallback<Void> callback);

	void getAllEnderzeugnis(AsyncCallback<Vector<Enderzeugnis>> callback);

	void getBaugruppeById(int id, AsyncCallback<Baugruppe> callback);
	void setLoginInfo (LoginInfo loginInfo, AsyncCallback<Void> callback);
}
