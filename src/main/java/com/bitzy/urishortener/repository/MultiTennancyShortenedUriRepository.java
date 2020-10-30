package com.bitzy.urishortener.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.Optional;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

/**
 * A Repository that
 */
@Repository
public class MultiTennancyShortenedUriRepository implements ShortenedUriRepository {

  private DataSource[] datasources;

  private int partitions;

  public MultiTennancyShortenedUriRepository(DataSource[] datasources,
      @Qualifier("numberOfPartitions") int partitions) {
    this.datasources = datasources;
    this.partitions = partitions;
  }

  @Override
  public Optional<String> getUri(String key) throws SQLException {

    int partition = key.hashCode() % this.partitions;
    int db = partition % this.datasources.length;

  
    try(Connection con = datasources[db].getConnection()){
      PreparedStatement ps = con.prepareStatement("SELECT uri FROM shortened_uri WHERE key=?");
      ps.setString(1, key);
      ResultSet rs = ps.executeQuery();
  
      Optional<String> maybeUri;
      if(rs.first()){
        maybeUri = Optional.of(rs.getString("uri"));
      } else{
        maybeUri = Optional.empty();
      }
      return maybeUri;
    }
    catch(SQLException e){
      //TODO: this should be wrapped a repo wrapper returned
      throw e;
    }
  }

  @Override
  public String putUri(String key, String uri) throws SQLException {
    int partition = Math.abs(key.hashCode()) % this.partitions;
    int db = partition % this.datasources.length;
    String smallKey = key.substring(0,5);

   
    try(Connection con = datasources[db].getConnection()){
      PreparedStatement ps = con.prepareStatement("INSERT INTO shortened_uri values (?,?,?,?)");
      ps.setString(1, smallKey);
      ps.setString(2, uri);
      ps.setInt(3, partition);
      ps.setTimestamp(4, Timestamp.from(Instant.now()));
      return smallKey;
    }
    catch(SQLException e){
      
      //TODO: deal with collisions
      //TODO: this should be wrapped a repo wrapper returned
      throw new RuntimeException(e.getMessage());
    }
  }

}
