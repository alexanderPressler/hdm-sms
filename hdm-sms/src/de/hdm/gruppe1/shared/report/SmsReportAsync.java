package de.hdm.gruppe1.shared.report;

import java.util.Vector;

import com.google.gwt.user.client.rpc.AsyncCallback;

import de.hdm.gruppe1.shared.bo.Baugruppe;
import de.hdm.gruppe1.shared.bo.Enderzeugnis;

/**
 * Das asynchrone Gegenstück des Interface {@link SmsReport}. Es wird
 * semiautomatisch durch das Google Plugin erstellt und gepflegt. Daher erfolgt
 * hier keine weitere Dokumentation. Für weitere Informationen siehe das
 * synchrone Interface {@link SmsReport}.
 * 
 * @author Thies, Schmidt & Pressler
 */
public interface SmsReportAsync {

	void getAllBaugruppen(AsyncCallback<Vector<Baugruppe>> callback);

	void getAllEnderzeugnis(AsyncCallback<Vector<Enderzeugnis>> callback);
}
