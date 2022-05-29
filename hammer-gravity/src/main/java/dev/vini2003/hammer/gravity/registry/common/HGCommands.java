package dev.vini2003.hammer.gravity.registry.common;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import dev.vini2003.hammer.gravity.api.common.manager.GravityManager;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.command.argument.RegistryKeyArgumentType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.text.Text;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;


public class HGCommands {
	public static void init() {
		CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
			dispatcher.register(
					CommandManager.literal("gravity").then(
							CommandManager.argument("world", RegistryKeyArgumentType.registryKey(Registry.WORLD_KEY)).then(
									CommandManager.argument("gravity", FloatArgumentType.floatArg()).executes(context -> {
										var world = RegistryKeyArgumentType.<World>getKey(context, "world", Registry.WORLD_KEY, new DynamicCommandExceptionType(id -> Text.translatable("command.hammer.unknown_registry_key", id)));
										var gravity = FloatArgumentType.getFloat(context, "gravity");
										
										GravityManager.set(world, gravity);
										
										return Command.SINGLE_SUCCESS;
									})
							)
					)
			);
		});
	}
}
