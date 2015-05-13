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
