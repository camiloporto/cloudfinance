package br.com.camiloporto.cloudfinance.model;

import javax.persistence.CascadeType;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.entity.RooJpaEntity;
import org.springframework.roo.addon.serializable.RooSerializable;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJpaEntity
@RooSerializable
@JsonSerialize(include=JsonSerialize.Inclusion.NON_EMPTY)
public class AccountTransaction {

    @NotNull
    @ManyToOne(cascade = CascadeType.ALL)
    private AccountEntry origin;

    @NotNull
    @ManyToOne(cascade = CascadeType.ALL)
    private AccountEntry destin;
}
