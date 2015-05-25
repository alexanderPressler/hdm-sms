package de.hdm.gruppe1.shared.bo;

import java.io.Serializable;

public class User implements GoogleAccountAPI {

	
	private String googleId;
	private String email;
	
	
	
	public String getEmail(){
		return this.email;
	}
	public void setEmail(String email){
		this.email= email;
	}
	
	public String getId(){
		return this.googleId;
	}
	
	public void setId(String googleId){
		this.googleId = googleId;
	}
}

