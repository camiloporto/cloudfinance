package br.com.camiloporto.cloudfinance.web;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import junit.framework.Assert;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import br.com.camiloporto.cloudfinance.AbstractCloudFinanceDatabaseTest;
import br.com.camiloporto.cloudfinance.checkers.WebResponseChecker;
import br.com.camiloporto.cloudfinance.model.Profile;

import com.jayway.jsonpath.JsonPath;

@ContextConfiguration(locations = {"classpath:/META-INF/spring/applicationContext*.xml", "classpath:/META-INF/spring/webmvc-*.xml"})
@WebAppConfiguration
public class AccountSystemControllerTest extends AbstractCloudFinanceDatabaseTest {
	
	@Autowired
    private WebApplicationContext wac;
	
	@BeforeMethod
	public void clearUserData() {
		cleanUserData();
	}

    private MockMvc mockMvc;

    @BeforeMethod
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
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
		);
		
		
		response
			.andExpect(status().isOk())
			.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$.userId").exists());
		
		new WebResponseChecker(response)
			.assertOperationSuccess();
		
		String jsonResponse = response.andReturn().getResponse().getContentAsString();
		Integer userId = JsonPath.read(jsonResponse, "$.userId");
		Profile profile = profileRepository.findOne(new Long(userId));
		Assert.assertNotNull("profile not created in database", profile);
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
		);
		
		//second request for signup
		ResultActions response = mockMvc.perform(post("/user/signup")
				.param("userName", userName)
				.param("pass", userPass)
				.param("confirmPass", userConfirmPass)
			);
		
		response
			.andExpect(status().isOk())
			.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));
		
		new WebResponseChecker(response)
			.assertOperationFail()
			.assertErrorMessageIsPresent("br.com.camiloporto.cloudfinance.profile.USER_ID_ALREADY_EXIST");
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
		);
		
		response
			.andExpect(status().isOk())
			.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));
		
		new WebResponseChecker(response)
			.assertOperationFail()
			.assertErrorMessageIsPresent("br.com.camiloporto.cloudfinance.profile.USER_ID_REQUIRED");
	}
	
	@Test
	public void shouldInformErrorIfPasswordIsEmptyOnSignUp() throws Exception {
		final String userName ="some@email.com";
		final String userPass ="";
		final String userConfirmPass ="";
		
		ResultActions response = mockMvc.perform(post("/user/signup")
			.param("userName", userName)
			.param("pass", userPass)
			.param("confirmPass", userConfirmPass)
		);
		
		response
			.andExpect(status().isOk())
			.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));
		
		new WebResponseChecker(response)
			.assertOperationFail()
			.assertErrorMessageIsPresent("br.com.camiloporto.cloudfinance.profile.USER_PASS_REQUIRED");
	}
}
