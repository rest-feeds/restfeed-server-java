package org.restfeeds.server;

import java.util.Objects;
import java.util.StringJoiner;

public class FeedItem {

  private final String id;
  private final String next;
  private final String type;
  private final String uri;
  private final String method;
  private final String timestamp;
  private final Object data;

  public FeedItem(
      String id,
      String next,
      String type,
      String uri,
      String method,
      String timestamp,
      Object data) {
    this.id = id;
    this.next = next;
    this.type = type;
    this.uri = uri;
    this.method = method;
    this.timestamp = timestamp;
    this.data = data;
  }

  public String getId() {
    return id;
  }

  public String getNext() {
    return next;
  }

  public String getType() {
    return type;
  }

  public String getUri() {
    return uri;
  }

  public String getMethod() {
    return method;
  }

  public String getTimestamp() {
    return timestamp;
  }

  public Object getData() {
    return data;
  }

  @Override
  public String toString() {
    return new StringJoiner(", ", FeedItem.class.getSimpleName() + "[", "]")
        .add("id='" + id + "'")
        .add("next='" + next + "'")
        .add("type='" + type + "'")
        .add("uri='" + uri + "'")
        .add("method='" + method + "'")
        .add("timestamp='" + timestamp + "'")
        .add("data=" + data)
        .toString();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    FeedItem feedItem = (FeedItem) o;
    return Objects.equals(id, feedItem.id)
        && Objects.equals(next, feedItem.next)
        && Objects.equals(type, feedItem.type)
        && Objects.equals(uri, feedItem.uri)
        && Objects.equals(method, feedItem.method)
        && Objects.equals(timestamp, feedItem.timestamp)
        && Objects.equals(data, feedItem.data);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, next, type, uri, method, timestamp, data);
  }
}
