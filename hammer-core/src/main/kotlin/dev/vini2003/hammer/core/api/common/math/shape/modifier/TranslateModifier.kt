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

package dev.vini2003.hammer.core.api.common.math.shape.modifier

import dev.vini2003.hammer.core.api.common.math.position.Position
import dev.vini2003.hammer.core.api.common.math.shape.modifier.BaseModifier

/**
 * A [TranslateNoiseModifier] is a modifier that applies an offset to a position.
 */
class TranslateModifier(
	/**
	 * Constructs a translate modifier.
	 *
	 * @param x the X offset to use.
	 * @param y the Y offset to use.
	 * @param z the Z offset to use.
	 * @return the modifier.
	 */
	var x: Float,
	var y: Float,
	var z: Float
) : BaseModifier {
	override fun invoke(position: Position): Position {
		return Position(
			position.x + x,
			position.y + y,
			position.z + z
		)
	}
}