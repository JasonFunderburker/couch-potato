package com.jasonfunderburker.couchpotato.service.userdetails;

import com.jasonfunderburker.couchpotato.entities.UserDO;
import com.jasonfunderburker.couchpotato.entities.UserPrincipal;
import com.jasonfunderburker.couchpotato.entities.util.CryptMaster;
import com.jasonfunderburker.couchpotato.repositories.SingleUserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * Created on 24.05.2017
 *
 * @author Ekaterina.Bashkankova
 */
@Service
public class SingleUserDetailsService implements UserDetailsService {

    private final SingleUserRepository userRepository;

    private static final Logger logger = LoggerFactory.getLogger(SingleUserDetailsService.class);

    @Autowired
    public SingleUserDetailsService(SingleUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        logger.debug("username={}", username);
        UserDO user = userRepository.findByUsername(username);
        logger.debug("user={}", user);
        if (user == null) throw new UsernameNotFoundException("Unknown user");
        CryptMaster.setKey(user.getPassword());
        return new UserPrincipal(user, getAuthorities(user.getAuthorityNames()));
    }

    private Collection<? extends GrantedAuthority> getAuthorities(List<String> roles) {
        return AuthorityUtils.createAuthorityList(roles.toArray(new String[]{}));
    }
}
