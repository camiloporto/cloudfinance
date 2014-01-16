package br.com.camiloporto.cloudfinance.checkers;

import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultMatcher;
import org.testng.Assert;

public class CustomMockMvcResultMatchers {
	
	public static ResultMatcher redirectUrlStartsWith(final String prefix) {
		return new ResultMatcher() {
			
			@Override
			public void match(MvcResult result) throws Exception {
				String actualRedirectedUrl = result.getResponse().getRedirectedUrl();
				Assert.assertTrue(actualRedirectedUrl.startsWith(prefix), "redirected url did not started with given prefix. actual = " +actualRedirectedUrl);
			}
		};
	}

}
