package org.sulevsky.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Reference {
    private String reference;
    private String domain;

    @JsonCreator
    public Reference(@JsonProperty("reference") String reference,
                     @JsonProperty("domain") String domain) {
        this.reference = reference;
        this.domain = domain;
    }

    public String getReference() {
        return reference;
    }

    public String getDomain() {
        return domain;
    }

}
