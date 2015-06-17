package de.hdm.gruppe1.shared;


import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import de.hdm.gruppe1.shared.report.MaterialBedarfReport;
import de.hdm.gruppe1.shared.report.StrukturStuecklisteReport;

/**
 * <p>
 * Synchrone Schnittstelle für eine RPC-fähige Klasse zur Erstellung von
 * Reports. Diese Schnittstelle benutzt die gleiche Realisierungsgrundlage wir
 * das Paar {@link BankAdministration} und {@lBankAdministrationImplImpl}. Zu
 * technischen Erläuterung etwa bzgl. GWT RPC bzw. {@link RemoteServiceServlet}
 * siehe {@link BankAdministration} undBankAdministrationImpltungImpl}.
 * </p>
 * <p>
 * Ein ReportGenerator bietet die Möglichkeit, eine Menge von Berichten
 * (Reports) zu erstellen, die Menge von Daten bzgl. bestimmter Sachverhalte des
 * Systems zweckspezifisch darstellen.
 * </p>
 * <p>
 * Die Klasse bietet eine Reihe von <code>create...</code>-Methoden, mit deren
 * Hilfe die Reports erstellt werden können. Jede dieser Methoden besitzt eine
 * dem Anwendungsfall entsprechende Parameterliste. Diese Parameter benötigt der
 * der Generator, um den Report erstellen zu können.
 * </p>
 * <p> 
 * Bei neu hinzukommenden Bedarfen an Berichten, kann diese Klasse auf einfache
 * Weise erweitert werden. Hierzu können zusätzliche <code>create...</code>
 * -Methoden implementiert werden. Die bestehenden Methoden bleiben davon
 * unbeeinflusst, so dass bestehende Programmlogik nicht verändert werden muss.
 * </p>
 * 
 * @author thies
 */
@RemoteServiceRelativePath("reportgenerator")
public interface SmsReport extends RemoteService {

  /**
   * Initialisierung des Objekts. Diese Methode ist vor dem Hintergrund von GWT
   * RPC zusätzlich zum No Argument Constructor der implementierenden Klasse
   *BankAdministrationImpltungImpl} notwendig. Bitte diese Methode direkt nach der
   * Instantiierung aufrufen.
   * 
   * @throws IllegalArgumentException
   */
  public void init() throws IllegalArgumentException;

  /**
   * Setzen der zugeordneten Bank.
   * 
   * @para Bank-Objekt
   * @throws IllegalArgumentException
   */
  //public void setBank(Bank b) throws IllegalArgumentException;

  /**
   * Erstellen eines <code>AllAccountsOfCustomerReport</code>-Reports. Dieser
   * Report-Typ stellt sämtliche Konten eines Kunden dar.
   * 
   * @param c eine Referenz auf das Kundenobjekt bzgl. dessen der Report
   *          erstellt werden soll.
   * @return das fertige Reportobjekt
   * @throws IllegalArgumentException
   * @see AllAccountsOfCustomerReport
   */
  public abstract MaterialBedarfReport createMaterialBedarfReport(
      ) throws IllegalArgumentException;

  /**
   * Erstellen eines <code>AllAccountsOfAllCustomersReport</code>-Reports.
   * Dieser Report-Typ stellt sämtliche Konten aller Kunden dar.
   * 
   * @return das fertige Reportobjekt
   * @throws IllegalArgumentException
   * @see AllAccountsOfAllCustomersReport
   */
  public abstract StrukturStuecklisteReport createStrukturStuecklisteReport()
      throws IllegalArgumentException;
}

