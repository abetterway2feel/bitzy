package com.bitzy.urishortener.repository;

import java.sql.SQLException;
import java.util.Optional;

import com.bitzy.urishortener.model.Bitzy;
import com.bitzy.urishortener.model.BitzyURI;


public interface BitzyRepository {

  /**
   * If the given key exists in the repository then the Uri will be returned as a string
   * otherwise returns and empty option
 * @throws SQLException
   * */
  public Optional<BitzyURI> getUriFor(Bitzy bitzy); 

  /**
   * Create a Bitzy for the given URI
   * 
 * @throws SQLException
   * */
  public Bitzy generateBitzyFor(BitzyURI uri); 

}