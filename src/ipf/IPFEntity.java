package ipf;

import java.util.Queue;

import org.apache.log4j.Logger;

import siso.smackdown.utilities.Vector3;

public class IPFEntity {
	final static Logger logger = Logger.getLogger(IPFEntity.class);
	
	public enum RegolithState{
		NULL,
		WaitingForDeposit,
		Depositing,
		Processing,
		ProcessingAndWaitingForDeposit,
		WaitingForLoad,
		Loading
	}
	public enum ChargeState{
		NULL,
		OpenPorts,
		NoPorts
	}
	public enum ProcessingState{
		NULL,
		StorageFull,
		StorageNotFull,
		HaveResources,
		NeedResources,
		NeedHydrogen,
		HaveWater,
		NeedWater,
		HaveRegolith,
		NeedRegolith,
		WaitingForRegolith,
		NeedCarbonDioxide,
		NeedHydrogenAndCarbonDioxide
	}
	
	public Vector3 location;
	public int height;
	public int width;
	public int length;
	public Queue<Long> waitingEntityQueue;
	public Queue chargingEntityQueue;
	public Queue<Vector3> chargePortLocations;
	public int numChargePorts;
	public int numOpenChargePorts;
	public double chargePowerLoad;
	public double maxUnprocessedRegolithCapacity;
	public double maxProcessedRegolithCapacity;
	public double maxHydrogenCapacity;
	public double maxOxygenCapacity;
	public double maxWaterCapacity;
	public double maxMethaneCapacity;
	public double maxCarbonDioxideCapacity;
	public double maxPowerCapacity;
	public double amountWater;
	public double amountHydrogen;
	public double amountOxygen;
	public double amountMethane;
	public double amountCarbonDioxide;
	public double amountProcessedRegolith;
	public double amountUnprocessedRegolith;
	public double amountPower;
	public boolean powerConnection;
	
	public RegolithState regolithState = RegolithState.NULL;
	public ChargeState chargeState = ChargeState.NULL;
	public ProcessingState processingState = ProcessingState.NULL;

	public IPFEntity(){
		
	}

}
