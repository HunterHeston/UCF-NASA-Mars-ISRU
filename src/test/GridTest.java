package test;

/**
 * Created by Andrew on 1/1/2017.
 *
 * Test the project setup
 *
 */
public class GridTest {

//    public static void log(String message) {
//        System.out.println(message);
//    }
//
//    public static class GridTestExecution extends EnvironmentGridExecution {
//        private String local_settings_designator = null;
//
//        public GridTestExecution(SEEAbstractFederateAmbassador federateAmbassador) {
//            super(federateAmbassador);
//        }
//
//        public void configureAndStart(Configuration config) {
//
//            try {
//                // 1. configure the SKF framework
//                super.configure(config);
//                log("Configured");
//
//                // 2. Connect on RTI
//                local_settings_designator = "crcHost="+config.getCrcHost()+"\ncrcPort="+config.getCrcPort();
//                super.connectOnRTI(local_settings_designator);
//                log("Connected to RTI");
//
//                // 3. The Federate joins into the Federation execution
//                super.joinIntoFederationExecution();
//                log("Joined federate execution");
//
//                // 4. Subscribe the Subject
//                super.subscribeSubject(this);
//                log("Subscribed to ourself");
//
//                // 5. publish any objects and subscribe to coordinate frames as needed
//                super.subscribeReferenceFrame(FrameType.MoonCentricFixed);
//                log("Subscribed to targets");
//
//                // 6. Execution-loop
//                super.startExecution();
//                log("Started execution");
//
//                try {
//                    this.doPlaceEntity(1, 4, 4, 2);
//
//                    System.out.println("Press any key to disconnect the federate from the federation execution");
//                    new Scanner(System.in).next();
//
//                    stopExecution();
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            } catch(Exception e) {
//                System.out.println("Unhandled exception in configureAndStart: " + e);
//                e.printStackTrace();
//            }
//
//        }
//
//        private void stopExecution() {
//            try {
//                super.unsubscribeSubject(this);
//                super.diconnectFromRTI();
//            } catch(Exception e) {
//                System.out.println("Unhandled exception in stopExecution: " + e);
//                e.printStackTrace();
//            }
//        }
//    }
//
//    public static class GridTestAmbassador extends SEEAbstractFederateAmbassador {
//        public GridTestAmbassador() {super();}
//    }
//
//    public static final File confFile = new File("conf\\GridTestConf.json");
//
//    public static void main(String[] args) {
//        GridTestAmbassador testAmbassador = new GridTestAmbassador();
//        GridTestExecution testFederate = new GridTestExecution(testAmbassador);
//
//        try {
//            testFederate.configureAndStart(new ConfigurationFactory().importConfiguration(confFile));
//        } catch (Exception e) {
//            System.out.println("Unhandled outer exception: " + e);
//            e.printStackTrace();
//        }
//    }
}




