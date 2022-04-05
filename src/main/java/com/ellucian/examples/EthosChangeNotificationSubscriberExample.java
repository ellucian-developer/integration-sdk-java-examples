/*
 * ******************************************************************************
 *   Copyright 2022 Ellucian Company L.P. and its affiliates.
 * ******************************************************************************
 */
package com.ellucian.examples;

import com.ellucian.ethos.integration.notification.EthosChangeNotificationListPollService;
import com.ellucian.ethos.integration.notification.EthosChangeNotificationPollService;
import com.ellucian.ethos.integration.service.EthosChangeNotificationService;

/**
 * This is an example class that shows how to retrieve change notifications in a more automated way.
 * A custom subscriber class can be added to the EthosChangeNotificationPollService so that messages can be retrieved
 * until the subscription is cancelled.
 */
public class EthosChangeNotificationSubscriberExample {

    // ==========================================================================
    // Attributes
    // ==========================================================================
    private String apiKey;

    public EthosChangeNotificationSubscriberExample( String apiKey ) {
        this.apiKey = apiKey;
    }

    // ==========================================================================
    // Methods
    // ==========================================================================
    public static void main( String[] args ) {
        if( args == null || args.length == 0 ) {
            System.out.println("Please enter an API key as a program argument when running this example class.  " +
                               "An API key is required to run these examples.");
            return;
        }
        String apiKey = args[ 0 ];
        EthosChangeNotificationSubscriberExample subscriberExample = new EthosChangeNotificationSubscriberExample( apiKey );
        subscriberExample.subscribeToChangeNotifications();
        subscriberExample.subscribeToChangeNotificationLists();
    }

    /**
     * This example builds an EthosChangeNotificationService to be used with an EthosChangeNotificationPollService.
     * The poll service is then used to subscribe your custom application notification subscriber.  Change notifications
     * are then received in your custom application notification subscriber until the subscription is cancelled by calling
     * "cancelSubscription()" on the subscriber.
     */
    public void subscribeToChangeNotifications() {
        int numNotifications = 3;
        long pollingIntervalSeconds = 5;
        // Build an EthosChangeNotificationService using an API key.
        EthosChangeNotificationService cnService = new EthosChangeNotificationService.Builder(apiKey)
                                                   .build();
        // Build an EthosChangeNotificationPollService using the cnService.
        EthosChangeNotificationPollService ethosChangeNotificationPollService = new EthosChangeNotificationPollService( cnService, pollingIntervalSeconds );
        // Instantiate your custom application subscriber, taking in the number of notifications to retrieve at once.
        MyChangeNotificationSubscriber myChangeNotificationSubscriber = new MyChangeNotificationSubscriber( numNotifications );
        // Subscribe to the poll service.  This starts the notification process for the subscriber receiving notifications.
        ethosChangeNotificationPollService.subscribe( myChangeNotificationSubscriber );
        try {
            // Sleeping to simulate time taken to do other stuff...
            System.out.println( "Waiting 5 seconds to simulate other processing while change notifications are retrieved.");
            // Notifications should come through the MyChangeNotificationSubscriber.onChangeNotification() method.
            Thread.sleep(5000);
        }
        catch( InterruptedException ie ) {
            ie.printStackTrace();
        }
        // When ready, cancel the subscription to stop receiving notifications.
        if( myChangeNotificationSubscriber.isSubscriptionRunning() ) {
            myChangeNotificationSubscriber.cancelSubscription();
        }
    }

    /**
     * This example builds an EthosChangeNotificationService to be used with an EthosChangeNotificationListPollService.
     * The poll service is then used to subscribe your custom application notification list subscriber.  Change notification lists
     * are then received in your custom application notification list subscriber until the subscription is cancelled by calling
     * "cancelSubscription()" on the subscriber.
     */
    public void subscribeToChangeNotificationLists() {
        int numNotifications = 3;
        long pollingIntervalSeconds = 5;
        // Build an EthosChangeNotificationService using an API key.
        EthosChangeNotificationService cnService = new EthosChangeNotificationService.Builder(apiKey)
                                                   .build();
        // Build an EthosChangeNotificationPollService using the cnService.
        EthosChangeNotificationListPollService ethosChangeNotificationListPollService = new EthosChangeNotificationListPollService( cnService, pollingIntervalSeconds );
        // Instantiate your custom application list subscriber, taking in the number of notifications to retrieve at once.
        MyChangeNotificationListSubscriber myChangeNotificationListSubscriber = new MyChangeNotificationListSubscriber( numNotifications );
        // Subscribe to the poll service.  This starts the notification process for the subscriber receiving list notifications.
        ethosChangeNotificationListPollService.subscribe( myChangeNotificationListSubscriber );
        try {
            // Sleeping to simulate time taken to do other stuff...
            System.out.println( "Waiting 5 seconds to simulate other processing while change notifications are retrieved.");
            // Notifications should come through the MyChangeNotificationListSubscriber.onChangeNotificationList() method.
            Thread.sleep(5000);
        }
        catch( InterruptedException ie ) {
            ie.printStackTrace();
        }
        // When ready, cancel the list subscription to stop receiving list notifications.
        if( myChangeNotificationListSubscriber.isSubscriptionRunning() ) {
            myChangeNotificationListSubscriber.cancelSubscription();
        }
    }

}