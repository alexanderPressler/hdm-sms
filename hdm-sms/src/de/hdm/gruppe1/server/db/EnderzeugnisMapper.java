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
		
	}
	
	public boolean delete(Enderzeugnis enderzeugnis){
		
	}
	
	public Enderzeugnis findByID(int id){
		
	}
	
	public ArrayList<Enderzeugnis> findByName(String name){
		
	}
	
	public ArrayList<Enderzeugnis> getAll(){
		
	}
}
