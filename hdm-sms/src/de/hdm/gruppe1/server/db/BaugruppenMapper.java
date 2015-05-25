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
	
	public Baugruppe insert(Baugruppe baugruppe) throws SQLException{
		Connection con = DBConnection.connection();
		Statement stmt = con.createStatement();
		Integer aendererID = new Integer(baugruppe.getAenderer().getUserId());
		Integer stuecklistenID = new Integer(baugruppe.getStueckliste().getId());
		try{
			ResultSet rs = stmt.executeQuery("INSERT INTO 'Baugruppe'('name',"
					+ "'stueckliste','bearbeitet_Von','datum') VALUES('"+baugruppe.getName()+"','"
					+stuecklistenID.toString()+"','"+aendererID.toString()
					+"','"+baugruppe.getAenderungsDatum().toString().substring(0,19)+"');");
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
		Integer aendererID = new Integer(baugruppe.getAenderer().getUserId());
		Integer stuecklistenID = new Integer(baugruppe.getStueckliste().getId());
		Connection con = DBConnection.connection();
		Statement stmt = con.createStatement();
		try{
			stmt.executeUpdate("UPDATE 'Baugruppe' SET 'name'='"
		+baugruppe.getName()+"','stueckliste'='"+baugruppe.getStueckliste().getId().toString()
		+"','bearbeitet_Von'='"
		+baugruppe.getAenderer().getUserId().toString()+"','datum'='"
		+baugruppe.getAenderungsDatum().toString().substring(0,19)
		+"' WHERE 'bg_ID'='"+baugruppe.getId().toString()+"';");
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
		    	StuecklistenMapper slm = StuecklistenMapper.stuecklistenMapper();
		    	baugruppe.setStueckliste(slm.findByID(rs.getInt("stueckliste")));
		    	
		    	User user = new User();
		    	user.setId(rs.getInt("userID"));
		    	user.setEmail(rs.getString("eMail"));
		    	user.setGoogleId(rs.getString("googleID"));
		    	baugruppe.setAenderer(user);
		    	//Timestamp Objekt aus Datumsstring erzeugen, um es in baugruppe einzuf�gen
				Timestamp timestamp = Timestamp.valueOf(rs.getString("datum"));
				baugruppe.setAenderungsDatum(timestamp);
		    }
		}
		catch(SQLException e){
			e.printStackTrace();
		}
		return baugruppe;
	}
	
	public Vector<Baugruppe> findByName(String name){
		Vector<Baugruppe> vBaugruppe = new Vector<Baugruppe>();
		Connection con = DBConnection.connection();
		try{
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM Baugruppe JOIN User ON 'Baugruppe.bearbeitet_Von'='User.userID' WHERE 'Baugruppe.name' LIKE '%"
					+name+"%';");
			//Da es viele Baugruppen geben kann, die diesen Namen haben m�ssen wir eine Schleife benutzen
			while(rs.next()){
				//Neue Baugruppe erzeugen
				Baugruppe baugruppe = new Baugruppe();
		    	baugruppe.setId(rs.getInt("bg_ID"));
		    	baugruppe.setName(rs.getString("name"));
		    	//Da wir die Stueckliste der Baugruppe aufl�sen m�ssen brauchen wir einen StuecklistenMapper
		    	StuecklistenMapper slm = StuecklistenMapper.stuecklistenMapper();
		    	baugruppe.setStueckliste(slm.findByID(rs.getInt("stueckliste")));
		    	//Neuen User erzeugen
		    	User user = new User();
		    	user.setId(rs.getInt("userID"));
		    	user.setEmail(rs.getString("eMail"));
		    	user.setGoogleId(rs.getString("googleID"));
		    	baugruppe.setAenderer(user);
		    	//Timestamp Objekt aus Datumsstring erzeugen, um es in baugruppe einzuf�gen
				Timestamp timestamp = Timestamp.valueOf(rs.getString("datum"));
				baugruppe.setAenderungsDatum(timestamp);
				//Baugruppe der ArrayList hinzuf�gen
				vBaugruppe.addElement(baugruppe);
			}
		}
		catch(SQLException e){
			e.printStackTrace();
		}
		return vBaugruppe;
	}
	public Vector<Baugruppe> getAll(){
		Vector<Baugruppe> vBaugruppe = new Vector<Baugruppe>();
		Connection con = DBConnection.connection();
		try{
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM Baugruppe JOIN User ON 'Baugruppe.bearbeitet_Von'='User.userID'%';");
			//Da es viele Baugruppen geben kann m�ssen wir eine Schleife benutzen
			while(rs.next()){
				//Neue Baugruppe erzeugen
				Baugruppe baugruppe = new Baugruppe();
		    	baugruppe.setId(rs.getInt("bg_ID"));
		    	baugruppe.setName(rs.getString("name"));
		    	//Da wir die Stueckliste der Baugruppe aufl�sen m�ssen brauchen wir einen StuecklistenMapper
		    	StuecklistenMapper slm = StuecklistenMapper.stuecklistenMapper();
		    	baugruppe.setStueckliste(slm.findByID(rs.getInt("stueckliste")));
		    	//Neuen User erzeugen
		    	User user = new User();
		    	user.setID(rs.getInt("userID"));
		    	user.setEmail(rs.getString("eMail"));
		    	user.setGoogleId(rs.getString("googleID"));
		    	baugruppe.setAenderer(user);
		    	//Timestamp Objekt aus Datumsstring erzeugen, um es in baugruppe einzuf�gen
				Timestamp timestamp = Timestamp.valueOf(rs.getString("datum"));
				baugruppe.setAenderungsDatum(timestamp);
				//Baugruppe der ArrayList hinzuf�gen
				vBaugruppe.addElement(baugruppe);
			}
		}
		catch(SQLException e){
			e.printStackTrace();
		}
		return vBaugruppe;
	}
}