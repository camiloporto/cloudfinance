package br.com.camiloporto.cloudfinance.ui.mobile;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.testng.Assert;

public class FormNewAccountPage extends TemplatePage {
	
	@FindBy(how = How.CSS, css="h3")
	private WebElement parentAccountElement;
	
	@FindBy(how = How.CSS, css = "input[name=name]")
	private WebElement inputName;
	
	@FindBy(how = How.CSS, css = "input[name=description]")
	private WebElement inputDescription;
	
	@FindBy(how = How.CSS, css = "#newAccountForm input[type=submit]")
	private WebElement inputSubmit;

	public void assertParentAccountName(String expectedAccountName) {
		Assert.assertEquals(parentAccountElement.getText(), expectedAccountName, "parent account name did not match");
	}

	public FormNewAccountPage fillNewAccount(String newName, String newDescription) {
		inputName.sendKeys(newName);
		inputDescription.sendKeys(newDescription);
		return this;
	}

	public void submitForm() {
		System.out.println("FormNewAccountPage.submitForm() " + inputSubmit.getAttribute("value"));
		inputSubmit.submit();
	}

}
