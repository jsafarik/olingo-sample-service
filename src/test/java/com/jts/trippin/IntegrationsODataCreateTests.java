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
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import com.jts.trippin.web.Main;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT, classes = Main.class)
@Slf4j
public class IntegrationsODataCreateTests {

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
    public void createProduct() throws JSONException {
        String collection = "Products";

        Map<String, Object> product = new HashMap<>();
        String name = "Raspberry Pi 4 B";
        String desc = "Pocket Computer";
        product.put("Name", name);
        product.put("Description", desc);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        ResponseEntity<String> response =
            rest.postForEntity(URL + collection, new HttpEntity<>(new JSONObject(product).toString(), headers), String.class);
        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(response.getBody()).contains(name, desc);
        log.info("Created entity was: {}", response.getBody());
        int id = (int) new JSONObject(response.getBody()).get("ID");
        assertThat(rest.getForEntity(URL + "Products("+ String.valueOf(id) +")", String.class).getBody()).contains(name, desc);
    }

}
