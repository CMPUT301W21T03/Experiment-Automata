package com.example.experiment_automata.QRCode;


/**
 * Controls all interactions with QR codes and Bar codes
 * */
public class QRCodeManager {
    //Header for custom QR codes
    private final String AUTOMATA_QR_HEADER = "ATMA";


    /**
     * Unpacks a string from QR code into UUID
     * @param code
     * packed QR string
     * @return
     * returns the content of the packed QR code String if it's valid, returns an empty string if it's not a valid Experiment Automata QR code
     */
    //keep ability for extra content code at substring[4] for future use
    private String unpackQRString(String code){
        //check header
        if(!code.substring(0,4).equals(AUTOMATA_QR_HEADER)){
            //not an ExperimentAutomata QR code
            return "";//change to raise exception?
        }
        else{
            return code.substring(4);
        }
    }

}
