package qndk.ionizingradiation.radiationItems;

import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import qndk.ionizingradiation.radiationSystem.radiationWorldManager;

public class geigerCounter extends Item {

    public geigerCounter(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        if (!level.isClientSide()) {
            float zoneRad = radiationWorldManager.getRadiationAt(player.blockPosition());
            float halfLife = radiationWorldManager.getHalfLifeAt(player.blockPosition());

            if (zoneRad <= 0) {
                player.displayClientMessage(
                        Component.literal("Радиация: 0.00 мЗв/с - чисто"),
                        true
                );
            } else {
                player.displayClientMessage(
                        Component.literal(String.format("%.2f", zoneRad) + " мЗв/с | T½: " + String.format("%.1f", halfLife) + "с"),
                        true
                );
            }
        }
        return InteractionResult.SUCCESS;
    }
}