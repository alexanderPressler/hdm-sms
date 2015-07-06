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
 * Synchrone Schnittstelle für eine RPC-fähige Klasse zur Verwaltung von
 * Strucktustücklisten.
 * 
 * <p>
 * <b>Frage:</b> Warum werden diese Methoden nicht als Teil der Klassen
 * {@link Baugruppen}, {@link Bauteil}, {@link Enderzeugnis} oder
 * {@link Stueckliste} implementiert?<br>
 * <b>Antwort:</b> Z.B. das Löschen eines Bauteils erfordert Kenntnisse über die
 * Verflechtung eines Bauteils mit Stueckliste-Objekts. Um die Klasse
 * <code>Bauteil</code> bzw. <code>Stueckliste</code> nicht zu stark an andere
 * Klassen zu koppeln, wird das Wissen darüber, wie einzelne "Daten"-Objekte
 * koexistieren, in der vorliegenden Klasse gekapselt.
 * </p>
 * <p>
 * <code>@RemoteServiceRelativePath("Sms")</code> ist bei der Adressierung des
 * aus der zugehörigen Impl-Klasse entstehenden Servlet-Kompilats behilflich. Es
 * gibt im Wesentlichen einen Teil der URL des Servlets an.
 * </p>
 * 
 * @author Alexander Pressler & Thies & Schmidt
 */

@RemoteServiceRelativePath("sms")
public interface Sms extends RemoteService {

	/**
	 * Initialisierung des Objekts. Diese Methode ist vor dem Hintergrund von
	 * GWT RPC zusätzlich zum No Argument Constructor der implementierenden
	 * Klasse {@link SmsImpl} notwendig. Bitte diese Methode direkt nach der
	 * Instantiierung aufrufen.
	 * 
	 * @throws IllegalArgumentException
	 */
	public void init() throws IllegalArgumentException;

	/**
	 * Ein Bauteil erstellen.
	 * 
	 * @param bauteilBeschreibung
	 *            BauteilBeschreibung
	 * @param materialBeschreibung
	 *            MaterialBeschreibung
	 * @return Ein fertiges Bauteil-Objekt.
	 * @throws IllegalArgumentException
	 */
	Bauteil createBauteil(String name, String bauteilBeschreibung,
			String materialBeschreibung) throws IllegalArgumentException, 
			DuplicateBauteilException;

	/**
	 * ein Bauteil wird gespreichert
	 * 
	 * @param b
	 * @throws IllegalArgumentException
	 */
	public void save(Bauteil b) throws IllegalArgumentException, 
	DuplicateBauteilException;
	/**
	 * Das Bauteil löschen
	 * @param b
	 *    der zu löschende Bauteil
	 * @throws IllegalArgumentException
	 */
	void delete(Bauteil b) throws IllegalArgumentException;

	/**
	 * Für den Fall dass alle Bauteile anzeigt werden sollen
	 * @throws IllegalArgumentException
	 */
	Vector<Bauteil> getAllBauteile() throws IllegalArgumentException;

	/**
	 * Suchen eines Bauteil-Objekts, dessen Bauteilnummer bekannt ist
	 * @param id
	 * @throws IllegalArgumentException
	 */
	Bauteil getBauteilById(int id) throws IllegalArgumentException;

	/**
	 * Ein Stuecklisten Objekt wird erstellt. Die Baugruppe kann einen oder
	 * mehrere BauteilPaare und/oder BaugruppenPaare beinhalten in zusammnehang
	 * mit Anzahl dessen.Die zu gebende Parameter sind ElementenPaare und der
	 * Name der Stückliste.
	 * 
	 * @param name
	 * @param BauteilPaare
	 * @param BaugruppenPaare
	 * @throws IllegalArgumentException
	 */
	Stueckliste createStueckliste(String name,
			Vector<ElementPaar> BauteilPaare,
			Vector<ElementPaar> BaugruppenPaare)
			throws IllegalArgumentException;

	/**
	 * 
	 * @return
	 * @throws IllegalArgumentException
	 */
	Vector<Stueckliste> getAllStuecklisten() throws 
	IllegalArgumentException;

	/**
	 * Eine Stückliste löschen
	 * @param s
	 * @throws IllegalArgumentException
	 */
	void deleteStueckliste(Stueckliste s) throws IllegalArgumentException;

