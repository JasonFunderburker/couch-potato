package com.jasonfunderburker.couchpotato.entities;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

/**
 * Created on 05.03.2017
 *
 * @author JasonFunderburker
 */
public class UserImpl extends User {
    private String rssPublic;

    public UserImpl(String username, String password, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
    }

    public UserImpl(UserDetails user) {
        super(user.getUsername(), user.getPassword(), user.getAuthorities());
    }

    public UserImpl(String username, String password, boolean enabled, boolean accountNonExpired, boolean credentialsNonExpired, boolean accountNonLocked, Collection<? extends GrantedAuthority> authorities, String rssPublic) {
        super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
        this.rssPublic = rssPublic;
    }

    public String getRssPublic() {
        return rssPublic;
    }

    public void setRssPublic(String rssPublic) {
        this.rssPublic = rssPublic;
    }
}
