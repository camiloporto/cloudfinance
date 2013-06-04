package br.com.camiloporto.cloudfinance.web;

import javax.validation.ConstraintViolationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;

import br.com.camiloporto.cloudfinance.model.Profile;
import br.com.camiloporto.cloudfinance.service.UserProfileManager;

@RequestMapping("/user")
@Controller
@SessionAttributes("logged")
public class UserProfileController {
	
	@Autowired
	private UserProfileManager userProfileManager;
	
	@RequestMapping(value = "/signup", method = RequestMethod.POST ,produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody AbstractOperationResponse signUp(String userName, String pass, String confirmPass) {
		Profile newProfile = new Profile();
		newProfile.setPass(pass);
		newProfile.setUserId(userName);
		
		Profile saved;
		UserOperationResponse response = new UserOperationResponse(true);
		try {
			saved = userProfileManager.signUp(newProfile);
			response.setUserId(saved.getId());
		} catch (ConstraintViolationException e) {
			response = new UserOperationResponse(e);
			
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
			return new UserOperationResponse(true);
		}
		return new UserOperationResponse(false);
	}
}
