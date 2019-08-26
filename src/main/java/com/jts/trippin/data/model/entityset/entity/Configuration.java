package com.jts.trippin.data.model.entityset.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class Configuration {

    private String cpu;
    private String gpu;
    private String ram;

}
