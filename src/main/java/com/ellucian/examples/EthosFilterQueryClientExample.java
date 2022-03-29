/*
 * ******************************************************************************
 *   Copyright 2022 Ellucian Company L.P. and its affiliates.
 * ******************************************************************************
 */
package com.ellucian.examples;


import com.ellucian.ethos.integration.client.EthosClientBuilder;
import com.ellucian.ethos.integration.client.EthosResponse;
import com.ellucian.ethos.integration.client.proxy.EthosFilterQueryClient;
import com.ellucian.ethos.integration.client.proxy.filter.CriteriaFilter;
import com.ellucian.ethos.integration.client.proxy.filter.FilterMap;
import com.ellucian.ethos.integration.client.proxy.filter.NamedQueryFilter;
import com.ellucian.ethos.integration.client.proxy.filter.SimpleCriteria;
import com.ellucian.generated.bpapi.ban.person_comments.v1_0_0.PersonComments100PostRequest;
import com.ellucian.generated.bpapi.ban.person_search.v1_0_0.PersonSearch100GetResponse;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.IOException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;

/**
 * This is an example class that shows how to use the EthosFilterQueryClient to make API GET requests using filter criteria,
 * named-queries, and filter maps.
 */
public class EthosFilterQueryClientExample {

    // ==========================================================================
    // Attributes
    // ==========================================================================
    private String apiKey;

    public EthosFilterQueryClientExample(String apiKey ) {
        this.apiKey = apiKey;
    }

    // ==========================================================================
    // Methods
    // ==========================================================================
    public static void main( String[] args ) {
        if( args == null || args.length == 0 ) {
            System.out.println( "Please enter an API key as a program argument when running this example class.  " +
                                "An API key is required to run these examples." );
            return;
        }
        String apiKey = args[ 0 ];
        EthosFilterQueryClientExample ethosFilterQueryClientExample = new EthosFilterQueryClientExample( apiKey );
        ethosFilterQueryClientExample.getUsingCriteriaFilterString();
        ethosFilterQueryClientExample.getUsingCriteriaFilter();
        ethosFilterQueryClientExample.getUsingNamedQueryFilter();
        ethosFilterQueryClientExample.getWithSimpleCriteriaArrayFilter();
        ethosFilterQueryClientExample.getUsingFilterMap();
        ethosFilterQueryClientExample.getPagesUsingCriteriaFilter();
        ethosFilterQueryClientExample.getPagesFromOffsetUsingCriteriaFilter();
        ethosFilterQueryClientExample.getPagesUsingNamedQueryFilter();
        ethosFilterQueryClientExample.getPagesUsingFilterMapValues();
        ethosFilterQueryClientExample.getPagesFromOffsetUsingFilterMapValues();
        ethosFilterQueryClientExample.getAccountCodesWithCriteriaFilter();
    }

    /**
     * This shows how to obtain an EthosFilterQueryClient for the given API key.
     * @return An EthosFilterQueryClient
     */
    public EthosFilterQueryClient getEthosFilterQueryClient() {
        return new EthosClientBuilder(apiKey)
                   .withConnectionTimeout(60)
                   .withConnectionRequestTimeout(60)
                   .withSocketTimeout(60)
                   .buildEthosFilterQueryClient();
    }

    /**
     * This example shows how to use a JSON criteria filter string to make a GET request with a criteria filter.
     */
    public void getUsingCriteriaFilterString() {
        System.out.println( "******* getWithCriteriaFilter() using filter string *******" );
        String resource = "persons";
        String version = "application/vnd.hedtech.integration.v12.1.0+json";
        String criteriaFilterStr = "?criteria={\"names\":[{\"firstName\":\"John\",\"lastName\":\"Smith\"}]}";
        EthosFilterQueryClient ethosFilterQueryClient = getEthosFilterQueryClient();
        try {
            EthosResponse ethosResponse = ethosFilterQueryClient.getWithCriteriaFilter(resource, version, criteriaFilterStr);
            System.out.println( "REQUESTED URL: " + ethosResponse.getRequestedUrl() );
            System.out.println( "Number of resources returned: " + ethosResponse.getContentAsJson().size() );
            // Printing out the response JSON string, but the response body can be handled using the Jackson JsonNode library.
            System.out.println( ethosResponse.getContentAsJson().toPrettyString() );
        }
        catch( IOException ioe ) {
            ioe.printStackTrace();
        }
    }

