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

public class UserProfileManagerTest extends AbstractCloudFinanceDatabaseTest {
	
	@Autowired
	private UserProfileManager userProfileManager;
	
	@BeforeMethod
	public void clearUserData() {
		cleanUserData();
	}
	
	@Test
	public void deveCadastrarPerfilDeUsuario() {
		final String camiloporto = "some@email.com";
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
		final String camiloporto = "some@email.com";
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
	
	@Test
	public void shouldThrowsConstraintViolationIfEmailIsEmpty() {
		final String email = "";
		final String pass = "1234";
		Profile p = new ProfileBuilder()
			.newProfile()
			.comEmail(email)
			.comSenha(pass)
			.create();
		try {
			userProfileManager.signUp(p);
			Assert.fail("did not throw expected exception");
		} catch (Exception e) {
			e.printStackTrace();
			new ExceptionChecker(e)
				.assertExpectedErrorCountIs(1)
				.assertContainsMessageTemplate(
						"br.com.camiloporto.cloudfinance.profile.USER_ID_REQUIRED"
				);
		}
		
	}
	
	@Test
	public void shouldThrowsConstraintViolationIfEmailIsNull() {
		final String email = null;
		final String pass = "1234";
		Profile p = new ProfileBuilder()
			.newProfile()
			.comEmail(email)
			.comSenha(pass)
			.create();
		try {
			userProfileManager.signUp(p);
			Assert.fail("did not throw expected exception");
		} catch (Exception e) {
			e.printStackTrace();
			new ExceptionChecker(e)
				.assertExpectedErrorCountIs(1)
				.assertContainsMessageTemplate(
						"br.com.camiloporto.cloudfinance.profile.USER_ID_REQUIRED"
				);
		}
		
	}
	
	@Test
	public void shouldThrowsConstraintViolationIfPasswordIsNull() {
		final String email = "some@email.com";
		final String pass = null;
		Profile p = new ProfileBuilder()
			.newProfile()
			.comEmail(email)
			.comSenha(pass)
			.create();
		try {
			userProfileManager.signUp(p);
			Assert.fail("did not throw expected exception");
		} catch (Exception e) {
			e.printStackTrace();
			new ExceptionChecker(e)
				.assertExpectedErrorCountIs(1)
				.assertContainsMessageTemplate(
						"br.com.camiloporto.cloudfinance.profile.USER_PASS_REQUIRED"
				);
		}
		
	}
	
	@Test
	public void shouldThrowsConstraintViolationIfPasswordIsEmpty() {
		final String email = "some@email.com";
		final String pass = "";
		Profile p = new ProfileBuilder()
			.newProfile()
			.comEmail(email)
			.comSenha(pass)
			.create();
		try {
			userProfileManager.signUp(p);
			Assert.fail("did not throw expected exception");
		} catch (Exception e) {
			e.printStackTrace();
			new ExceptionChecker(e)
				.assertExpectedErrorCountIs(1)
				.assertContainsMessageTemplate(
						"br.com.camiloporto.cloudfinance.profile.USER_PASS_REQUIRED"
				);
		}
	}
	
	@Test
	public void shouldFindProfileByCredentials() {
		final String camiloporto = "some@email.com";
		final String senha = "1234";
		
		Profile p = new ProfileBuilder()
			.newProfile()
			.comEmail(camiloporto)
			.comSenha(senha)
			.create();
		
		userProfileManager.signUp(p);
		
		Profile logged = userProfileManager.login(camiloporto, senha);
		
		new ProfileChecker(logged)
			.assertUserNameEquals(camiloporto)
			.assertPasswordIsEmpty();
	}
	
	@Test
	public void shouldReturnNullIfDoNotFoundByCredentials() {
		final String camiloporto = "some@email.com";
		final String senha = "1234";
		
		Profile p = new ProfileBuilder()
			.newProfile()
			.comEmail(camiloporto)
			.comSenha(senha)
			.create();
		
		userProfileManager.signUp(p);
		
		Profile logged = userProfileManager.login(camiloporto, "WRONG_PASS");
		Assert.assertNull(logged, "should return null");
	}
}
