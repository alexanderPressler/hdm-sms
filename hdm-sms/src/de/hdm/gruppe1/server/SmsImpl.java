package de.hdm.gruppe1.server;

import java.util.Date;
import java.util.Vector;

import de.hdm.gruppe1.shared.DuplicateBaugruppeException;
import de.hdm.gruppe1.shared.FieldVerifier;
import de.hdm.gruppe1.server.db.*;
import de.hdm.gruppe1.shared.*;
import de.hdm.gruppe1.shared.bo.*;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * <p>
 * Implementierungsklasse des Interface <code>Sms</code>. Diese Klasse ist
 * <em>die</em> Klasse, die neben {@link SmsReportImpl} sämtliche
 * Applikationslogik (oder engl. Business Logic) aggregiert. Sie ist wie eine
 * Spinne, die sämtliche Zusammenhänge in ihrem Netz (in unserem Fall die Daten
 * der Applikation) überblickt und für einen geordneten Ablauf und dauerhafte
 * Konsistenz der Daten und Abläufe sorgt.
 * </p>
 * <p>
 * Die Applikationslogik findet sich in den Methoden dieser Klasse. Jede dieser
 * Methoden kann als <em>Transaction Script</em> bezeichnet werden. Dieser Name
 * lässt schon vermuten, dass hier analog zu Datenbanktransaktion pro
 * Transaktion gleiche mehrere Teilaktionen durchgeführt werden, die das System
 * von einem konsistenten Zustand in einen anderen, auch wieder konsistenten
 * Zustand überführen. Wenn dies zwischenzeitig scheitern sollte, dann ist das
 * jeweilige Transaction Script dafür verantwortlich, eine Fehlerbehandlung
 * durchzuführen.
 * </p>
 * <p>
 * Diese Klasse steht mit einer Reihe weiterer Datentypen in Verbindung. Dies
 * sind:
 * <ol>
 * <li>{@link Sms}: Dies ist das <em>lokale</em> - also Server-seitige -
 * Interface, das die im System zur Verfügung gestellten Funktionen deklariert.</li>
 * <li>{@link SmsAsync}: <code>SmsImpl</code> und<code>Sms</code> bilden nur die
 * Server-seitige Sicht der Applikationslogik ab. Diese basiert vollständig auf
 * synchronen Funktionsaufrufen. Wir müssen jedoch in der Lage sein,
 * Client-seitige asynchrone Aufrufe zu bedienen. Dies bedingt ein weiteres
 * Interface, das in der Regel genauso benannt wird, wie das synchrone
 * Interface, jedoch mit dem zusätzlichen Suffix "Async". Es steht nur mittelbar
 * mit dieser Klasse in Verbindung. Die Erstellung und Pflege der Async
 * Interfaces wird durch das Google Plugin semiautomatisch unterstützt. Weitere
 * Informationen unter {@link SmsAsync}.</li>
 * <li> {@link RemoteServiceServlet}: Jede Server-seitig instantiierbare und
 * Client-seitig über GWT RPC nutzbare Klasse muss die Klasse
 * <code>RemoteServiceServlet</code> implementieren. Sie legt die funktionale
 * Basis für die Anbindung von <code>SmsImpl</code> an die Runtime des GWT
 * RPC-Mechanismus.</li>
 * </ol>
 * </p>
 * <p>
 * <b>Wichtiger Hinweis:</b> Diese Klasse bedient sich sogenannter
 * Mapper-Klassen. Sie gehören der Datenbank-Schicht an und bilden die
 * objektorientierte Sicht der Applikationslogik auf die relationale
 * organisierte Datenbank ab. Zuweilen kommen "kreative" Zeitgenossen auf die
 * Idee, in diesen Mappern auch Applikationslogik zu realisieren. Einzig
 * nachvollziehbares Argument für einen solchen Ansatz ist die Steigerung der
 * Performance umfangreicher Datenbankoperationen. Doch auch dieses Argument
 * zieht nur dann, wenn wirklich große Datenmengen zu handhaben sind. In einem
 * solchen Fall würde man jedoch eine entsprechend erweiterte Architektur
 * realisieren, die wiederum sämtliche Applikationslogik in der
 * Applikationsschicht isolieren würde. Also, keine Applikationslogik in die
 * Mapper-Klassen "stecken" sondern dies auf die Applikationsschicht
 * konzentrieren!
 * </p>
 * <p>
 * Beachten Sie, dass sämtliche Methoden, die mittels GWT RPC aufgerufen werden
 * können ein <code>throws IllegalArgumentException</code> in der
 * Methodendeklaration aufweisen. Diese Methoden dürfen also Instanzen von
 * {@link IllegalArgumentException} auswerfen. Mit diesen Exceptions können z.B.
 * Probleme auf der Server-Seite in einfacher Weise auf die Client-Seite
 * transportiert und dort systematisch in einem Catch-Block abgearbeitet werden.
 * </p>
 * <p>
 * Es gibt sicherlich noch viel mehr über diese Klasse zu schreiben. Weitere
 * Infos erhalten Sie in der Lehrveranstaltung.
 * </p>
 * 
 * @see Sms
 * @see SmsAsync
 * @see RemoteServiceServlet
 * @author Thies, Schmidt & Pressler
 */
