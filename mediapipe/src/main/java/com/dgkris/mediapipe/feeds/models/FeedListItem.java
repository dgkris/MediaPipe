package com.dgkris.mediapipe.feeds.models;

import org.bson.types.ObjectId;

/**
 * Represents one row of the mongo collection : FeedListItem
 * (id,feedurl,state,country,name, type)
 */
public class FeedListItem {

    private ObjectId feedId;
    private String publisherName;
    private String feedUrl;
    private String state, country;


    public FeedListItem() {

    }

    public String getFeedUrl() {
        return feedUrl;
    }

    public void setFeedUrl(String feedUrl) {
        this.feedUrl = feedUrl;
    }

    public ObjectId getFeedId() {
        return feedId;
    }

    public void setFeedId(ObjectId feedId) {
        this.feedId = feedId;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getPublisherName() {
        return publisherName;
    }

    public void setPublisherName(String publisherName) {
        this.publisherName = publisherName;
    }

}
