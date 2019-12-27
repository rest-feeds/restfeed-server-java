# restfeed-server-java

[![Maven Central](https://maven-badges.herokuapp.com/maven-central/org.restfeeds/restfeed-server/badge.svg)](https://maven-badges.herokuapp.com/maven-central/org.restfeeds/restfeed-server)

Library to provide a [REST Feed](http://rest-feeds.org/) server endpoint in Java.

An included Spring Boot Auto Configuration simplifies the implementation as a Spring application.

## Getting Started 

Go to [start.spring.io](https://start.spring.io/#!type=maven-project&language=java&platformVersion=2.2.2.RELEASE&packaging=jar&jvmVersion=1.8&groupId=com.example&artifactId=restfeed-server-example&name=restfeed-server-example&description=Demo%20project%20for%20Spring%20Boot&packageName=com.example.restfeed-server-example&dependencies=web,jdbc,h2) and create an new application. Select these dependencies:

- Spring Web (to provide an HTTP endpoint)
- JDBC API (for database connectivity)

for testing, you might also want to add 

- H2 Database

Then add this library to your `pom.xml`:

```xml
    <dependency>
      <groupId>org.restfeeds</groupId>
      <artifactId>restfeed-server</artifactId>
      <version>0.0.1</version>
    </dependency>
```

The [`RestFeedServerAutoConfiguration`](src/main/java/org/restfeeds/server/spring/RestFeedServerAutoConfiguration.java) adds all relevant beans.
You only need to add a `@RestController` that calls the `RestFeedEndpoint#fetch` method, 
or use the generic `RestFeedEndpointController` by registering it as a bean.

```java
@SpringBootApplication
public class RestFeedServerApplication {

  public static void main(String[] args) {
    SpringApplication.run(RestFeedServerApplication.class, args);
  }

  @Bean
  public RestFeedEndpointController restFeedEndpointController(RestFeedEndpoint restFeedEndpoint) {
    return new RestFeedEndpointController(restFeedEndpoint);
  }
}
```

Next, make sure to have a valid schema for you database set up (use [Flyway](https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/#howto-use-a-higher-level-database-migration-tool) or the [schema.sql](https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/#howto-initialize-a-database-using-spring-jdbc) file):

```sql
create table feed
(
    position identity primary key,
    feed     varchar(1024) not null,
    id       varchar(1024) not null,
    type     varchar(1024),
    resource varchar(1024),
    method   varchar(1024),
    timestamp timestamp,
    data      clob
);

create index feed_position ON feed(feed, position);
```

and make sure your database is connected in your `application.properties`:

```properties
spring.datasource.url=jdbc:h2:mem:testdb
```

Finally, make sure that your application adds new feed items by calling the `FeedItemRepository#append` method.

```java
feedItemRepository.append(
    "myfeed",
    UUID.randomUUID().toString(),
    "application/vnd.org.example.resource",
    "/myresource/123",
    "PUT",
    Instant.now().toString(),
    data);
```

When you start the application, you can connect to http://localhost:8080/myfeed.

Find a fully working example at https://github.com/rest-feeds/rest-feed-server-example-spring-web.

## Components

When providing a server, you need to provide implementations for these components:

### FeedItemRepository

Default implementation for Spring: org.restfeeds.server.spring.JdbcFeedItemRepository

Feed items are stored in a _repository_ in chronological order of addition.
An SQL database is a good choice, as it provides auto incrementation of primary keys.
A single partitioned Kafka topic may also be reasonable as repository, but it requires more custom client and offset handling.

Provide access to the database that stores the feed items.

Consider a good primary key that identifies a feed item and guarantees the chronological sequence of addition to the feed.
An auto-incrementing database sequence is a good choice.

The RestFeedEndpoint polls the repository every few milliseconds for new items.
Make sure that the fields used for `feed` (if any) and `position` are indexed.

### NextLinkBuilder

Default implementation for Spring: org.restfeeds.server.spring.CurrentRequestNextLinkBuilder

The `next` link must provide access to all subsequent feed items.
The next link must not include the current feed items.

It is up to the implementation, how this link is build and evaluated.

### HTTP endpoint

Default implementation for Spring: org.restfeeds.server.spring.RestFeedEndpointController

GET endpoint to access the feed, both the base URL and the `next` URLs.

This endpoint must call the `RestFeedEndpoint#fetch` method and pass the item offset and the page limit.

This endpoint (or the framework) must implement content negotiation.


## Other Java Stacks

The library is written in pure Java and has no transitive compile dependencies.

Feel free to implement your endpoint in Java EE, Quarkus, Kotlin, Spring Webflux, etc.

Further examples are highly appreciated.
