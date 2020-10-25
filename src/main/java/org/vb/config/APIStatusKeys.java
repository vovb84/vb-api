package org.vb.config;

import io.dropwizard.Configuration;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class APIStatusKeys extends Configuration {
    @Getter
    private String request;

    @Getter
    private String reply;

}
