package com.jasonfunderburker.couchpotato.dao.mybatis;

import com.jasonfunderburker.couchpotato.dao.TorrentItemDao;
import com.jasonfunderburker.couchpotato.domain.TorrentItem;
import com.jasonfunderburker.couchpotato.mapper.TorrentItemMapper;

import java.util.List;

/**
 * Created by Ekaterina.Bashkankova on 19.08.2016
 */
public class MyBatisTorrentItemDao implements TorrentItemDao {
    TorrentItemMapper mapper;

    @Override
    public TorrentItem findById(Long id) {
        return mapper.findById(id);
    }

    @Override
    public List<TorrentItem> getItemsList() {
        return mapper.getItemsList();
    }

    @Override
    public void updateItem(TorrentItem item) {
        mapper.updateItem(item);
    }

    public TorrentItemMapper getMapper() {
        return mapper;
    }

    public void setMapper(TorrentItemMapper mapper) {
        this.mapper = mapper;
    }
}
