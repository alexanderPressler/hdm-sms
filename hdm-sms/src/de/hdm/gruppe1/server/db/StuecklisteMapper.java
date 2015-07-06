package de.hdm.gruppe1.server.db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;
import de.hdm.gruppe1.shared.bo.Baugruppe;
import de.hdm.gruppe1.shared.bo.Bauteil;
import de.hdm.gruppe1.shared.bo.ElementPaar;
import de.hdm.gruppe1.shared.bo.Stueckliste;
import de.hdm.gruppe1.shared.bo.User;

/**
 * Mapper-Klasse, die Stücklisten-Objekte auf eine relationale Datenbank
 * abbildet (Google Cloud SQL). Hierzu wird eine Reihe von Methoden zur
 * Verfügung gestellt, mit deren Hilfe z.B. Objekte gesucht, erzeugt,
 * modifiziert und gelöscht werden können. Das Mapping ist bidirektional. D.h.,
 * Objekte können in DB-Strukturen und DB-Strukturen in Objekte umgewandelt
 * werden.
 * 
 * @author Andreas Herrmann (in Anlehnung an Hr. Thies)
 */
public class StuecklisteMapper {

	/**
	 * Die Klasse BaugruppenMapper wird nur einmal instantiiert. Man spricht
	 * hierbei von einem sogenannten Singleton (Autor: Hr. Thies).
	 */
	private static StuecklisteMapper stuecklisteMapper = null;

	protected StuecklisteMapper() {
	}

	/**
	 * ?
	 * 
	 * @return stuecklisteMapper
	 */
	public static StuecklisteMapper stuecklisteMapper() {
		if (stuecklisteMapper == null) {
			stuecklisteMapper = new StuecklisteMapper();
		}

		return stuecklisteMapper;
	}

