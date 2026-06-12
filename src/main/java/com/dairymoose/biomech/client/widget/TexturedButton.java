package com.dairymoose.biomech.client.widget;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

/**
 * Replicates the pre-1.21 {@code ImageButton} behaviour: blit a sub-region of a texture atlas at
 * (texU, texV), shifting down by {@code hoverVOffset} rows while hovered/focused. 1.21's ImageButton
 * dropped this in favour of the WidgetSprites system; this keeps BioMech's existing GUI atlas usage.
 *
 * Constructor arg order matches the old ImageButton so call sites read the same. The atlas is assumed
 * to be 256x256 (texWidth/texHeight are accepted for signature parity but the 256-based blit is used).
 */
public class TexturedButton extends Button {
	private final ResourceLocation texture;
	private final int texU;
	private final int texV;
	private final int hoverVOffset;

	public TexturedButton(int x, int y, int width, int height, int texU, int texV, int hoverVOffset,
			ResourceLocation texture, int texWidth, int texHeight, OnPress onPress) {
		super(x, y, width, height, Component.empty(), onPress, Button.DEFAULT_NARRATION);
		this.texture = texture;
		this.texU = texU;
		this.texV = texV;
		this.hoverVOffset = hoverVOffset;
	}

	@Override
	protected void renderWidget(GuiGraphics gui, int mouseX, int mouseY, float partialTick) {
		int v = this.texV;
		if (this.isHoveredOrFocused()) {
			v += this.hoverVOffset;
		}
		gui.blit(this.texture, this.getX(), this.getY(), this.texU, v, this.width, this.height);
	}
}
