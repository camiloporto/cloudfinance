package br.com.camiloporto.cloudfinance.model;

import javax.persistence.FetchType;
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
public class Account {

    @NotNull
    private String name;

    private String description;

    @ManyToOne(fetch = FetchType.EAGER)
    private br.com.camiloporto.cloudfinance.model.Account parentAccount;
}
