package com.jasonfunderburker.couchpotato.controller;

import com.jasonfunderburker.couchpotato.service.rss.RSSFeedGeneratorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by Ekaterina.Bashkankova on 19.10.2016
 */
@Controller
@RequestMapping("rss")
public class RSSController {
    private static final Logger logger = LoggerFactory.getLogger(RSSController.class);

    @Autowired
    RSSFeedGeneratorService feedGeneratorService;


    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String generateRss(ModelMap model) {
        return "";
    }
}
