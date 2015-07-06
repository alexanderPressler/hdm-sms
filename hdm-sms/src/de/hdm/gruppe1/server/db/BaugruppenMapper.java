package de.hdm.gruppe1.server.db;

import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;
import de.hdm.gruppe1.shared.bo.*;

/**
 * Mapper-Klasse, die Baugruppen-Objekte auf eine relationale Datenbank abbildet
 * (Google Cloud SQL). Hierzu wird eine Reihe von Methoden zur Verfügung
 * gestellt, mit deren Hilfe z.B. Objekte gesucht, erzeugt, modifiziert und
 * gelöscht werden können. Das Mapping ist bidirektional. D.h., Objekte können
 * in DB-Strukturen und DB-Strukturen in Objekte umgewandelt werden.
 * 
 * @author Andreas Herrmann (in Anlehnung an Prof. Dr. Thies)
 * 
 */
public class BaugruppenMapper {

	/**
	 * Die Klasse BaugruppenMapper wird nur einmal instantiiert. Man spricht
	 * hierbei von einem sogenannten Singleton (Autor: Prof. Dr. Thies).
	 */
	private static BaugruppenMapper baugruppenMapper = null;

	protected BaugruppenMapper() {
	}

	/**
	 * ?
	 * 
	 * @return baugruppenMapper
	 */
	public static BaugruppenMapper baugruppenMapper() {
		if (baugruppenMapper == null) {
			baugruppenMapper = new BaugruppenMapper();
		}
		return baugruppenMapper;
	}

