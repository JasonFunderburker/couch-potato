package com.jasonfunderburker.couchpotato.service.check;

import com.jasonfunderburker.couchpotato.domain.TorrentItem;

/**
 * Created by JasonFunderburker on 01.09.2016
 */
public interface TorrentCheckService {

    void check(TorrentItem item);

}
