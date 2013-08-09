package br.com.camiloporto.cloudfinance.checkers;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import java.io.UnsupportedEncodingException;

import net.minidev.json.JSONArray;

import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.ResultActions;
import org.testng.Assert;

import br.com.camiloporto.cloudfinance.model.Account;
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

	public WebResponseChecker assertUserIsInSession(String userName) throws Exception {
		Profile logged = (Profile) mockSession.getAttribute("logged");
		Assert.assertEquals(logged.getUserId(), userName, "userName is not in session");
		return this;
	}

	public void assertUserNotInSession() {
		Profile logged = (Profile) mockSession.getAttribute("logged");
		Assert.assertNull(logged, "user should not be in session");
	}

	public void assertDefaultAccountTreeWasSetInSession() {
		Account rootAccount = (Account) mockSession.getAttribute("rootAccount");
		Assert.assertNotNull(rootAccount, "default root account should be set");
	}

}
