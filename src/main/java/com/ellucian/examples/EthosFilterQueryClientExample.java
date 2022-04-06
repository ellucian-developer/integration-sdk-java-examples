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
import com.ellucian.generated.bpapi.ban.account_codes.v1_0_0.AccountCodes100GetResponse;
import com.ellucian.generated.bpapi.ban.person_comments.v1_0_0.PersonComments100PostRequest;
import com.ellucian.generated.bpapi.ban.person_comments.v1_0_0.PersonComments100PutRequest;
import com.ellucian.generated.bpapi.ban.person_search.v1_0_0.PersonSearch100GetResponse;
import com.ellucian.generated.eedm.persons.v12_4_0.Persons;
import com.ellucian.generated.eedm.sections.v16_1_0.Sections;
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
        ethosFilterQueryClientExample.criteriaFilterExamples();
        ethosFilterQueryClientExample.getUsingCriteriaFilterString();
        ethosFilterQueryClientExample.getUsingCriteriaFilter();
        ethosFilterQueryClientExample.getUsingCriteriaFilterWithJavaBeans();
        ethosFilterQueryClientExample.getUsingNamedQueryFilter();
        ethosFilterQueryClientExample.getUsingNamedQueryFilterWithJavaBeans();
        ethosFilterQueryClientExample.getWithSimpleCriteriaArrayValues();
        ethosFilterQueryClientExample.getWithSimpleCriteriaArrayValuesAsJavaBeans();
        ethosFilterQueryClientExample.getUsingFilterMap();
        ethosFilterQueryClientExample.getUsingFilterMapAsJavaBeans();
        ethosFilterQueryClientExample.getPagesUsingCriteriaFilter();
        ethosFilterQueryClientExample.getPagesUsingCriteriaFilterAsJavaBeans();
        ethosFilterQueryClientExample.getPagesFromOffsetUsingCriteriaFilter();
        ethosFilterQueryClientExample.getPagesFromOffsetUsingCriteriaFilterAsJavaBeans();
        ethosFilterQueryClientExample.getPagesUsingNamedQueryFilter();
        ethosFilterQueryClientExample.getPagesUsingNamedQueryFilterAsJavaBeans();
        ethosFilterQueryClientExample.getPagesUsingFilterMapValues();
        ethosFilterQueryClientExample.getPagesUsingFilterMapValuesAsJavaBeans();
        ethosFilterQueryClientExample.getPagesFromOffsetUsingFilterMapValues();
        ethosFilterQueryClientExample.getPagesFromOffsetUsingFilterMapValuesAsJavaBeans();
        ethosFilterQueryClientExample.getAccountCodesWithCriteriaFilter();
        ethosFilterQueryClientExample.getAccountCodesWithCriteriaFilterAsJavaBeans();
        ethosFilterQueryClientExample.applyPersonCommentsUsingJsonNodes();
        ethosFilterQueryClientExample.applyPersonCommentsUsingJavaBeans();
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
     * This is a list of criteria filter examples showing various ways to build criteria filters and the
     * criteria filter syntax generated.
     */
    public void criteriaFilterExamples() {
        /**********************************************************************************************************
        /* Examples 1 - 15 show how to build criteria filters primarily when making GET requests for Ethos APIs,
        /* as opposed to BPAPIs.  However, the Ethos Integration SDK makes no distinction between Ethos API and
        /* BPAPI requests.  How to build a criteria filter largely depends on which Ethos API a request is made for.
        /*********************************************************************************************************/

        System.out.println( "Examples 1 - 15 are primarily used for Ethos API requests as opposed to BPAPI requests, " +
                            "though the Ethos Integration SDK makes no distinction between the type of API request. " + System.lineSeparator());

        // Example 1:  This example generates this syntax:   ?criteria={"lastName":"Smith"}
        CriteriaFilter cf = new SimpleCriteria.Builder()
                .withSimpleCriteria("lastName", "Smith")
                .buildCriteriaFilter();
        System.out.println( "Example 1:  " + cf.toString() );

        // Example 2:  This example generates this syntax:   ?criteria={"names":{"lastName":"Smith"}}
        cf = new SimpleCriteria.Builder()
                .withSimpleCriteriaObject("names", "lastName", "Smith")
                .buildCriteriaFilter();
        System.out.println( "Example 2:  " + cf.toString() );

        // Example 3:  This example generates this syntax:   ?criteria={"ethos":{"resources":["persons","organizations"]}}
        SimpleCriteria.Builder scBuilder = new SimpleCriteria.Builder();
        cf = scBuilder.withSimpleCriteriaObject("ethos",
             scBuilder.withSimpleCriteriaValueArray("resources", "persons")
                      .addValue("organizations"))
             .buildCriteriaFilter();
        System.out.println( "Example 3:  " + cf.toString() );

        // Example 4:  This example generates this syntax:  ?criteria={"ethos":{"startOn":{"year":2021,"month":08}}}
        scBuilder = new SimpleCriteria.Builder();
        cf = scBuilder.withSimpleCriteriaObject("ethos",
             scBuilder.withMultiCriteriaObject("startOn", "year", "2021", true)
                      .addNumericSimpleCriteria("month", "08"))
             .buildCriteriaFilter();
        System.out.println( "Example 4:  " + cf.toString() );

        // Example 5:  This example generates this syntax:  ?criteria={"startOn":{"year":2021,"month":08}}
        cf = new SimpleCriteria.Builder()
                .withMultiCriteriaObject("startOn", "year", "2021", true)
                .addNumericSimpleCriteria("month", "08")
                .buildCriteriaFilter();
        System.out.println( "Example 5:  " + cf.toString() );

        // Example 6:  This example generates this syntax:  ?criteria={"credentials":["bannerId","colleagueId","anotherId"]}
        cf = new SimpleCriteria.Builder()
                .withSimpleCriteriaValueArray("credentials", "bannerId")
                .addValue("colleagueId")
                .addValue("anotherId")
                .buildCriteriaFilter();
        System.out.println( "Example 6:  " + cf.toString() );

        // Example 7:  This example generates this syntax:  ?criteria={"credentials":[{"type":{"id":"11111111-1111-1111-1111-111111111111"}},{"value":"someValue"}]}
        cf = new SimpleCriteria.Builder()
                .withSimpleCriteriaArray("credentials")
                .addSimpleCriteriaObject("type", "id", "11111111-1111-1111-1111-111111111111")
                .addSimpleCriteria("value", "someValue")
                .buildCriteriaFilter();
        System.out.println( "Example 7:  " + cf.toString() );

        // Example 8:  This example generates this syntax:  ?criteria={"names":[{"lastName":"Smith"}]}
        cf = new SimpleCriteria.Builder()
                .withSimpleCriteriaArray("names", "lastName", "Smith")
                .buildCriteriaFilter();
        System.out.println( "Example 8:  " + cf.toString() );

        // Example 9:  This example generates this syntax:  ?criteria={"authors":[{"person":{"id":"11111111-1111-1111-1111-111111111111"}}]}
        cf = new SimpleCriteria.Builder()
                .withSimpleCriteriaObjectArray("authors")
                .addSimpleCriteriaObject("person", "id", "11111111-1111-1111-1111-111111111111")
                .buildCriteriaFilter();
        System.out.println( "Example 9:  " + cf.toString() );

        // Example 10:  This example generates this syntax:  ?criteria={"solicitors":[{"solicitor":{"constituent":{"person":{"id":"11111111-1111-1111-1111-111111111111"}}}}]}
        scBuilder = new SimpleCriteria.Builder();
        cf = scBuilder.withSimpleCriteriaObjectArray("solicitors",
             scBuilder.withSimpleCriteriaObject("person", "id", "11111111-1111-1111-1111-111111111111")
                      .nestCriteria("constituent")
                      .nestCriteria("solicitor"))
             .buildCriteriaFilter();
        System.out.println( "Example 10: " + cf.toString() );

        // Example 11:  This example generates this syntax:  ?criteria={"credentials":[{"type":"bannerId","value":"myBannerId"}]}
        scBuilder = new SimpleCriteria.Builder();
        cf = scBuilder.withMultiCriteriaObjectArray("credentials")
                      .addMultiCriteriaObject(scBuilder.withMultiCriteriaObjectForArray("type", "bannerId").addSimpleCriteria("value", "myBannerId"))
                      .buildCriteriaFilter();
        System.out.println( "Example 11: " + cf.toString() );

        // Example 12:  This example generates this syntax:  ?keywordSearch={"keywordSearch": "Culture"}
        NamedQueryFilter namedQueryFilter = new SimpleCriteria.Builder()
                .withNamedQuery("keywordSearch", "keywordSearch", "Culture")
                .buildNamedQueryFilter();
        System.out.println( "Example 12: " + namedQueryFilter.toString() );

        // Example 13:  This example generates this syntax:  ?instructor={"instructor": {"id": "11111111-1111-1111-1111-111111111111"}}
        namedQueryFilter = new SimpleCriteria.Builder()
                .withNamedQueryObject("instructor", "instructor", "id", "11111111-1111-1111-1111-111111111111")
                .buildNamedQueryFilter();
        System.out.println( "Example 13: " + namedQueryFilter.toString() );

        // Example 14:  This example generates this syntax:  ?advancedSearch={"keyword": "Culture", "defaultSettings": {"id": "11111111-1111-1111-1111-111111111111"}}
        namedQueryFilter = new SimpleCriteria.Builder()
                .withNamedQueryCombination("advancedSearch", "keyword", "Culture")
                .addNamedQueryObject("defaultSettings", "id", "11111111-1111-1111-1111-111111111111")
                .buildNamedQueryFilter();
        System.out.println( "Example 14: " + namedQueryFilter.toString() );

        // Example 15:  This example generates this syntax:  ?registrationStatusesByAcademicPeriod={"academicPeriod":{"id":"11111111-1111-1111-1111-111111111111"},"statuses":[{"detail":{"id":"22222222-2222-2222-2222-222222222222"}}]}
        NamedQueryFilter nqf = new SimpleCriteria.Builder()
                .withNamedQueryObjectArrayCombination("registrationStatusesByAcademicPeriod", "academicPeriod", "id", "11111111-1111-1111-1111-111111111111")
                .withArrayLabel("statuses")
                .addToNamedQueryObjectArray("detail", "id", "22222222-2222-2222-2222-222222222222")
                .buildNamedQueryFilter();
        System.out.println( "Example 15: " + nqf.toString() );

        /**********************************************************************************************************
         /* Examples 16 - 17 show how to build criteria filters primarily when making GET requests for BPAPIs,
         /* as opposed to Ethos APIs.  However, the Ethos Integration SDK makes no distinction between Ethos API and
         /* BPAPI requests.
         /*********************************************************************************************************/

        System.out.println( System.lineSeparator() + "Examples 16 - 17 are primarily used for BPAPI requests as opposed to Ethos API requests, " +
                "though the Ethos Integration SDK makes no distinction between the type of API request." + System.lineSeparator() );

        // Example 16:  This example generates this syntax:  ?criteria={"acctCode":"04","statusInd":"A"}
        cf = new SimpleCriteria.Builder()
                .withMultiCriteriaObject(null,"acctCode", "04", false)
                .addSimpleCriteria("statusInd", "A")
                .buildCriteriaFilter();
        System.out.println( "Example 16: " + cf.toString() );

        // Example 17:  This example generates this syntax:  ?statusInd=A&acctCode=04
        FilterMap filterMap = new FilterMap.Builder()
                .withParameterPair("acctCode", "04")
                .withParameterPair("statusInd", "A")
                .build();
        System.out.println( "Example 17: " + filterMap.toString() );
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
            JsonNode jsonNode = ethosResponse.getContentAsJson();
            System.out.println( jsonNode.toPrettyString() );
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
            JsonNode jsonNode = ethosResponse.getContentAsJson();
            System.out.println( jsonNode.toPrettyString() );
        }
        catch( IOException ioe ) {
            ioe.printStackTrace();
        }
    }

    /**
     * This example shows how to build a CriteriaFilter to make a criteria filter GET request.
     * How the filter GET request is made depends on the resource requested.
     * The response body within the returned EthosResponse is a list of JavaBeans for easier property access.
     */
    public void getUsingCriteriaFilterWithJavaBeans() {
        System.out.println( "******* getUsingCriteriaFilterWithJavaBeans() using CriteriaFilter *******" );
        String resource = "persons";
        // Build a SimpleCriteriaArray filter because the persons resource supports that filter syntax.
        CriteriaFilter criteriaFilter = new SimpleCriteria.Builder()
                .withSimpleCriteriaArray("names", "firstName", "John")
                .buildCriteriaFilter();
        EthosFilterQueryClient ethosFilterQueryClient = getEthosFilterQueryClient();
        try {
            // Use the criteriaFilter to get a page of data for Persons specifying the Persons.class JavaBean type for easier response body handling.
            EthosResponse<List<Persons>> ethosResponse = ethosFilterQueryClient.getWithCriteriaFilter( resource, criteriaFilter, Persons.class );
            // Get the response body content from the ethosResponse as a list of Persons JavaBeans.
            List<Persons> personsList = ethosResponse.getContentAsType();
            System.out.println( "REQUESTED URL: " + ethosResponse.getRequestedUrl() );
            System.out.println( "Number of resources returned: " + personsList.size() );
            for( Persons persons : personsList ) {
                // We can use the getter methods on the persons object for easier property access, but just print out toString() here.
                System.out.println( "PERSON: " + persons.toString() );
            }
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
            JsonNode jsonNode = ethosResponse.getContentAsJson();
            System.out.println( jsonNode.toPrettyString() );
        }
        catch( IOException ioe ) {
            ioe.printStackTrace();
        }
    }

    /**
     * This example shows how to build a NamedQueryFilter to make a GET request for a resource that supports named queries.
     * The response body within the returned EthosResponse is a list of JavaBeans for easier property access.
     */
    public void getUsingNamedQueryFilterWithJavaBeans() {
        System.out.println( "******* getUsingNamedQueryFilterWithJavaBeans() using NamedQueryFilter *******" );
        String resource = "sections";
        String queryName = "keywordSearch";
        String queryKey = "keywordSearch";
        String queryValue = "Culture";
        NamedQueryFilter namedQueryFilter = new SimpleCriteria.Builder()
                .withNamedQuery( queryName, queryKey, queryValue)
                .buildNamedQueryFilter();
        EthosFilterQueryClient ethosFilterQueryClient = getEthosFilterQueryClient();
        try {
            // Use the namedQueryFilter to get a page of data for Sections specifying the Sections.class JavaBean type for easier response body handling.
            EthosResponse<List<Sections>> ethosResponse = ethosFilterQueryClient.getWithNamedQueryFilter( resource, namedQueryFilter, Sections.class );
            // Get the response body content from the ethosResponse as a list of Sections JavaBeans.
            List<Sections> sectionsList = ethosResponse.getContentAsType();
            System.out.println( "REQUESTED URL: " + ethosResponse.getRequestedUrl() );
            System.out.println( "Number of resources returned: " + sectionsList.size() );
            for( Sections sections : sectionsList ) {
                // We can use the getter methods on the sections object for easier property access, but just print out toString() here.
                System.out.println( "SECTION: " + sections.toString() );
            }
        }
        catch( IOException ioe ) {
            ioe.printStackTrace();
        }
    }

    /**
     * This example shows how to submit a GET request using values that form a simple criteria array to make the request.
     */
    public void getWithSimpleCriteriaArrayValues() {
        System.out.println( "******* getWithSimpleCriteriaArrayValues() using values *******" );
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
            JsonNode jsonNode = ethosResponse.getContentAsJson();
            System.out.println( jsonNode.toPrettyString() );
        }
        catch( IOException ioe ) {
            ioe.printStackTrace();
        }
    }

    /**
     * This example shows how to submit a GET request using values that form a simple criteria array to make the request.
     * The response body within the returned EthosResponse is a list of JavaBeans for easier property access.
     */
    public void getWithSimpleCriteriaArrayValuesAsJavaBeans() {
        System.out.println( "******* getWithSimpleCriteriaArrayValuesAsJavaBeans() using values *******" );
        String resource = "persons";
        String criteriaLabel = "names";
        String criteriaKey = "firstName";
        String criteriaValue = "John";
        EthosFilterQueryClient ethosFilterQueryClient = getEthosFilterQueryClient();
        try {
            // Just pass in the criteria label, key, and value using this method...the SDK does the rest of the work to make the request.
            // Specify the Persons.class JavaBean type for easier response body handling.
            EthosResponse<List<Persons>> ethosResponse = ethosFilterQueryClient.getWithSimpleCriteriaArrayValues( resource, criteriaLabel, criteriaKey, criteriaValue, Persons.class );
            // Get the response body content from the ethosResponse as a list of Sections JavaBeans.
            List<Persons> personsList = ethosResponse.getContentAsType();
            System.out.println( "REQUESTED URL: " + ethosResponse.getRequestedUrl() );
            System.out.println( "Number of resources returned: " + personsList.size() );
            for( Persons persons : personsList ) {
                // We can use the getter methods on the persons object for easier property access, but just print out toString() here.
                System.out.println( "PERSON: " + persons.toString() );
            }
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
            JsonNode jsonNode = ethosResponse.getContentAsJson();
            System.out.println( jsonNode.toPrettyString() );
        }
        catch( IOException ioe ) {
            ioe.printStackTrace();
        }
    }

    /**
     * This example shows how to use a filter map containing key/value pairs to make a GET request.
     * The response body within the returned EthosResponse is a list of JavaBeans for easier property access.
     */
    public void getUsingFilterMapAsJavaBeans() {
        System.out.println( "******* getUsingFilterMapAsJavaBeans() using filterMap *******" );
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
            // Specify the v6 in this example of the Persons.class JavaBean type from the version header value listed above for easy response body handling.
            EthosResponse<List<com.ellucian.generated.eedm.persons.v6_0.Persons>> ethosResponse = ethosFilterQueryClient.getWithFilterMap( resource, version, filterMap, com.ellucian.generated.eedm.persons.v6_0.Persons.class );
            // Get the response body content from the ethosResponse as a list of Sections JavaBeans.
            List<com.ellucian.generated.eedm.persons.v6_0.Persons> personsList = ethosResponse.getContentAsType();
            System.out.println( "REQUESTED URL: " + ethosResponse.getRequestedUrl() );
            System.out.println( "Number of resources returned: " + personsList.size() );
            for( com.ellucian.generated.eedm.persons.v6_0.Persons persons : personsList ) {
                // We can use the getter methods on the persons object for easier property access, but just print out toString() here.
                System.out.println( "PERSON: " + persons.toString() );
            }
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
                JsonNode jsonNode = ethosResponse.getContentAsJson();
                System.out.println( "PAGE SIZE: " + jsonNode.size() );
                System.out.println( jsonNode.toString() );
            }
        }
        catch( IOException ioe ) {
            ioe.printStackTrace();
        }
    }

    /**
     * This example shows how criteria filter requests also support paging when multiple pages are returned.
     * This uses a criteria filter on a GET request returning a list of EthosResponses where each EthosResponse
     * represents one page of data and contains a list of JavaBeans as the response body for easier property access.
     * This example shows how to retrieve ALL the pages from a criteria filter GET request.
     * NOTE:  IT MAY NOT BE ADVISABLE TO RUN THIS EXAMPLE BECAUSE THE QUANTITY OF DATA RETURNED COULD BE A LOT,
     * DEPENDING ON THE ENVIRONMENT THIS IS RUN AGAINST SINCE IT RETURNS ALL PAGES ACCORDING TO THE FILTER APPLIED.
     */
    public void getPagesUsingCriteriaFilterAsJavaBeans() {
        System.out.println( "******* getPagesUsingCriteriaFilterAsJavaBeans() using criteria filter *******" );
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
            List<EthosResponse<List<Persons>>> ethosResponseList = ethosFilterQueryClient.getPagesWithCriteriaFilter( resource, criteriaFilter, pageSize, Persons.class );
            System.out.println( "Number of pages returned: " + ethosResponseList.size() );
            // Each EthosResponse in the list represents a page of data.  So iterate through the ethosResponseList for each page.
            for( EthosResponse<List<Persons>> ethosResponse : ethosResponseList ) {
                // The response body in each ethosResponse is a list of Persons JavaBean objects.  So get that list.
                List<Persons> personsList = ethosResponse.getContentAsType();
                System.out.println( "PAGE SIZE: " + personsList.size() );
                // Iterate through the personsList for each person row.
                for( Persons persons : personsList ) {
                    // We can use the getter methods on the persons object for easier property access, but just print out toString() here.
                    System.out.println( "PERSON: " + persons.toString() );
                }
                System.out.println( "REQUESTED URL: " + ethosResponse.getRequestedUrl() );
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
                JsonNode jsonNode = ethosResponse.getContentAsJson();
                System.out.println( "PAGE SIZE: " + jsonNode.size() );
                System.out.println( jsonNode.toString() );
            }
        }
        catch( IOException ioe ) {
            ioe.printStackTrace();
        }
    }

    /**
     * This example shows how criteria filter requests also support paging when multiple pages are returned.
     * This uses a criteria filter on a GET request returning a list of EthosResponses where each EthosResponse
     * represents one page of data and contains a list of JavaBeans as the response body for easier property access.
     * In this example, a calculation is done to retrieve only the last 5% of rows due to the potential for paging across
     * many rows of data.
     */
    public void getPagesFromOffsetUsingCriteriaFilterAsJavaBeans() {
        System.out.println( "******* getPagesFromOffsetUsingCriteriaFilterAsJavaBeans() using criteria filter *******" );
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
            List<EthosResponse<List<Persons>>> ethosResponseList = ethosFilterQueryClient.getPagesFromOffsetWithCriteriaFilter( resourceName, criteriaFilter, pageSize, offset, Persons.class );
            System.out.println(String.format("Get data for resource: %s", resourceName));
            System.out.println(String.format("Calculated offset of %s which is 95 percent of a total count of %s to avoid paging through potentially lots of pages.", offset, totalCount));
            System.out.println("To run with more paging, manually set the offset to a lower value, or reduce the percentage of the total count.");
            System.out.println( "Number of pages returned: " + ethosResponseList.size() );
            System.out.println( "OFFSET: " + offset );
            // Each EthosResponse in the list represents a page of data.  So iterate through the ethosResponseList for each page.
            for( EthosResponse<List<Persons>> ethosResponse : ethosResponseList ) {
                // The response body in each ethosResponse is a list of Persons JavaBean objects.  So get that list.
                List<Persons> personsList = ethosResponse.getContentAsType();
                System.out.println( "PAGE SIZE: " + personsList.size() );
                // Iterate through the personsList for each person row.
                for( Persons persons : personsList ) {
                    // We can use the getter methods on the persons object for easier property access, but just print out toString() here.
                    System.out.println( "PERSON: " + persons.toString() );
                }
                System.out.println( "REQUESTED URL: " + ethosResponse.getRequestedUrl() );
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
                JsonNode jsonNode = ethosResponse.getContentAsJson();
                System.out.println( "PAGE SIZE: " + jsonNode.size() );
                System.out.println( jsonNode.toString() );
            }
        }
        catch( IOException ioe ) {
            ioe.printStackTrace();
        }
    }

    /**
     * This example shows how named-query requests also support paging when multiple pages are returned.
     * This uses a named-query on a GET request returning a list of EthosResponses where each EthosResponse
     * represents one page of data and contains a list of JavaBeans as the response body for easier property access.
     * In this example, a calculation is done to retrieve only the last 5% of rows due to
     * the potential for paging across many rows of data.
     */
    public void getPagesUsingNamedQueryFilterAsJavaBeans() {
        System.out.println( "******* getPagesUsingNamedQueryFilterAsJavaBeans() using named query filter *******" );
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
            List<EthosResponse<List<Sections>>> ethosResponseList = ethosFilterQueryClient.getPagesFromOffsetWithNamedQueryFilter( resourceName, namedQueryFilter, pageSize, offset, Sections.class );
            System.out.println(String.format("Get data for resource: %s", resourceName));
            System.out.println(String.format("Calculated offset of %s which is 95 percent of a total count of %s to avoid paging through potentially lots of pages.", offset, totalCount));
            System.out.println("To run with more paging, manually set the offset to a lower value, or reduce the percentage of the total count.");
            System.out.println( "Number of pages returned: " + ethosResponseList.size() );
            System.out.println( "OFFSET: " + offset );
            // Each EthosResponse in the list represents a page of data.  So iterate through the ethosResponseList for each page.
            for( EthosResponse<List<Sections>> ethosResponse : ethosResponseList ) {
                // The response body in each ethosResponse is a list of Sections JavaBean objects.  So get that list.
                List<Sections> sectionsList = ethosResponse.getContentAsType();
                System.out.println( "PAGE SIZE: " + sectionsList.size() );
                // Iterate through the sectionsList for each section row.
                for( Sections sections : sectionsList ) {
                    // We can use the getter methods on the sections object for easier property access, but just print out toString() here.
                    System.out.println( "SECTION: " + sections.toString() );
                }
                System.out.println( "REQUESTED URL: " + ethosResponse.getRequestedUrl() );
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
                // Printing out the response JSON string, but the response body can be handled using the Jackson JsonNode library.
                JsonNode jsonNode = ethosResponse.getContentAsJson();
                System.out.println( "PAGE SIZE: " + jsonNode.size() );
                System.out.println( jsonNode.toString() );
            }
        }
        catch( IOException ioe ) {
            ioe.printStackTrace();
        }
    }

    /**
     * This example shows how to retrieve ALL the pages from a filter map GET request.
     * This uses a FilterMap on a GET request returning a list of EthosResponses where each EthosResponse
     * represents one page of data and contains a list of JavaBeans as the response body for easier property access.
     * NOTE:  IT MAY NOT BE ADVISABLE TO RUN THIS EXAMPLE BECAUSE THE QUANTITY OF DATA RETURNED COULD BE A LOT,
     * DEPENDING ON THE ENVIRONMENT THIS IS RUN AGAINST SINCE IT RETURNS ALL PAGES ACCORDING TO THE FILTER APPLIED.
     */
    public void getPagesUsingFilterMapValuesAsJavaBeans() {
        System.out.println( "******* getPagesUsingFilterMapValuesAsJavaBeans() *******" );
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
            // Specifying v6 of the Persons JavaBean to make the request with per the version header value above.
            List<EthosResponse<List<com.ellucian.generated.eedm.persons.v6_0.Persons>>> ethosResponseList = ethosFilterQueryClient.getPagesWithFilterMap( resource, version, filterMap, pageSize, com.ellucian.generated.eedm.persons.v6_0.Persons.class );
            System.out.println( "Number of pages returned: " + ethosResponseList.size() );
            // Each EthosResponse in the list represents a page of data.  So iterate through the ethosResponseList for each page.
            for( EthosResponse<List<com.ellucian.generated.eedm.persons.v6_0.Persons>> ethosResponse : ethosResponseList ) {
                // The response body in each ethosResponse is a list of Persons JavaBean objects.  So get that list.
                List<com.ellucian.generated.eedm.persons.v6_0.Persons> personsList = ethosResponse.getContentAsType();
                System.out.println( "PAGE SIZE: " + personsList.size() );
                // Iterate through the personsList for each person row.
                for( com.ellucian.generated.eedm.persons.v6_0.Persons persons : personsList ) {
                    // We can use the getter methods on the persons object for easier property access, but just print out toString() here.
                    System.out.println( "PERSON: " + persons.toString() );
                }
                System.out.println( "REQUESTED URL: " + ethosResponse.getRequestedUrl() );
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
                JsonNode jsonNode = ethosResponse.getContentAsJson();
                System.out.println( "PAGE SIZE: " + jsonNode.size() );
                System.out.println( jsonNode.toString() );
            }
        }
        catch( IOException ioe ) {
            ioe.printStackTrace();
        }
    }

    /**
     * This example shows how filter map requests also support paging when multiple pages are returned.
     * This uses a filter map on a GET request returning a list of EthosResponses where each EthosResponse
     * represents one page of data and contains a list of JavaBeans as the response body for easier property access.
     * In this example, a calculation is done to retrieve only the last 5% of rows due to the potential for paging across
     * many rows of data.
     */
    public void getPagesFromOffsetUsingFilterMapValuesAsJavaBeans() {
        System.out.println( "******* getPagesFromOffsetUsingFilterMapValuesAsJavaBeans() *******" );
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
            // Specifying v6 of the Persons JavaBean to make the request with per the version header value above.
            List<EthosResponse<List<com.ellucian.generated.eedm.persons.v6_0.Persons>>> ethosResponseList = ethosFilterQueryClient.getPagesFromOffsetWithFilterMap( resourceName, version, filterMap, pageSize, offset, com.ellucian.generated.eedm.persons.v6_0.Persons.class );
            System.out.println(String.format("Get data for resource: %s", resourceName));
            System.out.println(String.format("Calculated offset of %s which is 95 percent of a total count of %s to avoid paging through potentially lots of pages.", offset, totalCount));
            System.out.println("To run with more paging, manually set the offset to a lower value, or reduce the percentage of the total count.");
            System.out.println( "Number of pages returned: " + ethosResponseList.size() );
            System.out.println( "OFFSET: " + offset );
            // Each EthosResponse in the list represents a page of data.  So iterate through the ethosResponseList for each page.
            for( EthosResponse<List<com.ellucian.generated.eedm.persons.v6_0.Persons>> ethosResponse : ethosResponseList ) {
                // The response body in each ethosResponse is a list of Persons JavaBean objects.  So get that list.
                List<com.ellucian.generated.eedm.persons.v6_0.Persons> personsList = ethosResponse.getContentAsType();
                System.out.println( "PAGE SIZE: " + personsList.size() );
                // Iterate through the personsList for each person row.
                for( com.ellucian.generated.eedm.persons.v6_0.Persons persons : personsList ) {
                    // We can use the getter methods on the persons object for easier property access, but just print out toString() here.
                    System.out.println( "PERSON: " + persons.toString() );
                }
                System.out.println( "REQUESTED URL: " + ethosResponse.getRequestedUrl() );
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
            JsonNode jsonNode = ethosResponse.getContentAsJson();
            System.out.println( "PAGE SIZE: " + jsonNode.size() );
            System.out.println( jsonNode.toPrettyString() );
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
    public void getAccountCodesWithCriteriaFilterAsJavaBeans() {
        System.out.println( "******* getAccountCodesWithCriteriaFilterAsJavaBeans() *******" );
        String resourceName = "account-codes";
        String version = "application/json";
        EthosFilterQueryClient ethosFilterQueryClient = getEthosFilterQueryClient();
        try {
            CriteriaFilter cf = new SimpleCriteria.Builder()
                    .withMultiCriteriaObject(null,"acctCode", "04", false)
                    .addSimpleCriteria("statusInd", "A")
                    .buildCriteriaFilter();
            // Specify the AccountCodes100GetResponse JavaBean to make this request with.
            EthosResponse<List<AccountCodes100GetResponse>> ethosResponse = ethosFilterQueryClient.getWithCriteriaFilter( resourceName, version, cf, AccountCodes100GetResponse.class );
            // The ethosResponse contains a list of account codes JavaBeans as the response body, so iterate through that list.
            List<AccountCodes100GetResponse> accountCodes100GetResponseList = ethosResponse.getContentAsType();
            for( AccountCodes100GetResponse accountCodes100GetResponse : accountCodes100GetResponseList ) {
                // We could use the getter methods on the account codes JavaBean for easy property access, but just printing toString() here.
                System.out.println( "ACCOUNT CODES: " + accountCodes100GetResponse.toString() );
            }
            System.out.println( "REQUESTED URL:  " + ethosResponse.getRequestedUrl() );
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
                    .withParameterPair("lastName", "Smith")
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
//                "id": "210009506",
//                "date": "10/02/2022T20:53:54.872Z",
//                "contactDate": "10/02/2022",
//                "cmttCode": "105",
//                "origCode": "CCON",
//                "confidentialInd": "s",
//                "text": "testing, testing, 1234",
//                "textNar": "1234, testing, testing"
//            }

            Instant rightNow = Instant.now().truncatedTo( ChronoUnit.SECONDS );
            // Use a JsonNode for the POST request body.
            ObjectNode personCommentNode = JsonNodeFactory.instance.objectNode();
            personCommentNode.put( "id", personId );
            personCommentNode.put( "date", rightNow.toString() );
            personCommentNode.put( "contactDate", rightNow.toString() );
            personCommentNode.put( "cmttCode", "105" );
            personCommentNode.put( "origCode", "CCON" );
            personCommentNode.put( "confidentialInd", "s" );
            personCommentNode.put( "text", "Testing through BPAPI Java SDK example" );
            personCommentNode.put( "textNar", "Testing through BPAPI Java SDK example" );

            // Make a POST request to add a new person comment using a JavaBean request body object.
            System.out.println( "Making POST request to add a new person comment for person with ID: " + personId + ", using contact date: " + personCommentNode.get("contactDate") );
            // Reset the resource name for the resource we are POSTing and PUTting to.
            resourceName = "person-comments";
            ethosResponse = ethosFilterQueryClient.post( resourceName, personCommentNode );
            System.out.println( "POST EthosResponse body: " + ethosResponse.getContent() );

            // All the other properties are already in the personCommentNode JsonNode object from the POST request,
            // so, change the contact date on the personCommentNode so that we can make a PUT request to update it.  Adds a day to the date.
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
                    .withParameterPair("lastName", "Smith")
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
            PersonComments100PostRequest personCommentsPostRequest = new PersonComments100PostRequest()
                    .withId(personId)
                    .withDate(today)
                    .withContactDate(today)
                    .withCmttCode("105")
                    .withOrigCode("CCON")
                    .withConfidentialInd("s")
                    .withText("adding a comment through Java SDK")
                    .withTextNar("comment through Java SDK");

            // Reset the resource name for the resource we are POSTing and PUTting to.
            resourceName = "person-comments";
            // Make a POST request to add a new person comment using a JavaBean request body object.
            System.out.println( "Making POST request to add a new person comment for person with ID: " + personId + ", using contact date: " + personCommentsPostRequest.getContactDate() );
            ethosResponse = ethosFilterQueryClient.post( resourceName, personCommentsPostRequest );
            System.out.println( "POST made using this URL: " + ethosResponse.getRequestedUrl() );
            System.out.println( "POST EthosResponse body: " + ethosResponse.getContent() );

            // Now build a person comment PUT request to update the contact date with the property values from the PersonComments100PostRequest object.
            PersonComments100PutRequest personCommentsPutRequest = new PersonComments100PutRequest()
                    .withId(personCommentsPostRequest.getId())
                    .withDate(today)
                    .withContactDate(Date.from(Instant.now().plus(1, ChronoUnit.DAYS))) // Add a day to the contact date to update it.
                    .withCmttCode(personCommentsPostRequest.getCmttCode())
                    .withOrigCode(personCommentsPostRequest.getOrigCode())
                    .withConfidentialInd(personCommentsPostRequest.getConfidentialInd())
                    .withText(personCommentsPostRequest.getText())
                    .withTextNar(personCommentsPostRequest.getTextNar());

            // Change the contact date on the personCommentRequest so that we can make a PUT request to update it.  Adds a day to the date.
            System.out.println( "Making PUT request with updated contact date: " + personCommentsPutRequest.getContactDate() );
            ethosResponse = ethosFilterQueryClient.put( resourceName, personCommentsPutRequest );
            System.out.println( "PUT made using this URL: " + ethosResponse.getRequestedUrl() );
            System.out.println( "PUT EthosResponse body: " + ethosResponse.getContent() );
        }
        catch( Exception e ) {
            e.printStackTrace();
        }
    }


}