/*
 * ******************************************************************************
 *   Copyright 2022 Ellucian Company L.P. and its affiliates.
 * ******************************************************************************
 */
package com.ellucian.bpapiban;


import com.ellucian.ExamplesBase;
import com.ellucian.ethos.integration.client.EthosResponse;
import com.ellucian.ethos.integration.client.proxy.EthosFilterQueryClient;
import com.ellucian.ethos.integration.client.proxy.filter.FilterMap;
import com.ellucian.generated.bpapi.ban.person_comments.v1_0_0.PersonComments100PostRequest;
import com.ellucian.generated.bpapi.ban.person_search.v1_0_0.PersonSearch100GetResponse;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;

public class BPAPIBanExamples extends ExamplesBase {

    // ==========================================================================
    // Attributes
    // ==========================================================================

    // ==========================================================================
    // Methods
    // ==========================================================================
    public void runExamples() {
        System.out.println( "Running BPAPI SDK examples..." );
        applyPersonCommentsUsingJavaBeans();
        applyPersonCommentsUsingJsonNodes();
    }

    /**
     * This is an example of using the Ethos Integration SDK with the Ethos Integration SDK objects library.
     * This library contains strongly typed objects (JavaBeans).  JavaBeans are like data transfer objects (DTOs)
     * that help manage the requests and responses from API calls through the SDK.
     * This example does the following:
     * 1) Finds a person ID using the person-search API with a lastName filter.
     * 2) Builds a PersonComments100PostRequest object with request body data.
     * 3) Makes a POST request to the person-comments API to apply a new person comment for the given person ID.
     * 4) Changes the contactDate field for the person comment.
     * 5) Makes a PUT request to update the person comment for the given person ID.
     */
    public void applyPersonCommentsUsingJavaBeans() {
        // Use an EthosFilterQueryClient since we need to make a filter GET request.  Can also use it to make POST and PUT requests since it extends EthosProxyClient.
        EthosFilterQueryClient ethosFilterQueryClient = getEthosFilterQueryClient();
        try {
            System.out.println("\n\n--- Running apply person comments JavaBeans example... ---");
            System.out.println( "Finding a person record to use..." );
            String resourceName = "person-search";
            String version = "application/json";
            // Build a filterMap used to get person data.  The person-search BPAPI requires a filter with lastName to get data.
            FilterMap filterMap = new FilterMap.Builder()
                                      .withParameterPair("lastName", "Abbe")
                                      .build();
            // Make a GET request using the ethosFilterQueryClient with the filterMap and the specified JavaBean class, which is used in the return type of the EthosResponse.
            List<EthosResponse<List<PersonSearch100GetResponse>>> ethosResponseList = ethosFilterQueryClient.getPagesWithFilterMap( resourceName, version, filterMap, PersonSearch100GetResponse.class );
            // Just pull the first response from the list...
            EthosResponse<List<PersonSearch100GetResponse>> ethosResponse = ethosResponseList.get( 0 );
            // Get the JavaBean object list from the response...
            List<PersonSearch100GetResponse> personSearch100GetResponseList = ethosResponse.getContentAsType();
            // Now get the JavaBean in the list...
            PersonSearch100GetResponse personSearch100GetResponse = personSearch100GetResponseList.get( 0 );
            // Now get the ID value from the JavaBean.
            String personId = personSearch100GetResponse.getId();
            System.out.println( "Found person with ID: " + personId );

            // Now build a person comment JavaBean to make a POST request adding a new person comment for the given person ID.
            Instant now = Instant.now();
            Date today = Date.from( now );
            PersonComments100PostRequest personCommentRequest = new PersonComments100PostRequest()
                                                                .withCmttCode("105")
                                                                .withId(personId)
                                                                .withConfidentialInd("s")
                                                                .withOrigCode("CCON")
                                                                .withText("adding a comment through Java SDK")
                                                                .withTextNar("comment through Java SDK")
                                                                .withContactDate(today)
                                                                .withDate(today);
            resourceName = "person-comments";
            // Make a POST request to add a new person comment using a JavaBean request body object.
            System.out.println( "Making POST request to add a new person comment for person with ID: " + personId + ", using contact date: " + personCommentRequest.getContactDate() );
            ethosResponse = ethosFilterQueryClient.post( resourceName, personCommentRequest );
            System.out.println( "POST made using this URL: " + ethosResponse.getRequestedUrl() );
            System.out.println( "POST EthosResponse body: " + ethosResponse.getContent() );

            // Change the contact date on the personCommentRequest so that we can make a PUT request to update it.  Adds a day to the date.
            personCommentRequest.setContactDate( Date.from(Instant.now().plus(1, ChronoUnit.DAYS)) );
            System.out.println( "Making PUT request with updated contact date: " + personCommentRequest.getContactDate() );
            ethosResponse = ethosFilterQueryClient.put( resourceName, personCommentRequest );
            System.out.println( "PUT made using this URL: " + ethosResponse.getRequestedUrl() );
            System.out.println( "PUT EthosResponse body: " + ethosResponse.getContent() );
        }
        catch( Exception e ) {
            e.printStackTrace();
        }
    }

