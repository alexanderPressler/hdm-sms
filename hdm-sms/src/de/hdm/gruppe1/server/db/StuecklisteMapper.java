package de.hdm.gruppe1.server.db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;

import de.hdm.gruppe1.shared.bo.Stueckliste;
import de.hdm.gruppe1.shared.bo.User;

/**
 * Mapper-Klasse, die <code>Steuckliste</code>-Objekte auf eine relationale
 * Datenbank abbildet. Hierzu wird eine Reihe von Methoden zur Verfügung
 * gestellt, mit deren Hilfe z.B. Objekte gesucht, erzeugt, modifiziert und
 * gelöscht werden können. Das Mapping ist bidirektional. D.h., Objekte können
 * in DB-Strukturen und DB-Strukturen in Objekte umgewandelt werden.
 * 
 * @see CustomerMapper, TransactionMapper
 * @author Thies
 */
public class StuecklisteMapper {

	/**
	 * Die Klasse StuecklisteMapper wird nur einmal instantiiert. Man spricht
	 * hierbei von einem sogenannten <b>Singleton</b>.
	 * <p>
	 * Diese Variable ist durch den Bezeichner <code>static</code> nur einmal
	 * für sämtliche eventuellen Instanzen dieser Klasse vorhanden. Sie
	 * speichert die einzige Instanz dieser Klasse.
	 * 
	 * @see StuecklisteMapper()
	 */
	private static StuecklisteMapper stuecklisteMapper = null;

	/**
	 * Geschützter Konstruktor - verhindert die Möglichkeit, mit
	 * <code>new</code> neue Instanzen dieser Klasse zu erzeugen.
	 */
	protected StuecklisteMapper() {
	}

	/**
	 * Diese statische Methode kann aufgrufen werden durch
	 * <code>StuecklisteMapper.StuecklisteMapper()</code>. Sie stellt die
	 * Singleton-Eigenschaft sicher, indem Sie dafür sorgt, dass nur eine
	 * einzige Instanz von <code>StuecklisteMapper</code> existiert.
	 * <p>
	 * 
	 * <b>Fazit:</b> StuecklisteMapper sollte nicht mittels <code>new</code>
	 * instantiiert werden, sondern stets durch Aufruf dieser statischen
	 * Methode.
	 * 
	 * @return DAS <code>StuecklisteMapper</code>-Objekt.
	 * @see StuecklisteMapper
	 */
	public static StuecklisteMapper stuecklisteMapper() {
		if (stuecklisteMapper == null) {
			stuecklisteMapper = new StuecklisteMapper();
		}

		return stuecklisteMapper;
	}
	
