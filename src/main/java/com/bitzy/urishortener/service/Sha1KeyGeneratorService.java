package com.bitzy.urishortener.service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.xml.bind.DatatypeConverter;

import org.springframework.stereotype.Service;

@Service
public class Sha1KeyGeneratorService implements KeyGeneratorService {

  @Override
  public String generateKey(String uri) {
    MessageDigest md;
    try {
      md = MessageDigest.getInstance("SHA1");
    } catch (NoSuchAlgorithmException e) {
      throw new RuntimeException(e);
    }
    md.update("somethingverylong.com/with/lots/of/sub/paths/too".getBytes());

    byte[] digest = md.digest();
    return DatatypeConverter.printBase64Binary(digest).toUpperCase();
  }

}
