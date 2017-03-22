package com.jasonfunderburker.couchpotato.domain.rss;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

/**
 * Created by JasonFunderburker on 19.10.2016
 */
@JacksonXmlRootElement(localName = "rss")
public class RSSFeed {
    @JacksonXmlProperty(isAttribute = true)
    String version = "2.0";
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

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    @Override
    public String toString() {
        return "RSSFeed{" +
                "channel=" + channel +
                '}';
    }
}
