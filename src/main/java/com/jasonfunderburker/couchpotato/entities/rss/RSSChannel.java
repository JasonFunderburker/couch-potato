package com.jasonfunderburker.couchpotato.entities.rss;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by JasonFunderburker on 21.10.2016
 */
public class RSSChannel {
    String title = "Couch-Potato";
    String link = "{tomcat_url}/";
    String description = "Couch-Potato service rss feed";

    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "item")
    List<RSSFeedMessage> entries = new ArrayList<>();

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<RSSFeedMessage> getEntries() {
        return entries;
    }

    public void setEntries(List<RSSFeedMessage> entries) {
        this.entries = entries;
    }

    @Override
    public String toString() {
        return "RSSChannel{" +
                "title='" + title + '\'' +
                ", link='" + link + '\'' +
                ", description='" + description + '\'' +
                ", entries=" + entries +
                '}';
    }
}
