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
	
	private static BaugruppenMapper baugruppenMapper = null;
	
	protected BaugruppenMapper(){
		
	}
	
	public static BaugruppenMapper baugruppenMapper(){
		if(baugruppenMapper==null){
			baugruppenMapper= new BaugruppenMapper();
		}
		return baugruppenMapper;
	}
	

	
	public Baugruppe insert(Baugruppe baugruppe) {
		Connection con = DBConnection.connection();
	

		try{
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("INSERT INTO 'Baugruppe'('name',"
					+ "'stueckliste','bearbeitet_Von','datum') VALUES('"+baugruppe.getName()+"','"
					+ baugruppe.getBaugruppe());
			if(rs.next()){
				baugruppe.setId(rs.getInt("bg_ID"));
			}
		}
		catch(SQLException e){
			e.printStackTrace();
		}
		return baugruppe;		
		
	}
	
	public Baugruppe update(Baugruppe baugruppe){
		Connection con = DBConnection.connection();
		
		
		try{
			Statement stmt = con.createStatement();
			stmt.executeUpdate("UPDATE 'Baugruppe' SET 'name'='"
					+baugruppe.getName()+"','baugruppe'='"
					+"','bearbeitet_Von'='"
					+"','datum'='"
					+"' WHERE 'bg_ID'='"+baugruppe.getId()+"';");
			
			
		}
		catch(SQLException e){
			e.printStackTrace();
		}
		return baugruppe;
		
	}
	
	public boolean delete(Baugruppe baugruppe){
		Connection con = DBConnection.connection();

	    try {
	      Statement stmt = con.createStatement();
	      //Da ich ein int nicht einfach durch casting in einen String wandeln kann, muss dies �ber eine Instanz der Klasse Integer geschehen
	      Integer baugruppeID = new Integer(baugruppe.getId());
	      
	      
	      if(stmt.executeUpdate("DELETE FROM `Baugruppe` WHERE `bg_ID`="+baugruppeID.toString()+"';")==0){
	    	  return false;
	      }
	      else{
	    	  return true;
	      }

	    }
	    catch (SQLException e2) {
	      e2.printStackTrace();
	      return false;
	    }
		
	}
	
	public Baugruppe findById (int id){
		Connection con = DBConnection.connection();
	Baugruppe baugruppe = null;
		try{
			Statement stmt = con.createStatement();
			//Da ich ein int nicht einfach durch casting in einen String wandeln kann, muss dies �ber eine Instanz der Klasse Integer geschehen
		    Integer baugruppeID = new Integer(id);
	    
	    ResultSet rs = stmt.executeQuery("SELECT * FROM 'Baugruppe' JOIN 'User' ON 'Baugruppe.bearbeitet_Von'='User.userID' WHERE 'bg_ID'='"
	    		+baugruppeID.toString()+"';");
		    //Da es nur eine Baugruppe mit dieser ID geben kann ist davon auszugehen, dass das ResultSet nur eine Zeile enth�lt
		    if(rs.next()){
		    	baugruppe = new Baugruppe();
		    	baugruppe.setId(rs.getInt("bg_ID"));
		    	baugruppe.setName(rs.getString("name"));
		    	//Da wir die Stueckliste der Baugruppe aufl�sen m�ssen brauchen wir einen StuecklistenMapper
	    
		    	
		  
			
	    }
		}
		catch(SQLException e){
			e.printStackTrace();
		}
		return baugruppe;}
	
	
	public Vector<Baugruppe> findByName(String name){
		Vector<Baugruppe> vBaugruppe = new Vector<Baugruppe>();
		Connection con = DBConnection.connection();
		try{
		Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM Baugruppe JOIN User ON "
					+ "'Baugruppe.bearbeitet_Von'='User.userID' WHERE 'Baugruppe.name' LIKE '%"
				+name+"%';");
			//Da es viele Baugruppen geben kann, die diesen Namen haben m�ssen wir eine Schleife benutzen
			while(rs.next()){
				//Neue Baugruppe erzeugen
				Baugruppe baugruppe = new Baugruppe();
		    	baugruppe.setId(rs.getInt("bg_ID"));
		    	baugruppe.setName(rs.getString("name"));
		    	//Da wir die Stueckliste der Baugruppe aufl�sen m�ssen brauchen wir einen StuecklistenMapper
		
				//Baugruppe der ArrayList hinzuf�gen
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