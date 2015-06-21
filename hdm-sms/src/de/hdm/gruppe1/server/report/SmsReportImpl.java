package de.hdm.gruppe1.server.report;






import java.util.Vector;

import de.hdm.gruppe1.server.SmsImpl;
import de.hdm.gruppe1.shared.Sms;
import de.hdm.gruppe1.shared.SmsReport;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import de.hdm.gruppe1.shared.bo.*;
import de.hdm.gruppe1.shared.report.*;



/**
 * Implementierung des <code>SmsReport</code>-Interface. Die technische
 * Realisierung bzgl. <code>RemoteServiceServlet</code> bzw. GWT RPC erfolgt
 * analog zu {@lBankAdministrationImplImpl}. Für Details zu GWT RPC siehe dort.
 * 
 * @see ReportGenerator
 * @author thies
 */
 
@SuppressWarnings("serial")
public class SmsReportImpl extends RemoteServiceServlet implements SmsReport{
	
	/*
	   * ***************************************************************************
	   * ABSCHNITT, Beginn: Initialisierung
	   * ***************************************************************************
	   */
	
	/**
     * Ein ReportGenerator benötigt Zugriff auf die Verwaltungsklasse, da diese
     * die essentiellen Methoden für die Koexistenz von Datenobjekten (vgl.
     * bo-Package) bietet.
     */
	
	private Sms verwaltung = null;
	
	  /**
     * <p>
     * Ein <code>RemoteServiceServlet</code> wird unter GWT mittels
     * <code>GWT.create(Klassenname.class)</code> Client-seitig erzeugt. Hierzu
     * ist ein solcher No-Argument-Konstruktor anzulegen. Ein Aufruf eines
     * anderen Konstruktors ist durch die Client-seitige Instantiierung durch
     * <code>GWT.create(Klassenname.class)</code> nach derzeitigem Stand nicht
     * möglich.
     * </p>
     * <p>
     * Es bietet sich also an, eine separate Instanzenmethode zu erstellen, die
     * Client-seitig direkt nach <code>GWT.create(Klassenname.class)</code>
     * aufgerufen wird, um eine Initialisierung der Instanz vorzunehmen.
     * </p>
     */
  
	 public SmsReportImpl() throws IllegalArgumentException {
	    }
	
	 
	 public void init() throws IllegalArgumentException {
		 /*
	         * Ein SmsReportImpl-Objekt instantiiert für seinen Eigenbedarf
	         * eine Impl-Instanz.
	         */
     SmsImpl a = new SmsImpl();
     a.init();
	 this.verwaltung = a;
		 
	 }
	 
	 
	  /**
	     * Auslesen der zugehörigen Verwaltungsklasse (interner Gebrauch).
	     * 
	     * @return das Verwaltungsklassenobjekt
	     */
	
	  protected Sms getSms() {
		    return this.verwaltung;
		  }

		  /**
		   * Setzen des zugehörigen Baugrppen-Objekte.
		   */
		  public void createBaugruppe(String name, Vector<ElementPaar> BauteilPaare,
				Vector<ElementPaar> BaugruppenPaare) {
		    this.verwaltung.createBaugruppe(name, BauteilPaare, BaugruppenPaare);
		  }

	  
  protected void addImprint(Report r) {
	    /*
	     * Das Impressum soll wesentliche Informationen über die Bank enthalten.
	     */
	   Baugruppe baugruppe= this.verwaltung.createBaugruppe(null, null, null);

	    /*
	     * Das Imressum soll mehrzeilig sein.
	     */
	    CompositeParagraph imprint = new CompositeParagraph();

	    imprint.addSubParagraph(new SimpleParagraph(baugruppe.getName()));
	

	    // Das eigentliche Hinzufügen des Impressums zum Report.
	    r.setImprint(imprint);

	  }
	 

	
	
