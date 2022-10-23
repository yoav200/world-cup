package com.ab.worldcup.registration.recaptcha;

import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Log4j2
@Service
public class RecaptchaService {

  // call the recpatcha url for verification.
  public static final String url = "https://www.google.com/recaptcha/api/siteverify";

  public static final String secret = "6LfgMaEiAAAAACY2NrZjYJt4Yboj7loymCzPnsN8";


  public boolean isValid(String recaptchaResponse) {

    if (StringUtils.isBlank(recaptchaResponse)) {
      return false;
    }

    RestTemplate restTemplate = new RestTemplate();
    HttpHeaders headers = new HttpHeaders();
    headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);

    UriComponentsBuilder builder = UriComponentsBuilder
        .fromHttpUrl(url).queryParam("secret", secret)
        .queryParam("response", recaptchaResponse);

    HttpEntity<String> entity = new HttpEntity<>(headers);

    ResponseEntity<RecaptchaResponse> response = restTemplate.exchange(
        builder.build().encode().toUri(),
        HttpMethod.GET,
        entity,
        RecaptchaResponse.class);

    if (response.getStatusCode().equals(HttpStatus.OK) && response.getBody() != null) {
      return response.getBody().isSuccess();
    } else {
      return false;
    }
  }
}
