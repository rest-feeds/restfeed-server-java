package org.restfeeds.server;

import java.util.Objects;
import java.util.StringJoiner;

/**
 * Java bean representing the <a href="https://github.com/rest-feeds/rest-feeds/#model">data
 * model</a> of the returned feed items.
 */
public class FeedItem {

  private String id;
  private String next;
  private String type;
  private String resource;
  private String method;
  private String timestamp;
  private Object data;

  /**
   * Constructor for {@link FeedItem}.
   */
  public FeedItem() {

  }

  /**
   * Constructor for {@link FeedItem}.
   */
  public FeedItem(
      String id,
      String next,
      String type,
      String resource,
      String method,
      String timestamp,
      Object data) {
    this.id = id;
    this.next = next;
    this.type = type;
    this.resource = resource;
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

  public String getResource() {
    return resource;
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
        .add("resource='" + resource + "'")
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
        && Objects.equals(resource, feedItem.resource)
        && Objects.equals(method, feedItem.method)
        && Objects.equals(timestamp, feedItem.timestamp)
        && Objects.equals(data, feedItem.data);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, next, type, resource, method, timestamp, data);
  }
}
