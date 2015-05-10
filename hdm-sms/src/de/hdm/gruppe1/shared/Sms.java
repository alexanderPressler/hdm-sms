package de.hdm.gruppe1.shared;

import java.util.ArrayList;
import java.util.Vector;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import de.hdm.gruppe1.shared.bo.Baugruppe;
import de.hdm.gruppe1.shared.bo.Bauteil;
<<<<<<< HEAD
import de.hdm.gruppe1.shared.bo.Element;
=======
import de.hdm.gruppe1.shared.bo.Stueckliste;
>>>>>>> refs/remotes/origin/Alex

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
// TODO: Hier den Path anpassen vorher "greet", Was muss hier rein?
@RemoteServiceRelativePath("sms")
public interface Sms extends RemoteService {

	Bauteil init();

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
	Bauteil createBauteil(String name, String bauteilBeschreibung,
			String materialBeschreibung) throws IllegalArgumentException;

	 Bauteil save(Bauteil b);

	 Bauteil delete(Bauteil b);

	Vector<Bauteil> getAllBauteile() throws IllegalArgumentException;


	Baugruppe createBaugruppe(String name, ArrayList<Element> element);
	
	Baugruppe editBaugruppe (Baugruppe baugruppe)
			 throws IllegalArgumentException;
	
	Baugruppe deleteBaugruppe(Baugruppe baugruppe);
	
	
	
	
	
	
	Baugruppe getBaugruppeByName (String name)
			 throws IllegalArgumentException;
	
	Baugruppe getBaugruppeById(int id)
			 throws IllegalArgumentException;
	
	
	Baugruppe getAllBaugruppen (ArrayList<Baugruppe> baugruppe)
			 throws IllegalArgumentException;
	
	
	
	
	
	
	
	
	
	
	

	Bauteil getBauteilById(int id) throws IllegalArgumentException;

	Stueckliste createStueckliste(String name) throws IllegalArgumentException;

	void saveStueckliste(Stueckliste s) throws IllegalArgumentException;

	void deleteStueckliste(Stueckliste s) throws IllegalArgumentException;

	Vector<Stueckliste> getAllStuecklisten() throws IllegalArgumentException;

	Stueckliste getStuecklisteById(int id) throws IllegalArgumentException;


}
