package com.dairymoose.biomech.client.screen;

import com.dairymoose.biomech.BioMech;
import com.dairymoose.biomech.BioMechNetwork;
import com.dairymoose.biomech.BioMechPlayerData;
import com.dairymoose.biomech.BioMechPlayerData.SlottedItem;
import com.dairymoose.biomech.item.armor.ArmorBase;
import com.dairymoose.biomech.item.armor.MechPart;
import com.dairymoose.biomech.menu.BioMechStationMenu;
import com.dairymoose.biomech.packet.serverbound.ServerboundUpdateVisibilityPacket;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.navigation.ScreenRectangle;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.Slot;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class BioMechStationScreen extends AbstractContainerScreen<BioMechStationMenu> {
	private static final ResourceLocation GUI_LOCATION = ResourceLocation.fromNamespaceAndPath(BioMech.MODID,
			"textures/gui/biomech_station.png");
	private float xMouse;
	private float yMouse;

	private boolean visibilityMatrix[] = {true, true, true, true, true, true};
	private MechPart[] mechParts = { MechPart.Back, MechPart.Head, MechPart.RightArm, MechPart.Chest, MechPart.LeftArm, MechPart.Leggings };
	
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

		int[] buttonStartX = { backButtonX, headButtonX, leftHandButtonX, chestButtonX, rightHandButtonX,
				leggingsButtonX };
		int[] buttonStartY = { backButtonY, headButtonY, leftHandButtonY, chestButtonY, rightHandButtonY,
				leggingsButtonY };

		BioMechPlayerData playerData = BioMech.globalPlayerData
				.get(Minecraft.getInstance().player.getUUID());
		if (playerData != null) {
			for (int i=0; i<mechParts.length; ++i) {
				SlottedItem slottedItem = playerData.getForSlot(mechParts[i]);
				if (slottedItem != null) {
					visibilityMatrix[i] = slottedItem.visible;
				}
			}
		}
		
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
									
									for (int i=0; i<mechParts.length; ++i) {
										if (mechParts[i] == thisPart) {
											visibilityMatrix[i] = slottedItem.visible;
										}
									}
								}
								
								BioMechNetwork.INSTANCE.sendToServer(new ServerboundUpdateVisibilityPacket(BioMechPlayerData.serialize(playerData)));
							}
						}

					});
			
			this.addRenderableWidget(imageButton);
			++intHolder.counter;
		}
		
		if (this.getMenu().containerId == -1) {
			int x = this.leftPos + 87;
			int y = this.topPos + 7;
			int buttonWidth = 9;
			int buttonHeight = 9;
			int texStartX = 194;
			int texStartY = 18;
			ImageButton inventoryButton = new ImageButton(x, y,
					buttonWidth, buttonHeight, texStartX, texStartY,
					buttonHeight, GUI_LOCATION, 256, 256, new Button.OnPress() {
						@Override
						public void onPress(Button btn) {
							Minecraft.getInstance().setScreen(new InventoryScreen(Minecraft.getInstance().player));
						}

					});
			this.addRenderableWidget(inventoryButton);
		}
	}

	public BioMechStationScreen(BioMechStationMenu p_98798_, Inventory p_98799_, Component p_98800_) {
		super(p_98798_, p_98799_, Component.empty());
		this.imageHeight = 166;
		this.inventoryLabelY = this.imageHeight - 94;
	}

	public static final int SLOT_TEXTURE_START_X = 176;
	public static final int SLOT_TEXTURE_START_Y = 20;
	
	public static final int SLOT_ICON_WIDTH = 16;
	public static final int SLOT_ICON_HEIGHT = 16;
	protected void renderSlotIcons(GuiGraphics gui, float p_282737_, int p_281678_, int p_281465_) {
		for (int i=0; i<BioMechStationMenu.mechPartsBySlot.length; ++i) {
			Slot slot = this.getMenu().getSlotForMechPart(BioMechStationMenu.mechPartsBySlot[i]);
			if (slot != null && !slot.hasItem()) {
				gui.blit(GUI_LOCATION, this.leftPos + BioMechStationMenu.xCoordinatesBySlot[i], this.topPos + BioMechStationMenu.yCoordinatesBySlot[i], SLOT_TEXTURE_START_X, SLOT_TEXTURE_START_Y + SLOT_ICON_HEIGHT*i, SLOT_ICON_WIDTH, SLOT_ICON_HEIGHT);
			}
		}
	}
	
	public static int TEX_DARK_X = 4;
	public static int TEX_DARK_Y = 170;
	public void render(GuiGraphics gui, int p_282102_, int p_282423_, float p_282621_) {
		this.renderBackground(gui);
		super.render(gui, p_282102_, p_282423_, p_282621_);
		this.renderSlotIcons(gui, p_282621_, p_282102_, p_282423_);
		this.renderTooltip(gui, p_282102_, p_282423_);

		for (int i=0; i<visibilityMatrix.length; ++i) {
			ScreenRectangle rect = this.children().get(i).getRectangle();
			if (!visibilityMatrix[i]) {
				//cover the eye with a gray blotch when the BioMech part is hidden
				gui.blit(GUI_LOCATION, rect.left() + 6, rect.top() + 3, TEX_DARK_X, TEX_DARK_Y, 4, 4);
			}
		}
		
		this.xMouse = (float) p_282102_;
		this.yMouse = (float) p_282423_;
		
		//update left-arm tooltip to show the correct hotkey
		Slot leftArmSlot = this.getMenu().getSlotForMechPart(MechPart.LeftArm);
		int tooltipMargin = 3;
		int xStart = -tooltipMargin + this.leftPos + leftArmSlot.x;
		int yStart = -tooltipMargin + this.topPos + leftArmSlot.y;
		boolean xMatch = this.xMouse >= xStart && this.xMouse <= (xStart + SLOT_ICON_WIDTH + tooltipMargin);
		boolean yMatch = this.yMouse >= yStart && this.yMouse <= (yStart + SLOT_ICON_HEIGHT + tooltipMargin);
		if (xMatch && yMatch) {
			ArmorBase.mousingOverLeftArm = true;
		} else {
			ArmorBase.mousingOverLeftArm = false;
		}
	}

	protected void renderBg(GuiGraphics p_281616_, float p_282737_, int p_281678_, int p_281465_) {
		int i = (this.width - this.imageWidth) / 2;
		int j = (this.height - this.imageHeight) / 2;
		p_281616_.blit(GUI_LOCATION, i, j, 0, 0, this.imageWidth, this.imageHeight);
		InventoryScreen.renderEntityInInventoryFollowsMouse(p_281616_, i + 144, j + 75, 30,
				(float) (i + 144) - this.xMouse, (float) (j + 75 - 50) - this.yMouse, this.minecraft.player);
	}
}