package com.jasonfunderburker.couchpotato.controller;

import com.jasonfunderburker.couchpotato.repositories.SingleUserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created on 28.02.2017
 *
 * @author JasonFunderburker
 */

@Controller
public class LoginController {
    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

    private final SingleUserRepository userRepository;

    public LoginController(SingleUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String loginPage(ModelMap model) {
        model.addAttribute("registered", userRepository.anyUserExist());
        logger.debug("registered={}", model.get("registered"));
        return "login";
    }
}
