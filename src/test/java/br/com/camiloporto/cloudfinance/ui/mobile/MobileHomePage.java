package br.com.camiloporto.cloudfinance.ui.mobile;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.testng.Assert;

public class MobileHomePage {

	@FindBy(how = How.CSS, using="section a")
	private WebElement newUserProfileLink;
	
	@FindBy(how = How.NAME, name="userName")
	private WebElement userNameEl;
	
	@FindBy(how = How.NAME, name="pass")
	private WebElement passwordEl;
	
	@FindBy(how = How.ID, id="btnSubmit")
	private WebElement submitEl;
	
	@FindBy(how = How.ID, id="loginStatus")
	private WebElement loginStatus;
	
	public void clickNewUserProfileLink() {
		newUserProfileLink.click();
	}

	public void login(String userName, String pass) {
		userNameEl.sendKeys(userName);
		passwordEl.sendKeys(pass);
		submitEl.submit();
	}

	
	public MobileHomePage assertLoginStatusMessageIs(String expectedLoginStatusMessage) {
		Assert.assertTrue(loginStatus.getText().equalsIgnoreCase(expectedLoginStatusMessage), expectedLoginStatusMessage + " not present");
		return this;
	}

}
