// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package br.com.camiloporto.cloudfinance.model;

import br.com.camiloporto.cloudfinance.model.Account;
import br.com.camiloporto.cloudfinance.model.AccountSystem;
import br.com.camiloporto.cloudfinance.model.Profile;

privileged aspect AccountSystem_Roo_JavaBean {
    
    public Account AccountSystem.getRootAccount() {
        return this.rootAccount;
    }
    
    public void AccountSystem.setRootAccount(Account rootAccount) {
        this.rootAccount = rootAccount;
    }
    
    public Profile AccountSystem.getUserProfile() {
        return this.userProfile;
    }
    
    public void AccountSystem.setUserProfile(Profile userProfile) {
        this.userProfile = userProfile;
    }
    
}
