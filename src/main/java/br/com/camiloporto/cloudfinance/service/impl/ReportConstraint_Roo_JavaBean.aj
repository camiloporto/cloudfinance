// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package br.com.camiloporto.cloudfinance.service.impl;

import br.com.camiloporto.cloudfinance.model.Profile;
import br.com.camiloporto.cloudfinance.service.impl.ReportConstraint;
import java.util.Date;

privileged aspect ReportConstraint_Roo_JavaBean {
    
    public Profile ReportConstraint.getProfile() {
        return this.profile;
    }
    
    public void ReportConstraint.setProfile(Profile profile) {
        this.profile = profile;
    }
    
    public Long ReportConstraint.getRootAccountId() {
        return this.rootAccountId;
    }
    
    public void ReportConstraint.setRootAccountId(Long rootAccountId) {
        this.rootAccountId = rootAccountId;
    }
    
    public Long ReportConstraint.getAccountId() {
        return this.accountId;
    }
    
    public void ReportConstraint.setAccountId(Long accountId) {
        this.accountId = accountId;
    }
    
    public Date ReportConstraint.getBegin() {
        return this.begin;
    }
    
    public void ReportConstraint.setBegin(Date begin) {
        this.begin = begin;
    }
    
    public Date ReportConstraint.getEnd() {
        return this.end;
    }
    
    public void ReportConstraint.setEnd(Date end) {
        this.end = end;
    }
    
    public Date ReportConstraint.getBalanceSheetDate() {
        return this.balanceSheetDate;
    }
    
    public void ReportConstraint.setBalanceSheetDate(Date balanceSheetDate) {
        this.balanceSheetDate = balanceSheetDate;
    }
    
}
