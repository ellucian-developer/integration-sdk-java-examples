/*
 * ******************************************************************************
 *   Copyright 2022 Ellucian Company L.P. and its affiliates.
 * ******************************************************************************
 */
package com.ellucian.examples;

import com.ellucian.ethos.integration.client.EthosClientBuilder;
import com.ellucian.ethos.integration.client.EthosResponse;
import com.ellucian.ethos.integration.client.proxy.EthosProxyClientAsync;
import com.ellucian.generated.eedm.student_cohorts.v7_2_0.StudentCohorts;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.ExecutionException;

/**
 * This is an example class that shows how to use the EthosProxyClientAsync for asynchronous handling of the responses
 * from API GET requests that may take longer to process.
 */
public class EthosProxyClientAsyncExample {

    // ==========================================================================
    // Attributes
    // ==========================================================================
    private String apiKey;

    public EthosProxyClientAsyncExample(String apiKey) {
        this.apiKey = apiKey;
    }

    // ==========================================================================
    // Methods
    // ==========================================================================
    public static void main(String[] args) {
        if( args == null || args.length == 0 ) {
            System.out.println( "Please enter an API key as a program argument when running this example class.  " +
                                "An API key is required to run these examples." );
            return;
        }
        String apiKey = args[ 0 ];

        EthosProxyClientAsyncExample ethosProxyClientExample = new EthosProxyClientAsyncExample( apiKey );

        // Asynchronous examples
        ethosProxyClientExample.doGetAllPagesFromOffsetAsyncExample();
        ethosProxyClientExample.doGetAllPagesFromOffsetAsJavaBeansAsyncExample();
        ethosProxyClientExample.doGetAllPagesFromOffsetAsStringsAsyncExample();
        ethosProxyClientExample.doGetRowsFromOffsetAsyncExample();
        ethosProxyClientExample.doGetRowsFromOffsetAsJavaBeansAsyncExample();
    }

    /**
     * This is an example of using the EthosClientBuilder to build an EthosProxyClientAsync client with the given API key.
     * @return an EthosProxyClientAsync client.
     */
    private EthosProxyClientAsync getEthosProxyClientAsync() {
        return new EthosClientBuilder(apiKey)
                .withConnectionTimeout(30)
                .withConnectionRequestTimeout(30)
                .withSocketTimeout(30)
                .buildEthosProxyAsyncClient();
    }

