package br.com.camiloporto.cloudfinance.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;

import br.com.camiloporto.cloudfinance.web.MediaTypeApplicationJsonUTF8;

public class MyLogoutSuccessHandler extends SimpleUrlLogoutSuccessHandler {
	
	@Override
	public void onLogoutSuccess(HttpServletRequest request,
			HttpServletResponse response, Authentication authentication)
			throws IOException, ServletException {
		boolean isAcceptJson = acceptJson(request);
		
		// Use the DefaultSavedRequest URL
		if(!isAcceptJson) {
			String targetUrl = getDefaultTargetUrl();
			logger.debug("Redirecting to DefaultTargetUrl: " + targetUrl);
			getRedirectStrategy().sendRedirect(request, response, targetUrl);
		}
	}
	
	private boolean acceptJson(HttpServletRequest request) {
		String acceptHeader = request.getHeader("Accept");
		return acceptHeader != null && MediaTypeApplicationJsonUTF8.APPLICATION_JSON_UTF8_VALUE.contains(acceptHeader);
	}

}
