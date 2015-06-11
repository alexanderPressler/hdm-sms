package de.hdm.gruppe1.server.db;

import java.sql.*;
import java.util.Vector;
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
	
	public Baugruppe insert(Baugruppe baugruppe){
		Connection con = DBConnection.connection();
		Integer aendererID = new Integer(baugruppe.getEditUser().getId());
		Integer stuecklistenID = new Integer(baugruppe.getStueckliste().getId());
		try{
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("INSERT INTO Baugruppe VALUES('"+baugruppe.getName()+"','"
					+stuecklistenID.toString()+"','"+aendererID.toString()+"','"+baugruppe.getEditDate().toString().substring(0,19)+"');");
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
			stmt.executeUpdate("UPDATE Baugruppe SET name='"+baugruppe.getName()+"',stueckliste='"+baugruppe.getStueckliste().getId()
					+"', bearbeitet_Von='"+baugruppe.getEditUser().getId()+"', datum ='"+baugruppe.getEditDate().toString().substring(0,19)
					+"' WHERE bg_ID ='"+baugruppe.getId()+"';");
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
	      //Da ich ein int nicht einfach durch casting in einen String wandeln kann, muss dies über eine Instanz der Klasse Integer geschehen
	      Integer baugruppeID = new Integer(baugruppe.getId());
	      
	      
	      if(stmt.executeUpdate("DELETE FROM Baugruppe WHERE bg_ID ="+baugruppeID.toString()+"';")==0){
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
	
	public Baugruppe findByID (int id){
		Connection con = DBConnection.connection();
		Baugruppe baugruppe = null;
		try{
			Statement stmt = con.createStatement();
			//Da ich ein int nicht einfach durch casting in einen String wandeln kann, muss dies über eine Instanz der Klasse Integer geschehen
		    Integer baugruppeID = new Integer(id);
		    
		    ResultSet rs = stmt.executeQuery("SELECT * FROM Baugruppe JOIN User ON Baugruppe.bearbeitet_Von=User.userID WHERE bg_ID ='"
		    		+baugruppeID.toString()+"';");
		    //Da es nur eine Baugruppe mit dieser ID geben kann ist davon auszugehen, dass das ResultSet nur eine Zeile enthält
		    if(rs.next()){
		    	baugruppe = new Baugruppe();
		    	baugruppe.setId(rs.getInt("bg_ID"));
		    	baugruppe.setName(rs.getString("name"));
		    	//Da wir die Stueckliste der Baugruppe auflösen müssen brauchen wir einen StuecklistenMapper
		    	StuecklisteMapper slm = StuecklisteMapper.stuecklisteMapper();
		    	baugruppe.setStueckliste(slm.findById(rs.getInt("stueckliste")));
		    	
		    	User user = new User();
		    	user.setId(rs.getInt("userID"));
		    	user.setName(rs.getString("eMail"));
		    	user.setGoogleID(rs.getString("googleID"));
		    	baugruppe.setEditUser(user);
		    	// Java Util Date wird umgewandelt in SQL Date um das Änderungsdatum in
		    	 // die Datenbank zu speichern 
		     	 java.sql.Timestamp sqlDate = rs.getTimestamp("datum");
		     	baugruppe.setEditDate(sqlDate);
		    }
		}
		catch(SQLException e){
			e.printStackTrace();
		}
		return baugruppe;
	}
	
//	public Vector<Baugruppe> findByName(String name){
//		Vector<Baugruppe> vBaugruppe = new Vector<Baugruppe>();
//		Connection con = DBConnection.connection();
//		try{
//			Statement stmt = con.createStatement();
//			ResultSet rs = stmt.executeQuery("SELECT * FROM Baugruppe JOIN User ON Baugruppe.bearbeitet_Von=User.userID WHERE Baugruppe.name LIKE '%"
//					+name+"%';");
//			//Da es viele Baugruppen geben kann, die diesen Namen haben müssen wir eine Schleife benutzen
//			while(rs.next()){
//				//Neue Baugruppe erzeugen
//				Baugruppe baugruppe = new Baugruppe();
//		    	baugruppe.setId(rs.getInt("bg_ID"));
//		    	baugruppe.setName(rs.getString("name"));
//		    	//Da wir die Stueckliste der Baugruppe auflösen müssen brauchen wir einen StuecklistenMapper
//		    	StuecklisteMapper slm = StuecklisteMapper.stuecklisteMapper();
//		    	baugruppe.setStueckliste(slm.findById(rs.getInt("stueckliste")));
//		    	//Neuen User erzeugen
//		    	User user = new User();
//		    	user.setId(rs.getInt("userID"));
//		    	user.setName(rs.getString("eMail"));
//		    	user.setGoogleID(rs.getString("googleID"));
//		    	baugruppe.setEditUser(user);
//		    	//Timestamp Objekt aus Datumsstring erzeugen, um es in baugruppe einzufügen
//				Timestamp timestamp = Timestamp.valueOf(rs.getString("datum"));
//				baugruppe.setEditDate(timestamp);
//				//Baugruppe der ArrayList hinzufügen
//				vBaugruppe.addElement(baugruppe);
//			}
//		}
//		catch(SQLException e){
//			e.printStackTrace();
//		}
//		return vBaugruppe;
//	}
	
	public Vector<Baugruppe> findAll(){
		Vector<Baugruppe> vBaugruppe = new Vector<Baugruppe>();
		Connection con = DBConnection.connection();
		try{
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM Baugruppe JOIN User ON Baugruppe.bearbeitet_Von=User.userID;");
			//Da es viele Baugruppen geben kann müssen wir eine Schleife benutzen
			while(rs.next()){
				//Neue Baugruppe erzeugen
				Baugruppe baugruppe = new Baugruppe();
		    	baugruppe.setId(rs.getInt("bg_ID"));
		    	baugruppe.setName(rs.getString("name"));
		    	//Da wir die Stueckliste der Baugruppe auflösen müssen brauchen wir einen StuecklistenMapper
		    	StuecklisteMapper slm = StuecklisteMapper.stuecklisteMapper();
		    	baugruppe.setStueckliste(slm.findById(rs.getInt("stueckliste")));
		    	//Neuen User erzeugen
		    	User user = new User();
		    	user.setId(rs.getInt("userID"));
		    	user.setName(rs.getString("eMail"));
		    	user.setGoogleID(rs.getString("googleID"));
		    	baugruppe.setEditUser(user);
		    	// Java Util Date wird umgewandelt in SQL Date um das Änderungsdatum in
		    	 // die Datenbank zu speichern 
		     	 java.sql.Timestamp sqlDate = rs.getTimestamp("datum");
		     	baugruppe.setEditDate(sqlDate);
				//Baugruppe der ArrayList hinzufügen
				vBaugruppe.addElement(baugruppe);
			}
		}
		catch(SQLException e){
			e.printStackTrace();
		}
		return vBaugruppe;
	}
}