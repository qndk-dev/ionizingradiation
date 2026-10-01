package qndk.ionizingradiation.radiationSystem;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;

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
            List<String> lines = Files.readAllLines(
                file,
                StandardCharsets.UTF_8
            );
            List<radiationZone> loaded = new ArrayList<>();
            for (String line : lines) {
                if (line == null || line.isBlank()) continue;
                String[] parts = line.split(",");
                if (parts.length < 7) continue;
                String dimStr = parts[1];
                net.minecraft.resources.Identifier id = net.minecraft.resources.Identifier.parse(dimStr);
                ResourceKey<Level> dim = ResourceKey.create(Registries.DIMENSION, id);
                int x = Integer.parseInt(parts[2]);
                int y = Integer.parseInt(parts[3]);
                int z = Integer.parseInt(parts[4]);
                double radius = Double.parseDouble(parts[5]);
                float radiationLevel = Float.parseFloat(parts[6]);
                float halfLife = Float.parseFloat(parts[7]);
                loaded.add(
                    new radiationZone(
                        new BlockPos(x, y, z),
                        dim,
                        radius,
                        radiationLevel,
                        halfLife
                    )
                );
            }
            List<radiationZone> filtered = loaded
                .stream()
                .filter(z -> z.dimension.equals(world.dimension()))
                .toList();
            radiationWorldManager.setZones(filtered);
        } catch (IOException | NumberFormatException e) {}
    }

    public static void saveToFile(MinecraftServer server, ServerLevel world) {
        try {
            Path dir = getStorageDir(server);
            if (!Files.exists(dir)) Files.createDirectories(dir);
            Path file = dir.resolve(worldId(world) + "_zones.dat");
            List<String> lines = new ArrayList<>();
            for (radiationZone zone : radiationWorldManager.getZones()) {
                if (!zone.dimension.equals(world.dimension())) continue;
                StringBuilder sb = new StringBuilder();
                sb.append(zone.center.getX())
                    .append(',')
                    .append(zone.dimension.toString())
                    .append(',')
                    .append(zone.center.getY())
                    .append(',')
                    .append(zone.center.getZ())
                    .append(',')
                    .append(zone.radius)
                    .append(',')
                    .append(zone.radiationLevel)
                    .append(',')
                    .append(zone.halfLife);
                lines.add(sb.toString());
            }
            Files.write(file, lines, StandardCharsets.UTF_8);
        } catch (IOException e) {}
    }
}
