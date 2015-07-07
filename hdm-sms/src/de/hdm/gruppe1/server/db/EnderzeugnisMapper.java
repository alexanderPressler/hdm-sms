package de.hdm.gruppe1.server.db;

import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;
import de.hdm.gruppe1.shared.bo.*;

/**
 * Mapper-Klasse, die Enderzeugnis-Objekte auf eine relationale Datenbank
 * abbildet (Google Cloud SQL). Hierzu wird eine Reihe von Methoden zur
 * Verfügung gestellt, mit deren Hilfe z.B. Objekte gesucht, erzeugt,
 * modifiziert und gelöscht werden können. Das Mapping ist bidirektional. D.h.,
 * Objekte können in DB-Strukturen und DB-Strukturen in Objekte umgewandelt
 * werden.
 * 
 * @author Andreas Herrmann (in Anlehnung an Hr. Thies)
 * 
 */
public class EnderzeugnisMapper {

	/**
	 * Die Klasse EnderzeugnisMapper wird nur einmal instantiiert. Man spricht
	 * hierbei von einem sogenannten Singleton (Autor: Hr. Thies).
	 */
	private static EnderzeugnisMapper enderzeugnisMapper = null;

	protected EnderzeugnisMapper() {

	}

	/**
	 * ?
	 * 
	 * @return enderzeugnisMapper
	 */
	public static EnderzeugnisMapper enderzeugnisMapper() {
		if (enderzeugnisMapper == null) {
			enderzeugnisMapper = new EnderzeugnisMapper();
		}
		return enderzeugnisMapper;
	}

