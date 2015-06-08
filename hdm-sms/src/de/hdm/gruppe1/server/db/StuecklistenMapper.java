/**
 * 
 */
package de.hdm.gruppe1.server.db;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Vector;

import com.google.storage.onestore.v3.OnestoreEntity.User;

import de.hdm.gruppe1.shared.bo.*;

/**
 * @author Andreas Herrmann
 *
 */
public class StuecklistenMapper {
	private static StuecklistenMapper stuecklistenMapper = null;
	
	protected StuecklistenMapper(){
		
	}
	
	public static StuecklistenMapper stuecklistenMapper(){
		if(stuecklistenMapper==null){
			stuecklistenMapper = new StuecklistenMapper();
		}
		return stuecklistenMapper;
	}
	
	public Stueckliste insert(Stueckliste stueckliste){
		Connection con = DBConnection.connection();
		Statement stmt = con.createStatement();
		//Da ich ein int nicht einfach durch casting in einen String wandeln kann, muss dies über eine Instanz der Klasse Integer geschehen
		Integer erstellerID = new Integer(stueckliste.getAenderer().getId());
		try{
			ResultSet rs = stmt.executeQuery("INSERT INTO 'Steuckliste'('name','ersteller','datum') VALUES('"+stueckliste.getName()+"','"
					+erstellerID.toString()+"','"+stueckliste.getAenderungsDatum().toString().substring(0,19)+"');");
			if(rs.next()){
				stueckliste.setId(rs.getInt("sl_ID"));
				//Wenn die Stueckliste eine ID bekommen hat, dann können die Elemente hinzugefügt werden
				//Bauteile hinzufügen
				for(int i=0;i<stueckliste.getBauteilPaare.size;i++){
					//Da ich ein int nicht einfach durch casting in einen String wandeln kann, muss dies über eine Instanz der Klasse Integer geschehen
					Integer stuecklistenID = new Integer(stueckliste.getId());
					Integer anzahl = new Integer(stueckliste.getBauteilPaare.get(i).getAnzahl());
					Integer elementID = new Integer(stueckliste.getBauteilPaare.get(i).getElement().getId());
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
					      }  
					//Bauteil hinzufügen
					stmt.executeUpdate("INSERT INTO 'StuecklistenBauteile' VALUES('"+sbt_ID.toString()+"','"+stuecklistenID.toString()+"','"
							+anzahl.toString()+"','"+elementID.toString()+"');");
				}
				
				//Baugruppen hinzufügen
				for(int i=0;i<stueckliste.getBaugruppenPaare.size;i++){
					//Da ich ein int nicht einfach durch casting in einen String wandeln kann, muss dies über eine Instanz der Klasse Integer geschehen
					Integer stuecklistenID = new Integer(stueckliste.getId());
					Integer anzahl = new Integer(stueckliste.getBaugruppenPaare.get(i).getAnzahl());
					Integer elementID = new Integer(stueckliste.getBaugruppenPaare.get(i).getElement().getId());
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
					      }  
					//Baugruppe hinzufügen
					stmt.executeUpdate("INSERT INTO 'StuecklistenBugruppe' VALUES('"+sbg_ID.toString()+"','"+stuecklistenID.toString()+"','"
							+anzahl.toString()+"','"+elementID.toString()+"');");
				}
					      
			}
		}
		catch(SQLException e){
			e.printStackTrace();
		}
		return stueckliste;
	}
	
	public Stueckliste update(Stueckliste stueckliste){
		Connection con = DBConnection.connection();
		Statement stmt = con.createStatement();
		//Da ich ein int nicht einfach durch casting in einen String wandeln kann, muss dies über eine Instanz der Klasse Integer geschehen
		Integer stuecklistenID = new Integer(stueckliste.getId());
		Integer erstellerID = new Integer(stueckliste.getAenderer());
		try{
			//Zuerst die Daten der Stueckliste ändern
			stmt.executeUpdate("UPDATE 'Stueckliste' SET 'name'='"+stueckliste.getName()+"','ersteller'='"+erstellerID.toString()+"','datum'='"
					+stueckliste.getAenderungsDatum().toString().substring(0,19)+"' WHERE 'sl_ID'="+stuecklistenID.toString()+"';");
			//Dann ggf. neue Elemente hinzufügen
			for(int i=0;i<stueckliste.size();i++){
				Integer elementID = new Integer(stueckliste.get(i).getElement().getId());
				//Herausfinden, ob das aktuelle Element schon Teil der Stueckliste ist
				ResultSet rs = stmt.executeQuery("SELECT 'stueckliste','anzahl','bauteil' AS 'elementID' FROM 'StuecklistenBauteile' WHERE 'stueckliste'='"
						+stuecklistenID.toString()+" AND 'elementID='"+elementID.toString()+"' UNION ALL SELECT 'stueckliste','anzahl','baugruppe' AS "
						+"'elementID' FROM 'StuecklistenBaugruppe' WHERE stueckliste'='"+stuecklistenID.toString()+"' AND elementID='"+elementID.toString()+";");
				//Wenn es schon ein Teil ist, dann herausfinden ob die Anzahl nicht übereinstimmt
				if(rs.next() && stueckliste.get(i).getAnzahl!=rs.getInt("anzahl")){
					//Wenn die Anzahl nicht übereinstimmt, dann ein Update vornehmen
					//Da ich ein int nicht einfach durch casting in einen String wandeln kann, muss dies über eine Instanz der Klasse Integer geschehen
					Integer anzahl = new Integer(stueckliste.get(i).getAnzahl());
					
					//Herausfinden, ob das Element im StuecklistenPaar von der Klasse Bauteil ist
					if(stueckliste.get(i).getElement().getClass()==Bauteil.class){
						//Anzahl des Bauteils korrigieren
						stmt.executeUpdate("UPDATE 'StuecklistenBauteile' SET 'anzahl'='"+anzahl.toString()+"' WHERE 'sbt_ID'='"+elementID.toString()+"';");
					}
					//Oder, ob das Element im StuecklistenPaar von der Klasse Baugruppe ist
					else if(stueckliste.get(i).getElement().getClass()==Baugruppe.class){
						//Anzahl des Bauteils korrigieren
						stmt.executeUpdate("UPDATE 'StuecklistenBaugruppe' SET 'anzahl'='"+anzahl.toString()+"' WHERE 'sbg_ID'='"+elementID.toString()+"';");
					}
				}
				//Wenn das Element noch kein Teil ist...
				else{
					//... es hinzufügen
					//Da ich ein int nicht einfach durch casting in einen String wandeln kann, muss dies über eine Instanz der Klasse Integer geschehen
					Integer anzahl = new Integer(stueckliste.get(i).getAnzahl());
					
					//Herausfinden, ob das Element im StuecklistenPaar von der Klasse Bauteil ist
					if(stueckliste.get(i).getElement().getClass()==Bauteil.class){
						//Bauteil hinzufügen
						stmt.executeUpdate("INSERT INTO 'StuecklistenBauteile' ('stueckliste','anzahl','bauteil') VALUES ('"+stuecklistenID.toString()+"','"
									+anzahl.toString()+"','"+elementID.toString()+"');");
					}
					//Oder, ob das Element im StuecklistenPaar von der Klasse Baugruppe ist
					else if(stueckliste.get(i).getElement().getClass()==Baugruppe.class){
						//Baugruppe hinzufügen
						stmt.executeUpdate("INSERT INTO 'StuecklistenBaugruppe' ('stueckliste','anzahl','baugruppe') VALUES ('"+stuecklistenID.toString()+"','"
									+anzahl.toString()+"','"+elementID.toString()+"');");
					}
				}
			}
		}
		catch(SQLException e){
			e.printStackTrace();
		}
	}
	
	public boolean delete(Stueckliste stueckliste){
		Connection con = DBConnection.connection();
		Statement stmt = con.createStatement();
		//Da ich ein int nicht einfach durch casting in einen String wandeln kann, muss dies über eine Instanz der Klasse Integer geschehen
		Integer stuecklistenID = new Integer(stueckliste.getId());
		try{
			//Zuerst die Elementzuordnungen der Stueckliste löschen
			//Bauteile
			stmt.executeUpdate("DELETE FROM 'StuecklistenBauteile' WHERE 'stueckliste'='"+stuecklistenID.toString()+"';");
			//Baugruppen
			stmt.executeUpdate("DELETE FROM 'StuecklistenBaugruppe' WHERE 'stueckliste'='"+stuecklistenID.toString()+"';");
			//Dann die Stueckliste löschen
			stmt.executeUpdate("DELETE FROM 'Stueckliste' WHERE 'sl_ID'='"+stuecklistenID.toString()+"';");
			return true;
		}
		catch(SQLException e){
			e.printStackTrace();
			return false;
		}
	}
	
	public Stueckliste findByID(int id){
		Connection con = DBConnection.connection();
		Statement stmt = con.createStatement();
		Stueckliste stueckliste = null;
		//Da ich ein int nicht einfach durch casting in einen String wandeln kann, muss dies über eine Instanz der Klasse Integer geschehen
		Integer stuecklistenID = new Integer(id);
		try{
			//Zuerst die Daten der Stueckliste abfragen
			ResultSet rs = stmt.executeQuery("SELECT * FROM 'Stueckliste JOIN 'User' ON 'Stueckliste.ersteller'='User.userID' WHERE 'sl_ID'='"
					+stuecklistenID.toString()+"';");
			//Es sollte nur eine Stueckliste mit dieser ID geben
			if(rs.next()){
				stueckliste = new Stueckliste();
				stueckliste.setId(rs.getInt("sl_ID"));
				stueckliste.setName(rs.getString("name"));
			
				User user = new User();
				user.setID(rs.getInt("userID"));
				user.setEmail(rs.getString("eMail"));
				user.setGoogleId(rs.getString("googleID"));
				stueckliste.setAenderer(user);
				//Timestamp Objekt aus Datumsstring erzeugen, um es in baugruppe einzufügen
				Timestamp timestamp = Timestamp.valueOf(rs.getString("datum"));
				stueckliste.setAenderungsDatum(timestamp);
				
				//Bauteile der Stueckliste abfragen
				rs = stmt.executeQuery("SELECT * FROM 'StuecklistenBauteile' JOIN (SELECT * FROM 'Bauteile' JOIN 'User' ON 'Bauteile.bearbeitet_Von'='User.userID')"
						+" ON 'StuecklistenBauteile.bauteil'='Bauteile.teilnummer' WHERE 'StuecklistenBauteile.stueckliste'='"+stuecklistenID.toString()+"';");
				while(rs.next()){
					//Letzter Aenderer anlegen
					user = new User();
					user.setID(rs.getInt("User.userID"));
					user.setEmail(rs.getString("User.eMail"));
					user.setGoogleId(rs.getString("User.googleID"));
					//Bauteil anlegen
					Bauteil bauteil = new Bauteil();
					bauteil.setId(rs.getInt("Bauteile.teilnummer"));
					bauteil.setName(rs.getString("Bauteile.name"));
					bauteil.setMaterialBeschreibung(rs.getString("Bauteile.material"));
					bauteil.setBauteilBeschreibung(rs.getString("Bauteile.beschreibung"));
					//User Objekt in bauteil einfügen
					bauteil.setAenderer(user);
					//Timestamp Objekt aus Datumsstring erzeugen, um es in bauteil einzufügen
					Timestamp timestamp = Timestamp.valueOf(rs.getString("Bauteile.datum"));
					bauteil.setAenderungsDatum(timestamp);
					//StuecklistenPaar erstellen und bauteil hinzufügen
					StuecklistenPaar stuecklistenPaar = new StuecklistenPaar();
					stuecklistenPaar.setAnzahl(rs.getInt("StuecklistenBauteile.anzahl"));
					stuecklistenPaar.setElement(bauteil);
					//stuecklistenPaar der Stueckliste hinzufügen
					stueckliste.add(stuecklistenPaar);
				}
				//Baugruppen der Stueckliste Abfragen
				rs = stmt.executeQuery("SELECT * FROM 'StuecklistenBaugruppe' JOIN (SELECT * FROM 'Baugruppe' JOIN 'User' ON 'Baugruppe.bearbeitet_Von'='User.userID')"
						+" ON 'StuecklistenBaugruppe.baugruppe'='Baugruppe.bg_ID' WHERE 'StuecklistenBaugruppe.stueckliste'='"+stuecklistenID.toString()+"';");
				while(rs.next()){
					//Letzter Aenderer anlegen
					user = new User();
					user.setID(rs.getInt("User.userID"));
					user.setEmail(rs.getString("User.eMail"));
					user.setGoogleId(rs.getString("User.googleID"));
					//Baugruppe anlegen
					Baugruppe baugruppe = new Baugruppe();
					baugruppe.setId(rs.getInt("Baugruppe.bg_ID"));
					baugruppe.setName(rs.getString("Baugruppe.name"));
					//User Objekt in baugruppe einfügen
					baugruppe.setAenderer(user);
					//Timestamp Objekt aus Datumsstring erzeugen, um es in bauteil einzufügen
					Timestamp timestamp = Timestamp.valueOf(rs.getString("Baugruppe.datum"));
					baugruppe.setAenderungsDatum(timestamp);
					//Stueckliste der Baugruppe abfragen und einfügen
					baugruppe.setStueckliste(findByID(rs.getInt("Baugruppe.stueckliste")));
					//Baugruppe der stueckliste hinzufügen
					stueckliste.add(baugruppe);
				}
			}
		}
		catch(SQLException e){
			e.printStackTrace();
		}
		return stueckliste;
	}
	
	public ArrayList<Stueckliste> findByName(String name){
		Connection con = DBConnection.connection();
		Statement stmt = con.createStatement();
		ArrayList<Stueckliste> alStueckliste = new ArrayList<Stueckliste>();
		try{
			//Zuerst die Daten der Stuecklisten abfragen
			ResultSet rs = stmt.executeQuery("SELECT * FROM 'Stueckliste JOIN 'User' ON 'Stueckliste.ersteller'='User.userID' WHERE 'name' LIKE '%"
					+name+"%';");
			while(rs.next()){
				//Stueckliste anlegen und Daten eintragen
				Stueckliste stueckliste = new Stueckliste();
				stueckliste = new Stueckliste();
				stueckliste.setId(rs.getInt("sl_ID"));
				stueckliste.setName(rs.getString("name"));
			
				User user = new User();
				user.setID(rs.getInt("userID"));
				user.setEmail(rs.getString("eMail"));
				user.setGoogleId(rs.getString("googleID"));
				stueckliste.setAenderer(user);
				//Timestamp Objekt aus Datumsstring erzeugen, um es in baugruppe einzufügen
				Timestamp timestamp = Timestamp.valueOf(rs.getString("datum"));
				stueckliste.setAenderungsDatum(timestamp);
				//Da ich ein int nicht einfach durch casting in einen String wandeln kann, muss dies über eine Instanz der Klasse Integer geschehen
				Integer stuecklistenID = new Integer(stueckliste.getId());
				
				//Bauteile der Stueckliste abfragen
				ResultSet rs2 = stmt.executeQuery("SELECT * FROM 'StuecklistenBauteile' JOIN (SELECT * FROM 'Bauteile' JOIN 'User' ON 'Bauteile.bearbeitet_Von'='User.userID')"
						+" ON 'StuecklistenBauteile.bauteil'='Bauteile.teilnummer' WHERE 'StuecklistenBauteile.stueckliste'='"+stuecklistenID.toString()+"';");
				while(rs.next()){
					//Letzter Aenderer anlegen
					user = new User();
					user.setID(rs2.getInt("User.userID"));
					user.setEmail(rs2.getString("User.eMail"));
					user.setGoogleId(rs2.getString("User.googleID"));
					//Bauteil anlegen
					Bauteil bauteil = new Bauteil();
					bauteil.setId(rs2.getInt("Bauteile.teilnummer"));
					bauteil.setName(rs2.getString("Bauteile.name"));
					bauteil.setMaterialBeschreibung(rs2.getString("Bauteile.material"));
					bauteil.setBauteilBeschreibung(rs2.getString("Bauteile.beschreibung"));
					//User Objekt in bauteil einfügen
					bauteil.setAenderer(user);
					//Timestamp Objekt aus Datumsstring erzeugen, um es in bauteil einzufügen
					Timestamp timestamp = Timestamp.valueOf(rs2.getString("Bauteile.datum"));
					bauteil.setAenderungsDatum(timestamp);
					//StuecklistenPaar erstellen und bauteil hinzufügen
					StuecklistenPaar stuecklistenPaar = new StuecklistenPaar();
					stuecklistenPaar.setAnzahl(rs2.getInt("StuecklistenBauteile.anzahl"));
					stuecklistenPaar.setElement(bauteil);
					//stuecklistenPaar der Stueckliste hinzufügen
					stueckliste.add(stuecklistenPaar);
				}
				//Baugruppen der Stueckliste Abfragen
				rs2 = stmt.executeQuery("SELECT * FROM 'StuecklistenBaugruppe' JOIN (SELECT * FROM 'Baugruppe' JOIN 'User' ON 'Baugruppe.bearbeitet_Von'='User.userID')"
						+" ON 'StuecklistenBaugruppe.baugruppe'='Baugruppe.bg_ID' WHERE 'StuecklistenBaugruppe.stueckliste'='"+stuecklistenID.toString()+"';");
				while(rs.next()){
					//Letzter Aenderer anlegen
					user = new User();
					user.setID(rs2.getInt("User.userID"));
					user.setEmail(rs2.getString("User.eMail"));
					user.setGoogleId(rs2.getString("User.googleID"));
					//Baugruppe anlegen
					Baugruppe baugruppe = new Baugruppe();
					baugruppe.setId(rs2.getInt("Baugruppe.bg_ID"));
					baugruppe.setName(rs2.getString("Baugruppe.name"));
					//User Objekt in baugruppe einfügen
					baugruppe.setAenderer(user);
					//Timestamp Objekt aus Datumsstring erzeugen, um es in bauteil einzufügen
					Timestamp timestamp = Timestamp.valueOf(rs2.getString("Baugruppe.datum"));
					baugruppe.setAenderungsDatum(timestamp);
					//Stueckliste der Baugruppe abfragen und einfügen
					baugruppe.setStueckliste(findByID(rs2.getInt("Baugruppe.stueckliste")));
					//Baugruppe der Stueckliste hinzufügen
					stueckliste.add(baugruppe);
				}
				//Stueckliste dem Ergebnis hinzufügen
				alStueckliste.add(stueckliste);
			}
		}
		catch(SQLException e){
			e.printStackTrace();
		}
		return alStueckliste;
	}
	
	public ArrayList<Stueckliste> getAll(){
		Connection con = DBConnection.connection();
		Statement stmt = con.createStatement();
		ArrayList<Stueckliste> alStueckliste = new ArrayList<Stueckliste>();
		try{
			//Zuerst die Daten der Stuecklisten abfragen
			ResultSet rs = stmt.executeQuery("SELECT * FROM 'Stueckliste JOIN 'User' ON 'Stueckliste.ersteller'='User.userID';");
			while(rs.next()){
				//Stueckliste anlegen und Daten eintragen
				Stueckliste stueckliste = new Stueckliste();
				stueckliste = new Stueckliste();
				stueckliste.setId(rs.getInt("sl_ID"));
				stueckliste.setName(rs.getString("name"));
			
				User user = new User();
				user.setID(rs.getInt("userID"));
				user.setEmail(rs.getString("eMail"));
				user.setGoogleId(rs.getString("googleID"));
				stueckliste.setAenderer(user);
				//Timestamp Objekt aus Datumsstring erzeugen, um es in baugruppe einzufügen
				Timestamp timestamp = Timestamp.valueOf(rs.getString("datum"));
				stueckliste.setAenderungsDatum(timestamp);
				//Da ich ein int nicht einfach durch casting in einen String wandeln kann, muss dies über eine Instanz der Klasse Integer geschehen
				Integer stuecklistenID = new Integer(stueckliste.getId());
				
				//Bauteile der Stueckliste abfragen
				ResultSet rs2 = stmt.executeQuery("SELECT * FROM 'StuecklistenBauteile' JOIN (SELECT * FROM 'Bauteile' JOIN 'User' ON 'Bauteile.bearbeitet_Von'='User.userID')"
						+" ON 'StuecklistenBauteile.bauteil'='Bauteile.teilnummer' WHERE 'StuecklistenBauteile.stueckliste'='"+stuecklistenID.toString()+"';");
				while(rs.next()){
					//Letzter Aenderer anlegen
					user = new User();
					user.setID(rs2.getInt("User.userID"));
					user.setEmail(rs2.getString("User.eMail"));
					user.setGoogleId(rs2.getString("User.googleID"));
					//Bauteil anlegen
					Bauteil bauteil = new Bauteil();
					bauteil.setId(rs2.getInt("Bauteile.teilnummer"));
					bauteil.setName(rs2.getString("Bauteile.name"));
					bauteil.setMaterialBeschreibung(rs2.getString("Bauteile.material"));
					bauteil.setBauteilBeschreibung(rs2.getString("Bauteile.beschreibung"));
					//User Objekt in bauteil einfügen
					bauteil.setAenderer(user);
					//Timestamp Objekt aus Datumsstring erzeugen, um es in bauteil einzufügen
					Timestamp timestamp = Timestamp.valueOf(rs2.getString("Bauteile.datum"));
					bauteil.setAenderungsDatum(timestamp);
					//StuecklistenPaar erstellen und bauteil hinzufügen
					StuecklistenPaar stuecklistenPaar = new StuecklistenPaar();
					stuecklistenPaar.setAnzahl(rs2.getInt("StuecklistenBauteile.anzahl"));
					stuecklistenPaar.setElement(bauteil);
					//stuecklistenPaar der Stueckliste hinzufügen
					stueckliste.add(stuecklistenPaar);
				}
				//Baugruppen der Stueckliste Abfragen
				rs2 = stmt.executeQuery("SELECT * FROM 'StuecklistenBaugruppe' JOIN (SELECT * FROM 'Baugruppe' JOIN 'User' ON 'Baugruppe.bearbeitet_Von'='User.userID')"
						+" ON 'StuecklistenBaugruppe.baugruppe'='Baugruppe.bg_ID' WHERE 'StuecklistenBaugruppe.stueckliste'='"+stuecklistenID.toString()+"';");
				while(rs.next()){
					//Letzter Aenderer anlegen
					user = new User();
					user.setID(rs2.getInt("User.userID"));
					user.setEmail(rs2.getString("User.eMail"));
					user.setGoogleId(rs2.getString("User.googleID"));
					//Baugruppe anlegen
					Baugruppe baugruppe = new Baugruppe();
					baugruppe.setId(rs2.getInt("Baugruppe.bg_ID"));
					baugruppe.setName(rs2.getString("Baugruppe.name"));
					//User Objekt in baugruppe einfügen
					baugruppe.setAenderer(user);
					//Timestamp Objekt aus Datumsstring erzeugen, um es in bauteil einzufügen
					Timestamp timestamp = Timestamp.valueOf(rs2.getString("Baugruppe.datum"));
					baugruppe.setAenderungsDatum(timestamp);
					//Stueckliste der Baugruppe abfragen und einfügen
					baugruppe.setStueckliste(findByID(rs2.getInt("Baugruppe.stueckliste")));
					//Baugruppe der Stueckliste hinzufügen
					stueckliste.add(baugruppe);
				}
				//Stueckliste dem Ergebnis hinzufügen
				alStueckliste.add(stueckliste);
			}
		}
		catch(SQLException e){
			e.printStackTrace();
		}
		return alStueckliste;
	}
}