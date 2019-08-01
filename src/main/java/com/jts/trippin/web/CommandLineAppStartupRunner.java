package com.jts.trippin.web;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class CommandLineAppStartupRunner implements CommandLineRunner {

    @Override
    public void run(String... args) throws Exception {
        log.info("OData service has started");
    }

}
