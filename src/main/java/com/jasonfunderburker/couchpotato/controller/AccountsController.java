package com.jasonfunderburker.couchpotato.controller;

import com.jasonfunderburker.couchpotato.entities.TorrentUserInfo;
import com.jasonfunderburker.couchpotato.entities.UserDO;
import com.jasonfunderburker.couchpotato.entities.util.CryptMaster;
import com.jasonfunderburker.couchpotato.repositories.SingleUserRepository;
import com.jasonfunderburker.couchpotato.service.accounts.TorrentsAccountsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/torrentTypes")
public class AccountsController {
    private static final Logger logger = LoggerFactory.getLogger(AccountsController.class);

    private final TorrentsAccountsService accountsService;

    @Autowired
    public AccountsController(TorrentsAccountsService accountsService) {
        this.accountsService = accountsService;
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    public List<TorrentUserInfo> getTorrentTypes() {
        return accountsService.getAllTorrentAccounts();
    }

    @RequestMapping(value = "/{id}/account", method = RequestMethod.POST)
    public TorrentUserInfo addTorrentAccount(@PathVariable("id")long id, @RequestBody TorrentUserInfo userInfo) {
        TorrentUserInfo currentInfo = accountsService.findByType(id);
        currentInfo.setUsername(userInfo.getUsername());
        currentInfo.generateHash(userInfo.getHash());
        logger.debug("add torrent account with info={}", currentInfo);
        return accountsService.addTorrentAccount(currentInfo);
    }
}
