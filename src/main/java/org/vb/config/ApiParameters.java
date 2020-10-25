package org.vb.config;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;

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

    @NonNull
    private String localApiUrl;

    @NonNull
    private String localApiPath;

    @NotNull
    private APIKeys apiKeys;

    @NotNull
    private APIStatusKeys apiStatusKeys;

}
