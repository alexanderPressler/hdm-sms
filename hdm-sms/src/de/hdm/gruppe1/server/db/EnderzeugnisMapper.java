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
			    	Statement stmt = con.createStatement();
					stmt.executeUpdate("INSERT INTO Enderzeugnis VALUES ('"
			    	+enderzeugnis.getId()+"','"+enderzeugnis.getName()+"','"+baugruppenID.toString()+"');");
			      }
		}
		catch(SQLException e){
			e.printStackTrace();
		}
		return enderzeugnis;
	}
	
	public Enderzeugnis update(Enderzeugnis enderzeugnis){
		Connection con = DBConnection.connection();
		Statement stmt = con.createStatement();
		//Da ich ein int nicht einfach durch casting in einen String wandeln kann, muss dies Ã¼ber eine Instanz der Klasse Integer geschehen
		Integer enderzeugnisID = new Integer(enderzeugnis.getId());
	    Integer baugruppenID = new Integer(enderzeugnis.getBaugruppe().getId());
		try{
			stmt.executeUpdate("UPDATE Enderzeugnis SET name='"+enderzeugnis.getName()+"',baugruppe='"+baugruppenID.toString()+"' WHERE ee_ID='"
				+enderzeugnisID.toString()+"';");
		}
		catch(SQLException e){
			e.printStackTrace();
		}
		return enderzeugnis;
	}
	
	public boolean delete(Enderzeugnis enderzeugnis){
		Connection con = DBConnection.connection();
		Statement stmt = con.createStatement();
		//Da ich ein int nicht einfach durch casting in einen String wandeln kann, muss dies Ã¼ber eine Instanz der Klasse Integer geschehen
		Integer enderzeugnisID = new Integer(enderzeugnis.getId());
		try{
			if(stmt.executeUpdate("DELETE FROM Enderzeugnis WHERE 'ee_ID='"+enderzeugnisID.toString()+"';")==0){
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
		Statement stmt = con.createStatement();
		Enderzeugnis enderzeugnis = null;
		//Da ich ein int nicht einfach durch casting in einen String wandeln kann, muss dies Ã¼ber eine Instanz der Klasse Integer geschehen
		Integer enderzeugnisID = new Integer(id);
		BaugruppenMapper bMapper = new BaugruppenMapper.BaugruppenMapper();
		try{
			ResultSet rs = stmt.executeQuery("SELECT * FROM 'Enderzeugnis' WHERE 'ee_ID'="+enderzeugnisID.toString()+"';");
			if(rs.next()){
				Enderzeugnis enderzeugnis = new Enderzeugnis();
				enderzeugnis.setId(rs.getInt("ee_ID"));
				enderzeugnis.setName(rs.getString("name"));
				enderzeugnis.setBaugruppe(bMapper.findByID(rs.getInt("baugruppe")));
			}
		}
		catch(SQLException e){
			e.printStackTrace();
		}
		return enderzeugnis;
	}
	
	public Vector<Enderzeugnis> findByName(String name){
		Connection con = DBConnection.connection();
		Statement stmt = con.createStatement();
		BaugruppenMapper bMapper = BaugruppenMapper.BaugruppenMapper();
		Vector<Enderzeugnis> vEnderzeugnis = null;
		try{
			ResultSet rs = stmt.executeQuery("SELECT * FROM 'Enderzeugnis' WHERE 'name' LIKE '%"+name+"%';");
			while(rs.next()){
				Enderzeugnis enderzeugnis = new Enderzeugnis();
				enderzeugnis.setId(rs.getInt("ee_ID"));
				enderzeugnis.setName(rs.getString("name"));
				enderzeugnis.setBaugruppe(bMapper.findByID(rs.getInt("baugruppe")));
				vEnderzeugnis.addElement(enderzeugnis);
			}	
		}
		catch(SQLException e){
			e.printStackTrace();
		}
		return vEnderzeugnis;
	}
	
	public Vector<Enderzeugnis> getAll(){
		Connection con = DBConnection.connection();
		try{
			
		}
	}
	public Vector<Enderzeugnis> findByBaugruppe(Baugruppe baugruppe){
		Connection con=DBConnection.connection();
		Vector<Enderzeugnis> vEnderzeugnis = new Vector<Enderzeugnis>();
		try{
			Statement stmt = con.createStatement();
			ResultSet eeResult = stmt.executeQuery("SELECT * FROM Enderzegnis WHERE baugruppe='"+baugruppe.getId()+"';");
			while(eeResult.next()){
				vEnderzeugnis.add(this.findByID(eeResult.getInt("ee_ID")));
			}
		}
		catch(SQLException e){
			e.printStackTrace();
		}
		return vEnderzeugnis;
	}
}
