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

package dev.vini2003.hammer.chat.mixin.client;

import dev.vini2003.hammer.chat.registry.common.HCUuids;
import dev.vini2003.hammer.chat.registry.common.HCValues;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.network.MessageSender;
import net.minecraft.network.MessageType;
import net.minecraft.text.Text;
import net.minecraft.util.registry.BuiltinRegistries;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Objects;
import java.util.UUID;

@Mixin(InGameHud.class)
public class InGameHudMixin {
	@Inject(method = "onChatMessage", at = @At("HEAD"), cancellable = true)
	private void hammer$chat$addChatMessage(MessageType type, Text message, MessageSender sender, CallbackInfo ci) {
		if (Objects.equals(type, BuiltinRegistries.MESSAGE_TYPE.get(MessageType.SYSTEM)) && Objects.equals(sender.uuid(), HCUuids.COMMAND_FEEDBACK_UUID)) {
			if (!HCValues.SHOW_FEEDBACK) {
				ci.cancel();
			}
		} else if (Objects.equals(type, BuiltinRegistries.MESSAGE_TYPE.get(MessageType.CHAT))) {
			if (!HCValues.SHOW_CHAT || !HCValues.SHOW_GLOBAL_CHAT) {
				ci.cancel();
			}
		}
	}
}
