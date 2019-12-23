package org.restfeeds.server;

public interface NextLinkBuilder {

  String nextLink(String feed, long currentPosition);

}