    /**
     * This example shows how to submit a GET request using values that form a simple criteria array to make the request.
     */
    public void getWithSimpleCriteriaArrayFilter() {
        System.out.println( "******* getWithSimpleCriteriaArrayFilter() using values *******" );
        String resource = "persons";
        String criteriaLabel = "names";
        String criteriaKey = "firstName";
        String criteriaValue = "John";
        EthosFilterQueryClient ethosFilterQueryClient = getEthosFilterQueryClient();
        try {
            // Just pass in the criteria label, key, and value using this method...the SDK does the rest of the work to make the request.
            EthosResponse ethosResponse = ethosFilterQueryClient.getWithSimpleCriteriaArrayValues( resource, criteriaLabel, criteriaKey, criteriaValue );
            System.out.println( "REQUESTED URL: " + ethosResponse.getRequestedUrl() );
            System.out.println( "Number of resources returned: " + ethosResponse.getContentAsJson().size() );
            // Printing out the response JSON string, but the response body can be handled using the Jackson JsonNode library.
            System.out.println( ethosResponse.getContentAsJson().toPrettyString() );
        }
        catch( IOException ioe ) {
            ioe.printStackTrace();
        }
    }

    /**
     * This example shows how to build a CriteriaFilter to make a criteria filter GET request.
     * How the filter GET request is made depends on the resource requested.
     */
    public void getUsingCriteriaFilter() {
        System.out.println( "******* getWithCriteriaFilter() using CriteriaFilter *******" );
        String resource = "persons";
        // Build a SimpleCriteriaArray filter because the persons resource supports that filter syntax.
        CriteriaFilter criteriaFilter = new SimpleCriteria.Builder()
                                        .withSimpleCriteriaArray("names", "firstName", "John")
                                        .buildCriteriaFilter();
        EthosFilterQueryClient ethosFilterQueryClient = getEthosFilterQueryClient();
        try {
            EthosResponse ethosResponse = ethosFilterQueryClient.getWithCriteriaFilter( resource, criteriaFilter );
            System.out.println( "REQUESTED URL: " + ethosResponse.getRequestedUrl() );
            System.out.println( "Number of resources returned: " + ethosResponse.getContentAsJson().size() );
            // Printing out the response JSON string, but the response body can be handled using the Jackson JsonNode library.
            System.out.println( ethosResponse.getContentAsJson().toPrettyString() );
        }
        catch( IOException ioe ) {
            ioe.printStackTrace();
        }
    }

    /**
     * This example shows how to build a NamedQueryFilter to make a GET request for a resource that supports named queries.
     */
    public void getUsingNamedQueryFilter() {
        System.out.println( "******* getWithNamedQueryFilter() using NamedQueryFilter *******" );
        String resource = "sections";
        String queryName = "keywordSearch";
        String queryKey = "keywordSearch";
        String queryValue = "Culture";
        NamedQueryFilter namedQueryFilter = new SimpleCriteria.Builder()
                                            .withNamedQuery( queryName, queryKey, queryValue)
                                            .buildNamedQueryFilter();
        EthosFilterQueryClient ethosFilterQueryClient = getEthosFilterQueryClient();
        try {
            EthosResponse ethosResponse = ethosFilterQueryClient.getWithNamedQueryFilter( resource, namedQueryFilter );
            System.out.println( "REQUESTED URL: " + ethosResponse.getRequestedUrl() );
            System.out.println( "Number of resources returned: " + ethosResponse.getContentAsJson().size() );
            // Printing out the response JSON string, but the response body can be handled using the Jackson JsonNode library.
            System.out.println( ethosResponse.getContentAsJson().toPrettyString() );
        }
        catch( IOException ioe ) {
            ioe.printStackTrace();
        }
    }

    /**
     * This example shows how to use a filter map containing key/value pairs to make a GET request.
     */
    public void getUsingFilterMap() {
        System.out.println( "******* getWithFilterMap() using filterMap *******" );
        String resource = "persons";
        String version = "application/vnd.hedtech.integration.v6+json";
        String filterKey = "firstName";
        String filterValue = "John";
        EthosFilterQueryClient ethosFilterQueryClient = getEthosFilterQueryClient();
        try {
            FilterMap filterMap = new FilterMap.Builder()
                                      .withParameterPair(filterKey, filterValue)
                                      .withParameterPair("lastName", "Smith")
                                      .build();
            EthosResponse ethosResponse = ethosFilterQueryClient.getWithFilterMap( resource, version, filterMap );
            System.out.println( "REQUESTED URL: " + ethosResponse.getRequestedUrl() );
            System.out.println( "Number of resources returned: " + ethosResponse.getContentAsJson().size() );
            // Printing out the response JSON string, but the response body can be handled using the Jackson JsonNode library.
            System.out.println( ethosResponse.getContentAsJson().toPrettyString() );
        }
        catch( IOException ioe ) {
            ioe.printStackTrace();
        }
    }

