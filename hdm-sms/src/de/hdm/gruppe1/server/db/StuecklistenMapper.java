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
				for(int i=0;i<stueckliste.size();i++){
						addElementZuStueckliste(stueckliste,stueckliste.get(i));
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
					updateElementVonStueckliste(stueckliste,stueckliste.get(i));
				}
				//Wenn das Element noch kein Teil ist...
				else{
					//... es hinzufügen
					addElementZuStueckliste(stueckliste,stueckliste.get(i));
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
				}
			}
			return stueckliste;
		}
		catch(SQLException e){
			e.printStackTrace();
		}
	}
	
	public ArrayList<Stueckliste> findByName(String name){
		
	}
	
	public ArrayList<Stueckliste> getAll(){
		
	}
	
	protected void addElementZuStueckliste(Stueckliste stueckliste,Element element){
		
	}
	
	protected void updateElementVonSteuckliste (Stueckliste stueckliste, Element element){
		
	}
}
