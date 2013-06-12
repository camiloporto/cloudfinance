package br.com.camiloporto.cloudfinance;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;

import br.com.camiloporto.cloudfinance.repository.AccountRepository;
import br.com.camiloporto.cloudfinance.repository.AccountSystemRepository;
import br.com.camiloporto.cloudfinance.repository.AccountTransactionRepository;
import br.com.camiloporto.cloudfinance.repository.ProfileRepository;

@ContextConfiguration(locations = {"classpath:/META-INF/spring/applicationContext*.xml"})
@ActiveProfiles("unit-test")
public abstract class AbstractCloudFinanceDatabaseTest extends
		AbstractTestNGSpringContextTests {
	
	@Autowired
	protected ProfileRepository profileRepository;
	
	@Autowired
	protected AccountSystemRepository accountSystemRepository;
	
	@Autowired
	protected AccountRepository accountRepository;
	
	@Autowired
	protected AccountTransactionRepository accountTransactionRepository;
	
	protected void cleanUserData() {
		accountTransactionRepository.deleteAll();
		accountSystemRepository.deleteAll();
		accountRepository.deleteAll();
		
		profileRepository.deleteAll();
		
	}

}
