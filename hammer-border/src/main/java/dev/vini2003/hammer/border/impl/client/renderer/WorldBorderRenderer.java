package dev.vini2003.hammer.border.impl.client.renderer;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import dev.vini2003.hammer.border.impl.common.border.CubicWorldBorder;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.*;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class WorldBorderRenderer {
	public static void render(MinecraftClient client, World world, Camera camera, Identifier texture) {
		var bufferBuilder = Tessellator.getInstance().getBuffer();
		
		var border = world.getWorldBorder();
		var cubicBorder = (CubicWorldBorder) border;
		
		var viewDistance = client.options.getClampedViewDistance() * 16;
		
		var cameraPos = camera.getPos();
		
		if (cameraPos.getX() >= border.getBoundEast() - viewDistance
				|| cameraPos.getX() <= border.getBoundWest() + viewDistance
				|| cameraPos.getY() <= cubicBorder.getBoundUp() - viewDistance
				|| cameraPos.getY() >= cubicBorder.getBoundDown() + viewDistance
				|| cameraPos.getZ() >= border.getBoundSouth() - viewDistance
				|| cameraPos.getZ() <= border.getBoundNorth() + viewDistance
		) {
			var scaledDistanceToBorder = MathHelper.clamp(1.0 - cubicBorder.getDistanceInsideBorder(cameraPos.getX(), cameraPos.getY(), cameraPos.getZ()) / viewDistance, 0.0D, 1.0D);
			
			scaledDistanceToBorder = Math.pow(scaledDistanceToBorder, 4.0);
			scaledDistanceToBorder = MathHelper.clamp(scaledDistanceToBorder, 0.0, 1.0);
			
			var cameraX = cameraPos.getX();
			var cameraY = cameraPos.getY();
			var cameraZ = cameraPos.getZ();
			
			RenderSystem.enableBlend();
			RenderSystem.enableDepthTest();
			RenderSystem.blendFuncSeparate(GlStateManager.SrcFactor.SRC_ALPHA, GlStateManager.DstFactor.ONE, GlStateManager.SrcFactor.ONE, GlStateManager.DstFactor.ZERO);
			RenderSystem.setShaderTexture(0, texture);
			RenderSystem.depthMask(MinecraftClient.isFabulousGraphicsOrBetter());
			
			var matrices = RenderSystem.getModelViewStack();
			matrices.push();
			
			RenderSystem.applyModelViewMatrix();
			
			var color = border.getStage().getColor();
			
			var red = (color >> 16 & 0xFF) / 255.0F;
			var green = (color >> 8 & 0xFF) / 255.0F;
			var blue = (color & 0xFF) / 255.0F;
			
			RenderSystem.setShaderColor(red, green, blue, (float) scaledDistanceToBorder);
			RenderSystem.setShader(GameRenderer::getPositionTexShader);
			RenderSystem.polygonOffset(-3.0F, -3.0F);
			RenderSystem.enablePolygonOffset();
			RenderSystem.disableCull();
			
			var fadeUv = (Util.getMeasuringTimeMs() % 3000L) / 3000.0F;
			
			bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE);
			
			var size = (float) border.getSize() / 2.0F;
			
			// Draw South border.
			bufferBuilder.vertex(border.getBoundEast() - cameraX, cubicBorder.getBoundUp() - cameraY, border.getBoundSouth() - cameraZ).texture(fadeUv - 0.0F, fadeUv + 0.0F).next();
			bufferBuilder.vertex(border.getBoundWest() - cameraX, cubicBorder.getBoundUp() - cameraY, border.getBoundSouth() - cameraZ).texture(fadeUv - size, fadeUv + 0.0F).next();
			bufferBuilder.vertex(border.getBoundWest() - cameraX, cubicBorder.getBoundDown() - cameraY, border.getBoundSouth() - cameraZ).texture(fadeUv - size, fadeUv + size).next();
			bufferBuilder.vertex(border.getBoundEast() - cameraX, cubicBorder.getBoundDown() - cameraY, border.getBoundSouth() - cameraZ).texture(fadeUv - 0.0F, fadeUv + size).next();
			
			// Draw North border.
			bufferBuilder.vertex(border.getBoundEast() - cameraX, cubicBorder.getBoundUp() - cameraY, border.getBoundNorth() - cameraZ).texture(fadeUv - 0.0F, fadeUv + 0.0F).next();
			bufferBuilder.vertex(border.getBoundWest() - cameraX, cubicBorder.getBoundUp() - cameraY, border.getBoundNorth() - cameraZ).texture(fadeUv - size, fadeUv + 0.0F).next();
			bufferBuilder.vertex(border.getBoundWest() - cameraX, cubicBorder.getBoundDown() - cameraY, border.getBoundNorth() - cameraZ).texture(fadeUv - size, fadeUv + size).next();
			bufferBuilder.vertex(border.getBoundEast() - cameraX, cubicBorder.getBoundDown() - cameraY, border.getBoundNorth() - cameraZ).texture(fadeUv - 0.0F, fadeUv + size).next();
			
			// Draw East border.
			bufferBuilder.vertex(border.getBoundEast() - cameraX, cubicBorder.getBoundUp() - cameraY, border.getBoundNorth() - cameraZ).texture(fadeUv - 0.0F, fadeUv + 0.0F).next();
			bufferBuilder.vertex(border.getBoundEast() - cameraX, cubicBorder.getBoundUp() - cameraY, border.getBoundSouth() - cameraZ).texture(fadeUv - size, fadeUv + 0.0F).next();
			bufferBuilder.vertex(border.getBoundEast() - cameraX, cubicBorder.getBoundDown() - cameraY, border.getBoundSouth() - cameraZ).texture(fadeUv - size, fadeUv + size).next();
			bufferBuilder.vertex(border.getBoundEast() - cameraX, cubicBorder.getBoundDown() - cameraY, border.getBoundNorth() - cameraZ).texture(fadeUv - 0.0F, fadeUv + size).next();
			
			// Draw West border.
			bufferBuilder.vertex(border.getBoundWest() - cameraX, cubicBorder.getBoundUp() - cameraY, border.getBoundNorth() - cameraZ).texture(fadeUv - 0.0F, fadeUv + 0.0F).next();
			bufferBuilder.vertex(border.getBoundWest() - cameraX, cubicBorder.getBoundUp() - cameraY, border.getBoundSouth() - cameraZ).texture(fadeUv - size, fadeUv + 0.0F).next();
			bufferBuilder.vertex(border.getBoundWest() - cameraX, cubicBorder.getBoundDown() - cameraY, border.getBoundSouth() - cameraZ).texture(fadeUv - size, fadeUv + size).next();
			bufferBuilder.vertex(border.getBoundWest() - cameraX, cubicBorder.getBoundDown() - cameraY, border.getBoundNorth() - cameraZ).texture(fadeUv - 0.0F, fadeUv + size).next();
			
			// Draw Up border.;
			bufferBuilder.vertex(border.getBoundEast() - cameraX, cubicBorder.getBoundUp() - cameraY, border.getBoundSouth() - cameraZ).texture(fadeUv - 0.0F, fadeUv + 0.0F).next();
			bufferBuilder.vertex(border.getBoundWest() - cameraX, cubicBorder.getBoundUp() - cameraY, border.getBoundSouth() - cameraZ).texture(fadeUv - size, fadeUv + 0.0F).next();
			bufferBuilder.vertex(border.getBoundWest() - cameraX, cubicBorder.getBoundUp() - cameraY, border.getBoundNorth() - cameraZ).texture(fadeUv - size, fadeUv + size).next();
			bufferBuilder.vertex(border.getBoundEast() - cameraX, cubicBorder.getBoundUp() - cameraY, border.getBoundNorth() - cameraZ).texture(fadeUv - 0.0F, fadeUv + size).next();
			
			// Draw Down border.
			bufferBuilder.vertex(border.getBoundEast() - cameraX, cubicBorder.getBoundDown() - cameraY, border.getBoundSouth() - cameraZ).texture(fadeUv - 0.0F, fadeUv + 0.0F).next();
			bufferBuilder.vertex(border.getBoundWest() - cameraX, cubicBorder.getBoundDown() - cameraY, border.getBoundSouth() - cameraZ).texture(fadeUv - size, fadeUv + 0.0F).next();
			bufferBuilder.vertex(border.getBoundWest() - cameraX, cubicBorder.getBoundDown() - cameraY, border.getBoundNorth() - cameraZ).texture(fadeUv - size, fadeUv + size).next();
			bufferBuilder.vertex(border.getBoundEast() - cameraX, cubicBorder.getBoundDown() - cameraY, border.getBoundNorth() - cameraZ).texture(fadeUv - 0.0F, fadeUv + size).next();
			
			BufferRenderer.drawWithShader(bufferBuilder.end());
			RenderSystem.enableCull();
			RenderSystem.polygonOffset(0.0F, 0.0F);
			RenderSystem.disablePolygonOffset();
			RenderSystem.disableBlend();
			
			matrices.pop();
			
			RenderSystem.applyModelViewMatrix();
			RenderSystem.depthMask(true);
		}
	}
}
