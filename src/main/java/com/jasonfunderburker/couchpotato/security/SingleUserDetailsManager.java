package com.jasonfunderburker.couchpotato.security;

import com.jasonfunderburker.couchpotato.domain.UserImpl;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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
    public static final String DEF_UPDATE_RSS_PUBLIC_STR_SQL = "update users set rss_public = ? where username = ?";
    public static final String DEF_GET_RSS_PUBLIC_STR_SQL = "select rss_public from users";

    private String anyUserExistsSql = DEF_ANY_USER_EXISTS_SQL;
    private String updateRssPublicStringSql = DEF_UPDATE_RSS_PUBLIC_STR_SQL;
    private String getRssPublicStringSql = DEF_GET_RSS_PUBLIC_STR_SQL;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserDetails userDetails = super.loadUserByUsername(username);
        return new UserImpl(userDetails);
    }

    public boolean anyUserExist() {
        List<String> users = getJdbcTemplate().queryForList(anyUserExistsSql, String.class);
        return users != null && !users.isEmpty();
    }

    public void saveRssPublicString(String userName, String rssPublicString) {
        getJdbcTemplate().update(updateRssPublicStringSql, rssPublicString, userName);
    }

    public boolean isCorrectRssPublicString(String stringForCheck) {
        if (stringForCheck == null || stringForCheck.isEmpty())
            return false;
        List<String> validRss = getJdbcTemplate().queryForList(getRssPublicStringSql, String.class);
        return validRss != null && validRss.contains(stringForCheck);
    }

    public void setAnyUserExistsSql(String anyUserExistsSql) {
        Assert.hasText(anyUserExistsSql);
        this.anyUserExistsSql = anyUserExistsSql;
    }

    public void setUpdateRssPublicStringSql(String updateRssPublicStringSql) {
        Assert.hasText(updateRssPublicStringSql);
        this.updateRssPublicStringSql = updateRssPublicStringSql;
    }

    public void setGetRssPublicStringSql(String getRssPublicStringSql) {
        Assert.hasText(getRssPublicStringSql);
        this.getRssPublicStringSql = getRssPublicStringSql;
    }
}
