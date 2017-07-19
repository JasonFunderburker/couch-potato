package com.jasonfunderburker.couchpotato.service.accounts;

import com.jasonfunderburker.couchpotato.entities.TorrentType;
import com.jasonfunderburker.couchpotato.entities.TorrentUserInfo;
import com.jasonfunderburker.couchpotato.repositories.AccountRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class TorrentsAccountsServiceImpl implements TorrentsAccountsService {
    private static final Logger logger = LoggerFactory.getLogger(TorrentsAccountsServiceImpl.class);

    private final AccountRepository accountRepo;
    private final Map<Long, TorrentType> torrentTypeMap;

    @Autowired
    public TorrentsAccountsServiceImpl(AccountRepository accountRepo) {
        this.accountRepo = accountRepo;
        torrentTypeMap = new HashMap<>();
        List<TorrentUserInfo> accounts = new ArrayList<>();
        Arrays.asList(TorrentType.values()).forEach(t -> {
            torrentTypeMap.put(t.getId(), t);
            accounts.add(new TorrentUserInfo(t));
        });
        accountRepo.saveIfNotExists(accounts);
        accountRepo.flush();
    }

    @Override
    public List<TorrentType> getAllTorrentTypes() {
        return new ArrayList<>(torrentTypeMap.values());
    }

    @Override
    public TorrentType getTypeById(Long id) {
        return torrentTypeMap.get(id);
    }

    @Override
    public TorrentUserInfo addTorrentAccount(TorrentUserInfo userInfo) {
        logger.debug("add torrent account: {}", userInfo);
        return accountRepo.saveAndFlush(userInfo);
    }

    @Override
    public TorrentUserInfo findByType(Long typeId) {
        return accountRepo.findByType(getTypeById(typeId));
    }

    @Override
    public List<TorrentUserInfo> getAllTorrentAccounts() {
        return accountRepo.findAll();
    }
}
