package qndk.ionizingradiation.radiationSystem;

import net.minecraft.core.BlockPos;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class radiationWorldSavedData {

    private static Path getStorageDir(MinecraftServer server) {
        String cwd = System.getProperty("user.dir");
        return java.nio.file.Paths.get(cwd).resolve("ionizingradiation");
    }

    private static String worldId(ServerLevel world) {
        return world.dimension().toString().replace('/', '_').replace(':', '_');
    }

    public static void loadFromFile(MinecraftServer server, ServerLevel world) {
        try {
            Path dir = getStorageDir(server);
            if (!Files.exists(dir)) return;
            Path file = dir.resolve(worldId(world) + "_zones.dat");
            if (!Files.exists(file)) return;
            List<String> lines = Files.readAllLines(file, StandardCharsets.UTF_8);
            List<radiationZone> loaded = new ArrayList<>();
            for (String line : lines) {
                if (line == null || line.isBlank()) continue;
                String[] parts = line.split(",");
                if (parts.length < 6) continue;
                int x = Integer.parseInt(parts[0]);
                int y = Integer.parseInt(parts[1]);
                int z = Integer.parseInt(parts[2]);
                double radius = Double.parseDouble(parts[3]);
                float radiationLevel = Float.parseFloat(parts[4]);
                float halfLife = Float.parseFloat(parts[5]);
                loaded.add(new radiationZone(new BlockPos(x, y, z), radius, radiationLevel, halfLife));
            }
            radiationWorldManager.setZones(loaded);
        } catch (IOException | NumberFormatException e) {
        }
    }

    public static void saveToFile(MinecraftServer server, ServerLevel world) {
        try {
            Path dir = getStorageDir(server);
            if (!Files.exists(dir)) Files.createDirectories(dir);
            Path file = dir.resolve(worldId(world) + "_zones.dat");
            List<String> lines = new ArrayList<>();
            for (radiationZone zone : radiationWorldManager.getZones()) {
                StringBuilder sb = new StringBuilder();
                sb.append(zone.center.getX()).append(',')
                        .append(zone.center.getY()).append(',')
                        .append(zone.center.getZ()).append(',')
                        .append(zone.radius).append(',')
                        .append(zone.radiationLevel).append(',')
                        .append(zone.halfLife);
                lines.add(sb.toString());
            }
            Files.write(file, lines, StandardCharsets.UTF_8);
        } catch (IOException e) {
        }
    }
}



