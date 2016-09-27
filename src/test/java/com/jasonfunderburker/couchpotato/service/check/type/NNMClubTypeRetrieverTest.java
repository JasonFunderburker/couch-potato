package com.jasonfunderburker.couchpotato.service.check.type;

import com.jasonfunderburker.couchpotato.domain.TorrentUserInfo;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

/**
 * Created by Ekaterina.Bashkankova on 27.09.2016
 */
public class NNMClubTypeRetrieverTest {

    @Test
    public void test() {
        TorrentUserInfo userInfo = new TorrentUserInfo();
        userInfo.setUserName("SomeUser");
        userInfo.setHash("somePassword123");

        assertNotEquals("somePassword123", userInfo.getHash());
        assertEquals("somePassword123", userInfo.getPassword());
    }
}
