package qndk.ionizingradiation.radiationSystem;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;

public class radiationTicker {

    private static int tickCounter = 0;

    public static void register() {
        ServerTickEvents.END_SERVER_TICK.register(radiationTicker::onServerTick);
    }

    private static void onServerTick(MinecraftServer server) {
        tickCounter++;
        if (tickCounter % 20 != 0) return;

        radiationWorldManager.tick();
        radiationWorldManager.applyToPlayers(server.getPlayerList().getPlayers());

        for (ServerPlayer player : server.getPlayerList().getPlayers()) {
            float radiation = radiationManager.getRadiation(player);
            applyEffects(player, radiation);
            decayRadiation(player, radiation);
        }
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