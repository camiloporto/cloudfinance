package br.com.camiloporto.cloudfinance.ui.mobile.page;

import java.util.List;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindAll;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

public class ReportHomePage extends TemplatePage {
	
	public static final String ACCOUNT_STATEMENT_MENU_OPTION = "Extrato de Conta";

	private static final String BALANCE_SHEET_MENU_OPTION = "Balanço Patrimonial";
	
	@FindAll({
		@FindBy(how = How.CSS, css = "#subnav li a")
	})
	private List<WebElement> reportNavLinks;

	@Override
	protected String getPageTitle() {
		return "Relatórios";
	}
	
	public void clickSubNavigationLink(String linkText) {
		WebElement link = getSubNavLink(linkText);
		link.click();
	}
	
	private WebElement getSubNavLink(String navLinkText) {
		for (WebElement link : reportNavLinks) {
			if(link.getText().equalsIgnoreCase(navLinkText)){
				return link;
			}
		}
		return null;
	}

	public void clickAccountStatementMenuOption() {
		clickSubNavigationLink(ACCOUNT_STATEMENT_MENU_OPTION);
	}

	public void clickBalanceSheetMenuOption() {
		clickSubNavigationLink(BALANCE_SHEET_MENU_OPTION);
	}

}
