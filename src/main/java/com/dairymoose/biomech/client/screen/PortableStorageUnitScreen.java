package com.dairymoose.biomech.client.screen;

import com.dairymoose.biomech.BioMech;
import com.dairymoose.biomech.BioMechPlayerData;
import com.dairymoose.biomech.menu.PortableStorageUnitMenu;
import com.mojang.blaze3d.platform.InputConstants;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class PortableStorageUnitScreen extends AbstractContainerScreen<PortableStorageUnitMenu> {
	private static final ResourceLocation GUI_LOCATION = ResourceLocation.fromNamespaceAndPath(BioMech.MODID,
			"textures/gui/storage_unit_" + BioMechPlayerData.PORTABLE_STORAGE_UNIT_CAPACITY_NO_CRAFTER + (BioMechPlayerData.storageUnitHasCraftingTable ? "c" : "") + ".png");

	@Override
	protected void init() {
		super.init();

	}

	public PortableStorageUnitScreen(PortableStorageUnitMenu p_98798_, Inventory p_98799_, Component p_98800_) {
		super(p_98798_, p_98799_, Component.empty());
		this.imageHeight = 220;
		if (BioMechPlayerData.storageUnitHasCraftingTable) {
			this.imageHeight += 23;
		}
		this.inventoryLabelX = 999;
		this.inventoryLabelY = this.imageHeight - 94;
	}
	
	public static int exitTick = 0;
	@Override
	public boolean keyPressed(int key, int p_97766_, int p_97767_) {
		InputConstants.Key pressed = InputConstants.getKey(key, p_97766_);
		if (super.keyPressed(key, p_97766_, p_97767_)) {
			return true;
		}
		else if (key == BioMech.ClientModEvents.HOTKEY_ACTIVATE_BACK_ITEM.getKey().getValue()) {
			this.onClose();
			return true;
		}
		return false;
	}

	@Override
	public void onClose() {
		exitTick = Minecraft.getInstance().player.tickCount;
		super.onClose();
	}
	
	public void render(GuiGraphics gui, int p_282102_, int p_282423_, float p_282621_) {
		this.renderBackground(gui);
		super.render(gui, p_282102_, p_282423_, p_282621_);
		this.renderTooltip(gui, p_282102_, p_282423_);
	}

	protected void renderBg(GuiGraphics p_281616_, float p_282737_, int p_281678_, int p_281465_) {
		int i = (this.width - this.imageWidth) / 2;
		int j = (this.height - this.imageHeight) / 2;
		p_281616_.blit(GUI_LOCATION, i, j, 0, 0, this.imageWidth, this.imageHeight);
	}
}