package federate;

import hla.rti1516e.exceptions.AttributeNotDefined;
import hla.rti1516e.exceptions.AttributeNotOwned;
import hla.rti1516e.exceptions.CallNotAllowedFromWithinCallback;
import hla.rti1516e.exceptions.ConnectionFailed;
import hla.rti1516e.exceptions.CouldNotCreateLogicalTimeFactory;
import hla.rti1516e.exceptions.CouldNotOpenFDD;
import hla.rti1516e.exceptions.ErrorReadingFDD;
import hla.rti1516e.exceptions.FederateIsExecutionMember;
import hla.rti1516e.exceptions.FederateNotExecutionMember;
import hla.rti1516e.exceptions.FederateOwnsAttributes;
import hla.rti1516e.exceptions.FederationExecutionDoesNotExist;
import hla.rti1516e.exceptions.IllegalName;
import hla.rti1516e.exceptions.InconsistentFDD;
import hla.rti1516e.exceptions.InteractionClassNotDefined;
import hla.rti1516e.exceptions.InteractionClassNotPublished;
import hla.rti1516e.exceptions.InteractionParameterNotDefined;
import hla.rti1516e.exceptions.InvalidLocalSettingsDesignator;
import hla.rti1516e.exceptions.InvalidResignAction;
import hla.rti1516e.exceptions.NotConnected;
import hla.rti1516e.exceptions.ObjectClassNotDefined;
import hla.rti1516e.exceptions.ObjectClassNotPublished;
import hla.rti1516e.exceptions.ObjectInstanceNameInUse;
import hla.rti1516e.exceptions.ObjectInstanceNameNotReserved;
import hla.rti1516e.exceptions.ObjectInstanceNotKnown;
import hla.rti1516e.exceptions.OwnershipAcquisitionPending;
import hla.rti1516e.exceptions.RTIinternalError;
import hla.rti1516e.exceptions.RestoreInProgress;
import hla.rti1516e.exceptions.SaveInProgress;
import hla.rti1516e.exceptions.UnsupportedCallbackModel;

import java.net.MalformedURLException;
import java.util.LinkedList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.Scanner;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import model.Rover;
import model.Interaction;
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
		this.rf = (ReferenceFrame) arg;
		logger.info(rf);

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

			for(int i=0; i<listRover.size(); i++){
				tmp = listRover.get(i);
				// update the Identifier of the Rover
				tmp.setIdentifier(count);
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
