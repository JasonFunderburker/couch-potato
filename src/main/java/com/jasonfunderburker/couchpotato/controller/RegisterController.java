package com.jasonfunderburker.couchpotato.controller;

import com.jasonfunderburker.couchpotato.entities.Authority;
import com.jasonfunderburker.couchpotato.entities.UserDO;
import com.jasonfunderburker.couchpotato.repositories.SingleUserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Collections;

/**
 * Created on 28.02.2017
 *
 * @author JasonFunderburker
 */
@Controller
public class RegisterController {
    private static final Logger logger = LoggerFactory.getLogger(RegisterController.class);

    private PasswordEncoder encoder = new BCryptPasswordEncoder();

    private final SingleUserRepository userRepository;

    public RegisterController(SingleUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @RequestMapping(value = "/register", method = RequestMethod.GET)
    public String registerPage(ModelMap model) {
        if (userRepository.anyUserExist())
            return "redirect:/login";
        model.addAttribute("username", "");
        model.addAttribute("password", "");
        return "register";
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public String register(String username, String password) {
        if (!userRepository.anyUserExist()) {
            userRepository.saveAndFlush(new UserDO(username, encoder.encode(password), Collections.singletonList(new Authority("ROLE_USER"))));
            logger.debug("User created: {}", username);
        }
        return "redirect:/login";
    }
}
