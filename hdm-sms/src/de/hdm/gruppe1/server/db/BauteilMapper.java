package de.hdm.gruppe1.server.db;

import java.sql.*;
import java.text.DateFormat;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.Vector;
import de.hdm.gruppe1.shared.bo.*;

/**
 * Mapper-Klasse, die Bauteil-Objekte auf eine relationale Datenbank abbildet
 * (Google Cloud SQL). Hierzu wird eine Reihe von Methoden zur Verfügung
 * gestellt, mit deren Hilfe z.B. Objekte gesucht, erzeugt, modifiziert und
 * gelöscht werden können. Das Mapping ist bidirektional. D.h., Objekte können
 * in DB-Strukturen und DB-Strukturen in Objekte umgewandelt werden.
 * 
 * @author Andreas Herrmann (in Anlehnung an Herr Prof. Dr. Thies)
 * 
 */
public class BauteilMapper {

	/**
	 * Die Klasse BauteilMapper wird nur einmal instantiiert. Man spricht
	 * hierbei von einem sogenannten Singleton (Autor: Herr Prof. Dr. Thies).
	 */
	private static BauteilMapper bauteilMapper = null;
	
	/**
	 * Der Standartkonstruktor wird auf protected gesetzt, sodass man über <code>new</code>
	 * kein Objekt der Klasse erzeugen kann
	 */
	protected BauteilMapper() {
	}

	/**
	 * Die statische Methode dient dazu die Singleton-Eigenschaft des Mappers sicherzustellen, indem
	 * sie dafür sorgt, dass nur eine Instanz des Mappers existiert.
	 * Der Mapper kann nur über <code>UserMapper.userMapper()</code> instanziiert werden
	 * 
	 * @return bauteilMapper
	 */
	public static BauteilMapper bauteilMapper() {
		if (bauteilMapper == null) {
			bauteilMapper = new BauteilMapper();
		}

		return bauteilMapper;
	}

	/**
	 * Die insert-Methode stellt die Möglichkeit zur Verfügung, ein
	 * Java-Bauteil-Objekt in der Datenbank anzulegen.
	 * 
	 * @param bauteil
	 * @return bauteil
	 */
	public Bauteil insert(Bauteil bauteil) {
		Connection con = DBConnection.connection();

		try {
			Statement stmt = con.createStatement();

			/**
			 * Mithilfe dieses Abschnitts wird stets vor dem Anlegen eines
			 * Bauteil-Objekts die derzeit höchste Id in der Datenbank-Tabelle
			 * abgefragt.
			 */
			ResultSet rs = stmt.executeQuery("SELECT MAX(teilnummer) AS maxid "
					+ "FROM Bauteile ");

			/**
			 * Wenn wir etwas zurückerhalten, kann dies nur einzeilig sein.
			 */
			if (rs.next()) {

				/**
				 * Das neue Bauteil erhält den bisher maximalen, nun um 1
				 * inkrementierten Primärschlüssel.
				 */
				bauteil.setId(rs.getInt("maxid") + 1);

				/**
				 * Java Util Date wird umgewandelt in SQL Date um das
				 * Änderungsdatum in die Datenbank zu speichern.
				 */
				Date utilDate = bauteil.getEditDate();
				java.sql.Timestamp sqlDate = new java.sql.Timestamp(
						utilDate.getTime());
				DateFormat df = new SimpleDateFormat("dd/MM/YYYY HH:mm:ss");
				df.format(sqlDate);
				bauteil.setEditDate(sqlDate);

				stmt = con.createStatement();

				/**
				 * Das SQL-Statement wird hier mithilfe der Objekt-Attribute
				 * erstellt und anschließend als Befehl an die Datenbank
				 * geschickt.
				 */
				stmt.executeUpdate("INSERT INTO Bauteile VALUES ('"
						+ bauteil.getId() + "', '"
						+ bauteil.getMaterialBeschreibung() + "', '"
						+ bauteil.getEditUser().getId() + "', '"
						+ bauteil.getName() + "', '"
						+ bauteil.getBauteilBeschreibung() + "', '"
						+ bauteil.getEditDate() + "');");

			}
		} catch (SQLException e2) {
			e2.printStackTrace();
		}

		/**
		 * Rückgabe, des evtl. korrigierten Bauteils.
		 * 
		 * HINWEIS: Da in Java nur Referenzen auf Objekte und keine physischen
		 * Objekte übergeben werden, wäre die Anpassung des bauteil-Objekts auch
		 * ohne diese explizite Rückgabe außerhalb dieser Methode sichtbar. Die
		 * explizite Rückgabe von a ist eher ein Stilmittel, um zu
		 * signalisieren, dass sich das Objekt evtl. im Laufe der Methode
		 * verändert hat.
		 */
		return bauteil;
	}

