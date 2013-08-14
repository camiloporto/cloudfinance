package br.com.camiloporto.cloudfinance.ui.mobile;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

public class MobileHomePage {

	@FindBy(how = How.CSS, using="section a")
	private WebElement newUserProfileLink;
	
	@FindBy(how = How.NAME, name="userName")
	private WebElement userNameEl;
	
	@FindBy(how = How.NAME, name="pass")
	private WebElement passwordEl;
	
	@FindBy(how = How.ID, id="btnSubmit")
	private WebElement submitEl;
	
	public void clickNewUserProfileLink() {
		newUserProfileLink.click();
	}

	public void login(String userName, String pass) {
		userNameEl.sendKeys(userName);
		passwordEl.sendKeys(pass);
		submitEl.submit();
	}

}
