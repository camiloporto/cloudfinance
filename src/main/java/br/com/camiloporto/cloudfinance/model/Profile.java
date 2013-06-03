package br.com.camiloporto.cloudfinance.model;

import javax.validation.constraints.NotNull;

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

    @NotNull
    @NotEmpty(groups={UserProfileManagerConstraint.SIGNUP_RULES.class}, message="br.com.camiloporto.cloudfinance.profile.USER_ID_REQUIRED")
    private String userId;

    @NotNull
    private String pass;
}
