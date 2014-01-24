package br.com.camiloporto.cloudfinance.checkers;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import java.io.UnsupportedEncodingException;

import javax.servlet.http.HttpSession;

import net.minidev.json.JSONArray;

import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultMatcher;
import org.testng.Assert;

import br.com.camiloporto.cloudfinance.model.AccountSystem;
import br.com.camiloporto.cloudfinance.model.Profile;

import com.jayway.jsonpath.JsonPath;

public class WebResponseChecker {

	private ResultActions response;
	private MockHttpSession mockSession;
	
	public WebResponseChecker(ResultActions response, MockHttpSession mockSession) {
		this.response = response;
		this.mockSession = mockSession;
	}

	public WebResponseChecker assertOperationFail() throws Exception {
		response.andExpect(jsonPath("$.success").value(false));
		return this;
	}

	public WebResponseChecker assertErrorMessageIsPresent(String errorMessage) throws UnsupportedEncodingException {
		MockHttpServletResponse res = response.andReturn().getResponse();
		System.out.println(res.getCharacterEncoding());
		String jsonResponse = response.andReturn().getResponse().getContentAsString();
		JSONArray errors = JsonPath.read(jsonResponse, "$.errors[*]");
		System.out.println(errors);
		Assert.assertTrue(errors.contains(errorMessage), "error message not present '" + errorMessage + "'");
		return this;
	}

	public WebResponseChecker assertOperationSuccess() throws Exception {
		response.andExpect(jsonPath("$.success").value(true));
		return this;
	}

	public WebResponseChecker assertUserIsInSession(final String userName) throws Exception {
		response.andExpect(new ResultMatcher() {
			@Override
			public void match(MvcResult result) throws Exception {
				HttpSession session = result.getRequest().getSession();
				SecurityContext securityContext = (SecurityContext) session.getAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY);
				String name = securityContext.getAuthentication().getName(); //get logged in username
				Assert.assertEquals(name, userName, "userName is not in session");
			}
		});
		return this;
	}

	public void assertUserNotInSession() throws Exception {
		response.andExpect(new ResultMatcher() {
			@Override
			public void match(MvcResult result) throws Exception {
				HttpSession session = result.getRequest().getSession();
				SecurityContext securityContext = (SecurityContext) session.getAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY);
				Assert.assertNull(securityContext, "security context should be null");
			}
		});
	}

	public void assertDefaultAccountSystemWasActivatedInSession() throws Exception {
		response.andExpect(new ResultMatcher() {
			@Override
			public void match(MvcResult result) throws Exception {
				HttpSession session = result.getRequest().getSession();
				AccountSystem activeAccountSystem = (AccountSystem) session.getAttribute("activeAccountSystem");
				Assert.assertNotNull(activeAccountSystem, "default account system should be activated");
			}
		});
	}

}