@SuppressWarnings("serial")
public class SmsImpl extends RemoteServiceServlet implements Sms {

	/**
	 * Referenzen auf die DatenbankMapper, welche die BusinessObjekte-Objekte
	 * mit der Datenbank abgleicht.
	 */
	private BauteilMapper bauteilMapper = null;
	private StuecklisteMapper stuecklisteMapper = null;
	private UserMapper userMapper = null;
	private BaugruppenMapper baugruppenMapper = null;
	private EnderzeugnisMapper enderzeugnisMapper = null;
	private LoginInfo logInfo = null;

	/*
	 * Da diese Klasse ein gewisse Größe besitzt - dies ist eigentlich ein
	 * Hinweise, dass hier eine weitere Gliederung sinnvoll ist - haben wir zur
	 * besseren Übersicht Abschnittskomentare eingefügt. Sie leiten ein Cluster
	 * in irgeneinerweise zusammengehöriger Methoden ein. Ein entsprechender
	 * Kommentar steht am Ende eines solchen Clusters.
	 */

	/*
	 * ***************************************************************************
	 * ABSCHNITT, Beginn: Initialisierung
	 * ***************************************
	 * ************************************
	 */

	/**
	 * Initialsierungsmethode. Siehe dazu Anmerkungen zum
	 * No-Argument-Konstruktor {@link #ReportGeneratorImpl()}. Diese Methode
	 * muss für jede Instanz von <code>BankVerwaltungImpl</code> aufgerufen
	 * werden.
	 * 
	 * @see #ReportGeneratorImpl()
	 */
	public SmsImpl() throws IllegalArgumentException {
		/*
		 * Eine weitergehende Funktion muss der No-Argument-Constructor nicht
		 * haben. Er muss einfach vorhanden sein.
		 */
	}

	
	/**
	 * <p>
	 * Ein <code>RemoteServiceServlet</code> wird unter GWT mittels
	 * <code>GWT.create(Klassenname.class)</code> Client-seitig erzeugt. Hierzu
	 * ist ein solcher No-Argument-Konstruktor anzulegen. Ein Aufruf eines
	 * anderen Konstruktors ist durch die Client-seitige Instantiierung durch
	 * <code>GWT.create(Klassenname.class)</code> nach derzeitigem Stand nicht
	 * möglich.
	 * </p>
	 * <p>
	 * Es bietet sich also an, eine separate Instanzenmethode zu erstellen, die
	 * Client-seitig direkt nach <code>GWT.create(Klassenname.class)</code>
	 * aufgerufen wird, um eine Initialisierung der Instanz vorzunehmen.
	 * </p>
	 * 
	 * @see #init()
	 */
	@Override
	public void init() throws IllegalArgumentException {
		/*
		 * Ganz wesentlich ist, dass die BankAdministration einen vollständigen
		 * Satz von Mappern besitzt, mit deren Hilfe sie dann mit der Datenbank
		 * kommunizieren kann.
		 */
		this.bauteilMapper = BauteilMapper.bauteilMapper();
		this.stuecklisteMapper = StuecklisteMapper.stuecklisteMapper();
		this.userMapper = UserMapper.userMapper();
		this.baugruppenMapper = BaugruppenMapper.baugruppenMapper();
		this.enderzeugnisMapper = EnderzeugnisMapper.enderzeugnisMapper();
	}

