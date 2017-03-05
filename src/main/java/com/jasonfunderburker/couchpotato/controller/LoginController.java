package com.jasonfunderburker.couchpotato.controller;

import com.jasonfunderburker.couchpotato.security.SingleUserDetailsManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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

    private final SingleUserDetailsManager userDetails;

    @Autowired
    public LoginController(SingleUserDetailsManager userDetails) {
        this.userDetails = userDetails;
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String loginPage(ModelMap model) {
        model.addAttribute("registered", userDetails.anyUserExist());
        logger.debug("registered={}", model.get("registered"));
        return "login";
    }
}
