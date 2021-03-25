package com.hly.july.common.entity;

import lombok.Data;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

/**
 * @author Linyuan Hou
 * @date 2021/3/15 12:40
 */
@Data
public class LoginUser implements UserDetails {
    private Long id;
    private String name;
    private String password;
    private Boolean enabled;
    private List<SimpleGrantedAuthority> authorities;

    public LoginUser(Long id, String name,List<SimpleGrantedAuthority> authorities){
        this.name = name;
        this.id =id;
        this.password = password;
        this.enabled = enabled;
        this.authorities = authorities;
    }

    public LoginUser(Long id, String name, String password, Boolean enabled){
        this.name = name;
        this.id =id;
        this.password = password;
        this.enabled = enabled;

    }

    @Override
    public List<SimpleGrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.name;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return this.enabled;
    }
}
