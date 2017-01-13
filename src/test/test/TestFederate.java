package test.test;

import org.apache.log4j.Logger;
import skf.core.SEEAbstractFederate;
import skf.core.SEEAbstractFederateAmbassador;
import sun.misc.Signal;
import sun.misc.SignalHandler;

import java.util.Observable;
import java.util.Observer;

/**
 * Created by Andrew on 1/10/2017.
 */
public class TestFederate extends SEEAbstractFederate implements Observer {
    final static Logger logger = Logger.getLogger(TestFederate.class);

    public TestFederate(SEEAbstractFederateAmbassador seefedamb) {
        super(seefedamb);
    }

    public static class TestAmbassador extends SEEAbstractFederateAmbassador {
        public TestAmbassador() {super();}
    }

    @Override
    public void update(Observable o, Object arg) {

    }

    @Override
    protected void doAction() {

    }

    public static void runFederate() {

        try {

        } catch (Exception e) {
            logger.debug("Unhandled outer exception: " + e);
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {
        int status = 0;

        // Print out a start up message.
        System.out.println();
        System.out.println();
        System.out.println("*** SEE Smackdown Environment Federate ***");
        System.out.println();

        // Get the preferences node for this application class.
        Environment.process_preferences();

        // Parse the command line options; these will override preferences.
        Environment.process_command_line_options(args);

        // Instantiate an environment (epoch dates are in UTC).
        final Environment env = new Environment(mjd_epoch);

        // NOTE: Need to add signal handling for Ctrl-C shutdown.
        // For now, we'll just use a ShutdownHook routine.
		/*
		 * Runtime.getRuntime().addShutdownHook(new Thread() {
		 *
		 * @Override public void run() { System.out.println(
		 * "Cleaning up with ShutdownHook routine!"); env.continue_execution =
		 * false; env.shutdown(); } });
		 */
        // Register an signal handler for Ctrl-C.
        SignalHandler handler = new SignalHandler() {
            public void handle(Signal sig) {
                System.out.println("In signal handler.");
                env.continue_execution = false;
            }
        };
        Signal.handle(new Signal("INT"), handler);

        try {
            // Configure the simulation.
            env.configure();

            // Initialize the simulation.
            if ((status = env.initialize()) == 0) {

                // Print out connection information if HLA is enabled.
                if (enable_hla) {
                    System.out.println("************************************************");
                    System.out.println("CRC host: " + crc_host);
                    System.out.println("CRC port: " + crc_port);
                }

                // Print out initial frame values.
                if (Environment.verbose_output) {
                    System.out.print("   Federate Physical Time TT  (TJD): ");
                    System.out.println(env.sim_time.mjd_tt() - MJD_TJD_OFFSET);
                    env.print_frames();
                }

                // If we're at the run loop then go ahead and save the
                // preferences.
                store_preferences();

                // Execute the run loop.
                env.run_loop();
            }

            // Execution is finished so shutdown.
            env.shutdown();

        } catch (Exception e) {
            // Bad Scoobees so try to see what happened.
            e.printStackTrace();
        } finally {
            System.exit(status);
        }
    }
}
