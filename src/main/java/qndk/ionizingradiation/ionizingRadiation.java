package qndk.ionizingradiation;

// Fabric Imports
import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// IonizingRadiation Imports
import qndk.ionizingradiation.radiationSystem.radiationRegistry;
import qndk.ionizingradiation.radiationSystem.radiationTicker;
import qndk.ionizingradiation.radiationSystem.radiationCommands;
import qndk.ionizingradiation.radiationItems.radiationItems;

public class ionizingRadiation implements ModInitializer {
	public static final String MOD_ID = "ionizingradiation";


	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {

		LOGGER.info("Чем гуще в лес, тем... if else if else");

		// Initialization and Registers
		radiationRegistry.register();
		radiationTicker.register();
		radiationCommands.register();
		LOGGER.info("Initialized Radiation (1/2)");
		radiationItems.register();
		LOGGER.info("Initialized Items (2/2)");

		LOGGER.info("Iozing Radiation by qndk has been initialized!");
	}
}