package org.restfeeds.server;

import java.util.List;

public interface FeedItemRepository {

  void append(
      String feed,
      String id,
      String type,
      String uri,
      String method,
      String timestamp,
      Object data);

  List<FeedItem> findByFeedPositionGreaterThanEqual(String feed, long position, int limit);
}
