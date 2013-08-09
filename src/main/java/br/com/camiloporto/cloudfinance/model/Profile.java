package br.com.camiloporto.cloudfinance.model;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.entity.RooJpaEntity;
import org.springframework.roo.addon.serializable.RooSerializable;
import org.springframework.roo.addon.tostring.RooToString;

import br.com.camiloporto.cloudfinance.service.impl.UserProfileManagerConstraint;

@RooJavaBean
@RooToString
@RooJpaEntity
@RooSerializable
public class Profile {

    @NotEmpty(groups={UserProfileManagerConstraint.SIGNUP_RULES.class}, message="{br.com.camiloporto.cloudfinance.profile.USER_ID_REQUIRED}")
    private String userId;

    @NotEmpty(groups={UserProfileManagerConstraint.SIGNUP_RULES.class}, message="{br.com.camiloporto.cloudfinance.profile.USER_PASS_REQUIRED}")
    private String pass;
}
