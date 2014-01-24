package br.com.camiloporto.cloudfinance.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

import br.com.camiloporto.cloudfinance.web.MediaTypeApplicationJsonUTF8;

public class RestfulExceptionMappingAuthenticationFailureHandler extends
		SimpleUrlAuthenticationFailureHandler {
	
	public void onAuthenticationFailure(HttpServletRequest request,
			HttpServletResponse response, AuthenticationException exception)
			throws IOException, ServletException {
		
		boolean isAcceptJson = acceptJson(request);
		
		// Use the DefaultSavedRequest URL
		if(!isAcceptJson) {
			super.onAuthenticationFailure(request, response, exception);
		} else {
			response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
		}
	}
	
	private boolean acceptJson(HttpServletRequest request) {
		String acceptHeader = request.getHeader("Accept");
		return acceptHeader != null && MediaTypeApplicationJsonUTF8.APPLICATION_JSON_UTF8_VALUE.contains(acceptHeader);
	}

}
