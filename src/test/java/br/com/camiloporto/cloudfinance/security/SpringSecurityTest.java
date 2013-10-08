package br.com.camiloporto.cloudfinance.security;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import br.com.camiloporto.cloudfinance.AbstractCloudFinanceDatabaseTest;

@ContextConfiguration(locations = {
		"classpath:/META-INF/spring/applicationContext*.xml",
		"classpath:/META-INF/spring/webmvc-*.xml" })
@WebAppConfiguration
public class SpringSecurityTest extends AbstractCloudFinanceDatabaseTest {
	
	private static String SEC_CONTEXT_ATTR = HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY;

	@Autowired
	private FilterChainProxy springSecurityFilterChain;
	
	@Autowired
    private WebApplicationContext wac;
	private MockMvc mockMvc;
	private MockHttpSession mockSession;
	
	@BeforeMethod
    public void setup() throws Exception {
		cleanUserData();
		
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
        this.mockSession = new MockHttpSession(wac.getServletContext(), UUID.randomUUID().toString());
		
    }
	
	@Test
	public void shouldRequireAuthenticatedUserOnRestrictedUrls() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/account/roots"))
		.andExpect(MockMvcResultMatchers.redirectedUrl("/spring_security_login"));
	}
}
