package de.hdm.gruppe1.server.db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import de.hdm.gruppe1.shared.bo.User;

/**
 * Mapper-Klasse, die User-Objekte auf eine relationale Datenbank abbildet
 * (Google Cloud SQL). Hierzu wird eine Reihe von Methoden zur Verfügung
 * gestellt, mit deren Hilfe Objekte gesucht werden können. Das Mapping ist
 * bidirektional. D.h., Objekte können in DB-Strukturen und DB-Strukturen in
 * Objekte umgewandelt werden.
 * 
 * @author Andreas Herrmann (in Anlehnung an Hr. Thies)
 */
public class UserMapper {

	/**
	 * Die Klasse UserMapper wird nur einmal instantiiert. Man spricht hierbei
	 * von einem sogenannten Singleton (Autor: Hr. Thies).
	 */
	private static UserMapper userMapper = null;

	protected UserMapper() {
	}

	/**
	 * ?
	 * 
	 * @return stuecklisteMapper
	 */
	public static UserMapper userMapper() {
		if (userMapper == null) {
			userMapper = new UserMapper();
		}

		return userMapper;
	}

	/**
	 * Die insert-Methode stellt die Möglichkeit zur Verfügung, ein
	 * Java-User-Objekt in der Datenbank anzulegen.
	 * 
	 * @param user
	 * @return user
	 */
	public User insert(User user) {
		Connection con = DBConnection.connection();

		try {
			Statement stmt = con.createStatement();

			/**
			 * Mithilfe dieses Abschnitts wird stets vor dem Anlegen eines
			 * Baugruppen-Objekts die derzeit höchste Id in der
			 * Datenbank-Tabelle abgefragt.
			 */
			ResultSet rs = stmt.executeQuery("SELECT MAX(userID) AS maxid "
					+ "FROM User ");

			/**
			 * Wenn wir etwas zurückerhalten, kann dies nur einzeilig sein.
			 */
			if (rs.next()) {

				/**
				 * Der neue User erhält den bisher maximalen, nun um 1
				 * inkrementierten Primärschlüssel.
				 */
				user.setId(rs.getInt("maxid") + 1);

				stmt = con.createStatement();

				/**
				 * Das SQL-Statement wird hier mithilfe der Objekt-Attribute
				 * erstellt und anschließend als Befehl an die Datenbank
				 * geschickt.
				 */
				stmt.executeUpdate("INSERT INTO User VALUES ('" + user.getId()
						+ "','" + user.getGoogleID() + "','" + user.getName()
						+ "');");

			}
		} catch (SQLException e2) {
			e2.printStackTrace();
		}

		return user;
	}

	/**
	 * Um ein User-Objekt mit einer spezifischen Id aus der Datenbank zu
	 * erhalten, wird die findByID-Methode aufgerufen. Sie liefert das gefundene
	 * Objekt zurück.
	 * 
	 * @param id
	 * @return user
	 */
	public User findByID(int id) {

		/**
		 * Eine DB-Verbindung holen.
		 */
		Connection con = DBConnection.connection();
		User user = null;

		try {

			/**
			 * Ein leeres SQL-Statement (JDBC) anlegen.
			 */
			Statement stmt = con.createStatement();

			/**
			 * Der SQL-Befehl zum Abfragen eines Users anhand seiner
			 * spezifischen ID wird mithilfe der Objekt-Id von User erstellt und
			 * an die Datenbank geschickt.
			 */
			ResultSet rs = stmt
					.executeQuery("SELECT * FROM User WHERE userID ='" + id
							+ "';");

			/**
			 * Da es nur eine User mit dieser ID geben kann ist davon
			 * auszugehen, dass das ResultSet nur eine Zeile enthält.
			 */
			if (rs.next()) {
				user = new User();
				user.setId(rs.getInt("userID"));
				user.setName(rs.getString("eMail"));
				user.setGoogleID(rs.getString("googleID"));

			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return user;
	}

	/**
	 * Um ein User-Objekt mit einer spezifischen googleID aus der Datenbank zu
	 * erhalten, wird die findByGoogleID-Methode aufgerufen. Sie liefert das
	 * gefundene Objekt zurück.
	 * 
	 * @param googleID
	 * @return user
	 */
	public User findByGoogleID(String googleID) {

		/**
		 * Eine DB-Verbindung holen.
		 */
		Connection con = DBConnection.connection();
		User user = null;

		try {

			/**
			 * Ein leeres SQL-Statement (JDBC) anlegen.
			 */
			Statement stmt = con.createStatement();

			/**
			 * Der SQL-Befehl zum Abfragen eines Users anhand seiner
			 * spezifischen googleID wird mithilfe der Objekt-googleID von User
			 * erstellt und an die Datenbank geschickt.
			 */
			ResultSet rs = stmt
					.executeQuery("SELECT * FROM User WHERE googleID ='"
							+ googleID + "';");

			/**
			 * Da es nur einen User mit dieser ID geben kann ist davon
			 * auszugehen, dass das ResultSet nur eine Zeile enthält.
			 */
			if (rs.next()) {
				user = new User();
				user.setId(rs.getInt("userID"));
				user.setName(rs.getString("eMail"));
				user.setGoogleID(rs.getString("googleID"));

			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return user;
	}
}