	/*
	 * ***************************************************************************
	 * ABSCHNITT, Ende: Initialisierung
	 * *****************************************
	 * **********************************
	 */

	/*
	 * ***************************************************************************
	 * ABSCHNITT, Beginn: Methoden für Bauteil-Objekte
	 * **************************
	 * *************************************************
	 */
	/**
	 * <p>
	 * Anlegen eines neuen Bauteiles. Dies führt implizit zu einem Speichern des
	 * neuen Bauteiles in der Datenbank.
	 * </p>
	 * 
	 * <p>
	 * <b>HINWEIS:</b> Änderungen an Bauteil-Objekten müssen stets durch Aufruf
	 * von {@link #save(Bauteil b)} in die Datenbank transferiert werden.
	 * </p>
	 * 
	 * @see #createBauteil(String, String, String)
	 */
	@Override
	public Bauteil createBauteil(String name, String bauteilBeschreibung,
			String materialBeschreibung) throws IllegalArgumentException,
			DuplicateBauteilException {

		// Überprüfen, ob ein Bauteil mit dem Namen schon existiert
		Bauteil bauteil = bauteilMapper.finByName(name);
		if (bauteil != null) {
			DuplicateBauteilException dBE = new DuplicateBauteilException(
					bauteil);
			throw dBE;
		}
		
		// Erstellen und befüllen eines neuen Bauteil-Objektes
		Bauteil b = new Bauteil();
		b.setName(name);
		b.setBauteilBeschreibung(bauteilBeschreibung);
		b.setMaterialBeschreibung(materialBeschreibung);

		// Erstellungsdatum wird generiert und dem Objekt angehängt
		Date date = new Date();
		b.setEditDate(date);

		// Setzen des Users 
		b.setEditUser(logInfo.getUser());

		// Objekt in der DB speichern.

		return this.bauteilMapper.insert(b);
	}

	/**
	 * Speichern eines Bauteils.
	 * @see #save(Bauteil)
	 */
	@Override
	public void save(Bauteil b) throws IllegalArgumentException,
			DuplicateBauteilException {

		// Überprüfen, ob ein Bauteil mit dem Namen schon existiert
		Bauteil bauteil = bauteilMapper.finByName(b.getName());
		if (bauteil != null) {
			DuplicateBauteilException dBE = new DuplicateBauteilException(
					bauteil);
			throw dBE;
		}
		// Setzen des Users
		b.setEditUser(logInfo.getUser());

		// Aenderungsdatum wird generiert und dem Objekt angehängt
		// Das Datum wird zum Zeitpunkt des RPC Aufrufs erstellt
		Date date = new Date();
		b.setEditDate(date);

		this.bauteilMapper.update(b);
	}

	/**
	 * Löschen eines Bauteils. 
	 * @see #delete(Bauteil)
	 */
	@Override
	public void delete(Bauteil b) throws IllegalArgumentException {

		Vector<Stueckliste> vStueckliste = this.stuecklisteMapper
				.findByBauteil(b);

		// Abgleich mit Stücklisten
		if (vStueckliste.isEmpty() == true) {
			this.bauteilMapper.delete(b);
		} else {
			// TODO Exception schreiben
			System.out.println("Bauteil wird in Stückliste verwendet: ");

		}

	}

	/**
	 * Auslesen aller Bauteile.
	 * @see #getAllBauteile()
	 */
	@Override
	public Vector<Bauteil> getAllBauteile() throws IllegalArgumentException {
		return this.bauteilMapper.findAll();
	}

	/**
	 * Auslesen eines Bauteils anhand seiner Id.
	 * @see #getBauteilById(int)
	 */
	@Override
	public Bauteil getBauteilById(int id) throws IllegalArgumentException {
		return this.bauteilMapper.findById(id);
	}

	/*
	 * ***************************************************************************
	 * ABSCHNITT, Ende: Methoden für Bauteil-Objekte
	 * ****************************
	 * ***********************************************
	 */

