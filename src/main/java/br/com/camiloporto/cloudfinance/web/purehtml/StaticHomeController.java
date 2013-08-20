package br.com.camiloporto.cloudfinance.web.purehtml;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

@RequestMapping
@Controller
@SessionAttributes(value = {"logged"})
public class StaticHomeController {
	
	@RequestMapping(value = "/mobile", method = RequestMethod.GET)
	public ModelAndView index(HttpSession session) {
		ModelAndView mav = new ModelAndView("mobile-index");
		if(session.getAttribute("logged") != null) {
			mav.setViewName("redirect:/account/roots");
		}
		return mav;
	}

}
