package de.hdm.gruppe1.shared;

import java.util.ArrayList;
import java.util.Vector;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.google.gwt.user.rebind.rpc.ServiceInterfaceProxyGenerator;

import de.hdm.gruppe1.shared.bo.ElementPaar;
import de.hdm.gruppe1.shared.bo.Baugruppe;
import de.hdm.gruppe1.shared.bo.Bauteil;
import de.hdm.gruppe1.shared.bo.Element;
import de.hdm.gruppe1.shared.bo.Stueckliste;


/**
 * <p>
 * Synchrone Schnittstelle f�r eine RPC-f�hige Klasse zur Verwaltung von Banken.
 * </p>
 * <p>
 * <b>Frage:</b> Warum werden diese Methoden nicht als Teil der Klassen
 * {@link Bank}, {@link Customer}, {@link Account} oder {@link Transaction}
 * implementiert?<br>
 * <b>Antwort:</b> Z.B. das L�schen eines Kunden erfordert Kenntnisse �ber die
 * Verflechtung eines Kunden mit Konto-Objekten. Um die Klasse <code>Bank</code>
 * bzw. <code>Customer</code> nicht zu stark an andere Klassen zu koppeln, wird
 * das Wissen dar�ber, wie einzelne "Daten"-Objekte koexistieren, in der
 * vorliegenden Klasse gekapselt.
 * </p>
 * <p>
 * Nat�rlich existieren Frameworks wie etwa Hibernate, die dies auf eine andere
 * Weise realisieren. Wir haben jedoch ganz bewusst auf deren Nutzung
 * verzichtet, um in diesem kleinen Demoprojekt den Blick auf das Wesentliche
 * nicht unn�tig zu verstellen.
 * </p>
 * <p>
 * <code>@RemoteServiceRelativePath("bankadministration")</code> ist bei der
 * Adressierung des aus der zugeh�rigen Impl-Klasse entstehenden
 * Servlet-Kompilats behilflich. Es gibt im Wesentlichen einen Teil der URL des
 * Servlets an.
 * </p>
 * 
 * @author Thies
 */
// TODO: Hier den Path anpassen vorher "greet", Was muss hier rein?
@RemoteServiceRelativePath("sms")
public interface Sms extends RemoteService {

	/**
	 * Initialisierung des Objekts. Diese Methode ist vor dem Hintergrund von
	 * GWT RPC zus�tzlich zum No Argument Constructor der implementierenden
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
	Bauteil createBauteil(String name, String bauteilBeschreibung,
			String materialBeschreibung) throws IllegalArgumentException;

	 /**
	   * Speichern eines Bauteils-Objekts in der Datenbank.
	   * 
	   * @param b zu sicherndes Objekt.
	   * @throws IllegalArgumentException
	   */
	void save(Bauteil b) throws IllegalArgumentException;

	 /**
	   * L�schen des �bergebenen Bauteils.
	   * 
	   * @param b der zu l�schende Bauteil
	   * @throws IllegalArgumentException
	   */
	void delete(Bauteil b) throws IllegalArgumentException;

	Vector<Bauteil> getAllBauteile() throws IllegalArgumentException;

	Bauteil getBauteilById(int id) throws IllegalArgumentException;

	
	
	Stueckliste createStueckliste(String name,
			Vector<ElementPaar> BauteilPaare,
			Vector<ElementPaar> BaugruppenPaare)
			throws IllegalArgumentException;

	Vector<Stueckliste> getAllStuecklisten() throws IllegalArgumentException;

	void deleteStueckliste(Stueckliste s) throws IllegalArgumentException;

	void saveStueckliste(Stueckliste s) throws IllegalArgumentException;

  
	
	Baugruppe createBaugruppe(String name, Vector<Baugruppe> Baugruppe);
	

	public void saveBaugruppe (Baugruppe bg)
			 throws IllegalArgumentException;
	
	void deleteBaugruppe(Baugruppe baugruppe);
	
	Vector<Baugruppe> getBaugruppeByName(String name);
	
	Baugruppe getBaugruppeById(int id)
			 throws IllegalArgumentException;
	
	Vector<Baugruppe> getAllBaugruppen();





	


}


