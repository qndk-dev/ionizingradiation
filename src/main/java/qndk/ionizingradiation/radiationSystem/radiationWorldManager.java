package qndk.ionizingradiation.radiationSystem;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;

import java.util.ArrayList;
import java.util.List;

public class radiationWorldManager {
    private static final List<radiationZone> zones = new ArrayList<>();

    public static void addZone(BlockPos center, double radius, float radiationLevel, float halfLife) {
        zones.add(new radiationZone(center, radius, radiationLevel, halfLife));
    }

    public static float getRadiationAt(BlockPos pos) {
        float total = 0;
        for (radiationZone zone : zones) {
            if (zone.isInZone(pos)) {
                total += zone.radiationLevel;
            }
        }
        return total;
    }

    public static void tick() {
        zones.removeIf(radiationZone::isDead);
        for (radiationZone zone : zones) {
            zone.tick();
        }
    }

    public static void applyToPlayers(List<ServerPlayer> players) {
        for (ServerPlayer player : players) {
            float zoneRadiation = getRadiationAt(player.blockPosition());
            if (zoneRadiation > 0) {
                radiationManager.addRadiation(player, zoneRadiation);
            }
        }
    }

    public static List<radiationZone> getZones() {
        return zones;
    }
}