	/**
	 * Die insert-Methode stellt die Möglichkeit zur Verfügung, ein
	 * Java-Enderzeugnis-Objekt in der Datenbank anzulegen.
	 * 
	 * @param enderzeugnis
	 * @return
	 */
	public Enderzeugnis insert(Enderzeugnis enderzeugnis) {
		Connection con = DBConnection.connection();

		try {
			Statement stmt = con.createStatement();

			/**
			 * Mithilfe dieses Abschnitts wird stets vor dem Anlegen eines
			 * Enderzeugnis-Objekts die derzeit höchste Id in der
			 * Datenbank-Tabelle abgefragt.
			 */
			ResultSet rs = stmt.executeQuery("SELECT MAX(ee_ID) AS maxid "
					+ "FROM Enderzeugnis;");

			/**
			 * Wenn wir etwas zurückerhalten, kann dies nur einzeilig sein.
			 */
			if (rs.next()) {

				/**
				 * Das neue Enderzeugnis erhält den bisher maximalen, nun um 1
				 * inkrementierten Primärschlüssel.
				 */
				enderzeugnis.setId(rs.getInt("maxid") + 1);

				/**
				 * Java Util Date wird umgewandelt in SQL Date um das
				 * Änderungsdatum in die Datenbank zu speichern.
				 */
				Date utilDate = enderzeugnis.getEditDate();
				java.sql.Timestamp sqlDate = new java.sql.Timestamp(
						utilDate.getTime());
				DateFormat df = new SimpleDateFormat("dd/MM/YYYY HH:mm:ss");
				df.format(sqlDate);
				enderzeugnis.setEditDate(sqlDate);

				/**
				 * Das SQL-Statement wird hier mithilfe der Objekt-Attribute
				 * erstellt und anschließend als Befehl an die Datenbank
				 * geschickt.
				 */
				stmt.executeUpdate("INSERT INTO Enderzeugnis VALUES ('"
						+ enderzeugnis.getId() + "','" + enderzeugnis.getName()
						+ "','" + enderzeugnis.getBaugruppe().getId() + "','"
						+ enderzeugnis.getEditUser().getId() + "','"
						+ enderzeugnis.getEditDate() + "');");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return enderzeugnis;
	}

	/**
	 * Mithilfe der update-Methode kann ein bestehendes Enderzeugnis-Objekt in
	 * der Datenbank geändert werden. Dabei werden alle Attribute neu
	 * überschrieben, bis auf die Objekt-Id.
	 * 
	 * @param enderzeugnis
	 * @return enderzeugnis
	 */
	public Enderzeugnis update(Enderzeugnis enderzeugnis) {
		Connection con = DBConnection.connection();

		try {

			/**
			 * Java Util Date wird umgewandelt in SQL Date um das Änderungsdatum
			 * in die Datenbank zu speichern.
			 */
			Date utilDate = enderzeugnis.getEditDate();
			java.sql.Timestamp sqlDate = new java.sql.Timestamp(
					utilDate.getTime());
			DateFormat df = new SimpleDateFormat("dd/MM/YYYY HH:mm:ss");
			df.format(sqlDate);
			enderzeugnis.setEditDate(sqlDate);

			Statement stmt = con.createStatement();

			/**
			 * Das SQL-Statement wird hier mithilfe der Objekt-Attribute
			 * erstellt und anschließend als Befehl an die Datenbank geschickt.
			 */
			stmt.executeUpdate("UPDATE Enderzeugnis SET name='"
					+ enderzeugnis.getName() + "', bearbeitet_Von='"
					+ enderzeugnis.getEditUser().getId() + "', datum='"
					+ enderzeugnis.getEditDate() + "' WHERE ee_ID='"
					+ enderzeugnis.getId() + "';");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return enderzeugnis;
	}

	/**
	 * Sofern der Benutzer ein Enderzeugnis-Objekt aus der Datenbank löschen
	 * möchte, wird dieser Befehl ausgeführt. Hierbei wird die Id des
	 * entsprechenden Objektes verwendet.
	 * 
	 * @param enderzeugnis
	 */
	public void delete(Enderzeugnis enderzeugnis) {
		Connection con = DBConnection.connection();

		try {
			Statement stmt = con.createStatement();

			/**
			 * Das SQL-Statement wird hier mithilfe der Objekt-Id erstellt und
			 * anschließend als Befehl an die Datenbank geschickt.
			 */
			stmt.executeUpdate("DELETE FROM Enderzeugnis WHERE ee_ID='"
					+ enderzeugnis.getId() + "';");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Um ein Enderzeugnis-Objekt mit einer spezifischen Id aus der Datenbank zu
	 * erhalten, wird die findByID-Methode aufgerufen. Sie liefert das gefundene
	 * Objekt zurück.
	 * 
	 * @param id
	 * @return enderzeugnis
	 */
	public Enderzeugnis findByID(int id) {
		Connection con = DBConnection.connection();

		Enderzeugnis enderzeugnis = null;
		try {
			Statement stmt = con.createStatement();

			/**
			 * Es wird die id des gesuchten Enderzeugnisses als SQL-Befehl an
			 * die Datenbank geschickt.
			 */
			ResultSet rs = stmt
					.executeQuery("SELECT * FROM Enderzeugnis WHERE ee_ID='"
							+ id + "';");

			/**
			 * Da es nur ein Enderzeugnis mit dieser ID geben kann ist davon
			 * auszugehen, dass das ResultSet nur eine Zeile enthält.
			 */
			if (rs.next()) {
				enderzeugnis = new Enderzeugnis();
				enderzeugnis.setId(rs.getInt("ee_ID"));
				enderzeugnis.setName(rs.getString("name"));

				/**
				 * Dem Enderzeugnis einen User zuordnen.
				 */
				int zugehörigeUserID = rs.getInt("bearbeitet_Von");
				UserMapper um = UserMapper.userMapper();
				User zugehörigerUser = um.findByID(zugehörigeUserID);
				enderzeugnis.setEditUser(zugehörigerUser);

				/**
				 * Dem Enderzeugnis eine Baugruppe zuordnen.
				 */
				int zugehörigeBaugruppeID = rs.getInt("baugruppe");
				BaugruppenMapper bm = BaugruppenMapper.baugruppenMapper();
				Baugruppe zugehörigeBaugruppe = bm
						.findByID(zugehörigeBaugruppeID);
				enderzeugnis.setBaugruppe(zugehörigeBaugruppe);

				/**
				 * Java Util Date wird umgewandelt in SQL Date um das
				 * Änderungsdatum in die Datenbank zu speichern.
				 */
				java.sql.Timestamp sqlDate = rs.getTimestamp("datum");
				enderzeugnis.setEditDate(sqlDate);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return enderzeugnis;
	}

	/**
	 * Mithilfe der findAll-Methode werden alle in der Datenbank vorhandenen
	 * Enderzeugnis-Objekte gesammelt in einem Vektor zurückgeliefert.
	 * 
	 * @return vEnderzeugnis
	 */
	public Vector<Enderzeugnis> findAll() {
		Vector<Enderzeugnis> vEnderzeugnis = new Vector<Enderzeugnis>();
		Connection con = DBConnection.connection();
		try {
			Statement stmt = con.createStatement();

			/**
			 * Es werden alle Inhalte der Enderzeugnis-Tabelle abgefragt.
			 */
			ResultSet rs = stmt.executeQuery("SELECT * FROM Enderzeugnis;");

			/**
			 * Da es viele Baugruppen geben kann müssen wir eine Schleife
			 * benutzen.
			 */
			while (rs.next()) {

				/**
				 * Eine neue Baugruppe erzeugen.
				 */
				Enderzeugnis enderzeugnis = new Enderzeugnis();
				enderzeugnis.setId(rs.getInt("ee_ID"));
				enderzeugnis.setName(rs.getString("name"));

				/**
				 * Java Util Date wird umgewandelt in SQL Date um das
				 * Änderungsdatum in die Datenbank zu speichern.
				 */
				java.sql.Timestamp sqlDate = rs.getTimestamp("datum");
				enderzeugnis.setEditDate(sqlDate);

				/**
				 * Dem Enderzeugnis einen User zuordnen.
				 */
				int zugehörigeUserID = rs.getInt("bearbeitet_Von");
				UserMapper um = UserMapper.userMapper();
				User zugehörigerUser = um.findByID(zugehörigeUserID);
				enderzeugnis.setEditUser(zugehörigerUser);

				/**
				 * Dem Enderzeugnis eine Baugruppe zuordnen.
				 */
				int zugehörigeBaugruppeID = rs.getInt("baugruppe");
				BaugruppenMapper bm = BaugruppenMapper.baugruppenMapper();
				Baugruppe zugehörigeBaugruppe = bm
						.findByID(zugehörigeBaugruppeID);
				enderzeugnis.setBaugruppe(zugehörigeBaugruppe);

				vEnderzeugnis.addElement(enderzeugnis);

			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return vEnderzeugnis;
	}

	/**
	 * Um die zugehörige Baugruppe eines vorhandenen Enderzeugnisses zu
	 * ermitteln, kann auf die findByBaugruppe- Methode zugegriffen werden.
	 * Hierfür wird die Baugruppen-Id benötigt, da diese in der Datenbank in der
	 * Enderzeugnis-Tabelle als Fremdschlüssel liegt. Es können mehrere
	 * Enderzeugnisse einer Baugruppe zugeordnet sein, daher liefert der
	 * return-Wert einen Vektor mit Enderzeugnissen zurück.
	 * 
	 * @param baugruppe
	 * @return vEnderzeugnis
	 */
	public Vector<Enderzeugnis> findByBaugruppe(Baugruppe baugruppe) {
		Vector<Enderzeugnis> vEnderzeugnis = new Vector<Enderzeugnis>();
		Connection con = DBConnection.connection();
		try {
			Statement stmt = con.createStatement();

			/**
			 * Das SQL-Statement wird hier mithilfe der Objekt-Id erstellt und
			 * anschließend als Befehl an die Datenbank geschickt.
			 */
			ResultSet rs = stmt
					.executeQuery("SELECT * FROM Enderzeugnis WHERE baugruppe='"
							+ baugruppe.getId() + "';");
			while (rs.next()) {
				vEnderzeugnis.add(this.findByID(rs.getInt("ee_ID")));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return vEnderzeugnis;
	}

	/**
	 * Mithilfe der findByName-Methode wird die Möglichkeit geschaffen, beim
	 * Anlegen oder Editieren eines Enderzeugnisses herauszufinden, ob ein
	 * Objekt mit dem identischen Namen bereits vorhanden ist. Es soll zu keiner
	 * Zeit möglich sein, dass es zwei Objekte mit identischem Namen in der
	 * Datenbank gibt.
	 * 
	 * @param name
	 * @return enderzeugnis
	 */
	public Enderzeugnis finByName(String name) {
		Connection con = DBConnection.connection();
		Enderzeugnis enderzeugnis = null;
		try {
			Statement stmt = con.createStatement();

			/**
			 * Das SQL-Statement wird hier mithilfe des Objekt-Namens erstellt
			 * und anschließend als Befehl an die Datenbank geschickt.
			 */
			ResultSet rs = stmt
					.executeQuery("SELECT * FROM Enderzeugnis WHERE name='"
							+ name + "';");
			if (rs.next()) {
				enderzeugnis = new Enderzeugnis();
				enderzeugnis.setId(rs.getInt("ee_ID"));
				enderzeugnis.setName(rs.getString("name"));

				/**
				 * Dem Enderzeugnis einen User zuordnen.
				 */
				int zugehörigeUserID = rs.getInt("bearbeitet_Von");
				UserMapper um = UserMapper.userMapper();
				User zugehörigerUser = um.findByID(zugehörigeUserID);
				enderzeugnis.setEditUser(zugehörigerUser);

				/**
				 * Dem Enderzeugnis eine Baugruppe zuordnen.
				 */
				int zugehörigeBaugruppeID = rs.getInt("baugruppe");
				BaugruppenMapper bm = BaugruppenMapper.baugruppenMapper();
				Baugruppe zugehörigeBaugruppe = bm
						.findByID(zugehörigeBaugruppeID);
				enderzeugnis.setBaugruppe(zugehörigeBaugruppe);

				/**
				 * Java Util Date wird umgewandelt in SQL Date um das
				 * Änderungsdatum in die Datenbank zu speichern.
				 */
				java.sql.Timestamp sqlDate = rs.getTimestamp("datum");
				enderzeugnis.setEditDate(sqlDate);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return enderzeugnis;
	}
}