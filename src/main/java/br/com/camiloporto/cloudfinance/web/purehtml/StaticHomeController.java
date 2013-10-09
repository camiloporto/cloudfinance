package br.com.camiloporto.cloudfinance.web.purehtml;

import java.security.Principal;

import javax.servlet.http.HttpSession;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

@RequestMapping
@Controller
@SessionAttributes(value = {"logged"})
public class StaticHomeController {
	
	private static String SEC_CONTEXT_ATTR = HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY;
	
	@RequestMapping(value = "/mobile", method = RequestMethod.GET)
	public ModelAndView mobileHome(HttpSession session, Principal principal) {
		ModelAndView mav = new ModelAndView("mobile-index");
		if(isAuthenticated(session)) {
			mav.setViewName("redirect:/account/roots");
		}
//		if(session.getAttribute("logged") != null) {
//			mav.setViewName("redirect:/account/roots");
//		}
		return mav;
	}
	
	private boolean isAuthenticated(HttpSession session) {
		SecurityContext securityContext = (SecurityContext) session.getAttribute(SEC_CONTEXT_ATTR);
		Authentication authentication =securityContext.getAuthentication();
        if (authentication == null) {
            return false;
        }
        if (authentication instanceof AnonymousAuthenticationToken) {
            return false;
        }
        return authentication.isAuthenticated();
    }

}