	/**
	 * Einfügen eines <code>Stueckliste</code>-Objekts in die Datenbank. Dabei wird
	 * auch der Primärschlüssel des übergebenen Objekts geprüft und ggf.
	 * berichtigt.
	 * 
	 * @param a
	 *            das zu speichernde Objekt
	 * @return das bereits übergebene Objekt, jedoch mit ggf. korrigierter
	 *         <code>id</code>.
	 */
	public Stueckliste insert(Stueckliste stueckliste) {
		Connection con = DBConnection.connection();
		
		//Da ich ein int nicht einfach durch casting in einen String wandeln kann, muss dies über eine Instanz der Klasse Integer geschehen
		Integer erstellerID = new Integer(stueckliste.getEditUser().getId());

		try {
			Statement stmt = con.createStatement();

			/*
			 * Zunächst schauen wir nach, welches der momentan höchste
			 * Primärschlüsselwert ist.
			 */
			ResultSet rs = stmt.executeQuery("SELECT MAX(sl_ID) AS maxid "
					+ "FROM Stueckliste ");

			// Wenn wir etwas zurückerhalten, kann dies nur einzeilig sein
			if (rs.next()) {
				/*
				 * a erhält den bisher maximalen, nun um 1 inkrementierten
				 * Primärschlüssel.
				 */
				stueckliste.setId(rs.getInt("maxid") + 1);
				
		     	  // Java Util Date wird umgewandelt in SQL Date um das Änderungsdatum in
		    	  // die Datenbank zu speichern 
		     	  Date utilDate = stueckliste.getEditDate();
		     	  java.sql.Timestamp sqlDate = new java.sql.Timestamp(utilDate.getTime());  
		     	  DateFormat df = new SimpleDateFormat("dd/MM/YYYY HH:mm:ss");
		     	  df.format(sqlDate);
		     	  
		     	  
		     	  stueckliste.setEditDate(sqlDate);
		     	  stueckliste.setCreationDate(sqlDate);


				stmt = con.createStatement();

				//TODO Tabellenspalte "ersteller" aus DB löschen. Anschließend Statement hier anpassen (herauslöschen)
				// Jetzt erst erfolgt die tatsächliche Einfügeoperation
				stmt.executeUpdate("INSERT INTO `Stueckliste` VALUES ('"+ stueckliste.getId() +"', '"+  stueckliste.getName()  +"', '"+ stueckliste.getEditDate() +"', '"+ stueckliste.getEditUser().getId() +"', '"+ stueckliste.getEditUser().getId() +"', '"+ stueckliste.getCreationDate() +"');");
			
				//Bauteile hinzufügen
				for(int i=0;i<stueckliste.getBauteilPaare().size();i++){
					//Da ich ein int nicht einfach durch casting in einen String wandeln kann, muss dies über eine Instanz der Klasse Integer geschehen
					Integer stuecklistenID = new Integer(stueckliste.getId());
					Integer anzahl = new Integer(stueckliste.getBauteilPaare().get(i).getAnzahl());
					Integer elementID = new Integer(stueckliste.getBauteilPaare().get(i).getElement().getId());
					//MaxID von StuecklistenBauteile abfragen
					rs = stmt.executeQuery("SELECT MAX(sbt_ID) AS maxid "
					          + "FROM StuecklistenBauteile;");

					     // Wenn wir etwas zurückerhalten, kann dies nur einzeilig sein
					     if (rs.next()) {
					        /*
					         * a erhält den bisher maximalen, nun um 1 inkrementierten
					         * Primärschlüssel.
					         */
					    	 Integer sbt_ID = new Integer((rs.getInt("maxid")+1));
					    	 
						     //Bauteil hinzufügen
						     stmt.executeUpdate("INSERT INTO StuecklistenBauteile VALUES('"+sbt_ID.toString()+"','"+stuecklistenID+"','"
						    		 +anzahl+"','"+elementID+"');");
					     }  

				}
				
				//Baugruppen hinzufügen
				for(int i=0;i<stueckliste.getBaugruppenPaare().size();i++){
					//Da ich ein int nicht einfach durch casting in einen String wandeln kann, muss dies über eine Instanz der Klasse Integer geschehen
					Integer stuecklistenID = new Integer(stueckliste.getId());
					Integer anzahl = new Integer(stueckliste.getBaugruppenPaare().get(i).getAnzahl());
					Integer elementID = new Integer(stueckliste.getBaugruppenPaare().get(i).getElement().getId());
					//MaxID von StuecklistenBauteile abfragen
					rs = stmt.executeQuery("SELECT MAX(sbg_ID) AS maxid "
					          + "FROM StuecklistenBaugruppe;");

					     // Wenn wir etwas zurückerhalten, kann dies nur einzeilig sein
					     if (rs.next()) {
					        /*
					         * a erhält den bisher maximalen, nun um 1 inkrementierten
					         * Primärschlüssel.
					         */
					    	Integer sbg_ID = new Integer((rs.getInt("maxid")+1));
					    	
							//Baugruppe hinzufügen
							stmt.executeUpdate("INSERT INTO StuecklistenBaugruppe VALUES('"+sbg_ID+"','"+stuecklistenID+"','"
									+anzahl+"','"+elementID+"');");
					      }  

				}
				
			}
		} 
		catch (SQLException e2) {
			e2.printStackTrace();
		}

		/*
		 * Rückgabe, des evtl. korrigierten Stueckliste.
		 * 
		 * HINWEIS: Da in Java nur Referenzen auf Objekte und keine physischen
		 * Objekte übergeben werden, wäre die Anpassung des Stueckliste-Objekts auch
		 * ohne diese explizite Rückgabe au�erhalb dieser Methode sichtbar. Die
		 * explizite Rückgabe von a ist eher ein Stilmittel, um zu
		 * signalisieren, dass sich das Objekt evtl. im Laufe der Methode
		 * verändert hat.
		 */
		return stueckliste;
	}
	
