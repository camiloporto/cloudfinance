package br.com.camiloporto.cloudfinance.web.purehtml;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.ModelAndView;

import br.com.camiloporto.cloudfinance.model.AccountSystem;
import br.com.camiloporto.cloudfinance.model.Profile;
import br.com.camiloporto.cloudfinance.service.AccountManager;
import br.com.camiloporto.cloudfinance.service.UserProfileManager;
import br.com.camiloporto.cloudfinance.web.UserOperationResponse;
import br.com.camiloporto.cloudfinance.web.UserProfileController;

@RequestMapping("/user")
@Controller
@SessionAttributes(value={"logged", "activeAccountSystem"})
public class StaticUserProfileController {
	
	@Autowired
	private UserProfileController jsonController;
	
	@Autowired
	private UserProfileManager userProfileManager;
	
	@Autowired
	private AccountManager accountManager;
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@RequestMapping(value = "/signup", method = RequestMethod.POST)
	public ModelAndView signUp(String userName, String pass, String confirmPass, ModelMap map, HttpServletRequest request) {
		UserOperationResponse response = (UserOperationResponse) jsonController.signUp(userName, pass, confirmPass);
		ModelAndView mav = new ModelAndView("mobile-newUser");
		if(response.isSuccess()) {
			try {
				authenticateUserAndSetSession(userName, pass, request);
				setDefaultRootAccount(userName, map);
				mav =  new ModelAndView("redirect:/account/roots");
			} catch (AuthenticationException e) {
				e.printStackTrace();
				return logonFailed(map);
			}
			
//			mav = login(userName, pass, map);
		}
		mav.addObject("response", response);
		return mav;
	}
	
	private void authenticateUserAndSetSession(String userName, String password, HttpServletRequest request) {
		UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
	            userName, password);
		
		// generate session if one doesn't exist
	    request.getSession();
	    
	    token.setDetails(new WebAuthenticationDetails(request));
	    Authentication authenticatedUser = authenticationManager.authenticate(token);
	    SecurityContextHolder.getContext().setAuthentication(authenticatedUser);

	}
	
	private void setDefaultRootAccount(String userName, ModelMap map) {
		Profile profile = userProfileManager.findByUsername(userName);
		map.addAttribute("logged", profile);
		List<AccountSystem> accountSystems = accountManager.findAccountSystems(profile);
		map.addAttribute("activeAccountSystem", accountSystems.get(0));
	}
	
	@RequestMapping(value = "/logonFailed", method = RequestMethod.GET)
	public ModelAndView logonFailed(ModelMap map) {
		UserOperationResponse response = new UserOperationResponse(false);
		ModelAndView mav = new ModelAndView("mobile-index");
		mav.getModelMap().put("response", response);
		return mav;
	}
	
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public ModelAndView login(String userName, String pass, ModelMap map) {
		UserOperationResponse response = (UserOperationResponse) jsonController.login(userName, pass, map);
		ModelAndView mav = new ModelAndView("mobile-index");
		if(response.isSuccess()) {
			mav = new ModelAndView("redirect:/account/roots");
			return mav;
		} else {
			mav.getModelMap().put("response", response);
		}
		return mav;
	}
	
	@RequestMapping(value = "/logoff", method = RequestMethod.POST)
	public ModelAndView logoff(HttpSession session, SessionStatus status) {
		ModelAndView mav = new ModelAndView("redirect:/mobile");
		UserOperationResponse response = jsonController.logoff(session, status);
		if(response.isSuccess()) {
			return mav;
		}
		return mav;
	}

}
