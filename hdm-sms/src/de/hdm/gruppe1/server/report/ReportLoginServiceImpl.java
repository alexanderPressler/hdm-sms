package de.hdm.gruppe1.server.report;

import de.hdm.gruppe1.server.db.UserMapper;
import de.hdm.gruppe1.shared.report.LoginInfo;
import de.hdm.gruppe1.shared.report.LoginService;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * Implementierung des <code>ReportGenerator-Login Service</code>-Interface. Die technische
 * Realisierung bzgl. <code>RemoteServiceServlet</code> bzw. GWT RPC erfolgt
 * analog zu {@ReportLoginService}. Für Details zu GWT RPC siehe dort.
 * 
 * @see ReportLoginService
 * @author Thies, Schmidt & Pressler 
 */
public class ReportLoginServiceImpl extends RemoteServiceServlet implements
    LoginService {

	private static final long serialVersionUID = 1L;

	/**
	   * Nutzerdaten mit Hilfe des Google User Service abgereifen
	   * @see #getUserInfo(String requestUri)
	   */
public LoginInfo getUserInfo(String requestUri) {
    //aktuellen User ziehen und Login Inho erstellen
	UserService userService = UserServiceFactory.getUserService();
    User user = userService.getCurrentUser();
    LoginInfo loginInfo = new LoginInfo();
    
    //wenn nutzer nicht vorhanden neu anlegen
    if (user != null) {
    	de.hdm.gruppe1.shared.bo.User currentUser = new de.hdm.gruppe1.shared.bo.User();
        UserMapper uMapper = UserMapper.userMapper();
        currentUser = uMapper.findByGoogleID(user.getUserId());
        if(currentUser==null){
      	  currentUser = new de.hdm.gruppe1.shared.bo.User();
      	  currentUser.setGoogleID(user.getUserId());
      	  currentUser.setName(user.getEmail());
      	  currentUser=uMapper.insert(currentUser);
        }
      //Ansonsten eingeloggten Nutzer setzen 
      loginInfo.setLoggedIn(true);
      loginInfo.setEmailAddress(user.getEmail());
      loginInfo.setLogoutUrl(userService.createLogoutURL(requestUri));
      loginInfo.setUser(currentUser);
      
      
    } else {
      loginInfo.setLoggedIn(false);
      loginInfo.setLoginUrl(userService.createLoginURL(requestUri));
    }
    return loginInfo;
  }

}