	/**
	 * Die insert-Methode stellt die Möglichkeit zur Verfügung, ein
	 * Java-Stücklisten-Objekt in der Datenbank anzulegen. Hierbei werden alle
	 * zugeordneten Baugruppen und Bauteile in die entsprechenden
	 * Zwischentabellen gespeichert.
	 * 
	 * @param stueckliste
	 * @return stueckliste
	 */
	public Stueckliste insert(Stueckliste stueckliste) {
		Connection con = DBConnection.connection();

		try {
			Statement stmt = con.createStatement();

			/**
			 * Mithilfe dieses Abschnitts wird stets vor dem Anlegen eines
			 * Baugruppen-Objekts die derzeit höchste Id in der
			 * Datenbank-Tabelle abgefragt.
			 */
			ResultSet rs = stmt.executeQuery("SELECT MAX(sl_ID) AS maxid "
					+ "FROM Stueckliste ");

			/**
			 * Wenn wir etwas zurückerhalten, kann dies nur einzeilig sein.
			 */
			if (rs.next()) {

				/**
				 * Die neue Baugruppe erhält den bisher maximalen, nun um 1
				 * inkrementierten Primärschlüssel.
				 */
				stueckliste.setId(rs.getInt("maxid") + 1);

				/**
				 * Java Util Date wird umgewandelt in SQL Date um das
				 * Änderungsdatum in die Datenbank zu speichern.
				 */
				Date utilDate = stueckliste.getEditDate();
				java.sql.Timestamp sqlDate = new java.sql.Timestamp(
						utilDate.getTime());
				DateFormat df = new SimpleDateFormat("dd/MM/YYYY HH:mm:ss");
				df.format(sqlDate);

				/**
				 * Da zum Zeitpunkt der Erstellung des Stücklisten-Datensatzes
				 * das Erstellungs-Datum identisch mit dem Änderungs-Datum ist,
				 * wird hier für beide Werte das identische Datum gesetzt. Beim
				 * späteren Editieren wird dann nur noch das Änderungs-Datum
				 * angepasst.
				 */
				stueckliste.setEditDate(sqlDate);
				stueckliste.setCreationDate(sqlDate);

				stmt = con.createStatement();

				/**
				 * Das SQL-Statement wird hier mithilfe der Objekt-Attribute
				 * erstellt und anschließend als Befehl an die Datenbank
				 * geschickt.
				 */
				stmt.executeUpdate("INSERT INTO `Stueckliste` VALUES ('"
						+ stueckliste.getId() + "', '" + stueckliste.getName()
						+ "', '" + stueckliste.getEditDate() + "', '"
						+ stueckliste.getEditUser().getId() + "', '"
						+ stueckliste.getCreationDate() + "');");

				/**
				 * Zugehörige Bauteile hinzufügen.
				 */
				for (int i = 0; i < stueckliste.getBauteilPaare().size(); i++) {

					/**
					 * Da ich ein int nicht einfach durch casting in einen
					 * String wandeln kann, muss dies über eine Instanz der
					 * Klasse Integer geschehen.
					 */
					Integer stuecklistenID = new Integer(stueckliste.getId());
					Integer anzahl = new Integer(stueckliste.getBauteilPaare()
							.get(i).getAnzahl());
					Integer elementID = new Integer(stueckliste
							.getBauteilPaare().get(i).getElement().getId());

					/**
					 * Mithilfe dieses Abschnitts wird stets vor dem Anlegen
					 * eines StuecklistenBauteil-Objekts die derzeit höchste Id
					 * in der Datenbank-Tabelle abgefragt.
					 */
					rs = stmt.executeQuery("SELECT MAX(sbt_ID) AS maxid "
							+ "FROM StuecklistenBauteile;");

					/**
					 * Wenn wir etwas zurückerhalten, kann dies nur einzeilig
					 * sein.
					 */
					if (rs.next()) {

						/**
						 * Das neue StuecklistenBauteil erhält den bisher
						 * maximalen, nun um 1 inkrementierten Primärschlüssel.
						 */
						Integer sbt_ID = new Integer((rs.getInt("maxid") + 1));

						/**
						 * Das SQL-Statement wird hier mithilfe der
						 * Objekt-Attribute erstellt und anschließend als Befehl
						 * an die Datenbank geschickt.
						 */
						stmt.executeUpdate("INSERT INTO StuecklistenBauteile VALUES('"
								+ sbt_ID.toString()
								+ "','"
								+ stuecklistenID
								+ "','" + anzahl + "','" + elementID + "');");
					}

				}

				/**
				 * Zugehörige Baugruppen hinzufügen.
				 */
				for (int i = 0; i < stueckliste.getBaugruppenPaare().size(); i++) {

					/**
					 * Da ich ein int nicht einfach durch casting in einen
					 * String wandeln kann, muss dies über eine Instanz der
					 * Klasse Integer geschehen.
					 */
					Integer stuecklistenID = new Integer(stueckliste.getId());
					Integer anzahl = new Integer(stueckliste
							.getBaugruppenPaare().get(i).getAnzahl());
					Integer elementID = new Integer(stueckliste
							.getBaugruppenPaare().get(i).getElement().getId());

					/**
					 * Mithilfe dieses Abschnitts wird stets vor dem Anlegen
					 * eines StuecklistenBaugruppe-Objekts die derzeit höchste
					 * Id in der Datenbank-Tabelle abgefragt.
					 */
					rs = stmt.executeQuery("SELECT MAX(sbg_ID) AS maxid "
							+ "FROM StuecklistenBaugruppe;");

					/**
					 * Wenn wir etwas zurückerhalten, kann dies nur einzeilig
					 * sein.
					 */
					if (rs.next()) {

						/**
						 * Die neue StuecklistenBaugruppe erhält den bisher
						 * maximalen, nun um 1 inkrementierten Primärschlüssel.
						 */
						Integer sbg_ID = new Integer((rs.getInt("maxid") + 1));

						/**
						 * Das SQL-Statement wird hier mithilfe der
						 * Objekt-Attribute erstellt und anschließend als Befehl
						 * an die Datenbank geschickt.
						 */
						stmt.executeUpdate("INSERT INTO StuecklistenBaugruppe VALUES('"
								+ sbg_ID
								+ "','"
								+ stuecklistenID
								+ "','"
								+ anzahl + "','" + elementID + "');");
					}

				}

			}
		} catch (SQLException e2) {
			e2.printStackTrace();
		}

		return stueckliste;
	}

