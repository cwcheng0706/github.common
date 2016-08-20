/**
 * @Author: zy
 * @Company: 
 * @Create Time: 2016年8月20日 下午11:25:43
 */
package com.zy.sec1.entity;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Transient;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * @Project: sec1
 * @Author zy
 * @Company:
 * @Create Time: 2016年8月20日 下午11:25:43
 */
public class User implements UserDetails,Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6951765438611977416L;
	private String username;
	private String password;
	
	private List<GrantedAuthority> authorities;
	private final boolean accountNonExpired = true;
	private final boolean accountNonLocked = true;
	private final boolean credentialsNonExpired = true;
	private final boolean enabled = true;
	
	public User(String username,String password,List<GrantedAuthority> authorities) {
		this.username = username;
		this.password = password;
		this.authorities = authorities;
				
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Transient
	public List<GrantedAuthority> getAuthorities() {
		return authorities;
	}

	@Transient
	public void setAuthorities(List<GrantedAuthority> authorities) {
		this.authorities = authorities;
	}
	
	@Transient
	public boolean isAccountNonExpired() {
		return accountNonExpired;
	}

	@Transient
	public boolean isAccountNonLocked() {
		return accountNonLocked;
	}

	@Transient
	public boolean isCredentialsNonExpired() {
		return credentialsNonExpired;
	}

	@Transient
	public boolean isEnabled() {
		return enabled;
	}

}
