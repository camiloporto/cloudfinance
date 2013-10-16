package br.com.camiloporto.cloudfinance;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;

import br.com.camiloporto.cloudfinance.helpers.DataInsertionHelper;
import br.com.camiloporto.cloudfinance.repository.AccountRepository;
import br.com.camiloporto.cloudfinance.repository.AccountSystemRepository;
import br.com.camiloporto.cloudfinance.repository.AccountTransactionRepository;
import br.com.camiloporto.cloudfinance.repository.ProfileRepository;
import br.com.camiloporto.cloudfinance.service.AccountManager;
import br.com.camiloporto.cloudfinance.service.TransactionManager;
import br.com.camiloporto.cloudfinance.service.UserProfileManager;

@ContextConfiguration(locations = {"classpath:/META-INF/spring/applicationContext*.xml",
		"classpath:/META-INF/spring/spring-security*.xml"})
@ActiveProfiles("unit-test")
public abstract class AbstractCloudFinanceDatabaseTest extends
		AbstractTestNGSpringContextTests {
	
	@Autowired
	protected TransactionManager transactionManager;
	
	@Autowired
	protected AccountManager accountManager;
	
	@Autowired
	protected ProfileRepository profileRepository;
	
	@Autowired
	protected AccountSystemRepository accountSystemRepository;
	
	@Autowired
	protected AccountRepository accountRepository;
	
	@Autowired
	protected AccountTransactionRepository accountTransactionRepository;
	
	@Autowired
	protected UserProfileManager userProfileManager;
	
	protected DataInsertionHelper accountInsertionHelper;
	
	protected void cleanUserData() {
		accountTransactionRepository.deleteAll();
		accountSystemRepository.deleteAll();
		accountRepository.deleteAll();
		
		profileRepository.deleteAll();
		
	}

}
