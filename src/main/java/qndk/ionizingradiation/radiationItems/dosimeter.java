package qndk.ionizingradiation.radiationItems;

import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.server.level.ServerPlayer;
import qndk.ionizingradiation.radiationSystem.radiationManager;

public class dosimeter extends Item {

    public dosimeter(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        if (!level.isClientSide()) {
            float radiation = radiationManager.getRadiation((ServerPlayer) player);
            player.displayClientMessage(
                    Component.literal("Радиация: " + String.format("%.2f", radiation) + " мЗв"),
                    true
            );
        }
        return InteractionResult.SUCCESS;
    }
}