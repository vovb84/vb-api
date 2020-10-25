package org.vb.config;

import io.dropwizard.Configuration;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class APIKeys extends Configuration {
    @Getter
    private String data;

    @Getter
    private String apiStatus;

}
