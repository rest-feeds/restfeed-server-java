# restfeed-server-java

[![Maven Central](https://maven-badges.herokuapp.com/maven-central/org.restfeeds/restfeed-server/badge.svg)](https://maven-badges.herokuapp.com/maven-central/org.restfeeds/restfeed-server)

Library to provide a [REST Feed](http://rest-feeds.org/) server endpoint in Java.

The library is written in pure Java and has no transitive dependencies.

## Usage

Add this library to your `pom.xml`:

```xml
    <dependency>
      <groupId>org.restfeeds</groupId>
      <artifactId>restfeed-server</artifactId>
      <version>0.0.2</version>
    </dependency>
```

## Spring Boot

Use this library when using Spring Boot:

- [restfeed-server-spring](https://github.com/rest-feeds/restfeed-server-spring)

Feel free to implement the server endpoint in a framework of your choice, such as Java EE, Quarkus, Kotlin, Spring Webflux, etc.

Further examples are highly appreciated.

## Components

### [RestFeedEndpoint](src/main/java/org/restfeeds/server/RestFeedEndpoint.java)

This is the core class that handles long polling.

### [FeedItemRepository](src/main/java/org/restfeeds/server/FeedItemRepository.java)

You need to implement a [FeedItemRepository](src/main/java/org/restfeeds/server/FeedItemRepository.java) with the database of your choice.

Feed items are stored in a _repository_ in chronological order of addition.
An SQL database is a good choice, as it provides auto incrementation of primary keys.
A single partitioned Kafka topic may also be reasonable as repository, but it requires more custom client and offset handling.

Provide access to the database that stores the feed items.

Consider a good primary key that identifies a feed item and guarantees the chronological sequence of addition to the feed.
An auto-incrementing database sequence is a good choice.

### [FeedItem](src/main/java/org/restfeeds/server/FeedItem.java)

Java bean representing the [data model](https://github.com/rest-feeds/rest-feeds/#model) of the returned feed items.

### HTTP endpoint

You need to implement a GET endpoint in a HTTP framework of your choice.

This endpoint must call the `RestFeedEndpoint#fetch` method and pass the item offset and the page limit.
The endpoint must support the `next` link.

This endpoint must support [content negotiation](https://github.com/rest-feeds/rest-feeds/#content-negotiation).

### [NextLinkBuilder](src/main/java/org/restfeeds/server/NextLinkBuilder.java)

You need to implement a [NextLinkBuilder](src/main/java/org/restfeeds/server/NextLinkBuilder.java) to point to your REST endpoint.

The `next` link must provide access to all subsequent feed items.
The next link response must not include the current feed item(s).

It is up to the implementation, how this link is build and evaluated.


