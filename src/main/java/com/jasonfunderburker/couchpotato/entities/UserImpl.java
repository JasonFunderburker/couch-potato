package com.jasonfunderburker.couchpotato.entities;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Collection;

/**
 * Created on 05.03.2017
 *
 * @author JasonFunderburker
 */
@Entity
@Table(name = "users")
public class UserImpl extends User {

    @Id
    @GeneratedValue
    private Long id;
    private String username;
    private String password;
    private String rssPublic;
    private boolean enabled;

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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRssPublic() {
        return rssPublic;
    }

    public void setRssPublic(String rssPublic) {
        this.rssPublic = rssPublic;
    }

    @Override
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
