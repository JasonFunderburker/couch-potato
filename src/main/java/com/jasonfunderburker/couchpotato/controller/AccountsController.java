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
public class AccountsController {
    private static final Logger logger = LoggerFactory.getLogger(AccountsController.class);

    private final TorrentsAccountsService accountsService;
    private final SingleUserRepository userRepository;
    private boolean isKeySet = false;

    @Autowired
    public AccountsController(TorrentsAccountsService accountsService, SingleUserRepository userRepository) {
        this.accountsService = accountsService;
        this.userRepository = userRepository;
    }

    @RequestMapping(value = "/torrentType", method = RequestMethod.GET)
    public List<TorrentUserInfo> getTorrentTypes() {
        return accountsService.getAllTorrentAccounts();
    }

    @RequestMapping(value = "/torrentType/{id}/account", method = RequestMethod.POST)
    public TorrentUserInfo addTorrentAccount(@PathVariable("id")long id, @RequestBody TorrentUserInfo userInfo, Principal principal) {
        setKey(principal.getName());
        TorrentUserInfo currentInfo = accountsService.findByType(id);
        currentInfo.setUsername(userInfo.getUsername());
        currentInfo.setHash(userInfo.getHash());
        return accountsService.addTorrentAccount(currentInfo);
    }

    private void setKey(String userName) {
        if (!isKeySet) {
            UserDO userDO = userRepository.findByUsername(userName);
            logger.debug("set key={}", userDO.getPassword());
            CryptMaster.setKey(userDO.getPassword());
            isKeySet = true;
        }
    }
}
