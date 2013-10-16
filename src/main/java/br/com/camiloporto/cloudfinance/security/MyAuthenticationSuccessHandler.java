package br.com.camiloporto.cloudfinance.security;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

import br.com.camiloporto.cloudfinance.model.AccountSystem;
import br.com.camiloporto.cloudfinance.model.Profile;
import br.com.camiloporto.cloudfinance.service.AccountManager;
import br.com.camiloporto.cloudfinance.service.UserProfileManager;
import br.com.camiloporto.cloudfinance.web.MediaTypeApplicationJsonUTF8;
public class MyAuthenticationSuccessHandler extends
		SimpleUrlAuthenticationSuccessHandler {
	
	@Autowired
	private UserProfileManager userProfileManager;
	
	@Autowired
	private AccountManager accountManager;

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request,
			HttpServletResponse response, Authentication authentication)
			throws ServletException, IOException {
		clearAuthenticationAttributes(request);
		String userName = authentication.getName();
		Profile logged = userProfileManager.findByUsername(userName);
		if(logged != null) {
			request.getSession().setAttribute("logged", logged);
			List<AccountSystem> accountSystems = accountManager.findAccountSystems(logged);
			request.getSession().setAttribute("activeAccountSystem", accountSystems.get(0));
		}
		
		boolean isAcceptJson = acceptJson(request);
		
		// Use the DefaultSavedRequest URL
		if(!isAcceptJson) {
			String targetUrl = getDefaultTargetUrl();
			logger.debug("Redirecting to DefaultSavedRequest Url: " + targetUrl);
			getRedirectStrategy().sendRedirect(request, response, targetUrl);
		}
	}

	private boolean acceptJson(HttpServletRequest request) {
		String acceptHeader = request.getHeader("Accept");
		return acceptHeader != null && MediaTypeApplicationJsonUTF8.APPLICATION_JSON_UTF8_VALUE.contains(acceptHeader);
	}

}
