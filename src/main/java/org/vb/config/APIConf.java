package org.vb.config;

import io.dropwizard.Configuration;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class APIConf extends Configuration {
    @Getter
    private String tlsCertPath;

    @Getter
    private String tlsKeyPath;

    @Getter
    private String tlsTrustPath;

    @Getter
    private String tlsIntermediatePath;

    @Getter
    private ApiParameters apiParameters;

}
