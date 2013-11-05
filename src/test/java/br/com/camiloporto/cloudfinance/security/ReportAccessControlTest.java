package br.com.camiloporto.cloudfinance.security;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.ResultActions;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import br.com.camiloporto.cloudfinance.AbstractWebMvcCloudFinanceTest;
import br.com.camiloporto.cloudfinance.builders.WebUserManagerOperationBuilder;
import br.com.camiloporto.cloudfinance.model.Account;
import br.com.camiloporto.cloudfinance.model.AccountSystem;
import br.com.camiloporto.cloudfinance.model.Profile;

public class ReportAccessControlTest extends
		AbstractWebMvcCloudFinanceTest {
	
	private Account otherIncome;
	
	private final String username = "oneuser@email.com";
	private final String password = "onepass";

	private final String otherUsername = "otheruser@email.com";
	private final String otherPassword = "otherpass";

	private AccountSystem otherUserAccountSystem;
	
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
		
		Profile other = profileRepository.findByUserId(otherUsername);
		otherUserAccountSystem = accountSystemRepository.findByUserProfile(other).get(0);
		
		Account otherRootAccount = accountRepository.findOne(otherUserAccountSystem.getRootAccount().getId());
		otherIncome = accountRepository.findByNameAndRootAccount(Account.INCOME_NAME, otherRootAccount);
		
	}
	
	@Test
	public void oneUserShouldNotAccessAccountStatementOfOtherUserAccounts() throws Exception {
		
		ResultActions response = mockMvc.perform(prepareJsonGetRequest("/report/statement", (MockHttpSession) authenticatedSession)
				.param("accountId", otherIncome.getId().toString())
				.param("begin", "12/06/2013")
				.param("end", "14/06/2013")
			);
		
		response
			.andExpect(status().isUnauthorized());
	}
	
	@Test
	public void oneUserShouldNotAccessBalanceSheetOfOtherUsers() throws Exception {
		
		//forcing active account system of other user to try to get their balance sheet
		authenticatedSession.setAttribute("activeAccountSystem", otherUserAccountSystem);
		
		ResultActions response = mockMvc.perform(prepareJsonGetRequest("/report/balancesheet", (MockHttpSession) authenticatedSession)
				.param("date", "20/09/2013")
			);
		
		response
			.andExpect(status().isUnauthorized());
	}
	
}
