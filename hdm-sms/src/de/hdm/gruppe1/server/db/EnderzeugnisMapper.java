/**
 * 
 */
package de.hdm.gruppe1.server.db;

import java.sql.*;
import java.util.Vector;

import de.hdm.gruppe1.shared.bo.*;

/**
 * @author Andreas Herrmann
 *
 */
public class EnderzeugnisMapper {

	private static EnderzeugnisMapper enderzeugnisMapper = null;
	
	protected EnderzeugnisMapper(){
		
	}
	
	public static EnderzeugnisMapper enderzeugnisMapper(){
		if(enderzeugnisMapper==null){
			enderzeugnisMapper = new EnderzeugnisMapper();
		}
		return enderzeugnisMapper;
	}
	
	public Enderzeugnis insert(Enderzeugnis enderzeugnis){
		Connection con = DBConnection.connection();
		//Da ich ein int nicht einfach durch casting in einen String wandeln kann, muss dies Ã¼ber eine Instanz der Klasse Integer geschehen
	    Integer baugruppenID = new Integer(enderzeugnis.getBaugruppe().getId());
		try{
			Statement stmt = con.createStatement();
			//MaxID von StuecklistenBauteile abfragen
			ResultSet rs = stmt.executeQuery("SELECT MAX(ee_ID) AS maxid "
			          + "FROM Enderzeugnis;");

			     // Wenn wir etwas zurückerhalten, kann dies nur einzeilig sein
			     if (rs.next()) {
			        /*
			         * a erhält den bisher maximalen, nun um 1 inkrementierten
			         * Primärschlüssel.
			         */
			    	enderzeugnis.setId(rs.getInt("maxid")+1);
					stmt.executeUpdate("INSERT INTO Enderzeugnis VALUES ('"
			    	+enderzeugnis.getId()+"','"+enderzeugnis.getName()+"','"+baugruppenID+"');");
			      }
		}
		catch(SQLException e){
			e.printStackTrace();
		}
		return enderzeugnis;
	}
	
	public Enderzeugnis update(Enderzeugnis enderzeugnis){
		Connection con = DBConnection.connection();
		//Da ich ein int nicht einfach durch casting in einen String wandeln kann, muss dies Ã¼ber eine Instanz der Klasse Integer geschehen
		Integer enderzeugnisID = new Integer(enderzeugnis.getId());
	    Integer baugruppenID = new Integer(enderzeugnis.getBaugruppe().getId());
		try{
			Statement stmt = con.createStatement();
			stmt.executeUpdate("UPDATE Enderzeugnis SET name='"+enderzeugnis.getName()+"',baugruppe='"+baugruppenID+"' WHERE ee_ID='"
				+enderzeugnisID.toString()+"';");
		}
		catch(SQLException e){
			e.printStackTrace();
		}
		return enderzeugnis;
	}
	
	public boolean delete(Enderzeugnis enderzeugnis){
		Connection con = DBConnection.connection();
		//Da ich ein int nicht einfach durch casting in einen String wandeln kann, muss dies Ã¼ber eine Instanz der Klasse Integer geschehen
		Integer enderzeugnisID = new Integer(enderzeugnis.getId());
		try{
			Statement stmt = con.createStatement();
			if(stmt.executeUpdate("DELETE FROM Enderzeugnis WHERE 'ee_ID='"+enderzeugnisID+"';")==0){
				return false;
			}
			else{
				return true;
			}
		}
		catch(SQLException e){
			e.printStackTrace();
			return false;
		}
	}
	
