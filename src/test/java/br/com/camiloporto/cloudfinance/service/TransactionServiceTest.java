package br.com.camiloporto.cloudfinance.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import br.com.camiloporto.cloudfinance.AbstractCloudFinanceDatabaseTest;
import br.com.camiloporto.cloudfinance.builders.ProfileBuilder;
import br.com.camiloporto.cloudfinance.model.Account;
import br.com.camiloporto.cloudfinance.model.AccountNode;
import br.com.camiloporto.cloudfinance.model.AccountTransaction;
import br.com.camiloporto.cloudfinance.model.Profile;

public class TransactionServiceTest extends AbstractCloudFinanceDatabaseTest {
	
	@Autowired
	private AccountManager accountManager;
	
	@Autowired
	private UserProfileManager userProfileManager;
	
	@Autowired
	private TransactionManager transactionManager;
	
	private Profile profile;
	
	private Account origin;
	private Account dest;
	
	@BeforeMethod
	public void clearUserData() {
		cleanUserData();
		
		final String camiloporto = "some@email.com";
		final String senha = "1234";
		Profile p = new ProfileBuilder()
		.newProfile()
		.comEmail(camiloporto)
		.comSenha(senha)
		.create();
		profile = userProfileManager.signUp(p);
		
		List<Account> roots = accountManager.findRootAccounts(profile);
		Account root = roots.get(0);
		
		AccountNode rootBranch = accountManager.getAccountBranch(profile, root.getId());
		origin = rootBranch.getChildren().get(0).getAccount();
		dest = rootBranch.getChildren().get(1).getAccount();
	}
	
	@Test
	public void shouldAddNewTransaction() {
		AccountTransaction saved = transactionManager.saveAccountTransaction(
				origin.getId(), dest.getId(), new Date(), new BigDecimal("1250.25"), "transaction description");
		
		Assert.assertNotNull(saved.getId(), "did not assigned an id for new transaction");
	}
	
	//FIXME adicionar testes de validacao de entradas para transacao
}
