package com.jasonfunderburker.couchpotato.entities;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.util.*;

import static java.util.stream.Collectors.toList;

/**
 * Created on 05.03.2017
 *
 * @author JasonFunderburker
 */
@Entity
@Table(name = "users")
public class UserDO {

    @Id
    @GeneratedValue
    private Long id;
    private String username;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;
    private String rssPublic;
    private boolean enabled;
    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    private List<Authority> authorities = new ArrayList<>();

    public UserDO() {
    }

    public UserDO(String username, String password, List<Authority> authorities) {
        this.username = username;
        this.password = password;
        this.authorities = authorities;
        this.enabled = true;
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

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public List<Authority> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(List<Authority> authorities) {
        this.authorities = authorities;
    }

    public List<String> getAuthorityNames() {
        return authorities.stream().map(Authority::getAuthority).collect(toList());
    }
}
