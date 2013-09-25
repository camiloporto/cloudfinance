package br.com.camiloporto.cloudfinance.web;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
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
import org.springframework.web.servlet.ModelAndView;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import br.com.camiloporto.cloudfinance.AbstractCloudFinanceDatabaseTest;
import br.com.camiloporto.cloudfinance.builders.WebUserManagerOperationBuilder;
import br.com.camiloporto.cloudfinance.checkers.WebResponseChecker;
import br.com.camiloporto.cloudfinance.helpers.DataInsertionHelper;
import br.com.camiloporto.cloudfinance.i18n.ValidationMessages;
import br.com.camiloporto.cloudfinance.model.Account;
import br.com.camiloporto.cloudfinance.model.AccountNode;
import br.com.camiloporto.cloudfinance.model.Profile;
import br.com.camiloporto.cloudfinance.service.AccountManager;
import br.com.camiloporto.cloudfinance.service.TransactionManager;
import br.com.camiloporto.cloudfinance.service.impl.BalanceSheet;
import br.com.camiloporto.cloudfinance.service.utils.DateUtils;

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
				.accept(MediaType.APPLICATION_JSON)
			);
		String json = response.andReturn().getResponse().getContentAsString();
		rootAccountId = JsonPath.read(json, "$.rootAccounts[0].id");
		
		AccountNode rootBranch = accountManager.getAccountBranch(profile, new Long(rootAccountId));
		incomes = getByName(rootBranch.getChildren(), Account.INCOME_NAME);
		bank = getByName(rootBranch.getChildren(), Account.ASSET_NAME);
		outgoings = getByName(rootBranch.getChildren(), Account.OUTGOING_NAME);
		
		accountInsertionHelper = new DataInsertionHelper(rootBranch.getAccount());
		accountInsertionHelper.insertAccountsFromFile(profile, DataInsertionHelper.ACCOUNT_DATA);
		accountInsertionHelper.insertTransactionsFromFile(profile, DataInsertionHelper.TRANSACTION_DATA);
		
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
				.accept(MediaType.APPLICATION_JSON)
			);
		
		response
			.andExpect(status().isOk())
			.andExpect(content().contentType(MediaTypeApplicationJsonUTF8.APPLICATION_JSON_UTF8))
			.andExpect(jsonPath("$.success").value(true));
		
		String json = response.andReturn().getResponse().getContentAsString();
		Double balanceBefore = JsonPath.read(json, "$.accountStatement.balanceBeforeInterval");
		Assert.assertTrue(new BigDecimal("1000.0").compareTo(new BigDecimal(balanceBefore)) == 0, "balance before did not match");
	}
	
	@Test
	public void shouldGetBalanceSheet() throws Exception {
		ResultActions response = mockMvc.perform(get("/report/balancesheet")
				.session(mockSession)
				.param("date", "20/09/2013")
				.accept(MediaType.APPLICATION_JSON)
			);
		
		response
			.andExpect(status().isOk())
			.andExpect(content().contentType(MediaTypeApplicationJsonUTF8.APPLICATION_JSON_UTF8))
			.andExpect(jsonPath("$.success").value(true));
		
		String json = response.andReturn().getResponse().getContentAsString();
		Double assetBalance = JsonPath.read(json, "$.balanceSheet.assetBalanceSheetTree.balance");
		Assert.assertTrue(new BigDecimal("1200.0").compareTo(new BigDecimal(assetBalance)) == 0, "asset balance did not match");
	}
	
	@Test
	public void shouldGetBalanceSheet_NoJs() throws Exception {
		ResultActions response = mockMvc.perform(get("/report/balanceSheet")
				.session(mockSession)
				.param("date", "20/09/2013")
			);
		
		ModelAndView modelAndView = response
			.andExpect(status().isOk())
			.andReturn().getModelAndView();
		
		ReportOperationResponse ror = (ReportOperationResponse) modelAndView.getModelMap().get("response");
		BalanceSheet balance = ror.getBalanceSheet();
		Assert.assertNotNull(balance, "balance should not be null");
		Assert.assertNotNull(balance.getBalanceDate(), "balance date should not be null");
	}
	
	@Test
	public void shouldGetAccountStatement_NoJS() throws Exception {
		ResultActions response = mockMvc.perform(get("/report/statement")
				.session(mockSession)
				.param("accountId", bank.getId().toString())
				.param("begin", "12/06/2013")
				.param("end", "14/06/2013")
			);
		
		ModelAndView mav = response
			.andExpect(status().isOk())
			.andReturn().getModelAndView();
		ReportOperationResponse ror = (ReportOperationResponse) mav.getModelMap().get("response");
		
		BigDecimal balanceBefore = ror.getAccountStatement().getBalanceBeforeInterval();
		Assert.assertTrue(new BigDecimal("1000.0").compareTo(balanceBefore) == 0, "balance before did not match");
		
		Assert.assertNotNull(ror, "response object should be present");
		Assert.assertNotNull(ror.getAccountList(), "account list for statement should be present");
		
	}
	
	@Test
	public void shouldGoToAccountStatementHomePage() throws Exception {
		ResultActions response = mockMvc.perform(get("/report/statement")
				.session(mockSession)
			);
		
		ModelAndView mav = response
			.andExpect(status().isOk())
			.andExpect(view().name("mobile-statement"))
			.andReturn().getModelAndView();
		
		ReportOperationResponse ror = (ReportOperationResponse) mav.getModelMap().get("response");
		Assert.assertNotNull(ror, "response object should be present");
		Assert.assertNotNull(ror.getAccountList(), "account list for statement should be present");
	}
	
	@Test
	public void shouldGoToBalanceSheetHomePage() throws Exception {
		ResultActions response = mockMvc.perform(get("/report/balanceSheet")
				.session(mockSession)
			);
		
		response
			.andExpect(status().isOk())
			.andExpect(view().name("mobile-balanceSheet"))
			.andReturn().getModelAndView();
		
	}
	
	@Test
	public void shouldFailIfErrorOccurWhenGetAccountStatement() throws Exception {
		final String emptyAccountId = "";
		
		ResultActions response = mockMvc.perform(get("/report/statement")
				.session(mockSession)
				.param("accountId", emptyAccountId)
				.param("begin", "12/06/2013")
				.param("end", "14/06/2013")
				.accept(MediaType.APPLICATION_JSON)
			);
		
		response
			.andExpect(status().isOk())
			.andExpect(content().contentType(MediaTypeApplicationJsonUTF8.APPLICATION_JSON_UTF8))
			.andExpect(jsonPath("$.success").value(false));
		
		new WebResponseChecker(response, mockSession)
			.assertOperationFail()
			.assertErrorMessageIsPresent(ValidationMessages.ACCOUNT_REQUIRED);
	}
	
	@Test
	public void shouldSetDefaultDateIfNoneInformedOnGetBalanceSheet() throws Exception {
		//no date informed for balance sheet. should set current date
		ResultActions response = mockMvc.perform(get("/report/balancesheet")
				.session(mockSession)
				.accept(MediaType.APPLICATION_JSON)
			);
		
		response
			.andExpect(status().isOk())
			.andExpect(content().contentType(MediaTypeApplicationJsonUTF8.APPLICATION_JSON_UTF8))
			.andExpect(jsonPath("$.success").value(true));
		
		String json = response.andReturn().getResponse().getContentAsString();
		Object balanceDate = JsonPath.read(json, "$.balanceSheet.balanceDate");
		Assert.assertNotNull(balanceDate, "default date not setted");
	}
}
