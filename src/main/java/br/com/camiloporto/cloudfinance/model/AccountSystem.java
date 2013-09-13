package br.com.camiloporto.cloudfinance.model;

import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.entity.RooJpaEntity;
import org.springframework.roo.addon.serializable.RooSerializable;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJpaEntity
@RooSerializable
public class AccountSystem {

    @NotNull
    @ManyToOne
    private Account rootAccount;

    @NotNull
    @ManyToOne
    private Profile userProfile;
    
}
