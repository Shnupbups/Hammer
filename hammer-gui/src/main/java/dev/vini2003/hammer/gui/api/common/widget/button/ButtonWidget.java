package dev.vini2003.hammer.gui.api.common.widget.button;

import dev.vini2003.hammer.core.HC;
import dev.vini2003.hammer.core.api.client.texture.PartitionedTexture;
import dev.vini2003.hammer.core.api.client.texture.base.Texture;
import dev.vini2003.hammer.core.api.client.util.DrawingUtil;
import dev.vini2003.hammer.core.api.client.util.InstanceUtil;
import dev.vini2003.hammer.core.api.common.supplier.LabelSupplier;
import dev.vini2003.hammer.core.api.common.supplier.TextureSupplier;
import dev.vini2003.hammer.core.api.common.util.TextUtil;
import dev.vini2003.hammer.gui.api.common.event.MouseClickedEvent;
import dev.vini2003.hammer.gui.api.common.event.annotation.EventSubscriber;
import dev.vini2003.hammer.gui.api.common.event.type.EventType;
import dev.vini2003.hammer.gui.api.common.widget.Widget;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;

import java.util.function.BooleanSupplier;

public class ButtonWidget extends Widget {
	public static final Texture STANDARD_ENABLED_TEXTURE = new PartitionedTexture(HC.id("textures/widget/button_enabled.png"), 18.0F, 18.0F, 0.11F, 0.11F, 0.11F, 0.16F);
	public static final Texture STANDARD_DISABLED_TEXTURE = new PartitionedTexture(HC.id("textures/widget/button_disabled.png"), 18.0F, 18.0F, 0.11F, 0.11F, 0.11F, 0.16F);
	public static final Texture STANDARD_FOCUSED_TEXTURE = new PartitionedTexture(HC.id("textures/widget/button_focused.png"), 18.0F, 18.0F, 0.11F, 0.11F, 0.11F, 0.16F);
	
	protected TextureSupplier enabledTextureSupplier = () -> STANDARD_ENABLED_TEXTURE;
	protected TextureSupplier disabledTextureSupplier = () -> STANDARD_DISABLED_TEXTURE;
	protected TextureSupplier focusedTextureSupplier = () -> STANDARD_FOCUSED_TEXTURE;
	
	protected BooleanSupplier disabledSupplier = () -> false;
	
	protected LabelSupplier labelSupplier = () -> null;
	
	@Override
	protected void onMouseClicked(MouseClickedEvent event) {
		if (isFocused() && rootCollection != null && rootCollection.isScreenHandler()) {
			if (rootCollection.isClient()) {
				playSound();
			}
		}
	}
	
	@Override
	public boolean shouldSync(EventType type) {
		return type == EventType.MOUSE_CLICKED;
	}
	
	@Override
	public void draw(MatrixStack matrices, VertexConsumerProvider provider, float tickDelta) {
		var texture = (Texture) null;
		
		if (isDisabled()) {
			texture = disabledTextureSupplier.get();
		} else if (isFocused()) {
			texture = focusedTextureSupplier.get();
		} else {
			texture = enabledTextureSupplier.get();
		}
		
		texture.draw(matrices, provider, getX(), getY(), getWidth(), getHeight());
		
		if (provider instanceof VertexConsumerProvider.Immediate immediate) {
			immediate.draw();
		}
		
		var label = labelSupplier.get();
		
		if (label != null) {
			var textRenderer = DrawingUtil.getTextRenderer();
			
			textRenderer.drawWithShadow(matrices, label, getX() + (getWidth() / 2.0F - TextUtil.getWidth(label) / 2.0F), getY() + (getHeight() / 2.0F - TextUtil.getHeight(label) / 2.0F), 0xFCFCFC);
		}
	}
	
	protected void playSound() {
		var client = InstanceUtil.getClient();
		
		client.getSoundManager().play(PositionedSoundInstance.master(SoundEvents.UI_BUTTON_CLICK, 1.0F));
	}
	
	public void setEnabledTexture(TextureSupplier enabledTextureSupplier) {
		this.enabledTextureSupplier = enabledTextureSupplier;
	}
	
	public void setEnabledTexture(Texture enabledTexture) {
		setEnabledTexture(() -> enabledTexture);
	}
	
	public void setDisabledTexture(TextureSupplier disabledTextureSupplier) {
		this.disabledTextureSupplier = disabledTextureSupplier;
	}
	
	public void setDisabledTexture(Texture disabledTexture) {
		setDisabledTexture(() -> disabledTexture);
	}
	
	public void setFocusedTexture(TextureSupplier focusedTextureSupplier) {
		this.focusedTextureSupplier = focusedTextureSupplier;
	}
	
	public void setFocusedTexture(Texture focusedTexture) {
		setFocusedTexture(() -> focusedTexture);
	}
	
	public boolean isDisabled() {
		return disabledSupplier.getAsBoolean();
	}
	
	public void setDisabled(BooleanSupplier disabledSupplier) {
		this.disabledSupplier = disabledSupplier;
	}
	
	public void setDisabled(boolean disabled) {
		setDisabled(() -> disabled);
	}
	
	public void setLabel(LabelSupplier labelSupplier) {
		this.labelSupplier = labelSupplier;
	}
	
	public void setLabel(Text label) {
		setLabel(() -> label);
	}
}
