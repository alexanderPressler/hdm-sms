/**
 * 
 */
package de.hdm.gruppe1.server.db;

import java.sql.*;
import java.util.ArrayList;

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
		Statement stmt = con.createStatement();
		//Da ich ein int nicht einfach durch casting in einen String wandeln kann, muss dies über eine Instanz der Klasse Integer geschehen
	    Integer baugruppenID = new Integer(enderzeugnis.getBaugruppe().getId());
		try{
			ResultSet rs = stmt.executeQuery("INSERT INTO 'Enderzeugnis' ('name','baugruppe') VALUES ('"+enderzeugnis.getName()+"','"+baugruppenID.toString()+"');");
			if(rs.next()){
				enderzeugnis.setId(rs.getInt("ee_ID"));
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
		//Da ich ein int nicht einfach durch casting in einen String wandeln kann, muss dies über eine Instanz der Klasse Integer geschehen
		Integer enderzeugnisID = new Integer(enderzeugnis.getId());
	    Integer baugruppenID = new Integer(enderzeugnis.getBaugruppe().getId());
		try{
			stmt.executeUpdate("UPDATE 'Enderzeugnis' SET 'name'='"+enderzeugnis.getName()+"','baugruppe'='"+baugruppenID.toString()+"' WHERE 'ee_ID'='"
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
		//Da ich ein int nicht einfach durch casting in einen String wandeln kann, muss dies über eine Instanz der Klasse Integer geschehen
		Integer enderzeugnisID = new Integer(enderzeugnis.getId());
		try{
			if(stmt.executeUpdate("DELETE FROM 'Enderzeugnis' WHERE 'ee_ID'='"+enderzeugnisID.toString()+"';")==0){
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
		//Da ich ein int nicht einfach durch casting in einen String wandeln kann, muss dies über eine Instanz der Klasse Integer geschehen
		Integer enderzeugnisID = new Integer(id);
		BaugruppenMapper bMapper = new BaugruppenMapper();
		try{
			ResultSet rs = stmt.executeQuery("SELECT * FROM 'Enderzeugnis' WHERE 'ee_ID'="+enderzeugnisID.toString()+"';");
			enderzeugnis = new Enderzeugnis();
			enderzeugnis.setId(rs.getInt("ee_ID"));
			enderzeugnis.setName(rs.getString("name"));
			enderzeugnis.setBaugruppe(bMapper.findByID(rs.getInt("baugruppe")));
		}
		catch(SQLException e){
			e.printStackTrace();
		}
		return enderzeugnis;
	}
	
	public ArrayList<Enderzeugnis> findByName(String name){
		Connection con = DBConnection.connection();
		Statement stmt = con.createStatement();
		
	}
	
	public ArrayList<Enderzeugnis> getAll(){
		
	}
}
