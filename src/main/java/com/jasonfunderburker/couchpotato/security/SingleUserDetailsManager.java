package com.jasonfunderburker.couchpotato.security;

import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.util.Assert;

import java.util.List;

/**
 * Created on 28.02.2017
 *
 * @author JasonFunderburker
 */
public class SingleUserDetailsManager extends JdbcUserDetailsManager {

    public static final String DEF_ANY_USER_EXISTS_SQL = "select username from users";

    private String anyUserExistsSql = DEF_ANY_USER_EXISTS_SQL;

    public boolean anyUserExist() {
        List<String> users = getJdbcTemplate().queryForList(anyUserExistsSql, String.class);
        return users != null && !users.isEmpty();
    }

    public void setAnyUserExistsSql(String anyUserExistsSql) {
        Assert.hasText(anyUserExistsSql);
        this.anyUserExistsSql = anyUserExistsSql;
    }
}
