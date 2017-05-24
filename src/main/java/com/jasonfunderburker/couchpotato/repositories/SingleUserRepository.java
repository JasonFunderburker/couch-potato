package com.jasonfunderburker.couchpotato.repositories;

import com.jasonfunderburker.couchpotato.entities.UserDO;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created on 24.05.2017
 *
 * @author Ekaterina.Bashkankova
 */
public interface SingleUserRepository extends JpaRepository<UserDO, Long> {

    UserDO findByUsername(String username);

    default boolean anyUserExist() {
        return count() > 0;
    }
}