    /**
     * This is an example of using the Ethos Integration SDK with the Jackson JsonNode library.
     * Jackson is a popular Java library for handling JSON formatted data, which can be used with the Ethos Integration SDK
     * to help manage the requests and responses from API calls through the SDK.
     * This example does the following:
     * 1) Finds a person ID using the person-search API with a lastName filter.
     * 2) Builds a POST request body as a JsonNode object to add a person comment using the person-comment API for given person ID.
     * 3) Makes a POST request to the person-comments API to apply a new person comment for the given person ID.
     * 4) Changes the contactDate field for the person comment.
     * 5) Makes a PUT request using a JsonNode request body to update the person comment for the given person ID.
     */
    public void applyPersonCommentsUsingJsonNodes() {
        // Use an EthosFilterQueryClient since we need to make a filter GET request.  Can also use it to make POST and PUT requests since it extends EthosProxyClient.
        EthosFilterQueryClient ethosFilterQueryClient = getEthosFilterQueryClient();
        try {
            System.out.println("\n\n--- Running apply person comments JsonNodes example... ---");
            System.out.println( "Finding a person record to use..." );
            String resourceName = "person-search";
            String version = "application/json";
            // Build a filterMap used to get person data.  The person-search BPAPI requires a filter with lastName to get data.
            FilterMap filterMap = new FilterMap.Builder()
                    .withParameterPair("lastName", "Abbe")
                    .build();
            List<EthosResponse> ethosResponseList = ethosFilterQueryClient.getPagesWithFilterMap( resourceName, version, filterMap );
            // Just pull the first response from the list...
            EthosResponse ethosResponse = ethosResponseList.get( 0 );
            // Get the JsonNode from the response...
            JsonNode personSearchNode = ethosResponse.getContentAsJson();
            // Now get the ID from the first personSearch in the node list.
            String personId = personSearchNode.elements().next().get("id").asText();
            System.out.println( "Found person with ID: " + personId );

//             Now build a person comment using JsonNode with this structure.
//            {
//                "date": "10/02/2022T20:53:54.872Z",
//                "contactDate": "10/02/2022",
//                "cmttCode": "105",
//                "id": "210009506",
//                "confidentialInd": "s",
//                "text": "testing, testing, 1234",
//                "origCode": "CCON",
//                "textNar": "1234, testing, testing"
//            }

            Instant rightNow = Instant.now().truncatedTo( ChronoUnit.SECONDS );
            // Use a JsonNode for the POST request body.
            ObjectNode personCommentNode = JsonNodeFactory.instance.objectNode();
            personCommentNode.put( "id", personId );
            personCommentNode.put( "date", rightNow.toString() );
            personCommentNode.put( "contactDate", rightNow.toString() );
            personCommentNode.put( "cmttCode", "105" );
            personCommentNode.put( "confidentialInd", "s" );
            personCommentNode.put( "text", "Testing through BPAPI Java SDK example" );
            personCommentNode.put( "origCode", "CCON" );
            personCommentNode.put( "textNar", "Testing through BPAPI Java SDK example" );

            // Make a POST request to add a new person comment using a JavaBean request body object.
            System.out.println( "Making POST request to add a new person comment for person with ID: " + personId + ", using contact date: " + personCommentNode.get("contactDate") );
            resourceName = "person-comments";
            ethosResponse = ethosFilterQueryClient.post( resourceName, personCommentNode );
            System.out.println( "POST made using this URL: " + ethosResponse.getRequestedUrl() );
            System.out.println( "POST EthosResponse body: " + ethosResponse.getContent() );

            // Change the contact date on the personCommentNode so that we can make a PUT request to update it.  Adds a day to the date.
            personCommentNode.put( "contactDate", rightNow.plus(1, ChronoUnit.DAYS).toString() );
            System.out.println( "Making PUT request with updated contact date: " + personCommentNode.get("contactDate") );
            ethosResponse = ethosFilterQueryClient.put( resourceName, personCommentNode );
            System.out.println( "PUT made using this URL: " + ethosResponse.getRequestedUrl() );
            System.out.println( "PUT EthosResponse body: " + ethosResponse.getContent() );

        }
        catch( Exception e ) {
            e.printStackTrace();
        }
    }
}
