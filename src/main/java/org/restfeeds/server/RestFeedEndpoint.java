package org.restfeeds.server;

import static java.time.temporal.ChronoUnit.MILLIS;
import static java.time.temporal.ChronoUnit.SECONDS;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RestFeedEndpoint {

  static final Logger log = LoggerFactory.getLogger(RestFeedEndpoint.class);

  private final FeedItemRepository feedItemRepository;
  private final Duration pollInterval;
  private final Duration timeout;

  public RestFeedEndpoint(FeedItemRepository feedItemRepository) {
    this(feedItemRepository, Duration.of(50L, MILLIS), Duration.of(5, SECONDS));
  }

  public RestFeedEndpoint(
      FeedItemRepository feedItemRepository, Duration pollInterval, Duration timeout) {
    this.feedItemRepository = feedItemRepository;
    this.pollInterval = pollInterval;
    this.timeout = timeout;
  }

  public List<FeedItem> fetch(String feed, long offset, int limit) {
    Instant timeoutTimestamp = Instant.now().plus(timeout);
    log.debug(
        "Polling for items in feed {} with offset {} and a timeout of {}", feed, offset, timeout);
    List<FeedItem> items;
    while (true) {
      items = feedItemRepository.findByFeedPositionGreaterThanEqual(feed, offset, limit);

      if (items.size() > 0) {
        log.debug("Returning {} items.", items.size());
        return items;
      }

      if (Instant.now().isAfter(timeoutTimestamp)) {
        log.debug("Polling timed out. Returning the empty response.");
        return items;
      }

      try {
        log.trace("No items found. Wait a bit and then retry again.");
        Thread.sleep(pollInterval.toMillis());
      } catch (InterruptedException e) {
        log.debug("Thread was interrupted. Probably a graceful shutdown. Try to send response.");
        return items;
      }
    }
  }
}
