package qndk.ionizingradiation.radiationSystem;

import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.commands.CommandSourceStack;
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
                                float zoneRad = radiationWorldManager.getRadiationAt(player.blockPosition());
                                ctx.getSource().sendSuccess(
                                        () -> Component.literal("Доза: " + String.format("%.2f", rad) + " мЗв | Зона: " + String.format("%.2f", zoneRad) + " мЗв/с"),
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
                                                () -> Component.literal("Доза установлена: " + amount + " мЗв"),
                                                false
                                        );
                                        return 1;
                                    })
                            )
                    )
                    .then(literal("zone")
                            .then(literal("create")
                                    .then(argument("level", FloatArgumentType.floatArg(0))
                                            .then(argument("radius", IntegerArgumentType.integer(1))
                                                    .then(argument("halfLife", FloatArgumentType.floatArg(1))
                                                            .executes(ctx -> {
                                                                ServerPlayer player = ctx.getSource().getPlayerOrException();
                                                                float level = FloatArgumentType.getFloat(ctx, "level");
                                                                int radius = IntegerArgumentType.getInteger(ctx, "radius");
                                                                float halfLife = FloatArgumentType.getFloat(ctx, "halfLife");
                                                                radiationWorldManager.addZone(player.blockPosition(), radius, level, halfLife);
                                                                ctx.getSource().sendSuccess(
                                                                        () -> Component.literal("Зона создана: " + level + " мЗв/с, радиус " + radius + ", полураспад " + halfLife + "с"),
                                                                        false
                                                                );
                                                                return 1;
                                                            })
                                                    )
                                            )
                                    )
                            )
                    )
            );
        });
    }
}