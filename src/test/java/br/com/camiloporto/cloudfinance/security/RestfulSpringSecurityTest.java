package br.com.camiloporto.cloudfinance.security;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.UUID;

import javax.servlet.http.HttpSession;

import org.hamcrest.Matchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import br.com.camiloporto.cloudfinance.AbstractCloudFinanceDatabaseTest;
import br.com.camiloporto.cloudfinance.builders.WebUserManagerOperationBuilder;
import br.com.camiloporto.cloudfinance.checkers.WebResponseChecker;

@ContextConfiguration(locations = {
		"classpath:/META-INF/spring/applicationContext*.xml",
		"classpath:/META-INF/spring/webmvc-*.xml",
		"classpath:/META-INF/spring/spring-security*.xml"})
@WebAppConfiguration
public class RestfulSpringSecurityTest extends AbstractCloudFinanceDatabaseTest {
	
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
		
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac)
        		.addFilters(springSecurityFilterChain)
        		.build();
        this.mockSession = new MockHttpSession(wac.getServletContext(), UUID.randomUUID().toString());
		
    }
	
	@Test
	public void shouldNotAuthorizeAccessForUnauthenticatedUserOnRestrictedUrls() throws Exception {
		mockMvc.perform(get("/account/roots")
				.accept(MediaType.APPLICATION_JSON))
		.andExpect(status().isUnauthorized());
	}
	
	@Test
	public void shouldLogoffLoggedUser() throws Exception {
		final String username = "camiloporto";
		final String password = "s3cret";
		mockMvc.perform(post("/user/login").param("userName", username).param("pass", password).accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk());
		
		ResultActions response = mockMvc.perform(post("/user/logoff")
				.session(mockSession)
				.accept(MediaType.APPLICATION_JSON)
			);
		
		response
			.andExpect(status().isOk())
			.andExpect(new ResultMatcher() {
				public void match(MvcResult mvcResult) throws Exception {
					HttpSession session = mvcResult.getRequest().getSession();
					SecurityContext securityContext = (SecurityContext) session.getAttribute(SEC_CONTEXT_ATTR);
					Assert.assertNull(securityContext);
				}
			});
	}
	
	@Test
	public void shouldRedirectWhenLogoffWithHTML() throws Exception {
		final String userName ="some@email.com";
		final String userPass ="1234";
		final String userConfirmPass ="1234";
		new WebUserManagerOperationBuilder(mockMvc, mockSession)
			.signup(userName, userPass, userConfirmPass);
		
		ResultActions response = mockMvc.perform(post("/user/login")
				.session(mockSession)
				.param("userName", userName)
				.param("pass", userPass)
				.accept(MediaType.APPLICATION_JSON)
			);
		
		response
			.andExpect(status().isOk());
		
		new WebResponseChecker(response, mockSession)
			.assertUserIsInSession(userName)
			.assertDefaultAccountSystemWasActivatedInSession();
		
		
		//logoff
		response = mockMvc.perform(post("/user/logoff")
				.session(mockSession)
			);
		
		String expectedRedirectedUrl = "/mobile";
		response
			.andExpect(status().isMovedTemporarily())
			.andExpect(MockMvcResultMatchers.redirectedUrl(expectedRedirectedUrl));
	
		new WebResponseChecker(response, mockSession)
			.assertUserNotInSession();
		
	}
	
	@Test
	public void shouldAuthenticateExistentUser() throws Exception {
		final String username = "camiloporto";
		final String password = "s3cret";
		mockMvc.perform(post("/user/login").param("userName", username).param("pass", password).accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(new ResultMatcher() {
				public void match(MvcResult mvcResult) throws Exception {
					HttpSession session = mvcResult.getRequest().getSession();
					SecurityContext securityContext = (SecurityContext) session.getAttribute(SEC_CONTEXT_ATTR);
					Assert.assertEquals(securityContext.getAuthentication().getName(), username);
				}
			});
	}
	
	@Test
	public void shouldRedirectToRootAccountsPageIfHasAlreadyALoggedUser_WithNoJS() throws Exception {
		final String userName ="some@email.com";
		final String userPass ="1234";
		final String userConfirmPass ="1234";
		new WebUserManagerOperationBuilder(mockMvc, mockSession)
			.signup(userName, userPass, userConfirmPass);
		
		
		ResultActions response = mockMvc.perform(post("/user/login")
						.session(mockSession)
						.param("userName", userName)
						.param("pass", userPass)
					);
		HttpSession session = response.andReturn().getRequest().getSession();
		
		//already logged user try to access home page again
		response = mockMvc.perform(get("/mobile")
				.session((MockHttpSession) session)
				.param("userName", userName)
				.param("pass", userPass)
			); 
		
		String expectedRedirectedUrl = "/account/roots";
		response
			.andExpect(status().isMovedTemporarily()) //redirected
			.andExpect(MockMvcResultMatchers.redirectedUrl(expectedRedirectedUrl));
		
		new WebResponseChecker(response, mockSession)
			.assertUserIsInSession(userName)
			.assertDefaultAccountSystemWasActivatedInSession();
		
	}
	
	@Test
	public void shouldStayOnLogonPageIfFailAuthenticationInHTMLMode() throws Exception {
		final String userName ="some@email.com";
		final String userPass ="1234";
		final String userConfirmPass ="1234";
		new WebUserManagerOperationBuilder(mockMvc, mockSession)
			.signup(userName, userPass, userConfirmPass).logoutCurrentUser();
		
		ResultActions response = mockMvc.perform(post("/user/login")
				.session(mockSession)
				.param("userName", userName)
				.param("pass", "WRONG_PASS")
			);
		
		response
			.andExpect(status().isMovedTemporarily());
		
		response = mockMvc.perform(get(response.andReturn().getResponse().getRedirectedUrl()));
				
		
		String expectedViewName = "mobile-index";
		response
			.andExpect(status().isOk())
			.andExpect(MockMvcResultMatchers.view().name(expectedViewName))
			.andExpect(MockMvcResultMatchers.model().attribute("response", Matchers.hasProperty("success", Matchers.is(false))));
		
		
		new WebResponseChecker(response, mockSession)
			.assertUserNotInSession();
		
	}
	
	@Test
	public void shouldFailAuthenticationWithWrongCredentials() throws Exception {
		final String username = "camiloporto";
		final String password = "invalidPass";
		mockMvc.perform(post("/user/login").param("userName", username).param("pass", password).accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isUnauthorized())
			.andExpect(new ResultMatcher() {
				public void match(MvcResult mvcResult) throws Exception {
					HttpSession session = mvcResult.getRequest().getSession();
					SecurityContext securityContext = (SecurityContext) session.getAttribute(SEC_CONTEXT_ATTR);
					Assert.assertNull(securityContext);
				}
			});
	}
	
	@Test
	public void shouldFailAuthenticationWithEmptyCredentials() throws Exception {
		final String username = "";
		final String password = "";
		mockMvc.perform(post("/user/login").param("userName", username).param("pass", password).accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isUnauthorized())
			.andExpect(new ResultMatcher() {
				public void match(MvcResult mvcResult) throws Exception {
					HttpSession session = mvcResult.getRequest().getSession();
					SecurityContext securityContext = (SecurityContext) session.getAttribute(SEC_CONTEXT_ATTR);
					Assert.assertNull(securityContext);
				}
			});
	}
}
