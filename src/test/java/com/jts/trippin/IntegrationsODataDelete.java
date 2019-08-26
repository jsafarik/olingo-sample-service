package com.jts.trippin;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import com.jts.trippin.web.Main;

import lombok.extern.slf4j.Slf4j;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT, classes = Main.class)
@Slf4j
public class IntegrationsODataDelete {

    private final String URL = "http://localhost:8080/odata.svc/";

    @Autowired
    private TestRestTemplate rest;

    @Before
    public void setUp() throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        ResponseEntity<String> response = rest.postForEntity(URL + "Reset", new HttpEntity<>("{}", headers), String.class);
        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        log.info("Done service reset\n");
    }

    @Test
    public void readEntityCollection() throws JSONException {
        String entity = "Products(1)";
        rest.delete(URL + entity);
        ResponseEntity<String> response = rest.getForEntity(URL + entity, String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

}