    /**
     * This example shows how criteria filter requests also support paging when multiple pages are returned.
     * This uses a criteria filter on a GET request returning a list of EthosResponses where each EthosResponse
     * represents one page of data.
     * This example shows how to retrieve ALL the pages from a criteria filter GET request.
     * NOTE:  IT MAY NOT BE ADVISABLE TO RUN THIS EXAMPLE BECAUSE THE QUANTITY OF DATA RETURNED COULD BE A LOT,
     * DEPENDING ON THE ENVIRONMENT THIS IS RUN AGAINST SINCE IT RETURNS ALL PAGES ACCORDING TO THE FILTER APPLIED.
     */
    public void getPagesUsingCriteriaFilter() {
        System.out.println( "******* getPagesWithCriteriaFilter() using criteria filter *******" );
        String resource = "persons";
        String criteriaLabel = "names";
        String criteriaKey = "firstName";
        String criteriaValue = "John";
        int pageSize = 50;
        EthosFilterQueryClient ethosFilterQueryClient = getEthosFilterQueryClient();
        try {
            CriteriaFilter criteriaFilter = new SimpleCriteria.Builder()
                                            .withSimpleCriteriaArray(criteriaLabel, criteriaKey, criteriaValue)
                                            .buildCriteriaFilter();
            List<EthosResponse> ethosResponseList = ethosFilterQueryClient.getPagesWithCriteriaFilter( resource, criteriaFilter, pageSize );
            System.out.println( "Number of pages returned: " + ethosResponseList.size() );
            for( EthosResponse ethosResponse : ethosResponseList ) {
                System.out.println( "REQUESTED URL: " + ethosResponse.getRequestedUrl() );
                // Printing out the response JSON string, but the response body can be handled using the Jackson JsonNode library.
                System.out.println( "PAGE SIZE: " + ethosResponse.getContentAsJson().size() );
            }
        }
        catch( IOException ioe ) {
            ioe.printStackTrace();
        }
    }

    /**
     * This example shows how criteria filter requests also support paging when multiple pages are returned.
     * This uses a criteria filter on a GET request returning a list of EthosResponses where each EthosResponse
     * represents one page of data.  In this example, a calculation is done to retrieve only the last 5% of rows due to
     * the potential for paging across many rows of data.
     */
    public void getPagesFromOffsetUsingCriteriaFilter() {
        System.out.println( "******* getPagesFromOffsetWithCriteriaFilter() using criteria filter *******" );
        String resourceName = "persons";
        String criteriaLabel = "names";
        String criteriaKey = "firstName";
        String criteriaValue = "John";
        int pageSize = 50;
        EthosFilterQueryClient ethosFilterQueryClient = getEthosFilterQueryClient();
        try {
            CriteriaFilter criteriaFilter = new SimpleCriteria.Builder()
                                            .withSimpleCriteriaArray(criteriaLabel, criteriaKey, criteriaValue)
                                            .buildCriteriaFilter();
            int totalCount = ethosFilterQueryClient.getTotalCount( resourceName, criteriaFilter );
            // Calculate the offset to be 95% of the totalCount to avoid paging through potentially tons of pages.
            int offset = (int)(totalCount * 0.95);
            // Make the request using the criteriaFilter and other params.
            List<EthosResponse> ethosResponseList = ethosFilterQueryClient.getPagesFromOffsetWithCriteriaFilter( resourceName, criteriaFilter, pageSize, offset );
            System.out.println(String.format("Get data for resource: %s", resourceName));
            System.out.println(String.format("Calculated offset of %s which is 95 percent of a total count of %s to avoid paging through potentially lots of pages.", offset, totalCount));
            System.out.println("To run with more paging, manually set the offset to a lower value, or reduce the percentage of the total count.");
            System.out.println( "Number of pages returned: " + ethosResponseList.size() );
            System.out.println( "OFFSET: " + offset );
            for( EthosResponse ethosResponse : ethosResponseList ) {
                System.out.println( "REQUESTED URL: " + ethosResponse.getRequestedUrl() );
                // Printing out the response JSON string, but the response body can be handled using the Jackson JsonNode library.
                System.out.println( "PAGE SIZE: " + ethosResponse.getContentAsJson().size() );
            }
        }
        catch( IOException ioe ) {
            ioe.printStackTrace();
        }
    }

