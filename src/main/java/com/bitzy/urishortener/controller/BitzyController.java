package com.bitzy.urishortener.controller;

import java.util.Optional;

import com.bitzy.urishortener.model.Bitzy;
import com.bitzy.urishortener.model.BitzyURI;
import com.bitzy.urishortener.repository.BitzyRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BitzyController {
  Logger log = LoggerFactory.getLogger(BitzyController.class);

  @Autowired
  private BitzyRepository shortenedUriRepository;

  @Value("${bitzy.url}")
  private String applicationHostUrl;

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  @RequestMapping("/")
  public String create(@RequestBody BitzyURI bitzyUri) {
    log.info("POST: uri='" + bitzyUri + "''");
    if (bitzyUri.isValid()) {
      Bitzy bitzy = shortenedUriRepository.generateBitzyFor(bitzyUri);
      return applicationHostUrl + "/" + bitzy.get();
    } else {
      throw new InvalidUriException();
    }
  }

  @GetMapping("/{bitzy}")
  public ResponseEntity<String> redirectWithUsingRedirectView(@PathVariable String bitzy) {
    log.info("GET: /" + bitzy);
    if (bitzy.length() > Bitzy.BITZY_LENGTH) {
      throw new InvalidUriException();
    }
    Optional<BitzyURI> maybeUri = shortenedUriRepository.getUriFor(new Bitzy(bitzy));
    if (!maybeUri.isPresent()) {
      throw new BitzyNotFoundException();
    }

    HttpHeaders headers = new HttpHeaders();
    String redirectURI = maybeUri.map(u ->  u.toString().startsWith("http") ? u.toString() : "http://"+ u.toString()).get();
    headers.add(HttpHeaders.LOCATION, redirectURI);

    ResponseEntity<String> respone = new ResponseEntity<>(maybeUri.get().toString(), headers,
        HttpStatus.MOVED_PERMANENTLY);

    return respone;
  }
}