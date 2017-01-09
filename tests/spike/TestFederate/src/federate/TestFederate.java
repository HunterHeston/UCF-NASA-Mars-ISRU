package federate;

import hla.rti1516e.exceptions.*;
import model.Interaction;
import model.Rover;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import siso.smackdown.FrameType;
import siso.smackdown.ReferenceFrame;
import skf.config.Configuration;
import skf.core.SEEAbstractFederate;
import skf.core.SEEAbstractFederateAmbassador;
import skf.exception.UnsubscribeException;
import skf.exception.UpdateException;
import skf.utility.JulianDateType;
import skf.utility.TimeUnit;
import skf.utility.TimeUtility;

import java.net.MalformedURLException;
import java.util.*;


public class TestFederate extends SEEAbstractFederate implements Observer {
	
	private Logger logger = LogManager.getLogger(TestFederate.class);

	/*
	 * For MAK local_settings_designator = "";
	 * For PITCH local_settings_designator = "crcHost=" + <crc_host> + "\ncrcPort=" + <crc_port>;
	 */
	private static final String local_settings_designator = "";
	
	private List<Rover> listRover = new LinkedList<Rover>();
	private ReferenceFrame rf = null;
	private Interaction interaction = null;

	public TestFederate(SEEAbstractFederateAmbassador seefedamb) {
		super(seefedamb);
	}

	public void configureAndStart(Configuration config) throws ConnectionFailed, InvalidLocalSettingsDesignator, UnsupportedCallbackModel, 
														CallNotAllowedFromWithinCallback, RTIinternalError, CouldNotCreateLogicalTimeFactory, 
														FederationExecutionDoesNotExist, InconsistentFDD, ErrorReadingFDD, CouldNotOpenFDD, 
														SaveInProgress, RestoreInProgress, NotConnected, MalformedURLException, FederateNotExecutionMember {
		
		// 1. configure the SKF framework
		super.configure(config);

		// 2. Connect on RTI
		super.connectOnRTI(local_settings_designator);
		
		// 3. The Federate joins into the Federation execution
		super.joinIntoFederationExecution();

		try {
			// 4. Publish/Subscribe
			super.subscribeSubject(this);
			
			// 5. Publish/Subscribe objects and/or interaction
			super.subscribeReferenceFrame(FrameType.MarsCentricInertial);
			super.subscribeReferenceFrame(FrameType.EarthCentricFixed);

			listRover.add(new Rover("lunarRover1","lunarRover1"));
			listRover.add(new Rover("lunarRover2","lunarRover2"));

			for(Rover r : listRover)
				super.publishElement(r, r.getName());
			
			interaction = new Interaction("interaction", "payload");
			super.publishInteraction(interaction);
			super.subscribeInteraction(Interaction.class);

		} catch (Exception e) {
			e.printStackTrace();
		}
		
		// 6. Execution-loop
		super.startExecution();
		
		try {
			System.out.println("Press any key to disconnect the federate from the federation execution");
			new Scanner(System.in).next();
			stop();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void update(Observable o, Object arg) {
		//	Receive interactions here!!!
		//	All you have to do is check the class of arg and act appropriately!

		if(arg instanceof ReferenceFrame) {
			this.rf = (ReferenceFrame) arg;
			logger.info(rf);
		} else if(arg instanceof Interaction) {
			logger.info("Got an interaction!");
			logger.info(arg);
		} else {
			logger.info("Update of unknown type " + arg.getClass());
			logger.info(arg);
		}

	}

	private int count = 1;

	@Override
	protected void doAction() {
		logger.info("federation execution time cycle: "+TimeUtility.convert(super.getTime().getFederationExecutionTimeCycle(), TimeUnit.MICROSECONDS, TimeUnit.SECONDS));
		logger.info("federate time cycle: "+TimeUtility.convert(super.getTime().getFederateExecutionTimeCycle(), TimeUnit.MICROSECONDS, TimeUnit.SECONDS));
		
		logger.info("federate time date: "+super.getTime().getFederationExecutionTime());
		
		logger.info("federation execution jd: "+super.getTime().getFederationExecutionTimeInJulianDate(JulianDateType.DATE));
		logger.info("federation execution mjd: "+super.getTime().getFederationExecutionTimeInJulianDate(JulianDateType.MODIFIED));
		logger.info("federation execution rjd: "+super.getTime().getFederationExecutionTimeInJulianDate(JulianDateType.REDUCED));
		logger.info("federation execution tjd: "+super.getTime().getFederationExecutionTimeInJulianDate(JulianDateType.TRUNCATED));
		
		try {

			Rover tmp = null;

			for(int i=0; i<listRover.size(); i++) {
				tmp = listRover.get(i);
				// update the Identifier of the Rover
				tmp.setIdentifier(count);

				//	update the position (custom)
				PositionVector old = tmp.getPosition();
				PositionVector nev = new PositionVector(old.x+1, old.y+1, old.z+0.5);

				tmp.setPosition(nev);

				// update on RTI the rover
				super.updateElement(tmp);
				count++;
			}

			//update the 'payload' of the RoverInteraction
			interaction.setPayload("payload"+Math.random());
			super.updateInteraction(interaction);


		} catch (FederateNotExecutionMember | NotConnected
				| AttributeNotOwned | AttributeNotDefined
				| ObjectInstanceNotKnown | SaveInProgress
				| RestoreInProgress | RTIinternalError | UpdateException e) {
			e.printStackTrace();
		} catch (InteractionClassNotPublished | InteractionParameterNotDefined |
				InteractionClassNotDefined | ObjectInstanceNameInUse | ObjectInstanceNameNotReserved |
				IllegalName | ObjectClassNotPublished | ObjectClassNotDefined e) {
			e.printStackTrace();
		}

	}

	public void stop() throws InvalidResignAction, OwnershipAcquisitionPending, FederateOwnsAttributes, FederateNotExecutionMember,
							NotConnected, RTIinternalError, FederateIsExecutionMember, CallNotAllowedFromWithinCallback, 
							SaveInProgress, RestoreInProgress, ObjectClassNotDefined, UnsubscribeException, InteractionClassNotDefined {
		
		// Unsubcribe from the Subject
		super.unsubscribeSubject(this);
		
		// Disconnect from the HLA/RTI platform
		super.diconnectFromRTI();
	}

}
