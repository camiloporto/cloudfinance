package br.com.camiloporto.cloudfinance.web;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.UUID;

import net.minidev.json.JSONArray;

import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
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
import org.springframework.web.servlet.ModelAndView;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import br.com.camiloporto.cloudfinance.AbstractCloudFinanceDatabaseTest;
import br.com.camiloporto.cloudfinance.builders.WebUserManagerOperationBuilder;
import br.com.camiloporto.cloudfinance.checkers.WebResponseChecker;
import br.com.camiloporto.cloudfinance.i18n.ValidationMessages;
import br.com.camiloporto.cloudfinance.model.Account;

import com.jayway.jsonpath.JsonPath;

@ContextConfiguration(locations = {"classpath:/META-INF/spring/applicationContext*.xml", "classpath:/META-INF/spring/webmvc-*.xml"})
@WebAppConfiguration
public class AccountSystemControllerTest extends AbstractCloudFinanceDatabaseTest {
	
	@Autowired
    private WebApplicationContext wac;
	

    private MockMvc mockMvc;
    private MockHttpSession mockSession;
    private Integer rootAccountId;

    @BeforeMethod
    public void setup() throws Exception {
    	cleanUserData();
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
        this.mockSession = new MockHttpSession(wac.getServletContext(), UUID.randomUUID().toString());
        
        final String userName ="some@email.com";
		final String userPass ="1234";
		final String userConfirmPass ="1234";
		new WebUserManagerOperationBuilder(mockMvc, mockSession)
			.signup(userName, userPass, userConfirmPass)
			.login(userName, userPass);
		
		ResultActions response = mockMvc.perform(get("/account/roots")
				.session(mockSession)
				.accept(MediaType.APPLICATION_JSON)
			);
		String json = response.andReturn().getResponse().getContentAsString();
		rootAccountId = JsonPath.read(json, "$.accountSystems[0].rootAccount.id");
    }
	
	@Test
	public void shouldGetUsersAccountSystems() throws Exception {
		
		ResultActions response = mockMvc.perform(get("/account/roots")
				.session(mockSession)
				.accept(MediaType.APPLICATION_JSON)
			);
		
		response
			.andExpect(status().isOk())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$.accountSystems").exists());
		
		String json = response.andReturn().getResponse().getContentAsString();
		JSONArray accountSystems = JsonPath.read(json, "$.accountSystems");
		
		final int EXPECTED_ACCOUNT_SYSTEM_COUNTS = 1;
		Assert.assertEquals(accountSystems.size(), EXPECTED_ACCOUNT_SYSTEM_COUNTS, "account systems count not match");
		Assert.assertNotNull(JsonPath.read(json, "$.accountSystems[0].id"), "id of account system should not be null");
		Assert.assertNotNull(JsonPath.read(json, "$.accountSystems[0].rootAccount.id"), "account system root account should not be null");
		
		//XXX Improve data transfer excluding properties not worth for operation requested
		
//		Assert.assertNull(JsonPath.read(json, "$.rootAccounts[0].version"), "trash properties should not be included");
	}
	
	@Test
	public void shouldGetUsersAccountsSystemsWithNoJs() throws Exception {
		
		ResultActions response = mockMvc.perform(get("/account/roots")
				.session(mockSession)
			);
		final int EXPECTED_ACCOUNT_COUNTS = 1;
		
		response
			.andExpect(status().isOk())
			.andExpect(view().name("mobile-rootAccountHome"))
			.andExpect(model().attribute("response", Matchers.hasProperty("accountSystems", Matchers.arrayWithSize(EXPECTED_ACCOUNT_COUNTS))));
		
	}
	
	//FIXME fazer testes com possibilidade de falhas da consulta de contas raiz. Se usuario nao logado???
	
