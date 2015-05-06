package de.hdm.gruppe1.server.db;
import java.sql.*;

import de.hdm.gruppe1.shared.bo.*;


/**
 * Mapper-Klasse, die <code>Baugruppe</code>-Objekte auf eine relationale
 * Datenbank abbildet. Hierzu wird eine Reihe von Methoden zur Verfügung
 * gestellt, mit deren Hilfe z.B. Objekte gesucht, erzeugt, modifiziert und
 * gelöscht werden können. Das Mapping ist bidirektional. D.h., Objekte können
 * in DB-Strukturen und DB-Strukturen in Objekte umgewandelt werden.
 * @author Thies
 */
public class BaugruppeMapper {
	/**
	 * Die Klasse BaugruppeMapper wird nur einmal instantiiert. Man spricht
	 * hierbei von einem sogenannten <b>Singleton</b>.
	 * <p>
	 * Diese Variable ist durch den Bezeichner <code>static</code> nur einmal
	 * für sämtliche eventuellen Instanzen dieser Klasse vorhanden. Sie
	 * speichert die einzige Instanz dieser Klasse.
	 * 
	 * @see BaugruppeMapper()
	 */
	
	private static BaugruppeMapper baugruppeMapper = null;
	
	/**
	 * Geschützter Konstruktor - verhindert die Möglichkeit, mit
	 * <code>new</code> neue Instanzen dieser Klasse zu erzeugen.
	 */
	protected BaugruppeMapper() {
	}

	/**
	 * Diese statische Methode kann aufgrufen werden durch
	 * <code>BaugruppeMapper.baugruppeMapper()</code>. Sie stellt die
	 * Singleton-Eigenschaft sicher, indem Sie dafuer sorgt, dass nur eine
	 * einzige Instanz von <code>BaugruppeMapper</code> existiert.
	 * <p>
	 * 
	 * <b>Fazit:</b> BaugruppeMapper sollte nicht mittels <code>new</code>
	 * instantiiert werden, sondern stets durch Aufruf dieser statischen
	 * Methode.
	 * 
	 * @return DAS <code>BaugruppeMapper</code>-Objekt.
	 * @see baugruppeMapper
	 */
	
	public static BaugruppeMapper baugruppeMapper() {
		    if (baugruppeMapper == null) {
		    	baugruppeMapper = new BaugruppeMapper();
		    }

		    return baugruppeMapper;
	}
/**
 * Einfuegen eines <code>Baugruppe</code>-Objekts in die DB. Dabei wird
 * auch der Primärschlüssel des Uebergebenen Objekts geprueft und ggf.
 * berichtigt.
 * @return das bereits übergebene Objekt, jedoch mit ggf. korrigierter
 *         <code>id</code>.
 */
	public Baugruppe insert(Baugruppe baugruppe) {
		Connection con = DBConnection.connection();

		try {
			Statement stmt = con.createStatement();

			/*
			 * Zunächst schauen wir nach, welches der momentan höchste
			 * Primärschlüsselwert ist.
			 */
			ResultSet rs = stmt.executeQuery("SELECT MAX(id) AS maxid "
					+ "FROM baugruppe ");

			// Wenn wir etwas zurückerhalten, kann dies nur einzeilig sein
			if (rs.next()) {
				/*
				 * a erhält den bisher maximalen, nun um 1 inkrementierten
				 * Primärschlüssel.
				 */
				baugruppe.setId(rs.getInt("maxid") + 1);

				stmt = con.createStatement();

				// Jetzt erst erfolgt die tatsächliche Einfügeoperation
				stmt.executeUpdate("INSERT INTO `baugruppe`"
			+ " (`id`, `name` ) VALUES ('"+ baugruppe.getId() + "', '"
			+ baugruppe.getName() + "');");

			}
		} catch (SQLException e2) {
			e2.printStackTrace();
		}
	    /*
	     * Rückgabe, des evtl. korrigierten Accounts.
	     * 
	     * HINWEIS: Da in Java nur Referenzen auf Objekte und keine physischen
	     * Objekte übergeben werden, wäre die Anpassung des Baugruppe-Objekts auch
	     * ohne diese explizite Rückgabe au�erhalb dieser Methode sichtbar. Die
	     * explizite Rückgabe von baugruppe ist eher ein Stilmittel, um zu signalisieren,
	     * dass sich das Objekt evtl. im Laufe der Methode verändert hat.
	     */
		return baugruppe;
	}}
	
	
	
	
	