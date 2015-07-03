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
 * {@link Baugruppen}, {@link Bauteil}, {@link Enderzeugnis} oder {@link Stueckliste}
 * implementiert?<br>
 * <b>Antwort:</b> Z.B. das Löschen eines Bauteils erfordert Kenntnisse über die
 * Verflechtung eines Bauteils mit Stueckliste-Objekts. Um die Klasse <code>Bauteil</code>
 * bzw. <code>Stueckliste</code> nicht zu stark an andere Klassen zu koppeln, wird
 * das Wissen darüber, wie einzelne "Daten"-Objekte koexistieren, in der
 * vorliegenden Klasse gekapselt.
 * </p>
 * <p>
 * <code>@RemoteServiceRelativePath("Sms")</code> ist bei der Adressierung des
 * aus der zugehörigen Impl-Klasse entstehenden Servlet-Kompilats behilflich. 
 * Es gibt im Wesentlichen einen Teil der URL des Servlets an.
 * </p>
 * 
 * @author  Alexander Pressler & Thies & Schmidt
 */

@RemoteServiceRelativePath("sms")
public interface Sms extends RemoteService {

	/**
	 * Initialisierung des Objekts. Diese Methode ist vor dem Hintergrund von
	 * GWT RPC zusätzlich zum No Argument Constructor der implementierenden
	 * Klasse {@link SmsImpl} notwendig. Bitte diese Methode direkt
	 * nach der Instantiierung aufrufen.
	 * 
	 * @throws IllegalArgumentException
	 */
	public void init() throws IllegalArgumentException;

	/**
	 * Ein Bauteil erstellen.
	 * @param bauteilBeschreibung
	 *            BauteilBeschreibung
	 * @param materialBeschreibung
	 *            MaterialBeschreibung
	 * @return Ein fertiges Bauteil-Objekt.
	 * @throws IllegalArgumentException
	 */
	 Bauteil createBauteil(String name, String bauteilBeschreibung,
			String materialBeschreibung) throws IllegalArgumentException;

	 /**
	  * ein Bauteil wird bearbeitet
	  * @param b
	  * @throws IllegalArgumentException
	  */
	 public void save(Bauteil b) throws IllegalArgumentException;

	 /**
	   * 
	   * @param b der zu löschende Bauteil
	   * @throws IllegalArgumentException
	   */
	void delete(Bauteil b) throws IllegalArgumentException;

	
	/**
	 * @throws IllegalArgumentException
	 */
	Vector<Bauteil> getAllBauteile() throws IllegalArgumentException;

	/**
	 * 
	 * @param id
	
	 * @throws IllegalArgumentException
	 */
	Bauteil getBauteilById(int id) throws IllegalArgumentException;

	/**
	 * 
	 * @param name
	 * @param BauteilPaare
	 * @param BaugruppenPaare
	 * @return
	 * @throws IllegalArgumentException
	 */
	Stueckliste createStueckliste(String name,
			Vector<ElementPaar> BauteilPaare,
			Vector<ElementPaar> BaugruppenPaare)
			throws IllegalArgumentException;

	Vector<Stueckliste> getAllStuecklisten() throws IllegalArgumentException;

	void deleteStueckliste(Stueckliste s) throws IllegalArgumentException;

	void saveStueckliste(Stueckliste s) throws IllegalArgumentException, BaugruppenReferenceException;

	/**
	 * 
	 * @param googleID
	 * @param name
	 * @return
	 * @throws IllegalArgumentException
	 */
	User createUser(String googleID, String name)
			throws IllegalArgumentException;

	/**
	 *  Ein Baugruppen Objekt wird erstellt. Die Baugruppe kann einen oder mehrere
	 *   BauteilPaare und/oder BaugruppenPaare beinhalten.
	 *   Eine Baugruppe ist ein spezielles Bauteil, das aus weiteren Baugruppen
	 *   und Bauteilen besteht, zu denen jeweils deren Anzahl in der übergeordneten
		Baugruppe anzugeben ist.
	 * @param name
	 * @param BauteilPaare
	 * @param BaugruppenPaare
	 * @return Baugruppen Objekt 
	 * @throws IllegalArgumentException
	 */
	Baugruppe createBaugruppe(String name, Vector<ElementPaar> BauteilPaare,
			Vector<ElementPaar> BaugruppenPaare)
			throws IllegalArgumentException;
	
	/**
	 * Das löschen von eine Baugruppe
	 * @param b
	 * @throws IllegalArgumentException
	 */

	void deleteBaugruppe(Baugruppe b) throws IllegalArgumentException;
	
	/**
	 * Speichern der Baugruppe
	 * @param b
	 * @throws IllegalArgumentException
	 * @throws BaugruppenReferenceException
	 */

	void saveBaugruppe(Baugruppe b) throws IllegalArgumentException,
	BaugruppenReferenceException;

	/**
	 * Ausgabe der allen vorhandenen Baugruppen
	 * @return Vector-Objekt mit allen Baugruppen-Objekten 
	 */
	Vector<Baugruppe> getAllBaugruppen() throws IllegalArgumentException;
	
	/**
	 * Erstellen eines Enderzeugnis-Objekts, es beinhalten baugruppen-objekte da
	 * Enderzeugnis eine spezielle Baugruppe ist.
	 * @param name
	 * @param baugruppe
	 * @throws IllegalArgumentException
	 */

	Enderzeugnis createEnderzeugnis(String name, Baugruppe baugruppe)
			throws IllegalArgumentException;

	/**
	 * Enderzeugnis wird gelöscht
	 * @param e
	 * @throws IllegalArgumentException
	 */
	void deleteEnderzeugnis(Enderzeugnis e) throws IllegalArgumentException;

	/**
	 * Enderzeugnis wird gespreichert
	 * @param e
	 * @throws IllegalArgumentException
	 */
	void saveEnderzeugnis(Enderzeugnis e) throws IllegalArgumentException;
	

	/**
	 * Es werde alle Enderzeungisse die gespreichert wurden ausgegeben
	 * @return Vector-Objekt mit allen erstellten Enderzeugnis-Objekten 
	 * @throws IllegalArgumentException
	 */

	Vector<Enderzeugnis> getAllEnderzeugnis() throws IllegalArgumentException;
	
	/**
	 * Suchen eines Baugruppen-Objekts, dessen Baugruppennummer bekannt ist
	 * @param id
	 * @return Das erste Baugruppen-Objekt, dass den Suchkriterien entspricht.
	 * @throws IllegalArgumentException
	 */

	Baugruppe getBaugruppeById(int id) throws IllegalArgumentException;
}