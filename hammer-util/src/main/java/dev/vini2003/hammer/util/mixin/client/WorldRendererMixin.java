package dev.vini2003.hammer.util.mixin.client;

import dev.vini2003.hammer.core.api.client.util.InstanceUtil;
import dev.vini2003.hammer.util.api.common.item.TriggerItem;
import dev.vini2003.hammer.core.api.common.util.RaycastUtil;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.FallingBlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtDouble;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.nbt.NbtList;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Matrix4f;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WorldRenderer.class)
public abstract class WorldRendererMixin {
	@Shadow
	protected abstract void renderEntity(Entity entity, double cameraX, double cameraY, double cameraZ, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers);
	
	@Shadow
	@Final
	private BufferBuilderStorage bufferBuilders;
	
	@Shadow
	private @Nullable ClientWorld world;
	
	@Shadow
	public abstract void render(MatrixStack matrices, float tickDelta, long limitTime, boolean renderBlockOutline, Camera camera, GameRenderer gameRenderer, LightmapTextureManager lightmapTextureManager, Matrix4f positionMatrix);
	
	@ModifyVariable(
			method = "render",
			at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/BufferBuilderStorage;getEntityVertexConsumers()Lnet/minecraft/client/render/VertexConsumerProvider$Immediate;"),
			ordinal = 3
	)
	private boolean hammer$render$getEntityVertexConsumers(boolean original) {
		var client = InstanceUtil.getClient();
		
		var player = client.player;
		
		if (player == null) {
			return original;
		}
		
		var hand = player.getActiveHand();
		
		var stack = player.getStackInHand(hand);
		
		if (stack.getItem() instanceof TriggerItem triggerItem) {
			if (triggerItem.shouldEnableOutline()) {
				return true;
			}
		}
		
		return original;
	}
	
	@Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/VertexConsumerProvider$Immediate;drawCurrentLayer()V"), method = "render")
	private void hammer$render$drawCurrentLayer(MatrixStack matrices, float tickDelta, long limitTime, boolean renderBlockOutline, Camera camera, GameRenderer gameRenderer, LightmapTextureManager lightmapTextureManager, Matrix4f positionMatrix, CallbackInfo ci) {
		var client = InstanceUtil.getClient();
		
		var player = client.player;
		
		if (player == null) {
			return;
		}
		
		var hand = player.getActiveHand();
		
		var stack = player.getStackInHand(hand);
		
		if (stack.getItem() instanceof TriggerItem triggerItem) {
			if (RaycastUtil.raycastEntity(player, tickDelta, 256.0F) == null) {
				var raycastHitResult = player.raycast(256.0F, tickDelta, false);
				
				if (raycastHitResult instanceof BlockHitResult raycastBlockHitResult) {
					if (triggerItem.shouldOutlineBlockRaycast(world.getBlockState(raycastBlockHitResult.getBlockPos()).getBlock())) {
						renderTarget(matrices, tickDelta, camera, raycastBlockHitResult.getBlockPos());
					}
				}
			}
		}
	}
	
	@Unique
	private void renderTarget(MatrixStack matrices, float tickDelta, Camera camera, BlockPos blockPos) {
		FallingBlockEntity entity = new FallingBlockEntity(EntityType.FALLING_BLOCK, world) {
			@Override
			public BlockPos getBlockPos() {
				return BlockPos.ORIGIN;
			}
			
			@Override
			public boolean isGlowing() {
				return true;
			}
		};
		
		var state = world.getBlockState(blockPos);
		
		var nbt = new NbtCompound();
		
		var posList = new NbtList();
		posList.add(NbtDouble.of(blockPos.getX() + 0.5));
		posList.add(NbtDouble.of(blockPos.getY()));
		posList.add(NbtDouble.of(blockPos.getZ() + 0.5));
		
		nbt.put("Pos", posList);
		nbt.putBoolean("NoGravity", true);
		nbt.putBoolean("Glowing", true);
		
		nbt.put("BlockState", NbtHelper.fromBlockState(state));
		entity.readNbt(nbt);
		entity.lastRenderX = entity.getX();
		entity.lastRenderY = entity.getY();
		entity.lastRenderZ = entity.getZ();
		entity.setFallingBlockPos(blockPos);
		entity.setGlowing(true);
		
		var cameraPos = camera.getPos();
		
		var outlineProvider = bufferBuilders.getOutlineVertexConsumers();
		
		outlineProvider.setColor(255, 252, 71, 255);
		
		renderEntity(entity, cameraPos.x, cameraPos.y, cameraPos.z, 1.0F, matrices, outlineProvider);
	}
}
