package com.bitzy.urishortener.strategy;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.xml.bind.DatatypeConverter;

import com.bitzy.urishortener.model.Bitzy;
import com.bitzy.urishortener.model.BitzyURI;

/**
 * This BitzyGeneratorStrategy uses a SHA1 algorithm to create a hash of the
 * uri, encodes this using base64 and then chooses at random the number of
 * charachters required to create the Bitzy
 * 
 * Note that this could result in colissions but the number of Bitzys that would
 * need to to be generated would be extrodinarily rare.
 * 
 */
public class Sha1BitzyGeneratorStrategy implements BitzyGeneratorStrategy {

  private BitzyURI bitzyUri;
  private int attempts;
  private String base64SHA1Hash;

  public Sha1BitzyGeneratorStrategy(BitzyURI bitzyUri) {
    this.bitzyUri = bitzyUri;
    this.attempts = 0;
    genereateBase64SHA1Hash();
  }

  private void genereateBase64SHA1Hash() {
    MessageDigest md;
    try {
      md = MessageDigest.getInstance("SHA1");
      md.update(bitzyUri.toBytes());
      byte[] digest = md.digest();
      base64SHA1Hash = DatatypeConverter.printBase64Binary(digest).replace("+", "").replace("/", "").replace("=", "");
    } catch (NoSuchAlgorithmException e) {
      throw new RuntimeException("Algorithm for MessageDigest.getInstance should be 'SHA1'");
    }
  }

  @Override
  public Bitzy next() {
    int startindex = attempts;
    try {
      return new Bitzy(base64SHA1Hash.substring(startindex, startindex + Bitzy.BITZY_LENGTH));
    } catch (IndexOutOfBoundsException e) {
      throw new BitzyGenerationFailureException("All posible bitzys for uri have been exhausted");
    } finally {
      attempts++;

    }
  }

  /**
   * Checks if the number of attempts for this uri have already exhausted the
   * number of possible bitzys from within the hash
   */
  @Override
  public boolean hasNext() {
    return (Bitzy.BITZY_LENGTH + attempts < base64SHA1Hash.length()) ? true : false;
  }

}