	/**
	 * Mithilfe der update-Methode kann ein bestehendes Bauteil-Objekt in der
	 * Datenbank geändert werden. Dabei werden alle Attribute neu überschrieben,
	 * bis auf die Objekt-Id.
	 * 
	 * @param bauteil
	 * @return bauteil
	 */
	public Bauteil update(Bauteil bauteil) {
		Connection con = DBConnection.connection();

		Integer bId = new Integer(bauteil.getId());

		try {
			Statement stmt = con.createStatement();

			/**
			 * Java Util Date wird umgewandelt in SQL Date um das Änderungsdatum
			 * in die Datenbank zu speichern.
			 */
			Date utilDate = bauteil.getEditDate();
			java.sql.Timestamp sqlDate = new java.sql.Timestamp(
					utilDate.getTime());
			DateFormat df = new SimpleDateFormat("dd/MM/YYYY HH:mm:ss");
			df.format(sqlDate);
			bauteil.setEditDate(sqlDate);

			/**
			 * Das SQL-Statement wird hier mithilfe der Objekt-Attribute
			 * erstellt und anschließend als Befehl an die Datenbank geschickt.
			 */
			stmt.executeUpdate("UPDATE Bauteile SET " + "name='"
					+ bauteil.getName() + "',beschreibung='"
					+ bauteil.getBauteilBeschreibung() + "',material='"
					+ bauteil.getMaterialBeschreibung() + "',bearbeitet_Von='"
					+ bauteil.getEditUser().getId() + "',datum='"
					+ bauteil.getEditDate() + "' WHERE teilnummer='"
					+ bId.toString() + "';");

		} catch (SQLException e2) {
			e2.printStackTrace();
		}

		return bauteil;
	}

	/**
	 * Sofern der Benutzer ein Bauteil-Objekt aus der Datenbank löschen möchte,
	 * wird dieser Befehl ausgeführt. Hierbei wird die Id des entsprechenden
	 * Objektes verwendet.
	 * 
	 * @param bauteil
	 */
	public void delete(Bauteil bauteil) {
		Connection con = DBConnection.connection();

		try {
			Statement stmt = con.createStatement();

			/**
			 * Das SQL-Statement wird hier mithilfe der Objekt-Id erstellt und
			 * anschließend als Befehl an die Datenbank geschickt.
			 */
			stmt.executeUpdate("DELETE FROM Bauteile WHERE teilnummer ='"
					+ bauteil.getId() + "'");

		} catch (SQLException e2) {
			e2.printStackTrace();
		}
	}

