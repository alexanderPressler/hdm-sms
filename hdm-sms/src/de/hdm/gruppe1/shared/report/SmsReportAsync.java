package de.hdm.gruppe1.shared.report;

import java.util.Vector;

import com.google.gwt.user.client.rpc.AsyncCallback;

import de.hdm.gruppe1.shared.bo.Baugruppe;
import de.hdm.gruppe1.shared.bo.Enderzeugnis;
import de.hdm.gruppe1.shared.bo.Stueckliste;


/**
 * Das asynchrone Gegenstück des Interface {@link BankAdministration}. Es wird
 * semiautomatisch durch das Google Plugin erstellt und gepflegt. Daher erfolgt
 * hier keine weitere Dokumentation. Für weitere Informationen siehe das
 * synchrone Interface {@link BankAdministration}.
 * 
 * @author Alexander Pressler &  thies
 */
public interface SmsReportAsync {

//	void createMaterialBedarfReport(int id, int anzahl,
//			AsyncCallback<Enderzeugnis> callback);
//
//	void createBaugruppenReport(Stueckliste BaugruppenStueckliste,
//			AsyncCallback<BaugruppenReport> callback);

	void getAllBaugruppen(AsyncCallback<Vector<Baugruppe>> callback);

	void getAllEnderzeugnis(AsyncCallback<Vector<Enderzeugnis>> callback);

}