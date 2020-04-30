package org.restfeeds.server;

import java.util.List;

public interface FeedItemRepository<T extends FeedItem> {

  void append(
      String feed,
      String id,
      String type,
      String resource,
      String method,
      String timestamp,
      Object data);

  <S extends T> S append(String feed, S feedItem);

  List<T> findByFeedPositionGreaterThanEqual(String feed, long position, int limit);
}
