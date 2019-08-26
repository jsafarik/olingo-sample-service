package com.jts.trippin;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import com.jts.trippin.web.Main;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT, classes = Main.class)
public class MetadataTests {

    private final String URL = "http://localhost:8080/odata.svc/";

    @Autowired
    private TestRestTemplate rest;

    @Test
    public void checkMetadataCorrect() {
        ResponseEntity<String> response =
            rest.getForEntity(URL + "$metadata", String.class);
        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(response.getBody())
            .contains(
                "OData.TripPin.Category",
                "OData.TripPin.Product",
                "OData.TripPin.User",
                "OData.TripPin.Gender",
                "OData.TripPin.Advertisement",
                "OData.TripPin.Configuration");
    }
}
