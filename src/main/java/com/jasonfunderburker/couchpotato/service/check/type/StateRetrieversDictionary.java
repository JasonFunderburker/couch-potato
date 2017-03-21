package com.jasonfunderburker.couchpotato.service.check.type;

import com.jasonfunderburker.couchpotato.domain.TorrentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by JasonFunderburker on 13.09.2016
 */
@Repository
public class StateRetrieversDictionary {
    private final Map<TorrentType, TorrentRetriever> retrieverTypeMap;
    @Autowired
    private LostFilmTypeRetriever lostFilmTypeRetriever;
    @Autowired
    private NNMClubTypeRetriever nnmClubTypeRetriever;
    @Autowired
    private RutrackerTypeRetriever rutrackerTypeRetriever;
    @Autowired
    private NewStudioTypeRetriever newStudioTypeRetriever;

    public StateRetrieversDictionary() {
        Map<TorrentType, TorrentRetriever> map = new HashMap<>();
        map.put(TorrentType.LOST_FILM, lostFilmTypeRetriever);
        map.put(TorrentType.NNM_CLUB, nnmClubTypeRetriever);
        map.put(TorrentType.RUTRACKER, rutrackerTypeRetriever);
        map.put(TorrentType.NEW_STUDIO, newStudioTypeRetriever);
        retrieverTypeMap = Collections.unmodifiableMap(map);
    }

    public TorrentRetriever getRetrieverType(TorrentType type) {
        if (retrieverTypeMap.containsKey(type))
            return retrieverTypeMap.get(type);
        else
            return null;
    }
}
