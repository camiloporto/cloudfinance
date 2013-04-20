package br.com.camiloporto.cloudfinance.model;

import java.math.BigDecimal;
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
public class AccountEntry {

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER)
    private Account account;

    @NotNull
    private BigDecimal entryValue;

    @NotNull
    private String comment;
}
