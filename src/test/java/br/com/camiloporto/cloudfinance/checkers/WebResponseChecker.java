package br.com.camiloporto.cloudfinance.checkers;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import java.io.UnsupportedEncodingException;

import net.minidev.json.JSONArray;

import org.springframework.test.web.servlet.ResultActions;
import org.testng.Assert;

import com.jayway.jsonpath.JsonPath;

public class WebResponseChecker {

	private ResultActions response;
	
	public WebResponseChecker(ResultActions response) {
		this.response = response;
	}

	public WebResponseChecker assertOperationFail() throws Exception {
		response.andExpect(jsonPath("$.success").value(false));
		return this;
	}

	public WebResponseChecker assertErrorMessageIsPresent(String errorMessage) throws UnsupportedEncodingException {
		String jsonResponse = response.andReturn().getResponse().getContentAsString();
		JSONArray errors = JsonPath.read(jsonResponse, "$.errors");
		;
		Assert.assertTrue(errors.contains(errorMessage), "error message not present '" + errorMessage + "'");
		return this;
	}

	public WebResponseChecker assertOperationSuccess() throws Exception {
		response.andExpect(jsonPath("$.success").value(true));
		return this;
	}

}
