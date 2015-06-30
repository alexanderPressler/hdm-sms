package de.hdm.gruppe1.server.db;

import java.sql.*;
import java.text.DateFormat;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.Vector;

import com.google.gwt.i18n.shared.DateTimeFormat;

import de.hdm.gruppe1.shared.bo.*;

/**
 * Mapper-Klasse, die <code>bauteil</code>-Objekte auf eine relationale
 * Datenbank abbildet. Hierzu wird eine Reihe von Methoden zur Verf�gung
 * gestellt, mit deren Hilfe z.B. Objekte gesucht, erzeugt, modifiziert und
 * gel�scht werden k�nnen. Das Mapping ist bidirektional. D.h., Objekte k�nnen
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
	 * f�r s�mtliche eventuellen Instanzen dieser Klasse vorhanden. Sie
	 * speichert die einzige Instanz dieser Klasse.
	 * 
	 * @see bauteilMapper()
	 */
	private static BauteilMapper bauteilMapper = null;

	/**
	 * Gesch�tzter Konstruktor - verhindert die M�glichkeit, mit
	 * <code>new</code> neue Instanzen dieser Klasse zu erzeugen.
	 */
	protected BauteilMapper() {
	}
	
	 /**
	   * Diese statische Methode kann aufgrufen werden durch
	   * <code>BauteilMapper.bauteilMapper()</code>. Sie stellt die
	   * Singleton-Eigenschaft sicher, indem Sie daf�r sorgt, dass nur eine einzige
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
	   * Einf�gen eines <code>Bauteil</code>-Objekts in die Datenbank. Dabei wird
	   * auch der Prim�rschl�ssel des �bergebenen Objekts gepr�ft und ggf.
	   * berichtigt.
	   * 
	   * @param a das zu speichernde Objekt
	   * @return das bereits �bergebene Objekt, jedoch mit ggf. korrigierter
	   *         <code>id</code>.
	   */
	  public Bauteil insert(Bauteil bauteil) {
	    Connection con = DBConnection.connection();

	    try {
	      Statement stmt = con.createStatement();

	      /*
	       * Zun�chst schauen wir nach, welches der momentan h�chste
	       * Prim�rschl�sselwert ist.
	       */
	      ResultSet rs = stmt.executeQuery("SELECT MAX(teilnummer) AS maxid "
	          + "FROM Bauteile ");

	      // Wenn wir etwas zur�ckerhalten, kann dies nur einzeilig sein
	      if (rs.next()) {
	        /*
	         * a erh�lt den bisher maximalen, nun um 1 inkrementierten
	         * Prim�rschl�ssel.
	         */
	    	  bauteil.setId(rs.getInt("maxid") + 1);

	     	  // Java Util Date wird umgewandelt in SQL Date um das �nderungsdatum in
	    	  // die Datenbank zu speichern 
	     	  Date utilDate = bauteil.getEditDate();
	     	  java.sql.Timestamp sqlDate = new java.sql.Timestamp(utilDate.getTime());  
	     	  DateFormat df = new SimpleDateFormat("dd/MM/YYYY HH:mm:ss");
	     	  df.format(sqlDate);
	     	  
	     	  bauteil.setEditDate(sqlDate);
	    	  
	        stmt = con.createStatement();
	        stmt.executeUpdate("INSERT INTO Bauteile VALUES ('"+ bauteil.getId() +"', '"+ bauteil.getMaterialBeschreibung() +"', '"+bauteil.getEditUser().getId()+"', '"+ bauteil.getName() +"', '"+ bauteil.getBauteilBeschreibung() +"', '"+ bauteil.getEditDate() +"');");
	      
	      }
	    }
	    catch (SQLException e2) {
	      e2.printStackTrace();
	    }

	    /*
	     * R�ckgabe, des evtl. korrigierten Bauteil.
	     * 
	     * HINWEIS: Da in Java nur Referenzen auf Objekte und keine physischen
	     * Objekte �bergeben werden, w�re die Anpassung des bauteil-Objekts auch
	     * ohne diese explizite R�ckgabe au�erhalb dieser Methode sichtbar. Die
	     * explizite R�ckgabe von a ist eher ein Stilmittel, um zu signalisieren,
	     * dass sich das Objekt evtl. im Laufe der Methode ver�ndert hat.
	     */
	    return bauteil;
	  }
	  
	  /**
	   * Wiederholtes Schreiben eines Objekts in die Datenbank.
	   * 
	   * @param a das Objekt, das in die DB geschrieben werden soll
	   * @return das als Parameter �bergebene Objekt
	   */
	  public Bauteil update(Bauteil bauteil) {
	    Connection con = DBConnection.connection();
	    
	    Integer bId = new Integer(bauteil.getId());

	    try {
	      Statement stmt = con.createStatement();
	      
	   // Java Util Date wird umgewandelt in SQL Date um das �nderungsdatum in
    	  // die Datenbank zu speichern 
     	  Date utilDate = bauteil.getEditDate();
     	  java.sql.Timestamp sqlDate = new java.sql.Timestamp(utilDate.getTime());  
     	  DateFormat df = new SimpleDateFormat("dd/MM/YYYY HH:mm:ss");
     	  df.format(sqlDate);
     	  
     	  bauteil.setEditDate(sqlDate);
     	  
	      stmt.executeUpdate("UPDATE Bauteile SET "
	      		+"name='"+ bauteil.getName() 
	      		+"',beschreibung='"+ bauteil.getBauteilBeschreibung() 
	      		+"',material='"+ bauteil.getMaterialBeschreibung() 
	      		+"',bearbeitet_Von='"+ bauteil.getEditUser().getId()
	      		+"',datum='"+ bauteil.getEditDate() 
	      		+"' WHERE teilnummer='"+bId.toString()+"';");

	    }
	    catch (SQLException e2) {
	      e2.printStackTrace();
	    }

	    // Um Analogie zu insert(Bauteil a) zu wahren, geben wir a zur�ck
	    return bauteil;
	  }
	  
	  /**
	   * L�schen der Daten eines <code>Bauteil</code>-Objekts aus der Datenbank.
	   * 
	   * @param a das aus der DB zu l�schende "Objekt"
	   */
	  public void delete(Bauteil bauteil) {
	    Connection con = DBConnection.connection();

	    try {
	      Statement stmt = con.createStatement();

	      stmt.executeUpdate("DELETE FROM Bauteile WHERE teilnummer ='"+ bauteil.getId()+"'");

	    }
	    catch (SQLException e2) {
	      e2.printStackTrace();
	    }
	  }
	  
	  /**
	   * Auslesen aller Kunden.
	   * 
	   * @return Ein Vektor mit Customer-Objekten, die s�mtliche Kunden
	   *         repr�sentieren. Bei evtl. Exceptions wird ein partiell gef�llter
	   *         oder ggf. auch leerer Vetor zur�ckgeliefert.
	   */
	  public Vector<Bauteil> findAll() {
	    Connection con = DBConnection.connection();
	    // Ergebnisvektor vorbereiten
	    Vector<Bauteil> result = new Vector<Bauteil>();

	    try {
	      Statement stmt = con.createStatement();

	      //Ergebnis soll anhand der Id sortiert werden
	      ResultSet rs = stmt.executeQuery("SELECT * FROM Bauteile JOIN User ON Bauteile.bearbeitet_Von=User.userID ORDER BY teilnummer");

	      // F�r jeden Eintrag im Suchergebnis wird nun ein Customer-Objekt
	      // erstellt.
	      while (rs.next()) {
	        Bauteil bauteil = new Bauteil();
	        bauteil.setId(rs.getInt("teilnummer"));
	        bauteil.setName(rs.getString("name"));
	        bauteil.setBauteilBeschreibung(rs.getString("beschreibung"));
	        bauteil.setMaterialBeschreibung(rs.getString("material"));
	        
	        // Java Util Date wird umgewandelt in SQL Date um das �nderungsdatum in
	    	 // die Datenbank zu speichern 
	     	 java.sql.Timestamp sqlDate = rs.getTimestamp("datum");
	     	 bauteil.setEditDate(sqlDate);  

				User editUser = new User();
				editUser.setName(rs.getString("User.eMail"));
		        editUser.setId(rs.getInt("userID"));
		        editUser.setGoogleID(rs.getString("googleID"));
		        bauteil.setEditUser(editUser);
	     	 
	        // Hinzuf�gen des neuen Objekts zum Ergebnisvektor
	        result.addElement(bauteil);
	        
	      }
	    }
	    catch (SQLException e) {
	      e.printStackTrace();
	    }

	    // Ergebnisvektor zur�ckgeben
	    return result;
	  }
	  
	  /**
		 * Suchen eines Bauteils mit vorgegebener Id. Da diese eindeutig
		 * ist, wird genau ein Objekt zur�ckgegeben.
		 * 
		 * @param id
		 *            Prim�rschl�sselattribut (->DB)
		 * @return Bauteil-Objekt, das dem �bergebenen Schl�ssel entspricht, null bei
		 *         nicht vorhandenem DB-Tupel.
		 */
		public Bauteil findById(int id) {
			// DB-Verbindung holen
			Connection con = DBConnection.connection();

			try {
				// Leeres SQL-Statement (JDBC) anlegen
				Statement stmt = con.createStatement();

				// Statement ausf�llen und als Query an die DB schicken
				ResultSet rs = stmt
						.executeQuery("SELECT * FROM Bauteile JOIN User ON Bauteile.bearbeitet_Von=User.userID WHERE teilnummer="
								+ id + ";");
				// "SELECT * FROM `bauteile` ORDER BY `name`"

				/*
				 * Da id Prim�rschl�ssel ist, kann max. nur ein Tupel zur�ckgegeben
				 * werden. Pr�fe, ob ein Ergebnis vorliegt.
				 */
				if (rs.next()) {
					// Ergebnis-Tupel in Objekt umwandeln
					Bauteil bauteil = new Bauteil();
					bauteil.setId(rs.getInt("id"));
					bauteil.setName(rs.getString("name"));
					bauteil.setBauteilBeschreibung(rs.getString("bauteilBeschreibung"));
					bauteil.setMaterialBeschreibung(rs.getString("materialBeschreibung"));
					
					User editUser = new User();
					editUser.setName(rs.getString("eMail"));
			        editUser.setId(rs.getInt("userID"));
			        editUser.setGoogleID(rs.getString("googleID"));
			        bauteil.setEditUser(editUser);
			        
			        // Java Util Date wird umgewandelt in SQL Date um das �nderungsdatum in
			    	 // die Datenbank zu speichern 
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

}