    /**
     * This is an example of how to get all pages from some offset asynchronously.
     */
    public void doGetAllPagesFromOffsetAsyncExample() {
        EthosProxyClientAsync ethosProxyClientAsync = getEthosProxyClientAsync();
        try {
            System.out.println( "******* doGetAllPagesFromOffsetExample() *******" );
            String resourceName = "student-cohorts";
            int totalCount = ethosProxyClientAsync.getTotalCount( resourceName );
            // Calculate the offset to be 95% of the totalCount to avoid paging through potentially tons of pages.
            int offset = (int)(totalCount * 0.95);
            ObjectMapper objectMapper = new ObjectMapper();

            // Wait on the result with exception handling
            CompletableFuture<List<EthosResponse>> asyncResponse = ethosProxyClientAsync.getAllPagesFromOffsetAsync( resourceName, offset );

            // While the CompletableFuture thread is running, additional operations can be performed.  For the sake of
            // demonstrating this we are just running a few printlns to print out the current time.
            System.out.println(LocalDateTime.now());
            try {
                // Other processing could occur in an actual application, but this is sleeping here to simulate the time taken for other processing
                // until we are ready to handle the actual response from the ethosProxyClientAsync call.
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(LocalDateTime.now());

            // This is to simulate that when ready, use .get() to allow for InterruptedException and ExecutionException handling.  This blocks here until
            // the response data is completely returned, unless it already has returned.
            List<EthosResponse> ethosResponseList = asyncResponse.get();

            System.out.println(String.format("Get data for resource: %s", resourceName));
            System.out.println(String.format("Calculated offset of %s which is 95 percent of a total count of %s to avoid paging through potentially lots of pages.", offset, totalCount));
            System.out.println("To run with more paging, manually set the offset to a lower value, or reduce the percentage of the total count.");
            for( int i = 0; i < ethosResponseList.size(); i++ ) {
                // This reads the response body content JSON string into a Jackson library JsonNode, while enabling the use of
                // the EthosResponse containing helpful info like the requested URL below.  To return only the response bodies
                // for each page directly as JsonNodes, use ethosProxyClientAsync.getAllPagesFromOffsetAsJsonNodesAsync().
                JsonNode jsonNode = objectMapper.readTree( ethosResponseList.get(i).getContent() );
                System.out.println( String.format("PAGE %s: %s", (i+1), ethosResponseList.get(i).getContent()) );
                System.out.println( String.format("PAGE %s SIZE: %s", (i+1), jsonNode.size()) );
                System.out.println( String.format("OFFSET: %s", offset) );
                System.out.println( String.format("PAGE %s REQUESTED URL: %s ", (i+1), ethosResponseList.get(i).getRequestedUrl()) );
            }
        } catch (CompletionException | IOException | InterruptedException | ExecutionException ioe) {
            // catching InterruptedException, and ExecutionException, is required when using .get to get the value.
            ioe.printStackTrace();
        }
    }

    /**
     * This is an example of how to get all pages from some offset asynchronously.
     * For each EthosResponse in the list of EthosResponses in the returned asynchronous response, the response body is a list of
     * JavaBean objects of the specified type for easier property access.
     */
    public void doGetAllPagesFromOffsetAsJavaBeansAsyncExample() {
        EthosProxyClientAsync ethosProxyClientAsync = getEthosProxyClientAsync();
        try {
            System.out.println( "******* doGetAllPagesFromOffsetAsJavaBeansAsyncExample() *******" );
            String resourceName = "student-cohorts";
            int totalCount = ethosProxyClientAsync.getTotalCount( resourceName );
            // Calculate the offset to be 95% of the totalCount to avoid paging through potentially tons of pages.
            int offset = (int)(totalCount * 0.95);

            // Specify the JavaBean class to use for easier property access when dealing with the response bodies.
            // This returns an asynchronous response from which the response data can be retrieved later, when ready.
            CompletableFuture<List<EthosResponse<List<StudentCohorts>>>> asyncResponse = ethosProxyClientAsync.getAllPagesFromOffsetAsync( resourceName, offset, StudentCohorts.class );

            // While the CompletableFuture thread is running, additional operations can be performed.  For the sake of
            // demonstrating this we are just running a few printlns to print out the current time.
            System.out.println(LocalDateTime.now());
            try {
                // Other processing could occur in an actual application, but this is sleeping here to simulate the time taken for other processing
                // until we are ready to handle the actual response from the ethosProxyClientAsync call.
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(LocalDateTime.now());

            // This is to simulate that when ready, use .get() to allow for InterruptedException and ExecutionException handling.  This blocks here until
            // the response data is completely returned, unless it already has returned.
            List<EthosResponse<List<StudentCohorts>>> ethosResponseList = asyncResponse.get();

            System.out.println(String.format("Get data for resource: %s", resourceName));
            System.out.println(String.format("Calculated offset of %s which is 95 percent of a total count of %s to avoid paging through potentially lots of pages.", offset, totalCount));
            System.out.println("To run with more paging, manually set the offset to a lower value, or reduce the percentage of the total count.");
            for( int i = 0; i < ethosResponseList.size(); i++ ) {
                EthosResponse<List<StudentCohorts>> ethosResponse = ethosResponseList.get( i );
                // Each ethosResponse in the ethosResponseList represent a page of data, and contains a list of StudentCohort JavaBeans.
                List<StudentCohorts> studentCohortsList = ethosResponse.getContentAsType();
                System.out.println( String.format("PAGE %s SIZE: %s", (i+1), studentCohortsList.size()) );
                for( StudentCohorts studentCohorts : studentCohortsList ) {
                    // We could use the getter methods on the studentCohorts JavaBean, but just printing toString() here to see the data.
                    System.out.println( String.format("PAGE %s: %s", (i+1), studentCohorts.toString()) );
                }
                System.out.println( String.format("OFFSET: %s", offset) );
                System.out.println( String.format("PAGE %s REQUESTED URL: %s ", (i+1), ethosResponse.getRequestedUrl()) );
            }
        } catch (CompletionException | IOException | InterruptedException | ExecutionException ioe) {
            // catching InterruptedException, and ExecutionException, is required when using .get to get the value.
            ioe.printStackTrace();
        }
    }

    /**
     * This is an example of how to get all pages with only the response bodies as JSON formatted strings using the
     * EthosProxyClientAsync from some offset value.
     */
    public void doGetAllPagesFromOffsetAsStringsAsyncExample() {
        EthosProxyClientAsync ethosProxyClient = getEthosProxyClientAsync();
        try {
            System.out.println( "******* doGetAllPagesFromOffsetAsStringsAsyncExample() *******" );
            String resourceName = "student-cohorts";
            int totalCount = ethosProxyClient.getTotalCount( resourceName );
            // Calculate the offset to be 95% of the totalCount to avoid paging through potentially tons of pages.
            int offset = (int)(totalCount * 0.95);
            ObjectMapper objectMapper = new ObjectMapper();

            CompletableFuture<List<String>> asyncResponse = ethosProxyClient.getAllPagesFromOffsetAsStringsAsync(resourceName, offset);

            // While the CompletableFuture thread is running, additional operations can be performed.  For the sake of
            // demonstrating this we are just running a few printlns to print out the current time.
            System.out.println(LocalDateTime.now());
            try {
                // Other processing could occur in an actual application, but this is sleeping here to simulate the time taken for other processing
                // until we are ready to handle the actual response from the ethosProxyClientAsync call.
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(LocalDateTime.now());

            // Using .join here so there is no exception thrown - all exceptions will be unchecked.  This is the
            // same as in C# where the default is to use the async processing without checked exceptions.
            List<String> stringList = asyncResponse.join();

            System.out.println(String.format("Get data for resource: %s", resourceName));
            System.out.println(String.format("Calculated offset of %s which is 95 percent of a total count of %s to avoid paging through potentially lots of pages.", offset, totalCount));
            System.out.println("To run with more paging, manually set the offset to a lower value, or reduce the percentage of the total count.");
            for( int i = 0; i < stringList.size(); i++ ) {
                // This reads the response body content JSON string into a Jackson library JsonNode, while enabling the use of
                // the EthosResponse containing helpful info like the requested URL below.  To return only the response bodies
                // for each page directly as JsonNodes, use ethosProxyClientAsync.getAllPagesFromOffsetAsJsonNodesAsync().
                JsonNode jsonNode = objectMapper.readTree( stringList.get(i) );
                System.out.println( String.format("PAGE %s: %s", (i+1), stringList.get(i)) );
                System.out.println( String.format("PAGE %s SIZE: %s", (i+1), jsonNode.size()) );
                System.out.println( String.format("OFFSET: %s", offset) );
            }
        } catch (CompletionException | IOException ioe) {
            ioe.printStackTrace();
        }
    }

    /**
     * This example shows how to get some number of rows returned using asynchronous processing.  The number of rows
     * returned are returned in terms of pages where each page contains a certain number of rows until all rows are returned,
     * but altogether the number of rows requested are the number returned.
     */
    public void doGetRowsFromOffsetAsyncExample() {
        EthosProxyClientAsync ethosProxyClient = getEthosProxyClientAsync();
        try {
            System.out.println( "******* doGetRowsFromOffsetExample() *******" );
            String resourceName = "student-cohorts";
            String version = "application/json";
            // This specifies the page size to use when getting the number of rows as a list of JsonNodes.  This is used internally
            // within the SDK when paging.
            int pageSize = 15;
            int offset = 10;
            int numRows = 24;
            // The List<JsonNode> returned by the getRowsFromOffsetAsJsonNodesAsync() method is row-based, not page-based,
            // meaning that the returned list contains all of the rows requested rather than pages of rows.
            CompletableFuture<List<JsonNode>> asyncResponse =
                    ethosProxyClient.getRowsFromOffsetAsJsonNodesAsync( resourceName, version, pageSize, offset, numRows );

            // While the CompletableFuture thread is running, additional operations can be performed.  For the sake of
            // demonstrating this we are just running a few printlns to print out the current time.
            System.out.println(LocalDateTime.now());
            try {
                // Other processing could occur in an actual application, but this is sleeping here to simulate the time taken for other processing
                // until we are ready to handle the actual response from the ethosProxyClientAsync call.
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(LocalDateTime.now());

            // Now go back and block on the CompletableFuture until it returns a result.
            List<JsonNode> jsonNodeList = asyncResponse.join();

            System.out.println(String.format("Get data for resource: %s", resourceName));
            System.out.println( String.format("OFFSET: %s", offset) );
            for( int i = 0; i < jsonNodeList.size(); i++ ) {
                JsonNode jsonNode = jsonNodeList.get( i );
                System.out.println( String.format("ROW %s: %s", (i+1), jsonNode.toString()) );
            }
            System.out.println( String.format("NUM ROWS: %s", jsonNodeList.size()) );
        } catch (CompletionException ce) {
            ce.printStackTrace();
        }
    }

    /**
     * This example shows how to get some number of rows returned using asynchronous processing.  The number of rows
     * returned are returned in terms of pages where each page contains a certain number of rows until all rows are returned,
     * but altogether the number of rows requested are the number returned.
     */
    public void doGetRowsFromOffsetAsJavaBeansAsyncExample() {
        EthosProxyClientAsync ethosProxyClient = getEthosProxyClientAsync();
        try {
            System.out.println( "******* doGetRowsFromOffsetAsJavaBeansAsyncExample() *******" );
            String resourceName = "student-cohorts";
            String version = "application/json";
            // This specifies the page size to use when getting the number of rows as a list of JsonNodes.  This is used internally
            // within the SDK when paging.
            int pageSize = 15;
            int offset = 10;
            int numRows = 24;
            System.out.println(String.format("Get data for resource: %s", resourceName));

            // The List<JsonNode> returned by the getRowsFromOffsetAsJsonNodesAsync() method is row-based, not page-based,
            // meaning that the returned list contains all of the rows requested rather than pages of rows.
            CompletableFuture<List<EthosResponse<List<StudentCohorts>>>> asyncResponse = ethosProxyClient.getRowsFromOffsetAsync( resourceName, version, pageSize, offset, numRows, StudentCohorts.class );

            // While the CompletableFuture thread is running, additional operations can be performed.  For the sake of
            // demonstrating this we are just running a few printlns to print out the current time.
            System.out.println(LocalDateTime.now());
            try {
                // Other processing could occur in an actual application, but this is sleeping here to simulate the time taken for other processing
                // until we are ready to handle the actual response from the ethosProxyClientAsync call.
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(LocalDateTime.now());

            // Now go back and block on the CompletableFuture until it returns a result.
            List<EthosResponse<List<StudentCohorts>>> ethosResponseList = asyncResponse.join();

            for( int i = 0; i < ethosResponseList.size(); i++ ) {
                EthosResponse<List<StudentCohorts>> ethosResponse = ethosResponseList.get( i );
                // Each ethosResponse in the ethosResponseList represents a page of data, and contains a list of StudentCohort JavaBeans.
                List<StudentCohorts> studentCohortsList = ethosResponse.getContentAsType();
                System.out.println( String.format("PAGE %s NUM ROWS: %s", (i+1), studentCohortsList.size()) );
                for( int j = 0; j < studentCohortsList.size(); j++ ) {
                    StudentCohorts studentCohorts = studentCohortsList.get( j );
                    // We could use the getter methods on the studentCohorts JavaBean, but just printing toString() here to see the data.
                    System.out.println( String.format("ROW %s: %s", (j+1), studentCohorts.toString()) );
                }
                System.out.println( String.format("OFFSET: %s", offset) );
                System.out.println( String.format("PAGE %s REQUESTED URL: %s ", (i+1), ethosResponse.getRequestedUrl()) );
            }
        } catch (CompletionException ce) {
            ce.printStackTrace();
        }
    }

}
