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
 * Mapper-Klasse, die <code>Steuckliste</code>-Objekte auf eine relationale
 * Datenbank abbildet. Hierzu wird eine Reihe von Methoden zur Verfügung
 * gestellt, mit deren Hilfe z.B. Objekte gesucht, erzeugt, modifiziert und
 * gelöscht werden können. Das Mapping ist bidirektional. D.h., Objekte können
 * in DB-Strukturen und DB-Strukturen in Objekte umgewandelt werden.
 * 
 * 
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
	public Stueckliste update(Stueckliste stueckliste){
		Connection con = DBConnection.connection();
		Stueckliste dBStueckliste = new Stueckliste();
		//Da ich ein int nicht einfach durch casting in einen String wandeln kann, muss dies über eine Instanz der Klasse Integer geschehen
		Integer stuecklistenID = new Integer(stueckliste.getId());
		Integer erstellerID = new Integer(stueckliste.getEditUser().getId());
		
   	  // Java Util Date wird umgewandelt in SQL Date um das Änderungsdatum in
  	  // die Datenbank zu speichern 
   	  Date utilDate = stueckliste.getEditDate();
   	  java.sql.Timestamp sqlDate = new java.sql.Timestamp(utilDate.getTime());  
   	  DateFormat df = new SimpleDateFormat("dd/MM/YYYY HH:mm:ss");
   	  df.format(sqlDate);
   	  
		try{
			Statement stmt = con.createStatement();
			
			//Das edit-Datum wird erst gesetzt, wenn der UPDATE-Vorgang beginnt
			stueckliste.setEditDate(sqlDate);
			
			//Zuerst die Daten der Stueckliste ändern
			stmt.executeUpdate("UPDATE Stueckliste SET name='"+stueckliste.getName()+"' ,datum='"
					+stueckliste.getEditDate().toString().substring(0,19)+"',ersteller='"+erstellerID.toString()+"',bearbeitet_Von='"
					+stueckliste.getEditUser().getId()+"' WHERE sl_ID='"+stuecklistenID.toString()+"';");
			//Stueckliste aus der DB abfragen
			dBStueckliste=this.findById(stueckliste.getId());
			//Zuerst schauen, ob Bauteile in der DB gelöscht werden müssen
			for(int i=0; i<dBStueckliste.getBauteilPaare().size();i++){
				Boolean exists=false;
				//Für jedes Paar in der DB schauen, ob es in der neuen Stueckliste existiert
				for(int j=0;j<stueckliste.getBauteilPaare().size();j++){
					if(stueckliste.getBauteilPaare().get(j).getElement().equals(dBStueckliste.getBauteilPaare().get(i).getElement())){
						exists=true;
					}
				}
				//Wenn das Bauteil in der DB existiert, aber nicht in der neuen Stueckliste, dann aus der DB löschen
				if(!exists){
					stmt.executeUpdate("DELETE FROM StuecklistenBauteile WHERE stueckliste='"+stueckliste.getId()+"' AND bauteil='"
							+dBStueckliste.getBauteilPaare().get(i).getElement().getId()+"';");
				}
			}
			//Dann schauen, ob Bauteile der Datenbank hinzugefügt werden müssen
			for(int i=0; i<stueckliste.getBauteilPaare().size();i++){
				Boolean exists=false;
				//Für jedes Paar in der neuen Stueckliste schauen, ob es in der DB existiert
				for(int j=0; j<dBStueckliste.getBauteilPaare().size();j++){
					if(dBStueckliste.getBauteilPaare().get(j).getElement().equals(stueckliste.getBauteilPaare().get(i).getElement())){
						//Überprüfen, ob die Anzahl übereinstimmt
						if(dBStueckliste.getBauteilPaare().get(j).getAnzahl()!=stueckliste.getBauteilPaare().get(i).getAnzahl()){
							//Wenn nicht, dann Anzahl ändern
							stmt.executeUpdate("UPDATE StuecklistenBauteile SET anzahl='"+stueckliste.getBauteilPaare().get(i).getAnzahl()
									+"' WHERE stueckliste='"+stueckliste.getId()
									+"' AND bauteil='"+stueckliste.getBauteilPaare().get(i).getElement().getId()+"';");
						}
						exists=true;
					}
				}
				//Wenn das Bauteil in der neuen Stueckliste ist, aber nicht in der DB, dann in die DB schreiben
				if(!exists){
					//MaxID von StuecklistenBauteile abfragen
					ResultSet rs = stmt.executeQuery("SELECT MAX(sbt_ID) AS maxid "
					          + "FROM StuecklistenBauteile;");

					     // Wenn wir etwas zurückerhalten, kann dies nur einzeilig sein
					     if (rs.next()) {
					        /*
					         * a erhält den bisher maximalen, nun um 1 inkrementierten
					         * Primärschlüssel.
					         */
					    	Integer sbt_ID = new Integer((rs.getInt("maxid")+1));
					    	//Bauteil hinzufügen
							stmt.executeUpdate("INSERT INTO StuecklistenBauteile VALUES('"+sbt_ID.toString()+"','"+stuecklistenID.toString()+"','"
									+stueckliste.getBauteilPaare().get(i).getAnzahl()+"','"+stueckliste.getBauteilPaare().get(i).getElement().getId()+"');");
					      }  
					
				}
			}
			
			//Zuerst schauen, ob Baugruppen in der DB gelöscht werden müssen
			for(int i=0; i<dBStueckliste.getBaugruppenPaare().size();i++){
				Boolean exists=false;
				//Für jedes Paar in der DB schauen, ob es in der neuen Stueckliste existiert
				for(int j=0;j<stueckliste.getBaugruppenPaare().size();j++){
					if(stueckliste.getBaugruppenPaare().get(j).getElement().equals(dBStueckliste.getBaugruppenPaare().get(i).getElement())){
						exists=true;
					}
				}
				//Wenn die Baugruppe in der DB existiert, aber nicht in der neuen Stueckliste, dann aus der DB löschen
				if(!exists){
					stmt.executeUpdate("DELETE FROM StuecklistenBaugruppe WHERE stueckliste='"+stueckliste.getId()+"' AND baugruppe='"
							+dBStueckliste.getBaugruppenPaare().get(i).getElement().getId()+"';");
				}
			}
			//Dann schauen, ob Baugruppen in der Datenbank hinzugefügt werden müssen
			for(int i=0; i<stueckliste.getBaugruppenPaare().size();i++){
				Boolean exists=false;
				//Für jedes Paar in der neuen Stueckliste schauen, ob es in der DB existiert
				for(int j=0; j<dBStueckliste.getBaugruppenPaare().size();j++){
					if(dBStueckliste.getBaugruppenPaare().get(j).getElement().equals(stueckliste.getBaugruppenPaare().get(i).getElement())){
						//Überprüfen, ob die Anzahl übereinstimmt
						if(dBStueckliste.getBaugruppenPaare().get(j).getAnzahl()!=stueckliste.getBaugruppenPaare().get(i).getAnzahl()){
							//Wenn nicht, dann Anzahl ändern
							stmt.executeUpdate("UPDATE StuecklistenBaugruppe SET anzahl='"+stueckliste.getBaugruppenPaare().get(i).getAnzahl()
									+"' WHERE stueckliste='"+stueckliste.getId()
									+"' AND baugruppe='"+stueckliste.getBaugruppenPaare().get(i).getElement().getId()+"';");
						}
						exists=true;
					}
				}
				//Wenn die Baugruppe in der neuen Stueckliste ist, aber nicht in der DB, dann in die DB schreiben
				if(!exists){
					//MaxID von StuecklistenBaugruppe abfragen
					ResultSet rs = stmt.executeQuery("SELECT MAX(sbt_ID) AS maxid "
					          + "FROM StuecklistenBaugruppe;");

					     // Wenn wir etwas zurückerhalten, kann dies nur einzeilig sein
					     if (rs.next()) {
					        /*
					         * a erhält den bisher maximalen, nun um 1 inkrementierten
					         * Primärschlüssel.
					         */
					    	Integer sbt_ID = new Integer((rs.getInt("maxid")+1));
					    	//Baugruppe hinzufügen
							stmt.executeUpdate("INSERT INTO StuecklistenBaugruppe VALUES('"+sbt_ID.toString()+"','"+stuecklistenID.toString()+"','"
									+stueckliste.getBaugruppenPaare().get(i).getAnzahl()+"','"+stueckliste.getBaugruppenPaare().get(i).getElement().getId()+"');");
					      }  
					
				}
			}
			
		}
		catch(SQLException e){
			e.printStackTrace();
		}
		
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

			//Zuerst die Elementzuordnungen der Stueckliste löschen
			//Bauteile
			stmt.executeUpdate("DELETE FROM StuecklistenBauteile WHERE stueckliste='"+stueckliste.getId()+"';");
			//Baugruppen
			stmt.executeUpdate("DELETE FROM StuecklistenBaugruppe WHERE stueckliste='"+stueckliste.getId()+"';");
			//Dann die Stueckliste löschen
			stmt.executeUpdate("DELETE FROM Stueckliste WHERE sl_ID='"+stueckliste.getId()+"';");
			
			

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
					.executeQuery("SELECT * FROM Stueckliste JOIN User ON Stueckliste.ersteller=User.userID ORDER BY sl_ID");

			// Für jeden Eintrag im Suchergebnis wird nun ein Customer-Objekt
			// erstellt.
			while (rs.next()) {
				Stueckliste stueckliste = new Stueckliste();
				stueckliste.setId(rs.getInt("sl_ID"));
				stueckliste.setName(rs.getString("name"));
				
				// Java Util Date wird umgewandelt in SQL Date um das Änderungsdatum in
		    	  // die Datenbank zu speichern 
		     	  java.sql.Timestamp sqlDate = rs.getTimestamp("datum");
		     	  stueckliste.setEditDate(sqlDate);
		     	  
					// Java Util Date wird umgewandelt in SQL Date um das Änderungsdatum in
		    	  // die Datenbank zu speichern 
		     	  java.sql.Timestamp sqlDateCD = rs.getTimestamp("creationDate");
		     	  stueckliste.setCreationDate(sqlDateCD);
			
				//TODO dynamisch anpassen
		        User editUser = new User();
		        editUser.setName(rs.getString("eMail"));
		        editUser.setId(rs.getInt("userID"));
		        editUser.setGoogleID(rs.getString("googleID"));
		        stueckliste.setEditUser(editUser);
		        
			      //Bauteile der Stueckliste abfragen
		        	Statement stmt2=con.createStatement();
					ResultSet rs2 = stmt2.executeQuery("SELECT * FROM Bauteile JOIN User ON "
							+ "Bauteile.bearbeitet_Von=User.userID "
							+ "JOIN StuecklistenBauteile "
							+ "ON StuecklistenBauteile.bauteil=Bauteile.teilnummer "
							+ "WHERE StuecklistenBauteile.stueckliste='"+stueckliste.getId()+"';");
					
					while(rs2.next()){
						//Letzter Aenderer anlegen
						User user = new User();
						user.setId(rs2.getInt("userID"));
						user.setName(rs2.getString("eMail"));
						user.setGoogleID(rs2.getString("googleID"));
						//Bauteil anlegen
						Bauteil bauteil = new Bauteil();
						bauteil.setId(rs2.getInt("teilnummer"));
						bauteil.setName(rs2.getString("name"));
						bauteil.setMaterialBeschreibung(rs2.getString("material"));
						bauteil.setBauteilBeschreibung(rs2.getString("beschreibung"));
						//User Objekt in bauteil einfügen
						bauteil.setEditUser(user);
						//Timestamp Objekt aus Datumsstring erzeugen, um es in bauteil einzufügen
						Timestamp timestamp = Timestamp.valueOf(rs2.getString("datum"));
						bauteil.setEditDate(timestamp);
						//StuecklistenPaar erstellen und bauteil hinzufügen
						ElementPaar bauteilPaar = new ElementPaar();
						bauteilPaar.setAnzahl(rs2.getInt("anzahl"));
						bauteilPaar.setElement(bauteil);
						//stuecklistenPaar der Stueckliste hinzufügen
						stueckliste.getBauteilPaare().add(bauteilPaar);
					}
					
					//Baugruppen der Stueckliste Abfragen
					Statement stmt3=con.createStatement();
					ResultSet rs3 = stmt3.executeQuery("SELECT * FROM Baugruppe JOIN User ON "
							+ "Baugruppe.bearbeitet_Von=User.userID "
							+ "JOIN StuecklistenBaugruppe "
							+ "ON StuecklistenBaugruppe.baugruppe=Baugruppe.bg_ID "
							+ "WHERE StuecklistenBaugruppe.stueckliste='"+stueckliste.getId()+"';");
					while(rs3.next()){
						//Letzter Aenderer anlegen
						User user = new User();
						user.setId(rs3.getInt("userID"));
						user.setName(rs3.getString("eMail"));
						user.setGoogleID(rs3.getString("googleID"));
						//Baugruppe anlegen
						Baugruppe baugruppe = new Baugruppe();
						baugruppe.setId(rs3.getInt("bg_ID"));
						baugruppe.setName(rs3.getString("name"));
						//User Objekt in baugruppe einfügen
						baugruppe.setEditUser(user);
						//Timestamp Objekt aus Datumsstring erzeugen, um es in bauteil einzufügen
						Timestamp timestamp = Timestamp.valueOf(rs3.getString("datum"));
						baugruppe.setEditDate(timestamp);
						//Stueckliste der Baugruppe abfragen und einfügen
						baugruppe.setStueckliste(findById(rs3.getInt("stueckliste")));
						//StuecklistenPaar erstellen und bauteil hinzufügen
						ElementPaar baugruppenPaar = new ElementPaar();
						baugruppenPaar.setAnzahl(rs3.getInt("anzahl"));
						baugruppenPaar.setElement(baugruppe);
						//Baugruppe der stueckliste hinzufügen
						stueckliste.getBaugruppenPaare().add(baugruppenPaar);
					}

				// Hinzufügen des neuen Objekts zum Ergebnisvektor
				result.addElement(stueckliste);

			}
		} 
		catch (SQLException e) {
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
					.executeQuery("SELECT * FROM Stueckliste JOIN User ON Stueckliste.ersteller=User.userID WHERE sl_ID="
							+ id + ";");
			
			// "SELECT * FROM `stuecklisten` ORDER BY `name`"

			/*
			 * Da id Primärschlüssel ist, kann max. nur ein Tupel zurückgegeben
			 * werden. Prüfe, ob ein Ergebnis vorliegt.
			 */
			if (rs.next()) {
				// Ergebnis-Tupel in Objekt umwandeln
				Stueckliste stueckliste = new Stueckliste();
				stueckliste.setId(rs.getInt("sl_ID"));
				stueckliste.setName(rs.getString("Stueckliste.name"));
				
				//TODO dynamisch anpassen
		        User editUser = new User();
		        
		        editUser.setName(rs.getString("User.eMail"));
		        editUser.setId(rs.getInt("userID"));
		        editUser.setGoogleID(rs.getString("googleID"));
		        stueckliste.setEditUser(editUser);
		        
		      //Bauteile der Stueckliste abfragen
				rs = stmt.executeQuery("SELECT * FROM Bauteile JOIN User ON "
						+ "Bauteile.bearbeitet_Von=User.userID "
						+ "JOIN StuecklistenBauteile "
						+ "ON StuecklistenBauteile.bauteil=Bauteile.teilnummer "
						+ "WHERE StuecklistenBauteile.stueckliste='"+stueckliste.getId()+"';");
				
				while(rs.next()){
					//Letzter Aenderer anlegen
					User user = new User();
					user.setId(rs.getInt("userID"));
					user.setName(rs.getString("eMail"));
					user.setGoogleID(rs.getString("googleID"));
					//Bauteil anlegen
					Bauteil bauteil = new Bauteil();
					bauteil.setId(rs.getInt("teilnummer"));
					bauteil.setName(rs.getString("name"));
					bauteil.setMaterialBeschreibung(rs.getString("material"));
					bauteil.setBauteilBeschreibung(rs.getString("beschreibung"));
					//User Objekt in bauteil einfügen
					bauteil.setEditUser(user);
					//Timestamp Objekt aus Datumsstring erzeugen, um es in bauteil einzufügen
					Timestamp timestamp = Timestamp.valueOf(rs.getString("datum"));
					bauteil.setEditDate(timestamp);
					//StuecklistenPaar erstellen und bauteil hinzufügen
					ElementPaar bauteilPaar = new ElementPaar();
					bauteilPaar.setAnzahl(rs.getInt("anzahl"));
					bauteilPaar.setElement(bauteil);
					//stuecklistenPaar der Stueckliste hinzufügen
					stueckliste.getBauteilPaare().add(bauteilPaar);
				}
				
				//Baugruppen der Stueckliste Abfragen
				rs = stmt.executeQuery("SELECT * FROM Baugruppe JOIN User ON "
						+ "Baugruppe.bearbeitet_Von=User.userID "
						+ "JOIN StuecklistenBaugruppe "
						+ "ON StuecklistenBaugruppe.baugruppe=Baugruppe.bg_ID "
						+ "WHERE StuecklistenBaugruppe.stueckliste='"+stueckliste.getId()+"';");
				while(rs.next()){
					//Letzter Aenderer anlegen
					User user = new User();
					user.setId(rs.getInt("userID"));
					user.setName(rs.getString("eMail"));
					user.setGoogleID(rs.getString("googleID"));
					//Baugruppe anlegen
					Baugruppe baugruppe = new Baugruppe();
					baugruppe.setId(rs.getInt("bg_ID"));
					baugruppe.setName(rs.getString("name"));
					//User Objekt in baugruppe einfügen
					baugruppe.setEditUser(user);
					//Timestamp Objekt aus Datumsstring erzeugen, um es in bauteil einzufügen
					Timestamp timestamp = Timestamp.valueOf(rs.getString("datum"));
					baugruppe.setEditDate(timestamp);
					//Stueckliste der Baugruppe abfragen und einfügen
					baugruppe.setStueckliste(findById(rs.getInt("stueckliste")));
					//StuecklistenPaar erstellen und bauteil hinzufügen
					ElementPaar baugruppenPaar = new ElementPaar();
					baugruppenPaar.setAnzahl(rs.getInt("anzahl"));
					baugruppenPaar.setElement(baugruppe);
					//Baugruppe der stueckliste hinzufügen
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
	
	public Vector<Stueckliste> findByBauteil (Bauteil bauteil){
		Vector<Stueckliste> vStueckliste = new Vector<Stueckliste>();
		Connection con = DBConnection.connection();
		try{
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM StuecklistenBauteile WHERE bauteil='"+bauteil.getId()+"';");
			while(rs.next()){
				vStueckliste.add(this.findById(rs.getInt("stueckliste")));
			}
		}
		catch(SQLException e){
			e.printStackTrace();
		}
		return vStueckliste;
	}
	public Vector<Stueckliste> findByBaugruppe (Baugruppe baugruppe){
		Vector<Stueckliste> vStueckliste = new Vector<Stueckliste>();
		Connection con = DBConnection.connection();
		try{
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM StuecklistenBaugruppe WHERE baugruppe='"+baugruppe.getId()+"';");
			while(rs.next()){
				vStueckliste.add(this.findById(rs.getInt("stueckliste")));
			}
		}
		catch(SQLException e){
			e.printStackTrace();
		}
		return vStueckliste;
 	}
	
}