package qndk.ionizingradiation.radiationSystem;

import net.minecraft.server.level.ServerPlayer;
import java.util.HashMap;
import java.util.UUID;

public class radiationManager {
    private static final HashMap<UUID, Float> playerRadiation = new HashMap<>();

    public static float getRadiation(ServerPlayer player) {
        return playerRadiation.getOrDefault(player.getUUID(), 0.0f);
    }

    public static void setRadiation(ServerPlayer player, float amount) {
        playerRadiation.put(player.getUUID(), amount);
    }

    public static void addRadiation(ServerPlayer player, float amount) {
        float current = getRadiation(player);
        playerRadiation.put(player.getUUID(), current + amount);
    }
}