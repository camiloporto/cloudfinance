package br.com.camiloporto.cloudfinance.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

import br.com.camiloporto.cloudfinance.builders.ProfileBuilder;
import br.com.camiloporto.cloudfinance.checkers.ProfileChecker;
import br.com.camiloporto.cloudfinance.model.Profile;

@ContextConfiguration(locations = {"classpath:/META-INF/spring/applicationContext*.xml"})
@ActiveProfiles("unit-test")
public class UserProfileManagerTest extends AbstractTestNGSpringContextTests {
	
	@Autowired
	private UserProfileManager userProfileManager;
	
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
}