	/**
	 * Die insert-Methode stellt die Möglichkeit zur Verfügung, ein
	 * Java-Baugruppen-Objekt in der Datenbank anzulegen.
	 * 
	 * @param baugruppe
	 * @return baugruppe
	 */
	public Baugruppe insert(Baugruppe baugruppe) {
		Connection con = DBConnection.connection();
		try {

			Statement stmt = con.createStatement();

			/**
			 * Mithilfe dieses Abschnitts wird stets vor dem Anlegen eines
			 * Baugruppen-Objekts die derzeit höchste Id in der
			 * Datenbank-Tabelle abgefragt.
			 */
			ResultSet rs = stmt.executeQuery("SELECT MAX(bg_ID) AS maxid "
					+ "FROM Baugruppe ");

		      /**
		       * Wenn wir etwas zurückerhalten, kann dies nur einzeilig sein.
		       */
			if (rs.next()) {

				/**
				 * Die neue Baugruppe erhält den bisher maximalen, nun um 1
				 * inkrementierten Primärschlüssel.
				 */
				baugruppe.setId(rs.getInt("maxid") + 1);

				/**
				 * Java Util Date wird umgewandelt in SQL Date, um das
				 * Änderungsdatum in die Datenbank zu speichern .
				 */
				Date utilDate = baugruppe.getEditDate();
				java.sql.Timestamp sqlDate = new java.sql.Timestamp(
						utilDate.getTime());
				DateFormat df = new SimpleDateFormat("dd/MM/YYYY HH:mm:ss");
				df.format(sqlDate);
				baugruppe.setEditDate(sqlDate);

				stmt = con.createStatement();

				/**
				 * Das SQL-Statement wird hier mithilfe der Objekt-Attribute
				 * erstellt und anschließend als Befehl an die Datenbank
				 * geschickt.
				 */
				stmt.executeUpdate("INSERT INTO Baugruppe VALUES('"
						+ baugruppe.getId() + "','" + baugruppe.getName()
						+ "','" + baugruppe.getStueckliste().getId() + "','"
						+ baugruppe.getEditUser().getId() + "','"
						+ baugruppe.getEditDate() + "');");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return baugruppe;

	}

	/**
	 * Mithilfe der update-Methode kann ein bestehendes Baugruppen-Objekt in der
	 * Datenbank geändert werden. Dabei werden alle Attribute neu überschrieben,
	 * bis auf die Objekt-Id.
	 * 
	 * @param baugruppe
	 * @return baugruppe
	 */
	public Baugruppe update(Baugruppe baugruppe) {
		Connection con = DBConnection.connection();
		try {

			/**
			 * Java Util Date wird umgewandelt in SQL Date um das Änderungsdatum
			 * in die Datenbank zu speichern.
			 */
			Date utilDate = baugruppe.getEditDate();
			java.sql.Timestamp sqlDate = new java.sql.Timestamp(
					utilDate.getTime());
			DateFormat df = new SimpleDateFormat("dd/MM/YYYY HH:mm:ss");
			df.format(sqlDate);
			baugruppe.setEditDate(sqlDate);

			Statement stmt = con.createStatement();

			/**
			 * Das SQL-Statement wird hier mithilfe der Objekt-Attribute
			 * erstellt und anschließend als Befehl an die Datenbank geschickt.
			 */
			stmt.executeUpdate("UPDATE Baugruppe SET name='"
					+ baugruppe.getName() + "',stueckliste='"
					+ baugruppe.getStueckliste().getId()
					+ "', bearbeitet_Von='" + baugruppe.getEditUser().getId()
					+ "', datum ='" + baugruppe.getEditDate()
					+ "' WHERE bg_ID ='" + baugruppe.getId() + "';");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return baugruppe;

	}

	/**
	 * Sofern der Benutzer ein Baugruppen-Objekt aus der Datenbank löschen
	 * möchte, wird dieser Befehl ausgeführt. Hierbei wird die Id des
	 * entsprechenden Objektes verwendet.
	 * 
	 * @param baugruppe
	 */
	public boolean delete(Baugruppe baugruppe) {
		Connection con = DBConnection.connection();

		try {
			Statement stmt = con.createStatement();

			/**
			 * Das SQL-Statement wird hier mithilfe der Objekt-Id erstellt und
			 * anschließend als Befehl an die Datenbank geschickt.
			 */
			if (stmt.executeUpdate("DELETE FROM Baugruppe WHERE bg_ID ='"
					+ baugruppe.getId() + "';") == 0) {
				return false;
			} else {
				return true;
			}

		} catch (SQLException e2) {
			e2.printStackTrace();
			return false;
		}

	}

	/**
	 * Um ein Baugruppen-Objekt mit einer spezifischen Id aus der Datenbank zu
	 * erhalten, wird die findByID-Methode aufgerufen. Sie liefert das gefundene
	 * Objekt zurück.
	 * 
	 * @param id
	 * @return baugruppe
	 */
	public Baugruppe findByID(int id) {
		Connection con = DBConnection.connection();
		Baugruppe baugruppe = null;
		try {
			Statement stmt = con.createStatement();

			/**
			 * Um den letzten Bearbeiter nicht nur als Id, sondern mit seiner
			 * E-Mail Adresse darstellen zu können, wird dieser JOIN-Befehl
			 * benötigt.
			 */
			ResultSet rs = stmt
					.executeQuery("SELECT * FROM Baugruppe JOIN User ON Baugruppe.bearbeitet_Von=User.userID WHERE bg_ID ='"
							+ id + "';");
			/**
			 * Da es nur eine Baugruppe mit dieser ID geben kann ist davon
			 * auszugehen, dass das ResultSet nur eine Zeile enthält.
			 */
			if (rs.next()) {
				baugruppe = new Baugruppe();
				baugruppe.setId(rs.getInt("bg_ID"));
				baugruppe.setName(rs.getString("name"));
				
				/**
				 * Da wir die Stueckliste der Baugruppe auflösen müssen brauchen
				 * wir einen StuecklistenMapper.
				 */
				StuecklisteMapper slm = StuecklisteMapper.stuecklisteMapper();
				baugruppe
						.setStueckliste(slm.findById(rs.getInt("stueckliste")));

				User user = new User();
				user.setId(rs.getInt("userID"));
				user.setName(rs.getString("eMail"));
				user.setGoogleID(rs.getString("googleID"));
				baugruppe.setEditUser(user);

				/**
				 * Java Util Date wird umgewandelt in SQL Date um das
				 * Änderungsdatum in die Datenbank zu speichern.
				 */
				java.sql.Timestamp sqlDate = rs.getTimestamp("datum");
				baugruppe.setEditDate(sqlDate);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return baugruppe;
	}

	/**
	 * Mithilfe der findAll-Methode werden alle in der Datenbank vorhandenen
	 * Baugruppen-Objekte gesammelt in einem Vektor zurückgeliefert.
	 * 
	 * @return vBaugruppe
	 */
	public Vector<Baugruppe> findAll() {
		Vector<Baugruppe> vBaugruppe = new Vector<Baugruppe>();
		Connection con = DBConnection.connection();
		try {
			Statement stmt = con.createStatement();

			/**
			 * Um den letzten Bearbeiter nicht nur als Id, sondern mit seiner
			 * E-Mail Adresse darstellen zu können, wird dieser JOIN-Befehl
			 * benötigt.
			 */
			ResultSet rs = stmt
					.executeQuery("SELECT * FROM Baugruppe JOIN User ON Baugruppe.bearbeitet_Von=User.userID;");
			/**
			 * Da es viele Baugruppen geben kann, müssen wir eine Schleife
			 * benutzen.
			 */
			while (rs.next()) {
				/**
				 * Neue Baugruppe erzeugen.
				 */
				Baugruppe baugruppe = new Baugruppe();
				baugruppe.setId(rs.getInt("bg_ID"));
				baugruppe.setName(rs.getString("name"));

				/**
				 * Da wir die Stueckliste der Baugruppe auflösen müssen brauchen
				 * wir einen StuecklistenMapper
				 */
				StuecklisteMapper slm = StuecklisteMapper.stuecklisteMapper();
				baugruppe
						.setStueckliste(slm.findById(rs.getInt("stueckliste")));

				/**
				 * Neuen User erzeugen.
				 */
				User user = new User();
				user.setId(rs.getInt("userID"));
				user.setName(rs.getString("eMail"));
				user.setGoogleID(rs.getString("googleID"));
				baugruppe.setEditUser(user);

				/**
				 * Java Util Date wird umgewandelt in SQL Date um das
				 * Änderungsdatum in die Datenbank zu speichern.
				 */
				java.sql.Timestamp sqlDate = rs.getTimestamp("datum");
				baugruppe.setEditDate(sqlDate);
				/**
				 * Baugruppe der ArrayList hinzufügen.
				 */
				vBaugruppe.addElement(baugruppe);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return vBaugruppe;
	}

	/**
	 * Um die zugehörige Baugruppe einer vorhandenen Stückliste zu ermitteln,
	 * kann auf die findBaugruppeByStueckliste- Methode zugegriffen werden.
	 * Hierfür wird die Stücklisten-Id benötigt, da diese in der Datenbank in
	 * der Baugruppen-Tabelle als Fremdschlüssel liegt.
	 * 
	 * @param stueckliste
	 * @return baugruppe
	 */
	public Baugruppe findBaugruppeByStueckliste(Stueckliste stueckliste) {
		Connection con = DBConnection.connection();
		Baugruppe baugruppe = null;
		try {
			Statement stmt = con.createStatement();

			/**
			 * Das SQL-Statement wird hier mithilfe der Objekt-Id erstellt und
			 * anschließend als Befehl an die Datenbank geschickt.
			 */
			ResultSet rs = stmt
					.executeQuery("SELECT * FROM Baugruppe WHERE stueckliste='"
							+ stueckliste.getId() + "';");
			if (rs.next()) {
				baugruppe = this.findByID(rs.getInt("bg_ID"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return baugruppe;
	}

	/**
	 * Mithilfe der findByName-Methode wird die Möglichkeit geschaffen, beim
	 * Anlegen oder Editieren einer Baugruppe herauszufinden, ob ein Objekt mit
	 * dem identischen Namen bereits vorhanden ist. Es soll zu keiner Zeit
	 * möglich sein, dass es zwei Objekte mit identischem Namen in der Datenbank
	 * gibt.
	 * 
	 * @param name
	 * @return baugruppe
	 */
	public Baugruppe finByName(String name) {
		Connection con = DBConnection.connection();
		Baugruppe baugruppe = null;
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
					.executeQuery("SELECT * FROM Baugruppe JOIN User ON Baugruppe.bearbeitet_Von=User.userID WHERE name='"
							+ name + "';");
			if (rs.next()) {

				baugruppe = new Baugruppe();
				baugruppe.setId(rs.getInt("bg_ID"));
				baugruppe.setName(rs.getString("name"));

				/**
				 * Da wir die Stueckliste der Baugruppe auflösen müssen brauchen
				 * wir einen StuecklistenMapper.
				 */
				StuecklisteMapper slm = StuecklisteMapper.stuecklisteMapper();
				baugruppe
						.setStueckliste(slm.findById(rs.getInt("stueckliste")));

				User user = new User();
				user.setId(rs.getInt("userID"));
				user.setName(rs.getString("eMail"));
				user.setGoogleID(rs.getString("googleID"));
				baugruppe.setEditUser(user);

				/**
				 * Java Util Date wird umgewandelt in SQL Date um das
				 * Änderungsdatum in die Datenbank zu speichern.
				 */
				java.sql.Timestamp sqlDate = rs.getTimestamp("datum");
				baugruppe.setEditDate(sqlDate);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return baugruppe;
	}
}