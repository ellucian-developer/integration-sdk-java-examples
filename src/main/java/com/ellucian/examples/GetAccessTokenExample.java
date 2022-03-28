/*
 * ******************************************************************************
 *   Copyright 2022 Ellucian Company L.P. and its affiliates.
 * ******************************************************************************
 */
package com.ellucian.examples;

import com.ellucian.ethos.integration.authentication.AccessToken;
import com.ellucian.ethos.integration.client.EthosClientBuilder;
import com.ellucian.ethos.integration.client.proxy.EthosProxyClient;

import java.io.IOException;

/**
 * This example shows how to get an access token itself which is used within the Ethos Integration SDK internally to make API calls.
 * API calls do not require application developers to retrieve an access token.  Access token management is
 * handled by the SDK without the need for developers to retrieve access tokens.  This example only shows how
 * access token information can be obtained.
 * @since 0.0.1
 */
public class GetAccessTokenExample {

    /**
     * Run this sample program.  This has a required program argument of an API key.
     * @param args The string arguments passed into this main method class.
     * @throws Exception Propagates any exception thrown.
     */
    public static void main (String[] args) throws Exception {
        if( args == null || args.length == 0 ) {
            System.out.println("Please enter an API key as a program argument when running this example class.  " +
                               "An API key is required to run these examples.");
            return;
        }
        String apiKey = args[ 0 ];
        getToken(apiKey);
    }


    /**
     * An example of how to get an access token and view the properties.
     * @param apiKey The API key used to get an auth access token.
     * @throws IOException Propagated out if thrown from the EthosClient.
     */
    public static void getToken( String apiKey ) throws IOException {
        // Create an EthosClient using your API key.  Any EthosClient could be used here, so using an EthosProxyClient.
        EthosProxyClient ethosProxyClient = new EthosClientBuilder(apiKey)
                                            .withConnectionTimeout(30)
                                            .withConnectionRequestTimeout(30)
                                            .withSocketTimeout(30)
                                            .buildEthosProxyClient();
        System.out.printf("Using API Key '%s'\n", apiKey);
        System.out.printf("The token provider's AWS region is '%s'\n", ethosProxyClient.getRegion());
        System.out.printf("The token provider's auto-refresh value is '%s'\n", ethosProxyClient.getAutoRefresh());
        System.out.printf("The token provider's configured duration for a token to be valid is %s minutes\n", ethosProxyClient.getExpirationMinutes());

        // Get a new access token and check the properties
        AccessToken token = ethosProxyClient.getAccessToken();
        System.out.printf("The token's valid value is '%b' and it expires at %s\n", token.isValid(), token.getExpirationTime().toLocalTime().toString());
    }

}
