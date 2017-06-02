package com.jasonfunderburker.couchpotato.entities;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

/**
 * Created on 24.05.2017
 *
 * @author Ekaterina.Bashkankova
 */
public class UserPrincipal extends User {

    private UserDO user;

    public UserPrincipal(UserDO user, Collection<? extends GrantedAuthority> authorities) {
        super(user.getUsername(), user.getPassword(), user.isEnabled(), true, true, true, authorities);
        this.user = user;
    }

    public UserDO getUser() {
        return user;
    }
}
