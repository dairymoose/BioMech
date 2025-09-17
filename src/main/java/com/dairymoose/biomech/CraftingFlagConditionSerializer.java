package com.dairymoose.biomech;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.crafting.conditions.IConditionSerializer;

public class CraftingFlagConditionSerializer implements IConditionSerializer<CraftingFlagCondition> {

	private static final Logger LOGGER = LogManager.getLogger();
	
	@Override
	public void write(JsonObject json, CraftingFlagCondition condition) {
		json.addProperty("crafting_flag", condition.crafting_flag);
	}

	@Override
	public CraftingFlagCondition read(JsonObject json) {
		JsonPrimitive primitive = json.getAsJsonPrimitive("crafting_flag");
		if (primitive == null)
			return new CraftingFlagCondition("");
		return new CraftingFlagCondition(primitive.getAsString());
	}

	@Override
	public ResourceLocation getID() {
		return new ResourceLocation("biomech", "crafting_flag");
	}

}
