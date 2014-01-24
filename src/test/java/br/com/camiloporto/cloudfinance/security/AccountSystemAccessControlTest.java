package br.com.camiloporto.cloudfinance.security;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import net.minidev.json.JSONArray;

import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.ResultActions;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import br.com.camiloporto.cloudfinance.AbstractWebMvcCloudFinanceTest;
import br.com.camiloporto.cloudfinance.builders.WebUserManagerOperationBuilder;
import br.com.camiloporto.cloudfinance.model.AccountSystem;
import br.com.camiloporto.cloudfinance.model.Profile;

import com.jayway.jsonpath.JsonPath;

public class AccountSystemAccessControlTest extends
		AbstractWebMvcCloudFinanceTest {
	

	private final String username = "oneuser@email.com";
	private final String password = "onepass";
	
	private final String otherUsername = "otheruser@email.com";
	private final String otherPassword = "otherpass";

	private Integer rootAccountId;

	private Long otherRootAccountId;
	private Profile otherProfile;
	
	@BeforeMethod
    public void setup() throws Exception {
		cleanUserData();
		init();
        
        new WebUserManagerOperationBuilder(mockMvc, mockSession)
			.signup(username, password, password);
	
        new WebUserManagerOperationBuilder(mockMvc, mockSession)
			.signup(otherUsername, otherPassword, otherPassword);
        
        ResultActions response = mockMvc.perform(post("/user/login").param("userName", username).param("pass", password).accept(MediaType.APPLICATION_JSON));
        authenticatedSession = response.andReturn().getRequest().getSession();
        
        response = mockMvc.perform(prepareJsonGetRequest("/account/roots", (MockHttpSession) authenticatedSession));
		
		String json = response.andReturn().getResponse().getContentAsString();
		rootAccountId = JsonPath.read(json, "$.accountSystems[0].rootAccount.id");
		JsonPath.read(json, "$.accountSystems[0].id");
		
		otherProfile = profileRepository.findByUserId(otherUsername);
		List<AccountSystem> accountSystem = accountSystemRepository.findByUserProfile(otherProfile);
		otherRootAccountId = accountSystem.get(0).getRootAccount().getId();
		
    }

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
		
		//forcing use other user profile to try access to other users account systems
		authenticatedSession.setAttribute("logged", otherProfile);
		
		mockMvc.perform(prepareJsonGetRequest("/account/roots", (MockHttpSession) authenticatedSession))
			.andExpect(status().isUnauthorized());
		
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
	public void oneUserShouldNotAccessOtherUserAccountTree() throws Exception {
		
		mockMvc.perform(prepareJsonGetRequest("/account/tree/" + otherRootAccountId, (MockHttpSession) authenticatedSession))
			.andExpect(status().isUnauthorized());
	}
}
