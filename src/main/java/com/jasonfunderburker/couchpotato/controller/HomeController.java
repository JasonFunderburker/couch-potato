package com.jasonfunderburker.couchpotato.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created on 10.07.2017
 *
 * @author
 */
@RequestMapping("/")
@Controller
public class HomeController {

    @RequestMapping(value = "", method = RequestMethod.GET)
    public String index() {
        return "index.html";
    }
}