	@Test
	public void shouldGetRootAccountWholeTree() throws Exception {
		
		ResultActions response = mockMvc.perform(get("/account/tree/" + rootAccountId)
				.session(mockSession)
				.accept(MediaType.APPLICATION_JSON)
			);
		
		response
			.andExpect(status().isOk())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$.accountTree").exists());
		
		String json = response.andReturn().getResponse().getContentAsString();
		JSONArray children = JsonPath.read(json, "$.accountTree.children");
		
		final int EXPECTED_ACCOUNT_CHILDREN = 4;
		Assert.assertEquals(children.size(), EXPECTED_ACCOUNT_CHILDREN, "children count not match");
	}
	
	@Test
	public void shouldGetRootAccountLeaves() throws Exception {
		
		ResultActions response = mockMvc.perform(get("/account/leaf/" + rootAccountId)
				.session(mockSession)
				.accept(MediaType.APPLICATION_JSON)
			);
		
		response
			.andExpect(status().isOk())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$.leafAccounts").exists());
		
		String json = response.andReturn().getResponse().getContentAsString();
		JSONArray leaves = JsonPath.read(json, "$.leafAccounts");
		
		final int EXPECTED_LEAVES_COUNT = 4;
		Assert.assertEquals(leaves.size(), EXPECTED_LEAVES_COUNT, "leaves count not match");
	}
	
	@Test
	public void shouldRedirectToAccountHomeAfterSelectRootAccount_NoJs() throws Exception {
		//sets the active root accountId
		ResultActions response = mockMvc.perform(get("/account/" + rootAccountId)
				.session(mockSession)
			);
		
		//should redirect get to /account
		response
			.andExpect(status().isMovedTemporarily())
			.andExpect(redirectedUrl("/account"));
		
		//following redirect... should redirect to /account/tree/<rootAccountPreviousSelected>
		response = mockMvc.perform(get("/account")
				.session(mockSession))
				.andExpect(status().isMovedTemporarily())
				.andExpect(redirectedUrl("/account/tree/" + rootAccountId));
		
		//following redirect should return <selectedRootAccount> hierarchy
		response = mockMvc.perform(get("/account/tree/" + rootAccountId)
				.session(mockSession)
			);
		
		ModelAndView mav = response.andReturn().getModelAndView();
		AccountOperationResponse aor = (AccountOperationResponse) mav.getModel().get("response");
		
		final int EXPECTED_ACCOUNT_CHILDREN = 4;
		Assert.assertEquals(aor.getAccountTree().getChildren().size(), EXPECTED_ACCOUNT_CHILDREN, "children count not match");
	}
	
	@Test
	public void shouldRedirectToRootAccountHomeIfNoRootAccountSelectedTryingAccountHome_NoJs() throws Exception {
		mockSession.removeAttribute("rootAccount");
		//try to get account home, with no selected root account
		ResultActions response = mockMvc.perform(get("/account")
				.session(mockSession)
			);
		
		//should redirect get to /account/roots
		response
			.andExpect(status().isMovedTemporarily())
			.andExpect(redirectedUrl("/account/roots"));
		
		ModelAndView mav = response.andReturn().getModelAndView();
		AccountOperationResponse aor = (AccountOperationResponse) mav.getModel().get("response");
		Assert.assertTrue(aor.getErrors().length > 0, "errors should not be empty");
		
	}
	
	@Test
	public void shouldGetEmptyTreeIfAccountIdDoNotExists() throws Exception {
		
		ResultActions response = mockMvc.perform(get("/account/tree/9999")
				.session(mockSession)
				.accept(MediaType.APPLICATION_JSON)
			);
		
		response
			.andExpect(status().isOk())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$.success").value(true))
			.andExpect(jsonPath("$.accountTree").doesNotExist());
		
	}
	
