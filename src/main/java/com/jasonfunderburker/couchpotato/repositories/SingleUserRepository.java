package com.jasonfunderburker.couchpotato.repositories;

import com.jasonfunderburker.couchpotato.entities.UserDO;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Created on 24.05.2017
 *
 * @author Ekaterina.Bashkankova
 */
public interface SingleUserRepository extends JpaRepository<UserDO, Long> {

    UserDO findByUsername(String username);

    UserDO findByRssPublic(String rssPublic);

    Optional<UserDO> findFirstByOrderById();

    default boolean anyUserExist() {
        return count() > 0;
    }

    default boolean isCorrectRssString(String rssString) {
        return findByRssPublic(rssString) != null;
    }
}
