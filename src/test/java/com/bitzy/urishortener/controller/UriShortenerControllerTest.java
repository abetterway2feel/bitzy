package com.bitzy.urishortener.controller;

import static org.assertj.core.api.Assertions.assertThat;

import com.bitzy.urishortener.model.BitzyURI;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT)
@Profile("in-memory")
@ActiveProfiles({ "in-memory" })
public class UriShortenerControllerTest {
	public static BitzyURI LONG_BITZYURI2 = new BitzyURI("http://somethingverylong.com/use/your/imagination?param=x");

	@Value("${bitzy.url}")
	private String applicationHostUrl;

	@Autowired
	private TestRestTemplate restTemplate;

	@Test
	public void postAUriWillReturnABitzy() throws Exception {
		ResponseEntity<String> result = this.restTemplate.postForEntity(applicationHostUrl + "/", new BitzyURI("http://test.com") , String.class);

		assertThat(HttpStatus.CREATED).isEqualTo(result.getStatusCode());
		assertThat(applicationHostUrl + "/" + "UDNO4L").isEqualTo(result.getBody());
	}

	@Test
	public void getABitzyThatWasNeverCreatedWillReturnNotFound() throws Exception {
		String url = applicationHostUrl + "/NoK3y";
		ResponseEntity<String> result = this.restTemplate.getForEntity(url, String.class);

		assertThat(HttpStatus.NOT_FOUND).isEqualTo(result.getStatusCode());
	}

	@Test
	public void getABitzyThatIsInvalidWillReturnBadRequest() throws Exception {
		String url = applicationHostUrl + "/NotAValidBitzy";
		ResponseEntity<String> result = this.restTemplate.getForEntity(url, String.class);

		assertThat(HttpStatus.BAD_REQUEST).isEqualTo(result.getStatusCode());
	}

	@Test
	public void getABitzyThatWillReturnTheCorrectURI() throws Exception {
		ResponseEntity<String> createResult = this.restTemplate.postForEntity(applicationHostUrl + "/", new BitzyURI("http://google.com") , String.class);
		String url = createResult.getBody();

		ResponseEntity<String> result = this.restTemplate.getForEntity(url, String.class);

		// Note that this is the response code from the redirect therefore relies on
		// TestConfig.LONG_BITZYURI existing
		assertThat(HttpStatus.OK).isEqualTo(result.getStatusCode());
	}

	@Test
	public void postAUriWithoutAnSchemeWillHaveASchemeAdded() throws Exception {
		ResponseEntity<String> createResult = this.restTemplate.postForEntity(applicationHostUrl + "/", new BitzyURI("google.com"), String.class);
		String url = createResult.getBody();

		ResponseEntity<String> result = this.restTemplate.getForEntity(url, String.class);

		// Note that this is the response code from the redirect therefore relies on
		// TestConfig.LONG_BITZYURI existing
		assertThat(HttpStatus.OK).isEqualTo(result.getStatusCode());
	}


	@Test
	public void postTheSameUriWillReturnTheSameBitzy() throws Exception {
		ResponseEntity<String> result1 = this.restTemplate.postForEntity(applicationHostUrl + "/", LONG_BITZYURI2, String.class);
		ResponseEntity<String> result2 = this.restTemplate.postForEntity(applicationHostUrl + "/", LONG_BITZYURI2, String.class);

		assertThat(HttpStatus.CREATED).isEqualTo(result1.getStatusCode());
		assertThat(applicationHostUrl + "/" + "bhPE4e").isEqualTo(result1.getBody());
		assertThat(result2.getStatusCode()).isEqualTo(result2.getStatusCode());
		assertThat(result1.getBody()).isEqualTo(result2.getBody());
	}
}