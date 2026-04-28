package qndk.ionizingradiation.radiationSystem;

import com.mojang.brigadier.arguments.FloatArgumentType;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

import static net.minecraft.commands.Commands.argument;
import static net.minecraft.commands.Commands.literal;

public class radiationCommands {

    public static void register() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            dispatcher.register(literal("radiation")
                    .then(literal("get")
                            .executes(ctx -> {
                                ServerPlayer player = ctx.getSource().getPlayerOrException();
                                float rad = radiationManager.getRadiation(player);
                                ctx.getSource().sendSuccess(
                                        () -> Component.literal("Радиация: " + rad + " мЗв"),
                                        false
                                );
                                return 1;
                            })
                    )
                    .then(literal("set")
                            .then(argument("amount", FloatArgumentType.floatArg(0))
                                    .executes(ctx -> {
                                        ServerPlayer player = ctx.getSource().getPlayerOrException();
                                        float amount = FloatArgumentType.getFloat(ctx, "amount");
                                        radiationManager.setRadiation(player, amount);
                                        ctx.getSource().sendSuccess(
                                                () -> Component.literal("Радиация установлена: " + amount + " мЗв"),
                                                false
                                        );
                                        return 1;
                                    })
                            )
                    )
            );
        });
    }
}