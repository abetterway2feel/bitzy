package com.bitzy.urishortener.controller;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class UriShortenerControllerTest {

	@LocalServerPort
	private int port;

	@Autowired
	private TestRestTemplate restTemplate;

	@Test
	public void postAUriWillReturnAShortUri() throws Exception {

		String url = "http://somethingverylong.com/with/lots/of/sub/paths/too";
		ResponseEntity<String> result = this.restTemplate.postForEntity("http://localhost:" + port + "/shorten", url, String.class);

		assertThat(result.getStatusCode()).isEqualTo(HttpStatus.CREATED);
		assertThat(result.getBody()).isEqualTo("http://localhost/ABC123");

	}

	@Test
	public void getAShortUriWillReturnTheOriginalLongUri() throws Exception {

		String url = "http://localhost:" + port + "/ABC123";
		ResponseEntity<String> result = this.restTemplate.getForEntity(url, String.class);
		
		assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(result.getBody()).isEqualTo("http://somethingverylong.com/with/lots/of/sub/paths/too");

	}
}