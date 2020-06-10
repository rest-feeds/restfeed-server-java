package org.restfeeds.server;

import java.util.List;

public interface FeedItemRepository<T> {

  void append(
      String feed,
      String id,
      String type,
      String resource,
      String method,
      String timestamp,
      T data);

  List<FeedItem<T>> findByFeedPositionGreaterThanEqual(String feed, long position, int limit);
}
