/**
 * 
 */
package de.hdm.gruppe1.server.db;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Vector;

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
