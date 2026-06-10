package qndk.ionizingradiation.radiationSystem;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.core.Holder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.level.biome.Biome;

public class radiationTicker {

    private static int tickCounter = 0;

    public static void register() {
        ServerTickEvents.END_SERVER_TICK.register(radiationTicker::onServerTick);
    }

    private static void onServerTick(MinecraftServer server) {
        tickCounter++;
        if (tickCounter % 20 != 0) return;

        radiationWorldManager.tick();
        addBiomeRadiationZones(server);
        radiationWorldManager.applyToPlayers(server.getPlayerList().getPlayers());

        for (ServerPlayer player : server.getPlayerList().getPlayers()) {
            float radiation = radiationManager.getRadiation(player);
            applyEffects(player, radiation);
            decayRadiation(player, radiation);
        }
    }

    private static void addBiomeRadiationZones(MinecraftServer server) {
         for (ServerPlayer player : server.getPlayerList().getPlayers()) {
             net.minecraft.resources.ResourceKey<net.minecraft.world.level.Level> dimensionKey = player.level().dimension();

             if (dimensionKey.equals(net.minecraft.world.level.Level.END)) {
                 addRadiationZoneIfNotExists(player.blockPosition(), 50.0f);
             }
             else if (dimensionKey.equals(net.minecraft.world.level.Level.NETHER)) {
                 Holder<Biome> biome = player.level().getBiome(player.blockPosition());
                 var biomeKey = biome.unwrapKey();

                 if (biomeKey.isPresent() && isBasaltDeltas(biomeKey.get())) {
                     addRadiationZoneIfNotExists(player.blockPosition(), 75.0f);
                 }
             }
         }
     }

     private static boolean isBasaltDeltas(net.minecraft.resources.ResourceKey<Biome> biomeKey) {
         return biomeKey.toString().contains("basalt_deltas");
     }

    private static void addRadiationZoneIfNotExists(BlockPos center, float radiationLevel) {
        final double ZONE_RADIUS = 256;
        final float HALF_LIFE = 3600.0f;

        for (radiationZone zone : radiationWorldManager.getZones()) {
            if (zone.center.distSqr(center) <= (ZONE_RADIUS * ZONE_RADIUS)) {
                return;
            }
        }
        radiationWorldManager.addZone(center, ZONE_RADIUS, radiationLevel, HALF_LIFE);
    }

    private static void applyEffects(ServerPlayer player, float radiation) {
        Holder<MobEffect> effect = BuiltInRegistries.MOB_EFFECT.wrapAsHolder(radiationRegistry.RADIATION);

        if (radiation >= 4000) {
            player.hurtServer(player.level(), player.damageSources().magic(), 1.0f);
            player.addEffect(new MobEffectInstance(effect, 40, 2));
        } else if (radiation >= 2000) {
            player.addEffect(new MobEffectInstance(effect, 40, 2));
        } else if (radiation >= 500) {
            player.addEffect(new MobEffectInstance(effect, 40, 1));
        } else if (radiation >= 100) {
            player.addEffect(new MobEffectInstance(effect, 40, 0));
        }
    }

    private static void decayRadiation(ServerPlayer player, float radiation) {
        if (radiation <= 0) return;
        float decayed = radiation * 0.999f;
        radiationManager.setRadiation(player, decayed);
    }
}