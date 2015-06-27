package de.hdm.gruppe1.server.report;

import de.hdm.gruppe1.server.SmsImpl;
import de.hdm.gruppe1.server.db.*;
import de.hdm.gruppe1.shared.*;
import de.hdm.gruppe1.shared.bo.*;
import de.hdm.gruppe1.shared.report.BaugruppenReport;
import de.hdm.gruppe1.shared.report.SmsReport;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * <p>
 * Implementierungsklasse des Interface <code>BankAdministration</code>. Diese
 * Klasse ist <em>die</em> Klasse, die neben {@link ReportGeneratorImpl}
 * sämtliche Applikationslogik (oder engl. Business Logic) aggregiert. Sie ist
 * wie eine Spinne, die sämtliche Zusammenhänge in ihrem Netz (in unserem Fall
 * die Daten der Applikation) überblickt und für einen geordneten Ablauf und
 * dauerhafte Konsistenz der Daten und Abläufe sorgt.
 * </p>
 * <p>
 * Die Applikationslogik findet sich in den Methoden dieser Klasse. Jede dieser
 * Methoden kann als <em>Transaction Script</em> bezeichnet werden. Dieser Name
 * lässt schon vermuten, dass hier analog zu Datenbanktransaktion pro
 * Transaktion gleiche mehrere Teilaktionen durchgeführt werden, die das System
 * von einem konsistenten Zustand in einen anderen, auch wieder konsistenten
 * Zustand überführen. Wenn dies zwischenzeitig scheitern sollte, dann ist das
 * jeweilige Transaction Script dafür verwantwortlich, eine Fehlerbehandlung
 * durchzuführen.
 * </p>
 * <p>
 * Diese Klasse steht mit einer Reihe weiterer Datentypen in Verbindung. Dies
 * sind:
 * <ol>
 * <li>{@link BankAdministration}: Dies ist das <em>lokale</em> - also
 * Server-seitige - Interface, das die im System zur Verfügung gestellten
 * Funktionen deklariert.</li>
 * <li>{@link BankAdministrationAsync}: <code>BankVerwaltungImpl</code> und
 * <code>BankAdministration</code> bilden nur die Server-seitige Sicht der
 * Applikationslogik ab. Diese basiert vollständig auf synchronen
 * Funktionsaufrufen. Wir müssen jedoch in der Lage sein, Client-seitige
 * asynchrone Aufrufe zu bedienen. Dies bedingt ein weiteres Interface, das in
 * der Regel genauso benannt wird, wie das synchrone Interface, jedoch mit dem
 * zusätzlichen Suffix "Async". Es steht nur mittelbar mit dieser Klasse in
 * Verbindung. Die Erstellung und Pflege der Async Interfaces wird durch das
 * Google Plugin semiautomatisch unterstützt. Weitere Informationen unter
 * {@link BankAdministrationAsync}.</li>
 * <li> {@link RemoteServiceServlet}: Jede Server-seitig instantiierbare und
 * Client-seitig über GWT RPC nutzbare Klasse muss die Klasse
 * <code>RemoteServiceServlet</code> implementieren. Sie legt die funktionale
 * Basis für die Anbindung von <code>BankVerwaltungImpl</code> an die Runtime
 * des GWT RPC-Mechanismus.</li>
 * </ol>
 * </p>
 * <p>
 * <b>Wichtiger Hinweis:</b> Diese Klasse bedient sich sogenannter
 * Mapper-Klassen. Sie gehören der Datenbank-Schicht an und bilden die
 * objektorientierte Sicht der Applikationslogik auf die relationale
 * organisierte Datenbank ab. Zuweilen kommen "kreative" Zeitgenossen auf die
 * Idee, in diesen Mappern auch Applikationslogik zu realisieren. Siehe dazu
 * auch die Hinweise in {@link #delete(Customer)} Einzig nachvollziehbares
 * Argument für einen solchen Ansatz ist die Steigerung der Performance
 * umfangreicher Datenbankoperationen. Doch auch dieses Argument zieht nur dann,
 * wenn wirklich große Datenmengen zu handhaben sind. In einem solchen Fall
 * würde man jedoch eine entsprechend erweiterte Architektur realisieren, die
 * wiederum sämtliche Applikationslogik in der Applikationsschicht isolieren
 * würde. Also, keine Applikationslogik in die Mapper-Klassen "stecken" sondern
 * dies auf die Applikationsschicht konzentrieren!
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
 * @see BankAdministration
 * @see BankAdministrationAsync
 * @see RemoteServiceServlet
 * @author Alexander Pressler & Thies
 */
@SuppressWarnings("serial")
public class SmsReportImpl extends RemoteServiceServlet implements SmsReport {
	
	/**
	   * Ein ReportGenerator benötigt Zugriff auf die Stuecklisten Verwaltung (sms), da diese die
	   * essentiellen Methoden für die Koexistenz von Datenobjekten (vgl.
	   * bo-Package) bietet.
	   */
	private Sms stuecklistenVerwaltung = null;
	
	private BaugruppenMapper baugruppenMapper = null;
	private EnderzeugnisMapper enderzeugnisMapper = null;
	
	/*
	   * ***************************************************************************
	   * ABSCHNITT, Beginn: Initialisierung
	   * ***************************************************************************
	   */
	
	  /**
	   * <p>
	   * Ein <code>RemoteServiceServlet</code> wird unter GWT mittels
	   * <code>GWT.create(Klassenname.class)</code> Client-seitig erzeugt. Hierzu
	   * ist ein solcher No-Argument-Konstruktor anzulegen. Ein Aufruf eines anderen
	   * Konstruktors ist durch die Client-seitige Instantiierung durch
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
	  public SmsReportImpl() throws IllegalArgumentException {
	    /*
	     * Eine weitergehende Funktion muss der No-Argument-Constructor nicht haben.
	     * Er muss einfach vorhanden sein.
	     */
	  }
	  /**
	   * Initialsierungsmethode. Siehe dazu Anmerkungen zum No-Argument-Konstruktor
	   * {@link #ReportGeneratorImpl()}. Diese Methode muss für jede Instanz von
	   * <code>BankVerwaltungImpl</code> aufgerufen werden.
	   * 
	   * @see #ReportGeneratorImpl()
	   */
	  @Override
	public void init() throws IllegalArgumentException {
		/*
		 * Ein SmsReportImpl-Objekt instantiiert für seinen Eigenbedarf eine
		 * Impl-Instanz.
		 */
		SmsImpl a = new SmsImpl();
		a.init();
		this.stuecklistenVerwaltung = a;
		
		this.baugruppenMapper = BaugruppenMapper.baugruppenMapper();
		this.enderzeugnisMapper = EnderzeugnisMapper.enderzeugnisMapper();
	}
	  /*
	   * ***************************************************************************
	   * ABSCHNITT, Ende: Initialisierung
	   * ***************************************************************************
	   */

	/*
	 * ***************************************************************************
	 * ABSCHNITT, Beginn: Methoden für Report 1
	 * ***************************************************************************
	 */
	  //TODO: ANPASSEN AN UNSERE GUI
	@Override
	public BaugruppenReport createBaugruppenReport (Stueckliste BaugruppenStueckliste) throws IllegalArgumentException {
		BaugruppenReport baugruppenReport = new BaugruppenReport(BaugruppenStueckliste);
		return baugruppenReport;
	  }

	/*
	 * ***************************************************************************
	 * ABSCHNITT, Methoden für Report 1
	 * *****************************************
	 */
	
	/*
	 * ***************************************************************************
	 * ABSCHNITT, Beginn: Methoden für Report 2
	 * *********************************
	 * ******************************************
	 */
	//TODO: ANPASSEN AN UNSERE GUI
	@Override
	public Enderzeugnis createMaterialBedarfReport (int id, int anzahl) throws IllegalArgumentException {
	    return this.enderzeugnisMapper.findByID(id);
	  }

	/*
	 * ***************************************************************************
	 * ABSCHNITT, Methoden für Report 2
	 * *****************************************
	 * **********************************
	 */

}