	/*
	 * ***************************************************************************
	 * ABSCHNITT, : Methoden für Stueckliste-Objekte
	 * ****************************
	 * ***********************************************
	 */
	/**
	 * Anlegen eines neuen Stueckliste. Dies führt implizit zu einem Speichern
	 * des neuen Stuecklistes in der Datenbank.
	 * @see #createStueckliste(String, Vector, Vector)
	 */
	@Override
	public Stueckliste createStueckliste(String name,
			Vector<ElementPaar> BauteilPaare,
			Vector<ElementPaar> BaugruppenPaare)
			throws IllegalArgumentException, DuplicateStuecklisteException {

		// Überprüfen, ob eine Stueckliste mit dem Namen schon existiert
		Stueckliste stueckliste = stuecklisteMapper.finByName(name);
		if (stueckliste != null) {
			DuplicateStuecklisteException dSE = new DuplicateStuecklisteException(
					stueckliste);
			throw dSE;
		}

		Stueckliste s = new Stueckliste();
		s.setName(name);
		s.setBauteilPaare(BauteilPaare);
		s.setBaugruppenPaare(BaugruppenPaare);

		// Erstellungsdatum wird generiert und dem Objekt angehängt
		Date date = new Date();
		s.setEditDate(date);

		s.setEditUser(logInfo.getUser());

		// Objekt in der DB speichern.
		return this.stuecklisteMapper.insert(s);
	}

	/**
	 * Löschen einer Stueckliste
	 * @see #deleteStueckliste(Stueckliste)
	 */
	@Override
	public void deleteStueckliste(Stueckliste s)
			throws IllegalArgumentException {

		BaugruppenMapper bm = BaugruppenMapper.baugruppenMapper();
		Baugruppe b = bm.findBaugruppeByStueckliste(s);

		if (b == null) {
			this.stuecklisteMapper.delete(s);
		}

		// TODO Exception
		else {

			System.out.println("Stueckliste kann nicht gelöscht werden: ");
			System.out.println("Stueckliste wird verwendet in: " + b.getName());

		}

	}

	/**
	 * Speichern einer Stueckliste.
	 * @see #saveStueckliste(Stueckliste)
	 */
	@Override
	public void saveStueckliste(Stueckliste s) throws IllegalArgumentException,
			DuplicateStuecklisteException {

		// Überprüfen, ob eine Stueckliste mit dem Namen schon existiert
		Stueckliste stueckliste = stuecklisteMapper.finByName(s.getName());
		if (stueckliste != null) {
			DuplicateStuecklisteException dSE = new DuplicateStuecklisteException(
					stueckliste);
			throw dSE;
		}

		// Aenderungsdatum wird generiert und dem Objekt angehängt
		Date date = new Date();
		s.setEditDate(date);

		s.setEditUser(logInfo.getUser());

		this.stuecklisteMapper.update(s);
	}

	/**
	 * Auslesen aller Stuecklisten.
	 * @see #getAllStuecklisten()
	 */
	@Override
	public Vector<Stueckliste> getAllStuecklisten()
			throws IllegalArgumentException {
		return this.stuecklisteMapper.findAll();
	}

	/*
	 * ***************************************************************************
	 * ABSCHNITT, Ende: Methoden für Stuecklisten-Objekte
	 * ***************************************************************************
	 */

	/*
	 * ***************************************************************************
	 * ABSCHNITT, Beginn: Methoden für User-Objekte
	 * ***************************************************************************
	 */
	/**
	 * <p>
	 * Anlegen eines neuen Users. Dies führt implizit zu einem Speichern des
	 * neuen User in der Datenbank.
	 * </p>
	 * 
	 * <p>
	 * <b>HINWEIS:</b> Änderungen an User-Objekten müssen stets durch Aufruf
	 * von {@link #save(User b)} in die Datenbank transferiert werden.
	 * </p>
	 * 
	 * @see createUser(String googleID, String name)
	 */
	@Override
	public User createUser(String googleID, String name)
			throws IllegalArgumentException {
		User u = new User();
		u.setGoogleID(googleID);
		u.setName(name);

		// Objekt in der DB speichern.
		return this.userMapper.insert(u);
	}

