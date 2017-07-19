package com.jasonfunderburker.couchpotato.controller.rest;

import com.jasonfunderburker.couchpotato.entities.Authority;
import com.jasonfunderburker.couchpotato.entities.UserDO;
import com.jasonfunderburker.couchpotato.repositories.SingleUserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

import static java.util.Collections.singletonList;
import static java.util.Collections.singletonMap;

/**
 * Created on 28.02.2017
 *
 * @author JasonFunderburker
 */
@RestController
public class RegisterController {
    private static final Logger logger = LoggerFactory.getLogger(RegisterController.class);

    private PasswordEncoder encoder = new BCryptPasswordEncoder();

    private final SingleUserRepository userRepository;

    public RegisterController(SingleUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ResponseEntity<?> register(@RequestBody UserDO user) {
        if (!userRepository.anyUserExist()) {
            UserDO newUser = new UserDO(user.getUsername(), encoder.encode(user.getPassword()), singletonList(new Authority("ROLE_USER")));
            userRepository.saveAndFlush(newUser);
            logger.debug("User created: {}", newUser.getUsername());
            return ResponseEntity.status(HttpStatus.CREATED).body(newUser);
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(singletonMap("errorMessage","User already registered"));

    }

    @RequestMapping(value = "/register/check", method = RequestMethod.GET)
    public Map<String, Object> registerCheck() {
        return singletonMap("anyUserExist", userRepository.anyUserExist());
    }
}
