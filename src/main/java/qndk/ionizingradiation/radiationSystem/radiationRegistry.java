package qndk.ionizingradiation.radiationSystem;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.effect.MobEffect;

public class radiationRegistry {
    public static final MobEffect RADIATION = Registry.register(
            BuiltInRegistries.MOB_EFFECT,
            Identifier.fromNamespaceAndPath("ionizingradiation", "radiation"),
            new radiationEffect()
    );

    public static void register() {}
}