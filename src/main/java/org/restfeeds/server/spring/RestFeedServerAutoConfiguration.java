package org.restfeeds.server.spring;

import org.restfeeds.server.FeedItemRepository;
import org.restfeeds.server.NextLinkBuilder;
import org.restfeeds.server.RestFeedEndpoint;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

@Configuration
@ConditionalOnClass(RestFeedEndpoint.class)
public class RestFeedServerAutoConfiguration {

  @Bean
  @ConditionalOnMissingBean(NextLinkBuilder.class)
  CurrentRequestNextLinkBuilder nextLinkBuilder() {
    return new CurrentRequestNextLinkBuilder();
  }

  @Bean
  @ConditionalOnMissingBean(FeedItemMapper.class)
  FeedItemMapper feedItemMapper(NextLinkBuilder nextLinkBuilder) {
    return new FeedItemMapper(nextLinkBuilder);
  }

  @Bean
  @ConditionalOnClass(JdbcTemplate.class)
  @ConditionalOnMissingBean(FeedItemRepository.class)
  JdbcFeedItemRepository feedItemRepository(JdbcTemplate jdbcTemplate, FeedItemMapper feedItemMapper) {
    return new JdbcFeedItemRepository(jdbcTemplate, feedItemMapper);
  }

  @Bean
  @ConditionalOnMissingBean(RestFeedEndpoint.class)
  RestFeedEndpoint restFeedEndpoint(FeedItemRepository feedItemRepository) {
    return new RestFeedEndpoint(feedItemRepository);
  }
}
