
/**
 * 
 */
package de.hdm.gruppe1.server.db;

import de.hdm.gruppe1.shared.bo.User;

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
	
	public User insert(User user){
		return user;
	}
	
	public User findByGoogleID (String googleID){
		User user = new User();
		
		return user;
	}
	
	public User findByUserID(int userID){
		User user = new User();
		
		return user;
	}
	
	
}