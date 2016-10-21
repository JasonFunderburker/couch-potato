package com.jasonfunderburker.couchpotato.domain.rss;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

/**
 * Created by JasonFunderburker on 19.10.2016
 */
@JacksonXmlRootElement(localName = "rss")
public class RSSFeed {
    RSSChannel channel;

    public RSSFeed() {
        channel = new RSSChannel();
    }
    public RSSChannel getChannel() {
        return channel;
    }

    public void setChannel(RSSChannel channel) {
        this.channel = channel;
    }

    @Override
    public String toString() {
        return "RSSFeed{" +
                "channel=" + channel +
                '}';
    }
}
