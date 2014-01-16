package br.com.camiloporto.cloudfinance.web;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.Locale;

import net.minidev.json.JSONArray;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.servlet.ModelAndView;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import br.com.camiloporto.cloudfinance.AbstractWebMvcCloudFinanceTest;
import br.com.camiloporto.cloudfinance.builders.TransactionControllerTestHelper;
import br.com.camiloporto.cloudfinance.builders.WebUserManagerOperationBuilder;
import br.com.camiloporto.cloudfinance.checkers.CustomMockMvcResultMatchers;
import br.com.camiloporto.cloudfinance.checkers.WebResponseChecker;
import br.com.camiloporto.cloudfinance.i18n.ValidationMessages;
import br.com.camiloporto.cloudfinance.model.Account;
import br.com.camiloporto.cloudfinance.model.AccountTransaction;

import com.jayway.jsonpath.JsonPath;

public class TransactionControllerTest extends AbstractWebMvcCloudFinanceTest {
	
	private Account originAccount;
	private Account destAccount;
	private Integer rootAccountId;
	
	@BeforeMethod
    public void setup() throws Exception {
		super.init();
		cleanUserData();
		
        final String userName ="some@email.com";
		final String userPass ="1234";
		final String userConfirmPass ="1234";
		new WebUserManagerOperationBuilder(mockMvc, mockSession)
			.signup(userName, userPass, userConfirmPass);
		
		ResultActions response = mockMvc.perform(prepareHtmlPostRequest("/user/login", mockSession)
				.param("userName", userName)
				.param("pass", userPass)
			);
		
		authenticatedSession = response.andReturn().getRequest().getSession();
		
		
		response = mockMvc.perform(prepareJsonGetRequest("/account/roots", (MockHttpSession) authenticatedSession));
		
		String json = response.andReturn().getResponse().getContentAsString();
		rootAccountId = JsonPath.read(json, "$.accountSystems[0].rootAccount.id");
		JsonPath.read(json, "$.accountSystems[0].id");
		
		response = mockMvc.perform(prepareJsonGetRequest("/account/tree/" + rootAccountId, (MockHttpSession) authenticatedSession));
		
		json = response.andReturn().getResponse().getContentAsString();
		
		JSONArray accounts = JsonPath.read(json, "$.accountTree.children[*].account");
		setIncomeAccount(accounts);
		setAssetAccount(accounts);
		
    }
	
	private void setAssetAccount(JSONArray accounts) {
		for (Object account : accounts) {
			String name = JsonPath.read(account, "name");
			if(name.equalsIgnoreCase(Account.ASSET_NAME)) {
				this.destAccount = createAccountFromJson(account);
			}
		}
		
	}

	private void setIncomeAccount(JSONArray accounts) {
		for (Object account : accounts) {
			String name = JsonPath.read(account, "name");
			if(name.equalsIgnoreCase(Account.INCOME_NAME)) {
				this.originAccount = createAccountFromJson(account);
			}
		}
	}

