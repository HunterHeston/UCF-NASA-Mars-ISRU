import core.LunarRoverFederate;
import core.LunarRoverFederateAmbassador;
import hla.rti1516e.exceptions.*;
import model.LunarRover;
import model.Position;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import siso.smackdown.FrameType;
import skf.config.ConfigurationFactory;
import skf.exception.PublishException;
import skf.exception.UpdateException;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;


public class Main {
	
	private static final File confFile = new File("config/SetupTestConf.json");

	public static void main(String[] args) throws JsonParseException, JsonMappingException, ConnectionFailed, InvalidLocalSettingsDesignator, UnsupportedCallbackModel, CallNotAllowedFromWithinCallback, RTIinternalError, CouldNotCreateLogicalTimeFactory, FederationExecutionDoesNotExist, InconsistentFDD, ErrorReadingFDD, CouldNotOpenFDD, SaveInProgress, RestoreInProgress, NotConnected, MalformedURLException, FederateNotExecutionMember, NameNotFound, InvalidObjectClassHandle, AttributeNotDefined, ObjectClassNotDefined, InstantiationException, IllegalAccessException, IllegalName, ObjectInstanceNameInUse, ObjectInstanceNameNotReserved, ObjectClassNotPublished, AttributeNotOwned, ObjectInstanceNotKnown, PublishException, UpdateException, IOException {
		
		LunarRover rover = new LunarRover("LunarRover", FrameType.MoonCentricFixed.toString(),
											"Rover", new Position(100, 500, 800));
		
		LunarRoverFederateAmbassador ambassador = new LunarRoverFederateAmbassador();
		LunarRoverFederate federate = new LunarRoverFederate(ambassador, rover);
		
		federate.configureAndStart(new ConfigurationFactory().importConfiguration(confFile));
		

	}

}
