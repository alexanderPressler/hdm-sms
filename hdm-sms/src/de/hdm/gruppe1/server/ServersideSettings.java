package de.hdm.gruppe1.server;

import de.hdm.gruppe1.shared.CommonSettings;

import java.util.logging.Logger;

/**
 * <p>
 * Klasse mit Eigenschaften und Diensten, die f�r alle Server-seitigen Klassen
 * relevant sind.
 * </p>
 * <p>
 * In ihrem aktuellen Entwicklungsstand bietet die Klasse eine rudiment�re
 * Unterst�tzung der Logging-Funkionalit�t unter Java. Es wird ein
 * applikationszentraler Logger realisiert, der mittels
 * <code>ServerSideSettings.getLogger()</code> genutzt werden kann.
 * </p>
 * 
 * @author thies
 * @version 1.0
 * @since 28.02.2012
 * 
 */
public class ServersideSettings extends CommonSettings {

	 private static final String LOGGER_NAME = "Sms Server";
	  private static final Logger log = Logger.getLogger(LOGGER_NAME);
	
	  /**
	   * <p>
	   * Auslesen des applikationsweiten (Server-seitig!) zentralen Loggers.
	   * </p>
	   * 
	   * <h2>Anwendungsbeispiel:</h2> Zugriff auf den Logger herstellen durch:
	   * 
	   * <pre>
	   * Logger logger = ServerSideSettings.getLogger();
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
	   * Bitte auf <em>angemessene Log Levels</em> achten! <em>severe</em> und
	   * <em>info</em> sind nur Beispiele.
	   * </p>
	   * 
	   * <h2>HINWEIS:</h2>
	   * <p>
	   * Beachten Sie, dass Sie den auszugebenden Log nun nicht mehr durch
	   * bedarfsweise Einf�gen und Auskommentieren etwa von
	   * <code>System.out.println(...);</code> steuern. Sie belassen k�nftig
	   * s�mtliches Logging im Code und k�nnen ohne abermaliges Kompilieren den Log
	   * Level "von au�en" durch die Datei <code>logging.properties</code> steuern.
	   * Sie finden diese Datei in dem <code>war/WEB-INF</code>-Ordner Ihres
	   * Projekts. Der dort standardm��ig vorgegebene Log Level ist
	   * <code>WARN</code>. Dies w�rde bedeuten, dass Sie keine <code>INFO</code>
	   * -Meldungen wohl aber <code>WARN</code>- und <code>SEVERE</code>-Meldungen
	   * erhielten. Wenn Sie also auch Log des Levels <code>INFO</code> wollten,
	   * m�ssten Sie in dieser Datei <code>.level = INFO</code> setzen.
	   * </p>
	   * 
	   * Weitere Infos siehe Dokumentation zu Java Logging.
	   * 
	   * @return die Logger-Instanz f�r die Server-Seite
	   */
	  public static Logger getLogger() {
	    return log;
	  }
	  
}
