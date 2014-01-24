package br.com.camiloporto.cloudfinance.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.entity.RooJpaEntity;
import org.springframework.roo.addon.serializable.RooSerializable;
import org.springframework.roo.addon.tostring.RooToString;
import org.springframework.security.core.userdetails.UserDetails;

import br.com.camiloporto.cloudfinance.service.impl.UserProfileManagerConstraint;

@RooJavaBean
@RooToString
@RooJpaEntity(table = "users")
@RooSerializable
public class Profile implements UserDetails {
	
	@Id
	@Column(name = "username")
    @NotEmpty(groups={UserProfileManagerConstraint.SIGNUP_RULES.class}, message="{br.com.camiloporto.cloudfinance.profile.USER_ID_REQUIRED}")
    private String userId;

	@Column(name = "password")
    @NotEmpty(groups={UserProfileManagerConstraint.SIGNUP_RULES.class}, message="{br.com.camiloporto.cloudfinance.profile.USER_PASS_REQUIRED}")
    private String pass;
	
	@Column(name = "enabled")
	private boolean enabled = true;
	
	@OneToMany(cascade=CascadeType.ALL, fetch=FetchType.EAGER)
	@JoinColumn(name = "username")
	private List<UserRole> authorities = new ArrayList<UserRole>();

	@Override
	public String getPassword() {
		return getPass();
	}

	@Override
	public String getUsername() {
		return getUserId();
	}

	@Override
	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isAccountNonLocked() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return false;
	}
    
}
