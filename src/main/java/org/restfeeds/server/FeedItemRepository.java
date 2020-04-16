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
      Object data);

  void append(String feed, T feedItem);

  List<T> findByFeedPositionGreaterThanEqual(String feed, long position, int limit);
}
