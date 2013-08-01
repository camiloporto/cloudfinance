package br.com.camiloporto.cloudfinance.ui.mobile;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

public class MobileNewUserPage {
	
	@FindBy(how = How.NAME, name="userName")
	private WebElement userNameEl;
	
	@FindBy(how = How.NAME, name="pass")
	private WebElement passwordEl;
	
	@FindBy(how = How.NAME, name="confirmPass")
	private WebElement confirmPasswordEl;
	
	@FindBy(how = How.ID, id="btnSubmit")
	private WebElement submitEl;

	public MobileNewUserPage fillNewUserForm(String userName, String pass, String confirmPass) {
		userNameEl.sendKeys(userName);
		passwordEl.sendKeys(pass);
		confirmPasswordEl.sendKeys(confirmPass);
		return this;
	}

	public MobileNewUserPage submit() {
		submitEl.submit();
		return this;
	}

}
