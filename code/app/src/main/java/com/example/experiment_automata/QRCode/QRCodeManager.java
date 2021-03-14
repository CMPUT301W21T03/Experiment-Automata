package com.example.experiment_automata.QRCode;


/**
 * Controls all interactions with QR codes and Bar codes
 * */
public class QRCodeManager {
    //Header for custom QR codes
    private final String AUTOMATA_QR_HEADER = "ATMA";


    /**
     * Unpacks a string from custom ExperimentAutomata QRCode content to string
     * @param code
     * packed ExperimentAutomata QRCode string
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
    /**
     * Packs a string into custom ExperimentAutomata QR code
     * @param content
     * string to pack
     * @return
     * returns the custom ExperimentAutomata QR code
     */
    //keep as method in case we want to have special fields in the future
    private String packQRString(String content){
        return AUTOMATA_QR_HEADER + content;
    }

}
