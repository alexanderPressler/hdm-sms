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
	  public Bauteil insert(Bauteil bauteil) {
	    Connection con = DBConnection.connection();

	    try {
	      Statement stmt = con.createStatement();

	      /*
	       * Zunächst schauen wir nach, welches der momentan höchste
	       * Primärschlüsselwert ist.
	       */
	      ResultSet rs = stmt.executeQuery("SELECT MAX(teilnummer) AS maxid "
	          + "FROM Bauteile ");

	      // Wenn wir etwas zurückerhalten, kann dies nur einzeilig sein
	      if (rs.next()) {
	        /*
	         * a erhält den bisher maximalen, nun um 1 inkrementierten
	         * Primärschlüssel.
	         */
	    	  bauteil.setId(rs.getInt("maxid") + 1);

	     	  // Java Util Date wird umgewandelt in SQL Date um das Änderungsdatum in
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
	     * Rückgabe, des evtl. korrigierten Bauteil.
	     * 
	     * HINWEIS: Da in Java nur Referenzen auf Objekte und keine physischen
	     * Objekte übergeben werden, wäre die Anpassung des bauteil-Objekts auch
	     * ohne diese explizite Rückgabe au�erhalb dieser Methode sichtbar. Die
	     * explizite Rückgabe von a ist eher ein Stilmittel, um zu signalisieren,
	     * dass sich das Objekt evtl. im Laufe der Methode verändert hat.
	     */
	    return bauteil;
	  }
	  
	  /**
	   * Wiederholtes Schreiben eines Objekts in die Datenbank.
	   * 
	   * @param a das Objekt, das in die DB geschrieben werden soll
	   * @return das als Parameter übergebene Objekt
	   */
	  public Bauteil update(Bauteil bauteil) {
	    Connection con = DBConnection.connection();
	    
	    Integer bId = new Integer(bauteil.getId());

	    try {
	      Statement stmt = con.createStatement();
	      
	   // Java Util Date wird umgewandelt in SQL Date um das Änderungsdatum in
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

	    // Um Analogie zu insert(Bauteil a) zu wahren, geben wir a zurück
	    return bauteil;
	  }
	  
	  /**
	   * Löschen der Daten eines <code>Bauteil</code>-Objekts aus der Datenbank.
	   * 
	   * @param a das aus der DB zu löschende "Objekt"
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
	   * @return Ein Vektor mit Customer-Objekten, die sämtliche Kunden
	   *         repräsentieren. Bei evtl. Exceptions wird ein partiell gef�llter
	   *         oder ggf. auch leerer Vetor zurückgeliefert.
	   */
	  public Vector<Bauteil> findAll() {
	    Connection con = DBConnection.connection();
	    // Ergebnisvektor vorbereiten
	    Vector<Bauteil> result = new Vector<Bauteil>();

	    try {
	      Statement stmt = con.createStatement();

	      //Ergebnis soll anhand der Id sortiert werden
	      ResultSet rs = stmt.executeQuery("SELECT * FROM Bauteile JOIN User ON Bauteile.bearbeitet_Von=User.userID ORDER BY teilnummer");

	      // Für jeden Eintrag im Suchergebnis wird nun ein Customer-Objekt
	      // erstellt.
	      while (rs.next()) {
	        Bauteil bauteil = new Bauteil();
	        bauteil.setId(rs.getInt("teilnummer"));
	        bauteil.setName(rs.getString("name"));
	        bauteil.setBauteilBeschreibung(rs.getString("beschreibung"));
	        bauteil.setMaterialBeschreibung(rs.getString("material"));
	        
	        // Java Util Date wird umgewandelt in SQL Date um das Änderungsdatum in
	    	 // die Datenbank zu speichern 
	     	 java.sql.Timestamp sqlDate = rs.getTimestamp("datum");
	     	 bauteil.setEditDate(sqlDate);  

				User editUser = new User();
				editUser.setName(rs.getString("User.eMail"));
		        editUser.setId(rs.getInt("userID"));
		        editUser.setGoogleID(rs.getString("googleID"));
		        bauteil.setEditUser(editUser);
	     	 
	        // Hinzufügen des neuen Objekts zum Ergebnisvektor
	        result.addElement(bauteil);
	        
	      }
	    }
	    catch (SQLException e) {
	      e.printStackTrace();
	    }

	    // Ergebnisvektor zurückgeben
	    return result;
	  }
	  
	  /**
		 * Suchen eines Bauteils mit vorgegebener Id. Da diese eindeutig
		 * ist, wird genau ein Objekt zur�ckgegeben.
		 * 
		 * @param id
		 *            Primärschlüsselattribut (->DB)
		 * @return Bauteil-Objekt, das dem übergebenen Schlüssel entspricht, null bei
		 *         nicht vorhandenem DB-Tupel.
		 */
		public Bauteil findById(int id) {
			// DB-Verbindung holen
			Connection con = DBConnection.connection();

			try {
				// Leeres SQL-Statement (JDBC) anlegen
				Statement stmt = con.createStatement();

				// Statement ausfüllen und als Query an die DB schicken
				ResultSet rs = stmt
						.executeQuery("SELECT * FROM Bauteile JOIN User ON Bauteile.bearbeitet_Von=User.userID WHERE teilnummer="
								+ id + ";");
				// "SELECT * FROM `bauteile` ORDER BY `name`"

				/*
				 * Da id Primärschlüssel ist, kann max. nur ein Tupel zurückgegeben
				 * werden. Prüfe, ob ein Ergebnis vorliegt.
				 */
				if (rs.next()) {
					// Ergebnis-Tupel in Objekt umwandeln
					Bauteil bauteil = new Bauteil();
					bauteil.setId(rs.getInt("teilnummer"));
					bauteil.setName(rs.getString("name"));
					bauteil.setBauteilBeschreibung(rs.getString("bauteilBeschreibung"));
					bauteil.setMaterialBeschreibung(rs.getString("materialBeschreibung"));
					
					User editUser = new User();
					editUser.setName(rs.getString("eMail"));
			        editUser.setId(rs.getInt("userID"));
			        editUser.setGoogleID(rs.getString("googleID"));
			        bauteil.setEditUser(editUser);
			        
			        // Java Util Date wird umgewandelt in SQL Date um das Änderungsdatum in
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
		public Bauteil finByName (String name){
			Connection con = DBConnection.connection();
			Bauteil bauteil = null;
			try{
				Statement stmt = con.createStatement();
				ResultSet rs = stmt.executeQuery("SELECT * FROM Bauteile JOIN User ON Bauteile.bearbeitet_Von=User.userID WHERE name='"+name+"';");
				if(rs.next()){
					bauteil = new Bauteil();
			    	bauteil.setId(rs.getInt("teilnummer"));
			    	bauteil.setName(rs.getString("name"));
			    	
			    	User user = new User();
			    	user.setId(rs.getInt("userID"));
			    	user.setName(rs.getString("eMail"));
			    	user.setGoogleID(rs.getString("googleID"));
			    	bauteil.setEditUser(user);
			    	// Java Util Date wird umgewandelt in SQL Date um das Änderungsdatum in
			    	 // die Datenbank zu speichern 
			     	 java.sql.Timestamp sqlDate = rs.getTimestamp("datum");
			     	bauteil.setEditDate(sqlDate);
				}
			}
			catch(SQLException e){
				e.printStackTrace();
			}
			return bauteil;
		}	
}