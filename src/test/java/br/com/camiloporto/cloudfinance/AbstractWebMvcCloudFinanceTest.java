package br.com.camiloporto.cloudfinance;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import java.util.UUID;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import br.com.camiloporto.cloudfinance.web.MediaTypeApplicationJsonUTF8;

public abstract class AbstractWebMvcCloudFinanceTest extends
		AbstractCloudFinanceDatabaseTest {
	
	@Autowired
	protected WebApplicationContext wac;
	

	protected MockMvc mockMvc;
	protected MockHttpSession mockSession;

	@Autowired
	protected FilterChainProxy springSecurityFilterChain;
	
	protected HttpSession authenticatedSession;
	
	protected void init() {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac)
        		.addFilters(springSecurityFilterChain)
        		.build();
        this.mockSession = new MockHttpSession(wac.getServletContext(), UUID.randomUUID().toString());
	}
	
	protected MockHttpServletRequestBuilder prepareHtmlPostRequest(String url, MockHttpSession session) {
		return post(url).session(session);
	}
	
	protected MockHttpServletRequestBuilder prepareJsonPostRequest(String url, MockHttpSession session) {
		return prepareHtmlPostRequest(url, session).accept(MediaTypeApplicationJsonUTF8.APPLICATION_JSON_UTF8);
	}
	
	protected MockHttpServletRequestBuilder prepareHtmlGetRequest(String url, MockHttpSession session) {
		return get(url).session(session);
	}
	
	protected MockHttpServletRequestBuilder prepareJsonGetRequest(String url, MockHttpSession session) {
		return prepareHtmlGetRequest(url, session).accept(MediaTypeApplicationJsonUTF8.APPLICATION_JSON_UTF8);
	}

}
