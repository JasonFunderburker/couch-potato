package com.jasonfunderburker.couchpotato.service.check.type;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.jasonfunderburker.couchpotato.domain.TorrentState;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by JasonFunderburker on 12.09.2016
 */
public class LostFilmTypeRetrieverTest {
    private TorrentRetriever retriever = new LostFilmTypeRetriever();
    private HtmlPage checkedPage;

    @Before
    public void before() throws Exception {
        try (final WebClient webClient = new WebClient()) {
            webClient.getOptions().setThrowExceptionOnScriptError(false);
            webClient.getOptions().setJavaScriptEnabled(false);
            checkedPage = webClient.getPage(getClass().getResource("/htmlpageSample.html"));
        }

    }

    @Test
    public void testGetStateWhenSourceMatchesPattern() throws Exception {
        String expectedState = "22";

        TorrentState resultState = retriever.getState(checkedPage);

        assertEquals(expectedState, resultState.getState());
    }


    @Test
    public void testGetName() throws Exception {
        String name = retriever.getName(checkedPage);
        System.out.println(name);
    }
}
