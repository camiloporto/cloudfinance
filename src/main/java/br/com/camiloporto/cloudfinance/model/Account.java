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

	public static final String ASSET_NAME = "br.com.camiloporto.cloudfinance.model.Account.ASSET_NAME";

	public static final String LIABILITY_NAME = "br.com.camiloporto.cloudfinance.model.Account.LIABILITY_NAME";

	public static final String INCOME_NAME = "br.com.camiloporto.cloudfinance.model.Account.INCOME_NAME";

	public static final String OUTGOING_NAME = "br.com.camiloporto.cloudfinance.model.Account.OUTGOING_NAME";

	@NotNull
    private String name;

    private String description;

    @ManyToOne(fetch = FetchType.EAGER)
    private Account parentAccount;
    
    public Account(String name, Account father) {
		this.name = name;
		parentAccount = father;
    	
	}
}
