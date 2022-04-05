/*
 * ******************************************************************************
 *   Copyright 2022 Ellucian Company L.P. and its affiliates.
 * ******************************************************************************
 */
package com.ellucian.examples;

import com.ellucian.ethos.integration.client.EthosClientBuilder;
import com.ellucian.ethos.integration.client.messages.ChangeNotification;
import com.ellucian.ethos.integration.client.messages.EthosMessagesClient;

import java.io.IOException;
import java.util.List;

/**
 * This is an example class that shows how to retrieve change notification messages from the message queue service
 * in Ethos Integration.
 */
public class EthosMessagesClientExample {

    private String apiKey;

    public EthosMessagesClientExample( String apiKey ) {
        this.apiKey = apiKey;
    }

    /**
     * Run this sample program.  This has a required program argument of an API key.
     * @param args The string arguments passed into this main method class.
     * @throws Exception Propagates any exception thrown.
     */
    public static void main (String[] args) throws Exception {
        if( args == null || args.length == 0 ) {
            System.out.println( "Please enter an API key as a program argument when running this example class.  " +
                                "An API key is required to run these examples." );
            return;
        }
        String apiKey = args[ 0 ];
        EthosMessagesClientExample ethosMessagesClientExample = new EthosMessagesClientExample( apiKey );
        ethosMessagesClientExample.checkAvailableMessages();
        ethosMessagesClientExample.consumeMessages();
    }

    /**
     *  This example shows how to build an EthosMessagesClient with the given API key, and retrieves the number of
     *  available messages for the given API key.
     * @throws IOException
     */
    public void checkAvailableMessages() throws IOException {
        EthosMessagesClient client = new EthosClientBuilder(apiKey).buildEthosMessagesClient();
        int numMessages = client.getNumAvailableMessages();
        System.out.printf("Number of available messages: %s\n", numMessages);
    }

    /**
     * This example shows how to retrieve change notification messages using the 'consume()' method, and also
     * using the 'consumeFromId()' method.  The 'consumeFromId()' method uses the lastProcessedID param which is the
     * ID of the last message that was successfully processed, thereby retrieving messages from that point.
     * @throws IOException
     */
    public void consumeMessages() throws IOException {
        EthosMessagesClient client = new EthosClientBuilder(apiKey).buildEthosMessagesClient();
        List<ChangeNotification> cnList = client.consume();
        System.out.printf("Retrieved '%d' messages.\n", cnList.size());
        System.out.println("Requesting the same set of messages again, using 'lastProcessedID=0'.");
        cnList = client.consumeFromId(0);
        System.out.printf("Retrieved '%d' messages.\n", cnList.size());
        // Print out properties from the change notifications retrieved.
        // The content could be accessed using JsonNode, but just printing toString() for this example.
        for( ChangeNotification cn : cnList ) {
            System.out.println( "Resource: " + cn.getResource().getName() +
                                ", version: " + cn.getResource().getVersion() +
                                ", content: " + cn.getContent().toString() );
        }
    }
}
