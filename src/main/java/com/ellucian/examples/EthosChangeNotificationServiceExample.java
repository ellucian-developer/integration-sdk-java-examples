/*
 * ******************************************************************************
 *   Copyright 2022 Ellucian Company L.P. and its affiliates.
 * ******************************************************************************
 */
package com.ellucian.examples;


import com.ellucian.ethos.integration.client.EthosClientBuilder;
import com.ellucian.ethos.integration.client.messages.ChangeNotification;
import com.ellucian.ethos.integration.service.EthosChangeNotificationService;

import java.io.IOException;
import java.util.List;

/**
 * This is an example class that shows how to use the EthosChangeNotificationService to retrieve change notification messages.
 *
 * Prerequisite to running this example class:
 * Configuration must be previously setup properly in Ethos Integration, and messages must be waiting for consumption
 * in order to consume messages using the EthosChangeNotificationService.
 */
public class EthosChangeNotificationServiceExample {

    // ==========================================================================
    // Attributes
    // ==========================================================================
    private String apiKey;

    public EthosChangeNotificationServiceExample(String apiKey ) {
        this.apiKey = apiKey;
    }


    // ==========================================================================
    // Methods
    // ==========================================================================
    public static void main(String[] args) {
        if( args == null || args.length == 0 ) {
            System.out.println("Please enter an API key as a program argument when running this example class.  " +
                               "An API key is required to run these examples.");
            return;
        }
        String apiKey = args[ 0 ];
        EthosChangeNotificationServiceExample ethosChangeNotificationServiceExample = new EthosChangeNotificationServiceExample( apiKey );
        ethosChangeNotificationServiceExample.getNotificationsWithoutOverridesExample();
        ethosChangeNotificationServiceExample.getNotificationsWithOverridesExample();
    }

    /**
     * This example builds an EthosChangeNotificationService to show how to retrieve change notification messages using that service.
     */
    public void getNotificationsWithoutOverridesExample() {
        System.out.println( "******* getNotificationsWithoutOverridesExample() *******" );
        // Build an EthosClientBuilder specifying an API key with timeout values.
        EthosClientBuilder ethosClientBuilder = new EthosClientBuilder(apiKey)
                                                .withConnectionTimeout(30)
                                                .withConnectionRequestTimeout(30)
                                                .withSocketTimeout(30);
        // Use the EthosClientBuilder to build an EthosChangeNotificationService.
        EthosChangeNotificationService ethosChangeNotificationService = new EthosChangeNotificationService.Builder(ethosClientBuilder)
                                                                        .build();
        // The number of notifications to retrieve at a single time, defaults to 20 if not specified.
        int changeNotificationLimit = 3;
        try {
            // Get the change notifications using the changeNotificationLimit.
            List<ChangeNotification> changeNotificationList = ethosChangeNotificationService.getChangeNotifications( changeNotificationLimit );
            // Handle the change notifications retrieved.
            System.out.println( "CHANGE NOTIFICATION LIST LENGTH: " + changeNotificationList.size() );
            System.out.println( changeNotificationList.toString() );
        }
        catch( IOException ioe ) {
            ioe.printStackTrace();
        }
    }

    /**
     * This example shows how to retrieve change notifications and override some version of a resource within the
     * change notifications retrieved.
     */
    public void getNotificationsWithOverridesExample() {
        System.out.println( "******* getNotificationsWithOverridesExample() *******" );
        // Build an EthosClientBuilder specifying an API key with timeout values.
        EthosClientBuilder ethosClientBuilder = new EthosClientBuilder(apiKey)
                                                .withConnectionTimeout(30)
                                                .withConnectionRequestTimeout(30)
                                                .withSocketTimeout(30);
        // Use the EthosClientBuilder to build an EthosChangeNotificationService.
        // This configuration will override any change notifications for sections that do not have a version of 16.0.0, with
        // content from a sections v16.0.0 request for the given sections ID (GUID) for the given change notification.
        EthosChangeNotificationService ethosChangeNotificationService = new EthosChangeNotificationService.Builder(ethosClientBuilder)
                                                                        .withResourceAbbreviatedVersionOverride("sections", "v16.0.0")
                                                                        .build();
        try {
            // Get the change notifications.
            List<ChangeNotification> changeNotificationList = ethosChangeNotificationService.getChangeNotifications();
            // Handle the change notifications.
            System.out.println( "CHANGE NOTIFICATION LIST LENGTH: " + changeNotificationList.size() );
            for( ChangeNotification cn : changeNotificationList ) {
                System.out.println( cn.toString() );
            }
        }
        catch( IOException ioe ) {
            ioe.printStackTrace();
        }
    }

}