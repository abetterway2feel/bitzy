package com.bitzy.urishortener.strategy;

public class BitzyGenerationFailureException extends RuntimeException{

  private static final long serialVersionUID = 1L;

  public BitzyGenerationFailureException(String message) {
    super(message);
  }
  
}
