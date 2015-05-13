package de.hdm.gruppe1.server.db;

import java.sql.*;
import java.util.ArrayList;

import com.google.storage.onestore.v3.OnestoreEntity.User;

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
	 * für sämtliche eventuellen Instanzen dieser Klasse vorhanden. Sie
	 * speichert die einzige Instanz dieser Klasse.
	 * 
	 * @see bauteilMapper()
	 */
	private static BauteilMapper bauteilMapper = null;

	/**
	 * Geschützter Konstruktor - verhindert die Möglichkeit, mit
	 * <code>new</code> neue Instanzen dieser Klasse zu erzeugen.
	 */
	protected BauteilMapper() {
	}
	
	 /**
	   * Diese statische Methode kann aufgrufen werden durch
	   * <code>BauteilMapper.bauteilMapper()</code>. Sie stellt die
	   * Singleton-Eigenschaft sicher, indem Sie dafür sorgt, dass nur eine einzige
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
	   * Einfügen eines <code>Bauteil</code>-Objekts in die Datenbank. Dabei wird
	   * auch der Primärschlüssel des übergebenen Objekts geprüft und ggf.
	   * berichtigt.
	   * 
	   * @param a das zu speichernde Objekt
	   * @return das bereits übergebene Objekt, jedoch mit ggf. korrigierter
	   *         <code>id</code>.
	   */
	  public Bauteil insert(Bauteil bauteil) {
	    Connection con = DBConnection.connection();

	    try {
	      Statement stmt = con.createStatement();
	      //Der Datumstring von AenderungsDatum muss um die Nanosekunden gek�rzt werden, da die Datenbank diese nicht aufnehmen kann
	      ResultSet rs = stmt.executeQuery("INSERT INTO 'Bauteile'('material','bearbeitet_Von','name','beschreibung','datum') VALUES('"
	      +bauteil.getMaterialBeschreibung()+"','"+bauteil.getAenderer.getId.toString()+"','"+bauteil.getName()+"','"+bauteil.getBauteilBeschreibung()+"','"
	    		  +bauteil.getAenderungsDatum().toString().substring(0,19)+"');");

	      // Zur�ckerhalten werden wir den von der Datenbank erstellten Prim�rschl�ssel
	      if (rs.next()) {
	        
	    	  bauteil.setId(rs.getInt("teilnummer"));

	      }
	    }
	    catch (SQLException e) {
	      e.printStackTrace();
	    }
	    //R�ckgabe des Bauteils mit der ID
	    return bauteil;
	  }
	  
	  /**
	   * Wiederholtes Schreiben eines Objekts in die Datenbank.
	   * 
	   * @param a das Objekt, das in die DB geschrieben werden soll
	   * @return das als Parameter übergebene Objekt
	   */
	  public Bauteil update(Bauteil bauteil) {
	    Connection con = DBConnection.connection();

	    try {
	      Statement stmt = con.createStatement();

//	      stmt.executeUpdate("UPDATE bauteile " + "SET name=\"" + a.getName()
//	          + "\" " + "WHERE id=" + a.getId());
	      
	      stmt.executeUpdate("UPDATE `Bauteile` SET `name`='"+ bauteil.getName() +"',`beschreibung`='"+ bauteil.getBauteilBeschreibung() +"',`material`='"
	      + bauteil.getMaterialBeschreibung() + "','bearbeitet_Von'='" + bauteil.getAenderer.getId.toString() + "','datum'='"
	    		  + bauteil.getAenderungsDatum().toString().substring(0,19) + "' WHERE `teilnummer`= "+ bauteil.getId() +";");

	    }
	    catch (SQLException e) {
	      e.printStackTrace();
	    }
	    return bauteil;
	  }
	  
	  /**
	   * Löschen der Daten eines <code>Bauteil</code>-Objekts aus der Datenbank.
	   * 
	   * @param a das aus der DB zu löschende "Objekt"
	   */
	  public boolean delete(Bauteil bauteil) {
	    Connection con = DBConnection.connection();

	    try {
	      Statement stmt = con.createStatement();
	      
	      if(stmt.executeUpdate("DELETE FROM `bauteile` WHERE `id`="+ bauteil.getId())==0){
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
			  ResultSet rs = stmt.executeQuery("SELECT * FROM 'Bauteile' JOIN 'USER' ON 'Bauteile.teilnummer'='User.userID' WHERE 'Bauteile.teilnummer'='"+id+"';");
			  //Da es nur ein Bauteil mit dieser ID geben kann k�nnen wir davon ausgehen, dass wir nur eine Zeile zur�ck bekommen
			  if(rs.next()){
				  bauteil = new Bauteil();
				  bauteil.setId(rs.getInt("Bauteile.teilnummer"));
				  bauteil.setName(rs.getString("Bauteile.name"));
				  bauteil.setMaterialBeschreibung(rs.getString("Bauteile.material"));
				  bauteil.setBauteilBeschreibung(rs.getString("Bauteile.beschreibung"));
				  //User Objekt erzeugen, um es in auteil einzuf�gen
				  User user = new User();
				  user.setId(rs.getInt("User.userID"));
				  user.setEmail(rs.getString("User.eMail"));
				  bauteil.setAenderer(user);
				  //Timestamp Objekt aus Datumsstring erzeugen, um es in bauteil einzuf�gen
				  Timestamp timestamp = Timestamp.valueOf(rs.getString("Bauteile.datum"));
				  bauteil.setAenderungsDatum(timestamp);
			  }
		  }
		  catch (SQLException e){
			  e.printStackTrace();
		  }
		  return bauteil;
	  }
	  
	  public ArrayList<Bauteil> findByName(String name){
		  ArrayList<Bauteil> alBauteil = null;
		  Connection con = DBConnection.connection();
		  try{
			  Statement stmt = con.createStatement();
			  ResultSet rs = stmt.executeQuery("SELECT * FROM 'Bauteile' JOIN 'User' ON 'Bauteile.teilnummer'='User.UserID' WHERE 'Bauteile.name' LIKE '%"
			  +name+"%';");
			  alBauteil = new ArrayList<Bauteil>();
			  //Da es viele Bauteile geben kann, die diesen Namen haben m�ssen wir eine Schleife benutzen
			  while(rs.next()){
				  Bauteil bauteil = new Bauteil();
				  bauteil.setId(rs.getInt("Bauteile.teilnummer"));
				  bauteil.setName(rs.getString("Bauteile.name"));
				  bauteil.setMaterialBeschreibung(rs.getString("Bauteile.material"));
				  bauteil.setBauteilBeschreibung(rs.getString("Bauteile.beschreibung"));
				  //User Objekt erzeugen, um es in auteil einzuf�gen
				  User user = new User();
				  user.setId(rs.getInt("User.userID"));
				  user.setEmail(rs.getString("User.eMail"));
				  bauteil.setAenderer(user);
				  //Timestamp Objekt aus Datumsstring erzeugen, um es in bauteil einzuf�gen
				  Timestamp timestamp = Timestamp.valueOf(rs.getString("Bauteile.datum"));
				  bauteil.setAenderungsDatum(timestamp);
				  //bauteil der ArrayList hinzuf�gen
				  alBauteil.add(bauteil);
				  }
		  }
		  catch(SQLException e){
			  e.printStackTrace();
			  return alBauteil;
		  }
		  return alBauteil;
	  }
	  
	  /**
	   * Auslesen aller Kunden.
	   * 
	   * @return Ein Vektor mit Customer-Objekten, die sämtliche Kunden
	   *         repräsentieren. Bei evtl. Exceptions wird ein partiell gef�llter
	   *         oder ggf. auch leerer Vetor zurückgeliefert.
	   */
	  public ArrayList<Bauteil> getAll() {
		  ArrayList<Bauteil> alBauteil = null;
		  Connection con = DBConnection.connection();
		  try{
			  Statement stmt = con.createStatement();
			  ResultSet rs = stmt.executeQuery("SELECT * FROM 'Bauteile' JOIN 'User' ON 'Bauteile.teilnummer'='User.UserID';");
			  alBauteil = new ArrayList<Bauteil>();
			  //Da es viele Bauteile in der Datenbank geben kann setzen wir eine Schleife ein
			  while(rs.next()){
				  Bauteil bauteil = new Bauteil();
				  bauteil.setId(rs.getInt("Bauteile.teilnummer"));
				  bauteil.setName(rs.getString("Bauteile.name"));
				  bauteil.setMaterialBeschreibung(rs.getString("Bauteile.material"));
				  bauteil.setBauteilBeschreibung(rs.getString("Bauteile.beschreibung"));
				  //User Objekt erzeugen, um es in auteil einzuf�gen
				  User user = new User();
				  user.setId(rs.getInt("User.userID"));
				  user.setEmail(rs.getString("User.eMail"));
				  bauteil.setAenderer(user);
				  //Timestamp Objekt aus Datumsstring erzeugen, um es in bauteil einzuf�gen
				  Timestamp timestamp = Timestamp.valueOf(rs.getString("Bauteile.datum"));
				  bauteil.setAenderungsDatum(timestamp);
				  //bauteil der ArrayList hinzuf�gen
				  alBauteil.add(bauteil);
				  }
		  }
		  catch(SQLException e){
			  e.printStackTrace();
			  return alBauteil;
		  }
		  return alBauteil;
	  }

}
