package dev.vini2003.hammer.chat.mixin.client;

import dev.vini2003.hammer.chat.registry.common.HCOptions;

import net.minecraft.client.gui.screen.option.ChatOptionsScreen;
import net.minecraft.client.option.SimpleOption;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(ChatOptionsScreen.class)
public class ChatOptionsScreenMixin {
	@ModifyArg(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/option/SimpleOptionsScreen;<init>(Lnet/minecraft/client/gui/screen/Screen;Lnet/minecraft/client/option/GameOptions;Lnet/minecraft/text/Text;[Lnet/minecraft/client/option/SimpleOption;)V"), method = "<init>", index = 3)
	private static SimpleOption<?>[] hammer$init(SimpleOption<?>[] options) {
		var prevOptions = options;

		options = new SimpleOption<?>[options.length + 3];

		System.arraycopy(prevOptions, 0, options, 0, options.length - 3);

		options[prevOptions.length] = HCOptions.SHOW_CHAT;
		options[prevOptions.length + 1] = HCOptions.SHOW_COMMAND_FEEDBACK;
		options[prevOptions.length + 2] = HCOptions.SHOW_WARNINGS;
		return options;
	}
}
