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
	
	private double charge;
	
	private SRMRState userState = SRMRState.Standby;

	public SRMREntity(int gridX, int gridY) {
		super(gridX, gridY);
		// TODO Auto-generated constructor stub
	}

	public void setUserState(SRMRState userState) {
		this.userState = userState;
	}
	
	public void beginMoveToISRU(){
		assert userState == SRMRState.Standby;
		// begin movement
		
		setUserState(SRMRState.MoveToISRU);
		logger.debug("SRMR State transition: Standby -> MoveToISRU");
	}
	
	public void ISRUArrivalResponse(){
		assert userState == SRMRState.MoveToISRU;
		assert this.movementState == MovementState.Stopped;
		assert this.gridIndex.equals(new GridIndex(ISRU_X+1, ISRU_Y)) ||
				this.gridIndex.equals(new GridIndex(ISRU_X-1, ISRU_Y)) ||
				this.gridIndex.equals(new GridIndex(ISRU_X, ISRU_Y+1)) ||
				this.gridIndex.equals(new GridIndex(ISRU_X, ISRU_Y-1));
		
		setUserState(SRMRState.AtISRU);
		logger.debug("SRMR State transition: MoveToISRU -> AtISRU");
	}
	
	public void handleCharge(){
		assert userState == SRMRState.AtISRU;
		assert charge != MAX_CHARGE;
		
		setUserState(SRMRState.HandleCharge);
		logger.debug("SRMR State transition: AtISRU -> HandleCharge");
	}
	
	public void handleChargeResponse(){
		assert userState == SRMRState.HandleCharge;
		assert charge == MAX_CHARGE;
		
		setUserState(SRMRState.AtISRU);
		logger.debug("SRMR State transition: HandleCharge -> AtISRU");
	}
	
	public void beginLoadRegolith(){
		assert userState == SRMRState.AtISRU;
		assert payload.quantity < MAX_CAPACITY;
		
		setUserState(SRMRState.LoadRegolith);
		logger.debug("SRMR State transition: AtISRU -> LoadRegolith");
	}
	
	public void LoadRegolithResponse(RegolithData payload){
		assert userState == SRMRState.LoadRegolith;
		assert payload.quantity >= EMPTY && payload.quantity <= MAX_CAPACITY;
		
		this.payload = payload;
		if(payload.quantity == EMPTY){
			setUserState(SRMRState.AtISRU);
			logger.debug("SRMR State transition: LoadRegolith -> AtISRU");
		}else{
			setUserState(SRMRState.FindDumpSite);
			logger.debug("SRMR State transition: LoadRegolith -> FindDumpSite");
		}
	}
	
	public void selectDumpLocation(){
		assert userState == SRMRState.FindDumpSite;
		
		setUserState(SRMRState.WaitForDumpSite);
		logger.debug("SRMR State transition: FindDumpSite -> WaitForDumpSite");
	}
	
	public void selectDumpSiteResponse(int locationX, int locationY){
		assert userState == SRMRState.WaitForDumpSite;
		
		dumpSiteLocationX = locationX;
		dumpSiteLocationY = locationY;
		setUserState(SRMRState.MoveToDumpSite);
		logger.debug("SRMR State transition: WaitForDumpSite -> MoveToDumpSite");
	}
	
	public void arriveAtDumpSite(){
		assert userState == SRMRState.MoveToDumpSite;
		assert this.movementState == MovementState.Stopped;
		assert this.gridIndex.row == dumpSiteLocationX &&
				this.gridIndex.col == dumpSiteLocationY;
		
		setUserState(SRMRState.AtDumpSite);
		logger.debug("SRMR State transition: MoveToDumpSite -> AtDumpSite");
	}
	
	public void beginDumpRegolith(){
		assert userState == SRMRState.AtDumpSite;
		assert payload.quantity > EMPTY;
		
		setUserState(SRMRState.DumpRegolith);
		logger.debug("SRMR State transition: AtDumpSite -> DumpRegolith");
	}
	
	public void dumpRegolithResponse(RegolithData payload){
		assert userState == SRMRState.DumpRegolith;
		assert payload.quantity >= EMPTY && payload.quantity <= MAX_CAPACITY;
		
		this.payload = payload;
		if(payload.quantity == EMPTY){
			setUserState(SRMRState.Standby);
			logger.debug("SRMR State transition: DumpRegolith -> Standby");
		}else{
			setUserState(SRMRState.FindDumpSite);
			logger.debug("SRMR State transition: DumpRegolith -> FindDumpSite");
		}
	}

}