    /**
     * This example shows how named-query requests also support paging when multiple pages are returned.
     * This uses a named-query on a GET request returning a list of EthosResponses where each EthosResponse
     * represents one page of data.  In this example, a calculation is done to retrieve only the last 5% of rows due to
     * the potential for paging across many rows of data.
     */
    public void getPagesUsingNamedQueryFilter() {
        System.out.println( "******* getPagesUsingNamedQueryFilter() using named query filter *******" );
        String resourceName = "sections";
        String queryName = "keywordSearch";
        String queryKey = "keywordSearch";
        String queryValue = "Culture";
        int pageSize = 50;
        EthosFilterQueryClient ethosFilterQueryClient = getEthosFilterQueryClient();
        try {
            NamedQueryFilter namedQueryFilter = new SimpleCriteria.Builder()
                                                .withNamedQuery( queryName, queryKey, queryValue)
                                                .buildNamedQueryFilter();
            int totalCount = ethosFilterQueryClient.getTotalCount( resourceName, namedQueryFilter );
            // Calculate the offset to be 95% of the totalCount to avoid paging through potentially tons of pages.
            int offset = (int)(totalCount * 0.95);
            // Make the request using the namedQueryFilter and other params.
            List<EthosResponse> ethosResponseList = ethosFilterQueryClient.getPagesFromOffsetWithNamedQueryFilter( resourceName, namedQueryFilter, pageSize, offset );
            System.out.println(String.format("Get data for resource: %s", resourceName));
            System.out.println(String.format("Calculated offset of %s which is 95 percent of a total count of %s to avoid paging through potentially lots of pages.", offset, totalCount));
            System.out.println("To run with more paging, manually set the offset to a lower value, or reduce the percentage of the total count.");
            System.out.println( "Number of pages returned: " + ethosResponseList.size() );
            System.out.println( "OFFSET: " + offset );
            for( EthosResponse ethosResponse : ethosResponseList ) {
                System.out.println( "REQUESTED URL: " + ethosResponse.getRequestedUrl() );
                // Printing out the response JSON string, but the response body can be handled using the Jackson JsonNode library.
                System.out.println( "PAGE SIZE: " + ethosResponse.getContentAsJson().size() );
            }
        }
        catch( IOException ioe ) {
            ioe.printStackTrace();
        }
    }

    /**
     * This example shows how to retrieve ALL the pages from a filter map GET request.
     * NOTE:  IT MAY NOT BE ADVISABLE TO RUN THIS EXAMPLE BECAUSE THE QUANTITY OF DATA RETURNED COULD BE A LOT,
     * DEPENDING ON THE ENVIRONMENT THIS IS RUN AGAINST SINCE IT RETURNS ALL PAGES ACCORDING TO THE FILTER APPLIED.
     */
    public void getPagesUsingFilterMapValues() {
        System.out.println( "******* getPagesWithFilterMap() *******" );
        String resource = "persons";
        String version = "application/vnd.hedtech.integration.v6+json";
        String filterMapKey = "firstName";
        String filterMapValue = "John";
        int pageSize = 50;
        EthosFilterQueryClient ethosFilterQueryClient = getEthosFilterQueryClient();
        try {
            FilterMap filterMap = new FilterMap.Builder()
                                      .withParameterPair(filterMapKey, filterMapValue)
                                      .build();
            List<EthosResponse> ethosResponseList = ethosFilterQueryClient.getPagesWithFilterMap( resource, version, filterMap, pageSize );
            System.out.println( "Number of pages returned: " + ethosResponseList.size() );
            for( EthosResponse ethosResponse : ethosResponseList ) {
                System.out.println( "REQUESTED URL: " + ethosResponse.getRequestedUrl() );
                System.out.println( "PAGE SIZE: " + ethosResponse.getContentAsJson().size() );
            }
        }
        catch( IOException ioe ) {
            ioe.printStackTrace();
        }
    }

