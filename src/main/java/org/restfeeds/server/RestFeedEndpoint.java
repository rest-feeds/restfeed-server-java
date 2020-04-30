package org.restfeeds.server;

import static java.lang.String.format;
import static java.time.temporal.ChronoUnit.MILLIS;
import static java.time.temporal.ChronoUnit.SECONDS;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.logging.Logger;

/**
 * Core class that handles long polling.
 * @param <T> the type of your objects to be queried.
 */
public class RestFeedEndpoint<T extends FeedItem> {

  private static final Logger log = Logger.getLogger(RestFeedEndpoint.class.getName());

  private final FeedItemRepository<T> feedItemRepository;
  private final Duration pollInterval;
  private final Duration timeout;

  public RestFeedEndpoint(FeedItemRepository<T> feedItemRepository) {
    this(feedItemRepository, Duration.of(50L, MILLIS), Duration.of(5, SECONDS));
  }

  public RestFeedEndpoint(
      FeedItemRepository<T> feedItemRepository, Duration pollInterval, Duration timeout) {
    this.feedItemRepository = feedItemRepository;
    this.pollInterval = pollInterval;
    this.timeout = timeout;
  }

  public List<T> fetch(String feed, long offset, int limit) {
    Instant timeoutTimestamp = Instant.now().plus(timeout);
    log.fine(
        () -> format("Poll for items in feed %s with offset=%s timeout=%s", feed, offset, timeout));
    List<T> items;
    while (true) {
      items = feedItemRepository.findByFeedPositionGreaterThanEqual(feed, offset, limit);

      int numberOfItems = items.size();
      if (numberOfItems > 0) {
        log.fine(() -> format("Returning %s items.", numberOfItems));
        return items;
      }

      if (Instant.now().isAfter(timeoutTimestamp)) {
        log.fine("Polling timed out. Returning the empty response.");
        return items;
      }

      try {
        log.finest("No items found. Wait a bit and then retry again.");
        Thread.sleep(pollInterval.toMillis());
      } catch (InterruptedException e) {
        log.fine("Thread was interrupted. Probably a graceful shutdown. Try to send response.");
        return items;
      }
    }
  }
}
