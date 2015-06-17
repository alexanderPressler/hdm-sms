package de.hdm.gruppe1.client;

import java.util.logging.Logger;

import com.google.gwt.core.client.GWT;

import de.hdm.gruppe1.shared.*;

/**
 * Klasse mit Eigenschaften und Diensten, die f�r alle Client-seitigen Klassen
 * relevant sind.
 * 
 * @author thies
 * @version 1.0
 * @since 28.02.2012
 * 
 */
public class ClientsideSettings extends CommonSettings {

	/**
	 * Remote Service Proxy zur Verbindungsaufnahme mit dem Server-seitgen
	 * Dienst namens <code>Sms(Service)</code>.
	 */

	private static SmsAsync stuecklistenVerwaltung = null;
	
	/**
	 * Name des Client-seitigen Loggers.
	 */
	private static final String LOGGER_NAME = "Sms Web Client";

	/**
	 * Instanz des Client-seitigen Loggers.
	 */
	private static final Logger log = Logger.getLogger(LOGGER_NAME);
	
	 /**
	   * <p>
	   * Auslesen des applikationsweiten (Client-seitig!) zentralen Loggers.
	   * </p>
	   * 
	   * <h2>Anwendungsbeispiel:</h2> Zugriff auf den Logger herstellen durch:
	   * 
	   * <pre>
	   * Logger logger = ClientSideSettings.getLogger();
	   * </pre>
	   * 
	   * und dann Nachrichten schreiben etwa mittels
	   * 
	   * <pre>
	   * logger.severe(&quot;Sie sind nicht berechtigt, ...&quot;);
	   * </pre>
	   * 
	   * oder
	   * 
	   * <pre>
	   * logger.info(&quot;Lege neuen Kunden an.&quot;);
	   * </pre>
	   * 
	   * <p>
	   * Bitte auf <em>angemessene Log Levels</em> achten! Severe und info sind nur
	   * Beispiele.
	   * </p>
	   * 
	   * <h2>HINWEIS:</h2>
	   * <p>
	   * Beachten Sie, dass Sie den auszugebenden Log nun nicht mehr durch
	   * bedarfsweise Einf�gen und Auskommentieren etwa von
	   * <code>System.out.println(...);</code> steuern. Sie belassen k�nftig
	   * s�mtliches Logging im Code und k�nnen ohne abermaliges Kompilieren den Log
	   * Level "von au�en" durch die Datei <code>logging.properties</code> steuern.
	   * Sie finden diese Datei in Ihrem <code>war/WEB-INF</code>-Ordner. Der dort
	   * standardm��ig vorgegebene Log Level ist <code>WARN</code>. Dies w�rde
	   * bedeuten, dass Sie keine <code>INFO</code>-Meldungen wohl aber
	   * <code>WARN</code>- und <code>SEVERE</code>-Meldungen erhielten. Wenn Sie
	   * also auch Log des Levels <code>INFO</code> wollten, m�ssten Sie in dieser
	   * Datei <code>.level = INFO</code> setzen.
	   * </p>
	   * 
	   * Weitere Infos siehe Dokumentation zu Java Logging.
	   * 
	   * @return die Logger-Instanz f�r die Server-Seite
	   */
	  public static Logger getLogger() {
	    return log;
	  }
	  /**
	   * <p>
	   * Anlegen und Auslesen der applikationsweit eindeutigen Sms(Service). Diese
	   * Methode erstellt die Sms (Service), sofern sie noch nicht existiert. Bei
	   * wiederholtem Aufruf dieser Methode wird stets das bereits zuvor angelegte
	   * Objekt zur�ckgegeben.
	   * </p>
	   * 
	   * <p>
	   * Der Aufruf dieser Methode erfolgt im Client z.B. durch
	   * <code>BankAdministrationAsync bankVerwaltung = ClientSideSettings.getBankVerwaltung()</code>
	   * .
	   * </p>
	   * 
	   * @return eindeutige Instanz des Typs <code>SmsAsync</code>
	   * @author Peter Thies
	   * @since 28.02.2012
	   */
	  public static SmsAsync getSmsVerwaltung() {
	    // Gab es bislang noch keine Sms (Service)-Instanz, dann...
	    if ( stuecklistenVerwaltung == null) {
	      // Zun�chst instantiieren wir Sms(Service)
	    	stuecklistenVerwaltung = GWT.create(Sms.class);
	    }

	    // So, nun brauchen wir die Sms(Service) nur noch zur�ckzugeben.
	    return stuecklistenVerwaltung;
	  }

}