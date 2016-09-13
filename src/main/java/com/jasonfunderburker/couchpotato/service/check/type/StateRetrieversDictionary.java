package com.jasonfunderburker.couchpotato.service.check.type;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Ekaterina.Bashkankova on 13.09.2016
 */
public class StateRetrieversDictionary {
    private static final Map<String, TorrentStateRetriever> retrieverTypeMap;
    static {
        Map<String, TorrentStateRetriever> map = new HashMap<>();
        map.put("lostfilm", new LostFilmTypeStateRetriever());
        retrieverTypeMap = Collections.unmodifiableMap(map);
    }

    public static TorrentStateRetriever getRetrieverType(String typeName) {
        if (retrieverTypeMap.containsKey(typeName))
            return retrieverTypeMap.get(typeName);
        else
            return null;
    }
}
