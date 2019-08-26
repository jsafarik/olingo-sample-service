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
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import com.jts.trippin.web.Main;

import java.util.HashMap;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;

/**
 * Because reading is already checked in IntegrationsODataReadTests
 * (and should be tested for every entity/primitive/complex type in non-IntegrationsOData**Tests),
 * trying just updating the same things, that are being updated in syndesis-qe @integrations-odata-read-update
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT, classes = Main.class)
@Slf4j
public class IntegrationsODataReadUpdate {

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
    public void wrongDatashape() throws JSONException {
        readUpdate(2);
    }

    @Test
    public void callingNull() throws JSONException {
        readUpdate(5);
    }

    @Test
    public void NPE() throws JSONException {
        readUpdate(2);
    }

    @Test
    public void strWithoutQuotes() throws JSONException {
        String entity = "Users('coolBob')";

        JSONObject productEntity = new JSONObject(rest.getForEntity(URL + entity, String.class).getBody());
        String lastName = (String) productEntity.get("LastName");
        log.info("Initial first name was: {}", productEntity.get("FirstName"));

        Map<String, Object> product = new HashMap<>();
        product.put("FirstName", lastName);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        ResponseEntity<String> response =
            rest.exchange(URL + entity, HttpMethod.PATCH, new HttpEntity<>(new JSONObject(product).toString(), headers), String.class);
        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        // PATCH doesn't return the updated entity, have to read it again to check
        productEntity = new JSONObject(rest.getForEntity(URL + entity, String.class).getBody());
        log.info("New first name is: {}", productEntity.get("FirstName"));
        assertThat(productEntity.toString()).contains("\"FirstName\":\"" + lastName + "\"");
    }

    /**
     * FROM ODATA DOCUMENTATION:    Enumeration types are named primitive types whose
     *                              values are named constants with underlying integer values.
     *
     * But even with that, it seems that the preferred way to update enum is with String representation
     * @throws JSONException
     */
    @Test
    public void updateGenderWithString() throws JSONException {
        String entity = "Users('coolBob')";

        JSONObject productEntity = new JSONObject(rest.getForEntity(URL + entity, String.class).getBody());
        String gender = (String) productEntity.get("Gender");
        log.info("Initial gender was: {}", productEntity.get("Gender"));

        Map<String, Object> product = new HashMap<>();
        product.put("Gender", "UNSPECIFIED");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        ResponseEntity<String> response =
            rest.exchange(URL + entity, HttpMethod.PATCH, new HttpEntity<>(new JSONObject(product).toString(), headers), String.class);
        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        // PATCH doesn't return the updated entity, have to read it again to check
        productEntity = new JSONObject(rest.getForEntity(URL + entity, String.class).getBody());
        log.info("New gender is: {}", productEntity.get("Gender"));
        assertThat(productEntity.toString()).contains("\"Gender\":\"UNSPECIFIED\"");
    }

    private void readUpdate(int index) throws JSONException {
        String entity = "Products(" + index + ")";

        JSONObject productEntity = new JSONObject(rest.getForEntity(URL + entity, String.class).getBody());
        String name = (String) productEntity.get("Name");
        log.info("Initial description was: {}", (String) productEntity.get("Description"));

        Map<String, Object> product = new HashMap<>();
        product.put("Description", name);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        ResponseEntity<String> response =
            rest.exchange(URL + entity, HttpMethod.PATCH, new HttpEntity<>(new JSONObject(product).toString(), headers), String.class);
        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        // PATCH doesn't return the updated entity, have to read it again to check
        productEntity = new JSONObject(rest.getForEntity(URL + "Products(" + index + ")", String.class).getBody());
        log.info("New description is: {}", productEntity.get("Description"));
        assertThat(productEntity.toString()).contains("\"Description\":\"" + name + "\"");
    }
}
