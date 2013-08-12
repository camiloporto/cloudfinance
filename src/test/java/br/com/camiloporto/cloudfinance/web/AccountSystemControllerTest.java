package br.com.camiloporto.cloudfinance.web;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.UUID;

import net.minidev.json.JSONArray;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
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
			);
		String json = response.andReturn().getResponse().getContentAsString();
		rootAccountId = JsonPath.read(json, "$.rootAccounts[0].id");
    }
	
	@Test
	public void shouldGetUsersRootAccounts() throws Exception {
		
		ResultActions response = mockMvc.perform(get("/account/roots")
				.session(mockSession)
			);
		
		response
			.andExpect(status().isOk())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$.rootAccounts").exists());
		
		String json = response.andReturn().getResponse().getContentAsString();
		JSONArray accounts = JsonPath.read(json, "$.rootAccounts");
		
		final int EXPECTED_ACCOUNT_COUNTS = 1;
		Assert.assertEquals(accounts.size(), EXPECTED_ACCOUNT_COUNTS, "accounts count not match");
		Assert.assertNotNull(JsonPath.read(json, "$.rootAccounts[0].id"), "id of root account should not be null");
		
		//XXX Improve data transfer excluding properties not worth for operation requested
		
//		Assert.assertNull(JsonPath.read(json, "$.rootAccounts[0].version"), "trash properties should not be included");
	}
	
	//FIXME fazer testes com possibilidade de falhas da consulta de contas raiz. Se usuario nao logado???
	
	@Test
	public void shouldGetRootAccountWholeTree() throws Exception {
		
		ResultActions response = mockMvc.perform(get("/account/tree/" + rootAccountId)
				.session(mockSession)
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
	public void shouldGetEmptyTreeIfAccountIdDoNotExists() throws Exception {
		
		ResultActions response = mockMvc.perform(get("/account/tree/9999")
				.session(mockSession)
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
			);
		
		String json = response.andReturn().getResponse().getContentAsString();
		
		final String accountName = "NewAccount";
		final String accountDescription ="short description";
		
		final Integer parentId = JsonPath.read(json, "$.accountTree.children[0].account.id");
		response = mockMvc.perform(post("/account")
				.session(mockSession)
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
	public void shouldFailIfParentIdNotInformed() throws Exception {
		
		final String accountName = "NewAccount";
		final String accountDescription ="short description";
		
		ResultActions response = mockMvc.perform(post("/account")
				.session(mockSession)
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
}
