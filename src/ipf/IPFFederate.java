package ipf;

import java.net.MalformedURLException;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.Scanner;

import hla.rti1516e.exceptions.*;
import ipf.IPFEntity.ProcessingState;
import ipf.IPFEntity.RegolithState;
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
		
		switch(ipf.regolithState){
		case NULL:
			if(ipf.processingState == ProcessingState.WaitingForRegolith){
				ipf.changeRegolithState(RegolithState.WaitingForDeposit);
			}
			break;
		case Depositing:
			ipf.setRegolithProcessing(false);
			if(ipf.amountUnprocessedRegolith == 0.0){
				//Send DepositRegolithResponse
				ipf.changeRegolithState(RegolithState.WaitingForDeposit);
			} else if (ipf.amountUnprocessedRegolith > 0.0){
				//Send LoadRegolithResponse
				ipf.changeRegolithState(RegolithState.Processing);
			}
			break;
		case Processing:
			ipf.setRegolithProcessing(true);
			if(ipf.amountUnprocessedRegolith > 0.0 &&
					ipf.amountUnprocessedRegolith < ipf.maxUnprocessedRegolithCapacity){
				// Process Regolith
				ipf.changeRegolithState(RegolithState.ProcessingAndWaitingForDeposit);
			} else if (ipf.amountProcessedRegolith == ipf.maxProcessedRegolithCapacity ||
					ipf.amountUnprocessedRegolith == 0.0){
				ipf.changeRegolithState(RegolithState.WaitingForLoad);
			} else {
				// Process Regolith
			}
			break;
		case WaitingForLoad:
			ipf.setRegolithProcessing(false);
			break;
		case ProcessingAndWaitingForDeposit:
			if (ipf.amountProcessedRegolith == ipf.maxProcessedRegolithCapacity || 
					ipf.amountUnprocessedRegolith == 0.0){
				ipf.changeRegolithState(RegolithState.WaitingForLoad);
			}
			break;
		case Loading:
			//Send LoadRegolithResponse
			ipf.changeRegolithState(RegolithState.NULL);
			ipf.changeProcessingState(ProcessingState.NULL);
			break;
		default:
			break;
		}
		
		switch(ipf.processingState){
		case NULL:
			if(ipf.storageIsFull()){
				ipf.changeProcessingState(ProcessingState.StorageFull);
			} else {
				ipf.changeProcessingState(ProcessingState.StorageNotFull);
			}
			break;
		case StorageFull:
			// This may be able to be removed if nothing needs to be done here
			ipf.setSabatierProcessing(false);
			ipf.setAtmosphereProcessing(false);
			ipf.setElectrolysisProcessing(false);
			break;
		case StorageNotFull:
			if(ipf.resourcesAvailable()){
				ipf.changeProcessingState(ProcessingState.HaveResources);
			} else {
				ipf.changeProcessingState(ProcessingState.NeedResources);
			}
			break;
		case NeedResources:
			ipf.setSabatierProcessing(false);
			if(ipf.hasEnoughHydrogen()){
				ipf.changeProcessingState(ProcessingState.NeedCarbonDioxide);
			} else if(ipf.hasEnoughCarbonDioxide()){
				ipf.changeProcessingState(ProcessingState.NeedHydrogen);
			} else {
				ipf.changeProcessingState(ProcessingState.NeedHydrogenAndCarbonDioxide);
			}
			break;
		case HaveResources:
			ipf.setSabatierProcessing(true);
			ipf.changeProcessingState(ProcessingState.NULL);
			break;
		case NeedCarbonDioxide:
			if(ipf.hasEnoughCarbonDioxide()){
				ipf.changeProcessingState(ProcessingState.StorageNotFull);
			} else {
				ipf.setAtmosphereProcessing(true);
			}
			break;
		case NeedHydrogenAndCarbonDioxide:
			if(ipf.hasEnoughCarbonDioxide()){
				ipf.changeProcessingState(ProcessingState.NeedHydrogen);
			} else {
				ipf.setAtmosphereProcessing(true);
			}
			break;
		case NeedHydrogen:
			if(ipf.hasEnoughWater()){
				ipf.changeProcessingState(ProcessingState.HaveWater);
			} else {
				ipf.changeProcessingState(ProcessingState.NeedWater);
			}
			break;
		case HaveWater:
			ipf.setElectrolysisProcessing(true);
			ipf.changeProcessingState(ProcessingState.StorageNotFull);
			break;
		case NeedWater:
			ipf.setElectrolysisProcessing(false);
			if(ipf.hasEnoughRegolith()){
				ipf.changeProcessingState(ProcessingState.HaveRegolith);
			} else {
				ipf.changeProcessingState(ProcessingState.NeedRegolith);
			}
			break;
		case HaveRegolith:
			//Regolith extraction handled by regolith states
			ipf.changeProcessingState(ProcessingState.NeedHydrogen);
			break;
		case NeedRegolith:
			ipf.changeProcessingState(ProcessingState.WaitingForRegolith);
		default:
			break;
		}
		
		if(ipf.atmosphereProcessing){
			ipf.processAtmosphere();
		}
		
		if(ipf.electrolysisProcessing){
			ipf.electrolysis();
		}
		
		if(ipf.sabatierProcessing){
			ipf.sabatier();
		}
		
		if(ipf.regolithProcessing){
			ipf.processRegolith();
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
		System.out.println("Update test");
		System.out.println(arg);
	}

}
