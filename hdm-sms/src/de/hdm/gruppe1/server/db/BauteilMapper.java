package de.hdm.gruppe1.server.db;

import java.sql.*;

import de.hdm.gruppe1.shared.bo.*;



/**
 * Mapper-Klasse, die <code>bauteil</code>-Objekte auf eine relationale
 * Datenbank abbildet. Hierzu wird eine Reihe von Methoden zur Verfügung
 * gestellt, mit deren Hilfe z.B. Objekte gesucht, erzeugt, modifiziert und
 * gelöscht werden können. Das Mapping ist bidirektional. D.h., Objekte können
 * in DB-Strukturen und DB-Strukturen in Objekte umgewandelt werden.
 * 
 * @see CustomerMapper, TransactionMapper
 * @author Thies
 */
public class BauteilMapper {

	/**
	 * Die Klasse bauteilMapper wird nur einmal instantiiert. Man spricht
	 * hierbei von einem sogenannten <b>Singleton</b>.
	 * <p>
	 * Diese Variable ist durch den Bezeichner <code>static</code> nur einmal
	 * für sämtliche eventuellen Instanzen dieser Klasse vorhanden. Sie
	 * speichert die einzige Instanz dieser Klasse.
	 * 
	 * @see bauteilMapper()
	 */
	private static BauteilMapper bauteilMapper = null;

	/**
	 * Geschützter Konstruktor - verhindert die Möglichkeit, mit
	 * <code>new</code> neue Instanzen dieser Klasse zu erzeugen.
	 */
	protected BauteilMapper() {
	}
	
	 /**
	   * Diese statische Methode kann aufgrufen werden durch
	   * <code>BauteilMapper.bauteilMapper()</code>. Sie stellt die
	   * Singleton-Eigenschaft sicher, indem Sie dafür sorgt, dass nur eine einzige
	   * Instanz von <code>BauteilMapper</code> existiert.
	   * <p>
	   * 
	   * <b>Fazit:</b> BauteilMapper sollte nicht mittels <code>new</code>
	   * instantiiert werden, sondern stets durch Aufruf dieser statischen Methode.
	   * 
	   * @return DAS <code>BauteilMapper</code>-Objekt.
	   * @see bauteilMapper
	   */
	  public static BauteilMapper bauteilMapper() {
	    if (bauteilMapper == null) {
	    	bauteilMapper = new BauteilMapper();
	    }

	    return bauteilMapper;
	  }
	  
	  /**
	   * Einfügen eines <code>Bauteil</code>-Objekts in die Datenbank. Dabei wird
	   * auch der Primärschlüssel des übergebenen Objekts geprüft und ggf.
	   * berichtigt.
	   * 
	   * @param a das zu speichernde Objekt
	   * @return das bereits übergebene Objekt, jedoch mit ggf. korrigierter
	   *         <code>id</code>.
	   */
	  public Bauteil insert(Bauteil a) {
	    Connection con = DBConnection.connection();

	    try {
	      Statement stmt = con.createStatement();

	      /*
	       * Zunächst schauen wir nach, welches der momentan höchste
	       * Primärschlüsselwert ist.
	       */
	      ResultSet rs = stmt.executeQuery("SELECT MAX(id) AS maxid "
	          + "FROM bauteile ");

	      // Wenn wir etwas zurückerhalten, kann dies nur einzeilig sein
	      if (rs.next()) {
	        /*
	         * a erhält den bisher maximalen, nun um 1 inkrementierten
	         * Primärschlüssel.
	         */
	        a.setId(rs.getInt("maxid") + 1);

	        stmt = con.createStatement();

	        // Jetzt erst erfolgt die tatsächliche Einfügeoperation
	        stmt.executeUpdate("INSERT INTO bauteile (id, name) " + "VALUES ("
	            + a.getId() + "," + a.getName() + ")");
	      }
	    }
	    catch (SQLException e2) {
	      e2.printStackTrace();
	    }

	    /*
	     * Rückgabe, des evtl. korrigierten Bauteil.
	     * 
	     * HINWEIS: Da in Java nur Referenzen auf Objekte und keine physischen
	     * Objekte übergeben werden, wäre die Anpassung des bauteil-Objekts auch
	     * ohne diese explizite Rückgabe au�erhalb dieser Methode sichtbar. Die
	     * explizite Rückgabe von a ist eher ein Stilmittel, um zu signalisieren,
	     * dass sich das Objekt evtl. im Laufe der Methode verändert hat.
	     */
	    return a;
	  }
	  
	  /**
	   * Wiederholtes Schreiben eines Objekts in die Datenbank.
	   * 
	   * @param a das Objekt, das in die DB geschrieben werden soll
	   * @return das als Parameter übergebene Objekt
	   */
	  public Bauteil update(Bauteil a) {
	    Connection con = DBConnection.connection();

	    try {
	      Statement stmt = con.createStatement();

	      stmt.executeUpdate("UPDATE bauteile " + "SET name=\"" + a.getName()
	          + "\" " + "WHERE id=" + a.getId());

	    }
	    catch (SQLException e2) {
	      e2.printStackTrace();
	    }

	    // Um Analogie zu insert(Bauteil a) zu wahren, geben wir a zurück
	    return a;
	  }
	  
	  /**
	   * Löschen der Daten eines <code>Bauteil</code>-Objekts aus der Datenbank.
	   * 
	   * @param a das aus der DB zu löschende "Objekt"
	   */
	  public void delete(Bauteil a) {
	    Connection con = DBConnection.connection();

	    try {
	      Statement stmt = con.createStatement();

	      stmt.executeUpdate("DELETE FROM bauteile " + "WHERE id=" + a.getId());

	    }
	    catch (SQLException e2) {
	      e2.printStackTrace();
	    }
	  }

}