	/**
	 * Spreicher eine Stückliste
	 * @param s
	 * @throws IllegalArgumentException
	 * @throws BaugruppenReferenceException
	 */
	void saveStueckliste(Stueckliste s) throws IllegalArgumentException, 
	BaugruppenReferenceException;
	/**
	 * Erstellen eines AnwenderAccounts Objekts
	 * @param googleID
	 * @param name
	 * @return
	 * @throws IllegalArgumentException
	 */
	User createUser(String googleID, String name)
			throws IllegalArgumentException;

	/**
	 * Ein Baugruppen Objekt wird erstellt. Die Baugruppe kann einen oder
	 * mehrere BauteilPaare und/oder BaugruppenPaare beinhalten. Eine Baugruppe
	 * ist ein spezielles Bauteil, das aus weiteren Baugruppen und Bauteilen
	 * besteht, zu denen jeweils deren Anzahl in der übergeordneten Baugruppe
	 * anzugeben ist.
	 * 
	 * @param name
	 * @param BauteilPaare
	 * @param BaugruppenPaare
	 * @return Baugruppen Objekt
	 * @throws IllegalArgumentException
	 */
	Baugruppe createBaugruppe(String name, Vector<ElementPaar> BauteilPaare,
			Vector<ElementPaar> BaugruppenPaare)
			throws IllegalArgumentException, DuplicateBaugruppeException;

	/**
	 * Das löschen von eine Baugruppe
	 * 
	 * @param b
	 * @throws IllegalArgumentException
	 */

	void deleteBaugruppe(Baugruppe b) throws IllegalArgumentException;

	/**
	 * Speichern eines Baugruppen -Objekts in der Datenbank.
	 * 
	 * @param b
	 * @throws IllegalArgumentException
	 * @throws BaugruppenReferenceException
	 *             , um ein Loop zu vermeiden.
	 */

	void saveBaugruppe(Baugruppe b) throws IllegalArgumentException,
			BaugruppenReferenceException, DuplicateBaugruppeException;

	/**
	 * Ausgabe der allen vorhandenen Baugruppen
	 * 
	 * @return Vector-Objekt mit allen Baugruppen-Objekten
	 */
	Vector<Baugruppe> getAllBaugruppen() throws IllegalArgumentException;

	/**
	 * Erstellen eines Enderzeugnis-Objekts, es beinhalten baugruppen-objekte da
	 * Enderzeugnis eine spezielle Baugruppe ist.
	 * 
	 * @param name
	 * @param baugruppe
	 * @throws IllegalArgumentException
	 */

	Enderzeugnis createEnderzeugnis(String name, Baugruppe baugruppe)
			throws IllegalArgumentException, DuplicateEnderzeugnisException;

	/**
	 * Enderzeugnis wird gelöscht
	 * 
	 * @param e
	 * @throws IllegalArgumentException
	 */
	void deleteEnderzeugnis(Enderzeugnis e) throws IllegalArgumentException;

	/**
	 * Speichern eines Enderzeugnis-Objekts in der Datenbank.
	 * 
	 * @param e
	 * @throws IllegalArgumentException
	 */
	void saveEnderzeugnis(Enderzeugnis e) throws IllegalArgumentException,
	DuplicateEnderzeugnisException;

	/**
	 * Es werde alle Enderzeungisse die gespreichert wurden ausgegeben
	 * 
	 * @return Vector-Objekt mit allen erstellten Enderzeugnis-Objekten
	 * @throws IllegalArgumentException
	 */

	Vector<Enderzeugnis> getAllEnderzeugnis() throws IllegalArgumentException;

	/**
	 * Suchen eines Baugruppen-Objekts, dessen Baugruppennummer bekannt ist
	 * 
	 * @param id
	 * @return Das erste Baugruppen-Objekt, dass den Suchkriterien entspricht.
	 * @throws IllegalArgumentException
	 */

	Baugruppe getBaugruppeById(int id) throws IllegalArgumentException;
	
	/**
	 * 
	 * @param loginInfo
	 * @throws IllegalArgumentException
	 */
	
	void setLoginInfo (LoginInfo loginInfo) throws IllegalArgumentException;
}