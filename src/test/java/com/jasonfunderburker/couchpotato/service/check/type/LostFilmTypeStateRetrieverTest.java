package com.jasonfunderburker.couchpotato.service.check.type;

import com.jasonfunderburker.couchpotato.domain.TorrentState;
import com.jasonfunderburker.couchpotato.exceptions.TorrentStateRetrieveException;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by JasonFunderburker on 12.09.2016
 */
public class LostFilmTypeStateRetrieverTest {
    private TorrentStateRetriever retriever = new LostFilmTypeStateRetriever();

    @Test
    public void testGetStateWhenSourceMatchesPattern() throws Exception {
        String expectedState = "20";
        String source = "<td class=\"t_episode_num\" rowspan=\"2\">\n"+expectedState+"\n</td>";

        TorrentState resultState = retriever.getState(source);

        assertEquals(expectedState, resultState.getState());
    }


    @Test(expected = TorrentStateRetrieveException.class)
    public void testGetStateWhenSourceNotMatchesPattern() throws Exception {
        retriever.getState("something");
    }
}
