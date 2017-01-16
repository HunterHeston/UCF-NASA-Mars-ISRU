package test.entity;

import java.util.LinkedList;
import java.util.Queue;

import org.apache.log4j.Logger;
import org.junit.Test;

import entity.SRMREntityState;
import entity.SRMREntityState.SRMRState;
import environment.RegolithData;
import state.SimulationEntityState;
import state.SimulationEntityState.GridIndex;
import state.SimulationEntityState.MovementState;

/**
 * 
 * @author John-Paul Steed
 *
 */
public class SRMREntityStateTest {
	final static Logger logger = Logger.getLogger(SRMREntityStateTest.class);
	
	@Test
	public void stateTransition() {
		GridIndex ISRUIndex = new GridIndex(2, 2);
		double maxPayload = 100;
		double chargeCapacity = 100;
		double speed = 2;
		double gridSize = 1;
		
		// Initialize rover in Standby state.
		SRMREntityState rover = new SRMREntityState(0, 1, 1, ISRUIndex.row, ISRUIndex.col, chargeCapacity, speed, gridSize, maxPayload);
		assert rover.getUserState() == SRMRState.Standby;
		
		// Move to ISRU with full charge.
		rover.beginMoveToISRU();
		assert rover.getUserState() == SRMRState.MoveToISRU;
		assert rover.movementState == MovementState.PathFinding;
		
		// Movement steps
		Queue<SimulationEntityState.GridIndex> path = (Queue) new LinkedList<>();
		path.add(ISRUIndex);
		rover.beginTransit(path);
		rover.moveTowardsTarget();
		rover.ISRUArrivalResponse();
		assert rover.getUserState() == SRMRState.AtISRU;
		
		// Test charging rover when at ISRU.
		rover.capacity = 90;
		rover.chargeRover();
		assert rover.getUserState() == SRMRState.HandleCharge;
		
		// Charging steps
		rover.connectToISRU();
		rover.capacity = 100;
		rover.disconnectFromISRU();
		rover.handleChargeResponse();
		assert rover.getUserState() == SRMRState.AtISRU;
		
		// Load regolith.
		rover.beginLoadRegolith();
		assert rover.getUserState() == SRMRState.LoadRegolith;
		
		// Test if no regolith is loaded.
		rover.LoadRegolithResponse(new RegolithData(0, 0));
		assert rover.getUserState() == SRMRState.AtISRU;
		
		rover.beginLoadRegolith();
		assert rover.getUserState() == SRMRState.LoadRegolith;
		
		// Test if full regolith payload.
		rover.LoadRegolithResponse(new RegolithData(100, 0));
		assert rover.getUserState() == SRMRState.FindDumpSite;
		
		// Find dump site location.
		rover.selectDumpLocation();
		assert rover.getUserState() == SRMRState.WaitForDumpSite;
		
		// Move to dump site.
		GridIndex dumpSite = new GridIndex(3, 3);
		rover.selectDumpSiteResponse(dumpSite.row, dumpSite.col);
		assert rover.getUserState() == SRMRState.MoveToDumpSite;
		
		// Movement steps
		path = (Queue) new LinkedList<>();
		path.add(dumpSite);
		rover.beginTransit(path);
		rover.moveTowardsTarget();
		rover.arriveAtDumpSite();
		assert rover.getUserState() == SRMRState.AtDumpSite;
		
		// Dump regolith.
		rover.beginDumpRegolith();
		assert rover.getUserState() == SRMRState.DumpRegolith;
		
		// Test if dump site fills up before dumping entire payload.
		rover.dumpRegolithResponse(new RegolithData(50, 0));
		assert rover.getUserState() == SRMRState.FindDumpSite;
		
		// Find another location.
		rover.selectDumpLocation();
		assert rover.getUserState() == SRMRState.WaitForDumpSite;
		
		// Move to new dump site.
		dumpSite = new GridIndex(3, 2);
		rover.selectDumpSiteResponse(dumpSite.row, dumpSite.col);
		assert rover.getUserState() == SRMRState.MoveToDumpSite;
		
		// Movement steps.
		path = (Queue) new LinkedList<>();
		path.add(dumpSite);
		rover.beginTransit(path);
		rover.moveTowardsTarget();
		rover.arriveAtDumpSite();
		assert rover.getUserState() == SRMRState.AtDumpSite;
		
		// Dump regolith.
		rover.beginDumpRegolith();
		assert rover.getUserState() == SRMRState.DumpRegolith;
		
		// Test if entire payload is unloaded.
		rover.dumpRegolithResponse(new RegolithData(0, 0));
		assert rover.getUserState() == SRMRState.Standby;
		
		// Test if handle charge and begin movement to ISRU.
		rover.capacity = 50;
		rover.beginMoveToISRU();
		assert rover.getUserState() == SRMRState.HandleCharge;
		
	}
}
