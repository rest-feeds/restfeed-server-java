package org.restfeeds.server.spring;

import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import org.restfeeds.server.NextLinkBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

public class CurrentRequestNextLinkBuilder implements NextLinkBuilder {

  @Override
  public String nextLink(String feed, long currentPosition) {
    long nextPosition = currentPosition + 1;
    ServletUriComponentsBuilder builder = ServletUriComponentsBuilder.fromCurrentRequestUri();
    builder.replaceQueryParam("offset", nextPosition);

    // ServletUriComponentsBuilder does not reflect the x-forwarded-proto header?
    adjustHttpSchemeFromXForwardedProtoHeader(builder);

    return builder.toUriString();
  }

  private void adjustHttpSchemeFromXForwardedProtoHeader(ServletUriComponentsBuilder builder) {
    getCurrentHttpRequest()
        .ifPresent(
            request -> {
              String protoHeader = request.getHeader("x-forwarded-proto");
              if (protoHeader != null) {
                builder.scheme(protoHeader);
              }
            });
  }

  public static Optional<HttpServletRequest> getCurrentHttpRequest() {
    return Optional.ofNullable(RequestContextHolder.getRequestAttributes())
        .filter(
            requestAttributes ->
                ServletRequestAttributes.class.isAssignableFrom(requestAttributes.getClass()))
        .map(requestAttributes -> ((ServletRequestAttributes) requestAttributes))
        .map(ServletRequestAttributes::getRequest);
  }
}
