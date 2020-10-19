package org.vb.resources;

import com.google.inject.Inject;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import javax.ws.rs.*;

import org.vb.model.CountryCodes;
import org.vb.model.CountryNames;
import org.vb.resourceslibs.APILib;

// NOTE: when creating new resources, don't forget to add it to the list of resources in

@Slf4j
@Path("/api/v1/")
@Produces({"application/json"})
public class APIResource {

    @Inject
    protected APIResource() {};

    /* ============ convert Country_Code -> Country_Name ============ */

    @GET
    @Path("/convert/{countryCodes}")
    @Produces({"application/json"})
    public CountryNames getCountryNames(
            @NonNull @PathParam("countryCodes") String strCountryCodes,
            @QueryParam("refresh") boolean refresh) throws Exception {

        log.info("Method getCountryName. " +
            "Initiate Country Names for Codes: '{}' retrieval from remote API. Refresh flag: {}",
            strCountryCodes,
            refresh);

        CountryNames countryNames = new CountryNames();
        APILib apiLib = new APILib();
        countryNames = apiLib.getCountryNamesBuilder()
                .strCountryCodes(strCountryCodes)
                .bretrieve(refresh)
                .buildgetCountryNames();

        return countryNames;
    }

    /* ============ convert Country_Name -> Country_Code ============ */
    @GET
    @Path("/convertreverse/{countryNames}")
    @Produces({"application/json"})
    public CountryCodes getCountryCodes(
            @NonNull @PathParam("countryNames") String strCountryNames,
            @QueryParam("refresh") boolean refresh) throws Exception {

        log.info("Method getCountryCode. " +
            "Initiate Country Codes {} retrieval from remote API. Refresh flag: {}",
                strCountryNames,
                refresh);

        CountryCodes countryCodes = new CountryCodes();
        APILib apiLib = new APILib();
        countryCodes = apiLib.getCountryCodesBuilder()
                .strCountryNames(strCountryNames)
                .bretrieve(refresh)
                .buildgetCountryCodes();

        return countryCodes;
    }

    /* ============ get API Health ============ */
    /* @GET
    @Path("/health")
    @Produces({"application/json"})
    public HealthCheck getHealthCheck() throws Exception {

        log.info("Method getHealthCheck. " +
                "Initiate API Health Check.");

        return strHealthCheck;
    } */

    /* ============ get Remote API Status ============ */
    /* @GET
    @Path("/diag")
    @Produces({"application/json"})
    public RemoteApiStatus getDiagRemoteApi() throws Exception {

        log.info("Method getDiagRemoteApi. " +
                "Initiate Remote API Diagnostics.");

        return remoteApiStatus;
    } */
}

