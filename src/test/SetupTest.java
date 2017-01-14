package test;

import siso.smackdown.FrameType;
import skf.config.Configuration;
import skf.config.ConfigurationFactory;
import skf.core.SEEAbstractFederate;
import skf.core.SEEAbstractFederateAmbassador;

import java.io.File;
import java.util.Observable;
import java.util.Observer;
import java.util.Scanner;


/**
 * Created by Andrew on 1/1/2017.
 *
 * Test the project setup
 *
 */
public class SetupTest {

    public static void log(String message) {
        System.out.println(message);
    }

    public static class SetupTestFederate extends SEEAbstractFederate implements Observer {
        private String local_settings_designator = null;

        public SetupTestFederate(SEEAbstractFederateAmbassador federateAmbassador) {
            super(federateAmbassador);
        }

        public void update(Observable o, Object arg) {

        }

        protected void doAction() {
            log("This was an action");
        }

        public void configureAndStart(Configuration config) {

            try {
                // 1. configure the SKF framework
                super.configure(config);
                log("Configured");

                // 2. Connect on RTI
                local_settings_designator = "";//"crcHost="+config.getCrcHost()+"\ncrcPort="+config.getCrcPort();
                super.connectOnRTI(local_settings_designator);
                log("Connected to RTI");

                // 3. The Federate joins into the Federation execution
                super.joinIntoFederationExecution();
                log("Joined execution execution");

                // 4. Subscribe the Subject
                super.subscribeSubject(this);
                log("Subscribed to ourself");

                // 5. publish any objects and subscribe to coordinate frames as needed
                super.subscribeReferenceFrame(FrameType.MoonCentricFixed);
                log("Subscribed to targets");

                // 6. Execution-loop
                super.startExecution();
                log("Started execution");

                try {
                    System.out.println("Press any key to disconnect the execution from the federation execution");
                    new Scanner(System.in).next();

                    stopExecution();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } catch(Exception e) {
                System.out.println("Unhandled exception in configureAndStart: " + e);
                e.printStackTrace();
            }

        }

        private void stopExecution() {
            try {
                super.unsubscribeSubject(this);
                super.diconnectFromRTI();
            } catch(Exception e) {
                System.out.println("Unhandled exception in stopExecution: " + e);
                e.printStackTrace();
            }
        }
    }

    public static class SetupTestAmbassador extends SEEAbstractFederateAmbassador {
        public SetupTestAmbassador() {super();}
    }

    public static final File confFile = new File("conf\\SetupTestConf.json");

    public static void main(String[] args) {
        SetupTestAmbassador testAmbassador = new SetupTestAmbassador();
        SetupTestFederate testFederate = new SetupTestFederate(testAmbassador);

        try {
            testFederate.configureAndStart(new ConfigurationFactory().importConfiguration(confFile));
        } catch (Exception e) {
            System.out.println("Unhandled outer exception: " + e);
            e.printStackTrace();
        }
    }
}




