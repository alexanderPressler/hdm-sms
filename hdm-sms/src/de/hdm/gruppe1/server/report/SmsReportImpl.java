package de.hdm.gruppe1.server.report;

import java.util.Vector;

import de.hdm.gruppe1.server.SmsImpl;
import de.hdm.gruppe1.server.db.*;
import de.hdm.gruppe1.shared.*;
import de.hdm.gruppe1.shared.bo.*;
import de.hdm.gruppe1.shared.report.SmsReport;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * Implementierung des <code>ReportGenerator</code>-Interface. Die technische
 * Realisierung bzgl. <code>RemoteServiceServlet</code> bzw. GWT RPC erfolgt
 * analog zu {@SmsImpl}. Für Details zu GWT RPC siehe dort.
 * 
 * @see SmsReport
 * @author Thies, Schmidt & Pressler 
 */
@SuppressWarnings("serial")
public class SmsReportImpl extends RemoteServiceServlet implements SmsReport {
	
	/**
	   * Ein ReportGenerator benötigt Zugriff auf die Stuecklisten Verwaltung (sms), da diese die
	   * essentiellen Methoden für die Koexistenz von Datenobjekten (vgl.
	   * bo-Package) bietet.
	   */
	private Sms stuecklistenVerwaltung = null;
	
	/*
	   * ***************************************************************************
	   * ABSCHNITT, Beginn: Initialisierung
	   * ***************************************************************************
	   */
	
	/**
	   * Initialsierungsmethode. Siehe dazu Anmerkungen zum No-Argument-Konstruktor
	   * {@link #ReportGeneratorImpl()}. Diese Methode muss für jede Instanz von
	   * <code>BankVerwaltungImpl</code> aufgerufen werden.
	   * 
	   * @see #SmsReportImpl()
	   */
	  public SmsReportImpl() throws IllegalArgumentException {
	    /*
	     * Eine weitergehende Funktion muss der No-Argument-Constructor nicht haben.
	     * Er muss einfach vorhanden sein.
	     */
	  }
	  
	  /**
	   * <p>
	   * Ein <code>RemoteServiceServlet</code> wird unter GWT mittels
	   * <code>GWT.create(Klassenname.class)</code> Client-seitig erzeugt. Hierzu
	   * ist ein solcher No-Argument-Konstruktor anzulegen. Ein Aufruf eines anderen
	   * Konstruktors ist durch die Client-seitige Instantiierung durch
	   * <code>GWT.create(Klassenname.class)</code> nach derzeitigem Stand nicht
	   * möglich.
	   * </p>
	   * <p>
	   * Es bietet sich also an, eine separate Instanzenmethode zu erstellen, die
	   * Client-seitig direkt nach <code>GWT.create(Klassenname.class)</code>
	   * aufgerufen wird, um eine Initialisierung der Instanz vorzunehmen.
	   * </p>
	   * 
	   * @see #init()
	   */
	  @Override
	public void init() throws IllegalArgumentException {
		/*
		 * Ein SmsReportImpl-Objekt instantiiert für seinen Eigenbedarf eine
		 * Impl-Instanz.
		 */
		SmsImpl a = new SmsImpl();
		a.init();
		this.stuecklistenVerwaltung = a;
	}
	  
	  /*
	   * ***************************************************************************
	   * ABSCHNITT, Ende: Initialisierung
	   * ***************************************************************************
	   */

	/*
	 * ***************************************************************************
	 * ABSCHNITT, Beginn: Methoden für Report 1
	 * ***************************************************************************
	 */
	  
	  /**
	   * Auslesen aller BaugruppenObjekte für Auswahl in Report 1 .
	   * @see #getAllBaugruppen()
	   */
	  @Override
	  public Vector<Baugruppe> getAllBaugruppen() throws IllegalArgumentException {
		  Vector<Baugruppe> alleBaugruppen = stuecklistenVerwaltung.getAllBaugruppen();
		  return alleBaugruppen;
	  }

	/*
	 * ***************************************************************************
	 * ABSCHNITT, Methoden für Report 1
	 * *****************************************
	 */
	
	/*
	 * ***************************************************************************
	 * ABSCHNITT, Beginn: Methoden für Report 2
	 * ***************************************************************************
	 */
	  
	  /**
	   * Auslesen aller Baugruppen für Auswahl in Report 2.
	   * @see #getAllEnderzeugnis()
	   */
	  @Override
	public Vector<Enderzeugnis> getAllEnderzeugnis() throws IllegalArgumentException {
		 Vector<Enderzeugnis> alleEnderzeugnisse = stuecklistenVerwaltung.getAllEnderzeugnis();
		 return alleEnderzeugnisse;
	  }

	/*
	 * ***************************************************************************
	 * ABSCHNITT, Methoden für Report 2
	 * ***************************************************************************
	 */

}