package qndk.ionizingradiation.radiationItems;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;

public class radiationItems {

    public static final Item DOSIMETER = Registry.register(
            BuiltInRegistries.ITEM,
            Identifier.fromNamespaceAndPath("ionizingradiation", "dosimeter"),
            new dosimeter(new Item.Properties().setId(
                    ResourceKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath("ionizingradiation", "dosimeter"))
            ))
    );

    public static final Item GEIGER_COUNTER = Registry.register(
            BuiltInRegistries.ITEM,
            Identifier.fromNamespaceAndPath("ionizingradiation", "geiger_counter"),
            new geigerCounter(new Item.Properties().setId(
                    ResourceKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath("ionizingradiation", "geiger_counter"))
            ))
    );

    public static void register() {}
}