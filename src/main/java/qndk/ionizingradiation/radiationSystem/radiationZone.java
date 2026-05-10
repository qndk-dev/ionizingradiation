package qndk.ionizingradiation.radiationSystem;

import net.minecraft.core.BlockPos;

public class radiationZone {
    public final BlockPos center;
    public final double radius;
    public float radiationLevel; // мЗв/с
    public float halfLife;       // с

    public radiationZone(BlockPos center, double radius, float radiationLevel, float halfLife) {
        this.center = center;
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
        radiationLevel *= (float) Math.pow(0.5, 1.0 / halfLife); // ЧТОООО? HALF LIFE 3 ANNOUNCEMENT?
    }

    public boolean isDead() {
        return radiationLevel < 0.01f;
    }
}