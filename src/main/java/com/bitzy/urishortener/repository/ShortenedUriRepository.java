package com.bitzy.urishortener.repository;

import java.sql.SQLException;
import java.util.Optional;

public interface ShortenedUriRepository {

  /**
   * If the given key exists in the repository then the Uri will be returned as a string
   * otherwise returns and empty option
 * @throws SQLException
   * */
  public Optional<String> getUri(String key) throws SQLException; 

  /**
   * Associates the key with the uri.
   * If the key is taken then a ExistingKey exception will be thrown
 * @throws SQLException
   * */
  public String putUri(String key, String uri) throws SQLException; //will throw an already exists exception
}