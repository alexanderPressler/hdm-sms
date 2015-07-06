package de.hdm.gruppe1.shared;

import java.util.Vector;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import de.hdm.gruppe1.shared.bo.Baugruppe;
import de.hdm.gruppe1.shared.bo.Bauteil;
import de.hdm.gruppe1.shared.bo.ElementPaar;
import de.hdm.gruppe1.shared.bo.Enderzeugnis;
import de.hdm.gruppe1.shared.bo.Stueckliste;
import de.hdm.gruppe1.shared.bo.User;

/**
 * <p>
 * Synchrone Schnittstelle für eine RPC-fähige Klasse zur Ausgabe des Reports.
 * </p>
 * <p>
 * <b>Frage:</b> Warum werden diese Methoden nicht als Teil der Klassen
 * {@link Stueckliste}, {@link User}, {@link Baugruppe} oder {@link Bauteil}
 * implementiert?<br>
 * <b>Antwort:</b> Z.B. das Löschen eines Kunden erfordert Kenntnisse über die
 * Verflechtung eines Kunden mit Konto-Objekten. Um die Klasse <code>Bank</code>
 * bzw. <code>Customer</code> nicht zu stark an andere Klassen zu koppeln, wird
 * das Wissen darüber, wie einzelne "Daten"-Objekte koexistieren, in der
 * vorliegenden Klasse gekapselt.
 * </p>
 * <p>
 * Natürlich existieren Frameworks wie etwa Hibernate, die dies auf eine andere
 * Weise realisieren. Wir haben jedoch ganz bewusst auf deren Nutzung
 * verzichtet, um in diesem kleinen Demoprojekt den Blick auf das Wesentliche
 * nicht unnötig zu verstellen.
 * </p>
 * <p>
 * <code>@RemoteServiceRelativePath("bankadministration")</code> ist bei der
 * Adressierung des aus der zugehörigen Impl-Klasse entstehenden
 * Servlet-Kompilats behilflich. Es gibt im Wesentlichen einen Teil der URL des
 * Servlets an.
 * </p>
 * 
 * @author Thies, Schmidt & Pressler
 */
@RemoteServiceRelativePath("sms")
public interface Sms extends RemoteService {

	/**
	 * Initialisierung des Objekts. Diese Methode ist vor dem Hintergrund von
	 * GWT RPC zusätzlich zum No Argument Constructor der implementierenden
	 * Klasse {@link BankVerwaltungImpl} notwendig. Bitte diese Methode direkt
	 * nach der Instantiierung aufrufen.
	 * 
	 * @throws IllegalArgumentException
	 */
	public void init() throws IllegalArgumentException;

	/**
	 * Ein Bauteil anlegen.
	 * 
	 * @param bauteilBeschreibung
	 *            BauteilBeschreibung
	 * @param materialBeschreibung
	 *            MaterialBeschreibung
	 * @return Ein fertiges Kunden-Objekt.
	 * @throws IllegalArgumentException
	 * @throws DuplicateBauteilException
	 */
	Bauteil createBauteil(String name, String bauteilBeschreibung,
			String materialBeschreibung) throws IllegalArgumentException,
			DuplicateBauteilException;

	public void save(Bauteil b) throws IllegalArgumentException,
			DuplicateBauteilException;

	/**
	 * Löschen des übergebenen Bauteils.
	 * 
	 * @param b
	 *            der zu löschende Bauteil
	 * @throws IllegalArgumentException
	 */
	void delete(Bauteil b) throws IllegalArgumentException;

	Vector<Bauteil> getAllBauteile() throws IllegalArgumentException;

	Bauteil getBauteilById(int id) throws IllegalArgumentException;

	Stueckliste createStueckliste(String name,
			Vector<ElementPaar> BauteilPaare,
			Vector<ElementPaar> BaugruppenPaare)
			throws IllegalArgumentException, DuplicateStuecklisteException;

	Vector<Stueckliste> getAllStuecklisten() throws IllegalArgumentException;

	void deleteStueckliste(Stueckliste s) throws IllegalArgumentException;

	void saveStueckliste(Stueckliste s) throws IllegalArgumentException,
			DuplicateStuecklisteException;

	User createUser(String googleID, String name)
			throws IllegalArgumentException;

	Baugruppe createBaugruppe(String name, Vector<ElementPaar> BauteilPaare,
			Vector<ElementPaar> BaugruppenPaare)
			throws IllegalArgumentException, DuplicateBaugruppeException;

	void deleteBaugruppe(Baugruppe b) throws IllegalArgumentException;

	void saveBaugruppe(Baugruppe b) throws IllegalArgumentException,
			BaugruppenReferenceException, DuplicateBaugruppeException;

	Vector<Baugruppe> getAllBaugruppen() throws IllegalArgumentException;

	Enderzeugnis createEnderzeugnis(String name, Baugruppe baugruppe)
			throws IllegalArgumentException, DuplicateEnderzeugnisException;

	void deleteEnderzeugnis(Enderzeugnis e) throws IllegalArgumentException;

	void saveEnderzeugnis(Enderzeugnis e) throws IllegalArgumentException,
			DuplicateEnderzeugnisException;

	Vector<Enderzeugnis> getAllEnderzeugnis() throws IllegalArgumentException;

	Baugruppe getBaugruppeById(int id) throws IllegalArgumentException;

	void setLoginInfo(LoginInfo loginInfo) throws IllegalArgumentException;
}