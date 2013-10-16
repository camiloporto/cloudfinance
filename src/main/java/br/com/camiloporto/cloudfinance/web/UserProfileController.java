package br.com.camiloporto.cloudfinance.web;

import java.util.List;

import javax.servlet.http.HttpSession;
import javax.validation.ConstraintViolationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

import br.com.camiloporto.cloudfinance.model.AccountSystem;
import br.com.camiloporto.cloudfinance.model.Profile;
import br.com.camiloporto.cloudfinance.service.AccountManager;
import br.com.camiloporto.cloudfinance.service.UserProfileManager;

@RequestMapping("/user")
@Controller
@SessionAttributes(value={"logged", "activeAccountSystem"})
public class UserProfileController {
	
	@Autowired
	private UserProfileManager userProfileManager;
	
	@Autowired
	private AccountManager accountManager;
	
	@RequestMapping(value = "/signup", method = RequestMethod.POST ,produces = MediaTypeApplicationJsonUTF8.APPLICATION_JSON_UTF8_VALUE )
	public @ResponseBody AbstractOperationResponse signUp(String userName, String pass, String confirmPass) {
		Profile newProfile = new Profile();
		newProfile.setPass(pass);
		newProfile.setUserId(userName);
		Profile saved;
		UserOperationResponse response = new UserOperationResponse(true);
		try {
			saved = userProfileManager.signUp(newProfile);
			response.setUserId(saved.getUserId());
		} catch (ConstraintViolationException e) {
			response = new UserOperationResponse(e);
			e.printStackTrace();
		} catch (Throwable e) {
			response.setSuccess(false);
			e.printStackTrace();
		}
		
		return response;
	}
	
	@RequestMapping(value = "/login", method = RequestMethod.POST ,produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody AbstractOperationResponse login(String userName, String pass, ModelMap map) {
		Profile logged = userProfileManager.login(userName, pass);
		if(logged != null) {
			map.addAttribute("logged", logged);
			setDefaultRootAccount(logged, map);
			return new UserOperationResponse(true);
		}
		return new UserOperationResponse(false);
	}
	
	@RequestMapping(value = "/logoff", method = RequestMethod.POST ,produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody UserOperationResponse logoff(HttpSession session, SessionStatus status) {
		try {
			status.setComplete();
		} catch (IllegalStateException e) {
			e.printStackTrace();
			//OK. session already invalidated
		}
		return new UserOperationResponse(true);
	}

	private void setDefaultRootAccount(Profile logged, ModelMap map) {
		List<AccountSystem> accountSystems = accountManager.findAccountSystems(logged);
		map.addAttribute("activeAccountSystem", accountSystems.get(0));
	}
}
