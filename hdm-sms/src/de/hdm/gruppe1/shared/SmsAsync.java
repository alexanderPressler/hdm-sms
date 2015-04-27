package de.hdm.gruppe1.shared;

import com.google.gwt.user.client.rpc.AsyncCallback;

import de.hdm.gruppe1.shared.bo.Bauteil;

/**
 * The async counterpart of <code>GreetingService</code>.
 */
public interface SmsAsync {
	
	void createBauteil(String bauteilBeschreibung,
			String materialBeschreibung, AsyncCallback<Bauteil> callback);
	
	


}
