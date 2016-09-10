package org.sulevsky.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Reference {
    private final String ref;
    private final String domain;

//    @JsonCreator
    public Reference(@JsonProperty("reference") String reference,
                     @JsonProperty("domain") String domain) {
        this.ref = reference;
        this.domain = domain;
    }
}
