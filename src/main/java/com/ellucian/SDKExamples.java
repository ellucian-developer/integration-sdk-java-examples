/*
 * ******************************************************************************
 *   Copyright 2022 Ellucian Company L.P. and its affiliates.
 * ******************************************************************************
 */
package com.ellucian;


import com.ellucian.bpapiban.BPAPIBanExamples;
import com.ellucian.eedm.EEDMExamples;

public class SDKExamples {

    // ==========================================================================
    // Attributes
    // ==========================================================================

    // ==========================================================================
    // Methods
    // ==========================================================================
    public static void main( String[] args ) {
        System.out.println( "Runnning SDK Examples..." );
        BPAPIBanExamples bpapiBanExamples = new BPAPIBanExamples();
        EEDMExamples eedmExamples = new EEDMExamples();
        bpapiBanExamples.runExamples();
//        eedmExamples.runExamples();
    }
}
