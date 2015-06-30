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
 * Datenbank abbildet. Hierzu wird eine Reihe von Methoden zur Verf�gung
 * gestellt, mit deren Hilfe z.B. Objekte gesucht, erzeugt, modifiziert und
 * gel�scht werden k�nnen. Das Mapping ist bidirektional. D.h., Objekte k�nnen
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
	 * f�r s�mtliche eventuellen Instanzen dieser Klasse vorhanden. Sie
	 * speichert die einzige Instanz dieser Klasse.
	 * 
	 * @see StuecklisteMapper()
	 */
	private static StuecklisteMapper stuecklisteMapper = null;

	/**
	 * Gesch�tzter Konstruktor - verhindert die M�glichkeit, mit
	 * <code>new</code> neue Instanzen dieser Klasse zu erzeugen.
	 */
	protected StuecklisteMapper() {
	}

	/**
	 * Diese statische Methode kann aufgrufen werden durch
	 * <code>StuecklisteMapper.StuecklisteMapper()</code>. Sie stellt die
	 * Singleton-Eigenschaft sicher, indem Sie daf�r sorgt, dass nur eine
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
	 * Einf�gen eines <code>Stueckliste</code>-Objekts in die Datenbank. Dabei wird
	 * auch der Prim�rschl�ssel des �bergebenen Objekts gepr�ft und ggf.
	 * berichtigt.
	 * 
	 * @param a
	 *            das zu speichernde Objekt
	 * @return das bereits �bergebene Objekt, jedoch mit ggf. korrigierter
	 *         <code>id</code>.
	 */
	public Stueckliste insert(Stueckliste stueckliste) {
		Connection con = DBConnection.connection();
		
		//Da ich ein int nicht einfach durch casting in einen String wandeln kann, muss dies �ber eine Instanz der Klasse Integer geschehen
		Integer erstellerID = new Integer(stueckliste.getEditUser().getId());

		try {
			Statement stmt = con.createStatement();

			/*
			 * Zun�chst schauen wir nach, welches der momentan h�chste
			 * Prim�rschl�sselwert ist.
			 */
			ResultSet rs = stmt.executeQuery("SELECT MAX(sl_ID) AS maxid "
					+ "FROM Stueckliste ");

			// Wenn wir etwas zur�ckerhalten, kann dies nur einzeilig sein
			if (rs.next()) {
				/*
				 * a erh�lt den bisher maximalen, nun um 1 inkrementierten
				 * Prim�rschl�ssel.
				 */
				stueckliste.setId(rs.getInt("maxid") + 1);
				
		     	  // Java Util Date wird umgewandelt in SQL Date um das �nderungsdatum in
		    	  // die Datenbank zu speichern 
		     	  Date utilDate = stueckliste.getEditDate();
		     	  java.sql.Timestamp sqlDate = new java.sql.Timestamp(utilDate.getTime());  
		     	  DateFormat df = new SimpleDateFormat("dd/MM/YYYY HH:mm:ss");
		     	  df.format(sqlDate);
		     	  
		     	  
		     	  stueckliste.setEditDate(sqlDate);
		     	  stueckliste.setCreationDate(sqlDate);


				stmt = con.createStatement();

				//TODO Tabellenspalte "ersteller" aus DB l�schen. Anschlie�end Statement hier anpassen (herausl�schen)
				// Jetzt erst erfolgt die tats�chliche Einf�geoperation
				stmt.executeUpdate("INSERT INTO `Stueckliste` VALUES ('"+ stueckliste.getId() +"', '"+  stueckliste.getName()  +"', '"+ stueckliste.getEditDate() +"', '"+ stueckliste.getEditUser().getId() +"', '"+ stueckliste.getCreationDate() +"');");
			
				//Bauteile hinzuf�gen
				for(int i=0;i<stueckliste.getBauteilPaare().size();i++){
					//Da ich ein int nicht einfach durch casting in einen String wandeln kann, muss dies �ber eine Instanz der Klasse Integer geschehen
					Integer stuecklistenID = new Integer(stueckliste.getId());
					Integer anzahl = new Integer(stueckliste.getBauteilPaare().get(i).getAnzahl());
					Integer elementID = new Integer(stueckliste.getBauteilPaare().get(i).getElement().getId());
					//MaxID von StuecklistenBauteile abfragen
					rs = stmt.executeQuery("SELECT MAX(sbt_ID) AS maxid "
					          + "FROM StuecklistenBauteile;");

					     // Wenn wir etwas zur�ckerhalten, kann dies nur einzeilig sein
					     if (rs.next()) {
					        /*
					         * a erh�lt den bisher maximalen, nun um 1 inkrementierten
					         * Prim�rschl�ssel.
					         */
					    	 Integer sbt_ID = new Integer((rs.getInt("maxid")+1));
					    	 
						     //Bauteil hinzuf�gen
						     stmt.executeUpdate("INSERT INTO StuecklistenBauteile VALUES('"+sbt_ID.toString()+"','"+stuecklistenID+"','"
						    		 +anzahl+"','"+elementID+"');");
					     }  

				}
				
				//Baugruppen hinzuf�gen
				for(int i=0;i<stueckliste.getBaugruppenPaare().size();i++){
					//Da ich ein int nicht einfach durch casting in einen String wandeln kann, muss dies �ber eine Instanz der Klasse Integer geschehen
					Integer stuecklistenID = new Integer(stueckliste.getId());
					Integer anzahl = new Integer(stueckliste.getBaugruppenPaare().get(i).getAnzahl());
					Integer elementID = new Integer(stueckliste.getBaugruppenPaare().get(i).getElement().getId());
					//MaxID von StuecklistenBauteile abfragen
					rs = stmt.executeQuery("SELECT MAX(sbg_ID) AS maxid "
					          + "FROM StuecklistenBaugruppe;");

					     // Wenn wir etwas zur�ckerhalten, kann dies nur einzeilig sein
					     if (rs.next()) {
					        /*
					         * a erh�lt den bisher maximalen, nun um 1 inkrementierten
					         * Prim�rschl�ssel.
					         */
					    	Integer sbg_ID = new Integer((rs.getInt("maxid")+1));
					    	
							//Baugruppe hinzuf�gen
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
		 * R�ckgabe, des evtl. korrigierten Stueckliste.
		 * 
		 * HINWEIS: Da in Java nur Referenzen auf Objekte und keine physischen
		 * Objekte �bergeben werden, w�re die Anpassung des Stueckliste-Objekts auch
		 * ohne diese explizite R�ckgabe au�erhalb dieser Methode sichtbar. Die
		 * explizite R�ckgabe von a ist eher ein Stilmittel, um zu
		 * signalisieren, dass sich das Objekt evtl. im Laufe der Methode
		 * ver�ndert hat.
		 */
		return stueckliste;
	}
	
	/**
	 * Wiederholtes Schreiben eines Objekts in die Datenbank.
	 * 
	 * @param a
	 *            das Objekt, das in die DB geschrieben werden soll
	 * @return das als Parameter �bergebene Objekt
	 */
	public Stueckliste update(Stueckliste stueckliste){
		Connection con = DBConnection.connection();
		Stueckliste dBStueckliste = new Stueckliste();
		
		try{
			Statement stmt = con.createStatement();
			
			 // Java Util Date wird umgewandelt in SQL Date um das �nderungsdatum in
		  	  // die Datenbank zu speichern 
		   	  Date utilDate = stueckliste.getEditDate();
		   	  java.sql.Timestamp sqlDate = new java.sql.Timestamp(utilDate.getTime());  
		   	  DateFormat df = new SimpleDateFormat("dd/MM/YYYY HH:mm:ss");
		   	  df.format(sqlDate);
		   	  
				//Das edit-Datum wird erst gesetzt, wenn der UPDATE-Vorgang beginnt
				stueckliste.setEditDate(sqlDate);
			
			//Zuerst die Daten der Stueckliste �ndern
			stmt.executeUpdate("UPDATE Stueckliste SET name='"+stueckliste.getName()+"' ,datum='"
					+stueckliste.getEditDate()+"',bearbeitet_Von='"
					+stueckliste.getEditUser().getId()+"' WHERE sl_ID='"+stueckliste.getId()+"';");
			//Stueckliste aus der DB abfragen
			dBStueckliste=this.findById(stueckliste.getId());
			//Zuerst schauen, ob Bauteile in der DB gel�scht werden m�ssen
			for(int i=0; i<dBStueckliste.getBauteilPaare().size();i++){
				Boolean exists=false;
				//F�r jedes Paar in der DB schauen, ob es in der neuen Stueckliste existiert
				for(int j=0;j<stueckliste.getBauteilPaare().size();j++){
					if(stueckliste.getBauteilPaare().get(j).getElement().equals(dBStueckliste.getBauteilPaare().get(i).getElement())){
						exists=true;
					}
				}
				//Wenn das Bauteil in der DB existiert, aber nicht in der neuen Stueckliste, dann aus der DB l�schen
				if(!exists){
					stmt.executeUpdate("DELETE FROM StuecklistenBauteile WHERE stueckliste='"+stueckliste.getId()+"' AND bauteil='"
							+dBStueckliste.getBauteilPaare().get(i).getElement().getId()+"';");
				}
			}
			//Dann schauen, ob Bauteile der Datenbank hinzugef�gt werden m�ssen
			for(int i=0; i<stueckliste.getBauteilPaare().size();i++){
				Boolean exists=false;
				//F�r jedes Paar in der neuen Stueckliste schauen, ob es in der DB existiert
				for(int j=0; j<dBStueckliste.getBauteilPaare().size();j++){
					if(dBStueckliste.getBauteilPaare().get(j).getElement().equals(stueckliste.getBauteilPaare().get(i).getElement())){
						//�berpr�fen, ob die Anzahl �bereinstimmt
						if(dBStueckliste.getBauteilPaare().get(j).getAnzahl()!=stueckliste.getBauteilPaare().get(i).getAnzahl()){
							//Wenn nicht, dann Anzahl �ndern
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

					     // Wenn wir etwas zur�ckerhalten, kann dies nur einzeilig sein
					     if (rs.next()) {
					        /*
					         * a erh�lt den bisher maximalen, nun um 1 inkrementierten
					         * Prim�rschl�ssel.
					         */
					    	Integer sbt_ID = new Integer((rs.getInt("maxid")+1));
					    	//Bauteil hinzuf�gen
							stmt.executeUpdate("INSERT INTO StuecklistenBauteile VALUES('"+sbt_ID+"','"+stueckliste.getId()+"','"
									+stueckliste.getBauteilPaare().get(i).getAnzahl()+"','"+stueckliste.getBauteilPaare().get(i).getElement().getId()+"');");
					      }  
					
				}
			}
			
			//Zuerst schauen, ob Baugruppen in der DB gel�scht werden m�ssen
			for(int i=0; i<dBStueckliste.getBaugruppenPaare().size();i++){
				Boolean exists=false;
				//F�r jedes Paar in der DB schauen, ob es in der neuen Stueckliste existiert
				for(int j=0;j<stueckliste.getBaugruppenPaare().size();j++){
					if(stueckliste.getBaugruppenPaare().get(j).getElement().equals(dBStueckliste.getBaugruppenPaare().get(i).getElement())){
						exists=true;
					}
				}
				//Wenn die Baugruppe in der DB existiert, aber nicht in der neuen Stueckliste, dann aus der DB l�schen
				if(!exists){
					stmt.executeUpdate("DELETE FROM StuecklistenBaugruppe WHERE stueckliste='"+stueckliste.getId()+"' AND baugruppe='"
							+dBStueckliste.getBaugruppenPaare().get(i).getElement().getId()+"';");
				}
			}
			//Dann schauen, ob Baugruppen in der Datenbank hinzugef�gt werden m�ssen
			for(int i=0; i<stueckliste.getBaugruppenPaare().size();i++){
				Boolean exists=false;
				//F�r jedes Paar in der neuen Stueckliste schauen, ob es in der DB existiert
				for(int j=0; j<dBStueckliste.getBaugruppenPaare().size();j++){
					if(dBStueckliste.getBaugruppenPaare().get(j).getElement().equals(stueckliste.getBaugruppenPaare().get(i).getElement())){
						//�berpr�fen, ob die Anzahl �bereinstimmt
						if(dBStueckliste.getBaugruppenPaare().get(j).getAnzahl()!=stueckliste.getBaugruppenPaare().get(i).getAnzahl()){
							//Wenn nicht, dann Anzahl �ndern
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
					ResultSet rs = stmt.executeQuery("SELECT MAX(sbg_ID) AS maxid "
					          + "FROM StuecklistenBaugruppe;");

					     // Wenn wir etwas zur�ckerhalten, kann dies nur einzeilig sein
					     if (rs.next()) {
					        /*
					         * a erh�lt den bisher maximalen, nun um 1 inkrementierten
					         * Prim�rschl�ssel.
					         */
					    	Integer sbg_ID = new Integer((rs.getInt("maxid")+1));
					    	//Baugruppe hinzuf�gen
							stmt.executeUpdate("INSERT INTO StuecklistenBaugruppe VALUES('"+sbg_ID+"','"+stueckliste.getId()+"','"
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
	 * L�schen der Daten eines <code>Stueckliste</code>-Objekts aus der Datenbank.
	 * 
	 * @param a
	 *            das aus der DB zu l�schende "Objekt"
	 */
	public void delete(Stueckliste stueckliste) {
		Connection con = DBConnection.connection();

		try {
			Statement stmt = con.createStatement();

			//Zuerst die Elementzuordnungen der Stueckliste l�schen
			//Bauteile
			stmt.executeUpdate("DELETE FROM StuecklistenBauteile WHERE stueckliste='"+stueckliste.getId()+"';");
			//Baugruppen
			stmt.executeUpdate("DELETE FROM StuecklistenBaugruppe WHERE stueckliste='"+stueckliste.getId()+"';");
			//Dann die Stueckliste l�schen
			stmt.executeUpdate("DELETE FROM Stueckliste WHERE sl_ID='"+stueckliste.getId()+"';");
			
			

		} catch (SQLException e2) {
			e2.printStackTrace();
		}
	}
	
	/**
	 * Auslesen aller Stuecklisten.
	 * 
	 * @return Ein Vektor mit Stueckliste-Objekten, die s�mtliche Stuecklisten
	 *         repr�sentieren. Bei evtl. Exceptions wird ein partiell gef�llter
	 *         oder ggf. auch leerer Vetor zur�ckgeliefert.
	 */
	public Vector<Stueckliste> findAll() {
		Connection con = DBConnection.connection();
		// Ergebnisvektor vorbereiten
		Vector<Stueckliste> result = new Vector<Stueckliste>();

		try {
			Statement stmt = con.createStatement();

			ResultSet rs = stmt
					.executeQuery("SELECT * FROM Stueckliste JOIN User ON Stueckliste.bearbeitet_Von=User.userID ORDER BY sl_ID");

			// F�r jeden Eintrag im Suchergebnis wird nun ein Customer-Objekt
			// erstellt.
			while (rs.next()) {
				Stueckliste stueckliste = new Stueckliste();
				stueckliste.setId(rs.getInt("sl_ID"));
				stueckliste.setName(rs.getString("name"));
				
				// Java Util Date wird umgewandelt in SQL Date um das �nderungsdatum in
		    	  // die Datenbank zu speichern 
		     	  java.sql.Timestamp sqlDate = rs.getTimestamp("datum");
		     	  stueckliste.setEditDate(sqlDate);
		     	  
					// Java Util Date wird umgewandelt in SQL Date um das �nderungsdatum in
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
						//User Objekt in bauteil einf�gen
						bauteil.setEditUser(user);
						//Timestamp Objekt aus Datumsstring erzeugen, um es in bauteil einzuf�gen
						Timestamp timestamp = Timestamp.valueOf(rs2.getString("datum"));
						bauteil.setEditDate(timestamp);
						//StuecklistenPaar erstellen und bauteil hinzuf�gen
						ElementPaar bauteilPaar = new ElementPaar();
						bauteilPaar.setAnzahl(rs2.getInt("anzahl"));
						bauteilPaar.setElement(bauteil);
						//stuecklistenPaar der Stueckliste hinzuf�gen
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
						//User Objekt in baugruppe einf�gen
						baugruppe.setEditUser(user);
						//Timestamp Objekt aus Datumsstring erzeugen, um es in bauteil einzuf�gen
						Timestamp timestamp = Timestamp.valueOf(rs3.getString("datum"));
						baugruppe.setEditDate(timestamp);
						//Stueckliste der Baugruppe abfragen und einf�gen
						baugruppe.setStueckliste(findById(rs3.getInt("stueckliste")));
						//StuecklistenPaar erstellen und bauteil hinzuf�gen
						ElementPaar baugruppenPaar = new ElementPaar();
						baugruppenPaar.setAnzahl(rs3.getInt("anzahl"));
						baugruppenPaar.setElement(baugruppe);
						//Baugruppe der stueckliste hinzuf�gen
						stueckliste.getBaugruppenPaare().add(baugruppenPaar);
					}

				// Hinzuf�gen des neuen Objekts zum Ergebnisvektor
				result.addElement(stueckliste);

			}
		} 
		catch (SQLException e) {
			e.printStackTrace();
		}

		// Ergebnisvektor zur�ckgeben
		return result;
	}
	
	/**
	 * Suchen eines Stuecklistes mit vorgegebener Id. Da diese eindeutig
	 * ist, wird genau ein Objekt zur�ckgegeben.
	 * 
	 * @param id
	 *            Prim�rschl�sselattribut (->DB)
	 * @return Stueckliste-Objekt, das dem �bergebenen Schl�ssel entspricht, null bei
	 *         nicht vorhandenem DB-Tupel.
	 */
	public Stueckliste findById(int id) {
		// DB-Verbindung holen
		Connection con = DBConnection.connection();

		try {
			// Leeres SQL-Statement (JDBC) anlegen
			Statement stmt = con.createStatement();
			// Statement ausf�llen und als Query an die DB schicken
			ResultSet rs = stmt
					.executeQuery("SELECT * FROM Stueckliste JOIN User ON Stueckliste.bearbeitet_Von=User.userID WHERE sl_ID='"
							+ id + "';");
			
			// "SELECT * FROM `stuecklisten` ORDER BY `name`"

			/*
			 * Da id Prim�rschl�ssel ist, kann max. nur ein Tupel zur�ckgegeben
			 * werden. Pr�fe, ob ein Ergebnis vorliegt.
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
					//User Objekt in bauteil einf�gen
					bauteil.setEditUser(user);
					//Timestamp Objekt aus Datumsstring erzeugen, um es in bauteil einzuf�gen
					Timestamp timestamp = Timestamp.valueOf(rs.getString("datum"));
					bauteil.setEditDate(timestamp);
					//StuecklistenPaar erstellen und bauteil hinzuf�gen
					ElementPaar bauteilPaar = new ElementPaar();
					bauteilPaar.setAnzahl(rs.getInt("anzahl"));
					bauteilPaar.setElement(bauteil);
					//stuecklistenPaar der Stueckliste hinzuf�gen
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
					//User Objekt in baugruppe einf�gen
					baugruppe.setEditUser(user);
					//Timestamp Objekt aus Datumsstring erzeugen, um es in bauteil einzuf�gen
					Timestamp timestamp = Timestamp.valueOf(rs.getString("datum"));
					baugruppe.setEditDate(timestamp);
					//Stueckliste der Baugruppe abfragen und einf�gen
					baugruppe.setStueckliste(findById(rs.getInt("stueckliste")));
					//StuecklistenPaar erstellen und bauteil hinzuf�gen
					ElementPaar baugruppenPaar = new ElementPaar();
					baugruppenPaar.setAnzahl(rs.getInt("anzahl"));
					baugruppenPaar.setElement(baugruppe);
					//Baugruppe der stueckliste hinzuf�gen
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
