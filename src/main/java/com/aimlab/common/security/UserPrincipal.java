package com.aimlab.common.security;

import com.aimlab.entity.User;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

@Getter
public class UserPrincipal implements UserDetails {
    private final String userId;
    private final String userEmail;
    private final String userNickname;
    private final String password;
    private final Collection<? extends GrantedAuthority> authorities;
    private Map<String, Object> attributes;

    public UserPrincipal(String userId, String userEmail, String password, String userNickname, Collection<? extends GrantedAuthority> authorities) {
        this.userId = userId;
        this.userEmail = userEmail;
        this.password = password;
        this.userNickname = userNickname;
        this.authorities = authorities;
    }

    public static UserPrincipal create(User user){
        List<GrantedAuthority> authorities =
                Collections.singletonList(new SimpleGrantedAuthority(user.getAuthority().getValue()));

        return new UserPrincipal(
                user.getUserId().toString(),
                user.getUserEmail(),
                user.getUserPassword(),
                user.getUserNickname(),
                authorities);
    }

    public static UserPrincipal create(User user, Map<String, Object> attributes){
        UserPrincipal userPrincipal = create(user);
        userPrincipal.setAttributes(attributes);
        return userPrincipal;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return userId;
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
        return true;
    }

    public void setAttributes(Map<String, Object> attributes) {
        this.attributes = attributes;
    }
}
