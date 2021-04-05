package com.example.experiment_automata.backend;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;


/**
 * Role/Pattern:
 *     Sets up firestore for emulator
 *
 *  Known Issue:
 *
 *      1. None
 */
public class FirestoreEmulatorSettings {
    static public final boolean USE_EMULATOR = true;// This value controls if the emulator runs
    static public final String HOST = "10.0.2.2";//This is the firebase emulator host ip address
    static public final int PORT = 8080;//this is the firebase emulator host port
    /**
     * Setup firestore to use Firebase CLI firestore emulator if enabled
     * @return
     *  If the emulator is in use
     */
    static public boolean setupFirestoreEmulator(){
        if (USE_EMULATOR) {
            // 10.0.2.2 is the special IP address to connect to the 'localhost' of
            // the host computer from an Android emulator.
            FirebaseFirestore firestore = FirebaseFirestore.getInstance();
            firestore.useEmulator(HOST, PORT);


            FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                    .setPersistenceEnabled(false)
                    .build();
            firestore.setFirestoreSettings(settings);
        }
        return USE_EMULATOR;
    }
}
