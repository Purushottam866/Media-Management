package com.example.demoFB;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.connect.UsersConnectionRepository;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.social.facebook.api.User;
import org.springframework.social.facebook.connect.FacebookConnectionFactory;
import org.springframework.social.oauth1.OAuth1Parameters;
import org.springframework.social.oauth2.AccessGrant;
import org.springframework.social.oauth2.OAuth2Operations;
import org.springframework.social.oauth2.OAuth2Parameters;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class FacebookController {

    @GetMapping("/")
    public String loadHome()
    {
    	return "Home";
    } 
    
    private final String app_id = "279672881797984";	
	
	private final String app_secret ="6433362e993b6810f1fd880e42b15682";	
	
	
	private FacebookConnectionFactory factory = new FacebookConnectionFactory(app_id ,app_secret);
    
    @GetMapping("/facebookLogin")
    public String fbLogin()
    {
    	OAuth2Operations operations = factory.getOAuthOperations();
    	OAuth2Parameters params = new OAuth2Parameters();
    	params.setRedirectUri("http://localhost:8080/doLogin");
    	params.setScope("email,public_profile");
    	String authurl = operations.buildAuthenticateUrl(params);
    	System.out.println("Generated url is :" + authurl);
    	return "redirect:"+ authurl;
    }
    
    @GetMapping("/doLogin")
    public String doLogin(@RequestParam("code") String code,ModelMap map)
    {
    	System.out.println("code is............"+ code);
    	OAuth2Operations operations = factory.getOAuthOperations();
    	
    	//creating accessToken
    	AccessGrant accessToken = operations.exchangeForAccess(code, "http://localhost:8080/doLogin", null);
    	System.out.println("accessToken......"+ accessToken);
    	Connection<Facebook> connection = factory.createConnection(accessToken);
    	Facebook facebook = connection.getApi();
    	
    	//fetching details from facebook
    	String[] fields = {"id","name","email","birthday"};
    	User userProfile = facebook.fetchObject("me", User.class, fields);
    	System.out.println("userProfile......"+ userProfile);
    	System.out.println("email......."+ userProfile.getEmail());
    	System.out.println("name........"+ userProfile.getName());
    	map.put("name", userProfile.getName());
    	
    	//getting userdata in my application
    	return "Dashboard";
    	
    }
}
