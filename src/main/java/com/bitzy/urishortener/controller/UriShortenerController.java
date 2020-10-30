package com.bitzy.urishortener.controller;

import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;

import com.bitzy.urishortener.repository.ShortenedUriRepository;
import com.bitzy.urishortener.service.KeyGeneratorService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UriShortenerController {

  @Autowired
  private ShortenedUriRepository shortenedUriRepository;

  @Autowired
  private KeyGeneratorService keyGeneratorService;

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  @RequestMapping("/shorten")
  public String create(@RequestBody String uri) throws SQLException, NoSuchAlgorithmException {
    String key = keyGeneratorService.generateKey(uri);
    String shortenedKey = shortenedUriRepository.putUri(key, uri);

    return "http://localhost/"+shortenedKey;
  }

  @GetMapping("/{key}")
  public String getUriForKey(@PathVariable String key) throws NotFoundException, SQLException {
    return shortenedUriRepository.getUri(key).orElseThrow(BitzyNotFoundException::new);
  }

}