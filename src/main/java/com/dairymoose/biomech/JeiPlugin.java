package com.dairymoose.biomech;

import java.util.List;
import java.util.Optional;

import com.dairymoose.biomech.menu.PortableStorageUnitMenu;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.constants.RecipeTypes;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.transfer.IRecipeTransferInfo;
import mezz.jei.api.registration.IRecipeTransferRegistration;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.Slot;

@mezz.jei.api.JeiPlugin
public class JeiPlugin implements IModPlugin {

	@Override
	public ResourceLocation getPluginUid() {
		return ResourceLocation.fromNamespaceAndPath(BioMech.MODID, "jei");
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void registerRecipeTransferHandlers(IRecipeTransferRegistration registration) {
		IModPlugin.super.registerRecipeTransferHandlers(registration);
		BioMech.LOGGER.debug("Register JEI handler");
		registration.addRecipeTransferHandler(new IRecipeTransferInfo<PortableStorageUnitMenu, Object>() {
			@Override
			public boolean canHandle(PortableStorageUnitMenu menu, Object arg1) {
				return true;
			}

			@Override
			public Class getContainerClass() {
				return PortableStorageUnitMenu.class;
			}

			@Override
			public List<Slot> getInventorySlots(PortableStorageUnitMenu menu, Object arg1) {
				return menu.getInventorySlots();
			}

			@Override
			public Optional getMenuType() {
				return Optional.of(BioMechRegistry.MENU_TYPE_PORTABLE_STORAGE_UNIT.get());
			}

			@Override
			public List<Slot> getRecipeSlots(PortableStorageUnitMenu menu, Object arg1) {
				return menu.getCraftingSlots();
			}

			@Override
			public RecipeType getRecipeType() {
				return RecipeTypes.CRAFTING;
			}});
	}
	
}