	/**
	 * Mithilfe der update-Methode kann ein bestehendes Stücklisten-Objekt in
	 * der Datenbank geändert werden. Dabei werden alle Attribute neu
	 * überschrieben, bis auf die Objekt-Id.
	 * 
	 * @param stueckliste
	 * @return stueckliste
	 */
	public Stueckliste update(Stueckliste stueckliste) {
		Connection con = DBConnection.connection();
		Stueckliste dBStueckliste = new Stueckliste();

		try {
			Statement stmt = con.createStatement();

			/**
			 * Java Util Date wird umgewandelt in SQL Date um das Änderungsdatum
			 * in die Datenbank zu speichern.
			 */
			Date utilDate = stueckliste.getEditDate();
			java.sql.Timestamp sqlDate = new java.sql.Timestamp(
					utilDate.getTime());
			DateFormat df = new SimpleDateFormat("dd/MM/YYYY HH:mm:ss");
			df.format(sqlDate);
			stueckliste.setEditDate(sqlDate);

			/**
			 * Zuerst werden die Daten der Stueckliste geändert.
			 */
			stmt.executeUpdate("UPDATE Stueckliste SET name='"
					+ stueckliste.getName() + "' ,datum='"
					+ stueckliste.getEditDate() + "',bearbeitet_Von='"
					+ stueckliste.getEditUser().getId() + "' WHERE sl_ID='"
					+ stueckliste.getId() + "';");

			/**
			 * Die Stueckliste wird aus der DB abgefragt.
			 */
			dBStueckliste = this.findById(stueckliste.getId());

			/**
			 * Zuerst schauen, ob Bauteile in der DB gelöscht werden müssen.
			 */
			for (int i = 0; i < dBStueckliste.getBauteilPaare().size(); i++) {
				Boolean exists = false;

				/**
				 * Für jedes Paar in der DB schauen, ob es in der neuen
				 * Stueckliste existiert.
				 */
				for (int j = 0; j < stueckliste.getBauteilPaare().size(); j++) {
					if (stueckliste
							.getBauteilPaare()
							.get(j)
							.getElement()
							.equals(dBStueckliste.getBauteilPaare().get(i)
									.getElement())) {
						exists = true;
					}
				}

				/**
				 * Wenn das Bauteil in der DB existiert, aber nicht in der neuen
				 * Stueckliste, dann aus der DB löschen.
				 */
				if (!exists) {
					stmt.executeUpdate("DELETE FROM StuecklistenBauteile WHERE stueckliste='"
							+ stueckliste.getId()
							+ "' AND bauteil='"
							+ dBStueckliste.getBauteilPaare().get(i)
									.getElement().getId() + "';");
				}
			}

			/**
			 * Dann schauen, ob Bauteile der Datenbank hinzugefügt werden
			 * müssen.
			 */
			for (int i = 0; i < stueckliste.getBauteilPaare().size(); i++) {
				Boolean exists = false;
				/**
				 * Für jedes Paar in der neuen Stueckliste schauen, ob es in der
				 * DB existiert.
				 */
				for (int j = 0; j < dBStueckliste.getBauteilPaare().size(); j++) {
					if (dBStueckliste
							.getBauteilPaare()
							.get(j)
							.getElement()
							.equals(stueckliste.getBauteilPaare().get(i)
									.getElement())) {

						/**
						 * Überprüfen, ob die Anzahl übereinstimmt.
						 */
						if (dBStueckliste.getBauteilPaare().get(j).getAnzahl() != stueckliste
								.getBauteilPaare().get(i).getAnzahl()) {

							/**
							 * Wenn nicht, dann die Anzahl ändern.
							 */
							stmt.executeUpdate("UPDATE StuecklistenBauteile SET anzahl='"
									+ stueckliste.getBauteilPaare().get(i)
											.getAnzahl()
									+ "' WHERE stueckliste='"
									+ stueckliste.getId()
									+ "' AND bauteil='"
									+ stueckliste.getBauteilPaare().get(i)
											.getElement().getId() + "';");
						}
						exists = true;
					}
				}

				/**
				 * Wenn das Bauteil in der neuen Stueckliste ist, aber nicht in
				 * der DB, dann in die DB schreiben.
				 */
				if (!exists) {

					/**
					 * MaxID von StuecklistenBauteile abfragen.
					 */
					ResultSet rs = stmt
							.executeQuery("SELECT MAX(sbt_ID) AS maxid "
									+ "FROM StuecklistenBauteile;");

					/**
					 * Wenn wir etwas zurückerhalten, kann dies nur einzeilig
					 * sein.
					 */
					if (rs.next()) {

						/**
						 * Das neue StuecklistenBauteil erhält den bisher
						 * maximalen, nun um 1 inkrementierten Primärschlüssel.
						 */
						Integer sbt_ID = new Integer((rs.getInt("maxid") + 1));

						/**
						 * Zugehöriges Bauteil hinzufügen.
						 */
						stmt.executeUpdate("INSERT INTO StuecklistenBauteile VALUES('"
								+ sbt_ID
								+ "','"
								+ stueckliste.getId()
								+ "','"
								+ stueckliste.getBauteilPaare().get(i)
										.getAnzahl()
								+ "','"
								+ stueckliste.getBauteilPaare().get(i)
										.getElement().getId() + "');");
					}

				}
			}

			/**
			 * Zuerst schauen, ob Baugruppen in der DB gelöscht werden müssen.
			 */
			for (int i = 0; i < dBStueckliste.getBaugruppenPaare().size(); i++) {
				Boolean exists = false;

				/**
				 * Für jedes Paar in der DB schauen, ob es in der neuen
				 * Stueckliste existiert.
				 */
				for (int j = 0; j < stueckliste.getBaugruppenPaare().size(); j++) {
					if (stueckliste
							.getBaugruppenPaare()
							.get(j)
							.getElement()
							.equals(dBStueckliste.getBaugruppenPaare().get(i)
									.getElement())) {
						exists = true;
					}
				}

				/**
				 * Wenn die Baugruppe in der DB existiert, aber nicht in der
				 * neuen Stueckliste, dann aus der DB löschen.
				 */
				if (!exists) {
					stmt.executeUpdate("DELETE FROM StuecklistenBaugruppe WHERE stueckliste='"
							+ stueckliste.getId()
							+ "' AND baugruppe='"
							+ dBStueckliste.getBaugruppenPaare().get(i)
									.getElement().getId() + "';");
				}
			}

			/**
			 * Dann schauen, ob Baugruppen in der Datenbank hinzugefügt werden
			 * müssen.
			 */
			for (int i = 0; i < stueckliste.getBaugruppenPaare().size(); i++) {
				Boolean exists = false;

				/**
				 * Für jedes Paar in der neuen Stueckliste schauen, ob es in der
				 * DB existiert.
				 */
				for (int j = 0; j < dBStueckliste.getBaugruppenPaare().size(); j++) {
					if (dBStueckliste
							.getBaugruppenPaare()
							.get(j)
							.getElement()
							.equals(stueckliste.getBaugruppenPaare().get(i)
									.getElement())) {

						/**
						 * Überprüfen, ob die Anzahl übereinstimmt.
						 */
						if (dBStueckliste.getBaugruppenPaare().get(j)
								.getAnzahl() != stueckliste
								.getBaugruppenPaare().get(i).getAnzahl()) {

							/**
							 * Wenn nicht, dann die Anzahl ändern.
							 */
							stmt.executeUpdate("UPDATE StuecklistenBaugruppe SET anzahl='"
									+ stueckliste.getBaugruppenPaare().get(i)
											.getAnzahl()
									+ "' WHERE stueckliste='"
									+ stueckliste.getId()
									+ "' AND baugruppe='"
									+ stueckliste.getBaugruppenPaare().get(i)
											.getElement().getId() + "';");
						}
						exists = true;
					}
				}

				/**
				 * Wenn die Baugruppe in der neuen Stueckliste ist, aber nicht
				 * in der DB, dann in die DB schreiben.
				 */
				if (!exists) {

					/**
					 * MaxID von StuecklistenBaugruppe abfragen.
					 */
					ResultSet rs = stmt
							.executeQuery("SELECT MAX(sbg_ID) AS maxid "
									+ "FROM StuecklistenBaugruppe;");

					/**
					 * Wenn wir etwas zurückerhalten, kann dies nur einzeilig
					 * sein.
					 */
					if (rs.next()) {

						/**
						 * Die neue StuecklistenBaugruppe erhält den bisher
						 * maximalen, nun um 1 inkrementierten Primärschlüssel.
						 */
						Integer sbg_ID = new Integer((rs.getInt("maxid") + 1));

						/**
						 * Zugehörige Baugruppe hinzufügen.
						 */
						stmt.executeUpdate("INSERT INTO StuecklistenBaugruppe VALUES('"
								+ sbg_ID
								+ "','"
								+ stueckliste.getId()
								+ "','"
								+ stueckliste.getBaugruppenPaare().get(i)
										.getAnzahl()
								+ "','"
								+ stueckliste.getBaugruppenPaare().get(i)
										.getElement().getId() + "');");
					}

				}
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return stueckliste;
	}

	/**
	 * Sofern der Benutzer ein Stücklisten-Objekt aus der Datenbank löschen
	 * möchte, wird dieser Befehl ausgeführt. Hierbei wird die Id des
	 * entsprechenden Objektes verwendet. Es werden sowohl in der
	 * Stücklisten-Tabelle, als auch in der für die StücklistenBauteile und
	 * StücklistenBaugruppe Löschbefehle erstellt.
	 * 
	 * @param stueckliste
	 */
	public void delete(Stueckliste stueckliste) {
		Connection con = DBConnection.connection();

		try {
			Statement stmt = con.createStatement();

			/**
			 * Zuerst die Elementzuordnungen der Stueckliste löschen.
			 */
			stmt.executeUpdate("DELETE FROM StuecklistenBauteile WHERE stueckliste='"
					+ stueckliste.getId() + "';");
			stmt.executeUpdate("DELETE FROM StuecklistenBaugruppe WHERE stueckliste='"
					+ stueckliste.getId() + "';");

			/**
			 * Dann die Stueckliste löschen.
			 */
			stmt.executeUpdate("DELETE FROM Stueckliste WHERE sl_ID='"
					+ stueckliste.getId() + "';");

		} catch (SQLException e2) {
			e2.printStackTrace();
		}
	}

	/**
	 * Mithilfe der findAll-Methode werden alle in der Datenbank vorhandenen
	 * Baugruppen-Objekte gesammelt in einem Vektor zurückgeliefert.
	 * 
	 * @return result
	 */
	public Vector<Stueckliste> findAll() {
		Connection con = DBConnection.connection();

		/**
		 * Den Ergebnisvektor vorbereiten.
		 */
		Vector<Stueckliste> result = new Vector<Stueckliste>();

		try {
			Statement stmt = con.createStatement();

			/**
			 * Um den letzten Bearbeiter nicht nur als Id, sondern mit seiner
			 * E-Mail Adresse zu erhalten, wird dieser JOIN-Befehl benötigt.
			 */
			ResultSet rs = stmt
					.executeQuery("SELECT * FROM Stueckliste JOIN User ON Stueckliste.bearbeitet_Von=User.userID ORDER BY sl_ID");

			/**
			 * Für jeden Eintrag im Suchergebnis wird nun ein Customer-Objekt
			 * erstellt.
			 */
			while (rs.next()) {
				Stueckliste stueckliste = new Stueckliste();
				stueckliste.setId(rs.getInt("sl_ID"));
				stueckliste.setName(rs.getString("name"));

				/**
				 * Java Util Date wird umgewandelt in SQL Date um das
				 * Änderungsdatum in die Datenbank zu speichern.
				 */
				java.sql.Timestamp sqlDate = rs.getTimestamp("datum");
				stueckliste.setEditDate(sqlDate);

				/**
				 * Java Util Date wird umgewandelt in SQL Date um das
				 * Änderungsdatum in die Datenbank zu speichern.
				 */
				java.sql.Timestamp sqlDateCD = rs.getTimestamp("creationDate");
				stueckliste.setCreationDate(sqlDateCD);

				/**
				 * Der User wird in jedes Ergebnis geschrieben.
				 */
				User editUser = new User();
				editUser.setName(rs.getString("eMail"));
				editUser.setId(rs.getInt("userID"));
				editUser.setGoogleID(rs.getString("googleID"));
				stueckliste.setEditUser(editUser);

				/**
				 * Bauteile der Stueckliste abfragen.
				 */
				Statement stmt2 = con.createStatement();
				ResultSet rs2 = stmt2
						.executeQuery("SELECT * FROM Bauteile JOIN User ON "
								+ "Bauteile.bearbeitet_Von=User.userID "
								+ "JOIN StuecklistenBauteile "
								+ "ON StuecklistenBauteile.bauteil=Bauteile.teilnummer "
								+ "WHERE StuecklistenBauteile.stueckliste='"
								+ stueckliste.getId() + "';");

				while (rs2.next()) {
					/**
					 * Den Letzten Aenderer anlegen.
					 */
					User user = new User();
					user.setId(rs2.getInt("userID"));
					user.setName(rs2.getString("eMail"));
					user.setGoogleID(rs2.getString("googleID"));

					/**
					 * Das Bauteil anlegen.
					 */
					Bauteil bauteil = new Bauteil();
					bauteil.setId(rs2.getInt("teilnummer"));
					bauteil.setName(rs2.getString("name"));
					bauteil.setMaterialBeschreibung(rs2.getString("material"));
					bauteil.setBauteilBeschreibung(rs2
							.getString("beschreibung"));

					/**
					 * Das User Objekt in bauteil einfügen.
					 */
					bauteil.setEditUser(user);

					/**
					 * Ein Timestamp Objekt aus Datumsstring erzeugen, um es in
					 * bauteil einzufügen.
					 */
					Timestamp timestamp = Timestamp.valueOf(rs2
							.getString("datum"));
					bauteil.setEditDate(timestamp);

					/**
					 * Ein StuecklistenPaar erstellen und bauteil hinzufügen.
					 */
					ElementPaar bauteilPaar = new ElementPaar();
					bauteilPaar.setAnzahl(rs2.getInt("anzahl"));
					bauteilPaar.setElement(bauteil);

					/**
					 * Das stuecklistenPaar der Stueckliste hinzufügen.
					 */
					stueckliste.getBauteilPaare().add(bauteilPaar);
				}

				/**
				 * Die Baugruppen der Stueckliste Abfragen.
				 */
				Statement stmt3 = con.createStatement();
				ResultSet rs3 = stmt3
						.executeQuery("SELECT * FROM Baugruppe JOIN User ON "
								+ "Baugruppe.bearbeitet_Von=User.userID "
								+ "JOIN StuecklistenBaugruppe "
								+ "ON StuecklistenBaugruppe.baugruppe=Baugruppe.bg_ID "
								+ "WHERE StuecklistenBaugruppe.stueckliste='"
								+ stueckliste.getId() + "';");

				while (rs3.next()) {

					/**
					 * Den letzten Aenderer anlegen.
					 */
					User user = new User();
					user.setId(rs3.getInt("userID"));
					user.setName(rs3.getString("eMail"));
					user.setGoogleID(rs3.getString("googleID"));

					/**
					 * Die Baugruppe anlegen.
					 */
					Baugruppe baugruppe = new Baugruppe();
					baugruppe.setId(rs3.getInt("bg_ID"));
					baugruppe.setName(rs3.getString("name"));

					/**
					 * Das User Objekt in die baugruppe einfügen.
					 */
					baugruppe.setEditUser(user);

					/**
					 * Das Timestamp Objekt aus Datumsstring erzeugen, um es in
					 * bauteil einzufügen.
					 */
					Timestamp timestamp = Timestamp.valueOf(rs3
							.getString("datum"));
					baugruppe.setEditDate(timestamp);

					/**
					 * Die Stueckliste der Baugruppe abfragen und einfügen.
					 */
					baugruppe
							.setStueckliste(findById(rs3.getInt("stueckliste")));

					/**
					 * Ein StuecklistenPaar erstellen und bauteil hinzufügen.
					 */
					ElementPaar baugruppenPaar = new ElementPaar();
					baugruppenPaar.setAnzahl(rs3.getInt("anzahl"));
					baugruppenPaar.setElement(baugruppe);

					/**
					 * Die Baugruppe der stueckliste hinzufügen.
					 */
					stueckliste.getBaugruppenPaare().add(baugruppenPaar);
				}

				/**
				 * Hinzufügen des neuen Objekts zum Ergebnisvektor.
				 */
				result.addElement(stueckliste);

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
	 * Um ein Stücklisten-Objekt mit einer spezifischen Id aus der Datenbank zu
	 * erhalten, wird die findByID-Methode aufgerufen. Sie liefert das gefundene
	 * Objekt zurück.
	 * 
	 * @param id
	 * @return stueckliste (falls Ergebnis vorhanden)
	 */
	public Stueckliste findById(int id) {

		/**
		 * Eine DB-Verbindung holen.
		 */
		Connection con = DBConnection.connection();

		try {

			/**
			 * Ein leeres SQL-Statement (JDBC) anlegen.
			 */
			Statement stmt = con.createStatement();

			/**
			 * Um den letzten Bearbeiter nicht nur als Id, sondern mit seiner
			 * E-Mail Adresse darstellen zu können, wird dieser JOIN-Befehl
			 * benötigt.
			 */
			ResultSet rs = stmt
					.executeQuery("SELECT * FROM Stueckliste JOIN User ON Stueckliste.bearbeitet_Von=User.userID WHERE sl_ID='"
							+ id + "';");

			/**
			 * Da id Primärschlüssel ist, kann max. nur ein Tupel zurückgegeben
			 * werden. Prüfe, ob ein Ergebnis vorliegt.
			 */
			if (rs.next()) {

				/**
				 * Das Ergebnis-Tupel in ein Objekt umwandeln.
				 */
				Stueckliste stueckliste = new Stueckliste();
				stueckliste.setId(rs.getInt("sl_ID"));
				stueckliste.setName(rs.getString("Stueckliste.name"));
				
				/**
				 * Java Util Date wird umgewandelt in SQL Date um das
				 * Änderungsdatum in die Datenbank zu speichern.
				 */
				java.sql.Timestamp sqlDate = rs.getTimestamp("datum");
				stueckliste.setEditDate(sqlDate);

				/**
				 * Java Util Date wird umgewandelt in SQL Date um das
				 * Änderungsdatum in die Datenbank zu speichern.
				 */
				java.sql.Timestamp sqlDateCD = rs.getTimestamp("creationDate");
				stueckliste.setCreationDate(sqlDateCD);

				/**
				 * Den letzten Aenderer hinzufügen.
				 */
				User editUser = new User();
				editUser.setName(rs.getString("User.eMail"));
				editUser.setId(rs.getInt("userID"));
				editUser.setGoogleID(rs.getString("googleID"));
				stueckliste.setEditUser(editUser);

				/**
				 * Die Bauteile der Stueckliste abfragen.
				 */
				rs = stmt
						.executeQuery("SELECT * FROM Bauteile JOIN User ON "
								+ "Bauteile.bearbeitet_Von=User.userID "
								+ "JOIN StuecklistenBauteile "
								+ "ON StuecklistenBauteile.bauteil=Bauteile.teilnummer "
								+ "WHERE StuecklistenBauteile.stueckliste='"
								+ stueckliste.getId() + "';");

				while (rs.next()) {

					/**
					 * Den letzten Aenderer anlegen.
					 */
					User user = new User();
					user.setId(rs.getInt("userID"));
					user.setName(rs.getString("eMail"));
					user.setGoogleID(rs.getString("googleID"));

					/**
					 * Das Bauteil anlegen.
					 */
					Bauteil bauteil = new Bauteil();
					bauteil.setId(rs.getInt("teilnummer"));
					bauteil.setName(rs.getString("name"));
					bauteil.setMaterialBeschreibung(rs.getString("material"));
					bauteil.setBauteilBeschreibung(rs.getString("beschreibung"));

					/**
					 * Das User Objekt in bauteil einfügen.
					 */
					bauteil.setEditUser(user);

					/**
					 * Das Timestamp Objekt aus Datumsstring erzeugen, um es in
					 * bauteil einzufügen.
					 */
					Timestamp timestamp = Timestamp.valueOf(rs
							.getString("datum"));
					bauteil.setEditDate(timestamp);

					/**
					 * Das StuecklistenPaar erstellen und bauteil hinzufügen.
					 */
					ElementPaar bauteilPaar = new ElementPaar();
					bauteilPaar.setAnzahl(rs.getInt("anzahl"));
					bauteilPaar.setElement(bauteil);

					/**
					 * Das stuecklistenPaar der Stueckliste hinzufügen.
					 */
					stueckliste.getBauteilPaare().add(bauteilPaar);
				}

				/**
				 * Die Baugruppen der Stueckliste abfragen.
				 */
				rs = stmt.executeQuery("SELECT * FROM Baugruppe JOIN User ON "
						+ "Baugruppe.bearbeitet_Von=User.userID "
						+ "JOIN StuecklistenBaugruppe "
						+ "ON StuecklistenBaugruppe.baugruppe=Baugruppe.bg_ID "
						+ "WHERE StuecklistenBaugruppe.stueckliste='"
						+ stueckliste.getId() + "';");

				while (rs.next()) {

					/**
					 * Den letzten Aenderer anlegen.
					 */
					User user = new User();
					user.setId(rs.getInt("userID"));
					user.setName(rs.getString("eMail"));
					user.setGoogleID(rs.getString("googleID"));

					/**
					 * Die Baugruppe anlegen.
					 */
					Baugruppe baugruppe = new Baugruppe();
					baugruppe.setId(rs.getInt("bg_ID"));
					baugruppe.setName(rs.getString("name"));

					/**
					 * Das User Objekt in baugruppe einfügen.
					 */
					baugruppe.setEditUser(user);

					/**
					 * Das Timestamp Objekt aus Datumsstring erzeugen, um es in
					 * bauteil einzufügen.
					 */
					Timestamp timestamp = Timestamp.valueOf(rs
							.getString("datum"));
					baugruppe.setEditDate(timestamp);

					/**
					 * Die Stueckliste der Baugruppe abfragen und einfügen.
					 */
					baugruppe
							.setStueckliste(findById(rs.getInt("stueckliste")));

					/**
					 * Das StuecklistenPaar erstellen und bauteil hinzufügen.
					 */
					ElementPaar baugruppenPaar = new ElementPaar();
					baugruppenPaar.setAnzahl(rs.getInt("anzahl"));
					baugruppenPaar.setElement(baugruppe);

					/**
					 * Die Baugruppe der stueckliste hinzufügen.
					 */
					stueckliste.getBaugruppenPaare().add(baugruppenPaar);
				}

				return stueckliste;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}

		return null;
	}

	/**
	 * Um das zugehörige Bauteil einer vorhandenen Stückliste zu ermitteln, kann
	 * auf die findByBauteil- Methode zugegriffen werden. Hierfür wird die
	 * Bauteil-Id benötigt, da diese in der Datenbank in der
	 * StuecklistenBauteile-Tabelle als Fremdschlüssel liegt.
	 * 
	 * @param bauteil
	 * @return vStueckliste
	 */
	public Vector<Stueckliste> findByBauteil(Bauteil bauteil) {
		Vector<Stueckliste> vStueckliste = new Vector<Stueckliste>();
		Connection con = DBConnection.connection();
		try {
			Statement stmt = con.createStatement();

			/**
			 * Das SQL-Statement wird hier mithilfe der Objekt-Id erstellt und
			 * anschließend als Befehl an die Datenbank geschickt.
			 */
			ResultSet rs = stmt
					.executeQuery("SELECT * FROM StuecklistenBauteile WHERE bauteil='"
							+ bauteil.getId() + "';");

			while (rs.next()) {
				vStueckliste.add(this.findById(rs.getInt("stueckliste")));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return vStueckliste;
	}

	/**
	 * Um die zugehörige Baugruppe einer vorhandenen Stückliste zu ermitteln,
	 * kann auf die findByBaugruppe- Methode zugegriffen werden. Hierfür wird
	 * die Baugruppen-Id benötigt, da diese in der Datenbank in der
	 * StuecklistenBaugruppe-Tabelle als Fremdschlüssel liegt.
	 * 
	 * @param baugruppe
	 * @return
	 */
	public Vector<Stueckliste> findByBaugruppe(Baugruppe baugruppe) {
		Vector<Stueckliste> vStueckliste = new Vector<Stueckliste>();
		Connection con = DBConnection.connection();
		try {
			Statement stmt = con.createStatement();

			/**
			 * Das SQL-Statement wird hier mithilfe der Objekt-Id erstellt und
			 * anschließend als Befehl an die Datenbank geschickt.
			 */
			ResultSet rs = stmt
					.executeQuery("SELECT * FROM StuecklistenBaugruppe WHERE baugruppe='"
							+ baugruppe.getId() + "';");
			while (rs.next()) {
				vStueckliste.add(this.findById(rs.getInt("stueckliste")));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return vStueckliste;
	}

	/**
	 * Mithilfe der findByName-Methode wird die Möglichkeit geschaffen, beim
	 * Anlegen oder Editieren einer Stückliste herauszufinden, ob ein Objekt mit
	 * dem identischen Namen bereits vorhanden ist. Es soll zu keiner Zeit
	 * möglich sein, dass es zwei Objekte mit identischem Namen in der Datenbank
	 * gibt.
	 * 
	 * @param name
	 * @return stueckliste
	 */
	public Stueckliste finByName(String name) {
		Connection con = DBConnection.connection();
		Stueckliste stueckliste = null;
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
					.executeQuery("SELECT * FROM Stueckliste JOIN User ON Stueckliste.bearbeitet_Von=User.userID WHERE name='"
							+ name + "';");

			if (rs.next()) {

				stueckliste = new Stueckliste();
				stueckliste.setId(rs.getInt("sl_id"));
				stueckliste.setName(rs.getString("name"));

				User user = new User();
				user.setId(rs.getInt("userID"));
				user.setName(rs.getString("eMail"));
				user.setGoogleID(rs.getString("googleID"));
				stueckliste.setEditUser(user);

				/**
				 * Java Util Date wird umgewandelt in SQL Date um das
				 * Änderungsdatum in die Datenbank zu speichern.
				 */
				java.sql.Timestamp sqlDate = rs.getTimestamp("datum");
				stueckliste.setEditDate(sqlDate);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return stueckliste;
	}

}