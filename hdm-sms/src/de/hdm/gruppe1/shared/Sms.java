package de.hdm.gruppe1.shared;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.google.gwt.user.client.rpc.RemoteService;

import de.hdm.gruppe1.shared.bo.Bauteil;

/**
 * <p>
 * Synchrone Schnittstelle für eine RPC-fähige Klasse zur Verwaltung von Banken.
 * </p>
 * <p>
 * <b>Frage:</b> Warum werden diese Methoden nicht als Teil der Klassen
 * {@link Bank}, {@link Customer}, {@link Account} oder {@link Transaction}
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
 * @author Thies
 */
// TODO: Hier den Path anpassen vorher "greet"
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
	 */
	Bauteil createBauteil(String bauteilBeschreibung,
			String materialBeschreibung) throws IllegalArgumentException;

	 /**
	   * Speichern eines Bauteils-Objekts in der Datenbank.
	   * 
	   * @param b zu sicherndes Objekt.
	   * @throws IllegalArgumentException
	   */
	void save(Bauteil b) throws IllegalArgumentException;

	 /**
	   * Löschen des übergebenen Bauteils.
	   * 
	   * @param b der zu löschende Bauteil
	   * @throws IllegalArgumentException
	   */
	void delete(Bauteil b) throws IllegalArgumentException;

}
