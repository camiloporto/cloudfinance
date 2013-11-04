package br.com.camiloporto.cloudfinance.security;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import java.util.UUID;

import net.minidev.json.JSONArray;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import br.com.camiloporto.cloudfinance.AbstractWebMvcCloudFinanceTest;
import br.com.camiloporto.cloudfinance.builders.WebUserManagerOperationBuilder;
import br.com.camiloporto.cloudfinance.model.AccountSystem;
import br.com.camiloporto.cloudfinance.model.Profile;

import com.jayway.jsonpath.JsonPath;

@WebAppConfiguration
public class AccountSystemAccessControlTest extends
		AbstractWebMvcCloudFinanceTest {
	
	private static String SEC_CONTEXT_ATTR = HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY;

	@Autowired
    private WebApplicationContext wac;
	private MockMvc mockMvc;
	private MockHttpSession mockSession;

	private final String username = "oneuser@email.com";
	private final String password = "onepass";
	
	private final String otherUsername = "otheruser@email.com";
	private final String otherPassword = "otherpass";

	private Integer rootAccountId;

	private Integer accountSystemId;

	private Long otherRootAccountId;
	
	@BeforeMethod
    public void setup() throws Exception {
		cleanUserData();
		
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac)
        		.addFilters(springSecurityFilterChain)
        		.build();
        this.mockSession = new MockHttpSession(wac.getServletContext(), UUID.randomUUID().toString());
        
        new WebUserManagerOperationBuilder(mockMvc, mockSession)
			.signup(username, password, password);
	
        new WebUserManagerOperationBuilder(mockMvc, mockSession)
			.signup(otherUsername, otherPassword, otherPassword);
        
        ResultActions response = mockMvc.perform(post("/user/login").param("userName", username).param("pass", password).accept(MediaType.APPLICATION_JSON));
        authenticatedSession = response.andReturn().getRequest().getSession();
        
        response = mockMvc.perform(prepareJsonGetRequest("/account/roots", (MockHttpSession) authenticatedSession));
		
		String json = response.andReturn().getResponse().getContentAsString();
		rootAccountId = JsonPath.read(json, "$.accountSystems[0].rootAccount.id");
		accountSystemId = JsonPath.read(json, "$.accountSystems[0].id");
		
		Profile other = profileRepository.findByUserId(otherUsername);
		List<AccountSystem> accountSystem = accountSystemRepository.findByUserProfile(other);
		otherRootAccountId = accountSystem.get(0).getRootAccount().getId();
		
    }

	//FIXME implementar mais testes de controle de acesso para outras operacoes do AccountManager
	//proximo: createAccountSystem
	
	@Test
	public void onlyAccountSystemOwnerCanCreateAccountOnIt() throws Exception {
		
		ResultActions response = mockMvc.perform(prepareJsonGetRequest("/account/tree/" + rootAccountId, (MockHttpSession) authenticatedSession));
		
		String json = response.andReturn().getResponse().getContentAsString();
		
		final String accountName = "NewAccount";
		final String accountDescription ="short description";
		
		final Integer parentId = JsonPath.read(json, "$.accountTree.children[0].account.id");
		response = mockMvc.perform(prepareJsonPostRequest("/account", (MockHttpSession) authenticatedSession)
				.param("name", accountName)
				.param("description", accountDescription)
				.param("parentAccount.id", parentId.toString())
				.param("rootAccount.id", otherRootAccountId.toString())//trying to insert an account on other user accountSystem
			);
		
		response
			.andExpect(status().isUnauthorized());
	}
	
	@Test
	public void oneUserShouldOnlyHaveAccessToItsAccountSystems() throws Exception {
		
		ResultActions response = mockMvc.perform(prepareJsonGetRequest("/account/roots", (MockHttpSession) authenticatedSession))
			.andExpect(status().isOk());
		
		String json = response.andReturn().getResponse().getContentAsString();
		Profile loggedUserProfile = userProfileManager.findByUsername(username);
		Profile otherUserProfile = userProfileManager.findByUsername(otherUsername);
		
		List<AccountSystem> userAccountSystems = accountSystemRepository.findByUserProfile(loggedUserProfile);
		List<AccountSystem> otherUserAccountSystems = accountSystemRepository.findByUserProfile(otherUserProfile);
		
		JSONArray returnedAccountSystemIds = JsonPath.read(json, "$.accountSystems[*].id");
		
		assertAccountSystemIdsIsInJSONArray(userAccountSystems, returnedAccountSystemIds);
		assertAccountSystemIdsIsNotInJSONArray(otherUserAccountSystems, returnedAccountSystemIds);
	}
	
	@Test
	public void oneUserShouldOnlyHaveAccessToItsAccountSystem() throws Exception {
		Profile otherUserProfile = userProfileManager.findByUsername(otherUsername);
		
		List<AccountSystem> otherUserAccountSystems = accountSystemRepository.findByUserProfile(otherUserProfile);
		
		Long otherAccountSystemId = otherUserAccountSystems.get(0).getId(); 
		
		//trying to activate other user account system
		mockMvc.perform(prepareJsonGetRequest("/account/" + otherAccountSystemId, (MockHttpSession) authenticatedSession))
			.andExpect(status().isUnauthorized());
	}
	
	@Test
	public void oneUserShouldNotAccessOtherUserLeavesAccounts() throws Exception {
		Profile otherUserProfile = userProfileManager.findByUsername(otherUsername);
		
		AccountSystem otherUserAccountSystem = accountSystemRepository.findByUserProfile(otherUserProfile).get(0);
		
		//trying to access other user internal leaves account
		mockMvc.perform(prepareJsonGetRequest("/account/leaf/" + otherUserAccountSystem.getRootAccount().getId(), (MockHttpSession) authenticatedSession))
			.andExpect(status().isUnauthorized());
	}
	
	private void assertAccountSystemIdsIsNotInJSONArray(
			List<AccountSystem> accountSystems, JSONArray jsonArray) {
		for (AccountSystem as : accountSystems) {
			boolean found = false;
			for (Object object : jsonArray) {
				if(as.getId().toString().equals(object.toString())) {
					found = true;
					break;
				}
			}
			Assert.assertFalse(found, "account system " + as.getId() + " should not be found");
		}
	}

	private void assertAccountSystemIdsIsInJSONArray(List<AccountSystem> accountSystems, JSONArray jsonArray) {
		for (AccountSystem as : accountSystems) {
			boolean found = false;
			for (Object object : jsonArray) {
				if(as.getId().toString().equals(object.toString())) {
					found = true;
					break;
				}
			}
			Assert.assertTrue(found, "account system " + as.getId() + " not found");
		}
	}
	
	@Test
	public void oneUserShouldNotAccessOtherUserInformations() throws Exception {
		
		mockMvc.perform(prepareJsonGetRequest("/account/tree/" + otherRootAccountId, (MockHttpSession) authenticatedSession))
			.andExpect(status().isUnauthorized());
	}
}
