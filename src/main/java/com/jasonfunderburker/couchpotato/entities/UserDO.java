package com.jasonfunderburker.couchpotato.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.util.*;

import static java.util.stream.Collectors.toList;

/**
 * Created on 05.03.2017
 *
 * @author JasonFunderburker
 */
@Entity
@Data
@ToString(exclude="password")
@NoArgsConstructor
@Table(name = "users")
public class UserDO {

    @Id
    @GeneratedValue
    private Long id;
    private String username;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;
    private String rssPublic;
    private ScheduleSettings settings;
    private boolean enabled;
    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    private List<Authority> authorities = new ArrayList<>();

    public UserDO(String username, String password, List<Authority> authorities) {
        this.username = username;
        this.password = password;
        this.authorities = authorities;
        this.enabled = true;
    }
    public List<String> getAuthorityNames() {
        return authorities.stream().map(Authority::getAuthority).collect(toList());
    }
}
