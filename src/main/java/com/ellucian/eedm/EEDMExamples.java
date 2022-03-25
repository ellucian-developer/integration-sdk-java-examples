/*
 * ******************************************************************************
 *   Copyright 2022 Ellucian Company L.P. and its affiliates.
 * ******************************************************************************
 */
package com.ellucian.eedm;


import com.ellucian.ExamplesBase;
import com.ellucian.ethos.integration.client.EthosResponse;
import com.ellucian.ethos.integration.client.proxy.EthosProxyClient;
import com.ellucian.generated.eedm.person_holds.v6_0.Person;
import com.ellucian.generated.eedm.person_holds.v6_0.PersonHolds;
import com.ellucian.generated.eedm.person_holds.v6_0.Type;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.http.client.HttpResponseException;

import java.io.IOException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;

public class EEDMExamples extends ExamplesBase {

    // ==========================================================================
    // Attributes
    // ==========================================================================

    // ==========================================================================
    // Methods
    // ==========================================================================
    public void runExamples() {
        System.out.println( "Running EEDM SDK examples..." );
        applyPersonHoldUsingJavaBeans();
        applyPersonHoldUsingJsonNodes();
    }

    /**
     * This is an example of using the Ethos Integration SDK with the Ethos Integration SDK objects library.
     * This library contains strongly typed objects (JavaBeans).  JavaBeans are like data transfer objects (DTOs)
     * that help manage the requests and responses from API calls through the SDK.
     * This example does the following:
     * 1) Finds a person record.
     * 2) Builds a PersonHolds object with request body data.
     * 3) Makes a POST request to apply a new person hold for the given person.
     * 4) Changes the startOn date for the person hold.
     * 5) Makes a PUT request to update the person hold for the given person.
     * 6) Deletes the person hold.
     * 7) Tries to read the same person hold that was deleted to ensure it cannot be read.
     */
    public void applyPersonHoldUsingJavaBeans() {
        EthosProxyClient ethosProxyClient = getEthosProxyClient();
        try {
            System.out.println("\n\n--- Running apply person holds example... ---");
            System.out.println( "Finding a person record to use..." );
            // Get a single person record.
            List<EthosResponse> responses = ethosProxyClient.getRows("persons", 1);
            JsonNode personNode = responses.get(0).getContentAsJson();
            String personId = personNode.elements().next().get("id").asText();
            System.out.println(String.format("Found a person ID: %s", personId));

            // Build a person-holds resource using JavaBean objects.
            System.out.println("Building strongly typed object...");
            Instant rightNow = Instant.now().truncatedTo(ChronoUnit.SECONDS);
            System.out.println(String.format("RIGHT NOW: %S", rightNow));
            System.out.println(String.format("DATE NOW: %s", Date.from(rightNow)));
            PersonHolds personHolds = new PersonHolds();
            personHolds.setId("00000000-0000-0000-0000-000000000000");
            personHolds.setStartOn(Date.from(rightNow));
            Person person = new Person();
            person.setId(personId);
            personHolds.setPerson(person);
            Type personHoldsType = new Type();
            personHoldsType.setCategory(Type.Category.FINANCIAL);
            personHolds.setType(personHoldsType);

            // Send a POST request to create a new person-holds record.
            EthosResponse response = ethosProxyClient.post("person-holds", personHolds);
            System.out.println("Created a 'person-holds' record:");
            System.out.println(response.getContent());

            // Get the 'id' of the newly added record.
            String newId = response.getContentAsJson().get("id").asText();

            // Change the date on the person-holds record and send a PUT request to update the record.
            personHolds.setId(null);
            personHolds.setStartOn(Date.from(rightNow.plus(1, ChronoUnit.DAYS)));
            response = ethosProxyClient.put("person-holds", newId, personHolds);
            System.out.println(String.format("Successfully updated person-holds record %s.", newId));

            // Delete the record.
            ethosProxyClient.delete("person-holds", newId);
            System.out.println(String.format("Successfully deleted person-holds record %s.", newId));

            // Attempt to get the record that was created and make sure that fails, since it was deleted.
            try {
                ethosProxyClient.getById("person-holds", newId);
            } catch (HttpResponseException ex) {
                System.out.println(String.format("Failed to get person-holds record %s.  The delete was successful.", newId));
            }
            System.out.println("Done.");
            System.out.println("===============================================");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * This is an example of using the Ethos Integration SDK with the Jackson JsonNode library.
     * Jackson is a popular Java library for handling JSON formatted data, which can be used with the Ethos Integration SDK
     * to help manage the requests and responses from API calls through the SDK.
     * This example does the following:
     * 1) Finds a person record.
     * 2) Builds a personHold Jackson ObjectNode with request body data.
     * 3) Makes a POST request to apply a new person hold for the given person.
     * 4) Changes the startOn date for the person hold.
     * 5) Makes a PUT request to update the person hold for the given person.
     * 6) Deletes the person hold.
     * 7) Tries to read the same person hold that was deleted to ensure it cannot be read.
     */
    public void applyPersonHoldUsingJsonNodes() {
        EthosProxyClient ethosProxyClient = getEthosProxyClient();
        try {
            System.out.println("******* applyPersonHoldUsingJsonNodes() *******");
            System.out.println( "Finding a person record to use..." );
            // Get a single person record.
            List<EthosResponse> responses = ethosProxyClient.getRows("persons", 1);
            // Get the response body content as a JsonNode.
            JsonNode personNode = responses.get(0).getContentAsJson();
            String personId = personNode.elements().next().get("id").asText();

            // Build a person-holds resource using JsonNodes.
            Instant rightNow = Instant.now().truncatedTo(ChronoUnit.SECONDS);
            ObjectNode personHoldNode = JsonNodeFactory.instance.objectNode();
            personHoldNode.put("id", "00000000-0000-0000-0000-000000000000");
            personHoldNode.put("startOn", rightNow.toString());
            // Add a person object with 'id' to the personHold node.
            personHoldNode.putObject("person").put("id", personId);
            // Add a type object with 'category' to the personHold node.
            personHoldNode.putObject("type").put("category", "financial");

            // Send a POST request to create a new person-holds record, with the personHold JsonNode as the request body.
            EthosResponse response = ethosProxyClient.post("person-holds", personHoldNode);
            System.out.println("Created a 'person-holds' record:");
            System.out.println(response.getContent());

            // Get the 'id' of the new record
            String newId = response.getContentAsJson().get("id").asText();

            // Change the date on the person-holds record and send a PUT request to update the record.
            personHoldNode.remove("id");
            personHoldNode.put("startOn", rightNow.plus(1, ChronoUnit.DAYS).toString());
            response = ethosProxyClient.put("person-holds", newId, personHoldNode);
            System.out.println(String.format("Successfully updated person-holds record %s.", newId));

            // Delete the record
            ethosProxyClient.delete("person-holds", newId);
            System.out.println(String.format("Successfully deleted person-holds record %s.", newId));

            // Attempt to get the record that was created and make sure that fails.
            try{
                ethosProxyClient.getById("person-holds", newId);
            }
            catch (HttpResponseException ex) {
                System.out.println(String.format("Failed to get person-holds record %s.  The delete was successful.", newId));
            }
            System.out.println( "Done." );
            System.out.println("===============================================");
        }
        catch( IOException ioe ) {
            ioe.printStackTrace();
        }
    }
}
