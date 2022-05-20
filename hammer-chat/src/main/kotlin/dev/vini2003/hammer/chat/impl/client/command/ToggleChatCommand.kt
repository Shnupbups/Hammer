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

package dev.vini2003.hammer.chat.impl.client.command

import dev.vini2003.hammer.chat.registry.common.HCValues
import dev.vini2003.hammer.command.api.client.command.ClientRootCommand
import dev.vini2003.hammer.command.api.common.util.extension.command
import dev.vini2003.hammer.command.api.common.util.extension.execute
import net.minecraft.text.TranslatableText
val TOGGLE_CHAT_COMMAND = ClientRootCommand {
	command("toggle_chat") {
		execute {
			HCValues.SHOW_CHAT = !HCValues.SHOW_CHAT
			
			source.sendFeedback(TranslatableText("command.hammer.toggle_chat", if (HCValues.SHOW_CHAT) "enabled" else "disabled"))
		}
	}
}