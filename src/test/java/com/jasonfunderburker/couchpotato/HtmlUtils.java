package com.jasonfunderburker.couchpotato;

import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlElement;

/**
 * Created by JasonFunderburker on 08.04.17.
 */
public class HtmlUtils {
    public static Page preparePage(String resourceString) throws Exception {
        try (final WebClient webClient = new WebClient()) {
            webClient.getOptions().setJavaScriptEnabled(false);
            webClient.getOptions().setThrowExceptionOnScriptError(false);
            webClient.getOptions().setCssEnabled(false);
            return webClient.getPage(HtmlUtils.class.getResource(resourceString));
        }
    }
}
