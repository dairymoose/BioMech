package com.dairymoose.biomech.client.screen;

import com.dairymoose.biomech.BioMech;
import com.dairymoose.biomech.menu.BioMechArmorMenu;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class BioMechArmorScreen extends AbstractContainerScreen<BioMechArmorMenu> {
   private static final ResourceLocation HOPPER_LOCATION = ResourceLocation.fromNamespaceAndPath(BioMech.MODID, "textures/gui/biomech_station.png");
   private float xMouse;
   private float yMouse;
   
   public BioMechArmorScreen(BioMechArmorMenu p_98798_, Inventory p_98799_, Component p_98800_) {
      super(p_98798_, p_98799_, Component.empty());
      this.imageHeight = 166;
      this.inventoryLabelY = this.imageHeight - 94;
   }

   public void render(GuiGraphics p_282918_, int p_282102_, int p_282423_, float p_282621_) {
      this.renderBackground(p_282918_);
      super.render(p_282918_, p_282102_, p_282423_, p_282621_);
      this.renderTooltip(p_282918_, p_282102_, p_282423_);
      
      this.xMouse = (float)p_282102_;
      this.yMouse = (float)p_282423_;
   }

   protected void renderBg(GuiGraphics p_281616_, float p_282737_, int p_281678_, int p_281465_) {
      int i = (this.width - this.imageWidth) / 2;
      int j = (this.height - this.imageHeight) / 2;
      p_281616_.blit(HOPPER_LOCATION, i, j, 0, 0, this.imageWidth, this.imageHeight);
      InventoryScreen.renderEntityInInventoryFollowsMouse(p_281616_, i + 144, j + 75, 30, (float)(i + 144) - this.xMouse, (float)(j + 75 - 50) - this.yMouse, this.minecraft.player);
   }
}