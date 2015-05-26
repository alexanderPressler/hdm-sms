/**
 * 
 */
package de.hdm.gruppe1.server.db;

import java.sql.*;
import java.util.Vector;
import com.google.appengine.api.users.User;
import de.hdm.gruppe1.shared.bo.*;

/**
 * @author Andreas Herrmann
 *
 */
public class BaugruppenMapper {
	
	/**
	 * Die Klasse baugruppenMapper wird nur einmal instantiiert. Man spricht
	 * hierbei von einem sogenannten <b>Singleton</b>.
	 * <p>
	 * Diese Variable ist durch den Bezeichner <code>static</code> nur einmal
	 * für sämtliche eventuellen Instanzen dieser Klasse vorhanden. Sie
	 * speichert die einzige Instanz dieser Klasse.
	 * 
	 * @see baugruppenMapper()
	 */
	
	private static BaugruppenMapper baugruppenMapper = null;
	
	/**
	 * Geschützter Konstruktor - verhindert die Möglichkeit, mit
	 * <code>new</code> neue Instanzen dieser Klasse zu erzeugen.
	 */
	
	protected BaugruppenMapper(){
		
	}
	
	 /**
	   * Diese statische Methode kann aufgrufen werden durch
	   * <code>BaugruppenMapper.baugruppeMapper()</code>. Sie stellt die
	   * Singleton-Eigenschaft sicher, indem Sie dafür sorgt, dass nur eine einzige
	   * Instanz von <code>BaugruppeMapper</code> existiert.
	   * <p>
	   * 
	   * <b>Fazit:</b> BaugruppeMapper sollte nicht mittels <code>new</code>
	   * instantiiert werden, sondern stets durch Aufruf dieser statischen Methode.
	   * 
	   * @return DAS <code>BaugruppeMapper</code>-Objekt.
	   * @see baugruppeMapper
	   */
	
	public static BaugruppenMapper baugruppenMapper(){
		if(baugruppenMapper==null){
			baugruppenMapper= new BaugruppenMapper();
		}
		return baugruppenMapper;
	}
	
	/**
	   * Einfügen eines <code>Baugruppen</code>-Objekts in die Datenbank. Dabei wird
	   * auch der Primärschlüssel des übergebenen Objekts geprüft und ggf.
	   * berichtigt.
	   * 
	   * @param a das zu speichernde Objekt
	   * @return das bereits übergebene Objekt, jedoch mit ggf. korrigierter
	   *         <code>id</code>.
	   */

	
	public Baugruppe insert(Baugruppe baugruppe) {
		Connection con = DBConnection.connection();
	

		try{
			Statement stmt = con.createStatement();
			
			   /*
		       * Zunächst schauen wir nach, welches der momentan höchste
		       * Primärschlüsselwert ist.
		       */
			
			  ResultSet rs = stmt.executeQuery("SELECT MAX(teilnummer) AS maxid "
			          + "FROM Baugruppe ");
			  
			// Wenn wir etwas zurückerhalten, kann dies nur einzeilig sein
		      if (rs.next()) {
		        /*
		         * a erhält den bisher maximalen, nun um 1 inkrementierten
		         * Primärschlüssel.
		         */
		    	  baugruppe.setId(rs.getInt("maxid") + 1);

		        stmt = con.createStatement();

		        stmt.executeUpdate("INSERT INTO Baugruppe VALUES ('"+ baugruppe.getId() +"', '1', '"+ baugruppe.getName() +"', '2015-05-18 12:12:12');");
			      
		      }
		    }
		    catch (SQLException e2) {
		      e2.printStackTrace();
		    }

		    /*
		     * Rückgabe, des evtl. korrigierten Bauteil.
		     * 
		     * HINWEIS: Da in Java nur Referenzen auf Objekte und keine physischen
		     * Objekte übergeben werden, wäre die Anpassung des baugruppen-Objekts auch
		     * ohne diese explizite Rückgabe au�erhalb dieser Methode sichtbar. Die
		     * explizite Rückgabe von a ist eher ein Stilmittel, um zu signalisieren,
		     * dass sich das Objekt evtl. im Laufe der Methode verändert hat.
		     */
		    return baugruppe;
		  }
	  /**
	   * Wiederholtes Schreiben eines Objekts in die Datenbank.
	   * 
	   * @param a das Objekt, das in die DB geschrieben werden soll
	   * @return das als Parameter übergebene Objekt
	   */
	
	public Baugruppe update(Baugruppe baugruppe){
		Connection con = DBConnection.connection();
		
		
		Integer bId = new Integer(baugruppe.getId());

	    try {
	      Statement stmt = con.createStatement();

	      stmt.executeUpdate("UPDATE Baugruppe SET name='"+ baugruppe.getName() +"' WHERE teilnummer='"+bId.toString()+"';");

	    }
	    catch (SQLException e2) {
	      e2.printStackTrace();
	    }

	    // Um Analogie zu insert(Baugruppe a) zu wahren, geben wir a zurück
	    return baugruppe;
	  }
	