	/*
	 * ***************************************************************************
	 * ABSCHNITT, Ende: Methoden für User-Objekte
	 * ***************************************************************************
	 */

	/*
	 * ***************************************************************************
	 * ABSCHNITT, Methoden für Baugruppen-Objekte
	 * ***************************************************************************
	 */

	/**
	 * <p>
	 * Anlegen einer neuen Baugruppe . Dies führt implizit zu einem Speichern
	 * des neuen Baugruppe in der Datenbank.
	 * 
	 * @see #createBaugruppe(String, Vector, Vector)
	 */
	@Override
	public Baugruppe createBaugruppe(String name,
			Vector<ElementPaar> BauteilPaare,
			Vector<ElementPaar> BaugruppenPaare)
			throws IllegalArgumentException, DuplicateBaugruppeException {

		// Überprüfen, ob eine Baugruppe mit dem Namen schon existiert
		Baugruppe baugruppe = baugruppenMapper.finByName(name);
		if (baugruppe != null) {
			DuplicateBaugruppeException dBE = new DuplicateBaugruppeException(
					baugruppe);
			throw dBE;
		}

		// Erstellungsdatum wird generiert und dem Objekt angehäng
		Date date = new Date();

		Stueckliste s = new Stueckliste();
		s.setEditUser(logInfo.getUser());
		s.setName(name + "_sl");
		s.setBauteilPaare(BauteilPaare);
		s.setBaugruppenPaare(BaugruppenPaare);
		s.setEditDate(date);
		s = this.stuecklisteMapper.insert(s);

		Baugruppe b = new Baugruppe();
		b.setEditDate(date);
		b.setEditUser(logInfo.getUser());
		b.setName(name);
		b.setStueckliste(s);

		// Objekt in der DB speichern.
		return this.baugruppenMapper.insert(b);
	}

	/**
	 * Löschen einer Baugruppe
	 * @see #deleteBaugruppe(Baugruppe)
	 */
	@Override
	public void deleteBaugruppe(Baugruppe b) throws IllegalArgumentException {

		Vector<Stueckliste> vStueckliste = this.stuecklisteMapper
				.findByBaugruppe(b);
		Vector<Enderzeugnis> vEnderzeugnis = this.enderzeugnisMapper
				.findByBaugruppe(b);

		// Abgleich mit Stücklisten
		if (vStueckliste.isEmpty() == true && vEnderzeugnis.isEmpty() == true) {

			this.baugruppenMapper.delete(b);
			this.stuecklisteMapper.delete(b.getStueckliste());

		}

		else {
			if (vStueckliste.isEmpty() == false) {
				// TODO Exception schreiben
				System.out.println("Baugruppe wird in Stückliste verwendet.");
			}

			if (vEnderzeugnis.isEmpty() == false) {
				// TODO Exception schreiben
				System.out
						.println("Baugruppe wird in Enderzeugnis verwendet: ");
			}

		}

	}

	/**
	 * Speichern eines Baugruppe.
	 * @see #saveBaugruppe(Baugruppe)
	 */
	@Override
	public void saveBaugruppe(Baugruppe b) throws IllegalArgumentException,
			BaugruppenReferenceException, DuplicateBaugruppeException {

		// Überprüfen, ob eine Baugruppe mit dem Namen schon existiert
		Baugruppe baugruppe = baugruppenMapper.finByName(b.getName());
		if (baugruppe != null) {
			DuplicateBaugruppeException dBE = new DuplicateBaugruppeException(
					baugruppe);
			throw dBE;
		}

		for (int i = 0; i < b.getStueckliste().getBaugruppenPaare().size(); i++) {
			LoopPrevention lP = new LoopPrevention();
			if (!lP.checkForBaugruppenLoop(b, (Baugruppe) b.getStueckliste()
					.getBaugruppenPaare().get(i).getElement())) {
				BaugruppenReferenceException bRE = new BaugruppenReferenceException(
						(Baugruppe) b.getStueckliste().getBaugruppenPaare()
								.get(i).getElement());
				throw bRE;
			}
		}
		// Aenderungsdatum wird generiert und dem Objekt angehängt
		Date date = new Date();
		b.setEditDate(date);

		b.setEditUser(logInfo.getUser());

		Stueckliste s = b.getStueckliste();
		s.setEditDate(date);
		s.setEditUser(logInfo.getUser());

		this.stuecklisteMapper.update(s);

		this.baugruppenMapper.update(b);
	}