	/**
	 * Mithilfe der findAll-Methode werden alle in der Datenbank vorhandenen
	 * Bauteil-Objekte gesammelt in einem Vektor zurückgeliefert.
	 * 
	 * @return result
	 */
	public Vector<Bauteil> findAll() {
		Connection con = DBConnection.connection();

		/**
		 * Den Ergebnisvektor vorbereiten.
		 */
		Vector<Bauteil> result = new Vector<Bauteil>();

		try {
			Statement stmt = con.createStatement();

			/**
			 * Das Ergebnis soll anhand der Id sortiert werden.
			 */
			ResultSet rs = stmt
					.executeQuery("SELECT * FROM Bauteile JOIN User ON Bauteile.bearbeitet_Von=User.userID ORDER BY teilnummer");

			/**
			 * Für jeden Eintrag im Suchergebnis wird nun ein Customer-Objekt
			 * erstellt.
			 */
			while (rs.next()) {
				Bauteil bauteil = new Bauteil();
				bauteil.setId(rs.getInt("teilnummer"));
				bauteil.setName(rs.getString("name"));
				bauteil.setBauteilBeschreibung(rs.getString("beschreibung"));
				bauteil.setMaterialBeschreibung(rs.getString("material"));

				/**
				 * Java Util Date wird umgewandelt in SQL Date um das
				 * Änderungsdatum in die Datenbank zu speichern.
				 */
				java.sql.Timestamp sqlDate = rs.getTimestamp("datum");
				bauteil.setEditDate(sqlDate);

				User editUser = new User();
				editUser.setName(rs.getString("User.eMail"));
				editUser.setId(rs.getInt("userID"));
				editUser.setGoogleID(rs.getString("googleID"));
				bauteil.setEditUser(editUser);

				/**
				 * Hinzufügen des neuen Objekts zum Ergebnisvektor.
				 */
				result.addElement(bauteil);

			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		/**
		 * Den Ergebnisvektor zurückgeben.
		 */
		return result;
	}

	/**
	 * Um ein Bauteil-Objekt mit einer spezifischen Id aus der Datenbank zu
	 * erhalten, wird die findByID-Methode aufgerufen. Sie liefert das gefundene
	 * Objekt zurück.
	 * 
	 * @param id
	 * @return bauteil (falls Ergebnis gefunden)
	 */
	public Bauteil findById(int id) {
		/**
		 * Die DB-Verbindung holen.
		 */
		Connection con = DBConnection.connection();

		try {
			/**
			 * Ein Leeres SQL-Statement (JDBC) anlegen.
			 */
			Statement stmt = con.createStatement();

			/**
			 * Das Statement ausfüllen und als Query an die DB schicken. Um den
			 * letzten Bearbeiter nicht nur als Id, sondern mit seiner E-Mail
			 * Adresse darstellen zu können, wird dieser JOIN-Befehl benötigt.
			 */
			ResultSet rs = stmt
					.executeQuery("SELECT * FROM Bauteile JOIN User ON Bauteile.bearbeitet_Von=User.userID WHERE teilnummer="
							+ id + ";");

			/**
			 * Da id Primärschlüssel ist, kann max. nur ein Tupel zurückgegeben
			 * werden. Prüfe, ob ein Ergebnis vorliegt.
			 */
			if (rs.next()) {

				/**
				 * Ergebnis-Tupel in Objekt umwandeln
				 */
				Bauteil bauteil = new Bauteil();
				bauteil.setId(rs.getInt("teilnummer"));
				bauteil.setName(rs.getString("name"));
				bauteil.setBauteilBeschreibung(rs
						.getString("bauteilBeschreibung"));
				bauteil.setMaterialBeschreibung(rs
						.getString("materialBeschreibung"));

				User editUser = new User();
				editUser.setName(rs.getString("eMail"));
				editUser.setId(rs.getInt("userID"));
				editUser.setGoogleID(rs.getString("googleID"));
				bauteil.setEditUser(editUser);

				/**
				 * Java Util Date wird umgewandelt in SQL Date um das
				 * Änderungsdatum in die Datenbank zu speichern.
				 */
				java.sql.Timestamp sqlDate = rs.getTimestamp("datum");
				bauteil.setEditDate(sqlDate);

				return bauteil;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}

		return null;
	}

	/**
	 * Mithilfe der findByName-Methode wird die Möglichkeit geschaffen, beim
	 * Anlegen oder Editieren eines Bauteils herauszufinden, ob ein Objekt mit
	 * dem identischen Namen bereits vorhanden ist. Es soll zu keiner Zeit
	 * möglich sein, dass es zwei Objekte mit identischem Namen in der Datenbank
	 * gibt.
	 * 
	 * @param name
	 * @return bauteil
	 */
	public Bauteil finByName(String name) {
		Connection con = DBConnection.connection();
		Bauteil bauteil = null;
		try {
			Statement stmt = con.createStatement();

			/**
			 * Das SQL-Statement wird hier mithilfe des Objekt-Namens erstellt
			 * und anschließend als Befehl an die Datenbank geschickt. Um den
			 * letzten Bearbeiter nicht nur mit einer ID, sondern mit seiner
			 * E-Mail Adresse anzuzeigen, wird der User per JOIN-Befehl
			 * abgefragt.
			 */
			ResultSet rs = stmt
					.executeQuery("SELECT * FROM Bauteile JOIN User ON Bauteile.bearbeitet_Von=User.userID WHERE name='"
							+ name + "';");
			if (rs.next()) {
				bauteil = new Bauteil();
				bauteil.setId(rs.getInt("teilnummer"));
				bauteil.setName(rs.getString("name"));

				User user = new User();
				user.setId(rs.getInt("userID"));
				user.setName(rs.getString("eMail"));
				user.setGoogleID(rs.getString("googleID"));
				bauteil.setEditUser(user);
				/**
				 * Java Util Date wird umgewandelt in SQL Date um das
				 * Änderungsdatum in die Datenbank zu speichern.
				 */
				java.sql.Timestamp sqlDate = rs.getTimestamp("datum");
				bauteil.setEditDate(sqlDate);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return bauteil;
	}

}