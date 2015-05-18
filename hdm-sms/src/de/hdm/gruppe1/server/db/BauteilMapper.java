package de.hdm.gruppe1.server.db;

import java.sql.*;
import java.util.ArrayList;

import de.hdm.gruppe1.shared.bo.*;




/**
 * @author Andreas Herrmann
 */
public class BauteilMapper {

	/**
	 * Die Klasse bauteilMapper wird nur einmal instantiiert. Man spricht
	 * hierbei von einem sogenannten <b>Singleton</b>.
	 * <p>
	 * Diese Variable ist durch den Bezeichner <code>static</code> nur einmal
	 * fÃ¼r sÃ¤mtliche eventuellen Instanzen dieser Klasse vorhanden. Sie
	 * speichert die einzige Instanz dieser Klasse.
	 * 
	 * @see bauteilMapper()
	 */
	private static BauteilMapper bauteilMapper = null;

	/**
	 * GeschÃ¼tzter Konstruktor - verhindert die MÃ¶glichkeit, mit
	 * <code>new</code> neue Instanzen dieser Klasse zu erzeugen.
	 */
	protected BauteilMapper() {
	}
	
	 /**
	   * Diese statische Methode kann aufgrufen werden durch
	   * <code>BauteilMapper.bauteilMapper()</code>. Sie stellt die
	   * Singleton-Eigenschaft sicher, indem Sie dafÃ¼r sorgt, dass nur eine einzige
	   * Instanz von <code>BauteilMapper</code> existiert.
	   * <p>
	   * 
	   * <b>Fazit:</b> BauteilMapper sollte nicht mittels <code>new</code>
	   * instantiiert werden, sondern stets durch Aufruf dieser statischen Methode.
	   * 
	   * @return DAS <code>BauteilMapper</code>-Objekt.
	   * @see bauteilMapper
	   */
	  public static BauteilMapper bauteilMapper() {
	    if (bauteilMapper == null) {
	    	bauteilMapper = new BauteilMapper();
	    }

	    return bauteilMapper;
	  }
	  
	  /**
	   * EinfÃ¼gen eines <code>Bauteil</code>-Objekts in die Datenbank. Dabei wird
	   * auch der PrimÃ¤rschlÃ¼ssel des Ã¼bergebenen Objekts geprÃ¼ft und ggf.
	   * berichtigt.
	   * 
	   * @param a das zu speichernde Objekt
	   * @return das bereits Ã¼bergebene Objekt, jedoch mit ggf. korrigierter
	   *         <code>id</code>.
	   */
	  public Bauteil insert(Bauteil bauteil) {
	    Connection con = DBConnection.connection();

	    try {
	      Statement stmt = con.createStatement();
	      //Da ich ein int nicht einfach durch casting in einen String wandeln kann, muss dies über eine Instanz der Klasse Integer geschehen
	      Integer aendererID = new Integer(bauteil.getAenderer().getId());
	      //Der Datumstring von AenderungsDatum muss um die Nanosekunden gekürzt werden, da die Datenbank diese nicht aufnehmen kann
	      ResultSet rs = stmt.executeQuery("INSERT INTO 'Bauteile'('material','bearbeitet_Von','name','beschreibung','datum') VALUES('"
	      +bauteil.getMaterialBeschreibung()+"','"+aendererID.toString()+"','"+bauteil.getName()+"','"+bauteil.getBauteilBeschreibung()+"','"
	    		  +bauteil.getAenderungsDatum().toString().substring(0,19)+"');");

	      // Zurückerhalten werden wir den von der Datenbank erstellten Primärschlüssel
	      if (rs.next()) {
	        
	    	  bauteil.setId(rs.getInt("teilnummer"));

	      }
	    }
	    catch (SQLException e) {
	      e.printStackTrace();
	    }
	    //Rückgabe des Bauteils mit der ID
	    return bauteil;
	  }
	  
