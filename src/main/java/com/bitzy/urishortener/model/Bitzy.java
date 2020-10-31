package com.bitzy.urishortener.model;

import java.util.Objects;

/**
 * A Bitzy is a string of 5 charachters
 */
public class Bitzy {
  public static final int BITZY_LENGTH = 6;
  private String key;

  public Bitzy(String key) {
    if (key.length() > BITZY_LENGTH) {
      throw new IllegalArgumentException("A Bitzy is a key with max length of " + BITZY_LENGTH);
    }
    this.key = key;
  }

  /*
   * Get is the preferred method to retrieve the underyling bitzy. However it uses
   * toString so that these methods are kept in allignment
   */
  public String get() {
    return key.toString();
  }

  @Override
  public boolean equals(Object o) {
    if (o == this)
      return true;
    if (!(o instanceof Bitzy)) {
      return false;
    }
    Bitzy other = (Bitzy) o;
    return Objects.equals(this.key, other.key);
  }

  @Override
  public int hashCode() {
    return Objects.hash(key);
  }

  @Override
  public String toString() {
    return key;
  }
}