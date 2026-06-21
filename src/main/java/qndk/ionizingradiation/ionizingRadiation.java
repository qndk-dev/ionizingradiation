package qndk.ionizingradiation;

// Fabric Imports
import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerWorldEvents;
import net.minecraft.server.level.ServerLevel;

// IonizingRadiation Imports
import qndk.ionizingradiation.radiationSystem.radiationRegistry;
import qndk.ionizingradiation.radiationSystem.radiationTicker;
import qndk.ionizingradiation.radiationSystem.radiationCommands;
import qndk.ionizingradiation.radiationItems.radiationItems;
import qndk.ionizingradiation.radiationSystem.radiationWorldSavedData;


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
		LOGGER.info("Initialized Radiation (1/3)");
		radiationItems.register();
		LOGGER.info("Initialized Items (2/3)");
		ServerWorldEvents.LOAD.register((server, world) -> {
			if (!world.isClientSide()) {
				radiationWorldSavedData.loadFromFile(server, (ServerLevel) world);
			}
		});

		ServerWorldEvents.UNLOAD.register((server, world) -> {
			if (!world.isClientSide()) {
				radiationWorldSavedData.saveToFile(server, (ServerLevel) world);
			}
		});
		LOGGER.info("Initialized Saves (3/3)");
		LOGGER.info("Ionizing Radiation by qndk has been initialized!");
	}
}