	/**
	 * Wiederholtes Schreiben eines Objekts in die Datenbank.
	 * 
	 * @param a
	 *            das Objekt, das in die DB geschrieben werden soll
	 * @return das als Parameter übergebene Objekt
	 */
	public Stueckliste update(Stueckliste stueckliste) {
		Connection con = DBConnection.connection();

		try {
			Statement stmt = con.createStatement();
			
			   // Java Util Date wird umgewandelt in SQL Date um das Änderungsdatum in
	    	  // die Datenbank zu speichern 
	     	  Date utilDate = stueckliste.getEditDate();
	     	  java.sql.Timestamp sqlDate = new java.sql.Timestamp(utilDate.getTime());  
	     	  DateFormat df = new SimpleDateFormat("dd/MM/YYYY HH:mm:ss");
	     	  df.format(sqlDate);
	     	  
	     	 stueckliste.setEditDate(sqlDate);

			stmt.executeUpdate("UPDATE Stueckliste SET `name`='" + stueckliste.getName()  +"',bearbeitet_Von='"+ stueckliste.getEditUser().getId() + "',datum='"+ stueckliste.getEditDate() +"' WHERE `sl_ID`='"+stueckliste.getId()+"';");

			//Bauteile der STueckliste aus der DB Abfragen
			ResultSet rs = stmt.executeQuery("SELECT * FROM StuecklistenBauteile WHERE stueckliste = '"+stueckliste.getId()+"';");
			System.out.println("Select 2");
			while(rs.next()){
				Boolean exists=false;
				int bauteil = rs.getInt("bauteil");
				//System.out.println("Schleife Inhalt: "+rs.getRow());
				for(int i=0; i<stueckliste.getBauteilPaare().size();i++){
					if(bauteil==stueckliste.getBauteilPaare().get(i).getElement().getId()){
						exists=true;
						break;
					}
					if(exists==false){
						stmt.executeUpdate("DELETE FROM StuecklistenBauteile WHERE 'sbt_ID'='"+rs.getInt("sbt_ID")+"';");
					}
				}
			}
			rs = stmt.executeQuery("SELECT * FROM StuecklistenBauteile WHERE stueckliste = '"+stueckliste.getId()+"';");
			for(int i=0;i<stueckliste.getBauteilPaare().size();i++){
				System.out.println("Schleife 1:"+i);
				Boolean exists= new Boolean(false);
				while(rs.next()){
					System.out.println("Schleife 2:"+rs.getRow());
					if(rs.getInt("bauteil")==stueckliste.getBauteilPaare().get(i).getElement().getId()){
						exists=true;
					}
				}
					System.out.println("vor einsetzen");
					if(exists==false){
						System.out.println("Einsetzen");
						//MaxID von StuecklistenBauteile abfragen
						ResultSet rs2 = stmt.executeQuery("SELECT MAX(sbt_ID) AS maxid "
						          + "FROM StuecklistenBauteile;");

						     // Wenn wir etwas zurückerhalten, kann dies nur einzeilig sein
						     if (rs2.next()) {
						        /*
						         * a erhält den bisher maximalen, nun um 1 inkrementierten
						         * Primärschlüssel.
						         */
						    	Integer sbt_ID = new Integer((rs2.getInt("maxid")+1));
						    	//Bauteil hinzufügen
						    	stmt.executeUpdate("INSERT INTO StuecklistenBauteile VALUES('"+sbt_ID+"','"+stueckliste.getId()+"','"
						    			+stueckliste.getBauteilPaare().get(i).getAnzahl()+"','"+stueckliste.getBauteilPaare().get(i).getElement().getId()+"');");
						     }
					}
				}

			
		} catch (SQLException e2) {
			e2.printStackTrace();
			System.out.println(e2);
		}

		// Um Analogie zu insert(Stueckliste a) zu wahren, geben wir a zurück
		return stueckliste;
	}
	
	/**
	 * Löschen der Daten eines <code>Stueckliste</code>-Objekts aus der Datenbank.
	 * 
	 * @param a
	 *            das aus der DB zu löschende "Objekt"
	 */
	public void delete(Stueckliste stueckliste) {
		Connection con = DBConnection.connection();

		try {
			Statement stmt = con.createStatement();

			stmt.executeUpdate("DELETE FROM Stueckliste WHERE sl_ID ='"+ stueckliste.getId()+"'");
			

		} catch (SQLException e2) {
			e2.printStackTrace();
		}
	}
	