    public StrukturStuecklisteReport createStrukturStuecklisteReport(Baugruppe b)throws IllegalArgumentException {
	 
	        if (this.getSms() == null)
	            return null;
	 
	        /*
	         * Zunächst legen wir uns einen leeren Report an.
	         */
	        StrukturStuecklisteReport result = new  StrukturStuecklisteReport();
	 
        // Jeder Report hat einen Titel (Bezeichnung / Überschrift).
	        result.setTitle("Strukturstueckliste der Baugruppe");
	 
        // Imressum hinzufügen
	        this.addImprint(result);
	 
	        /*
	         * Datum der Erstellung hinzufügen. new Date() erzeugt autom. einen
	         * "Timestamp" des Zeitpunkts der Instantiierung des Date-Objekts.
	         */
	      
	        
        /*
	       * Ab hier erfolgt die Zusammenstellung der Kopfdaten (die Dinge, die
	         * oben auf dem Report stehen) des Reports. Die Kopfdaten sind
	         * mehrzeilig, daher die Verwendung von CompositeParagraph.
	         */
	        CompositeParagraph header = new CompositeParagraph();
	 
	      
	        // Baugruppen Name aufnehmen
	        header.addSubParagraph(new SimpleParagraph(b.getName()  ));     
		    
	 
	        // Hinzufügen der zusammengestellten Kopfdaten zu dem Report
	        result.setHeaderData(header);
	 
	        /*
	         * Ab hier erfolgt ein zeilenweises Hinzufügen von der
	         * Struckturstueckliste
	         */
	 
	        /*
	         * Zunächst legen wir eine Kopfzeile für die Strukturstuecklisten-Tabelle
	         * an.
	         */
	        Row headline = new Row();
	        headline.addColumn(new Column("Stücklisten-Nr."));
	        

	        // Hinzufügen der Kopfzeile
	       result.addRow(headline);

	       
	       Vector <Baugruppe> baugruppen = this.verwaltung.getAllBaugruppen();

	       for (Baugruppe bg:baugruppen){
	    	   Row baugruppeRow =new Row();
	      baugruppeRow.addColumn(new Column(String.valueOf(bg.getId())));
	      baugruppeRow.addColumn(new Column(String.valueOf(bg.getName())));
	      result.addRow(baugruppeRow);
	       }
	  	return null;
	   	}

	      
    
//	       int hilfsZeitslotId = 1;
//	       
//	        accountRow.addColumn(new Column(verwaltung.getZeitslotById(
//	                hilfsZeitslotId).toString()));
//	 
//	        hilfsZeitslotId = hilfsZeitslotId + 6;
//	 
//	        /*
//	         * Nun werden sämtliche Stundenplaneintraege des Dozenten ausgelesen und
//	         * in die Tabelle eingetragen. Dabei läuft die For-Schleife die Anzahl
//	         * der Zeitslot durch und Überprüft auf vorhandene Daten.
//	         */
//	 
//	        for (int i = 1; i < 37; i++) {
//	 
//	            Stundenplaneintrag aktuell = this.verwaltung
//	                    .getStundenplaneintragByDozentAndZeitslot(
//	                            d.getId(), i, studienhalbjahr);
//	 
//	            if (aktuell != null) {
//	                accountRow.addColumn(new Column(
//	 
//	                verwaltung.getLehrveranstaltungById(
//	                        aktuell.getLehrveranstaltungId()).toString()
//	                        + "\n"
//	                        + verwaltung.getRaumById(aktuell.getRaumId())
//	                                .toString()));
//	 
//	            } else {
//	                accountRow.addColumn(new Column("----"));
//	            }
//	 
//	            /*
//	             * Folgend wird am Ende der Zeile, die Reihe dem Gesamten
//	             * hinzugefügt und eine neue Reihe erzeugt, welche mit der
//	             * jeweiligen Uhrzeit beginnt
//	             */
//	 
//	            if (i == 6 | i == 12 | i == 18 | i == 24 | i == 30) {
//	 
//	                result.addRow(accountRow);
//	                accountRow = new Row();
//	                accountRow.addColumn(new Column(verwaltung
//	                          .getZeitslotById(hilfsZeitslotId)
//	                          .toString()));
//	                hilfsZeitslotId = hilfsZeitslotId + 6;
//	            }
//	             
//	            if ( i == 36){
//	                result.addRow(accountRow);
//	                }
//	        }
//	        /*
//	         * Zum Schluss müssen wir noch den fertigen Report zurückgeben.
//	         */
//	        return result;
//	    }
//	 
//	    public void setRaum(Raum r) throws IllegalArgumentException {
//	        this.verwaltung.setRaum(r);
//	    }
//	 
	    
    


	@Override
	public MaterialBedarfReport createMaterialBedarfReport()
			throws IllegalArgumentException {
		// TODO Auto-generated method stub
		return null;
	}





	

		}