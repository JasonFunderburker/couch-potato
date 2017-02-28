package com.jasonfunderburker.couchpotato.controller;

import com.jasonfunderburker.couchpotato.security.SingleUserDetailsManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Arrays;

/**
 * Created on 28.02.2017
 *
 * @author JasonFunderburker
 */
@Controller
public class RegisterController {
    private static final Logger logger = LoggerFactory.getLogger(RegisterController.class);

    PasswordEncoder encoder = new BCryptPasswordEncoder();

    @Autowired
    private SingleUserDetailsManager userDetails;

    @RequestMapping(value = "/register", method = RequestMethod.GET)
    public String registerPage(ModelMap model) {
        model.addAttribute("username", "");
        model.addAttribute("password", "");
        return "register";
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public String register(String username, String password) {
        userDetails.createUser(new User(username, encoder.encode(password), Arrays.asList(new SimpleGrantedAuthority("USER"))));
        logger.debug("User created: {}",username);
        return "redirect:/login";
    }
}
