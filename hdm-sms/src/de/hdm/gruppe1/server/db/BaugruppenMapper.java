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
		
	}
	
	public Baugruppe update(Baugruppe baugruppe){
		
	}
	
	public boolean delete(Baugruppe baugruppe){
		
	}
	
	public Baugruppe findByID (int id){
		
	}
	
	public ArrayList<Baugruppe> findByName(String name){
		
	}
	public ArrayList<Baugruppe> getAll(){
		
	}
}
