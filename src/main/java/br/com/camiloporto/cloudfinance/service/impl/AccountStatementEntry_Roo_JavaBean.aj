// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package br.com.camiloporto.cloudfinance.service.impl;

import br.com.camiloporto.cloudfinance.model.Account;
import br.com.camiloporto.cloudfinance.service.impl.AccountStatementEntry;
import java.math.BigDecimal;
import java.util.Date;

privileged aspect AccountStatementEntry_Roo_JavaBean {
    
    public Date AccountStatementEntry.getDate() {
        return this.date;
    }
    
    public void AccountStatementEntry.setDate(Date date) {
        this.date = date;
    }
    
    public String AccountStatementEntry.getDescription() {
        return this.description;
    }
    
    public void AccountStatementEntry.setDescription(String description) {
        this.description = description;
    }
    
    public BigDecimal AccountStatementEntry.getAmount() {
        return this.amount;
    }
    
    public void AccountStatementEntry.setAmount(BigDecimal amount) {
        this.amount = amount;
    }
    
    public Account AccountStatementEntry.getInvolvedAccount() {
        return this.involvedAccount;
    }
    
    public void AccountStatementEntry.setInvolvedAccount(Account involvedAccount) {
        this.involvedAccount = involvedAccount;
    }
    
}
