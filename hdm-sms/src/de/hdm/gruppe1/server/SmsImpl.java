package de.hdm.gruppe1.server;

import java.util.Date;
import java.util.Vector;

import de.hdm.gruppe1.shared.FieldVerifier;
import de.hdm.gruppe1.server.db.*;
import de.hdm.gruppe1.shared.*;
import de.hdm.gruppe1.shared.bo.*;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * <p>
 * Implementierungsklasse des Interface <code>BankAdministration</code>. Diese
 * Klasse ist <em>die</em> Klasse, die neben {@link ReportGeneratorImpl}
 * s�mtliche Applikationslogik (oder engl. Business Logic) aggregiert. Sie ist
 * wie eine Spinne, die s�mtliche Zusammenh�nge in ihrem Netz (in unserem Fall
 * die Daten der Applikation) �berblickt und f�r einen geordneten Ablauf und
 * dauerhafte Konsistenz der Daten und Abl�ufe sorgt.
 * </p>
 * <p>
 * Die Applikationslogik findet sich in den Methoden dieser Klasse. Jede dieser
 * Methoden kann als <em>Transaction Script</em> bezeichnet werden. Dieser Name
 * l�sst schon vermuten, dass hier analog zu Datenbanktransaktion pro
 * Transaktion gleiche mehrere Teilaktionen durchgef�hrt werden, die das System
 * von einem konsistenten Zustand in einen anderen, auch wieder konsistenten
 * Zustand �berf�hren. Wenn dies zwischenzeitig scheitern sollte, dann ist das
 * jeweilige Transaction Script daf�r verwantwortlich, eine Fehlerbehandlung
 * durchzuf�hren.
 * </p>
 * <p>
 * Diese Klasse steht mit einer Reihe weiterer Datentypen in Verbindung. Dies
 * sind:
 * <ol>
 * <li>{@link BankAdministration}: Dies ist das <em>lokale</em> - also
 * Server-seitige - Interface, das die im System zur Verf�gung gestellten
 * Funktionen deklariert.</li>
 * <li>{@link BankAdministrationAsync}: <code>BankVerwaltungImpl</code> und
 * <code>BankAdministration</code> bilden nur die Server-seitige Sicht der
 * Applikationslogik ab. Diese basiert vollst�ndig auf synchronen
 * Funktionsaufrufen. Wir m�ssen jedoch in der Lage sein, Client-seitige
 * asynchrone Aufrufe zu bedienen. Dies bedingt ein weiteres Interface, das in
 * der Regel genauso benannt wird, wie das synchrone Interface, jedoch mit dem
 * zus�tzlichen Suffix "Async". Es steht nur mittelbar mit dieser Klasse in
 * Verbindung. Die Erstellung und Pflege der Async Interfaces wird durch das
 * Google Plugin semiautomatisch unterst�tzt. Weitere Informationen unter
 * {@link BankAdministrationAsync}.</li>
 * <li> {@link RemoteServiceServlet}: Jede Server-seitig instantiierbare und
 * Client-seitig �ber GWT RPC nutzbare Klasse muss die Klasse
 * <code>RemoteServiceServlet</code> implementieren. Sie legt die funktionale
 * Basis f�r die Anbindung von <code>BankVerwaltungImpl</code> an die Runtime
 * des GWT RPC-Mechanismus.</li>
 * </ol>
 * </p>
 * <p>
 * <b>Wichtiger Hinweis:</b> Diese Klasse bedient sich sogenannter
 * Mapper-Klassen. Sie geh�ren der Datenbank-Schicht an und bilden die
 * objektorientierte Sicht der Applikationslogik auf die relationale
 * organisierte Datenbank ab. Zuweilen kommen "kreative" Zeitgenossen auf die
 * Idee, in diesen Mappern auch Applikationslogik zu realisieren. Siehe dazu
 * auch die Hinweise in {@link #delete(Customer)} Einzig nachvollziehbares
 * Argument f�r einen solchen Ansatz ist die Steigerung der Performance
 * umfangreicher Datenbankoperationen. Doch auch dieses Argument zieht nur dann,
 * wenn wirklich gro�e Datenmengen zu handhaben sind. In einem solchen Fall
 * w�rde man jedoch eine entsprechend erweiterte Architektur realisieren, die
 * wiederum s�mtliche Applikationslogik in der Applikationsschicht isolieren
 * w�rde. Also, keine Applikationslogik in die Mapper-Klassen "stecken" sondern
 * dies auf die Applikationsschicht konzentrieren!
 * </p>
 * <p>
 * Beachten Sie, dass s�mtliche Methoden, die mittels GWT RPC aufgerufen werden
 * k�nnen ein <code>throws IllegalArgumentException</code> in der
 * Methodendeklaration aufweisen. Diese Methoden d�rfen also Instanzen von
 * {@link IllegalArgumentException} auswerfen. Mit diesen Exceptions k�nnen z.B.
 * Probleme auf der Server-Seite in einfacher Weise auf die Client-Seite
 * transportiert und dort systematisch in einem Catch-Block abgearbeitet werden.
 * </p>
 * <p>
 * Es gibt sicherlich noch viel mehr �ber diese Klasse zu schreiben. Weitere
 * Infos erhalten Sie in der Lehrveranstaltung.
 * </p>
 * 
 * @see BankAdministration
 * @see BankAdministrationAsync
 * @see RemoteServiceServlet
 * @author Alexander Pressler & Thies
 */
@SuppressWarnings("serial")
public class SmsImpl extends RemoteServiceServlet implements
		Sms {

	
	/**
	 * Referenzen auf die DatenbankMapper, welche die BusinessObjekte-Objekte
	 * mit der Datenbank abgleicht.
	 */
	private BauteilMapper bauteilMapper = null;
	private StuecklisteMapper stuecklisteMapper = null;
	private UserMapper userMapper = null;
	private BaugruppenMapper baugruppenMapper = null;
	private EnderzeugnisMapper enderzeugnisMapper = null;
	
	/*
	   * Da diese Klasse ein gewisse Gr��e besitzt - dies ist eigentlich ein
	   * Hinweise, dass hier eine weitere Gliederung sinnvoll ist - haben wir zur
	   * besseren �bersicht Abschnittskomentare eingef�gt. Sie leiten ein Cluster in
	   * irgeneinerweise zusammengeh�riger Methoden ein. Ein entsprechender
	   * Kommentar steht am Ende eines solchen Clusters.
	   */

	  /*
	   * ***************************************************************************
	   * ABSCHNITT, Beginn: Initialisierung
	   * ***************************************************************************
	   */
	
	  /**
	   * <p>
	   * Ein <code>RemoteServiceServlet</code> wird unter GWT mittels
	   * <code>GWT.create(Klassenname.class)</code> Client-seitig erzeugt. Hierzu
	   * ist ein solcher No-Argument-Konstruktor anzulegen. Ein Aufruf eines anderen
	   * Konstruktors ist durch die Client-seitige Instantiierung durch
	   * <code>GWT.create(Klassenname.class)</code> nach derzeitigem Stand nicht
	   * m�glich.
	   * </p>
	   * <p>
	   * Es bietet sich also an, eine separate Instanzenmethode zu erstellen, die
	   * Client-seitig direkt nach <code>GWT.create(Klassenname.class)</code>
	   * aufgerufen wird, um eine Initialisierung der Instanz vorzunehmen.
	   * </p>
	   * 
	   * @see #init()
	   */
	  public SmsImpl() throws IllegalArgumentException {
	    /*
	     * Eine weitergehende Funktion muss der No-Argument-Constructor nicht haben.
	     * Er muss einfach vorhanden sein.
	     */
	  }
	  
	  /**
	   * Initialsierungsmethode. Siehe dazu Anmerkungen zum No-Argument-Konstruktor
	   * {@link #ReportGeneratorImpl()}. Diese Methode muss f�r jede Instanz von
	   * <code>BankVerwaltungImpl</code> aufgerufen werden.
	   * 
	   * @see #ReportGeneratorImpl()
	   */
	  @Override
	public void init() throws IllegalArgumentException {
	    /*
	     * Ganz wesentlich ist, dass die BankAdministration einen vollst�ndigen Satz
	     * von Mappern besitzt, mit deren Hilfe sie dann mit der Datenbank
	     * kommunizieren kann.
	     */
	    this.bauteilMapper = BauteilMapper.bauteilMapper();
	    this.stuecklisteMapper = StuecklisteMapper.stuecklisteMapper();
	    this.userMapper = UserMapper.userMapper();
	    this.baugruppenMapper = BaugruppenMapper.baugruppenMapper();
	    this.enderzeugnisMapper = EnderzeugnisMapper.enderzeugnisMapper();
	  }
	  /*
	   * ***************************************************************************
	   * ABSCHNITT, Ende: Initialisierung
	   * ***************************************************************************
	   */
	  
	  /*
	   * ***************************************************************************
	   * ABSCHNITT, Beginn: Methoden f�r Bauteil-Objekte
	   * ***************************************************************************
	   */
	  /**
	   * <p>
	   * Anlegen eines neuen Bauteiles. Dies f�hrt implizit zu einem Speichern des
	   * neuen Bauteiles in der Datenbank.
	   * </p>
	   * 
	   * <p>
	   * <b>HINWEIS:</b> �nderungen an Bauteil-Objekten m�ssen stets durch Aufruf
	   * von {@link #save(Bauteil b)} in die Datenbank transferiert werden.
	   * </p>
	   * 
	   * @see save(Bauteil b)
	   */
	  @Override
	public Bauteil createBauteil(String name, String bauteilBeschreibung, String materialBeschreibung)
	      throws IllegalArgumentException {
	    Bauteil b = new Bauteil();
	    b.setName(name);
	    b.setBauteilBeschreibung(bauteilBeschreibung);
	    b.setMaterialBeschreibung(materialBeschreibung);
	    
	    // Erstellungsdatum wird generiert und dem Objekt angeh�ngt
	    Date date = new Date();
	    b.setEditDate(date);
	    
	  //TODO dynamisch anpassen
        User editUser = new User();
        editUser.setName("statischer User");
        editUser.setId(1);
        editUser.setGoogleID("000000000000");
        b.setEditUser(editUser);

	    /*
	     * Setzen einer vorl�ufigen Kundennr. Der insert-Aufruf liefert dann ein
	     * Objekt, dessen Nummer mit der Datenbank konsistent ist.
	     */

	    // Objekt in der DB speichern.
	    
        
        return this.bauteilMapper.insert(b);
	  }
	  

	  /**
	   * Speichern eines Bauteils.
	   */
	  @Override
	public void save(Bauteil b) throws IllegalArgumentException {
		 
		//TODO dynamisch anpassen
	        User editUser = new User();
	        editUser.setName("statischer User");
	        editUser.setId(1);
	        editUser.setGoogleID("000000000000");
	        b.setEditUser(editUser);
	    
	        // Aenderungsdatum wird generiert und dem Objekt angeh�ngt
	        // Das Datum wird zum Zeitpunkt des RPC Aufrufs erstellt
		    Date date = new Date();
		    b.setEditDate(date);
	        
		  this.bauteilMapper.update(b);
	  }
	  
	  /**
	   * L�schen eines Kunden. Nat�rlich w�rde ein reales System zur Verwaltung von
	   * Bankkunden ein L�schen allein schon aus Gr�nden der Dokumentation nicht
	   * bieten, sondern deren Status z.B von "aktiv" in "ehemalig" �ndern. Wir
	   * wollen hier aber dennoch zu Demonstrationszwecken eine L�schfunktion
	   * vorstellen.
	   */
	  @Override
	public void delete(Bauteil b) throws IllegalArgumentException {
	 
		  Vector<Stueckliste> vStueckliste = this.stuecklisteMapper.findByBauteil(b);
		
		  //Abgleich mit St�cklisten
		  if(vStueckliste.isEmpty()==true){
			  this.bauteilMapper.delete(b);
		  } else {
			  //TODO Exception schreiben
			  System.out.println("Bauteil wird in St�ckliste verwendet: ");
			  
		  }
	    
	  }
	  
	  /**
	   * Auslesen aller Bauteile.
	   */
	  @Override
	public Vector<Bauteil> getAllBauteile() throws IllegalArgumentException {
	    return this.bauteilMapper.findAll();
	  }
	  
	  /**
	   * Auslesen eines Bauteils anhand seiner Id.
	   */
	  @Override
	public Bauteil getBauteilById(int id) throws IllegalArgumentException {
	    return this.bauteilMapper.findById(id);
	  }
	  
	  /*
	   * ***************************************************************************
	   * ABSCHNITT, Ende: Methoden f�r Bauteil-Objekte
	   * ***************************************************************************
	   */
	  
	  /*
	   * ***************************************************************************
	   * ABSCHNITT, : Methoden f�r Stueckliste-Objekte
	   * ***************************************************************************
	   */
	  /**
	   * <p>
	   * Anlegen eines neuen Stueckliste. Dies f�hrt implizit zu einem Speichern des
	   * neuen Stuecklistes in der Datenbank.
	   * @see createStueckliste(String name)
	   */
	  @Override
	public Stueckliste createStueckliste(String name, Vector<ElementPaar> BauteilPaare, 
			Vector<ElementPaar> BaugruppenPaare ) throws IllegalArgumentException {
		Stueckliste s = new Stueckliste();
	    s.setName(name);
	    s.setBauteilPaare(BauteilPaare);
	    s.setBaugruppenPaare(BaugruppenPaare);
	    
	 // Erstellungsdatum wird generiert und dem Objekt angeh�ngt
	    Date date = new Date();
	    s.setEditDate(date);
	    
	  //TODO dynamisch anpassen
        User editUser = new User();
        editUser.setName("statischer User");
        editUser.setId(1);
        editUser.setGoogleID("000000000000");
        s.setEditUser(editUser);
	    

	    // Objekt in der DB speichern.
	    return this.stuecklisteMapper.insert(s);
	  }
	  /**
	   * L�schen einer Stueckliste 
	   */
	  @Override
	public void deleteStueckliste(Stueckliste s) throws IllegalArgumentException {
	 	  
		  BaugruppenMapper bm = BaugruppenMapper.baugruppenMapper();
		  Baugruppe b = bm.findBaugruppeByStueckliste(s);
		  
		  if (b==null){
			  this.stuecklisteMapper.delete(s);  
		  }
		  
		  //TODO Exception
		  else {
			  
			  System.out.println("Stueckliste kann nicht gel�scht werden: ");
			  System.out.println("Stueckliste wird verwendet in: "+ b.getName());
			 
		  }
		  
		  
	  }
	  /**
	   * Speichern eines Bauteils.
	   */
	  @Override
	public void saveStueckliste(Stueckliste s) throws IllegalArgumentException {
		
		// Aenderungsdatum wird generiert und dem Objekt angeh�ngt
		    Date date = new Date();
		    s.setEditDate(date);
		  
		  //TODO dynamisch anpassen
	        User editUser = new User();
	        editUser.setName("statischer User");
	        editUser.setId(1);
	        editUser.setGoogleID("000000000000");
	        s.setEditUser(editUser);
		  
		  this.stuecklisteMapper.update(s);
	  }
	  /**
	   * Auslesen aller Stuecklisten.
	   */
	  @Override
	public Vector<Stueckliste> getAllStuecklisten() throws IllegalArgumentException {
	    return this.stuecklisteMapper.findAll();
	  }
	  /*
	   * ***************************************************************************
	   * ABSCHNITT, Ende: Methoden f�r Stuecklisten-Objekte
	   * ***************************************************************************
	   */
	  
	  /*
	   * ***************************************************************************
	   * ABSCHNITT, Beginn: Methoden f�r User-Objekte
	   * ***************************************************************************
	   */
	  /**
	   * <p>
	   * Anlegen eines neuen Bauteiles. Dies f�hrt implizit zu einem Speichern des
	   * neuen Bauteiles in der Datenbank.
	   * </p>
	   * 
	   * <p>
	   * <b>HINWEIS:</b> �nderungen an Bauteil-Objekten m�ssen stets durch Aufruf
	   * von {@link #save(Bauteil b)} in die Datenbank transferiert werden.
	   * </p>
	   * 
	   * @see save(Bauteil b)
	   */
	  @Override
	public User createUser(String googleID, String name)
	      throws IllegalArgumentException {
	    User u = new User();
	    u.setGoogleID(googleID);
	    u.setName(name);

	    /*
	     * Setzen einer vorl�ufigen Kundennr. Der insert-Aufruf liefert dann ein
	     * Objekt, dessen Nummer mit der Datenbank konsistent ist.
	     */
//	    b.setId(10);

	    // Objekt in der DB speichern.
	    return this.userMapper.insert(u);
	  }
	  /*
	   * ***************************************************************************
	   * ABSCHNITT, Ende: Methoden f�r User-Objekte
	   * ***************************************************************************
	   */
	  
	  /*
	   * ***************************************************************************
	   * ABSCHNITT, Methoden f�r Baugruppen-Objekte
	   * ***************************************************************************
	   */
	  
	  /**
	   * <p>
	   * Anlegen einer neuen Baugruppe . Dies f�hrt implizit zu einem Speichern des
	   * neuen Stuecklistes in der Datenbank.
	   * @see createStueckliste(String name)
	   */
	  @Override
	public Baugruppe createBaugruppe(String name, Vector<ElementPaar> BauteilPaare, 
			Vector<ElementPaar> BaugruppenPaare ) throws IllegalArgumentException {
			 
		  //TODO dynamisch anpassen
	        User editUser = new User();
	        editUser.setName("statischer User");
	        editUser.setId(1);
	        editUser.setGoogleID("000000000000");

	     // Erstellungsdatum wird generiert und dem Objekt angeh�ng
		Date date = new Date();
		
		Stueckliste s = new Stueckliste();
		s.setEditUser(editUser);
		s.setName(name+"_sl");
	    s.setBauteilPaare(BauteilPaare);
	    s.setBaugruppenPaare(BaugruppenPaare);
	    s.setEditDate(date);
	    s = this.stuecklisteMapper.insert(s);
	
	   
	    Baugruppe b = new Baugruppe();
	    b.setEditDate(date);
	    b.setEditUser(editUser);
	    b.setName(name);
	    b.setStueckliste(s);
	      
	    // Objekt in der DB speichern.
	    return this.baugruppenMapper.insert(b);
	  }
	 
	  
	  /**
	   * L�schen einer Baugruppe 
	   */
	  @Override
	public void deleteBaugruppe(Baugruppe b) throws IllegalArgumentException {
	 
		  Vector<Stueckliste> vStueckliste = this.stuecklisteMapper.findByBaugruppe(b);
		  Vector<Enderzeugnis> vEnderzeugnis = this.enderzeugnisMapper.findByBaugruppe(b);
			
		  //Abgleich mit St�cklisten
		  if(vStueckliste.isEmpty()==true && vEnderzeugnis.isEmpty()==true){
			 
			  this.baugruppenMapper.delete(b);
			  this.stuecklisteMapper.delete(b.getStueckliste());
			  
		  }
		  
		  else {
			  if(vStueckliste.isEmpty()==false){
				  //TODO Exception schreiben
				  System.out.println("Baugruppe wird in St�ckliste verwendet.");
			  }
			  
			  if(vEnderzeugnis.isEmpty()==false){
				  //TODO Exception schreiben
				  System.out.println("Baugruppe wird in Enderzeugnis verwendet: ");
			  }
			  
		  }
			  
		  }
	  /**
	   * Speichern eines Baugruppe.
	   */
	  @Override
	public void saveBaugruppe(Baugruppe b) throws IllegalArgumentException {
		
		// Aenderungsdatum wird generiert und dem Objekt angeh�ngt
		    Date date = new Date();
		    b.setEditDate(date);
		  
		  //TODO dynamisch anpassen
	        User editUser = new User();
	        editUser.setName("statischer User");
	        editUser.setId(1);
	        editUser.setGoogleID("000000000000");
	        b.setEditUser(editUser);
	        
	       Stueckliste s = b.getStueckliste();
	       s.setEditDate(date);
	       s.setEditUser(editUser);
	       
	       this.stuecklisteMapper.update(s);
		  
	        this.baugruppenMapper.update(b);
	  }
	  /**
	   * Auslesen aller Baugruppen.
	   */
	  @Override
	public Vector<Baugruppe> getAllBaugruppen() throws IllegalArgumentException {
	    return this.baugruppenMapper.findAll();
	  }  
	  
	  /**
	   * Auslesen einer Baugruppe anhand seiner Id.
	   */
	  @Override
	public Baugruppe getBaugruppeById(int id) throws IllegalArgumentException {
	    return this.baugruppenMapper.findByID(id);
	  }
	  /*
	   * ***************************************************************************
	   * ABSCHNITT Ende : Methoden f�r Baugruppen-Objekte
	   * ***************************************************************************
	   */
	  
	  /*
	   * ***************************************************************************
	   * ABSCHNITT: Methoden f�r Enderzeugnis-Objekte
	   * ***************************************************************************
	   */
	  
	  /**
	   * <p>
	   * Anlegen einer neuen Baugruppe . Dies f�hrt implizit zu einem Speichern des
	   * neuen Stuecklistes in der Datenbank.
	   * @see createStueckliste(String name)
	   */
	  @Override
	public Enderzeugnis createEnderzeugnis(String name, Baugruppe baugruppe ) throws IllegalArgumentException {
		
		     
		  //TODO dynamisch anpassen
	        User editUser = new User();
	        editUser.setName("statischer User");
	        editUser.setId(1);
	        editUser.setGoogleID("000000000000");

	     // Erstellungsdatum wird generiert und dem Objekt angeh�ng
		Date date = new Date();
	    
	    Enderzeugnis e = new Enderzeugnis();
	    e.setEditDate(date);
	    e.setName(name);
	    e.setBaugruppe(baugruppe);
	    e.setEditUser(editUser);
	      
	    // Objekt in der DB speichern.
	    return this.enderzeugnisMapper.insert(e);
	  }
	 
	  
	  /**
	   * L�schen einer Baugruppe 
	   */
	  @Override
	public void deleteEnderzeugnis(Enderzeugnis e) throws IllegalArgumentException {
	 
	    this.enderzeugnisMapper.delete(e);
	  }
	  /**
	   * Speichern eines Enderzeugnisses.
	   */
	  @Override
	public void saveEnderzeugnis(Enderzeugnis e) throws IllegalArgumentException {
		
		// Aenderungsdatum wird generiert und dem Objekt angeh�ngt
		    Date date = new Date();
		    e.setEditDate(date);
		  
		  //TODO dynamisch anpassen
	        User editUser = new User();
	        editUser.setName("statischer User");
	        editUser.setId(1);
	        editUser.setGoogleID("000000000000");
	        e.setEditUser(editUser);
		  
	        this.enderzeugnisMapper.update(e);
	  }
	  /**
	   * Auslesen aller Baugruppen.
	   */
	  @Override
	public Vector<Enderzeugnis> getAllEnderzeugnis() throws IllegalArgumentException {
	    return this.enderzeugnisMapper.findAll();
	  }
	  
	  /*
	   * ***************************************************************************
	   * ABSCHNITT Ende : Methoden f�r Enderzeugnis-Objekte
	   * ***************************************************************************
	   */
}