	  /**
	   * Wiederholtes Schreiben eines Objekts in die Datenbank.
	   * 
	   * @param a das Objekt, das in die DB geschrieben werden soll
	   * @return das als Parameter Ã¼bergebene Objekt
	   */
	  public Bauteil update(Bauteil bauteil) {
	    Connection con = DBConnection.connection();

	    try {
	      Statement stmt = con.createStatement();
	    //Da ich ein int nicht einfach durch casting in einen String wandeln kann, muss dies über eine Instanz der Klasse Integer geschehen
	      Integer aendererID = new Integer(bauteil.getAenderer().getId());
	      stmt.executeUpdate("UPDATE `Bauteile` SET `name`='"+ bauteil.getName() +"',`beschreibung`='"+ bauteil.getBauteilBeschreibung() +"',`material`='"
	      + bauteil.getMaterialBeschreibung() + "','bearbeitet_Von'='" + aendererID.toString() + "','datum'='"
	    		  + bauteil.getAenderungsDatum().toString().substring(0,19) + "' WHERE `teilnummer`= "+ bauteil.getId() +";");

	    }
	    catch (SQLException e) {
	      e.printStackTrace();
	    }
	    return bauteil;
	  }
	  
	  /**
	   * LÃ¶schen der Daten eines <code>Bauteil</code>-Objekts aus der Datenbank.
	   * 
	   * @param a das aus der DB zu lÃ¶schende "Objekt"
	   */
	  public boolean delete(Bauteil bauteil) {
	    Connection con = DBConnection.connection();

	    try {
	      Statement stmt = con.createStatement();
	      //Da ich ein int nicht einfach durch casting in einen String wandeln kann, muss dies über eine Instanz der Klasse Integer geschehen
	      Integer bauteilID = new Integer(bauteil.getId());
	      
	      if(stmt.executeUpdate("DELETE FROM `Bauteile` WHERE `teilnummer`="+ bauteilID.toString()+"';")==0){
	    	  return false;
	      }
	      else{
	    	  return true;
	      }

	    }
	    catch (SQLException e2) {
	      e2.printStackTrace();
	      return false;
	    }
	  }
	  
	  public Bauteil findByID(int id){
		  Connection con = DBConnection.connection();
		  Bauteil bauteil = null;
		  try{
			  Statement stmt = con.createStatement();
			  //Da ich ein int nicht einfach durch casting in einen String wandeln kann, muss dies über eine Instanz der Klasse Integer geschehen
		      Integer bauteilID = new Integer(id);
			  ResultSet rs = stmt.executeQuery("SELECT * FROM 'Bauteile' JOIN 'USER' ON 'Bauteile.teilnummer'='User.userID' WHERE 'Bauteile.teilnummer'='"
					  +bauteilID.toString()+"';");
			  //Da es nur ein Bauteil mit dieser ID geben kann können wir davon ausgehen, dass wir nur eine Zeile zurück bekommen
			  if(rs.next()){
				  bauteil = new Bauteil();
				  bauteil.setId(rs.getInt("Bauteile.teilnummer"));
				  bauteil.setName(rs.getString("Bauteile.name"));
				  bauteil.setMaterialBeschreibung(rs.getString("Bauteile.material"));
				  bauteil.setBauteilBeschreibung(rs.getString("Bauteile.beschreibung"));
				  //User Objekt erzeugen, um es in auteil einzufügen
				  User user = new User();
				  user.setId(rs.getInt("User.userID"));
				  user.setEmail(rs.getString("User.eMail"));
				  bauteil.setAenderer(user);
				  //Timestamp Objekt aus Datumsstring erzeugen, um es in bauteil einzufügen
				  Timestamp timestamp = Timestamp.valueOf(rs.getString("Bauteile.datum"));
				  bauteil.setAenderungsDatum(timestamp);
			  }
		  }
		  catch (SQLException e){
			  e.printStackTrace();
		  }
		  return bauteil;
	  }
	  
