package com.jasonfunderburker.couchpotato.entities.rss;

import java.util.Date;

/**
 * Created by JasonFunderburker on 19.10.2016
 */
/*
 * Represents one RSS message
 */
public class RSSFeedMessage {

    String title;
    String description;
    String link;
    String category;
    Date pubDate;
    String guid;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public Date getPubDate() {
        return pubDate;
    }

    public void setPubDate(Date pubDate) {
        this.pubDate = pubDate;
    }

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    @Override
    public String toString() {
        return "RSSFeedMessage{" +
                "title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", link='" + link + '\'' +
                ", category='" + category + '\'' +
                ", pubDate=" + pubDate +
                ", guid='" + guid + '\'' +
                '}';
    }

}
