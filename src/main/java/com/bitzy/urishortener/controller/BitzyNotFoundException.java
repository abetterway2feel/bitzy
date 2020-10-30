package com.bitzy.urishortener.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "bitzy not found")
public class BitzyNotFoundException extends RuntimeException {

  /**
   *
   */
  private static final long serialVersionUID = 1L;
 
}