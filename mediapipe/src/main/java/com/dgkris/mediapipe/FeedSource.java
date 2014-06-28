package com.dgkris.mediapipe;

import com.dgkris.mediapipe.feeds.FeedExtractor;
import com.dgkris.mediapipe.feeds.dao.MongoFeedListExtractor;
import com.dgkris.mediapipe.feeds.models.FeedPage;
import com.dgkris.mediapipe.feeds.types.FeedListener;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.flume.Context;
import org.apache.flume.Event;
import org.apache.flume.EventDrivenSource;
import org.apache.flume.channel.ChannelProcessor;
import org.apache.flume.conf.Configurable;
import org.apache.flume.event.EventBuilder;
import org.apache.flume.source.AbstractSource;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Extracts news items from rss sources enlisted in the yaml
 */
public class FeedSource extends AbstractSource
        implements EventDrivenSource,Configurable, FeedListener {

    private static final Logger logger = LoggerFactory.getLogger(FeedSource.class);
    private Gson gson = new Gson();

    private FeedExtractor extractor;

    /**
     * The initialization method for the Source. The context contains all the
     * Flume configuration info, and can be used to retrieve any configuration
     * values necessary to set up the Source.
     */
    @Override
    public void configure(Context context) {
        int crawlingFrequency = context.getInteger(MediaPipeConstants.CRAWLING_FREQ_PARAM_NAME);
        extractor = new FeedExtractor(new MongoFeedListExtractor(), crawlingFrequency);
    }

    /**
     * Start processing events. This uses the Twitter Streaming API to sample
     * Twitter, and process tweets.
     */
    @Override
    public void start() {
        // The channel is the piece of Flume that sits between the Source and Sink,
        // and is used to process events.
        logger.info("FeedSource started");
        extractor.registerListener(this);
        extractor.startThread();
        super.start();
    }

    /**
     * Stops the Source's event processing and shuts down the Twitter stream.
     */
    @Override
    public void stop() {
        logger.info("FeedSource stopped");
        extractor.shutdownThread();
        super.stop();
    }

    @Override
    public void finalize() {
        stop();
    }

    @Override
    public void onNewPage(FeedPage page) {
        logger.info("FeedSource new page received : {}", page.getFeedItemLink());
        final ChannelProcessor channel = getChannelProcessor();
        Event event = EventBuilder.withBody(gson.toJson(page).getBytes());
        channel.processEvent(event);
    }

}