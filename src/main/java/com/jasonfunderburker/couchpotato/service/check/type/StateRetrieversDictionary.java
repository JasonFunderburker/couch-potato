package com.jasonfunderburker.couchpotato.service.check.type;

import com.jasonfunderburker.couchpotato.entities.TorrentType;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by JasonFunderburker on 13.09.2016
 */
@Repository
public class StateRetrieversDictionary {
    private final Map<TorrentType, TorrentRetriever> retrieverTypeMap;

    public StateRetrieversDictionary(List<TorrentRetriever> torrentRetrieverList) {
        Map<TorrentType, TorrentRetriever> map = new HashMap<>();
        torrentRetrieverList.forEach(t -> map.put(t.getTorrentType(), t));
        retrieverTypeMap = Collections.unmodifiableMap(map);
    }

    public TorrentRetriever getRetrieverType(TorrentType type) {
        if (retrieverTypeMap.containsKey(type))
            return retrieverTypeMap.get(type);
        else
            return null;
    }
}
