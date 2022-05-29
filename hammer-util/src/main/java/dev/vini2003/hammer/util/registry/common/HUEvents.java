package dev.vini2003.hammer.util.registry.common;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import dev.vini2003.hammer.core.api.common.util.PlayerUtil;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.server.command.CommandManager;

public class HUEvents {
	public static void init() {
		CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
			dispatcher.register(
					CommandManager.literal("freeze").requires(source -> source.hasPermissionLevel(4)).then(
							CommandManager.argument("players", EntityArgumentType.players()).executes(context -> {
								var players = EntityArgumentType.getPlayers(context, "players");
								
								for (var player : players) {
									PlayerUtil.setFrozen(player, true);
								}
								
								return Command.SINGLE_SUCCESS;
							})
					)
			);
			
			dispatcher.register(
					CommandManager.literal("unfreeze").requires(source -> source.hasPermissionLevel(4)).then(
							CommandManager.argument("players", EntityArgumentType.players()).executes(context -> {
								var players = EntityArgumentType.getPlayers(context, "players");
								
								for (var player : players) {
									PlayerUtil.setFrozen(player, false);
								}
								
								return Command.SINGLE_SUCCESS;
							})
					)
			);
			
			dispatcher.register(
					CommandManager.literal("heal").requires(source -> source.hasPermissionLevel(4)).then(
							CommandManager.argument("players", EntityArgumentType.players()).executes(context -> {
								var players = EntityArgumentType.getPlayers(context, "players");
								
								for (var player : players) {
									player.setHealth(player.getMaxHealth());
								}
								
								return Command.SINGLE_SUCCESS;
							})
					)
			);
			
			dispatcher.register(
					CommandManager.literal("satiate").requires(source -> source.hasPermissionLevel(4)).then(
							CommandManager.argument("players", EntityArgumentType.players()).executes(context -> {
								var players = EntityArgumentType.getPlayers(context, "players");
								
								for (var player : players) {
									player.getHungerManager().setFoodLevel(20);
									player.getHungerManager().setSaturationLevel(20.0F);
								}
								
								return Command.SINGLE_SUCCESS;
							})
					)
			);
			
			dispatcher.register(
					CommandManager.literal("fly_speed").requires(source -> source.hasPermissionLevel(4)).then(
							CommandManager.argument("speed", IntegerArgumentType.integer(1, 200)).then(
									CommandManager.argument("players", EntityArgumentType.players()).executes(context -> {
										var speed = IntegerArgumentType.getInteger(context, "speed");
										var players = EntityArgumentType.getPlayers(context, "players");
										
										var buf = PacketByteBufs.create();
										buf.writeInt(speed);
										
										for (var player : players) {
											player.getAbilities().setFlySpeed(speed / 20.0F);
											
											ServerPlayNetworking.send(player, HUNetworking.FLY_SPEED_UPDATE, PacketByteBufs.duplicate(buf));
										}
										
										return Command.SINGLE_SUCCESS;
									})
							)
					)
			);
			
			dispatcher.register(
					CommandManager.literal("walk_speed").requires(source -> source.hasPermissionLevel(4)).then(
							CommandManager.argument("speed", IntegerArgumentType.integer(1, 200)).then(
									CommandManager.argument("players", EntityArgumentType.players()).executes(context -> {
										var speed = IntegerArgumentType.getInteger(context, "speed");
										var players = EntityArgumentType.getPlayers(context, "players");
										
										var buf = PacketByteBufs.create();
										buf.writeInt(speed);
										
										for (var player : players) {
											player.getAbilities().setWalkSpeed(speed / 20.0F);
											
											ServerPlayNetworking.send(player, HUNetworking.WALK_SPEED_UPDATE, PacketByteBufs.duplicate(buf));
										}
										
										return Command.SINGLE_SUCCESS;
									})
							)
					)
			);
		});
	}
}
