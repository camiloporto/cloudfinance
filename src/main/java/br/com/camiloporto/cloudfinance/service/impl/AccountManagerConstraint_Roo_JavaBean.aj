// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package br.com.camiloporto.cloudfinance.service.impl;

import br.com.camiloporto.cloudfinance.model.Account;
import br.com.camiloporto.cloudfinance.model.Profile;
import br.com.camiloporto.cloudfinance.repository.AccountRepository;
import br.com.camiloporto.cloudfinance.service.impl.AccountManagerConstraint;

privileged aspect AccountManagerConstraint_Roo_JavaBean {
    
    public AccountRepository AccountManagerConstraint.getAccountRepository() {
        return this.accountRepository;
    }
    
    public void AccountManagerConstraint.setAccountRepository(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }
    
    public Profile AccountManagerConstraint.getProfile() {
        return this.profile;
    }
    
    public void AccountManagerConstraint.setProfile(Profile profile) {
        this.profile = profile;
    }
    
    public Long AccountManagerConstraint.getAccountId() {
        return this.accountId;
    }
    
    public void AccountManagerConstraint.setAccountId(Long accountId) {
        this.accountId = accountId;
    }
    
    public Account AccountManagerConstraint.getAccount() {
        return this.account;
    }
    
    public void AccountManagerConstraint.setAccount(Account account) {
        this.account = account;
    }
    
}
