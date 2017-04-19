package com.jasonfunderburker.couchpotato.controller;

import com.jasonfunderburker.couchpotato.entities.TorrentUserInfo;
import com.jasonfunderburker.couchpotato.service.accounts.TorrentsAccountsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/settings")
public class SettingsController {
    private static final Logger logger = LoggerFactory.getLogger(SettingsController.class);

    private final TorrentsAccountsService accountsService;

    @Autowired
    public SettingsController(TorrentsAccountsService accountsService) {
        this.accountsService = accountsService;
    }

    @RequestMapping(method = RequestMethod.GET)
    public String getSettings(ModelMap model) {
        logger.debug("All torrent types: {}", accountsService.getAllTorrentTypes());
        model.addAttribute("torrentTypes", accountsService.getAllTorrentTypes());
        model.addAttribute("userInfo", new TorrentUserInfo());
        return "settings";
    }
}
