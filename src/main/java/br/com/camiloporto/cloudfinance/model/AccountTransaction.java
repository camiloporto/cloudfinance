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
public class AccountTransaction {

    @NotNull
    @ManyToOne
    private AccountEntry origin;

    @NotNull
    @ManyToOne
    private AccountEntry destin;
}
