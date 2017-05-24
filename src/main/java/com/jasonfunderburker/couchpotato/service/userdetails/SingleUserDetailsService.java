package com.jasonfunderburker.couchpotato.service.userdetails;

import com.jasonfunderburker.couchpotato.entities.UserDO;
import com.jasonfunderburker.couchpotato.entities.UserPrincipal;
import com.jasonfunderburker.couchpotato.repositories.SingleUserRepository;
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

    @Autowired
    public SingleUserDetailsService(SingleUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserDO user = userRepository.findByUsername(username);
        if (user == null) throw new UsernameNotFoundException("Unknown user");
        return new UserPrincipal(user, getAuthorities(user.getAuthorityNames()));
    }

    private Collection<? extends GrantedAuthority> getAuthorities(List<String> roles) {
        return AuthorityUtils.createAuthorityList(roles.toArray(new String[]{}));
    }
}
