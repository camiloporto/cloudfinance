package br.com.camiloporto.cloudfinance.web;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;
import java.util.Locale;
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

import com.jayway.jsonpath.JsonPath;

import br.com.camiloporto.cloudfinance.AbstractCloudFinanceDatabaseTest;
import br.com.camiloporto.cloudfinance.builders.TransactionControllerTestHelper;
import br.com.camiloporto.cloudfinance.builders.WebUserManagerOperationBuilder;
import br.com.camiloporto.cloudfinance.checkers.WebResponseChecker;
import br.com.camiloporto.cloudfinance.model.Account;
import br.com.camiloporto.cloudfinance.model.AccountTransaction;
import br.com.camiloporto.cloudfinance.model.Profile;
import br.com.camiloporto.cloudfinance.service.AccountManager;
import br.com.camiloporto.cloudfinance.service.TransactionManager;

@ContextConfiguration(locations = {"classpath:/META-INF/spring/applicationContext*.xml", "classpath:/META-INF/spring/webmvc-*.xml"})
@WebAppConfiguration
public class TransactionControllerTest extends AbstractCloudFinanceDatabaseTest {
	
	@Autowired
	private TransactionManager transactionManager;
	
	@Autowired
	private AccountManager accountManager;
	
	@Autowired
    private WebApplicationContext wac;
	
	private Account originAccount;
	private Account destAccount;
	private Integer rootAccountId;
	
	private MockMvc mockMvc;
    private MockHttpSession mockSession;

	private TransactionControllerTestHelper testHelper;
	
	
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
		
		testHelper = new TransactionControllerTestHelper(mockMvc, mockSession)
			.signUpAndLogUser(userName, userPass, userConfirmPass);
		
		originAccount = testHelper.getLoggedUserIncomeAccount();
		destAccount = testHelper.getLoggedUserAssetAccount();
		
    }

	
	@Test
	public void shouldAddNewTransaction() throws Exception {
		//Given a created a user and a account hierarchy
		
		ResultActions response = mockMvc.perform(post("/transaction")
				.session(mockSession)
				.param("originAccountId", originAccount.getId().toString())
				.param("destAccountId", destAccount.getId().toString())
				.param("date", "10/06/2013")
				.param("amount","1250,25")
				.param("description", "transaction description")
				.locale(new Locale("pt", "BR"))
			);
		
		response
			.andExpect(status().isOk())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON));
		
		String json = response.andReturn().getResponse().getContentAsString();
		
		Integer newTransactionId = JsonPath.read(json, "$.transaction.id");
		AccountTransaction transaction = transactionManager.findAccountTransaction(new Long(newTransactionId));
		Assert.assertNotNull(transaction, "transaction should not be null");
	}
	
	@Test
	public void shouldFailIfErrorOccurWhenAddNewTransaction() throws Exception {
		//Given a created a user and a account hierarchy
		
		String invalidOriginAccount = "";
		
		ResultActions response = mockMvc.perform(post("/transaction")
				.session(mockSession)
				.param("originAccountId", invalidOriginAccount)
				.param("destAccountId", destAccount.getId().toString())
				.param("date", "10/06/2013")
				.param("amount","1250,25")
				.param("description", "transaction description")
				.locale(new Locale("pt", "BR"))
			);
		
		response
			.andExpect(status().isOk())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$.success").value(false));
	
		new WebResponseChecker(response, mockSession)
			.assertOperationFail()
			.assertErrorMessageIsPresent("br.com.camiloporto.cloudfinance.transaction.ORIGIN_ACCOUNT_REQUIRED");
	}
	
	@Test
	public void shouldGetTransactionsByTimeInterval() throws Exception {
		testHelper.addTransaction(
				originAccount.getId().toString(),
				destAccount.getId().toString(),
				"10/06/2013",
				"1250,25",
				"t1");
		
		testHelper.addTransaction(
				originAccount.getId().toString(),
				destAccount.getId().toString(),
				"12/06/2013",
				"75,25",
				"t2");
		
		testHelper.addTransaction(
				originAccount.getId().toString(),
				destAccount.getId().toString(),
				"14/06/2013",
				"175,25",
				"t3");
		
		testHelper.addTransaction(
				originAccount.getId().toString(),
				destAccount.getId().toString(),
				"16/06/2013",
				"575,25",
				"t4");
		
		ResultActions response = mockMvc.perform(get("/transaction")
				.session(mockSession)
				.param("begin", "12/06/2013")
				.param("end", "14/06/2013")
			);
		
		response
			.andExpect(status().isOk())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$.success").value(true));
		
		String json = response.andReturn().getResponse().getContentAsString();
		System.out.println(json);
		
		JSONArray transactionDescs = JsonPath.read(json, "$.transactions[*].origin.comment");
		int expectedResultCount = 2;
		Assert.assertEquals(transactionDescs.size(), expectedResultCount, "result count did not match");
	}
	
	@Test
	public void shouldFailWhenGetTransactionsByTimeIntervalThrowsError() throws Exception {
		
		//begin date greater than end date
		ResultActions response = mockMvc.perform(get("/transaction")
				.session(mockSession)
				.param("begin", "16/06/2013")
				.param("end", "14/06/2013")
			);
		
		response
			.andExpect(status().isOk())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$.success").value(false));
		
		new WebResponseChecker(response, mockSession)
			.assertOperationFail()
			.assertErrorMessageIsPresent("br.com.camiloporto.cloudfinance.transaction.BEGIN_DATE_GREATER_THAN_END_DATE");
	}
	
	@Test
	public void shouldRemoveTransaction() throws Exception {
		testHelper.addTransaction(
				originAccount.getId().toString(),
				destAccount.getId().toString(),
				"10/06/2013",
				"1250,25",
				"t1");
		
		testHelper.addTransaction(
				originAccount.getId().toString(),
				destAccount.getId().toString(),
				"12/06/2013",
				"75,25",
				"t2");
		
		ResultActions response = mockMvc.perform(get("/transaction")
				.session(mockSession)
				.param("begin", "10/06/2013")
				.param("end", "10/06/2013")
			);
		
		String json = response.andReturn().getResponse().getContentAsString();
		Integer txId = JsonPath.read(json, "$.transactions[0].id");
		
		response = mockMvc.perform(post("/transaction/delete")
				.session(mockSession)
				.param("id", txId.toString())
			);
		
		response
			.andExpect(status().isOk())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$.success").value(true));
		
		Assert.assertNull(transactionManager.findAccountTransaction(new Long(txId)), "transaction '" + txId + "' was not deleted");
	}
	
}