	/**
	 * Auslesen aller Baugruppen.
	 * @see #getAllBaugruppen()
	 */
	@Override
	public Vector<Baugruppe> getAllBaugruppen() throws IllegalArgumentException {
		return this.baugruppenMapper.findAll();
	}

	/**
	 * Auslesen einer Baugruppe anhand seiner Id.
	 * @see #getBaugruppeById(int)
	 */
	@Override
	public Baugruppe getBaugruppeById(int id) throws IllegalArgumentException {
		return this.baugruppenMapper.findByID(id);
	}

	/*
	 * ***************************************************************************
	 * ABSCHNITT Ende : Methoden für Baugruppen-Objekte
	 * ***************************************************************************
	 */

	/*
	 * ***************************************************************************
	 * ABSCHNITT: Methoden für Enderzeugnis-Objekte
	 * ***************************************************************************
	 */

	/**
	 * <p>
	 * Anlegen eines neuen Enderzeugnisses . Dies führt implizit zu einem Speichern
	 * des neuen Enderzeugnisses in der Datenbank.
	 * 
	 * @see #createEnderzeugnis(String, Baugruppe)
	 */
	@Override
	public Enderzeugnis createEnderzeugnis(String name, Baugruppe baugruppe)
			throws IllegalArgumentException, DuplicateEnderzeugnisException {

		// Überprüfen, ob ein Enderzeugnis mit dem Namen schon existiert
		Enderzeugnis enderzeugnis = enderzeugnisMapper.finByName(name);
		if (enderzeugnis != null) {
			DuplicateEnderzeugnisException dEE = new DuplicateEnderzeugnisException(
					enderzeugnis);
			throw dEE;
		}

		// Erstellungsdatum wird generiert und dem Objekt angehäng
		Date date = new Date();

		Enderzeugnis e = new Enderzeugnis();
		e.setEditDate(date);
		e.setName(name);
		e.setBaugruppe(baugruppe);
		e.setEditUser(logInfo.getUser());

		// Objekt in der DB speichern.
		return this.enderzeugnisMapper.insert(e);
	}

	/**
	 * Löschen eines Enderzeugnisses
	 * @see #deleteEnderzeugnis(Enderzeugnis)
	 */
	@Override
	public void deleteEnderzeugnis(Enderzeugnis e)
			throws IllegalArgumentException {

		this.enderzeugnisMapper.delete(e);
	}

	/**
	 * Speichern eines Enderzeugnisses.
	 * @see #saveEnderzeugnis(Enderzeugnis)
	 */
	@Override
	public void saveEnderzeugnis(Enderzeugnis e)
			throws IllegalArgumentException, DuplicateEnderzeugnisException {

		// Überprüfen, ob ein Enderzeugnis mit dem Namen schon existiert
		Enderzeugnis enderzeugnis = enderzeugnisMapper.finByName(e.getName());
		if (enderzeugnis != null) {
			DuplicateEnderzeugnisException dEE = new DuplicateEnderzeugnisException(
					enderzeugnis);
			throw dEE;
		}

		// Aenderungsdatum wird generiert und dem Objekt angehängt
		Date date = new Date();
		e.setEditDate(date);

		e.setEditUser(logInfo.getUser());

		this.enderzeugnisMapper.update(e);
	}

	/**
	 * Auslesen aller Enderzeugnisse.
	 * @see #getAllEnderzeugnis()
	 */
	@Override
	public Vector<Enderzeugnis> getAllEnderzeugnis()
			throws IllegalArgumentException {
		return this.enderzeugnisMapper.findAll();
	}

	public void setLoginInfo(LoginInfo loginInfo) {
		logInfo = loginInfo;
	}

	/*
	 * ***************************************************************************
	 * ABSCHNITT Ende : Methoden für Enderzeugnis-Objekte
	 * ***************************************************************************
	 */
}