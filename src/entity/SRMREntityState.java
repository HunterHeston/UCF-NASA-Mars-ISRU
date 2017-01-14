package entity;

import org.apache.log4j.Logger;

import environment.RegolithData;
import state.SimpleChargeableEntityState;

public class SRMREntityState extends SimpleChargeableEntityState {

	final static Logger logger = Logger.getLogger(SRMREntityState.class);
	
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
	
	private double maxPayload;
	private RegolithData payload;
	
	private GridIndex dumpSiteIndex;
	
	private SRMRState userState = SRMRState.Standby;
	
	public SRMREntityState(long identifier, int gridX, int gridY, 
							int isruGridX, int isruGridY, double capacity,
							double movementSpeed, double gridCellSize,
							double maxPayload) {
		
		super(identifier, gridX, gridY, isruGridX, isruGridY, capacity, movementSpeed, gridCellSize);
		this.maxPayload = maxPayload;
	}

	public void setUserState(SRMRState userState) {
		this.userState = userState;
	}
	
	public void beginMoveToISRU(){
		assert userState == SRMRState.Standby;
		
		if(this.needsCharge()){
			this.handleCharge();
			setUserState(SRMRState.HandleCharge);
			logger.debug("SRMR State transition: Standby -> HandleCharge");
		}else{
			this.beginPathFinding(this.isruIndex.row, this.isruIndex.col);
			setUserState(SRMRState.MoveToISRU);
			logger.debug("SRMR State transition: Standby -> MoveToISRU");
		}
		
	}
	
	public void ISRUArrivalResponse(){
		assert userState == SRMRState.MoveToISRU;
		assert this.movementState == MovementState.Stopped;
		assert adjacentToISRU(this.gridIndex, this.isruIndex);
		
		setUserState(SRMRState.AtISRU);
		logger.debug("SRMR State transition: MoveToISRU -> AtISRU");
	}
	
	public void chargeRover(){
		assert userState == SRMRState.AtISRU;
		assert this.chargeState == ChargeState.Null;
		
		setUserState(SRMRState.HandleCharge);
		logger.debug("SRMR State transition: AtISRU -> HandleCharge");
		
		this.chargeState = ChargeState.TransitToISRU;
		this.attemptConnectToISRU();
	}
	
	public void handleChargeResponse(){
		assert userState == SRMRState.HandleCharge;
		assert this.chargeState == ChargeState.Null;
		
		setUserState(SRMRState.AtISRU);
		logger.debug("SRMR State transition: HandleCharge -> AtISRU");
	}
	
	public void beginLoadRegolith(){
		assert userState == SRMRState.AtISRU;
		assert payload.quantity >= EMPTY && payload.quantity <= maxPayload;
		
		if(payload.quantity < maxPayload){
			setUserState(SRMRState.LoadRegolith);
			logger.debug("SRMR State transition: AtISRU -> LoadRegolith");
		}else{
			setUserState(SRMRState.FindDumpSite);
			logger.debug("SRMR State transition: AtISRU -> FindDumpSite");
		}
		
	}
	
	public void LoadRegolithResponse(RegolithData payload){
		assert userState == SRMRState.LoadRegolith;
		assert payload.quantity >= EMPTY && payload.quantity <= maxPayload;
		
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
		
		dumpSiteIndex.row = locationX;
		dumpSiteIndex.col = locationY;
		this.beginPathFinding(dumpSiteIndex.row, dumpSiteIndex.col);
		setUserState(SRMRState.MoveToDumpSite);
		logger.debug("SRMR State transition: WaitForDumpSite -> MoveToDumpSite");
	}
	
	public void arriveAtDumpSite(){
		assert userState == SRMRState.MoveToDumpSite;
		assert this.movementState == MovementState.Stopped;
		assert this.gridIndex.row == dumpSiteIndex.row &&
				this.gridIndex.col == dumpSiteIndex.col;
		
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
		assert payload.quantity >= EMPTY && payload.quantity <= maxPayload;
		
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
