package de.hdm.gruppe1.server.db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;

import de.hdm.gruppe1.shared.bo.Stueckliste;

/**
 * Mapper-Klasse, die <code>Steuckliste</code>-Objekte auf eine relationale
 * Datenbank abbildet. Hierzu wird eine Reihe von Methoden zur Verfügung
 * gestellt, mit deren Hilfe z.B. Objekte gesucht, erzeugt, modifiziert und
 * gelöscht werden können. Das Mapping ist bidirektional. D.h., Objekte können
 * in DB-Strukturen und DB-Strukturen in Objekte umgewandelt werden.
 * 
 * @see CustomerMapper, TransactionMapper
 * @author Thies
 */
public class StuecklisteMapper {

	/**
	 * Die Klasse StuecklisteMapper wird nur einmal instantiiert. Man spricht
	 * hierbei von einem sogenannten <b>Singleton</b>.
	 * <p>
	 * Diese Variable ist durch den Bezeichner <code>static</code> nur einmal
	 * für sämtliche eventuellen Instanzen dieser Klasse vorhanden. Sie
	 * speichert die einzige Instanz dieser Klasse.
	 * 
	 * @see StuecklisteMapper()
	 */
	private static StuecklisteMapper stuecklisteMapper = null;

	/**
	 * Geschützter Konstruktor - verhindert die Möglichkeit, mit
	 * <code>new</code> neue Instanzen dieser Klasse zu erzeugen.
	 */
	protected StuecklisteMapper() {
	}

	/**
	 * Diese statische Methode kann aufgrufen werden durch
	 * <code>StuecklisteMapper.StuecklisteMapper()</code>. Sie stellt die
	 * Singleton-Eigenschaft sicher, indem Sie dafür sorgt, dass nur eine
	 * einzige Instanz von <code>StuecklisteMapper</code> existiert.
	 * <p>
	 * 
	 * <b>Fazit:</b> StuecklisteMapper sollte nicht mittels <code>new</code>
	 * instantiiert werden, sondern stets durch Aufruf dieser statischen
	 * Methode.
	 * 
	 * @return DAS <code>StuecklisteMapper</code>-Objekt.
	 * @see StuecklisteMapper
	 */
	public static StuecklisteMapper stuecklisteMapper() {
		if (stuecklisteMapper == null) {
			stuecklisteMapper = new StuecklisteMapper();
		}

		return stuecklisteMapper;
	}
	
	/**
	 * Einfügen eines <code>Stueckliste</code>-Objekts in die Datenbank. Dabei wird
	 * auch der Primärschlüssel des übergebenen Objekts geprüft und ggf.
	 * berichtigt.
	 * 
	 * @param a
	 *            das zu speichernde Objekt
	 * @return das bereits übergebene Objekt, jedoch mit ggf. korrigierter
	 *         <code>id</code>.
	 */
	public Stueckliste insert(Stueckliste stueckliste) {
		Connection con = DBConnection.connection();

		try {
			Statement stmt = con.createStatement();

			/*
			 * Zunächst schauen wir nach, welches der momentan höchste
			 * Primärschlüsselwert ist.
			 */
			ResultSet rs = stmt.executeQuery("SELECT MAX(id) AS maxid "
					+ "FROM stuecklisten ");

			// Wenn wir etwas zurückerhalten, kann dies nur einzeilig sein
			if (rs.next()) {
				/*
				 * a erhält den bisher maximalen, nun um 1 inkrementierten
				 * Primärschlüssel.
				 */
				stueckliste.setId(rs.getInt("maxid") + 1);

				stmt = con.createStatement();

				// Jetzt erst erfolgt die tatsächliche Einfügeoperation
				stmt.executeUpdate("INSERT INTO `stuecklisten` (`id`, `name`) VALUES ('"
						+ stueckliste.getId()
						+ "', '"
						+ stueckliste.getName()
						+ "');");

			}
		} catch (SQLException e2) {
			e2.printStackTrace();
		}

		/*
		 * Rückgabe, des evtl. korrigierten Stueckliste.
		 * 
		 * HINWEIS: Da in Java nur Referenzen auf Objekte und keine physischen
		 * Objekte übergeben werden, wäre die Anpassung des Stueckliste-Objekts auch
		 * ohne diese explizite Rückgabe au�erhalb dieser Methode sichtbar. Die
		 * explizite Rückgabe von a ist eher ein Stilmittel, um zu
		 * signalisieren, dass sich das Objekt evtl. im Laufe der Methode
		 * verändert hat.
		 */
		return stueckliste;
	}
	
