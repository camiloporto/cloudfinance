package br.com.camiloporto.cloudfinance.web;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

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
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.ModelAndView;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import br.com.camiloporto.cloudfinance.AbstractCloudFinanceDatabaseTest;
import br.com.camiloporto.cloudfinance.builders.TransactionControllerTestHelper;
import br.com.camiloporto.cloudfinance.builders.WebUserManagerOperationBuilder;
import br.com.camiloporto.cloudfinance.checkers.WebResponseChecker;
import br.com.camiloporto.cloudfinance.i18n.ValidationMessages;
import br.com.camiloporto.cloudfinance.model.Account;
import br.com.camiloporto.cloudfinance.model.AccountTransaction;
import br.com.camiloporto.cloudfinance.service.AccountManager;
import br.com.camiloporto.cloudfinance.service.TransactionManager;

import com.jayway.jsonpath.JsonPath;

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
				.accept(MediaType.APPLICATION_JSON)
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
				.accept(MediaType.APPLICATION_JSON)
				.locale(new Locale("pt", "BR"))
			);
		
		response
			.andExpect(status().isOk())
			.andExpect(content().contentType(MediaTypeApplicationJsonUTF8.APPLICATION_JSON_UTF8_VALUE));
		
		String json = response.andReturn().getResponse().getContentAsString();
		
		Integer newTransactionId = JsonPath.read(json, "$.transaction.id");
		AccountTransaction transaction = transactionManager.findAccountTransaction(new Long(newTransactionId));
		Assert.assertNotNull(transaction, "transaction should not be null");
	}
	
	@Test
	public void shouldAddNewTransaction_NoJS() throws Exception {
		//Given a created a user and a account hierarchy
		
		String expectedUrl = "/transaction";
		ResultActions response = mockMvc.perform(post("/transaction")
				.session(mockSession)
				.param("originAccountId", originAccount.getId().toString())
				.param("destAccountId", destAccount.getId().toString())
				.param("date", "10/06/2013")
				.param("amount","1250,25")
				.param("description", "transaction description")
				.locale(new Locale("pt", "BR"))
			);
		
		ModelAndView mav = response
			.andExpect(status().isMovedTemporarily())
			.andExpect(redirectedUrl(expectedUrl))
			.andReturn().getModelAndView();
		
		TransactionOperationResponse res = (TransactionOperationResponse) mav.getModelMap().get("response");
		Assert.assertNotNull(res.getTransaction(), "new saved transaction should not be null");
	}
	
	@Test
	public void shouldShowErrorsWhenFailAddNewTransaction_NoJS() throws Exception {
		//Given a created a user and a account hierarchy
		
		String expectedView = "mobile-transactionNewForm";
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
		
		ModelAndView mav = response
			.andExpect(status().isOk())
			.andExpect(view().name(expectedView))
			.andReturn().getModelAndView();
		
		TransactionOperationResponse res = (TransactionOperationResponse) mav.getModelMap().get("response");
		Assert.assertNull(res.getTransaction(), "should not create invalid transaction");
		Assert.assertTrue(res.getErrors().length > 0, "should have errors on response");
	}
	
	@Test
	public void shouldShowFormForNewTransaction_NoJs() throws Exception {
		ResultActions response = mockMvc.perform(get("/transaction/newForm")
				.session(mockSession)
			);
		String expectedViewName = "mobile-transactionNewForm";
		ModelAndView mav = response
			.andExpect(status().isOk())
			.andExpect(view().name(expectedViewName))
			.andReturn().getModelAndView();
		TransactionOperationResponse tor = (TransactionOperationResponse) mav.getModelMap().get("response");
		Assert.assertNotNull(tor.getOriginAccountList(), "origin account list attribute not found on response");
		Assert.assertNotNull(tor.getDestAccountList(), "dest account list attribute not found on response");
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
				.accept(MediaType.APPLICATION_JSON)
				.locale(new Locale("pt", "BR"))
			);
		
		response
			.andExpect(status().isOk())
			.andExpect(content().contentType(MediaTypeApplicationJsonUTF8.APPLICATION_JSON_UTF8_VALUE))
			.andExpect(jsonPath("$.success").value(false));
	
		new WebResponseChecker(response, mockSession)
			.assertOperationFail()
			.assertErrorMessageIsPresent(ValidationMessages.ORIGIN_ACCOUNT_REQUIRED);
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
				.accept(MediaType.APPLICATION_JSON)
			);
		
		response
			.andExpect(status().isOk())
			.andExpect(content().contentType(MediaTypeApplicationJsonUTF8.APPLICATION_JSON_UTF8_VALUE))
			.andExpect(jsonPath("$.success").value(true));
		
		String json = response.andReturn().getResponse().getContentAsString();
		
		JSONArray transactionDescs = JsonPath.read(json, "$.transactions[*].origin.comment");
		int expectedResultCount = 2;
		Assert.assertEquals(transactionDescs.size(), expectedResultCount, "result count did not match");
	}
	
	//request by static html, with No JavaScript enabled (no json, so)
	@Test
	public void shouldGetTransactionsByTimeInterval_NoJS() throws Exception {
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
		
		int expectedResultCount = 2;
		String expectedViewName = "mobile-transaction";
		ModelAndView mav = response
			.andExpect(status().isOk())
			.andExpect(MockMvcResultMatchers.view().name(expectedViewName))
			.andReturn().getModelAndView();
		
		TransactionOperationResponse res = (TransactionOperationResponse) mav.getModelMap().get("response");
		Assert.assertEquals(res.getTransactions().size(), expectedResultCount, "result count did not match");
	}
	
	@Test
	public void shouldFailWhenGetTransactionsByTimeIntervalThrowsError() throws Exception {
		
		//begin date greater than end date
		ResultActions response = mockMvc.perform(get("/transaction")
				.session(mockSession)
				.param("begin", "16/06/2013")
				.param("end", "14/06/2013")
				.accept(MediaType.APPLICATION_JSON)
			);
		
		response
			.andExpect(status().isOk())
			.andExpect(content().contentType(MediaTypeApplicationJsonUTF8.APPLICATION_JSON_UTF8_VALUE))
			.andExpect(jsonPath("$.success").value(false));
		
		new WebResponseChecker(response, mockSession)
			.assertOperationFail()
			.assertErrorMessageIsPresent(ValidationMessages.BEGIN_DATE_GREATER_THAN_END_DATE);
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
				.accept(MediaType.APPLICATION_JSON)
			);
		
		String json = response.andReturn().getResponse().getContentAsString();
		Integer txId = JsonPath.read(json, "$.transactions[0].id");
		
		response = mockMvc.perform(post("/transaction/delete")
				.session(mockSession)
				.param("id", txId.toString())
			);
		
		response
			.andExpect(status().isOk())
			.andExpect(content().contentType(MediaTypeApplicationJsonUTF8.APPLICATION_JSON_UTF8_VALUE))
			.andExpect(jsonPath("$.success").value(true));
		
		Assert.assertNull(transactionManager.findAccountTransaction(new Long(txId)), "transaction '" + txId + "' was not deleted");
	}
	
	@Test
	public void shouldFailWhenGeDeleteTransactionsThrowsError() throws Exception {

		//no transaction id informed for delete
		ResultActions response = mockMvc.perform(post("/transaction/delete")
				.session(mockSession)
				.accept(MediaType.APPLICATION_JSON)
			);
		
		response
			.andExpect(status().isOk())
			.andExpect(content().contentType(MediaTypeApplicationJsonUTF8.APPLICATION_JSON_UTF8_VALUE))
			.andExpect(jsonPath("$.success").value(false));
		
		new WebResponseChecker(response, mockSession)
			.assertOperationFail()
			.assertErrorMessageIsPresent(ValidationMessages.TRANSACTION_ID_REQUIRED);
	}
	
}
