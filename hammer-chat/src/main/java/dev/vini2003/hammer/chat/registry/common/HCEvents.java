/*
 * MIT License
 *
 * Copyright (c) 2020 - 2022 vini2003
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package dev.vini2003.hammer.chat.registry.common;

import dev.vini2003.hammer.chat.api.common.util.ChatUtil;
import dev.vini2003.hammer.chat.impl.common.accessor.PlayerEntityAccessor;
import dev.vini2003.hammer.core.api.common.event.ChatEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.TypedActionResult;

public class HCEvents {
	public static void init() {
		ChatEvents.SEND_MESSAGE.register((receiver, message, type, sender) -> {
			var us = receiver;
			var them = receiver.getServer().getPlayerManager().getPlayer(sender);
			
			if (them == null) {
				return TypedActionResult.pass(message);
			}
			
			var ourChannel = ((PlayerEntityAccessor) us).hammer$getSelectedChannel();
			var theirChannel = ((PlayerEntityAccessor) them).hammer$getSelectedChannel();
			
			if (ourChannel != theirChannel) {
				return TypedActionResult.fail(message);
			}
			
			return TypedActionResult.success(message);
		});
		
		ChatEvents.SEND_MESSAGE.register((receiver, message, type, sender) -> {
			var us = receiver;
			
			if (sender.equals(us.getUuid()) && ChatUtil.isMuted(us)) {
				us.sendMessage(Text.translatable("text.hammer.muted").formatted(Formatting.RED), false);
				
				return TypedActionResult.fail(message);
			}
			
			var them = receiver.getServer().getPlayerManager().getPlayer(sender);
			
			if (them == null) {
				return TypedActionResult.pass(message);
			}
			
			if (!sender.equals(us.getUuid()) && ChatUtil.isMuted(them)) {
				return TypedActionResult.fail(message);
			}
			
			return TypedActionResult.success(message);
		});
	}
}