	public Enderzeugnis findByID(int id){
		Connection con = DBConnection.connection();
		
		Enderzeugnis enderzeugnis = new Enderzeugnis();
		//Da ich ein int nicht einfach durch casting in einen String wandeln kann, muss dies Ã¼ber eine Instanz der Klasse Integer geschehen
		Integer enderzeugnisID = new Integer(id);
		try{
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM 'Enderzeugnis' WHERE 'ee_ID'="+enderzeugnisID+"';");
			if(rs.next()){
				enderzeugnis.setId(rs.getInt("ee_ID"));
				enderzeugnis.setName(rs.getString("name"));
				
				// Add User to Enderzeugnis
				int zugehörigeUserID = rs.getInt("bearbeitet_Von");
				UserMapper um = UserMapper.userMapper(); 
				User zugehörigerUser = um.findByID(zugehörigeUserID);
				enderzeugnis.setEditUser(zugehörigerUser);
				
				// Add Baugruppe to Enderzeugnis
				int zugehörigeBaugruppeID = rs.getInt("baugruppe");
				BaugruppenMapper bm = BaugruppenMapper.baugruppenMapper(); 
				Baugruppe zugehörigeBaugruppe = bm.findByID(zugehörigeBaugruppeID);
				enderzeugnis.setBaugruppe(zugehörigeBaugruppe);
				
				// Java Util Date wird umgewandelt in SQL Date um das Änderungsdatum in
		    	 // die Datenbank zu speichern 
		     	 java.sql.Timestamp sqlDate = rs.getTimestamp("datum");
		     	 enderzeugnis.setEditDate(sqlDate);
			}
		}
		catch(SQLException e){
			e.printStackTrace();
		}
		return enderzeugnis;
	}
	
//	public Vector<Enderzeugnis> findByName(String name){
//		Connection con = DBConnection.connection();
//		BaugruppenMapper bMapper = BaugruppenMapper.baugruppenMapper();
//		Vector<Enderzeugnis> vEnderzeugnis = new Vector<Enderzeugnis>();
//		try{
//			Statement stmt = con.createStatement();
//			ResultSet rs = stmt.executeQuery("SELECT * FROM 'Enderzeugnis' WHERE 'name' LIKE '%"+name+"%';");
//			while(rs.next()){
//				Enderzeugnis enderzeugnis = new Enderzeugnis();
//				enderzeugnis.setId(rs.getInt("ee_ID"));
//				enderzeugnis.setName(rs.getString("name"));
//				enderzeugnis.setBaugruppe(bMapper.findByID(rs.getInt("baugruppe")));
//				vEnderzeugnis.addElement(enderzeugnis);
//			}	
//		}
//		catch(SQLException e){
//			e.printStackTrace();
//		}
//		return vEnderzeugnis;
//	}
	
	public Vector<Enderzeugnis> findAll(){
		Vector<Enderzeugnis> vEnderzeugnis = new Vector<Enderzeugnis>();
		Connection con = DBConnection.connection();
		try{
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM Enderzeugnis;");
			//Da es viele Baugruppen geben kann müssen wir eine Schleife benutzen
			while(rs.next()){
				
				//Neue Baugruppe erzeugen
				Enderzeugnis enderzeugnis = new Enderzeugnis();
				enderzeugnis.setId(rs.getInt("ee_ID"));
				enderzeugnis.setName(rs.getString("name"));
				
				// Java Util Date wird umgewandelt in SQL Date um das Änderungsdatum in
		    	 // die Datenbank zu speichern 
		     	 java.sql.Timestamp sqlDate = rs.getTimestamp("datum");
		     	 enderzeugnis.setEditDate(sqlDate);
				
				// Add User to Enderzeugnis
				int zugehörigeUserID = rs.getInt("bearbeitet_Von");
				UserMapper um = UserMapper.userMapper(); 
				User zugehörigerUser = um.findByID(zugehörigeUserID);
				enderzeugnis.setEditUser(zugehörigerUser);
				
				int zugehörigeBaugruppeID = rs.getInt("baugruppe");
				BaugruppenMapper bm = BaugruppenMapper.baugruppenMapper(); 
				Baugruppe zugehörigeBaugruppe = bm.findByID(zugehörigeBaugruppeID);
				enderzeugnis.setBaugruppe(zugehörigeBaugruppe);
				
				vEnderzeugnis.addElement(enderzeugnis);
				

				
			}
		}
		catch(SQLException e){
			e.printStackTrace();
		}
		return vEnderzeugnis;
	}
}