	/**
	   * Löschen der Daten eines <code>Baugruppe</code>-Objekts aus der Datenbank.
	   * 
	   * @param a das aus der DB zu löschende "Objekt"
	   */
	
	public void delete(Baugruppe baugruppe){
		Connection con = DBConnection.connection();

	    try {
	      Statement stmt = con.createStatement();
	      stmt.executeUpdate("DELETE FROM Baugruppe WHERE teilnummer ='"+ baugruppe.getId()+"'");

	    }
	    catch (SQLException e2) {
	      e2.printStackTrace();
	    }
	  }

	public Vector<Baugruppe> findAll() {
	    Connection con = DBConnection.connection();
	    // Ergebnisvektor vorbereiten
	    Vector<Baugruppe> result = new Vector<Baugruppe>();

	    try {
	      Statement stmt = con.createStatement();

	      //Ergebnis soll anhand der Id sortiert werden
	      ResultSet rs = stmt.executeQuery("SELECT * FROM `Baugruppe` ORDER BY `teilnummer`");

	      // Für jeden Eintrag im Suchergebnis wird nun ein Customer-Objekt
	      // erstellt.
	      while (rs.next()) {
	        Baugruppe baugruppe = new Baugruppe();
	        baugruppe.setId(rs.getInt("teilnummer"));
	        baugruppe.setName(rs.getString("name"));

	        // Hinzufügen des neuen Objekts zum Ergebnisvektor
	        result.addElement(baugruppe);
	        
	      }
	    }
	    catch (SQLException e) {
	      e.printStackTrace();
	    }

	    // Ergebnisvektor zurückgeben
	    return result;
	  }
	
	 /**
	 * Suchen einer Baugruppe mit vorgegebener Id. Da diese eindeutig
	 * ist, wird genau ein Objekt zur�ckgegeben.
	 * 
	 * @param id
	 *            Primärschlüsselattribut (->DB)
	 * @return Baugruppen-Objekt, das dem übergebenen Schlüssel entspricht, null bei
	 *         nicht vorhandenem DB-Tupel.
	 */
	
	public Baugruppe findById (int id){
		Connection con = DBConnection.connection();
	
		try{
			// Leeres SQL-Statement (JDBC) anlegen
			Statement stmt = con.createStatement();
			//Da ich ein int nicht einfach durch casting in einen String wandeln kann, muss dies über eine Instanz der Klasse Integer geschehen
			// Statement ausfüllen und als Query an die DB schicken
			// TODO: SQL Statement anpassen 
			ResultSet rs = stmt
					.executeQuery("SELECT id, name"
							+ "  FROM baugruppe "
							+ "WHERE id="
							+ id
							+ " ORDER BY name");
			// "SELECT * FROM `baugruppe` ORDER BY `name`"

			/*
			 * Da id Primärschlüssel ist, kann max. nur ein Tupel zurückgegeben
			 * werden. Prüfe, ob ein Ergebnis vorliegt.
			 */
			if (rs.next()) {
				// Ergebnis-Tupel in Objekt umwandeln
				Baugruppe baugruppe = new Baugruppe();
				baugruppe.setId(rs.getInt("id"));
				baugruppe.setName(rs.getString("name"));

				return baugruppe;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}

		return null;
	}
	
	public Vector<Baugruppe> findByName(String name){
		Vector<Baugruppe> vBaugruppe = new Vector<Baugruppe>();
		Connection con = DBConnection.connection();
		try{
		Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM Baugruppe JOIN User ON "
					+ "'Baugruppe.bearbeitet_Von'='User.userID' WHERE 'Baugruppe.name' LIKE '%"
				+name+"%';");
			//Da es viele Baugruppen geben kann, die diesen Namen haben müssen wir eine Schleife benutzen
			while(rs.next()){
				//Neue Baugruppe erzeugen
				Baugruppe baugruppe = new Baugruppe();
		    	baugruppe.setId(rs.getInt("bg_ID"));
		    	baugruppe.setName(rs.getString("name"));
		    	//Da wir die Stueckliste der Baugruppe auflösen müssen brauchen wir einen StuecklistenMapper
		    	StuecklisteMapper slm = StuecklisteMapper.stuecklisteMapper();
		    	baugruppe.setStueckliste(slm.findById(rs.getInt("stueckliste")));
		    
				//Baugruppe der ArrayList hinzufügen
				vBaugruppe.addElement(baugruppe);
			}
		}
		catch(SQLException e){
			e.printStackTrace();
		}
		return vBaugruppe;
	
}



	public Vector<Baugruppe> getAll() {
		
		return null;
	}}
