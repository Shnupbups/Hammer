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

package dev.vini2003.hammer.core.mixin.common;

import dev.vini2003.hammer.core.api.common.event.ChatEvents;
import io.netty.util.concurrent.Future;
import net.minecraft.network.message.MessageType;
import net.minecraft.network.packet.s2c.play.GameMessageS2CPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.registry.RegistryKey;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.UUID;
@Mixin(ServerPlayerEntity.class)
public abstract class ServerPlayerEntityMixin {
	@Shadow
	public ServerPlayNetworkHandler networkHandler;
	@Shadow
	protected abstract boolean acceptsMessage(RegistryKey<MessageType> typeKey);
	@Shadow
	@Final
	public MinecraftServer server;

	@Shadow
	protected abstract int getMessageTypeId(RegistryKey<MessageType> typeKey);

	@Inject(at = @At("HEAD"), method = "sendMessage(Lnet/minecraft/text/Text;Lnet/minecraft/util/registry/RegistryKey;)V", cancellable = true)
	private void hammer$sendMessage(Text message, RegistryKey<MessageType> typeKey, CallbackInfo ci) {
		if (typeKey.equals(MessageType.CHAT) && acceptsMessage(typeKey)) {
			try {
				var result = ChatEvents.SEND_MESSAGE.invoker().send((ServerPlayerEntity) (Object) this, message, typeKey, null);

				if (result.getResult().isAccepted()) {
					networkHandler.sendPacket(new GameMessageS2CPacket(result.getValue(), getMessageTypeId(typeKey)), Future::isSuccess);
				}
				
				ci.cancel();
			} catch (Exception ignored) {
			}
		}
	}
}
