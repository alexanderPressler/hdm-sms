package de.hdm.gruppe1.server;

import de.hdm.gruppe1.server.db.UserMapper;
import de.hdm.gruppe1.shared.LoginInfo;
import de.hdm.gruppe1.shared.LoginService;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
/**
 * Implementierung des <code>LoginService</code>-Interface. Die technische
 * Realisierung bzgl. <code>RemoteServiceServlet</code> bzw. GWT RPC erfolgt
 * analog zu {@SmsImpl}. Für Details zu GWT RPC siehe dort.
 * 
 * @see LoginService
 * @author Thies, Schmidt & Pressler
 */
public class LoginServiceImpl extends RemoteServiceServlet implements
    LoginService {

	private static final long serialVersionUID = 1L;
	/**
	* Nutzerdaten mit Hilfe des Google User Service abgereifen
	* 
	* @see #getUserInfo(String requestUri)
	*/
public LoginInfo getUserInfo(String requestUri) {
    UserService userService = UserServiceFactory.getUserService();
    User user = userService.getCurrentUser();
    LoginInfo loginInfo = new LoginInfo();

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
