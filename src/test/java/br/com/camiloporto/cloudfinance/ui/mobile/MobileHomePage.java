package br.com.camiloporto.cloudfinance.ui.mobile;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

public class MobileHomePage {

	@FindBy(how = How.CSS, using="section a")
	private WebElement newUserProfileLink;
	
	public void clickNewUserProfileLink() {
		newUserProfileLink.click();
	}

}
