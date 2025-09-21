package com.dairymoose.biomech.config;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.dairymoose.biomech.BioMech;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.crafting.conditions.ICondition;

public class CraftingFlagCondition implements ICondition {

	private static final Logger LOGGER = LogManager.getLogger();
	
	String crafting_flag;
	
	CraftingFlagCondition(String crafting_flag) {
		this.crafting_flag = crafting_flag;
	}
	
	@Override
	public ResourceLocation getID() {
		return new ResourceLocation("biomech", "crafting_flag");
	}

	@Override
	public boolean test(IContext paramIContext) {
		boolean result = BioMechCraftingFlags.getFlag(crafting_flag);
		if (!result)
			BioMech.LOGGER.debug(crafting_flag + ": disabled");
		return result;
	}

}
