package br.com.camiloporto.cloudfinance.model;

import javax.validation.constraints.NotNull;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.entity.RooJpaEntity;
import org.springframework.roo.addon.serializable.RooSerializable;
import org.springframework.roo.addon.tostring.RooToString;
import org.springframework.security.core.GrantedAuthority;

@RooJavaBean
@RooToString
@RooJpaEntity(table = "authorities")
@RooSerializable
public class UserRole implements GrantedAuthority  {
	
	@NotNull
	private String authority;


}
