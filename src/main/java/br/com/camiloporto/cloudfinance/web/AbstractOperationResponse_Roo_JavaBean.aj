// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package br.com.camiloporto.cloudfinance.web;

import br.com.camiloporto.cloudfinance.web.AbstractOperationResponse;

privileged aspect AbstractOperationResponse_Roo_JavaBean {
    
    public boolean AbstractOperationResponse.isSuccess() {
        return this.success;
    }
    
    public void AbstractOperationResponse.setSuccess(boolean success) {
        this.success = success;
    }
    
    public String[] AbstractOperationResponse.getErrors() {
        return this.errors;
    }
    
    public void AbstractOperationResponse.setErrors(String[] errors) {
        this.errors = errors;
    }
    
}
