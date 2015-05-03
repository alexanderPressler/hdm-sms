package de.hdm.gruppe1.server.db;

public class BaugruppeMapper {
	
	private static BaugruppeMapper baugruppeMapper = null;
	
	
	public static BaugruppeMapper baugruppeMapper() {
		    if (baugruppeMapper == null) {
		    	baugruppeMapper = new BaugruppeMapper();
		    }

		    return baugruppeMapper;
		  }
}
