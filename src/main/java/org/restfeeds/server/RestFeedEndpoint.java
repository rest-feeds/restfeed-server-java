package org.restfeeds.server;

import static java.lang.String.format;
import static java.time.temporal.ChronoUnit.MILLIS;
import static java.time.temporal.ChronoUnit.SECONDS;

import java.time.Duration;
import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * Core class that handles long polling.
 * @param <T> the type of your objects to be queried.
 */
public class RestFeedEndpoint<T> {

  private static final Logger log = Logger.getLogger(RestFeedEndpoint.class.getName());

  private final FeedItemRepository<T> feedItemRepository;
  private final FeedItemMapper<T> mapper;
  private final Duration pollInterval;
  private final Duration timeout;

  /**
   * Constructor for {@link RestFeedEndpoint}.
   * @param feedItemRepository the repository that is polled
   * @param mapper the mapper that is used to map your repository object to {@link FeedItem}
   */
  public RestFeedEndpoint(FeedItemRepository<T> feedItemRepository, FeedItemMapper<T> mapper) {
    this(feedItemRepository, mapper, Duration.of(50L, MILLIS), Duration.of(5, SECONDS));
  }

  /**
   * Constructor for {@link RestFeedEndpoint}.
   * @param feedItemRepository the repository that is polled
   * @param mapper the mapper that is used to map your repository object to {@link FeedItem}
   * @param pollInterval defines the interval for which the repository is polled
   * @param timeout defines when a request should timeout and return an empty response
   */
  public RestFeedEndpoint(
      FeedItemRepository<T> feedItemRepository, FeedItemMapper<T> mapper, Duration pollInterval,
      Duration timeout) {
    this.feedItemRepository = feedItemRepository;
    this.mapper = mapper;
    this.pollInterval = pollInterval;
    this.timeout = timeout;
  }

  /**
   * Try to fetch items from the repository in a loop until the timeout occurs.
   * @param feed the feed for which should be queried
   * @param offset the offset
   * @param limit the limit of queried elements
   * @return a list of {@link FeedItem}, will be empty if the timeout occurred.
   */
  public List<FeedItem> fetch(String feed, long offset, int limit) {
    Instant timeoutTimestamp = Instant.now().plus(timeout);
    log.fine(
        () -> format("Poll for items in feed %s with offset=%s timeout=%s", feed, offset, timeout));
    while (true) {
      List<T> items = feedItemRepository.findByFeedPositionGreaterThanEqual(feed, offset, limit);

      int numberOfItems = items.size();
      if (numberOfItems > 0) {
        log.fine(() -> format("Returning %s items.", numberOfItems));
        return items.stream().map(mapper::mapToFeedItem).collect(Collectors.toList());
      }

      if (Instant.now().isAfter(timeoutTimestamp)) {
        log.fine("Polling timed out. Returning the empty response.");
        return Collections.emptyList();
      }

      try {
        log.finest("No items found. Wait a bit and then retry again.");
        Thread.sleep(pollInterval.toMillis());
      } catch (InterruptedException e) {
        log.fine("Thread was interrupted. Probably a graceful shutdown. Try to send response.");
        return Collections.emptyList();
      }
    }
  }
}
