package br.com.camiloporto.cloudfinance.web.purehtml;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.ModelAndView;

import br.com.camiloporto.cloudfinance.web.UserOperationResponse;
import br.com.camiloporto.cloudfinance.web.UserProfileController;

@RequestMapping("/user")
@Controller
@SessionAttributes(value={"logged", "rootAccount"})
public class StaticUserProfileController {
	
	@Autowired
	private UserProfileController jsonController;
	
	@RequestMapping(value = "/signup", method = RequestMethod.POST)
	public ModelAndView signUp(String userName, String pass, String confirmPass, ModelMap map) {
		UserOperationResponse response = (UserOperationResponse) jsonController.signUp(userName, pass, confirmPass);
		ModelAndView mav = new ModelAndView("mobile-newUser");
		if(response.isSuccess()) {
			mav = login(userName, pass, map);
		}
		mav.addObject("response", response);
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
