package com.bitzy.urishortener.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "invalid uri")
public class InvalidUriException extends RuntimeException {

  /**
   *
   */
  private static final long serialVersionUID = 1L;
 
}