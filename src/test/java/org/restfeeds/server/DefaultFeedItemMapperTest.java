package org.restfeeds.server;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class DefaultFeedItemMapperTest {

  @Test
  void shouldMapCorrectlyToFeedItem() {
    DefaultFeedItemMapper feedItemMapper = new DefaultFeedItemMapper();

    FeedItem feedItem = aFeedItem();
    FeedItem mappedFeedItem = feedItemMapper.mapToFeedItem(feedItem);
    assertEquals(feedItem, mappedFeedItem);
  }

  @Test
  void shouldMapCorrectlyFromFeedItem() {
    DefaultFeedItemMapper feedItemMapper = new DefaultFeedItemMapper();

    FeedItem feedItem = aFeedItem();
    FeedItem mappedFeedItem = feedItemMapper.mapFromFeedItem(feedItem);
    assertEquals(feedItem, mappedFeedItem);
  }

  private FeedItem aFeedItem() {
    return new FeedItem(
        "c82aa148-99d6-4fdd-b50b-138f4ec9790d",
        "/movies?offset=126",
        "application/vnd.org.themoviedb.movie",
        "/movies/18",
        null,
        "2019-12-16T08:41:519Z",
        "myData");
  }

}
