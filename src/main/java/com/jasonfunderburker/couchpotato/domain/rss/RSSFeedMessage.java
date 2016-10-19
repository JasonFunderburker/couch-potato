package com.jasonfunderburker.couchpotato.domain.rss;

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

    @Override
    public String toString() {
        return "FeedMessage [title=" + title + ", description=" + description + ", link=" + link + "]";
    }

}
