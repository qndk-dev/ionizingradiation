package qndk.ionizingradiation.radiationSystem;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;

public class radiationZone {

    public final BlockPos center;
    public final ResourceKey<Level> dimension;
    public final double radius;
    public float radiationLevel; // мЗв/с
    public float halfLife; // с

    public radiationZone(
        BlockPos center,
        ResourceKey<Level> dimension,
        double radius,
        float radiationLevel,
        float halfLife
    ) {
        this.center = center;
        this.dimension = dimension;
        this.radius = radius;
        this.radiationLevel = radiationLevel;
        this.halfLife = halfLife;
    }

    public boolean isInZone(BlockPos pos) {
        return center.distSqr(pos) <= radius * radius;
    }

    public void tick() {
        // N(t) = N0 * 0.5^(t/halfLife)
        // level *= 0.5^(1/halfLife)
        radiationLevel *= (float) Math.pow(0.5, 1.0 / halfLife);
    }

    public boolean isDead() {
        return radiationLevel < 0.01f;
    }
}
