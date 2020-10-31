package com.bitzy.urishortener.model;

import java.net.URI;

/**
 * A BitzyURI is wrapper around java.net.URI. Since most of the interaction with
 * URIs is via Strings it is important to make sure the convertsion to and from
 * Strings is consistant.
 */
public class BitzyURI {
  private String uri;

  public BitzyURI() {
  }

  public BitzyURI(String uri) {
    this.uri = uri;
  }

  public void setUri(String uri) {
    this.uri = uri;
  }

  public String getUri() {
    return this.uri;
  }

  public boolean isValid() {
    try {
      URI.create(uri);
      return true;
    } catch (IllegalArgumentException e) {
      return false;
    }
  }

  @Override
  public String toString() {
    return uri.toString();
  }

  public byte[] toBytes() {
    return uri.getBytes();
  }

  @Override
  public boolean equals(Object o) {
    if (o == this)
      return true;
    if (!(o instanceof BitzyURI)) {
      return false;
    }
    BitzyURI other = (BitzyURI) o;
    return other.uri.equals(this.uri);
  }

  @Override
  public int hashCode() {
    return uri.hashCode();
  }
}