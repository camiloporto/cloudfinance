// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package br.com.camiloporto.cloudfinance.web;

import br.com.camiloporto.cloudfinance.model.Account;
import br.com.camiloporto.cloudfinance.service.impl.AccountStatement;
import br.com.camiloporto.cloudfinance.service.impl.BalanceSheet;
import br.com.camiloporto.cloudfinance.web.ReportOperationResponse;

privileged aspect ReportOperationResponse_Roo_JavaBean {
    
    public AccountStatement ReportOperationResponse.getAccountStatement() {
        return this.accountStatement;
    }
    
    public void ReportOperationResponse.setAccountStatement(AccountStatement accountStatement) {
        this.accountStatement = accountStatement;
    }
    
    public BalanceSheet ReportOperationResponse.getBalanceSheet() {
        return this.balanceSheet;
    }
    
    public void ReportOperationResponse.setBalanceSheet(BalanceSheet balanceSheet) {
        this.balanceSheet = balanceSheet;
    }
    
    public Account[] ReportOperationResponse.getAccountList() {
        return this.accountList;
    }
    
    public void ReportOperationResponse.setAccountList(Account[] accountList) {
        this.accountList = accountList;
    }
    
}
