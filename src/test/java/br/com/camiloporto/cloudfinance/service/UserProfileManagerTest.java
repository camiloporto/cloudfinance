package br.com.camiloporto.cloudfinance.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import br.com.camiloporto.cloudfinance.AbstractCloudFinanceDatabaseTest;
import br.com.camiloporto.cloudfinance.builders.ProfileBuilder;
import br.com.camiloporto.cloudfinance.checkers.ExceptionChecker;
import br.com.camiloporto.cloudfinance.checkers.ProfileChecker;
import br.com.camiloporto.cloudfinance.model.Profile;

//@ContextConfiguration(locations = {"classpath:/META-INF/spring/applicationContext*.xml"})
//@ActiveProfiles("unit-test")
public class UserProfileManagerTest extends AbstractCloudFinanceDatabaseTest {
	
	@Autowired
	private UserProfileManager userProfileManager;
	
	@BeforeMethod
	public void clearUserData() {
		cleanUserData();
	}
	
	@Test
	public void deveCadastrarPerfilDeUsuario() {
		final String camiloporto = "camiloporto@gmail.com";
		final String senha = "1234";
		
		Profile p = new ProfileBuilder()
			.newProfile()
			.comEmail(camiloporto)
			.comSenha(senha)
			.create();
		
		Profile saved = userProfileManager.signUp(p);
		
		new ProfileChecker(saved)
			.checkProfileCreatedCorrectly();
		
	}
	
	@Test
	public void deveLancarConstraintViolationSeEmailJaExistir() {
		final String camiloporto = "camiloporto@gmail.com";
		final String senha = "1234";
		Profile p = new ProfileBuilder()
			.newProfile()
			.comEmail(camiloporto)
			.comSenha(senha)
			.create();
		Profile saved = userProfileManager.signUp(p);
		
		try {
			userProfileManager.signUp(p);
			Assert.fail("did not throw expected exception");
		} catch (Exception e) {
			e.printStackTrace();
			new ExceptionChecker(e)
				.assertExpectedErrorCountIs(1)
				.assertContainsMessageTemplate(
						"br.com.camiloporto.cloudfinance.profile.USER_ID_ALREADY_EXIST"
				);
			
		}
		
	}
}
