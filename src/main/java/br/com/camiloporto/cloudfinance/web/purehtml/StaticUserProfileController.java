package br.com.camiloporto.cloudfinance.web.purehtml;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
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
	public ModelAndView signUp(String userName, String pass, String confirmPass) {
		UserOperationResponse response = (UserOperationResponse) jsonController.signUp(userName, pass, confirmPass);
		ModelAndView mav = new ModelAndView("mobile-status");
		mav.addObject("response", response);
		mav.addObject("operation", "Cadastro de Usuario");
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
	public ModelAndView logoff(HttpServletRequest request) {
		ModelAndView mav = new ModelAndView("redirect:/mobile");
		jsonController.logoff(request);
		return mav;
	}

}
