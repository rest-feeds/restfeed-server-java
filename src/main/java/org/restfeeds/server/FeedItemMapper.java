package org.restfeeds.server;

public interface FeedItemMapper<T> {

  FeedItem mapToFeedItem(T source);

  T mapFromFeedItem(FeedItem source);

}