	/**
	 * Wiederholtes Schreiben eines Objekts in die Datenbank.
	 * 
	 * @param a
	 *            das Objekt, das in die DB geschrieben werden soll
	 * @return das als Parameter übergebene Objekt
	 */
	public Stueckliste update(Stueckliste stueckliste) {
		Connection con = DBConnection.connection();

		try {
			Statement stmt = con.createStatement();

			stmt.executeUpdate("UPDATE `stuecklisten` SET `name`='"
					+ stueckliste.getName() + "',`name`='"
					+ stueckliste.getId() + ";");

		} catch (SQLException e2) {
			e2.printStackTrace();
		}

		// Um Analogie zu insert(Stueckliste a) zu wahren, geben wir a zurück
		return stueckliste;
	}
	
	/**
	 * Löschen der Daten eines <code>Stueckliste</code>-Objekts aus der Datenbank.
	 * 
	 * @param a
	 *            das aus der DB zu löschende "Objekt"
	 */
	public void delete(Stueckliste stueckliste) {
		Connection con = DBConnection.connection();

		try {
			Statement stmt = con.createStatement();

			stmt.executeUpdate("DELETE FROM `stuecklisten` WHERE `id`="
					+ stueckliste.getId());

		} catch (SQLException e2) {
			e2.printStackTrace();
		}
	}
	
	/**
	 * Auslesen aller Stuecklisten.
	 * 
	 * @return Ein Vektor mit Stueckliste-Objekten, die sämtliche Stuecklisten
	 *         repräsentieren. Bei evtl. Exceptions wird ein partiell gef�llter
	 *         oder ggf. auch leerer Vetor zurückgeliefert.
	 */
	public Vector<Stueckliste> findAll() {
		Connection con = DBConnection.connection();
		// Ergebnisvektor vorbereiten
		Vector<Stueckliste> result = new Vector<Stueckliste>();

		try {
			Statement stmt = con.createStatement();

			ResultSet rs = stmt
					.executeQuery("SELECT * FROM `stuecklisten` ORDER BY `name`");

			// Für jeden Eintrag im Suchergebnis wird nun ein Customer-Objekt
			// erstellt.
			while (rs.next()) {
				Stueckliste stueckliste = new Stueckliste();
				stueckliste.setId(rs.getInt("id"));
				stueckliste.setName(rs.getString("name"));
			

				// Hinzufügen des neuen Objekts zum Ergebnisvektor
				result.addElement(stueckliste);

			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		// Ergebnisvektor zurückgeben
		return result;
	}
	
	/**
	 * Suchen eines Stuecklistes mit vorgegebener Id. Da diese eindeutig
	 * ist, wird genau ein Objekt zur�ckgegeben.
	 * 
	 * @param id
	 *            Primärschlüsselattribut (->DB)
	 * @return Stueckliste-Objekt, das dem übergebenen Schlüssel entspricht, null bei
	 *         nicht vorhandenem DB-Tupel.
	 */
	public Stueckliste findById(int id) {
		// DB-Verbindung holen
		Connection con = DBConnection.connection();

		try {
			// Leeres SQL-Statement (JDBC) anlegen
			Statement stmt = con.createStatement();

			// Statement ausfüllen und als Query an die DB schicken
			// TODO: SQL Statement anpassen 
			ResultSet rs = stmt
					.executeQuery("SELECT id, name"
							+ "  FROM stuecklisten "
							+ "WHERE id="
							+ id
							+ " ORDER BY name");
			// "SELECT * FROM `stuecklisten` ORDER BY `name`"

			/*
			 * Da id Primärschlüssel ist, kann max. nur ein Tupel zurückgegeben
			 * werden. Prüfe, ob ein Ergebnis vorliegt.
			 */
			if (rs.next()) {
				// Ergebnis-Tupel in Objekt umwandeln
				Stueckliste stueckliste = new Stueckliste();
				stueckliste.setId(rs.getInt("id"));
				stueckliste.setName(rs.getString("name"));

				return stueckliste;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}

		return null;
	}
	
}