package com.jasonfunderburker.couchpotato.service.check.type;

import com.jasonfunderburker.couchpotato.domain.TorrentType;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Ekaterina.Bashkankova on 13.09.2016
 */
public class StateRetrieversDictionary {
    private static final Map<TorrentType, TorrentRetriever> retrieverTypeMap;
    static {
        Map<TorrentType, TorrentRetriever> map = new HashMap<>();
        map.put(TorrentType.LOST_FILM, new LostFilmTypeRetriever());
        retrieverTypeMap = Collections.unmodifiableMap(map);
    }

    public static TorrentRetriever getRetrieverType(TorrentType type) {
        if (retrieverTypeMap.containsKey(type))
            return retrieverTypeMap.get(type);
        else
            return null;
    }
}
