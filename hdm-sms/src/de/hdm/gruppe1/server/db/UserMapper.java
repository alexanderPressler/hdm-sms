package de.hdm.gruppe1.server.db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Vector;

import de.hdm.gruppe1.shared.bo.Baugruppe;
import de.hdm.gruppe1.shared.bo.Bauteil;
import de.hdm.gruppe1.shared.bo.User;

/**
 * Mapper-Klasse, die <code>bauteil</code>-Objekte auf eine relationale
 * Datenbank abbildet. Hierzu wird eine Reihe von Methoden zur Verf�gung
 * gestellt, mit deren Hilfe z.B. Objekte gesucht, erzeugt, modifiziert und
 * gel�scht werden k�nnen. Das Mapping ist bidirektional. D.h., Objekte k�nnen
 * in DB-Strukturen und DB-Strukturen in Objekte umgewandelt werden.
 * 
 * @see CustomerMapper, TransactionMapper
 * @author Thies
 */
public class UserMapper {

	/**
	 * Die Klasse bauteilMapper wird nur einmal instantiiert. Man spricht
	 * hierbei von einem sogenannten <b>Singleton</b>.
	 * <p>
	 * Diese Variable ist durch den Bezeichner <code>static</code> nur einmal
	 * f�r s�mtliche eventuellen Instanzen dieser Klasse vorhanden. Sie
	 * speichert die einzige Instanz dieser Klasse.
	 * 
	 * @see UserMapper()
	 */
	private static UserMapper userMapper = null;

	/**
	 * Gesch�tzter Konstruktor - verhindert die M�glichkeit, mit
	 * <code>new</code> neue Instanzen dieser Klasse zu erzeugen.
	 */
	protected UserMapper() {
	}
	
	 /**
	   * Diese statische Methode kann aufgrufen werden durch
	   * <code>UserMapper.bauteilMapper()</code>. Sie stellt die
	   * Singleton-Eigenschaft sicher, indem Sie daf�r sorgt, dass nur eine einzige
	   * Instanz von <code>BauteilMapper</code> existiert.
	   * <p>
	   * 
	   * <b>Fazit:</b> BauteilMapper sollte nicht mittels <code>new</code>
	   * instantiiert werden, sondern stets durch Aufruf dieser statischen Methode.
	   * 
	   * @return DAS <code>BauteilMapper</code>-Objekt.
	   * @see bauteilMapper
	   */
	  public static UserMapper userMapper() {
	    if (userMapper == null) {
	    	userMapper = new UserMapper();
	    }

	    return userMapper;
	  }
	  
	  /**
	   * Einf�gen eines <code>Bauteil</code>-Objekts in die Datenbank. Dabei wird
	   * auch der Prim�rschl�ssel des �bergebenen Objekts gepr�ft und ggf.
	   * berichtigt.
	   * 
	   * @param a das zu speichernde Objekt
	   * @return das bereits �bergebene Objekt, jedoch mit ggf. korrigierter
	   *         <code>id</code>.
	   */
	  public User insert(User user) {
	    Connection con = DBConnection.connection();

	    try {
	      Statement stmt = con.createStatement();

	      /*
	       * Zun�chst schauen wir nach, welches der momentan h�chste
	       * Prim�rschl�sselwert ist.
	       */
	      ResultSet rs = stmt.executeQuery("SELECT MAX(userID) AS maxid "
	          + "FROM User ");

	      // Wenn wir etwas zur�ckerhalten, kann dies nur einzeilig sein
	      if (rs.next()) {
	        /*
	         * a erh�lt den bisher maximalen, nun um 1 inkrementierten
	         * Prim�rschl�ssel.
	         */
	    	  user.setId(rs.getInt("maxid") + 1);

	        stmt = con.createStatement();

	        stmt.executeUpdate("INSERT INTO User VALUES ('"+ user.getId() +"','"+ user.getGoogleID() +"'" +"','"+ user.getName() +"'");
	      
	      }
	    }
	    catch (SQLException e2) {
	      e2.printStackTrace();
	    }

	    /*
	     * R�ckgabe, des evtl. korrigierten Bauteil.
	     * 
	     * HINWEIS: Da in Java nur Referenzen auf Objekte und keine physischen
	     * Objekte �bergeben werden, w�re die Anpassung des bauteil-Objekts auch
	     * ohne diese explizite R�ckgabe au�erhalb dieser Methode sichtbar. Die
	     * explizite R�ckgabe von a ist eher ein Stilmittel, um zu signalisieren,
	     * dass sich das Objekt evtl. im Laufe der Methode ver�ndert hat.
	     */
	    return user;
	  }	
	  
	  public User findByID (int id){
			Connection con = DBConnection.connection();
			User user = null;
			try{
				Statement stmt = con.createStatement();
			    
			    ResultSet rs = stmt.executeQuery("SELECT * FROM User WHERE userID ='"
			    		+id+"';");
			    //Da es nur eine Baugruppe mit dieser ID geben kann ist davon auszugehen, dass das ResultSet nur eine Zeile enth�lt
			    if(rs.next()){
			    	user = new User();
			    	user.setId(rs.getInt("userID"));
			    	user.setName(rs.getString("eMail"));
			    	user.setGoogleID(rs.getString("googleID"));
			    	
			    }
			}
			catch(SQLException e){
				e.printStackTrace();
			}
			return user;
		}
}
