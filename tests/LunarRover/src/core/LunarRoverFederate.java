package core;

import hla.rti1516e.exceptions.*;
import model.LunarRover;
import model.Position;
import siso.smackdown.FrameType;
import skf.config.Configuration;
import skf.core.SEEAbstractFederate;
import skf.core.SEEAbstractFederateAmbassador;
import skf.exception.PublishException;
import skf.exception.UpdateException;

import java.net.MalformedURLException;
import java.util.Observable;
import java.util.Observer;
import java.util.Scanner;

public class LunarRoverFederate extends SEEAbstractFederate implements Observer {

	private LunarRover lunarRover = null;
	private String local_settings_designator = null;

	public LunarRoverFederate(SEEAbstractFederateAmbassador seefedamb, LunarRover lunarRover) {
		super(seefedamb);
		this.lunarRover  = lunarRover;
	}

	public void configureAndStart(Configuration config) throws ConnectionFailed, InvalidLocalSettingsDesignator, UnsupportedCallbackModel, CallNotAllowedFromWithinCallback, RTIinternalError, CouldNotCreateLogicalTimeFactory, FederationExecutionDoesNotExist, InconsistentFDD, ErrorReadingFDD, CouldNotOpenFDD, SaveInProgress, RestoreInProgress, NotConnected, MalformedURLException, FederateNotExecutionMember, NameNotFound, InvalidObjectClassHandle, AttributeNotDefined, ObjectClassNotDefined, InstantiationException, IllegalAccessException, IllegalName, ObjectInstanceNameInUse, ObjectInstanceNameNotReserved, ObjectClassNotPublished, AttributeNotOwned, ObjectInstanceNotKnown, PublishException, UpdateException {
		// 1. configure the SKF framework
		super.configure(config);

		// 2. Connect on RTI
    /*
    *For MAK local_settings_designator = "";
    *For PITCH local_settings_designator = "crcHost=" + <crc_host> + "\ncrcPort=" + <crc_port>;
    */
    local_settings_designator = "";//"crcHost="+config.getCrcHost()+"\ncrcPort="+config.getCrcPort();
		super.connectOnRTI(local_settings_designator);

		// 3. The Federate joins into the Federation execution
		super.joinIntoFederationExecution();

		// 4. Subscribe the Subject
		super.subscribeSubject(this);

		// 5. publish our lunarRover object on RTI
		super.publishElement(lunarRover);
		super.subscribeReferenceFrame(FrameType.MoonCentricFixed);

		// 6. Execution-loop
		super.startExecution();

		try {
			System.out.println("Press any key to disconnect the federate from the federation execution");
			new Scanner(System.in).next();
			stopExecution();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void stopExecution() throws InvalidResignAction, OwnershipAcquisitionPending, FederateOwnsAttributes, FederateNotExecutionMember, NotConnected, RTIinternalError, FederateIsExecutionMember, CallNotAllowedFromWithinCallback, SaveInProgress, RestoreInProgress {
		super.unsubscribeSubject(this);
		super.diconnectFromRTI();
	}

	@Override
	protected void doAction() {
		Position curr_pos = this.lunarRover.getPosition();
		curr_pos.setX(curr_pos.getX()+10); // update the x coordinate
		
		try {
			super.updateElement(this.lunarRover);
			
		} catch (FederateNotExecutionMember | NotConnected | AttributeNotOwned
				| AttributeNotDefined | ObjectInstanceNotKnown | SaveInProgress
				| RestoreInProgress | RTIinternalError | IllegalName
				| ObjectInstanceNameInUse | ObjectInstanceNameNotReserved
				| ObjectClassNotPublished | ObjectClassNotDefined
				| UpdateException e) {
			e.printStackTrace();
		}

	}

	@Override
	public void update(Observable arg0, Object arg1) {
		
		System.out.println("The lunarRover has received an update");
		System.out.println(arg1.getClass());
		System.out.println(arg1);
		System.out.println(this.lunarRover.getPosition());

	}

}
