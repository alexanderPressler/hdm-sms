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
		
	}
	
	public Stueckliste update(Stueckliste stueckliste){
		
	}
	
	public boolean delete(Stueckliste stueckliste){
		
	}
	
	public Stueckliste findByID(int id){
		
	}
	
	public ArrayList<Stueckliste> findByName(String name){
		
	}
	
	public ArrayList<Stueckliste> getAll(){
		
	}
}
