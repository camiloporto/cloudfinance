package br.com.camiloporto.cloudfinance.security;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import java.util.Locale;

import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.ResultActions;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import br.com.camiloporto.cloudfinance.AbstractWebMvcCloudFinanceTest;
import br.com.camiloporto.cloudfinance.builders.TransactionControllerTestHelper;
import br.com.camiloporto.cloudfinance.builders.WebUserManagerOperationBuilder;
import br.com.camiloporto.cloudfinance.model.Account;
import br.com.camiloporto.cloudfinance.model.AccountSystem;
import br.com.camiloporto.cloudfinance.model.Profile;

import com.jayway.jsonpath.JsonPath;

public class TransactionAccessControlTest extends AbstractWebMvcCloudFinanceTest {

	private final String username = "oneuser@email.com";
	private final String password = "onepass";

	private final String otherUsername = "otheruser@email.com";
	private final String otherPassword = "otherpass";
	
	private Long rootAccountId;

	private Long otherRootAccountId;
	private Account otherIncome;
	private Account otherAsset;
	private Account income;
	private Account asset;
	
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
		rootAccountId = new Long((Integer) JsonPath.read(json, "$.accountSystems[0].rootAccount.id"));
		
		Profile other = profileRepository.findByUserId(otherUsername);
		List<AccountSystem> accountSystem = accountSystemRepository.findByUserProfile(other);
		otherRootAccountId = accountSystem.get(0).getRootAccount().getId();
		
		Account otherRootAccount = accountRepository.findOne(otherRootAccountId);
		otherIncome = accountRepository.findByNameAndRootAccount(Account.INCOME_NAME, otherRootAccount);
		otherAsset = accountRepository.findByNameAndRootAccount(Account.ASSET_NAME, otherRootAccount);
		
		Account rootAccount = accountRepository.findOne(rootAccountId);
		income = accountRepository.findByNameAndRootAccount(Account.INCOME_NAME, rootAccount);
		asset = accountRepository.findByNameAndRootAccount(Account.ASSET_NAME, rootAccount);
		
    }

	@Test
	public void onlyTheOwnerOfInvolvedAccountCanInsertTransactionsBetweenAccounts() throws Exception {
		
		//logged user trying to insert transaction involving other user accounts
		ResultActions response = mockMvc.perform(prepareJsonPostRequest("/transaction", (MockHttpSession) authenticatedSession)
				.param("originAccountId", otherIncome.getId().toString())
				.param("destAccountId", otherAsset.getId().toString())
				.param("date", "10/06/2013")
				.param("amount","1250,25")
				.param("description", "transaction description")
				.locale(new Locale("pt", "BR"))
			);
		
		response
			.andExpect(status().isUnauthorized());
	}
	
	@Test
	public void oneUserCouldNotDeleteOtherUserTransactions() throws Exception {
		new TransactionControllerTestHelper(mockMvc, (MockHttpSession) authenticatedSession)
			.addTransaction(
				income.getId().toString(),
				asset.getId().toString(),
				"10/06/2013",
				"1250,25",
				"t1")
			.addTransaction(
					income.getId().toString(),
					asset.getId().toString(),
				"12/06/2013",
				"75,25",
				"t2");
		
		ResultActions response = mockMvc.perform(prepareJsonGetRequest("/transaction", (MockHttpSession) authenticatedSession)
				.param("begin", "10/06/2013")
				.param("end", "10/06/2013")
			);
		
		String json = response.andReturn().getResponse().getContentAsString();
		Integer txId = JsonPath.read(json, "$.transactions[0].id");
		
		//logoff current user
		mockMvc.perform(post("/user/logoff")
				.session((MockHttpSession) authenticatedSession)
				.accept(MediaType.APPLICATION_JSON)
			);
		
		//login other user
		mockMvc.perform(post("/user/login").param("userName", otherUsername).param("pass", otherPassword).accept(MediaType.APPLICATION_JSON));
        authenticatedSession = response.andReturn().getRequest().getSession();
		
		//other user trying to delete first user transaction
		response = mockMvc.perform(prepareJsonPostRequest("/transaction/delete", (MockHttpSession) authenticatedSession)
				.param("id", txId.toString())
			);
		
		response
			.andExpect(status().isUnauthorized());
	}
	
}
