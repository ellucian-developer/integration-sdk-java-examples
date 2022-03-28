/*
 * ******************************************************************************
 *   Copyright 2022 Ellucian Company L.P. and its affiliates.
 * ******************************************************************************
 */
package com.ellucian.examples;

import com.ellucian.ethos.integration.client.messages.ChangeNotification;
import com.ellucian.ethos.integration.notification.AbstractEthosChangeNotificationListSubscriber;

import java.util.List;

/**
 * This is an example of a custom application class that extends AbstractEthosChangeNotificationListSubscriber in the SDK to
 * receive change notification lists.
 */
public class MyChangeNotificationListSubscriber extends AbstractEthosChangeNotificationListSubscriber  {

    // ==========================================================================
    // Attributes
    // ==========================================================================
    public MyChangeNotificationListSubscriber() {
        super();
    }

    public MyChangeNotificationListSubscriber( int numNotifications ) {
        super( numNotifications );
    }
    // ==========================================================================
    // Methods
    // ==========================================================================

    /**
     * Override this method in your custom change notification list subscriber to receive and handle whole change notification lists.
     * @param changeNotificationList The change notification list to receive containing change notifications.
     */
    @Override
    public void onChangeNotificationList(List<ChangeNotification> changeNotificationList) {
        System.out.println( "onChangeNotificationList ON " + Thread.currentThread().getName() + ", LIST SIZE: " + changeNotificationList.size() );
        System.out.println( "onChangeNotificationList ON " +Thread.currentThread().getName() + ", " + changeNotificationList.toString() );
    }

    /**
     * Override this method in your custom change notification list subscriber to handle errors that might occur when receiving
     * change notification lists.
     * @param throwable The exception thrown when something bad happened while receiving change notification lists.
     */
    @Override
    public void onChangeNotificationListError(Throwable throwable) {
        System.out.println( "HANDLING CHANGE NOTIFICATION LIST ERROR ON " + Thread.currentThread().getName()+ ", " + throwable.getMessage() );
        throwable.printStackTrace();
    }
}