package org.restfeeds.server.spring;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import org.restfeeds.server.FeedItem;
import org.restfeeds.server.NextLinkBuilder;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

public class FeedItemMapper implements RowMapper<FeedItem> {

  private final NextLinkBuilder nextLinkBuilder;

  FeedItemMapper(NextLinkBuilder nextLinkBuilder) {
    this.nextLinkBuilder = nextLinkBuilder;
  }

  @Override
  public FeedItem mapRow(ResultSet rs, int rowNum) throws SQLException {
    long position = rs.getLong("position");
    String feed = rs.getString("feed");
    String id = rs.getString("id");
    String type = rs.getString("type");
    String uri = rs.getString("uri");
    String method = rs.getString("method");
    Timestamp timestamp = rs.getTimestamp("timestamp");
    String data = rs.getString("data");
    String nextLink = nextLinkBuilder.nextLink(feed, position);
    String timestampAsString = timestamp == null ? null : timestamp.toInstant().toString();
    Object dataAsObject = DataSerializer.toObject(data);
    return new FeedItem(id, nextLink, type, uri, method, timestampAsString, dataAsObject);
  }
}
