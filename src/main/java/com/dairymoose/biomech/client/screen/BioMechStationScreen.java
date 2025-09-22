package com.dairymoose.biomech.client.screen;

import com.dairymoose.biomech.BioMech;
import com.dairymoose.biomech.BioMechPlayerData;
import com.dairymoose.biomech.BioMechPlayerData.SlottedItem;
import com.dairymoose.biomech.item.armor.MechPart;
import com.dairymoose.biomech.menu.BioMechStationMenu;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class BioMechStationScreen extends AbstractContainerScreen<BioMechStationMenu> {
	private static final ResourceLocation GUI_LOCATION = ResourceLocation.fromNamespaceAndPath(BioMech.MODID,
			"textures/gui/biomech_station.png");
	private float xMouse;
	private float yMouse;

	@Override
	protected void init() {
		super.init();

		int visibilityButtonWidth = 16;
		int visibilityButtonHeight = 10;
		int visibiltyButtonTextureX = 178;
		int visibilityButtonTextureY = 0;

		int backButtonX = this.leftPos + 13;
		int backButtonY = this.topPos + 6;

		int headButtonX = this.leftPos + 68;
		int headButtonY = this.topPos + 4;

		int leftHandButtonX = this.leftPos + 41;
		int leftHandButtonY = this.topPos + 47;

		int chestButtonX = this.leftPos + 92;
		int chestButtonY = this.topPos + 58;

		int rightHandButtonX = this.leftPos + 95;
		int rightHandButtonY = this.topPos + 47;

		int leggingsButtonX = this.leftPos + 68;
		int leggingsButtonY = this.topPos + 70;

		MechPart[] mechParts = { MechPart.Back, MechPart.Head, MechPart.RightArm, MechPart.Chest, MechPart.LeftArm, MechPart.Leggings };
		int[] buttonStartX = { backButtonX, headButtonX, leftHandButtonX, chestButtonX, rightHandButtonX,
				leggingsButtonX };
		int[] buttonStartY = { backButtonY, headButtonY, leftHandButtonY, chestButtonY, rightHandButtonY,
				leggingsButtonY };

		class IntHolder {
			int counter;
		}
		IntHolder intHolder = new IntHolder();
		intHolder.counter = 0;
		for (int i = 0; i < buttonStartX.length; ++i) {
			ImageButton imageButton = new ImageButton(buttonStartX[intHolder.counter], buttonStartY[intHolder.counter],
					visibilityButtonWidth, visibilityButtonHeight, visibiltyButtonTextureX, visibilityButtonTextureY,
					visibilityButtonHeight, GUI_LOCATION, 256, 256, new Button.OnPress() {
						MechPart thisPart = mechParts[intHolder.counter];

						@Override
						public void onPress(Button btn) {
							BioMechPlayerData playerData = BioMech.globalPlayerData
									.get(Minecraft.getInstance().player.getUUID());
							if (playerData != null) {
								SlottedItem slottedItem = playerData.getForSlot(thisPart);
								if (slottedItem != null) {
									slottedItem.visible = !slottedItem.visible;
								}
							}
							// send updated playerData to server
						}

					});

			this.addRenderableWidget(imageButton);
			++intHolder.counter;
		}

		// this.addRenderableWidget(headVisibilityButton);
	}

	public BioMechStationScreen(BioMechStationMenu p_98798_, Inventory p_98799_, Component p_98800_) {
		super(p_98798_, p_98799_, Component.empty());
		this.imageHeight = 166;
		this.inventoryLabelY = this.imageHeight - 94;
	}

	public void render(GuiGraphics p_282918_, int p_282102_, int p_282423_, float p_282621_) {
		this.renderBackground(p_282918_);
		super.render(p_282918_, p_282102_, p_282423_, p_282621_);
		this.renderTooltip(p_282918_, p_282102_, p_282423_);

		this.xMouse = (float) p_282102_;
		this.yMouse = (float) p_282423_;
	}

	protected void renderBg(GuiGraphics p_281616_, float p_282737_, int p_281678_, int p_281465_) {
		int i = (this.width - this.imageWidth) / 2;
		int j = (this.height - this.imageHeight) / 2;
		p_281616_.blit(GUI_LOCATION, i, j, 0, 0, this.imageWidth, this.imageHeight);
		InventoryScreen.renderEntityInInventoryFollowsMouse(p_281616_, i + 144, j + 75, 30,
				(float) (i + 144) - this.xMouse, (float) (j + 75 - 50) - this.yMouse, this.minecraft.player);
	}
}