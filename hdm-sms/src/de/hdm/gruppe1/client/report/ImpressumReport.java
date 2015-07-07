package de.hdm.gruppe1.client.report;

/**
 * Die Klasse ImpressumReport ermöglicht dem Report, einen Impressum-String zu erzeugen und im HTML-Widget
 * mithilfe der HTML-Tags wie gewünscht anzuzeigen.
 * 
 * @author Mario Theiler, Katja Thiere
 *
 */
public class ImpressumReport {
	
	public String setImpressum(){
		
		String reportImpressum = new String("<b>Impressum</b></br>"
				+ "<b>(Angaben gemäß § 5 TMG)</b><br/>"
				+ "Hochschule der Medien<br/>"
				+ "Gruppe 1<br/>"
				+ "Nobelstraße 10<br/>"
				+ "70569 Stuttgart<br/>"
				+ "</p>"
				);		
		
		return reportImpressum;
		
	}

}