	/**
	 * Auslesen aller Stuecklisten.
	 * 
	 * @return Ein Vektor mit Stueckliste-Objekten, die sämtliche Stuecklisten
	 *         repräsentieren. Bei evtl. Exceptions wird ein partiell gef�llter
	 *         oder ggf. auch leerer Vetor zurückgeliefert.
	 */
	public Vector<Stueckliste> findAll() {
		Connection con = DBConnection.connection();
		// Ergebnisvektor vorbereiten
		Vector<Stueckliste> result = new Vector<Stueckliste>();

		try {
			Statement stmt = con.createStatement();

			ResultSet rs = stmt
					.executeQuery("SELECT * FROM `Stueckliste` ORDER BY `sl_ID`");

			// Für jeden Eintrag im Suchergebnis wird nun ein Customer-Objekt
			// erstellt.
			while (rs.next()) {
				Stueckliste stueckliste = new Stueckliste();
				stueckliste.setId(rs.getInt("sl_ID"));
				stueckliste.setName(rs.getString("name"));
				
				// Java Util Date wird umgewandelt in SQL Date um das Änderungsdatum in
		    	  // die Datenbank zu speichern 
		     	  java.sql.Timestamp sqlDate = rs.getTimestamp("datum");
		     	  java.util.Date utilDate = new java.util.Date(sqlDate.getTime());  
		     	  DateFormat df = new SimpleDateFormat("dd/MM/YYYY HH:mm:ss");
		     	  df.format(utilDate);  
		     	  
		     	  stueckliste.setEditDate(utilDate);
		     	  
					// Java Util Date wird umgewandelt in SQL Date um das Änderungsdatum in
		    	  // die Datenbank zu speichern 
		     	  java.sql.Timestamp sqlDateCD = rs.getTimestamp("datum");
		     	  java.util.Date utilDateCD = new java.util.Date(sqlDateCD.getTime());  
		     	  DateFormat dfCD = new SimpleDateFormat("dd/MM/YYYY HH:mm:ss");
		     	  dfCD.format(utilDateCD);  
		     	  
		     	  stueckliste.setCreationDate(utilDateCD);
			
				//TODO dynamisch anpassen
		        User editUser = new User();
		        editUser.setName("statischer User");
		        editUser.setId(1);
		        editUser.setGoogleID("000000000000");
		        stueckliste.setEditUser(editUser);

				// Hinzufügen des neuen Objekts zum Ergebnisvektor
				result.addElement(stueckliste);

			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		// Ergebnisvektor zurückgeben
		return result;
	}
	
	/**
	 * Suchen eines Stuecklistes mit vorgegebener Id. Da diese eindeutig
	 * ist, wird genau ein Objekt zur�ckgegeben.
	 * 
	 * @param id
	 *            Primärschlüsselattribut (->DB)
	 * @return Stueckliste-Objekt, das dem übergebenen Schlüssel entspricht, null bei
	 *         nicht vorhandenem DB-Tupel.
	 */
	public Stueckliste findById(int id) {
		// DB-Verbindung holen
		Connection con = DBConnection.connection();

		try {
			// Leeres SQL-Statement (JDBC) anlegen
			Statement stmt = con.createStatement();

			// Statement ausfüllen und als Query an die DB schicken
			// TODO: SQL Statement anpassen 
			ResultSet rs = stmt
					.executeQuery("SELECT id, name"
							+ "  FROM stuecklisten "
							+ "WHERE id="
							+ id
							+ " ORDER BY name");
			// "SELECT * FROM `stuecklisten` ORDER BY `name`"

			/*
			 * Da id Primärschlüssel ist, kann max. nur ein Tupel zurückgegeben
			 * werden. Prüfe, ob ein Ergebnis vorliegt.
			 */
			if (rs.next()) {
				// Ergebnis-Tupel in Objekt umwandeln
				Stueckliste stueckliste = new Stueckliste();
				stueckliste.setId(rs.getInt("id"));
				stueckliste.setName(rs.getString("name"));
				
				//TODO dynamisch anpassen
		        User editUser = new User();
		        editUser.setName("statischer User");
		        editUser.setId(1);
		        editUser.setGoogleID("000000000000");
		        stueckliste.setEditUser(editUser);

				return stueckliste;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}

		return null;
	}
	
}