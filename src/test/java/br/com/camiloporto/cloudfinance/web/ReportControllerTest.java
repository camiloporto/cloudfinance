package br.com.camiloporto.cloudfinance.web;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

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
import br.com.camiloporto.cloudfinance.model.Account;
import br.com.camiloporto.cloudfinance.model.AccountNode;
import br.com.camiloporto.cloudfinance.model.Profile;
import br.com.camiloporto.cloudfinance.service.AccountManager;
import br.com.camiloporto.cloudfinance.service.TransactionManager;

import com.jayway.jsonpath.JsonPath;

@ContextConfiguration(locations = {
		"classpath:/META-INF/spring/applicationContext*.xml",
		"classpath:/META-INF/spring/webmvc-*.xml" })
@WebAppConfiguration
public class ReportControllerTest extends AbstractCloudFinanceDatabaseTest {
	
	@Autowired
	private TransactionManager transactionManager;
	
	@Autowired
	private AccountManager accountManager;
	
	@Autowired
    private WebApplicationContext wac;
	
	private Integer rootAccountId;
	
	private MockMvc mockMvc;
    private MockHttpSession mockSession;

	private Account incomes;

	private Account bank;

	private Account outgoings;

	private Profile profile;

	private GregorianCalendar tx01Date;

	private GregorianCalendar tx02Date;

	private GregorianCalendar tx03Date;

	private GregorianCalendar tx04Date;

	private BigDecimal tx01Amount;

	private BigDecimal tx02Amount;

	private BigDecimal tx03Amount;

	private BigDecimal tx04Amount;

	@BeforeMethod
    public void setup() throws Exception {
		cleanUserData();
		
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
        this.mockSession = new MockHttpSession(wac.getServletContext(), UUID.randomUUID().toString());
        
        final String userName ="some@email.com";
		final String userPass ="1234";
		Profile p = new Profile();
		p.setPass(userPass);
		p.setUserId(userName);
		
		profile = super.userProfileManager.signUp(p);
		new WebUserManagerOperationBuilder(mockMvc, mockSession)
			.login(userName, userPass);
		
		ResultActions response = mockMvc.perform(get("/account/roots")
				.session(mockSession)
			);
		String json = response.andReturn().getResponse().getContentAsString();
		rootAccountId = JsonPath.read(json, "$.rootAccounts[0].id");
		
		AccountNode rootBranch = accountManager.getAccountBranch(profile, new Long(rootAccountId));
		incomes = getByName(rootBranch.getChildren(), Account.INCOME_NAME);
		bank = getByName(rootBranch.getChildren(), Account.ASSET_NAME);
		outgoings = getByName(rootBranch.getChildren(), Account.OUTGOING_NAME);
		
		prepareSampleTransactions();
		
    }
	
	Account getByName(List<AccountNode> nodes, String name) {
		for (AccountNode accountNode : nodes) {
			if(accountNode.getAccount().getName().equals(name)) {
				return accountNode.getAccount();
			}
		}
		return null;
	}
	
	private void prepareSampleTransactions() {
		tx01Date = new GregorianCalendar(2013, Calendar.JUNE, 10);
		tx02Date = new GregorianCalendar(2013, Calendar.JUNE, 12);
		tx03Date = new GregorianCalendar(2013, Calendar.JUNE, 14);
		tx04Date = new GregorianCalendar(2013, Calendar.JUNE, 16);
		
		tx01Amount = new BigDecimal("1000");
		
		transactionManager.saveAccountTransaction(
				profile,
				incomes.getId(), 
				bank.getId(), 
				tx01Date.getTime(), tx01Amount, 
				"t1");
		
		tx02Amount = new BigDecimal("200");
		transactionManager.saveAccountTransaction(
				profile,
				bank.getId(), 
				outgoings.getId(), 
				tx02Date.getTime(), tx02Amount, 
				"t2");
		tx03Amount = new BigDecimal("150");
		transactionManager.saveAccountTransaction(
				profile,
				bank.getId(), 
				outgoings.getId(), 
				tx03Date.getTime(), tx03Amount, 
				"t3");
		tx04Amount = new BigDecimal("50");
		transactionManager.saveAccountTransaction(
				profile,
				incomes.getId(), 
				bank.getId(), 
				tx04Date.getTime(), tx04Amount, 
				"t4");
	}
	
	@Test
	public void shouldGetAccountStatement() throws Exception {
		ResultActions response = mockMvc.perform(get("/report/statement")
				.session(mockSession)
				.param("accountId", bank.getId().toString())
				.param("begin", "12/06/2013")
				.param("end", "14/06/2013")
			);
		
		response
			.andExpect(status().isOk())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$.success").value(true));
		
		String json = response.andReturn().getResponse().getContentAsString();
		System.out.println(json);
		Double balanceBefore = JsonPath.read(json, "$.accountStatement.balanceBeforeInterval");
		Assert.assertTrue(new BigDecimal("1000.0").compareTo(new BigDecimal(balanceBefore)) == 0, "balance before did not match");
	}
}
