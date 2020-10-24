package org.vb.config;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
@ToString
public class ApiParameters {

    @NonNull
    private String remoteApiUrl;

    @NonNull
    private String remoteApiPath;

    @NonNull
    private String localDir;

    @NonNull
    private String localFile;

}