	@Test
	public void shouldAddNewAccount() throws Exception {
		
		ResultActions response = mockMvc.perform(get("/account/tree/" + rootAccountId)
				.session(mockSession)
				.accept(MediaType.APPLICATION_JSON)
			);
		
		String json = response.andReturn().getResponse().getContentAsString();
		
		final String accountName = "NewAccount";
		final String accountDescription ="short description";
		
		final Integer parentId = JsonPath.read(json, "$.accountTree.children[0].account.id");
		response = mockMvc.perform(post("/account")
				.session(mockSession)
				.accept(MediaType.APPLICATION_JSON)
				.param("name", accountName)
				.param("description", accountDescription)
				.param("parentAccount.id", parentId.toString())
				.param("rootAccount.id", rootAccountId.toString())
			);
		
		response
			.andExpect(status().isOk())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$.success").value(true))
			.andExpect(jsonPath("$.account.id").exists());
		
		json = response.andReturn().getResponse().getContentAsString();
		
		Integer newAccountId = JsonPath.read(json, "$.account.id");
		Account newAccount = accountRepository.findOne(new Long(newAccountId));
		Assert.assertNotNull(newAccount.getId(), "account id not setted");
		Assert.assertEquals(newAccount.getName(), accountName, "account name did not match");
		Assert.assertEquals(newAccount.getParentAccount().getId(), new Long(parentId), "parent did not match");
	}
	
	@Test
	public void shouldAddNewAccount_NoJS() throws Exception {
		final String newAccountName = "NewAccount";
		final String newAccountDescription ="short description";
		final Integer parentId;
		String json;
		String expectedRedirectedUrl = "/account/tree/" + rootAccountId;
		ModelAndView mav;
		
		ResultActions response = mockMvc.perform(get("/account/tree/" + rootAccountId)
				.session(mockSession)
				.accept(MediaType.APPLICATION_JSON)
			);
		json = response.andReturn().getResponse().getContentAsString();
		parentId = JsonPath.read(json, "$.accountTree.children[0].account.id");
		
		//navigate to 'showForm' page to insert new account
		response = mockMvc.perform(get("/account/showForm/" + parentId)
				.session(mockSession));
		mav = response.andExpect(status().isOk())
			.andReturn()
			.getModelAndView();
		AccountOperationResponse aor = (AccountOperationResponse) mav.getModelMap().get("response");
		Assert.assertNotNull(aor.getAccount(), "parent account not presente in request response");
		Assert.assertNotNull(aor.getAccount().getName(), "parent account name not presente in request response");
		Assert.assertEquals(aor.getAccount().getId().toString(), parentId.toString(), "parent account id did not match wth the requested");
		
		//insert new account as child of the requested parent in 'showForm' page
		response = mockMvc.perform(post("/account")
				.session(mockSession)
				.param("name", newAccountName)
				.param("description", newAccountDescription)
				.param("parentAccount.id", parentId.toString())
			);
		response
			.andExpect(status().isMovedTemporarily())
			.andExpect(MockMvcResultMatchers.redirectedUrl(expectedRedirectedUrl));
	}
	
	@Test
	public void shouldFailIfParentIdNotInformed() throws Exception {
		
		final String accountName = "NewAccount";
		final String accountDescription ="short description";
		
		ResultActions response = mockMvc.perform(post("/account")
				.session(mockSession)
				.accept(MediaType.APPLICATION_JSON)
				.param("name", accountName)
				.param("description", accountDescription)
			);
		
		response
			.andExpect(status().isOk())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$.success").value(false));
		
		new WebResponseChecker(response, mockSession)
			.assertOperationFail()
			.assertErrorMessageIsPresent(ValidationMessages.PARENT_ACCOUNT_REQUIRED);
		
	}
	
	@Test
	public void shouldFailIfhasErrorsWhenCreatenewAccount_NoJS() throws Exception {
		
		final String accountName = "NewAccount";
		final String accountDescription ="short description";
		
		ResultActions response = mockMvc.perform(post("/account")
				.session(mockSession)
				.param("name", accountName)
				.param("description", accountDescription)
			);
		
		String expectedViewName = "mobile-newAccountForm";
		ModelAndView mav = response.andExpect(status().isOk())
				.andExpect(MockMvcResultMatchers.view().name(expectedViewName))
				.andReturn()
				.getModelAndView();
		AccountOperationResponse aor = (AccountOperationResponse) mav.getModelMap().get("response");
		Assert.assertFalse(aor.isSuccess(), "success message shuould be false");
		
	}
}
