package com.jts.trippin;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.json.HTTP;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import com.jts.trippin.web.Main;

import lombok.extern.slf4j.Slf4j;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT, classes = Main.class)
@Slf4j
public class IntegrationsODataReadTests {

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
    public void readEntityCollection() {
        String collection = "Products";
        ResponseEntity<String> response =
            rest.getForEntity(URL + collection, String.class);
        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(response.getBody()).contains(
            "\"ID\":5",
            "\"ID\":3",
            "\"ID\":1",
            "\"Description\":\"Optimum Hi-Resolution max. 1600 x 1200 @ 85Hz, Dot Pitch: 0.24mm\"",
            "\"Description\":\"Notebook Basic, 1.7GHz - 15 XGA - 1024MB DDR2 SDRAM - 40GB\"",
            "\"Name\":\"1UMTS PDA\"",
            "1UMTS PDA",
            "Notebook Professional 17");
    }

    @Test
    public void readSingleEntity() {
        String entity = "Products(2)";
        ResponseEntity<String> response =
            rest.getForEntity(URL + entity, String.class);
        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(response.getBody())
            .contains(
                "1UMTS PDA",
                "\"Description\":\"Ultrafast 3G UMTS/HSDPA Pocket PC, supports GSM network\"",
                "\"ID\":2")
            .doesNotContain("Notebook Professional 17");
    }

    @Test
    public void readPrimitiveProperty() {
        String property = "Products(2)/Description";
        ResponseEntity<String> response =
            rest.getForEntity(URL + property, String.class);
        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(response.getBody())
            .contains(
                "Ultrafast 3G UMTS/HSDPA Pocket PC, supports GSM network",
                "\"value\":\"Ultrafast 3G UMTS/HSDPA Pocket PC, supports GSM network\"")
            .doesNotContain("1UMTS PDA");
    }

    @Test
    public void readQuery() {
        String query = "Products?$filter=Name eq 'Notebook Professional 17'&$expand=Category";
        ResponseEntity<String> response =
            rest.getForEntity(URL + query, String.class);
        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(response.getBody())
            .contains(
                "Notebooks",
                "\"Description\":\"Notebook Professional, 2.8GHz - 15 XGA - 8GB DDR3 RAM - 500GB\"",
                "\"Category\":")
            .doesNotContain("1UMTS PDA", "\"Name\":\"Monitors\"");
    }
}
