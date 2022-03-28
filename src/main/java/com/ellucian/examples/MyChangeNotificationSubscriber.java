/*
 * ******************************************************************************
 *   Copyright 2022 Ellucian Company L.P. and its affiliates.
 * ******************************************************************************
 */
package com.ellucian.examples;

import com.ellucian.ethos.integration.client.messages.ChangeNotification;
import com.ellucian.ethos.integration.notification.AbstractEthosChangeNotificationSubscriber;

/**
 * This is an example of a custom application class that extends AbstractEthosChangeNotificationSubscriber in the SDK to receive
 * change notifications.
 */
public class MyChangeNotificationSubscriber extends AbstractEthosChangeNotificationSubscriber {

    // ==========================================================================
    // Attributes
    // ==========================================================================

    public MyChangeNotificationSubscriber() {
        super();
    }
    public MyChangeNotificationSubscriber( int numNotifications ) {
        super(numNotifications);
    }

    // ==========================================================================
    // Methods
    // ==========================================================================

    /**
     * Override this method in your custom change notification subscriber to receive and handle individual change notifications.
     * @param changeNotification An individual change notification.
     */
    @Override
    public void onChangeNotification(ChangeNotification changeNotification) {
        System.out.println( "onChangeNotification on " + Thread.currentThread().getName() + ", CN: " + changeNotification.toString() );
    }

    /**
     * Override this method in your custom change notification subscriber to handle errors that might occur when receiving
     * change notifications.
     * @param throwable The exception thrown when something bad happened while receiving change notifications.
     */
    @Override
    public void onChangeNotificationError(Throwable throwable) {
        System.out.println( "onChangeNotificationError: " + Thread.currentThread().getName() + ", THROWABLE: " + throwable.getMessage() );
        throwable.printStackTrace();
    }
}