// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package br.com.camiloporto.cloudfinance.web;

import br.com.camiloporto.cloudfinance.model.AccountNode;
import br.com.camiloporto.cloudfinance.model.Account;
import br.com.camiloporto.cloudfinance.web.AccountOperationResponse;

privileged aspect AccountOperationResponse_Roo_JavaBean {
    
    public Account[] AccountOperationResponse.getRootAccounts() {
        return this.rootAccounts;
    }
    
    public void AccountOperationResponse.setRootAccounts(Account[] rootAccounts) {
        this.rootAccounts = rootAccounts;
    }
    
    public Account[] AccountOperationResponse.getLeafAccounts() {
        return this.leafAccounts;
    }
    
    public void AccountOperationResponse.setLeafAccounts(Account[] leafAccounts) {
        this.leafAccounts = leafAccounts;
    }
    
    public AccountNode AccountOperationResponse.getAccountTree() {
        return this.accountTree;
    }
    
    public void AccountOperationResponse.setAccountTree(AccountNode accountTree) {
        this.accountTree = accountTree;
    }
    
    public Account AccountOperationResponse.getAccount() {
        return this.account;
    }
    
    public void AccountOperationResponse.setAccount(Account account) {
        this.account = account;
    }
    
}
