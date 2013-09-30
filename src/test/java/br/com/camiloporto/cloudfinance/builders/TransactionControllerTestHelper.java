package br.com.camiloporto.cloudfinance.builders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import java.util.Locale;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;

import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import br.com.camiloporto.cloudfinance.model.Account;

import com.jayway.jsonpath.JsonPath;

public class TransactionControllerTestHelper {

	private MockMvc mockMvc;
	private MockHttpSession mockSession;
	
	private Account incomeAccount;
	private Account assetAccount;
	
	public TransactionControllerTestHelper(MockMvc mockMvc,
			MockHttpSession mockSession) {
				this.mockMvc = mockMvc;
				this.mockSession = mockSession;
		
	}

	public TransactionControllerTestHelper signUpAndLogUser(String userName, String userPass,
			String userConfirmPass) throws Exception {
		new WebUserManagerOperationBuilder(mockMvc, mockSession)
			.signup(userName, userPass, userConfirmPass)
			.login(userName, userPass);
		
		ResultActions response = mockMvc.perform(get("/account/roots")
				.session(mockSession)
				.accept(MediaType.APPLICATION_JSON)
			);
		String json = response.andReturn().getResponse().getContentAsString();
		
		Integer rootAccountId = JsonPath.read(json, "$.accountSystems[0].rootAccount.id");
		
		response = mockMvc.perform(get("/account/tree/" + rootAccountId)
				.session(mockSession)
				.accept(MediaType.APPLICATION_JSON)
			);
		
		json = response.andReturn().getResponse().getContentAsString();
		
		JSONArray accounts = JsonPath.read(json, "$.accountTree.children[*].account");
		setIncomeAccount(accounts);
		setAssetAccount(accounts);
		
		
		return this;
		
	}

	private void setAssetAccount(JSONArray accounts) {
		for (Object account : accounts) {
			String name = JsonPath.read(account, "name");
			if(name.equalsIgnoreCase(Account.ASSET_NAME)) {
				this.assetAccount = createAccountFromJson(account);
			}
		}
		
	}

	private void setIncomeAccount(JSONArray accounts) {
		for (Object account : accounts) {
			String name = JsonPath.read(account, "name");
			if(name.equalsIgnoreCase(Account.INCOME_NAME)) {
				this.incomeAccount = createAccountFromJson(account);
			}
		}
	}

	private Account createAccountFromJson(Object account) {
		Account a = new Account("", new Account());
		a.setId(new Long((Integer) JsonPath.read(account, "id")));
		return a;
	}

	public Account getLoggedUserIncomeAccount() {
		return this.incomeAccount;
	}

	public Account getLoggedUserAssetAccount() {
		return this.assetAccount;
	}

	public TransactionControllerTestHelper addTransaction(String originId, String destId, String date,
			String amount, String desc) throws Exception {
		mockMvc.perform(post("/transaction")
				.session(mockSession)
				.param("originAccountId", originId)
				.param("destAccountId", destId)
				.param("date", date)
				.param("amount",amount)
				.param("description", desc)
				.locale(new Locale("pt", "BR"))
			);
		return this;
	}

}
