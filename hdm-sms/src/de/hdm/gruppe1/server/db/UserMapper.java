/**
 * 
 */
package de.hdm.gruppe1.server.db;

/**
 * @author Andreas Herrmann
 *
 */
public class UserMapper {
	private static UserMapper userMapper = null;
	
	protected UserMapper(){
		
	}
	
	public static UserMapper userMapper() {
		if(userMapper==null){
			userMapper= new UserMapper();
		}
		return userMapper;
	}
	
}
