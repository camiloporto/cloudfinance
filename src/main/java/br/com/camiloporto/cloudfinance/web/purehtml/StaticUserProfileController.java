package br.com.camiloporto.cloudfinance.web.purehtml;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

import br.com.camiloporto.cloudfinance.service.AccountManager;
import br.com.camiloporto.cloudfinance.service.UserProfileManager;
import br.com.camiloporto.cloudfinance.web.AbstractOperationResponse;
import br.com.camiloporto.cloudfinance.web.UserOperationResponse;

@RequestMapping("/user")
@Controller
@SessionAttributes(value={"logged", "rootAccount"})
public class StaticUserProfileController {
	
	@Autowired
	private UserProfileManager userProfileManager;
	
	@Autowired
	private AccountManager accountManager;
	
	@Autowired
	private br.com.camiloporto.cloudfinance.web.UserProfileController jsonController;
	
	@RequestMapping(value = "/signup", method = RequestMethod.POST)
	public ModelAndView signUp(String userName, String pass, String confirmPass) {
		UserOperationResponse response = (UserOperationResponse) jsonController.signUp(userName, pass, confirmPass);
		ModelAndView mav = new ModelAndView("mobile-status");
		mav.addObject("response", response);
		mav.addObject("operation", "Cadastro de Usuario");
		return mav;
	}

}
