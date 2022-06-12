package dev.vini2003.hammer.chat.registry.common;

import dev.vini2003.hammer.chat.api.common.util.ChatUtil;
import dev.vini2003.hammer.core.api.client.util.InstanceUtil;
import net.minecraft.client.option.SimpleOption;

public class HCOptions {
	public static final SimpleOption<Boolean> SHOW_CHAT = SimpleOption.ofBoolean("options.show_chat", true, showChat -> {
		var client = InstanceUtil.getClient();
		
		if (client == null || client.player == null) {
			return;
		}
		
		ChatUtil.setShowChat(client.player, showChat);
	});
	
	public static final SimpleOption<Boolean> SHOW_COMMAND_FEEDBACK = SimpleOption.ofBoolean("options.show_command_feedback", true, showCommandFeedback -> {
		var client = InstanceUtil.getClient();
		
		if (client == null || client.player == null) {
			return;
		}
		
		ChatUtil.setShowCommandFeedback(client.player, showCommandFeedback);
	});

	public static final SimpleOption<Boolean> SHOW_WARNINGS = SimpleOption.ofBoolean("options.show_warnings", false, showWarnings -> {
		var client = InstanceUtil.getClient();

		if (client == null || client.player == null) {
			return;
		}

		ChatUtil.setShowWarnings(client.player, showWarnings);
	});
}