    /**
     * This example shows how filter map requests also support paging when multiple pages are returned.
     * This uses a filter map on a GET request returning a list of EthosResponses where each EthosResponse
     * represents one page of data.  In this example, a calculation is done to retrieve only the last 5% of rows due to
     * the potential for paging across many rows of data.
     */
    public void getPagesFromOffsetUsingFilterMapValues() {
        System.out.println( "******* getPagesFromOffsetWithFilterMap() *******" );
        String resourceName = "persons";
        String version = "application/vnd.hedtech.integration.v6+json";
        String filterMapKey = "firstName";
        String filterMapValue = "John";
        int pageSize = 50;
        EthosFilterQueryClient ethosFilterQueryClient = getEthosFilterQueryClient();
        try {
            FilterMap filterMap = new FilterMap.Builder()
                    .withParameterPair(filterMapKey, filterMapValue)
                    .build();
            int totalCount = ethosFilterQueryClient.getTotalCount( resourceName, version, filterMap );
            // Calculate the offset to be 95% of the totalCount to avoid paging through potentially tons of pages.
            int offset = (int)(totalCount * 0.95);
            // Make the request using the filterMap and other params.
            List<EthosResponse> ethosResponseList = ethosFilterQueryClient.getPagesFromOffsetWithFilterMap( resourceName, version, filterMap, pageSize, offset );
            System.out.println(String.format("Get data for resource: %s", resourceName));
            System.out.println(String.format("Calculated offset of %s which is 95 percent of a total count of %s to avoid paging through potentially lots of pages.", offset, totalCount));
            System.out.println("To run with more paging, manually set the offset to a lower value, or reduce the percentage of the total count.");
            System.out.println( "Number of pages returned: " + ethosResponseList.size() );
            System.out.println( "OFFSET: " + offset );
            for( EthosResponse ethosResponse : ethosResponseList ) {
                System.out.println( "REQUESTED URL: " + ethosResponse.getRequestedUrl() );
                // Printing out the response JSON string, but the response body can be handled using the Jackson JsonNode library.
                System.out.println( "PAGE SIZE: " + ethosResponse.getContentAsJson().size() );
            }
        }
        catch( IOException ioe ) {
            ioe.printStackTrace();
        }
    }

    /**
     * This is an example of using a criteria filter request with multiple criteria in
     * a MultiCriteriaObject.  This criteria filter structure is only used with (Banner) business API requests,
     * and not Ethos (EEDM) API requests.
     */
    public void getAccountCodesWithCriteriaFilter() {
        System.out.println( "******* getAccountCodesWithCriteriaFilter() *******" );
        String resourceName = "account-codes";
        String version = "application/json";
        EthosFilterQueryClient ethosFilterQueryClient = getEthosFilterQueryClient();
        try {
            CriteriaFilter cf = new SimpleCriteria.Builder()
                                .withMultiCriteriaObject(null,"acctCode", "04", false)
                                .addSimpleCriteria("statusInd", "A")
                                .buildCriteriaFilter();
            EthosResponse ethosResponse = ethosFilterQueryClient.getWithCriteriaFilter( resourceName, version, cf );
            System.out.println( "REQUEST URL: " + ethosResponse.getRequestedUrl() );
            // Printing out the response JSON string, but the response body can be handled using the Jackson JsonNode library.
            System.out.println( "RESPONSE: " + ethosResponse.getContent());
        }
        catch( IOException ioe ) {
            ioe.printStackTrace();
        }
    }

    /**
     * This is an example of using the Ethos Integration SDK with the Jackson JsonNode library.
     * Jackson is a popular Java library for handling JSON formatted data, which can be used with the Ethos Integration SDK
     * to help manage the requests and responses from API calls through the SDK.
     * This example does the following using the EthosFilterQueryClient, since it extends the EthosProxyClient:
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
            System.out.println( "POST EthosResponse body: " + ethosResponse.getContent() );

            // Change the contact date on the personCommentNode so that we can make a PUT request to update it.  Adds a day to the date.
            personCommentNode.put( "contactDate", rightNow.plus(1, ChronoUnit.DAYS).toString() );
            System.out.println( "Making PUT request with updated contact date: " + personCommentNode.get("contactDate") );
            ethosResponse = ethosFilterQueryClient.put( resourceName, personCommentNode );
            System.out.println( "PUT EthosResponse body: " + ethosResponse.getContent() );

        }
        catch( Exception e ) {
            e.printStackTrace();
        }
    }

    /**
     * This is an example of using the Ethos Integration SDK with the Ethos Integration SDK objects library.
     * This library contains strongly typed objects (JavaBeans).  JavaBeans are like data transfer objects (DTOs)
     * that help manage the requests and responses from API calls through the SDK.
     * This example does the following using the EthosFilterQueryClient, since it extends the EthosProxyClient:
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


}