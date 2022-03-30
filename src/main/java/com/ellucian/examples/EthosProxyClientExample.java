/*
 * ******************************************************************************
 *   Copyright 2022 Ellucian Company L.P. and its affiliates.
 * ******************************************************************************
 */
package com.ellucian.examples;


import com.ellucian.ethos.integration.client.EthosClientBuilder;
import com.ellucian.ethos.integration.client.EthosResponse;
import com.ellucian.ethos.integration.client.EthosResponseConverter;
import com.ellucian.ethos.integration.client.proxy.EthosProxyClient;
import com.ellucian.generated.eedm.person_holds.v6_0.Person;
import com.ellucian.generated.eedm.person_holds.v6_0.PersonHolds;
import com.ellucian.generated.eedm.person_holds.v6_0.Type;
import com.ellucian.generated.eedm.student_cohorts.v7_2_0.StudentCohorts;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.http.Header;
import org.apache.http.client.HttpResponseException;

import java.io.IOException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;

/**
 * This is an example class that shows how to use the EthosProxyClient to interact with the Ethos proxy service for
 * making API calls.
 */
public class EthosProxyClientExample {

    // ==========================================================================
    // Attributes
    // ==========================================================================
    private String apiKey;
    private String getByIdGUID;

    public EthosProxyClientExample(String apiKey, String guid ) {
        this.apiKey = apiKey;
        this.getByIdGUID = guid;
    }

    // ==========================================================================
    // Methods
    // ==========================================================================
    public static void main(String[] args) {
        if( args == null || args.length == 0 ) {
            System.out.println( "Please enter an API key and a valid student-cohorts GUID value (in that order) as program arguments when running this example class.  " +
                                "An API key is required to run these examples.\nIf a valid GUID value for student-cohorts is not provided, this class will still run but the " +
                                "example methods that get a resource by ID will be skipped.");
            return;
        }
        String apiKey = args[ 0 ];
        String getByIdGUID = null;
        if( args.length == 2 ) {
            getByIdGUID = args[ 1 ];
        }
        EthosProxyClientExample ethosProxyClientExample = new EthosProxyClientExample( apiKey, getByIdGUID );
        ethosProxyClientExample.doGetResourceByIdExample();
        ethosProxyClientExample.doGetResourceByIdAsStringExample();
        ethosProxyClientExample.doGetResourceByIdAsJsonNodeExample();
        ethosProxyClientExample.doGetResourceByIdAsJavaBeanExample();
        ethosProxyClientExample.doGetResourcePageSizeExample();
        ethosProxyClientExample.doGetResourceMaxPageSizeExample();
        ethosProxyClientExample.doGetResourceExample();
        ethosProxyClientExample.doGetResourceAsStringExample();
        ethosProxyClientExample.doGetResourceAsJsonNodeExample();
        ethosProxyClientExample.doGetResourceAsJavaBeanExample();
        ethosProxyClientExample.doGetResourceFromOffsetExample();
        ethosProxyClientExample.doGetResourceFromOffsetAsStringExample();
        ethosProxyClientExample.doGetResourceFromOffsetAsJavaBeansExample();
        ethosProxyClientExample.doGetResourceWithPageSizeExample();
        ethosProxyClientExample.doGetResourceWithPageSizeAsJsonNodeExample();
        ethosProxyClientExample.doGetResourceWithPageSizeAsJavaBeansExample();
        /*************************************************************
        Commenting the following methods, see block comment below.
        ethosProxyClientExample.doGetAllPagesExample();
        ethosProxyClientExample.doGetAllPagesAsStringsExample();
        ethosProxyClientExample.doGetAllPagesAsJsonNodesExample();
        *************************************************************/
        ethosProxyClientExample.doGetAllPagesFromOffsetExample();
        ethosProxyClientExample.doGetAllPagesFromOffsetAsStringsExample();
        ethosProxyClientExample.doGetAllPagesFromOffsetAsJsonNodesExample();
        ethosProxyClientExample.doGetAllPagesFromOffsetAsJavaBeansExample();
        ethosProxyClientExample.doGetPagesExample();
        ethosProxyClientExample.doGetPagesAsStringsExample();
        ethosProxyClientExample.doGetPagesAsJsonNodesExample();
        ethosProxyClientExample.doGetPagesAsJavaBeansExample();
        ethosProxyClientExample.doGetPagesFromOffsetExample();
        ethosProxyClientExample.doGetPagesFromOffsetAsStringsExample();
        ethosProxyClientExample.doGetPagesFromOffsetAsJsonNodesExample();
        ethosProxyClientExample.doGetPagesFromOffsetAsJavaBeansExample();
        ethosProxyClientExample.doGetRowsExample();
        ethosProxyClientExample.doGetRowsAsStringsExample();
        ethosProxyClientExample.doGetRowsAsJsonNodesExample();
        ethosProxyClientExample.doGetRowsAsJavaBeansExample();
        ethosProxyClientExample.doGetRowsFromOffsetExample();
        ethosProxyClientExample.doGetRowsFromOffsetAsStringsExample();
        ethosProxyClientExample.doGetRowsFromOffsetAsJsonNodesExample();
        ethosProxyClientExample.doGetRowsFromOffsetAsJavaBeansExample();
        ethosProxyClientExample.doGetResourcePageSizeExample();
        ethosProxyClientExample.doGetResourceMaxPageSizeExample();
        ethosProxyClientExample.doPostPutUsingJsonNodeExample();
        ethosProxyClientExample.doPostPutUsingJavaBeansExample();
    }


    /**
     * This is an example of how to build an EthosProxyClient client with the given API key.
     * @return An EthosProxyClient client.
     */
    private EthosProxyClient getEthosProxyClient() {
        return new EthosClientBuilder(apiKey)
                   .withConnectionTimeout(30)
                   .withConnectionRequestTimeout(30)
                   .withSocketTimeout(30)
                   .buildEthosProxyClient();
    }


    /**
     * This is just a utility method called by the various example methods for printing EthosResponse header values.
     * @param ethosResponse
     */
    public void printHeaders(EthosResponse ethosResponse) {
        System.out.println(String.format("Header: %s", ethosResponse.getHeader(EthosProxyClient.HDR_DATE)));
        System.out.println(String.format("Header: %s", ethosResponse.getHeader(EthosProxyClient.HDR_CONTENT_TYPE)));
        System.out.println(String.format("Header: %s", ethosResponse.getHeader(EthosProxyClient.HDR_X_TOTAL_COUNT)));
        System.out.println(String.format("Header: %s", ethosResponse.getHeader(EthosProxyClient.HDR_APPLICATION_CONTEXT)));
        System.out.println(String.format("Header: %s", ethosResponse.getHeader(EthosProxyClient.HDR_X_MAX_PAGE_SIZE)));
        System.out.println(String.format("Header: %s", ethosResponse.getHeader(EthosProxyClient.HDR_X_MEDIA_TYPE)));
        System.out.println(String.format("Header: %s", ethosResponse.getHeader(EthosProxyClient.HDR_HEDTECH_ETHOS_INTEGRATION_APPLICATION_ID)));
        System.out.println(String.format("Header: %s", ethosResponse.getHeader(EthosProxyClient.HDR_HEDTECH_ETHOS_INTEGRATION_APPLICATION_NAME)));
    }


