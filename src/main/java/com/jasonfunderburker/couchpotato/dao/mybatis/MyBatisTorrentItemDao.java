package com.jasonfunderburker.couchpotato.dao.mybatis;

import com.jasonfunderburker.couchpotato.dao.TorrentItemDao;
import com.jasonfunderburker.couchpotato.domain.TorrentItem;
import com.jasonfunderburker.couchpotato.domain.TorrentStatus;
import com.jasonfunderburker.couchpotato.mapper.TorrentItemMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by JasonFunderburker on 19.08.2016
 */
@Component
public class MyBatisTorrentItemDao implements TorrentItemDao {
    @Autowired
    TorrentItemMapper mapper;

    @Override
    public TorrentItem findById(Long id) {
        return mapper.findById(id);
    }

    @Override
    public List<TorrentItem> findByStatus(TorrentStatus status) {
        return mapper.findByStatus(status);
    }

    @Override
    public List<TorrentItem> getItemsList() {
        return mapper.getItemsList();
    }

    @Override
    public void updateItem(TorrentItem item) {
        mapper.updateItem(item);
    }

    @Override
    public void addItemToList(TorrentItem item) {
        mapper.addItemToList(item);
    }

    @Override
    public void deleteItemFromList(Long id) {
        mapper.deleteItemFromList(id);
    }

    public TorrentItemMapper getMapper() {
        return mapper;
    }

    public void setMapper(TorrentItemMapper mapper) {
        this.mapper = mapper;
    }
}
