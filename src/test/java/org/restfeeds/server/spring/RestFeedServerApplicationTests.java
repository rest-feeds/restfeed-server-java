package org.restfeeds.server.spring;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.time.Instant;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@SpringBootTest
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureMockMvc
class RestFeedServerApplicationTests {

  @Autowired MockMvc mvc;

  @Autowired JdbcFeedItemRepository feedItemRepository;

  @Test
  void contextLoads() {}

  @Test
  void shouldReturn200() throws Exception {
    mvc.perform(MockMvcRequestBuilders.get("/movies")).andExpect(status().isOk());
  }

  @Test
  void shouldReturnSingleItem() throws Exception {
    feedItemRepository.append(
        "movies",
        "89efb4ff-e995-4f94-ac8c-bf98b81f57f6",
        "application/vnd.org.themoviedb.movie",
        "/movies/123",
        "PUT",
        Instant.now().toString(),
        aMovie());

    mvc.perform(MockMvcRequestBuilders.get("/movies"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("[0].id").value("89efb4ff-e995-4f94-ac8c-bf98b81f57f6"))
        .andExpect(jsonPath("[0].next").value("http://localhost/movies?offset=2"))
        .andExpect(jsonPath("[0].data.original_title").value("Blondie"))
        .andExpect(jsonPath("[1]").doesNotExist());
  }

  @Test
  void shouldReturnItemFromOffset() throws Exception {
    feedItemRepository.append(
        "movies2",
        "89efb4ff-e995-4f94-ac8c-bf98b81f57f6",
        "application/vnd.org.themoviedb.movie",
        "/movies/123",
        "PUT",
        Instant.now().toString(),
        aMovie());
    feedItemRepository.append(
        "movies2",
        "cd5fadad-7c05-47c9-8d66-14b81588d445",
        "application/vnd.org.themoviedb.movie",
        "/movies/124",
        "PUT",
        Instant.now().toString(),
        aMovie());

    mvc.perform(MockMvcRequestBuilders.get("/movies2?offset=2"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("[0].id").value("cd5fadad-7c05-47c9-8d66-14b81588d445"))
        .andExpect(jsonPath("[0].next").value("http://localhost/movies2?offset=3"))
        .andExpect(jsonPath("[1]").doesNotExist());
  }

  @Test
  void nextLinkShouldRespectXForwardedProtoHeader() throws Exception {
    feedItemRepository.append(
        "movies3",
        "535be15c-fefc-412f-8c3b-44364fee5510",
        "application/vnd.org.themoviedb.movie",
        "/movies/123",
        "PUT",
        Instant.now().toString(),
        aMovie());

    mvc.perform(MockMvcRequestBuilders.get("/movies3").header("X-Forwarded-Proto", "https"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("[0].next").value("https://localhost/movies3?offset=2"));
  }

  Movie aMovie() {
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
  }
}
