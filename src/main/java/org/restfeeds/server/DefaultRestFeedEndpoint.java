package org.restfeeds.server;

import java.time.Duration;

/**
 * Default implementation of {@link RestFeedEndpoint} which uses {@link FeedItem} as a type.
 */
public class DefaultRestFeedEndpoint extends RestFeedEndpoint<FeedItem> {

  public DefaultRestFeedEndpoint(
      DefaultFeedItemRepository feedItemRepository) {
    super(feedItemRepository, new DefaultFeedItemMapper());
  }

  public DefaultRestFeedEndpoint(
      DefaultFeedItemRepository feedItemRepository, Duration pollInterval, Duration timeout) {
    super(feedItemRepository, new DefaultFeedItemMapper(), pollInterval, timeout);
  }
}
