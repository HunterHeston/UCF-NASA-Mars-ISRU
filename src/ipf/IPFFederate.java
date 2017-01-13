package ipf;

import java.net.MalformedURLException;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.Scanner;

import hla.rti1516e.exceptions.*;
import siso.smackdown.FrameType;
import siso.smackdown.utilities.Vector3;
import skf.config.Configuration;
import skf.core.SEEAbstractFederate;
import skf.core.SEEAbstractFederateAmbassador;
import skf.exception.PublishException;
import skf.exception.UpdateException;

public class IPFFederate extends SEEAbstractFederate implements Observer {

	private IPFEntity ipf = null;
	private String local_settings_designator = null;
	
	public IPFFederate(SEEAbstractFederateAmbassador seefedamb, IPFEntity ipf) {
		super(seefedamb);
		this.ipf = ipf;
		// TODO Auto-generated constructor stub
	}
	
	public void configureAndStart(Configuration config) throws ConnectionFailed, InvalidLocalSettingsDesignator, UnsupportedCallbackModel, CallNotAllowedFromWithinCallback, RTIinternalError, CouldNotCreateLogicalTimeFactory, FederationExecutionDoesNotExist, InconsistentFDD, ErrorReadingFDD, CouldNotOpenFDD, SaveInProgress, RestoreInProgress, NotConnected, MalformedURLException, FederateNotExecutionMember, AttributeNotDefined, ObjectClassNotDefined, NameNotFound, InvalidObjectClassHandle, InstantiationException, IllegalAccessException, IllegalName, ObjectInstanceNameInUse, ObjectInstanceNameNotReserved, ObjectClassNotPublished, AttributeNotOwned, ObjectInstanceNotKnown, PublishException, UpdateException{
		super.configure(config);
		
		local_settings_designator = "";
		super.connectOnRTI(local_settings_designator);
		super.joinIntoFederationExecution();
		super.subscribeSubject(this);
		super.publishElement(ipf);
		super.subscribeReferenceFrame(FrameType.MarsCentricFixed);
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
		// TODO Auto-generated method stub
		
		switch(ipf.chargeState){
		case NULL:
			break;
		case NoPorts:
			break;
		case OpenPorts:
			break;
		default:
			break;
		}
		
		switch(ipf.regolithState){
		case Depositing:
			break;
		case Loading:
			break;
		case NULL:
			break;
		case Processing:
			break;
		case ProcessingAndWaitingForDeposit:
			break;
		case WaitingForDeposit:
			break;
		case WaitingForLoad:
			break;
		default:
			break;
		}
		
		switch(ipf.processingState){
		case HaveRegolith:
			break;
		case HaveResources:
			break;
		case HaveWater:
			break;
		case NULL:
			break;
		case NeedCarbonDioxide:
			break;
		case NeedHydrogen:
			break;
		case NeedHydrogenAndCarbonDioxide:
			break;
		case NeedRegolith:
			break;
		case NeedResources:
			break;
		case NeedWater:
			break;
		case StorageFull:
			break;
		case StorageNotFull:
			break;
		case WaitingForRegolith:
			break;
		default:
			break;
		}
		
		try {
			super.updateElement(this.ipf);
		} catch (FederateNotExecutionMember | NotConnected | AttributeNotOwned | AttributeNotDefined
				| ObjectInstanceNotKnown | SaveInProgress | RestoreInProgress | RTIinternalError | IllegalName
				| ObjectInstanceNameInUse | ObjectInstanceNameNotReserved | ObjectClassNotPublished
				| ObjectClassNotDefined | UpdateException e) {
			e.printStackTrace();
		}

	}

	@Override
	public void update(Observable o, Object arg) {
		// TODO Auto-generated method stub
		System.out.println("Update test");
		System.out.println(arg);
	}

}
