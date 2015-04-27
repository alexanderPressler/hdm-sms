package de.hdm.gruppe1.shared;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * The client-side stub for the RPC service.
 */
@RemoteServiceRelativePath("greet")
public interface Sms extends RemoteService {
	String greetServer(String name) throws IllegalArgumentException;
}