    /**
     * This example shows how to get a initial page of data as an EthosResponse for the given resource.
     */
    public void doGetResourceExample() {
        EthosProxyClient ethosProxyClient = getEthosProxyClient();
        EthosResponse ethosResponse = null;
        try {
            String resourceName = "student-cohorts";
            // Get a page of data for the given resource.
            ethosResponse = ethosProxyClient.get(resourceName);
            Header totalCountHdr = ethosResponse.getHeader( EthosProxyClient.HDR_X_TOTAL_COUNT );
            System.out.println( "******* doGetResourceExample() *******" );
            System.out.println(String.format("Get data for resource: %s", resourceName));
            printHeaders(ethosResponse);
            System.out.println("get() TOTAL COUNT: " + totalCountHdr.getValue());
            // This prints the response body content, which is a JSON formatted string value.
            System.out.println("get() RESPONSE: " + ethosResponse.getContent());
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    /**
     * This is an example of getting a page of data as a JSON formatted string for the given resource.
     * Only the response body content is returned directly as a string value.
     */
    public void doGetResourceAsStringExample() {
        EthosProxyClient ethosProxyClient = getEthosProxyClient();
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            String resourceName = "student-cohorts";
            String response = ethosProxyClient.getAsString(resourceName);
            // Using an objectMapper to convert the string response body to JsonNode.
            JsonNode jsonNode = objectMapper.readTree( response );
            System.out.println( "******* doGetResourceAsStringExample() *******" );
            System.out.println(String.format("Get data for resource: %s", resourceName));
            System.out.println("getAsString() PAGE SIZE: " + jsonNode.size());
            System.out.println("getAsString() RESPONSE: " + response );
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    /**
     * This example gets a page of data as a Jackson JsonNode for the given resource.
     */
    public void doGetResourceAsJsonNodeExample() {
        EthosProxyClient ethosProxyClient = getEthosProxyClient();
        try {
            String resourceName = "student-cohorts";
            JsonNode jsonNode = ethosProxyClient.getAsJsonNode(resourceName);
            System.out.println( "******* doGetResourceAsJsonNodeExample() *******" );
            System.out.println(String.format("Get data for resource: %s", resourceName));
            System.out.println("getAsJsonNode() PAGE SIZE: " + jsonNode.size());
            System.out.println("getAsJsonNode() RESPONSE: " + jsonNode.toString() );
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    /**
     * This example gets a page of data as a list of JavaBeans for the given resource.
     */
    public void doGetResourceAsJavaBeanExample() {
        EthosProxyClient ethosProxyClient = getEthosProxyClient();
        try {
            String resourceName = "student-cohorts";
            // Specify the JavaBean class for the resource, ensure the version of the imported JavaBean package is correct.
            EthosResponse<List<StudentCohorts>> ethosResponse = ethosProxyClient.get( resourceName, StudentCohorts.class );
            // Get a list of StudentCohorts JavaBeans from the response.
            List<StudentCohorts> studentCohortsList = ethosResponse.getContentAsType();
            System.out.println( "******* doGetResourceAsJavaBeanExample() *******" );
            System.out.println(String.format("Get data for resource: %s", resourceName));
            System.out.println("getAsJavaBean() PAGE SIZE: " + studentCohortsList.size());
            for( StudentCohorts studentCohorts : studentCohortsList ) {
                // We can more easily access the properties with getter methods on the StudentCohorts JavaBean, but to
                // see the content just output toString().
                System.out.println("getAsJavaBean() RESPONSE: " + studentCohorts.toString() );
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    /**
     * This example gets a page of data from some offset as an EthosResponse for the given resource.
     */
    public void doGetResourceFromOffsetExample() {
        EthosProxyClient ethosProxyClient = getEthosProxyClient();
        try {
            EthosResponseConverter ethosResponseConverter = new EthosResponseConverter();
            String resourceName = "student-cohorts";
            int offset = 20;
            EthosResponse response = ethosProxyClient.getFromOffset( resourceName, offset );
            // Convert the ethosResponse into a JsonNode using the EthosResponseConverter.
            JsonNode jsonNode = ethosResponseConverter.toJsonNode( response );
            System.out.println( "******* doGetResourceFromOffsetExample *******" );
            System.out.println(String.format("Get data for resource: %s", resourceName));
            System.out.println("getFromOffset() PAGE SIZE: " + jsonNode.size());
            System.out.println("getFromOffset() RESPONSE: " + response.getContent() );
            System.out.println( String.format("OFFSET: %s", offset) );
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    /**
     * This example gets data directly as a JSON formatted string from some offset for the given resource.
     * Only the response body content is returned directly as a string value.
     */
    public void doGetResourceFromOffsetAsStringExample() {
        EthosProxyClient ethosProxyClient = getEthosProxyClient();
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            String resourceName = "student-cohorts";
            int offset = 20;
            String response = ethosProxyClient.getFromOffsetAsString( resourceName, offset );
            // Using an objectMapper to convert the response into a JsonNode.
            JsonNode jsonNode = objectMapper.readTree( response );
            System.out.println( "******* doGetResourceFromOffsetAsStringExample() *******" );
            System.out.println(String.format("Get data for resource: %s", resourceName));
            System.out.println("getFromOffsetAsString() PAGE SIZE: " + jsonNode.size());
            System.out.println("getFromOffsetAsString() RESPONSE: " + response );
            System.out.println( String.format("OFFSET: %s", offset) );
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    /**
     * This example gets an EthosResponse containing a response body as JavaBeans from some offset for the given resource.
     */
    public void doGetResourceFromOffsetAsJavaBeansExample() {
        EthosProxyClient ethosProxyClient = getEthosProxyClient();
        try {
            String resourceName = "student-cohorts";
            int offset = 20;
            // Specify the JavaBean class for the resource, ensure the version of the imported JavaBean package is correct.
            EthosResponse<List<StudentCohorts>> ethosResponse = ethosProxyClient.getFromOffset( resourceName, offset, StudentCohorts.class );
            // Get the response body content as a list of JavaBeans from the EthosResponse.
            List<StudentCohorts> studentCohortsList = ethosResponse.getContentAsType();
            System.out.println( "******* doGetResourceFromOffsetAsJavaBeansExample() *******" );
            System.out.println(String.format("Get data for resource: %s", resourceName));
            System.out.println( String.format("OFFSET: %s", offset) );
            for( StudentCohorts studentCohorts : studentCohortsList ) {
                // We can more easily access the properties with getter methods on the StudentCohorts JavaBean, but to
                // see the content just output toString().
                System.out.println("getAsJavaBean() RESPONSE: " + studentCohorts.toString() );
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    /**
     * This example gets the given resource with the specified page size.
     * The returned response will contain the page size (number of rows) specified.
     */
    public void doGetResourceWithPageSizeExample() {
        EthosProxyClient ethosProxyClient = getEthosProxyClient();
        try {
            EthosResponseConverter ethosResponseConverter = new EthosResponseConverter();
            String resourceName = "student-cohorts";
            int pageSize = 30;
            EthosResponse response = ethosProxyClient.getWithPageSize( resourceName, pageSize );
            JsonNode jsonNode = ethosResponseConverter.toJsonNode( response );
            System.out.println( "******* doGetResourceWithPageSizeExample() *******" );
            System.out.println(String.format("Get data for resource: %s", resourceName));
            System.out.println("getWithPageSize() PAGE SIZE: " + jsonNode.size());
            System.out.println("getWithPageSize() RESPONSE: " + response.getContent() );
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    /**
     * This example gets a page of data with the specified page size as a Jackson JsonNode for the given resource.
     */
    public void doGetResourceWithPageSizeAsJsonNodeExample() {
        EthosProxyClient ethosProxyClient = getEthosProxyClient();
        try {
            String resourceName = "student-cohorts";
            int pageSize = 30;
            JsonNode response = ethosProxyClient.getWithPageSizeAsJsonNode( resourceName, pageSize );
            System.out.println( "******* doGetResourceWithPageSizeAsJsonNodeExample() *******" );
            System.out.println(String.format("Get data for resource: %s", resourceName));
            System.out.println("getWithPageSizeAsJsonNode() PAGE SIZE: " + response.size());
            System.out.println("getWithPageSizeAsJsonNode() RESPONSE: " + response.toString() );
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    /**
     * This example gets an EthosResponse containing a response body as JavaBeans with some page size for the given resource.
     */
    public void doGetResourceWithPageSizeAsJavaBeansExample() {
        EthosProxyClient ethosProxyClient = getEthosProxyClient();
        try {
            String resourceName = "student-cohorts";
            int pageSize = 30;
            // Specify the JavaBean class for the resource, ensure the version of the imported JavaBean package is correct.
            EthosResponse<List<StudentCohorts>> ethosResponse = ethosProxyClient.getWithPageSize( resourceName, pageSize, StudentCohorts.class );
            // Get the response body content as a list of JavaBeans from the EthosResponse.
            List<StudentCohorts> studentCohortsList = ethosResponse.getContentAsType();
            System.out.println( "******* doGetResourceWithPageSizeAsJavaBeansExample() *******" );
            System.out.println(String.format("Get data for resource: %s", resourceName));
            System.out.println("getWithPageSizeAsJsonNode() PAGE SIZE: " + studentCohortsList.size());
            for( StudentCohorts studentCohorts : studentCohortsList ) {
                // We can more easily access the properties with getter methods on the StudentCohorts JavaBean, but to
                // see the content just output toString().
                System.out.println("getAsJavaBean() RESPONSE: " + studentCohorts.toString() );
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    /************************************************************************************************************
     * Commenting out the getAllPages*() methods due to the potential for a large quantity of data retrieved
     * at runtime.  But if desired, this code can be uncommented and executed as part of these example methods.
     * Be aware that these commented methods may take some time to run depending on the quantity of data,
     * since all pages are retrieved.
     ************************************************************************************************************/
//    /**
//     * This example gets all pages with the specified page size for the given resource.
//     */
//    public void doGetAllPagesExample() {
//        EthosProxyClient ethosProxyClient = getEthosProxyClient();
//        try {
//            String resourceName = "student-cohorts";
//            int pageSize = 15;
//            EthosResponseConverter ethosResponseConverter = new EthosResponseConverter();
//            List<EthosResponse> ethosResponseList = ethosProxyClient.getAllPages( resourceName, pageSize );
//            System.out.println( "******* doGetAllPagesExample() *******" );
//            System.out.println(String.format("Get data for resource: %s", resourceName));
//            for( int i = 0; i < ethosResponseList.size(); i++ ) {
//                JsonNode jsonNode = ethosResponseConverter.toJsonNode( ethosResponseList.get(i) );
//                System.out.println( String.format("PAGE %s: %s", (i+1), ethosResponseList.get(i).getContent()) );
//                System.out.println( String.format("PAGE %s SIZE: %s", (i+1), jsonNode.size()) );
//                System.out.println( String.format("PAGE %s REQUESTED URL: %s ", (i+1), ethosResponseList.get(i).getRequestedUrl()) );
//            }
//        } catch (IOException ioe) {
//            ioe.printStackTrace();
//        }
//    }
//

//    /**
//     * This example gets all pages with the specified page size as JSON formatted string responses for the given resource.
//     */
//    public void doGetAllPagesAsStringsExample() {
//        EthosProxyClient ethosProxyClient = getEthosProxyClient();
//        try {
//            String resourceName = "student-cohorts";
//            int pageSize = 15;
//            ObjectMapper objectMapper = new ObjectMapper();
//            List<String> stringList = ethosProxyClient.getAllPagesAsStrings( resourceName, pageSize );
//            System.out.println( "******* doGetAllPagesAsStringsExample() *******" );
//            System.out.println(String.format("Get data for resource: %s", resourceName));
//            for( int i = 0; i < stringList.size(); i++ ) {
//                JsonNode jsonNode = objectMapper.readTree( stringList.get(i) );
//                System.out.println( String.format("PAGE %s: %s", (i+1), stringList.get(i)) );
//                System.out.println( String.format("PAGE %s SIZE: %s", (i+1), jsonNode.size()) );
//            }
//        } catch (IOException ioe) {
//            ioe.printStackTrace();
//        }
//    }
//
//    /**
//     * This examples gets all pages as JsonNodes with the specified page size for the given resource.
//     */
//    public void doGetAllPagesAsJsonNodesExample() {
//        EthosProxyClient ethosProxyClient = getEthosProxyClient();
//        try {
//            String resourceName = "student-cohorts";
//            int pageSize = 15;
//            List<JsonNode> jsonNodeList = ethosProxyClient.getAllPagesAsJsonNodes( resourceName, pageSize );
//            System.out.println( "******* doGetAllPagesAsJsonNodesExample() *******" );
//            System.out.println(String.format("Get data for resource: %s", resourceName));
//            for( int i = 0; i < jsonNodeList.size(); i++ ) {
//                JsonNode jsonNode = jsonNodeList.get( i );
//                System.out.println( String.format("PAGE %s: %s", (i+1), jsonNode.toString()) );
//                System.out.println( String.format("PAGE %s SIZE: %s", (i+1), jsonNode.size()) );
//            }
//        } catch (IOException ioe) {
//            ioe.printStackTrace();
//        }
//    }

    /**
     * This example gets all pages for the given resource from some calculated offset value to demonstrate getting all
     * pages without paging for a long period of time through a potentially large volume of data.
     */
    public void doGetAllPagesFromOffsetExample() {
        EthosProxyClient ethosProxyClient = getEthosProxyClient();
        try {
            String resourceName = "student-cohorts";
            int totalCount = ethosProxyClient.getTotalCount( resourceName );
            // Calculate the offset to be 95% of the totalCount to avoid paging through potentially tons of pages.
            int offset = (int)(totalCount * 0.95);
            EthosResponseConverter ethosResponseConverter = new EthosResponseConverter();
            List<EthosResponse> ethosResponseList = ethosProxyClient.getAllPagesFromOffset( resourceName, offset );
            System.out.println( "******* doGetAllPagesFromOffsetExample() *******" );
            System.out.println(String.format("Get data for resource: %s", resourceName));
            System.out.println(String.format("Calculated offset of %s which is 95 percent of a total count of %s to avoid paging through potentially lots of pages.", offset, totalCount));
            System.out.println("To run with more paging, manually set the offset to a lower value, or reduce the percentage of the total count.");
            for( int i = 0; i < ethosResponseList.size(); i++ ) {
                JsonNode jsonNode = ethosResponseConverter.toJsonNode( ethosResponseList.get(i) );
                System.out.println( String.format("PAGE %s: %s", (i+1), ethosResponseList.get(i).getContent()) );
                System.out.println( String.format("PAGE %s SIZE: %s", (i+1), jsonNode.size()) );
                System.out.println( String.format("OFFSET: %s", offset) );
                System.out.println( String.format("PAGE %s REQUESTED URL: %s ", (i+1), ethosResponseList.get(i).getRequestedUrl()) );
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    /**
     * This example gets all pages for the given resource as JSON formatted string values from some calculated offset
     * to demonstrate getting all pages without paging for a long period of time through a potentially large volume of data.
     */
    public void doGetAllPagesFromOffsetAsStringsExample() {
        EthosProxyClient ethosProxyClient = getEthosProxyClient();
        try {
            String resourceName = "student-cohorts";
            int totalCount = ethosProxyClient.getTotalCount( resourceName );
            // Calculate the offset to be 95% of the totalCount to avoid paging through potentially tons of pages.
            int offset = (int)(totalCount * 0.95);
            ObjectMapper objectMapper = new ObjectMapper();
            List<String> stringList = ethosProxyClient.getAllPagesFromOffsetAsStrings( resourceName, offset );
            System.out.println( "******* doGetAllPagesFromOffsetAsStringsExample() *******" );
            System.out.println(String.format("Get data for resource: %s", resourceName));
            System.out.println(String.format("Calculated offset of %s which is 95 percent of a total count of %s to avoid paging through potentially lots of pages.", offset, totalCount));
            System.out.println("To run with more paging, manually set the offset to a lower value, or reduce the percentage of the total count.");
            for( int i = 0; i < stringList.size(); i++ ) {
                JsonNode jsonNode = objectMapper.readTree( stringList.get(i) );
                System.out.println( String.format("PAGE %s: %s", (i+1), stringList.get(i)) );
                System.out.println( String.format("PAGE %s SIZE: %s", (i+1), jsonNode.size()) );
                System.out.println( String.format("OFFSET: %s", offset) );
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }


    /**
     * This example gets all pages for the given resource as JsonNodes from some calculated offset value to demonstrate
     * getting all pages without paging for a long period of time through a potentially large volume of data.
     */
    public void doGetAllPagesFromOffsetAsJsonNodesExample() {
        EthosProxyClient ethosProxyClient = getEthosProxyClient();
        try {
            String resourceName = "student-cohorts";
            int totalCount = ethosProxyClient.getTotalCount( resourceName );
            // Calculate the offset to be 95% of the totalCount to avoid paging through potentially tons of pages.
            int offset = (int)(totalCount * 0.95);
            List<JsonNode> jsonNodeList = ethosProxyClient.getAllPagesFromOffsetAsJsonNodes( resourceName, offset );
            System.out.println( "******* doGetAllPagesFromOffsetAsJsonNodesExample() *******" );
            System.out.println(String.format("Get data for resource: %s", resourceName));
            System.out.println(String.format("Calculated offset of %s which is 95 percent a total count of %s to avoid paging through potentially lots of pages.", offset, totalCount));
            System.out.println("To run with more paging, manually set the offset to a lower value, or reduce the percentage of the total count.");
            for( int i = 0; i < jsonNodeList.size(); i++ ) {
                JsonNode jsonNode = jsonNodeList.get( i );
                System.out.println( String.format("PAGE %s: %s", (i+1), jsonNode.toString()) );
                System.out.println( String.format("PAGE %s SIZE: %s", (i+1), jsonNode.size()) );
                System.out.println( String.format("OFFSET: %s", offset) );
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    /**
     * This example gets all pages for the given resource from some calculated offset value to demonstrate getting all
     * pages without paging for a long period of time through a potentially large volume of data.
     * The EthosResponse returned contains a response body of JavaBeans for easier access to the contained data properties.
     */
    public void doGetAllPagesFromOffsetAsJavaBeansExample() {
        EthosProxyClient ethosProxyClient = getEthosProxyClient();
        try {
            String resourceName = "student-cohorts";
            int totalCount = ethosProxyClient.getTotalCount( resourceName );
            // Calculate the offset to be 95% of the totalCount to avoid paging through potentially tons of pages.
            int offset = (int)(totalCount * 0.95);
            EthosResponseConverter ethosResponseConverter = new EthosResponseConverter();
            List<EthosResponse<List<StudentCohorts>>> ethosResponseList = ethosProxyClient.getAllPagesFromOffset( resourceName, offset, StudentCohorts.class );
            System.out.println( "******* doGetAllPagesFromOffsetAsJavaBeansExample() *******" );
            System.out.println(String.format("Get data for resource: %s", resourceName));
            System.out.println(String.format("Calculated offset of %s which is 95 percent of a total count of %s to avoid paging through potentially lots of pages.", offset, totalCount));
            System.out.println("To run with more paging, manually set the offset to a lower value, or reduce the percentage of the total count.");
            for( int i = 0; i < ethosResponseList.size(); i++ ) {
                EthosResponse<List<StudentCohorts>> ethosResponse = ethosResponseList.get( i );
                List<StudentCohorts> studentCohortsList = ethosResponse.getContentAsType();
                System.out.println( String.format("PAGE %s SIZE: %s", (i+1), studentCohortsList.size()) );
                for( StudentCohorts studentCohorts : studentCohortsList ) {
                    // We can more easily access the properties with getter methods on the StudentCohorts JavaBean, but to
                    // see the content just output toString().
                    System.out.println( String.format("PAGE %s: %s", (i+1), studentCohorts.toString()) );
                }
                System.out.println( String.format("PAGE %s REQUESTED URL: %s ", (i+1), ethosResponse.getRequestedUrl()) );
            }
            System.out.println( String.format("OFFSET: %s", offset) );
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    /**
     * This example shows how to get some number of pages containing the specified page size.  Each EthosResponse in
     * the returned list represents a page of data.
     */
    public void doGetPagesExample() {
        EthosProxyClient ethosProxyClient = getEthosProxyClient();
        try {
            String resourceName = "student-cohorts";
            int pageSize = 15;
            int numPages = 3;
            EthosResponseConverter ethosResponseConverter = new EthosResponseConverter();
            List<EthosResponse> ethosResponseList = ethosProxyClient.getPages( resourceName, pageSize, numPages );
            System.out.println( "******* doGetPagesExample() *******" );
            System.out.println(String.format("Get data for resource: %s", resourceName));
            for( int i = 0; i < ethosResponseList.size(); i++ ) {
                JsonNode jsonNode = ethosResponseConverter.toJsonNode( ethosResponseList.get(i) );
                System.out.println( String.format("PAGE %s: %s", (i+1), ethosResponseList.get(i).getContent()) );
                System.out.println( String.format("PAGE %s SIZE: %s", (i+1), jsonNode.size()) );
                System.out.println( String.format("PAGE %s REQUESTED URL: %s ", (i+1), ethosResponseList.get(i).getRequestedUrl()) );
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    /**
     * This example shows how to get some number of pages for the given resource as a list of JSON formatted strings.
     */
    public void doGetPagesAsStringsExample() {
        EthosProxyClient ethosProxyClient = getEthosProxyClient();
        try {
            String resourceName = "student-cohorts";
            int pageSize = 15;
            int numPages = 3;
            ObjectMapper objectMapper = new ObjectMapper();
            List<String> stringList = ethosProxyClient.getPagesAsStrings( resourceName, pageSize, numPages );
            System.out.println( "******* doGetPagesAsStringsExample() *******" );
            System.out.println(String.format("Get data for resource: %s", resourceName));
            for( int i = 0; i < stringList.size(); i++ ) {
                JsonNode jsonNode = objectMapper.readTree( stringList.get(i) );
                System.out.println( String.format("PAGE %s: %s", (i+1), stringList.get(i)) );
                System.out.println( String.format("PAGE %s SIZE: %s", (i+1), jsonNode.size()) );
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    /**
     * This example show how to get some number of pages for the given resource as a list of JsonNodes.
     */
    public void doGetPagesAsJsonNodesExample() {
        EthosProxyClient ethosProxyClient = getEthosProxyClient();
        try {
            String resourceName = "student-cohorts";
            int pageSize = 15;
            int numPages = 3;
            List<JsonNode> jsonNodeList = ethosProxyClient.getPagesAsJsonNodes( resourceName, pageSize, numPages );
            System.out.println( "******* doGetPagesAsJsonNodesExample() *******" );
            System.out.println(String.format("Get data for resource: %s", resourceName));
            for( int i = 0; i < jsonNodeList.size(); i++ ) {
                JsonNode jsonNode = jsonNodeList.get( i );
                System.out.println( String.format("PAGE %s: %s", (i+1), jsonNode.toString()) );
                System.out.println( String.format("PAGE %s SIZE: %s", (i+1), jsonNode.size()) );
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    /**
     * This example shows how to get some number of pages containing the specified page size.  Each EthosResponse in
     * the returned list represents a page of data, and contains the response body as a list of JavaBeans for easier
     * property access.
     */
    public void doGetPagesAsJavaBeansExample() {
        EthosProxyClient ethosProxyClient = getEthosProxyClient();
        try {
            String resourceName = "student-cohorts";
            int pageSize = 15;
            int numPages = 3;
            List<EthosResponse<List<StudentCohorts>>> ethosResponseList = ethosProxyClient.getPages( resourceName, pageSize, numPages, StudentCohorts.class );
            System.out.println( "******* doGetPagesAsJavaBeansExample() *******" );
            System.out.println(String.format("Get data for resource: %s", resourceName));
            for( int i = 0; i < ethosResponseList.size(); i++ ) {
                EthosResponse<List<StudentCohorts>> ethosResponse = ethosResponseList.get( i );
                List<StudentCohorts> studentCohortsList = ethosResponse.getContentAsType();
                System.out.println( String.format("PAGE %s SIZE: %s", (i+1), studentCohortsList.size()) );
                for( StudentCohorts studentCohorts : studentCohortsList ) {
                    // We can more easily access the properties with getter methods on the StudentCohorts JavaBean, but to
                    // see the content just output toString().
                    System.out.println( String.format("PAGE %s: %s", (i+1), studentCohorts.toString()) );
                }
                System.out.println( String.format("PAGE %s REQUESTED URL: %s ", (i+1), ethosResponseList.get(i).getRequestedUrl()) );
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    /**
     * This example shows how to some number of pages containing the specified page size from the specified offset
     * for the given resource.
     */
    public void doGetPagesFromOffsetExample() {
        EthosProxyClient ethosProxyClient = getEthosProxyClient();
        try {
            String resourceName = "student-cohorts";
            int pageSize = 15;
            int offset = 10;
            int numPages = 3;
            EthosResponseConverter ethosResponseConverter = new EthosResponseConverter();
            List<EthosResponse> ethosResponseList = ethosProxyClient.getPagesFromOffset( resourceName, pageSize, offset, numPages );
            System.out.println( "******* doGetPagesFromOffsetExample() *******" );
            System.out.println(String.format("Get data for resource: %s", resourceName));
            System.out.println( String.format("OFFSET: %s", offset) );
            for( int i = 0; i < ethosResponseList.size(); i++ ) {
                JsonNode jsonNode = ethosResponseConverter.toJsonNode( ethosResponseList.get(i) );
                System.out.println( String.format("PAGE %s: %s", (i+1), ethosResponseList.get(i).getContent()) );
                System.out.println( String.format("PAGE %s SIZE: %s", (i+1), jsonNode.size()) );
                System.out.println( String.format("PAGE %s REQUESTED URL: %s ", (i+1), ethosResponseList.get(i).getRequestedUrl()) );
            }
            System.out.println( String.format("NUM PAGES: %s", ethosResponseList.size()) );
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    /**
     * This example shows how to get some number of pages as JSON formatted strings containing the specified page size
     * from some offset for the given resource.
     */
    public void doGetPagesFromOffsetAsStringsExample() {
        EthosProxyClient ethosProxyClient = getEthosProxyClient();
        try {
            String resourceName = "student-cohorts";
            int pageSize = 15;
            int offset = 10;
            int numPages = 3;
            ObjectMapper objectMapper = new ObjectMapper();
            List<String> stringList = ethosProxyClient.getPagesFromOffsetAsStrings( resourceName, pageSize, offset, numPages );
            System.out.println( "******* doGetPagesFromOffsetAsStringsExample() *******" );
            System.out.println(String.format("Get data for resource: %s", resourceName));
            System.out.println( String.format("OFFSET: %s", offset) );
            for( int i = 0; i < stringList.size(); i++ ) {
                JsonNode jsonNode = objectMapper.readTree( stringList.get(i) );
                System.out.println( String.format("PAGE %s: %s", (i+1), stringList.get(i)) );
                System.out.println( String.format("PAGE %s SIZE: %s", (i+1), jsonNode.size()) );
            }
            System.out.println( String.format("NUM PAGES: %s", stringList.size()) );
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    /**
     * This example shows how to get some number of pages as JsonNodes with the specified page size from the specified offset
     * for the given resource.
     */
    public void doGetPagesFromOffsetAsJsonNodesExample() {
        EthosProxyClient ethosProxyClient = getEthosProxyClient();
        try {
            String resourceName = "student-cohorts";
            int pageSize = 15;
            int offset = 10;
            int numPages = 3;
            List<JsonNode> jsonNodeList = ethosProxyClient.getPagesFromOffsetAsJsonNodes( resourceName, pageSize, offset, numPages );
            System.out.println( "******* doGetPagesFromOffsetAsJsonNodesExample() *******" );
            System.out.println(String.format("Get data for resource: %s", resourceName));
            System.out.println( String.format("OFFSET: %s", offset) );
            for( int i = 0; i < jsonNodeList.size(); i++ ) {
                JsonNode jsonNode = jsonNodeList.get( i );
                System.out.println( String.format("PAGE %s: %s", (i+1), jsonNode.toString()) );
                System.out.println( String.format("PAGE %s SIZE: %s", (i+1), jsonNode.size()) );
            }
            System.out.println( String.format("NUM PAGES: %s", jsonNodeList.size()) );
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    /**
     * This example shows how to some number of pages containing the specified page size from the specified offset
     * for the given resource.  Each EthosResponse in the returned list represents a page of data, and contains the response
     * body as a list of JavaBeans for easier property access.
     */
    public void doGetPagesFromOffsetAsJavaBeansExample() {
        EthosProxyClient ethosProxyClient = getEthosProxyClient();
        try {
            String resourceName = "student-cohorts";
            int pageSize = 15;
            int offset = 10;
            int numPages = 3;
            List<EthosResponse<List<StudentCohorts>>> ethosResponseList = ethosProxyClient.getPagesFromOffset( resourceName, pageSize, offset, numPages, StudentCohorts.class );
            System.out.println( "******* doGetPagesFromOffsetAsJavaBeansExample() *******" );
            System.out.println(String.format("Get data for resource: %s", resourceName));
            System.out.println( String.format("OFFSET: %s", offset) );
            for( int i = 0; i < ethosResponseList.size(); i++ ) {
                EthosResponse<List<StudentCohorts>> ethosResponse = ethosResponseList.get( i );
                List<StudentCohorts> studentCohortsList = ethosResponse.getContentAsType();
                System.out.println( String.format("PAGE %s SIZE: %s", (i+1), studentCohortsList.size()) );
                for( StudentCohorts studentCohorts : studentCohortsList ) {
                    // We can more easily access the properties with getter methods on the StudentCohorts JavaBean, but to
                    // see the content just output toString().
                    System.out.println( String.format("PAGE %s: %s", (i+1), studentCohorts.toString()) );
                }
                System.out.println( String.format("PAGE %s REQUESTED URL: %s ", (i+1), ethosResponseList.get(i).getRequestedUrl()) );
            }
            System.out.println( String.format("NUM PAGES: %s", ethosResponseList.size()) );
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    /**
     * This example shows how to get some number of rows using the specified page size for the given resource.
     * Each EthosResponse contains some number of rows up to the specified page size.  All EthosResponses in the
     * returned list together should contain the requested number of rows.
     */
    public void doGetRowsExample() {
        EthosProxyClient ethosProxyClient = getEthosProxyClient();
        try {
            String resourceName = "student-cohorts";
            String version = "application/vnd.hedtech.integration.v7.2.0+json";
            // The List<EthosResponse> returned by the getRows() method is page-based, not row-based, even though it is
            // returning some number of rows because of the ancillary header information, requested URL, etc.
            // that is pertinent to each page contained in each EthosResponse.
            // This specifies the page size to use when getting the number of rows as a list of EthosResponses.
            int pageSize = 15;
            int numRows = 40;
            int rowCount = 0;
            EthosResponseConverter ethosResponseConverter = new EthosResponseConverter();
            List<EthosResponse> ethosResponseList = ethosProxyClient.getRows( resourceName, version, pageSize, numRows );
            System.out.println( "******* doGetRowsExample() *******" );
            System.out.println(String.format("Get data for resource: %s", resourceName));
            for( int i = 0; i < ethosResponseList.size(); i++ ) {
                EthosResponse ethosResponse = ethosResponseList.get( i );
                JsonNode jsonNode = ethosResponseConverter.toJsonNode( ethosResponse );
                rowCount += jsonNode.size();
                System.out.println( String.format("PAGE %s: %s", (i+1), ethosResponse.getContent()) );
                System.out.println( String.format("PAGE %s SIZE: %s", (i+1), jsonNode.size()) );
                System.out.println( String.format("PAGE %s REQUESTED URL: %s ", (i+1), ethosResponseList.get(i).getRequestedUrl()) );
            }
            System.out.println( String.format("NUM ROWS: %s", rowCount) );
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }


    /**
     * This example shows how to get some number of rows as JSON formatted strings using the specified page size for the given resource.
     * Each string in the returned list represents a page of data and contains some number of rows up to the specified page size.  All
     * strings in the returned list together should contain the requested number of rows.
     */
    public void doGetRowsAsStringsExample() {
        EthosProxyClient ethosProxyClient = getEthosProxyClient();
        try {
            String resourceName = "student-cohorts";
            String version = "application/vnd.hedtech.integration.v7.2.0+json";
            // The List<String> returned by the getRowsAsStrings() method is row-based, not page-based, because it only
            // contains the resource data itself, and not the ancillary header information, requested URL, etc. found
            // in the EthosResponse.
            // This specifies the page size for the SDK to use internally when getting the rows.
            int pageSize = 15;
            int numRows = 17;
            List<String> stringList = ethosProxyClient.getRowsAsStrings( resourceName, version, pageSize, numRows );
            System.out.println( "******* doGetRowsAsStringsExample() *******" );
            System.out.println(String.format("Get data for resource: %s", resourceName));
            System.out.println( String.format("NUM ROWS: %s", stringList.size()) );
            for( int i = 0; i < stringList.size(); i++ ) {
                System.out.println( String.format("ROW %s: %s", (i+1), stringList.get(i)) );
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    /**
     * This example shows how to get some number of rows as JsonNodes using the specified number of rows for the given resource.
     * Each JsonNode in the returned list represents a row of data and contains the number of rows specified.
     */
    public void doGetRowsAsJsonNodesExample() {
        EthosProxyClient ethosProxyClient = getEthosProxyClient();
        try {
            String resourceName = "student-cohorts";
            String version = "application/vnd.hedtech.integration.v7.2.0+json";
            // The List<JsonNode> returned by the getRowsAsJsonNodes() method is row-based, not page-based, because it only
            // contains the resource data itself, and not the ancillary header information, requested URL, etc. found
            // in the EthosResponse.
            // Notice that the page size is NOT specified here so the SDK will use the default page size when paging internally
            // to get the specified number of rows.
            int numRows = 17;
            List<JsonNode> jsonNodeList = ethosProxyClient.getRowsAsJsonNodes( resourceName, version, numRows );
            System.out.println( "******* doGetRowsAsJsonNodesExample() *******" );
            System.out.println(String.format("Get data for resource: %s", resourceName));
            System.out.println( String.format("NUM ROWS: %s", jsonNodeList.size()) );
            for( int i = 0; i < jsonNodeList.size(); i++ ) {
                JsonNode jsonNode = jsonNodeList.get( i );
                System.out.println( String.format("ROW %s: %s", (i+1), jsonNode.toString()) );
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    /**
     * This example shows how to get some number of rows using the specified page size for the given resource.
     * Each EthosResponse contains some number of rows up to the specified page size.  All EthosResponses in the
     * returned list together should contain the requested number of rows.  Each EthosResponse in
     * the returned list represents a page of data, and contains the response body as a list of JavaBeans for easier
     * property access.
     */
    public void doGetRowsAsJavaBeansExample() {
        EthosProxyClient ethosProxyClient = getEthosProxyClient();
        try {
            String resourceName = "student-cohorts";
            String version = "application/vnd.hedtech.integration.v7.2.0+json";
            // The List<EthosResponse> returned by the getRows() method is page-based, not row-based, even though it is
            // returning some number of rows because of the ancillary header information, requested URL, etc.
            // that is pertinent to each page contained in each EthosResponse.
            // This specifies the page size to use when getting the number of rows as a list of EthosResponses.
            int pageSize = 15;
            int numRows = 40;
            int rowCount = 0;
            EthosResponseConverter ethosResponseConverter = new EthosResponseConverter();
            List<EthosResponse<List<StudentCohorts>>> ethosResponseList = ethosProxyClient.getRows( resourceName, version, pageSize, numRows, StudentCohorts.class );
            System.out.println( "******* doGetRowsAsJavaBeansExample() *******" );
            System.out.println(String.format("Get data for resource: %s", resourceName));
            for( int i = 0; i < ethosResponseList.size(); i++ ) {
                EthosResponse<List<StudentCohorts>> ethosResponse = ethosResponseList.get( i );
                List<StudentCohorts> studentCohortsList = ethosResponse.getContentAsType();
                System.out.println( String.format("PAGE %s SIZE: %s", (i+1), studentCohortsList.size()) );
                rowCount += studentCohortsList.size();
                for( StudentCohorts studentCohorts : studentCohortsList ) {
                    // We can more easily access the properties with getter methods on the StudentCohorts JavaBean, but to
                    // see the content just output toString().
                    System.out.println( String.format("PAGE %s: %s", (i+1), studentCohorts.toString()) );
                }
                System.out.println( String.format("PAGE %s REQUESTED URL: %s ", (i+1), ethosResponseList.get(i).getRequestedUrl()) );
            }
            System.out.println( String.format("NUM ROWS: %s", rowCount) );
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    /**
     * This example shows how to get some number of rows from the specified offset using the specified page size for
     * the given resource.  Each EthosResponse contains some number of rows up to the specified page size.  All EthosResponses in the
     * returned list together should contain the requested number of rows.
     */
    public void doGetRowsFromOffsetExample() {
        EthosProxyClient ethosProxyClient = getEthosProxyClient();
        try {
            String resourceName = "student-cohorts";
            String version = "application/json";
            // The List<EthosResponse> returned by the getRowsFromOffset() method is page-based, not row-based, even
            // though it is returning some number of rows because of the ancillary header information, requested URL, etc.
            // that is pertinent to each page contained in each EthosResponse.
            // This specifies the page size to use when getting the number of rows as a list of EthosResponses.
            int pageSize = 15;
            int offset = 10;
            int numRows = 24;
            int rowCount = 0;
            EthosResponseConverter ethosResponseConverter = new EthosResponseConverter();
            List<EthosResponse> ethosResponseList = ethosProxyClient.getRowsFromOffset( resourceName, version, pageSize, offset, numRows );
            System.out.println( "******* doGetRowsFromOffsetExample() *******" );
            System.out.println(String.format("Get data for resource: %s", resourceName));
            System.out.println( String.format("OFFSET: %s", offset) );
            for( int i = 0; i < ethosResponseList.size(); i++ ) {
                EthosResponse ethosResponse = ethosResponseList.get( i );
                JsonNode jsonNode = ethosResponseConverter.toJsonNode( ethosResponse );
                rowCount += jsonNode.size();
                System.out.println( String.format("PAGE %s: %s", (i+1), ethosResponse.getContent()) );
                System.out.println( String.format("PAGE %s SIZE: %s", (i+1), jsonNode.size()) );
                System.out.println( String.format("PAGE %s REQUESTED URL: %s ", (i+1), ethosResponseList.get(i).getRequestedUrl()) );
            }
            System.out.println( String.format("NUM ROWS: %s", rowCount) );
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    /**
     * This example shows how to get some number of rows from the specified offset for the given resource as JSON formatted
     * strings.  Each string in the returned list represents a page of data.  All strings in the returned list together should
     * contain the requested number of rows.
     */
    public void doGetRowsFromOffsetAsStringsExample() {
        EthosProxyClient ethosProxyClient = getEthosProxyClient();
        try {
            String resourceName = "student-cohorts";
            String version = "application/json";
            // The List<String> returned by the getRowsFromOffsetAsStrings() method is row-based, not page-based, because it only
            // contains the resource data itself, and not the ancillary header information, requested URL, etc. found
            // in the EthosResponse.
            // Notice that the page size is NOT specified here so the SDK will use the default page size when paging internally
            // to get the specified number of rows.
            int offset = 10;
            int numRows = 17;
            List<String> stringList = ethosProxyClient.getRowsFromOffsetAsStrings( resourceName, version, offset, numRows );
            System.out.println( "******* doGetRowsFromOffsetAsStringsExample() *******" );
            System.out.println( String.format("Get data for resource: %s", resourceName) );
            System.out.println( String.format("NUM ROWS: %s", stringList.size()) );
            System.out.println( String.format("OFFSET: %s", offset) );
            for( int i = 0; i < stringList.size(); i++ ) {
                System.out.println( String.format("ROW %s: %s", (i+1), stringList.get(i)) );
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    /**
     * This example shows how to get some number of rows from the specified offset using the specified page size for the
     * given resource.  Each JsonNode in the returned list represents a row of data and contains the number of rows specified.
     */
    public void doGetRowsFromOffsetAsJsonNodesExample() {
        EthosProxyClient ethosProxyClient = getEthosProxyClient();
        try {
            String resourceName = "student-cohorts";
            String version = "application/json";
            // The List<JsonNode> returned by the getRowsAsJsonNodes() method is row-based, not page-based, because it only
            // contains the resource data itself, and not the ancillary header information, requested URL, etc. found
            // in the EthosResponse.
            // This specifies the page size for the SDK to use internally when getting the rows.
            int pageSize = 15;
            int offset = 10;
            int numRows = 17;
            List<JsonNode> jsonNodeList = ethosProxyClient.getRowsFromOffsetAsJsonNodes( resourceName, version, pageSize, offset, numRows );
            System.out.println( "******* doGetRowsFromOffsetAsJsonNodesExample() *******" );
            System.out.println( String.format("Get data for resource: %s", resourceName) );
            System.out.println( String.format("NUM ROWS: %s", jsonNodeList.size()) );
            System.out.println( String.format("OFFSET: %s", offset) );
            for( int i = 0; i < jsonNodeList.size(); i++ ) {
                JsonNode jsonNode = jsonNodeList.get( i );
                System.out.println( String.format("ROW %s: %s", (i+1), jsonNode.toString()) );
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    /**
     * This example shows how to get some number of rows from the specified offset using the specified page size for
     * the given resource.  Each EthosResponse contains some number of rows up to the specified page size.  All EthosResponses in the
     * returned list together should contain the requested number of rows.  Each EthosResponse in
     * the returned list represents a page of data, and contains the response body as a list of JavaBeans for easier
     * property access.
     */
    public void doGetRowsFromOffsetAsJavaBeansExample() {
        EthosProxyClient ethosProxyClient = getEthosProxyClient();
        try {
            String resourceName = "student-cohorts";
            String version = "application/json";
            // The List<EthosResponse> returned by the getRowsFromOffset() method is page-based, not row-based, even
            // though it is returning some number of rows because of the ancillary header information, requested URL, etc.
            // that is pertinent to each page contained in each EthosResponse.
            // This specifies the page size to use when getting the number of rows as a list of EthosResponses.
            int pageSize = 15;
            int offset = 10;
            int numRows = 24;
            int rowCount = 0;
            List<EthosResponse<List<StudentCohorts>>> ethosResponseList = ethosProxyClient.getRowsFromOffset( resourceName, version, pageSize, offset, numRows, StudentCohorts.class );
            System.out.println( "******* doGetRowsFromOffsetAsJavaBeansExample() *******" );
            System.out.println(String.format("Get data for resource: %s", resourceName));
            System.out.println( String.format("OFFSET: %s", offset) );
            for( int i = 0; i < ethosResponseList.size(); i++ ) {
                EthosResponse<List<StudentCohorts>> ethosResponse = ethosResponseList.get( i );
                List<StudentCohorts> studentCohortsList = ethosResponse.getContentAsType();
                System.out.println( String.format("PAGE %s SIZE: %s", (i+1), studentCohortsList.size()) );
                rowCount += studentCohortsList.size();
                for( StudentCohorts studentCohorts : studentCohortsList ) {
                    // We can more easily access the properties with getter methods on the StudentCohorts JavaBean, but to
                    // see the content just output toString().
                    System.out.println( String.format("PAGE %s: %s", (i+1), studentCohorts.toString()) );
                }
                System.out.println( String.format("PAGE %s REQUESTED URL: %s ", (i+1), ethosResponseList.get(i).getRequestedUrl()) );
            }
            System.out.println( String.format("NUM ROWS: %s", rowCount) );
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    /**
     * This is an example of how to get a resource by ID (GUID).
     * The returned EthosResponse contains a JSON formatted string response body for the single instance of the resource
     * for the given ID.
     */
    public void doGetResourceByIdExample() {
        EthosProxyClient ethosProxyClient = getEthosProxyClient();
        String resource = "student-cohorts";
        try {
            if( getByIdGUID != null && getByIdGUID.isBlank() == false ) {
                EthosResponse ethosResponse = ethosProxyClient.getById(resource, getByIdGUID);
                System.out.println("******* doGetResourceByIdExample() *******");
                System.out.println(String.format("RESOURCE: %s", resource));
                System.out.println(String.format("RESOURCE ID: %s", getByIdGUID));
                System.out.println(String.format("RESPONSE: %s", ethosResponse.getContent()));
                System.out.println(String.format("REQUESTED URL: %s ", ethosResponse.getRequestedUrl()));
            }
            else {
                System.out.println( "******* Skipping doGetResourceByIdExample() because the getByIdGUID was not set.  Please pass in a valid GUID value as a 2nd program argument to run this method. *******" );
            }
        }
        catch( IOException ioe ) {
            ioe.printStackTrace();
        }
    }

    /**
     * This is an example of how to get a resource by ID (GUID).
     * The response is a JSON formatted string response body for the single instance of the resource
     * for the given ID.
     */
    public void doGetResourceByIdAsStringExample() {
        EthosProxyClient ethosProxyClient = getEthosProxyClient();
        String resource = "student-cohorts";
        try {
            if( getByIdGUID != null && getByIdGUID.isBlank() == false ) {
                String response = ethosProxyClient.getByIdAsString(resource, getByIdGUID);
                System.out.println("******* doGetResourceAsStringByIdExample() *******");
                System.out.println(String.format("RESOURCE: %s", resource));
                System.out.println(String.format("RESOURCE ID: %s", getByIdGUID));
                System.out.println(String.format("RESPONSE: %s", response));
            }
            else {
                System.out.println( "******* Skipping doGetResourceAsStringByIdExample() because the getByIdGUID was not set.  Please pass in a valid GUID value as a 2nd program argument to run this method. *******" );
            }
        }
        catch( IOException ioe ) {
            ioe.printStackTrace();
        }
    }

    /**
     * This is an example of how to get a resource by ID (GUID).
     * The response is a JsonNode response body for the single instance of the resource
     * for the given ID.
     */
    public void doGetResourceByIdAsJsonNodeExample() {
        EthosProxyClient ethosProxyClient = getEthosProxyClient();
        String resource = "student-cohorts";
        try {
            if( getByIdGUID != null && getByIdGUID.isBlank() == false ) {
                JsonNode jsonNode = ethosProxyClient.getByIdAsJsonNode(resource, getByIdGUID);
                System.out.println("******* doGetResourceAsJsonNodeByIdExample() *******");
                System.out.println(String.format("RESOURCE: %s", resource));
                System.out.println(String.format("RESOURCE ID: %s", getByIdGUID));
                System.out.println(String.format("RESPONSE: %s", jsonNode.toString()));
            }
            else {
                System.out.println( "******* Skipping doGetResourceAsJsonNodeByIdExample() because the getByIdGUID was not set.  Please pass in a valid GUID value as a 2nd program argument to run this method. *******" );
            }
        }
        catch( IOException ioe ) {
            ioe.printStackTrace();
        }
    }

    /**
     * This is an example of how to get a resource by ID (GUID).
     * The returned EthosResponse contains a JavaBean for the single instance of the resource for the given ID.
     */
    public void doGetResourceByIdAsJavaBeanExample() {
        EthosProxyClient ethosProxyClient = getEthosProxyClient();
        String resource = "student-cohorts";
        try {
            if (getByIdGUID != null && getByIdGUID.isBlank() == false) {
                EthosResponse<StudentCohorts> ethosResponse = ethosProxyClient.getById(resource, getByIdGUID, StudentCohorts.class);
                System.out.println("******* doGetResourceByIdAsJavaBeanExample() *******");
                System.out.println(String.format("RESOURCE: %s", resource));
                System.out.println(String.format("RESOURCE ID: %s", getByIdGUID));
                // Get the studentCohorts response body as a JavaBean from the ethosResponse.
                StudentCohorts studentCohorts = ethosResponse.getContentAsType();
                System.out.println(String.format("RESPONSE: %s", studentCohorts.toString()));
                System.out.println(String.format("REQUESTED URL: %s ", ethosResponse.getRequestedUrl()));
            } else {
                System.out.println("******* Skipping doGetResourceByIdExample() because the getByIdGUID was not set.  Please pass in a valid GUID value as a 2nd program argument to run this method. *******");
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    /**
     * This is an example of getting the default page size for a given resource.
     */
    public void doGetResourcePageSizeExample() {
        EthosProxyClient ethosProxyClient = getEthosProxyClient();
        String resource = "student-cohorts";
        try {
            int pageSize = ethosProxyClient.getPageSize( resource );
            System.out.println("******* doGetResourcePageSizeExample() *******");
            System.out.println( String.format("RESOURCE: %s", resource) );
            System.out.println( String.format("PAGE SIZE: %s", pageSize) );
        }
        catch( IOException ioe ) {
            ioe.printStackTrace();
        }
    }

    /**
     * This is an example of getting the max page size derived from the 'x-max-page-size' header
     * for the given resource.
     */
    public void doGetResourceMaxPageSizeExample() {
        EthosProxyClient ethosProxyClient = getEthosProxyClient();
        String resource = "student-cohorts";
        try {
            int pageSize = ethosProxyClient.getMaxPageSize( resource );
            System.out.println("******* doGetResourceMaxPageSizeExample() *******");
            System.out.println( String.format("RESOURCE: %s", resource) );
            System.out.println( String.format("MAX PAGE SIZE: %s", pageSize) );
        }
        catch( IOException ioe ) {
            ioe.printStackTrace();
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
    public void doPostPutUsingJsonNodeExample() {
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
    public void doPostPutUsingJavaBeansExample() {
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

}
