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
	
	public Enderzeugnis insert(Enderzeugnis enderzeugnis) throws SQLException{
		Connection con = DBConnection.connection();
		Statement stmt = con.createStatement();
		//Da ich ein int nicht einfach durch casting in einen String wandeln kann, muss dies über eine Instanz der Klasse Integer geschehen
	    Integer baugruppenID = new Integer(enderzeugnis.getBaugruppe().getId());
		try{
			ResultSet rs = stmt.executeQuery("INSERT INTO 'Enderzeugnis' ('name','baugruppe') VALUES"
					+ " ('"+enderzeugnis.getName()+"','"+baugruppenID.toString()+"');");
			if(rs.next()){
				enderzeugnis.setId(rs.getInt("ee_ID"));
			}
		}
		catch(SQLException e){
			e.printStackTrace();
		}
		return enderzeugnis;
	}
	
	public Enderzeugnis update(Enderzeugnis enderzeugnis) throws SQLException{
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
	
	public boolean delete(Enderzeugnis enderzeugnis) throws SQLException{
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
	
	public Enderzeugnis findById(int id) throws SQLException{
		Connection con = DBConnection.connection();
		Statement stmt = con.createStatement();
		Enderzeugnis enderzeugnis = null;
		//Da ich ein int nicht einfach durch casting in einen String wandeln kann, muss dies über eine Instanz der Klasse Integer geschehen
		Integer enderzeugnisID = new Integer(id);
		EnderzeugnisMapper eMapper = EnderzeugnisMapper.enderzeugnisMapper();
		try{
			ResultSet rs = stmt.executeQuery("SELECT * FROM 'Enderzeugnis' WHERE 'ee_ID'="+enderzeugnisID.toString()+"';");
			if(rs.next()){
				Enderzeugnis ee = new Enderzeugnis();
				ee.setId(rs.getInt("ee_ID"));
				ee.setName(rs.getString("name"));
				ee.setBaugruppe(eMapper.findById(rs.getInt("baugruppe")));
			}
		}
		catch(SQLException e){
			e.printStackTrace();
		}
		return enderzeugnis;
	}
	
	public Vector<Enderzeugnis> findByName(String name) throws SQLException{
		Connection con = DBConnection.connection();
		Statement stmt = con.createStatement();
		EnderzeugnisMapper eMapper = EnderzeugnisMapper.enderzeugnisMapper();
		
		Vector<Enderzeugnis> vEnderzeugnis = null;
		try{
			ResultSet rs = stmt.executeQuery("SELECT * FROM 'Enderzeugnis' WHERE 'name' LIKE '%"+name+"%';");
			while(rs.next()){
				Enderzeugnis enderzeugnis = new Enderzeugnis();
				enderzeugnis.setId(rs.getInt("ee_ID"));
				enderzeugnis.setName(rs.getString("name"));
				enderzeugnis.setBaugruppe(eMapper.findById(rs.getInt("baugruppe")));
				vEnderzeugnis.addElement(enderzeugnis);
			}	
		}
		catch(SQLException e){
			e.printStackTrace();
		}
		return vEnderzeugnis;
	}
	

}