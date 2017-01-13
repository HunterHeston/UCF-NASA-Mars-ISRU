package entity;

import org.apache.log4j.Logger;

import environment.RegolithData;

public class SRMREntity extends SimulationEntity {
	
	final static Logger logger = Logger.getLogger(SRMREntity.class);
	
	public enum SRMRState {
		Standby,
		MoveToISRU,
		AtISRU,
		HandleCharge,
		LoadRegolith,
		FindDumpSite,
		WaitForDumpSite,
		SetDumpSite,
		MoveToDumpSite,
		AtDumpSite,
		DumpRegolith
	}
	
	public final static int EMPTY = 0;
	
	private int ISRU_X;
	private int ISRU_Y;
	private double MAX_CAPACITY;
	private double MAX_CHARGE;
	
	private RegolithData payload;
	private int dumpSiteLocationX;
	private int dumpSiteLocationY;
	
	private SRMRState userState = SRMRState.Standby;

	public SRMREntity(int gridX, int gridY) {
		super(gridX, gridY);
		// TODO Auto-generated constructor stub
	}

	public void setUserState(SRMRState userState) {
		this.userState = userState;
	}
	
	public void beginMovementToISRU(int X, int Y){
		assert this.userState == SRMRState.Standby;
		this.beginPathFinding(X, Y);
	}
	
	

}
