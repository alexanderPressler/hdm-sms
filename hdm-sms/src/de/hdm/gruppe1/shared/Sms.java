package de.hdm.gruppe1.shared;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import de.hdm.gruppe1.shared.bo.Bauteil;


/**
 * The client-side stub for the RPC service.
 */
@RemoteServiceRelativePath("greet")
public interface Sms extends RemoteService {
	

	Bauteil createBauteil(String bauteilBeschreibung,
			String materialBeschreibung) throws IllegalArgumentException;
	
	

}
