// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package br.com.camiloporto.cloudfinance.repository;

import br.com.camiloporto.cloudfinance.model.AccountSystem;
import br.com.camiloporto.cloudfinance.repository.AccountSystemRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

privileged aspect AccountSystemRepository_Roo_Jpa_Repository {
    
    declare parents: AccountSystemRepository extends JpaRepository<AccountSystem, Long>;
    
    declare parents: AccountSystemRepository extends JpaSpecificationExecutor<AccountSystem>;
    
    declare @type: AccountSystemRepository: @Repository;
    
}