	private Account createAccountFromJson(Object account) {
		Account a = new Account("", new Account());
		a.setId(new Long((Integer) JsonPath.read(account, "id")));
		return a;
	}

	
	@Test
	public void shouldAddNewTransaction() throws Exception {
		//Given a created a user and a account hierarchy
		
		ResultActions response = mockMvc.perform(prepareJsonPostRequest("/transaction", (MockHttpSession) authenticatedSession)
				.param("originAccountId", originAccount.getId().toString())
				.param("destAccountId", destAccount.getId().toString())
				.param("date", "10/06/2013")
				.param("amount","1250,25")
				.param("description", "transaction description")
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
		ResultActions response = mockMvc.perform(prepareHtmlPostRequest("/transaction",  (MockHttpSession) authenticatedSession)
				.param("originAccountId", originAccount.getId().toString())
				.param("destAccountId", destAccount.getId().toString())
				.param("date", "10/06/2013")
				.param("amount","1250,25")
				.param("description", "transaction description")
				.locale(new Locale("pt", "BR"))
			);
		
		ModelAndView mav = response
			.andExpect(status().isMovedTemporarily())
			.andExpect(CustomMockMvcResultMatchers.redirectUrlStartsWith(expectedUrl))
			.andReturn().getModelAndView();
		
		TransactionOperationResponse res = (TransactionOperationResponse) mav.getModelMap().get("response");
		Assert.assertNotNull(res.getTransaction(), "new saved transaction should not be null");
	}
	
	@Test
	public void shouldShowErrorsWhenFailAddNewTransaction_NoJS() throws Exception {
		//Given a created a user and a account hierarchy
		
		String expectedView = "mobile-transactionNewForm";
		String invalidOriginAccount = "";
		ResultActions response = mockMvc.perform(prepareHtmlPostRequest("/transaction",  (MockHttpSession) authenticatedSession)
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
		ResultActions response = mockMvc.perform(prepareHtmlGetRequest("/transaction/newForm", (MockHttpSession) authenticatedSession));
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
		
		ResultActions response = mockMvc.perform(prepareJsonPostRequest("/transaction",  (MockHttpSession) authenticatedSession)
				.param("originAccountId", invalidOriginAccount)
				.param("destAccountId", destAccount.getId().toString())
				.param("date", "10/06/2013")
				.param("amount","1250,25")
				.param("description", "transaction description")
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
	public void shouldValidateNumericConversionWhenAddNewTransaction() throws Exception {
		//Given a created a user and a account hierarchy
		String invalidNumeric = "abc";
		
		ResultActions response = mockMvc.perform(prepareHtmlPostRequest("/transaction",  (MockHttpSession) authenticatedSession)
				.param("originAccountId", originAccount.getId().toString())
				.param("destAccountId", destAccount.getId().toString())
				.param("date", "10/06/2013")
				.param("amount",invalidNumeric)
				.param("description", "transaction description")
			);
		
		ModelAndView mav = response
			.andExpect(status().isOk())
			.andReturn().getModelAndView();
		
		TransactionOperationResponse tor = (TransactionOperationResponse) mav.getModelMap().get("response");
		Assert.assertFalse(tor.isSuccess(), "add transaction should not succeed with invalid amount number");
		Assert.assertEquals(tor.getErrors()[0], "O valor da transação deve ser um número válido");
	}
	
	@Test
	public void shouldValidateDateFieldConversionWhenAddNewTransaction() throws Exception {
		//Given a created a user and a account hierarchy
		String invalidDate = "abc";
		
		ResultActions response = mockMvc.perform(prepareHtmlPostRequest("/transaction",  (MockHttpSession) authenticatedSession)
				.param("originAccountId", originAccount.getId().toString())
				.param("destAccountId", destAccount.getId().toString())
				.param("date", invalidDate)
				.param("amount","1250,50")
				.param("description", "transaction description")
			);
		
		ModelAndView mav = response
			.andExpect(status().isOk())
			.andReturn().getModelAndView();
		
		TransactionOperationResponse tor = (TransactionOperationResponse) mav.getModelMap().get("response");
		Assert.assertFalse(tor.isSuccess(), "add transaction should not succeed with invalid amount number");
		Assert.assertEquals(tor.getErrors()[0], "Informe uma data válida para a transação");
	}
	
	@Test
	public void shouldGetTransactionsByTimeInterval() throws Exception {
		new TransactionControllerTestHelper(mockMvc, (MockHttpSession) authenticatedSession)
			.addTransaction(
					originAccount.getId().toString(),
					destAccount.getId().toString(),
					"10/06/2013",
					"1250,25",
					"t1")
			.addTransaction(
					originAccount.getId().toString(),
					destAccount.getId().toString(),
					"12/06/2013",
					"75,25",
					"t2")
			.addTransaction(
					originAccount.getId().toString(),
					destAccount.getId().toString(),
					"14/06/2013",
					"175,25",
					"t3")
			.addTransaction(
					originAccount.getId().toString(),
					destAccount.getId().toString(),
					"16/06/2013",
					"575,25",
					"t4");
		
		ResultActions response = mockMvc.perform(prepareJsonGetRequest("/transaction",  (MockHttpSession) authenticatedSession)
				.param("begin", "12/06/2013")
				.param("end", "14/06/2013")
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
		new TransactionControllerTestHelper(mockMvc, (MockHttpSession) authenticatedSession)
			.addTransaction(
					originAccount.getId().toString(),
					destAccount.getId().toString(),
					"10/06/2013",
					"1250,25",
					"t1")
			.addTransaction(
					originAccount.getId().toString(),
					destAccount.getId().toString(),
					"12/06/2013",
					"75,25",
					"t2")
			.addTransaction(
					originAccount.getId().toString(),
					destAccount.getId().toString(),
					"14/06/2013",
					"175,25",
					"t3")
			.addTransaction(
					originAccount.getId().toString(),
					destAccount.getId().toString(),
					"16/06/2013",
					"575,25",
					"t4");
		
		ResultActions response = mockMvc.perform(prepareHtmlGetRequest("/transaction", (MockHttpSession) authenticatedSession)
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
		ResultActions response = mockMvc.perform(prepareJsonGetRequest("/transaction", (MockHttpSession) authenticatedSession)
				.param("begin", "16/06/2013")
				.param("end", "14/06/2013")
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
		new TransactionControllerTestHelper(mockMvc, (MockHttpSession) authenticatedSession)
			.addTransaction(
				originAccount.getId().toString(),
				destAccount.getId().toString(),
				"10/06/2013",
				"1250,25",
				"t1")
			.addTransaction(
				originAccount.getId().toString(),
				destAccount.getId().toString(),
				"12/06/2013",
				"75,25",
				"t2");
		
		ResultActions response = mockMvc.perform(prepareJsonGetRequest("/transaction", (MockHttpSession) authenticatedSession)
				.param("begin", "10/06/2013")
				.param("end", "10/06/2013")
			);
		
		String json = response.andReturn().getResponse().getContentAsString();
		Integer txId = JsonPath.read(json, "$.transactions[0].id");
		
		response = mockMvc.perform(prepareJsonPostRequest("/transaction/delete", (MockHttpSession) authenticatedSession)
				.param("id", txId.toString())
			);
		
		response
			.andExpect(status().isOk())
			.andExpect(content().contentType(MediaTypeApplicationJsonUTF8.APPLICATION_JSON_UTF8_VALUE))
			.andExpect(jsonPath("$.success").value(true));
		
		Assert.assertNull(transactionManager.findAccountTransaction(new Long(txId)), "transaction '" + txId + "' was not deleted");
	}
	
	@Test
	public void shouldRemoveTransaction_NoJS() throws Exception {
		new TransactionControllerTestHelper(mockMvc, (MockHttpSession) authenticatedSession)
			.addTransaction(
				originAccount.getId().toString(),
				destAccount.getId().toString(),
				"10/06/2013",
				"1250,25",
				"t1")
			.addTransaction(
				originAccount.getId().toString(),
				destAccount.getId().toString(),
				"12/06/2013",
				"75,25",
				"t2");
		
		ResultActions response = mockMvc.perform(prepareJsonGetRequest("/transaction", (MockHttpSession) authenticatedSession)
				.param("begin", "10/06/2013")
				.param("end", "10/06/2013")
			);
		
		String json = response.andReturn().getResponse().getContentAsString();
		Integer txId = JsonPath.read(json, "$.transactions[0].id");
		
		response = mockMvc.perform(prepareHtmlPostRequest("/transaction/delete", (MockHttpSession) authenticatedSession)
				.param("transactionId", txId.toString())
			);
		
		String expectedUrl = "/transaction";
		ModelAndView mav = response
			.andExpect(status().isMovedTemporarily())
			.andExpect(CustomMockMvcResultMatchers.redirectUrlStartsWith(expectedUrl))
			.andReturn().getModelAndView();
		
		Assert.assertNull(transactionManager.findAccountTransaction(new Long(txId)), "transaction '" + txId + "' was not deleted");
	}
	
	@Test
	public void shouldShowTransactionDetail_NoJS() throws Exception {
		new TransactionControllerTestHelper(mockMvc, (MockHttpSession) authenticatedSession)
			.addTransaction(
				originAccount.getId().toString(),
				destAccount.getId().toString(),
				"10/06/2013",
				"1250,25",
				"t1");
		
		ResultActions response = mockMvc.perform(prepareJsonGetRequest("/transaction", (MockHttpSession) authenticatedSession)
				.param("begin", "10/06/2013")
				.param("end", "10/06/2013")
			);
		
		String json = response.andReturn().getResponse().getContentAsString();
		Integer txId = JsonPath.read(json, "$.transactions[0].id");
		
		response = mockMvc.perform(prepareHtmlGetRequest("/transaction/" + txId.toString(), (MockHttpSession) authenticatedSession));
		
		ModelAndView mav = response
			.andExpect(status().isOk())
			.andReturn().getModelAndView();
		TransactionOperationResponse tor = (TransactionOperationResponse) mav.getModelMap().get("response");
		Assert.assertNotNull(tor.getTransaction(), "transaction attribute should be present");
	}
	
	@Test
	public void shouldFailWhenGeDeleteTransactionsThrowsError() throws Exception {

		//no transaction id informed for delete
		ResultActions response = mockMvc.perform(prepareJsonPostRequest("/transaction/delete", (MockHttpSession) authenticatedSession));
		
		response
			.andExpect(status().isOk())
			.andExpect(content().contentType(MediaTypeApplicationJsonUTF8.APPLICATION_JSON_UTF8_VALUE))
			.andExpect(jsonPath("$.success").value(false));
		
		new WebResponseChecker(response, mockSession)
			.assertOperationFail()
			.assertErrorMessageIsPresent(ValidationMessages.TRANSACTION_ID_REQUIRED);
	}
	
}
