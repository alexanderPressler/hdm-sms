package de.hdm.gruppe1.shared.report;

import com.google.gwt.user.client.rpc.AsyncCallback;

import de.hdm.gruppe1.shared.bo.Baugruppe;
import de.hdm.gruppe1.shared.bo.Enderzeugnis;


/**
 * Das asynchrone Gegenstück des Interface {@link BankAdministration}. Es wird
 * semiautomatisch durch das Google Plugin erstellt und gepflegt. Daher erfolgt
 * hier keine weitere Dokumentation. Für weitere Informationen siehe das
 * synchrone Interface {@link BankAdministration}.
 * 
 * @author Alexander Pressler &  thies
 */
public interface SmsReportAsync {

	void createStrukturStuecklisteReport(int id,
			AsyncCallback<Baugruppe> callback);

	void createMaterialBedarfReport(int id, int anzahl,
			AsyncCallback<Enderzeugnis> callback);

}