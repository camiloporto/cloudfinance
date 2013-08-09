package br.com.camiloporto.cloudfinance.web;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import br.com.camiloporto.cloudfinance.AbstractCloudFinanceDatabaseTest;
import br.com.camiloporto.cloudfinance.builders.WebUserManagerOperationBuilder;
import br.com.camiloporto.cloudfinance.checkers.WebResponseChecker;
import br.com.camiloporto.cloudfinance.i18n.ValidationMessages;
import br.com.camiloporto.cloudfinance.model.Profile;

import com.jayway.jsonpath.JsonPath;

@ContextConfiguration(locations = {"classpath:/META-INF/spring/applicationContext*.xml", "classpath:/META-INF/spring/webmvc-*.xml"})
@WebAppConfiguration
public class UserProfileControllerTest extends AbstractCloudFinanceDatabaseTest {
	
	@Autowired
    private WebApplicationContext wac;
	
	@BeforeMethod
	public void clearUserData() {
		cleanUserData();
	}

    private MockMvc mockMvc;
    private MockHttpSession mockSession;

    @BeforeMethod
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
        this.mockSession = new MockHttpSession(wac.getServletContext(), UUID.randomUUID().toString());
    }
    
    @Test
	public void shouldInsertNewAccountSystemWhenUserSignUpWithNoJS() throws Exception {
		final String userName ="some@email.com";
		final String userPass ="1234";
		final String userConfirmPass ="1234";
		
		//normal html POST - no JSON
		ResultActions response = mockMvc.perform(post("/user/signup")
			.param("userName", userName)
			.param("pass", userPass)
			.param("confirmPass", userConfirmPass)
		);
		
		
		response
			.andExpect(status().isOk())
			.andExpect(MockMvcResultMatchers.model().attributeExists("response"));
		
	}
    
	@Test
	public void shouldInsertNewAccountSystemWhenUserSignUp() throws Exception {
		final String userName ="some@email.com";
		final String userPass ="1234";
		final String userConfirmPass ="1234";
		
		ResultActions response = mockMvc.perform(post("/user/signup")
			.param("userName", userName)
			.param("pass", userPass)
			.param("confirmPass", userConfirmPass)
			.accept(MediaType.APPLICATION_JSON)
		);
		
		
		response
			.andExpect(status().isOk())
			.andExpect(MockMvcResultMatchers.content().contentType(MediaTypeApplicationJsonUTF8.APPLICATION_JSON_UTF8_VALUE))
			.andExpect(jsonPath("$.userId").exists());
		
		new WebResponseChecker(response, mockSession)
			.assertOperationSuccess();
		
		String jsonResponse = response.andReturn().getResponse().getContentAsString();
		Integer userId = JsonPath.read(jsonResponse, "$.userId");
		Profile profile = profileRepository.findOne(new Long(userId));
		Assert.assertNotNull(profile, "profile not created in database");
	}
	
	@Test
	public void shouldInformErrorIfEmailAlreadyExistsOnSignUp() throws Exception {
		final String userName ="some@email.com";
		final String userPass ="1234";
		final String userConfirmPass ="1234";
		
		mockMvc.perform(post("/user/signup")
			.param("userName", userName)
			.param("pass", userPass)
			.param("confirmPass", userConfirmPass)
			.accept(MediaType.APPLICATION_JSON)
		);
		
		//second request for signup
		ResultActions response = mockMvc.perform(post("/user/signup")
				.param("userName", userName)
				.param("pass", userPass)
				.param("confirmPass", userConfirmPass)
				.accept(MediaType.APPLICATION_JSON)
			);
		
		response
			.andExpect(status().isOk())
			.andExpect(MockMvcResultMatchers.content().contentType(MediaTypeApplicationJsonUTF8.APPLICATION_JSON_UTF8_VALUE));
		
		new WebResponseChecker(response, mockSession)
			.assertOperationFail()
			.assertErrorMessageIsPresent(ValidationMessages.USER_ID_ALREADY_EXIST);
	}
	
	@Test
	public void shouldInformErrorIfEmailIsEmptyOnSignUp() throws Exception {
		final String userName ="";
		final String userPass ="1234";
		final String userConfirmPass ="1234";
		
		ResultActions response = mockMvc.perform(post("/user/signup")
			.param("userName", userName)
			.param("pass", userPass)
			.param("confirmPass", userConfirmPass)
			.accept(MediaType.APPLICATION_JSON)
		);
		
		response
			.andExpect(status().isOk())
			.andExpect(MockMvcResultMatchers.content().contentType(MediaTypeApplicationJsonUTF8.APPLICATION_JSON_UTF8_VALUE));
		
		new WebResponseChecker(response, mockSession)
			.assertOperationFail()
			.assertErrorMessageIsPresent(ValidationMessages.USER_ID_REQUIRED);
	}
	
	@Test
	public void shouldInformErrorIfPasswordIsEmptyOnSignUp() throws Exception {
		final String userName ="some@email.com";
		final String userPass ="";
		final String userConfirmPass ="";
		
		ResultActions response = mockMvc.perform(post("/user/signup")
			.session(mockSession)
			.param("userName", userName)
			.param("pass", userPass)
			.param("confirmPass", userConfirmPass)
			.accept(MediaType.APPLICATION_JSON)
		);
		
		response
			.andExpect(status().isOk())
			.andExpect(MockMvcResultMatchers.content().contentType(MediaTypeApplicationJsonUTF8.APPLICATION_JSON_UTF8_VALUE));
		
		new WebResponseChecker(response, mockSession)
			.assertOperationFail()
			.assertErrorMessageIsPresent(ValidationMessages.USER_PASS_REQUIRED);
	}
	
	@Test
	public void shouldAuthenticateRegisteredUser() throws Exception {
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
		
		response
			.andExpect(status().isOk())
			.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));
		
		new WebResponseChecker(response, mockSession)
			.assertOperationSuccess()
			.assertUserIsInSession(userName)
			.assertDefaultAccountTreeWasSetInSession();
		
	}
	
	@Test
	public void shouldFailAuthenticationWithWrongPassword() throws Exception {
		final String userName ="some@email.com";
		final String userPass ="1234";
		final String userConfirmPass ="1234";
		new WebUserManagerOperationBuilder(mockMvc, mockSession)
			.signup(userName, userPass, userConfirmPass);
		
		ResultActions response = mockMvc.perform(post("/user/login")
				.session(mockSession)
				.param("userName", userName)
				.param("pass", "WRONG_PASS")
			);
		
		response
			.andExpect(status().isOk())
			.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));
		
		new WebResponseChecker(response, mockSession)
			.assertOperationFail()
			.assertUserNotInSession();
		
	}
	
	@Test
	public void shouldFailAuthenticationWithWrongUsername() throws Exception {
		final String userName ="some@email.com";
		final String userPass ="1234";
		final String userConfirmPass ="1234";
		new WebUserManagerOperationBuilder(mockMvc, mockSession)
			.signup(userName, userPass, userConfirmPass);
		
		ResultActions response = mockMvc.perform(post("/user/login")
				.session(mockSession)
				.param("userName", "doNotExist@SuchEmail.com")
				.param("pass", userPass)
			);
		
		response
			.andExpect(status().isOk())
			.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));
		
		new WebResponseChecker(response, mockSession)
			.assertOperationFail()
			.assertUserNotInSession();
		
	}
	
	@Test
	public void shouldFailAuthenticationWithEmptyCredentials() throws Exception {
		final String userName ="some@email.com";
		final String userPass ="1234";
		final String userConfirmPass ="1234";
		new WebUserManagerOperationBuilder(mockMvc, mockSession)
			.signup(userName, userPass, userConfirmPass);
		
		ResultActions response = mockMvc.perform(post("/user/login")
				.session(mockSession)
				.param("userName", "")
				.param("pass", "")
			);
		
		response
			.andExpect(status().isOk())
			.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));
		
		new WebResponseChecker(response, mockSession)
			.assertOperationFail()
			.assertUserNotInSession();
		
	}
}
