/**
 * @Author: zy
 * @Company: 
 * @Create Time: 2016年8月9日 下午10:50:31
 */
package com.zy.springboot_1.entity;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;

/**
 * @Project: springboot_1
 * @Author zy
 * @Company: 
 * @Create Time: 2016年8月9日 下午10:50:31
 */
public class UserDetails extends User implements org.springframework.security.core.userdetails.UserDetails {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8591854548835329677L;

	public Collection<? extends GrantedAuthority> getAuthorities() {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	public boolean isAccountNonLocked() {
		// TODO Auto-generated method stub
		return true;
	}

	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return true;
	}

}