	  public Vector<Bauteil> findByName(String name){
		  Vector<Bauteil> vBauteil = null;
		  Connection con = DBConnection.connection();
		  try{
			  Statement stmt = con.createStatement();
			  ResultSet rs = stmt.executeQuery("SELECT * FROM 'Bauteile' JOIN 'User' ON 'Bauteile.bearbeitet_Von'='User.UserID' WHERE 'Bauteile.name' LIKE '%"
			  +name+"%';");
			  vBauteil = new Vector<Bauteil>();
			  //Da es viele Bauteile geben kann, die diesen Namen haben müssen wir eine Schleife benutzen
			  while(rs.next()){
				  Bauteil bauteil = new Bauteil();
				  bauteil.setId(rs.getInt("Bauteile.teilnummer"));
				  bauteil.setName(rs.getString("Bauteile.name"));
				  bauteil.setMaterialBeschreibung(rs.getString("Bauteile.material"));
				  bauteil.setBauteilBeschreibung(rs.getString("Bauteile.beschreibung"));
				  //User Objekt erzeugen, um es in auteil einzufügen
				  User user = new User();
				  user.setId(rs.getInt("User.userID"));
				  user.setEmail(rs.getString("User.eMail"));
				  bauteil.setAenderer(user);
				  //Timestamp Objekt aus Datumsstring erzeugen, um es in bauteil einzufügen
				  Timestamp timestamp = Timestamp.valueOf(rs.getString("Bauteile.datum"));
				  bauteil.setAenderungsDatum(timestamp);
				  //bauteil der ArrayList hinzufügen
				  vBauteil.addElement(bauteil);
				  }
		  }
		  catch(SQLException e){
			  e.printStackTrace();
			  return vBauteil;
		  }
		  return vBauteil;
	  }
	  
	  /**
	   * Auslesen aller Kunden.
	   * 
	   * @return Ein Vektor mit Customer-Objekten, die sÃ¤mtliche Kunden
	   *         reprÃ¤sentieren. Bei evtl. Exceptions wird ein partiell gefï¿½llter
	   *         oder ggf. auch leerer Vetor zurÃ¼ckgeliefert.
	   */
	  public Vector<Bauteil> getAll() {
		  Vector<Bauteil> vBauteil = null;
		  Connection con = DBConnection.connection();
		  try{
			  Statement stmt = con.createStatement();
			  ResultSet rs = stmt.executeQuery("SELECT * FROM 'Bauteile' JOIN 'User' ON 'Bauteile.teilnummer'='User.UserID';");
			  vBauteil = new Vector<Bauteil>();
			  //Da es viele Bauteile in der Datenbank geben kann setzen wir eine Schleife ein
			  while(rs.next()){
				  Bauteil bauteil = new Bauteil();
				  bauteil.setId(rs.getInt("Bauteile.teilnummer"));
				  bauteil.setName(rs.getString("Bauteile.name"));
				  bauteil.setMaterialBeschreibung(rs.getString("Bauteile.material"));
				  bauteil.setBauteilBeschreibung(rs.getString("Bauteile.beschreibung"));
				  //User Objekt erzeugen, um es in auteil einzufügen
				  User user = new User();
				  user.setId(rs.getInt("User.userID"));
				  user.setEmail(rs.getString("User.eMail"));
				  bauteil.setAenderer(user);
				  //Timestamp Objekt aus Datumsstring erzeugen, um es in bauteil einzufügen
				  Timestamp timestamp = Timestamp.valueOf(rs.getString("Bauteile.datum"));
				  bauteil.setAenderungsDatum(timestamp);
				  //bauteil der ArrayList hinzufügen
				  vBauteil.addElement(bauteil);
				  }
		  }
		  catch(SQLException e){
			  e.printStackTrace();
		  }
		  return vBauteil;
	  }

}
