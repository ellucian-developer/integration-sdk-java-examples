/*
 * ******************************************************************************
 *   Copyright 2022 Ellucian Company L.P. and its affiliates.
 * ******************************************************************************
 */
package com.ellucian;


import com.ellucian.ethos.integration.client.EthosClientBuilder;
import com.ellucian.ethos.integration.client.proxy.EthosFilterQueryClient;
import com.ellucian.ethos.integration.client.proxy.EthosProxyClient;
import com.ellucian.ethos.integration.client.proxy.EthosProxyClientAsync;

public class ExamplesBase {

    // ==========================================================================
    // Attributes
    // ==========================================================================
    private final String API_KEY = "65a92347-e5d6-4a67-a96e-ae78c4714d6c";

    // ==========================================================================
    // Methods
    // ==========================================================================
    public EthosProxyClient getEthosProxyClient() {
        return new EthosClientBuilder(API_KEY)
                .withConnectionTimeout(30)
                .withConnectionRequestTimeout(30)
                .withSocketTimeout(30)
                .buildEthosProxyClient();
    }

    public EthosProxyClientAsync getEthosProxyClientAsync() {
        return new EthosClientBuilder(API_KEY)
                .withConnectionTimeout(30)
                .withConnectionRequestTimeout(30)
                .withSocketTimeout(30)
                .buildEthosProxyAsyncClient();
    }

    public EthosFilterQueryClient getEthosFilterQueryClient() {
        return new EthosClientBuilder(API_KEY)
                .withConnectionTimeout(30)
                .withConnectionRequestTimeout(30)
                .withSocketTimeout(30)
                .buildEthosFilterQueryClient();
    }

}
