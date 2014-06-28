package com.dgkris.mediapipe.feeds.parser;

import com.dgkris.mediapipe.feeds.models.Feed;
import com.dgkris.mediapipe.feeds.models.FeedItem;
import com.dgkris.mediapipe.feeds.models.FeedPage;
import com.dgkris.mediapipe.utils.Utils;
import de.jetwick.snacktory.HtmlFetcher;
import de.jetwick.snacktory.JResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;

/**
 * Extracts info out of the url received from a feed
 */
public class HTMLParser {

    private static final Logger logger = LoggerFactory.getLogger(HTMLParser.class);

    public FeedPage getFeedPageFromUrl(String url, FeedItem feedItem) {
        logger.info("HTML and text extraction for url : {} started", url);
        FeedPage feedPage = new FeedPage();
        try {
            String html = Utils.getStringFromStream(new URL(url).openConnection().getInputStream());
            HtmlFetcher fetcher = new HtmlFetcher();
            JResult res = fetcher.fetchAndExtract(url, 30000, true);
            setFeedItemInfo(feedPage, feedItem);
            setFeedInfo(feedPage,feedItem.getParentFeed());
            feedPage.setBestGuessRelevantText(res.getText());
            feedPage.setDecodedPubTime(Utils.convertToDateTime(feedItem.getPubDate()).toString());
            feedPage.setFullText(html);
            return feedPage;
        } catch (Exception e) {
            logger.info("Exception : {}", e.getLocalizedMessage());
            return null;
        }
    }

    private void setFeedInfo(FeedPage feedPage, Feed feed) {
        feedPage.setFeedCopyRight(feed.getCopyright());
        feedPage.setFeedDesc(feed.getDescription());
        feedPage.setFeedLanguage(feed.getLanguage());
        feedPage.setFeedLink(feed.getLink());
        feedPage.setFeedPubDate(feed.getPubDate());
        feedPage.setFeedTitle(feed.getTitle());
    }

    private void setFeedItemInfo(FeedPage feedPage, FeedItem feedItem) {
        feedPage.setFeedItemAuthor(feedItem.getAuthor());
        feedPage.setFeedItemDescription(feedItem.getDescription());
        feedPage.setFeedItemGuid(feedItem.getGuid());
        feedPage.setFeedItemLink(feedItem.getLink());
        feedPage.setFeedItemPubDate(feedItem.getPubDate());
        feedPage.setFeedItemTitle(feedItem.getTitle());
    }
}