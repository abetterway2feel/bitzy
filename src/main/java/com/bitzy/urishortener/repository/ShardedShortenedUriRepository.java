package com.bitzy.urishortener.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Timestamp;
import java.time.Clock;
import java.time.Instant;
import java.util.Optional;

import javax.sql.DataSource;

import com.bitzy.urishortener.model.Bitzy;
import com.bitzy.urishortener.model.BitzyURI;
import com.bitzy.urishortener.strategy.BitzyGeneratorStrategy;
import com.bitzy.urishortener.strategy.Sha1BitzyGeneratorStrategy;

import org.postgresql.util.PSQLException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

/**
 * A ShortenedUriRepository based that supports the data being sharded accross
 * multiple datasources.
 * 
 * Datasource selection is based on the retrieval of a longKey
 * 
 */
@Repository
public class ShardedShortenedUriRepository implements BitzyRepository {

  //a change to NUMBER_OF_PARTITIONS would require a rehash of all data
  private static final int NUMBER_OF_PARTITIONS = 10;

  private DataSource[] datasources;

  public ShardedShortenedUriRepository(@Qualifier("datasources") DataSource[] datasources) {
    this.datasources = datasources;
  }

  @Override
  public Optional<BitzyURI> getUriFor(Bitzy bitzy) {
    DataSource datasource = selectDataSourceFor(bitzy);

    try (Connection con = datasource.getConnection()) {
      PreparedStatement ps = con.prepareStatement("SELECT uri FROM shortened_uri WHERE key=?");
      ps.setString(1, bitzy.get());
      ResultSet rs = ps.executeQuery();

      Optional<BitzyURI> maybeUri;
      if (rs.next()) {
        maybeUri = Optional.of(new BitzyURI(rs.getString("uri")));
      } else {
        maybeUri = Optional.empty();
      }
      return maybeUri;
    } catch (SQLException e) {
      throw new RuntimeException("Retrieving has failed:", e);
    } catch (IllegalArgumentException e) {
      throw new RuntimeException("An invalid URI has been stored in the db:", e);
    }
  }

  @Override
  public Bitzy generateBitzyFor(BitzyURI bitzyUri) {
    //TODO: it would be beter to inject this as a facory so that the 
    //respository doesnt need to know that its the SHA1 implementation
    BitzyGeneratorStrategy bitzyGenerator = new Sha1BitzyGeneratorStrategy(bitzyUri);
    return generateBitzyFor(bitzyGenerator, bitzyUri);
  }

  private Bitzy generateBitzyFor(BitzyGeneratorStrategy bitzyGenerator, BitzyURI bitzyURI) {
    Bitzy bitzy = bitzyGenerator.next();
    try (Connection con = selectDataSourceFor(bitzy).getConnection()) {
      PreparedStatement ps = con.prepareStatement("INSERT INTO shortened_uri values (?,?,?,?)");
      ps.setString(1, bitzy.get());
      ps.setString(2, bitzyURI.toString());
      ps.setInt(3, getPartitionFor(bitzy));
      ps.setTimestamp(4, Timestamp.from(Instant.now(Clock.systemUTC())));
      ps.execute();
      return bitzy;
    } catch (PSQLException | SQLIntegrityConstraintViolationException e) {
      Optional<BitzyURI> maybeUri = getUriFor(bitzy);
      if (maybeUri.isPresent()) {
        if (maybeUri.get().equals(bitzyURI)) {
          return bitzy;
        } else {
          return generateBitzyFor(bitzyGenerator, bitzyURI);
        }
      } else {
        throw new RuntimeException("SQLIntegrityConstraintViolationException but the key was not in the database?");
      }
    } catch (SQLException e) {
      throw new RuntimeException(e.getMessage());
    }
  }

  private int getPartitionFor(Bitzy bitzy) {
    return Math.abs(bitzy.hashCode()) % NUMBER_OF_PARTITIONS;
  }

  private DataSource selectDataSourceFor(Bitzy bitzy) {
    int db = getPartitionFor(bitzy) % this.datasources.length;
    return datasources[db];
  }
}
