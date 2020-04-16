package org.restfeeds.server;

import static java.time.temporal.ChronoUnit.MILLIS;
import static java.time.temporal.ChronoUnit.SECONDS;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class RestFeedEndpointTests {

  @Test
  void shouldCallRepositoryOnceWhenItemsAreReturned() {
    FeedItemRepository<FeedItem> mockedRepository = Mockito.mock(FeedItemRepository.class);
    FeedItemMapper<FeedItem> mockedMapper = Mockito.mock(FeedItemMapper.class);
    RestFeedEndpoint<FeedItem> restFeedEndpoint = new RestFeedEndpoint<>(mockedRepository,
        mockedMapper);
    when(mockedRepository.findByFeedPositionGreaterThanEqual(any(), anyLong(), anyInt()))
        .thenReturn(
            Collections.singletonList(aFeedItem()));

    when(mockedMapper.mapToFeedItem(any())).thenReturn(aFeedItem());

    List<FeedItem> items = restFeedEndpoint.fetch("movies", 0L, 1000);

    assertEquals(1, items.size());
    verify(mockedRepository).findByFeedPositionGreaterThanEqual("movies", 0L, 1000);
    verify(mockedMapper).mapToFeedItem(aFeedItem());
  }

  @Test
  void shouldPollUntilRepositoryReturnsItems() {
    FeedItemRepository<FeedItem> mockedRepository = Mockito.mock(FeedItemRepository.class);
    FeedItemMapper<FeedItem> mockedMapper = Mockito.mock(FeedItemMapper.class);
    RestFeedEndpoint<FeedItem> restFeedEndpoint = new RestFeedEndpoint<>(mockedRepository,
        mockedMapper);
    when(mockedRepository.findByFeedPositionGreaterThanEqual(any(), anyLong(), anyInt()))
        .thenReturn(new ArrayList<>())
        .thenReturn(new ArrayList<>())
        .thenReturn(
            Collections.singletonList(
                new FeedItem(
                    "c82aa148-99d6-4fdd-b50b-138f4ec9790d",
                    "/movies?offset=126",
                    "application/vnd.org.themoviedb.movie",
                    "/movies/18",
                    null,
                    "2019-12-16T08:41:519Z",
                    aMovie())));

    when(mockedMapper.mapToFeedItem(any())).thenReturn(aFeedItem());

    List<FeedItem> items = restFeedEndpoint.fetch("movies", 0L, 1000);

    assertEquals(1, items.size());
    verify(mockedRepository, times(3)).findByFeedPositionGreaterThanEqual("movies", 0L, 1000);
    verify(mockedMapper).mapToFeedItem(aFeedItem());
  }

  @Test
  void shouldPollUntilTimeout() {
    Duration pollInterval = Duration.of(50L, MILLIS);
    Duration timeout = Duration.of(1, SECONDS);
    FeedItemRepository<FeedItem> mockedRepository = Mockito.mock(FeedItemRepository.class);
    FeedItemMapper<FeedItem> mockedMapper = Mockito.mock(FeedItemMapper.class);
    RestFeedEndpoint<FeedItem> restFeedEndpoint =
        new RestFeedEndpoint<>(mockedRepository, mockedMapper, pollInterval, timeout);
    when(mockedRepository.findByFeedPositionGreaterThanEqual(any(), anyLong(), anyInt()))
        .thenReturn(new ArrayList<>());

    when(mockedMapper.mapToFeedItem(any())).thenReturn(aFeedItem());

    List<FeedItem> items = restFeedEndpoint.fetch("movies", 0L, 1000);

    assertTrue(items.isEmpty());
    verify(mockedRepository, atLeast(19)).findByFeedPositionGreaterThanEqual("movies", 0L, 1000);
    verify(mockedMapper, times(0)).mapToFeedItem(aFeedItem());
  }

  private FeedItem aFeedItem() {
    return new FeedItem(
        "c82aa148-99d6-4fdd-b50b-138f4ec9790d",
        "/movies?offset=126",
        "application/vnd.org.themoviedb.movie",
        "/movies/18",
        null,
        "2019-12-16T08:41:519Z",
        aMovie());
  }

  private Movie aMovie() {
    Movie movie = new Movie();
    movie.setId(3924L);
    movie.setOriginal_title("Blondie");
    movie.setPopularity(new BigDecimal("3.653"));
    return movie;
  }

  static class Movie {

    private Long id;
    private String original_title;
    private BigDecimal popularity;

    public Long getId() {
      return id;
    }

    public void setId(Long id) {
      this.id = id;
    }

    public String getOriginal_title() {
      return original_title;
    }

    public void setOriginal_title(String original_title) {
      this.original_title = original_title;
    }

    public BigDecimal getPopularity() {
      return popularity;
    }

    public void setPopularity(BigDecimal popularity) {
      this.popularity = popularity;
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) {
        return true;
      }
      if (o == null || getClass() != o.getClass()) {
        return false;
      }
      Movie movie = (Movie) o;
      return Objects.equals(id, movie.id)
          && Objects.equals(original_title, movie.original_title)
          && Objects.equals(popularity, movie.popularity);
    }

    @Override
    public int hashCode() {
      return Objects.hash(id, original_title, popularity);
    }
  }
}
