import java.io.File;
import java.io.FileNotFoundException;

import federate.TestAmbassador;
import federate.TestFederate;
import skf.config.Configuration;
import skf.config.ConfigurationFactory;


public class MainTest {

	private static final File configurationFile = new File("testResources/configuration/SetupTestConf.json");

	public static void main(String[] args) throws FileNotFoundException {

		TestAmbassador testfedamb = new TestAmbassador();
		TestFederate testfed = new TestFederate(testfedamb);

		ConfigurationFactory factory = null;
		Configuration config = null;

		try {

			factory = new ConfigurationFactory();
			config = factory.importConfiguration(configurationFile);
			testfed.configureAndStart(config);
			
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
