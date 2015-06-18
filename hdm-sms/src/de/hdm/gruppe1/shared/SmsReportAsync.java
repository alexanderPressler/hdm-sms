package de.hdm.gruppe1.shared;

import com.google.gwt.user.client.rpc.AsyncCallback;

import de.hdm.gruppe1.shared.report.MaterialBedarfReport;
import de.hdm.gruppe1.shared.report.StrukturStuecklisteReport;


/**
 * Das asynchrone Gegenstück des Interface {@link BankAdministration}. Es wird
 * semiautomatisch durch das Google Plugin erstellt und gepflegt. Daher erfolgt
 * hier keine weitere Dokumentation. Für weitere Informationen siehe das
 * synchrone Interface {@link BankAdministration}.
 * 
 * @author Alexander Pressler &  thies
 */
public interface SmsReportAsync {

	void createMaterialBedarfReport(AsyncCallback<MaterialBedarfReport> callback);

	void createStrukturStuecklisteReport(
			AsyncCallback<StrukturStuecklisteReport> callback);

	void init(AsyncCallback<Void> callback);

}