package org.restfeeds.server;

/**
 * Default implementation of {@link FeedItemMapper} which uses {@link FeedItem} as a type.
 */
public class DefaultFeedItemMapper implements FeedItemMapper<FeedItem> {

  @Override
  public FeedItem mapToFeedItem(FeedItem source) {
    return source;
  }

  @Override
  public FeedItem mapFromFeedItem(FeedItem source) {
    return source;
  }
}
